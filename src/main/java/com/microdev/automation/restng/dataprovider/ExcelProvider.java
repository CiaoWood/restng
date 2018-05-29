package com.microdev.automation.restng.dataprovider;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.util.basic.FileUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by wuchao on 17/6/15.
 */
public class ExcelProvider {

    private final Logger logger = LoggerFactory.getLogger(ExcelProvider.class);

    int rows;
    int columns;
    String sourceFile;
    private String fileName;
    private String caseName;
    private ArrayList<String> arrkey = new ArrayList<>();
    private String rowsPattern;
    private RangeSet<Integer> ranges = TreeRangeSet.create();

    /**
     * @param fileName excel文件名
     * @param caseName sheet名
     */
    public ExcelProvider(String fileName, String caseName) {
        super();
        this.fileName = fileName;
        this.caseName = caseName;
    }

    /**
     * @param fileName    excel文件名
     * @param caseName    sheet名
     * @param rowsPattern 感兴趣的行模式，支持格式如："1,2,4-6"
     */
    public ExcelProvider(String fileName, String caseName, String rowsPattern) {
        super();
        this.fileName = fileName;
        this.caseName = caseName;
        this.rowsPattern = rowsPattern;
        for (String s : this.rowsPattern.split(",")) {
            if (s.matches("[0-9]+")) {
                ranges.add(Range.closed(Integer.parseInt(s), Integer.parseInt(s)));
            } else if (s.matches("[0-9]+-[0-9]+")) {
                String[] tuple = s.split("-");
                ranges.add(Range.closed(Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1])));
            } else {
                //ignore
            }
        }
    }

    /**
     * 获得excel表中的数据
     */
    public Object[][] getExcelData() throws IOException, BiffException {
        Workbook workbook;
        Sheet sheet;
        sourceFile = FileUtils.getPath(this.fileName);
        workbook = getWorkbook(sourceFile);
        sheet = workbook.getSheet(caseName);
        checkNotNull(sheet, String.format("can't find sheet name %s", caseName));
        rows = sheet.getRows();
        columns = sheet.getColumns();
        // 为了返回值是Object[][],定义一个多行单列的二维数组
        List<HashMap<String, String>> arr = new ArrayList();
        // 对数组中所有元素hashmap进行初始化
        checkState(rows > 1, "excel中没有数据");

        // 获得首行的列名，作为hashmap的key值
        for (int c = 0; c < columns; c++) {
            String cellvalue = sheet.getCell(c, 0).getContents();
            if (StringUtils.isNotBlank(cellvalue)) {
                arrkey.add(cellvalue);
            }
        }
        // 遍历所有的单元格的值添加到hashmap中
        for (int r = 1; r < rows; r++) {
            if ((!ranges.isEmpty() && !ranges.contains(r)) ||
                    StringUtils.isBlank(sheet.getCell(0, r).getContents())) {
                continue;
            }
            HashMap pair = new HashMap();
            for (int c = 0; c < arrkey.size(); c++) {
                String cellvalue = sheet.getCell(c, r).getContents();
                String key = arrkey.get(c);
                if (StringUtils.isNotBlank(key)) {
                    pair.put(key, cellvalue);
                }
            }
            if (pair.containsKey(Constance.TAG) && StringUtils.isNotBlank(Property.get(Constance.TAG)) && !Property.get(Constance.TAG).equals(pair.get(Constance.TAG))) {
                continue; // 若表格里有tag,启动参数有tag,而对应tag不一致的话,跳过
            }
            arr.add(pair);
        }
        Object[][] objects = new Object[arr.size()][1];
        for (int i = 0, len = arr.size(); i < len; i++) {
            objects[i][0] = arr.get(i);
        }
        return objects;
    }

    private Workbook getWorkbook(String sourceFile) throws IOException, BiffException {
        try {
            return Workbook.getWorkbook(new File(sourceFile));
        } catch (IOException ex) {
            logger.error("找不到文件{}", sourceFile);
            throw ex;
        } catch (BiffException ex) {
            logger.error("excel文件读取错误{}", ex);
            throw ex;
        }
    }

}
