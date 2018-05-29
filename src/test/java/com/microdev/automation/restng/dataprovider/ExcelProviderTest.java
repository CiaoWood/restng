package com.microdev.automation.restng.dataprovider;

import jxl.read.biff.BiffException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ExcelProviderTest {

    String path = "xlsdptest";
    String sheet = "sheet1";

    @Test
    public void all_rows_returned_when_no_rows_specified() throws IOException, BiffException {
        ExcelProvider ep = new ExcelProvider(path, "sheet1");
        Object[][] data = ep.getExcelData();
        assertThat(data.length, is(equalTo(9)));
        for (int i = 0; i < 9; i++) {
            assertThat(data[i].length, is(equalTo(1)));
            assertThat(data[i][0].getClass(), typeCompatibleWith(Map.class));
            assertThat(((Map) data[i][0]).get("id"), is(equalTo("" + (i + 1))));
        }
    }

    @Test
    public void only_specified_rows_returned() throws IOException, BiffException {
        ExcelProvider ep = new ExcelProvider(path, "sheet1", "2,1-2,4-4,6-8");
        Object[][] data = ep.getExcelData();
        assertThat(data.length, is(equalTo(6)));
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        ids.add(4);
        ids.add(6);
        ids.add(7);
        ids.add(8);
        int i = 0;
        for (Integer id : ids) {
            assertThat(data[i].length, is(equalTo(1)));
            assertThat(data[i][0].getClass(), typeCompatibleWith(Map.class));
            assertThat(((Map) data[i][0]).get("id"), is(equalTo("" + (id))));
            i++;
        }
    }

    @Test
    public void testTagData() {
        System.setProperty("service.tag", "demo");
        String result = DataProviderFactory.getFilePath("ownership", ".xls");
        Assert.assertEquals(result, "demo/ownership");
    }

}
