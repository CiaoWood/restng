package com.microdev.automation.restng.dataprovider;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CsvDataProviderTest {

    @Test
    public void all_rows_default() throws IOException {
        CsvDataProvider cdp = new CsvDataProvider("csvdptest", "", "");
        Object[][] data = cdp.getData();
        assertEquals(data.length, 4);
        Map<String, String> map;
        //1,lucy
        assertEquals(data[0].length, 1);
        map = (Map) data[0][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "1");
        assertEquals(map.get("name"), "lucy");
        //2,
        assertEquals(data[1].length, 1);
        map = (Map) data[1][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "2");
        assertEquals(map.get("name"), "");
        //,marry
        assertEquals(data[2].length, 1);
        map = (Map) data[2][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "");
        assertEquals(map.get("name"), "marry");
        //david
        assertEquals(data[3].length, 1);
        map = (Map) data[3][0];
        assertEquals(map.size(), 1);
        assertEquals(map.get("id"), "david");
    }

    @Test
    public void specific_rows() throws IOException {
        CsvDataProvider cdp = new CsvDataProvider("csvdptest", "4,1-2", "");
        Object[][] data = cdp.getData();
        assertEquals(data.length, 3);
        Map<String, String> map;
        //1,lucy
        assertEquals(data[0].length, 1);
        map = (Map) data[0][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "1");
        assertEquals(map.get("name"), "lucy");
        //2,
        assertEquals(data[1].length, 1);
        map = (Map) data[1][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "2");
        assertEquals(map.get("name"), "");
        //david
        assertEquals(data[2].length, 1);
        map = (Map) data[2][0];
        assertEquals(map.size(), 1);
        assertEquals(map.get("id"), "david");
    }

    @Test
    public void relative_path() throws IOException {
        CsvDataProvider cdp = new CsvDataProvider("dir/csvdptest", "1-3", "");
        Object[][] data = cdp.getData();
        assertEquals(data.length, 3);
        Map<String, String> map;
        //1,lucy
        assertEquals(data[0].length, 1);
        map = (Map) data[0][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "1");
        assertEquals(map.get("name"), "lucy");
    }

    @Test
    public void skip_rows() throws IOException {
        CsvDataProvider cdp = new CsvDataProvider("csvdptest", "", "1,3-4");
        Object[][] data = cdp.getData();
        assertEquals(data.length, 1);
        Map<String, String> map;
        //2,
        assertEquals(data[0].length, 1);
        map = (Map) data[0][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "2");
        assertEquals(map.get("name"), "");
    }

    @Test
    public void skip_rows2() throws IOException {
        CsvDataProvider cdp = new CsvDataProvider("csvdptest", "1-3", "1,3-4");
        Object[][] data = cdp.getData();
        assertEquals(data.length, 1);
        Map<String, String> map;
        //2,
        assertEquals(data[0].length, 1);
        map = (Map) data[0][0];
        assertEquals(map.size(), 2);
        assertEquals(map.get("id"), "2");
        assertEquals(map.get("name"), "");
    }

}
