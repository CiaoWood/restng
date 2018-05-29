package com.microdev.automation.restng.keydrive.engine;

import java.util.Map;

/**
 * Created by wuchao on 17/7/18.
 */
public abstract class AbstractEngine {

    protected Map<String, String> data;

    protected AbstractEngine(Map<String, String> data) {
        this.data = data;
    }

    public abstract void run();

}
