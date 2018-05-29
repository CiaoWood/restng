package com.microdev.automation.restng.demo;

import com.microdev.automation.restng.RestNG;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 这里主要是准备mock接口
 */
public class DemoBase extends RestNG {

    protected String baseUri = "http://localhost:19999/";
    //宠物和主人对应关系
    protected Map<String, String> owners = new HashMap<String, String>();
    int port = 19999;
    MockWebServer server;
    Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch(RecordedRequest request) {
            switch (request.getPath()) {
                //宠物列表
                case "/pets":
                    return new MockResponse().setBody("[" +
                            "{\"species\":\"dog\", \"name\":\"dogA\",\"age\":3}," +
                            "{\"species\":\"dog\", \"name\":\"dogB\",\"age\":4}," +
                            "{\"species\":\"dog\", \"name\":\"dogC\",\"age\":5}" +
                            "]").addHeader("Content-Type", "application/json");
                // {pet}/owner 返回某只宠物的主人信息
                case "/dogA/owner":
                    return new MockResponse().setBody("{\"name\":\"Peter\"}").
                            addHeader("Content-Type", "application/json");
                case "/dogB/owner":
                    return new MockResponse().setBody("{\"name\":\"Marry\"}").
                            addHeader("Content-Type", "application/json");
                case "/dogC/owner":
                    return new MockResponse().setBody("{\"name\":\"Henry\"}").
                            addHeader("Content-Type", "application/json");
                default:
                    return new MockResponse().setResponseCode(404);
            }
        }
    };

    public DemoBase() {
        owners.put("dogA", "Peter");
        owners.put("dogB", "Marry");
        owners.put("dogC", "Henry");
    }

    @BeforeClass
    public void beforeClassInDemoBase() {
        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        try {
            server.start(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public void afterClassInDemoBase() {
        try {
            server.shutdown();
        } catch (IOException e) {
            //ignore
        }
    }

}
