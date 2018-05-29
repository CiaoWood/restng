package com.microdev.automation.restng.dataprovider;

import com.microdev.automation.restng.util.basic.FileUtils;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvDataProvider {

    private final Logger logger = LoggerFactory.getLogger(CsvDataProvider.class);

    private String fileName;

    private RangeSet<Integer> includeRanges = TreeRangeSet.create();

    private RangeSet<Integer> skipRanges = TreeRangeSet.create();


    /**
     * @param fileName
     * @param rowsPattern 感兴趣的行模式，支持格式如："1,2,4-6"
     */
    public CsvDataProvider(String fileName, String rowsPattern, String skipPattern) {
        this.fileName = fileName;
        for (String s : rowsPattern.split(",")) {
            if (s.matches("[0-9]+")) {
                includeRanges.add(Range.closed(Integer.parseInt(s), Integer.parseInt(s)));
            } else if (s.matches("[0-9]+-[0-9]+")) {
                String[] tuple = s.split("-");
                includeRanges.add(Range.closed(Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1])));
            } else {
                //ignore
            }
        }
        if (includeRanges.isEmpty()) {
            includeRanges.add(Range.closed(1, 65535)); //03版excel最多这么多行(除去标题行)
        }
        for (String s : skipPattern.split(",")) {
            if (s.matches("[0-9]+")) {
                skipRanges.add(Range.closed(Integer.parseInt(s), Integer.parseInt(s)));
            } else if (s.matches("[0-9]+-[0-9]+")) {
                String[] tuple = s.split("-");
                skipRanges.add(Range.closed(Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1])));
            } else {
                //ignore
            }
        }
    }


    public Object[][] getData() throws IOException {
        Reader fileReader = null;
        CSVReader reader = null;
        Object[][] data;
        try {
            fileReader = new FileReader(FileUtils.getPath(this.fileName, ".csv"));
            reader = new CSVReader(fileReader);
            List<String[]> lines = reader.readAll();
            if (lines == null || lines.size() < 1) {
                data = new Object[0][0];
            } else {
                String[] headers = lines.remove(0);
                for (int i = lines.size(); i > 0; i--) {
                    if (skipRanges.contains(i) || !includeRanges.contains(i))
                        lines.remove(i - 1);
                }
                data = new Object[lines.size()][1];
                int count = 0;
                for (String[] line : lines) {
                    Map<String, String> map = new LinkedHashMap<>();
                    int len = headers.length < line.length ? headers.length : line.length;
                    for (int i = 0; i < len; i++) {
                        map.put(headers[i], line[i]);
                    }
                    data[count][0] = map;
                    count++;
                }
            }
        } finally {
            if (fileReader != null) fileReader.close();
            if (reader != null) reader.close();
        }
        return data;
    }
}
