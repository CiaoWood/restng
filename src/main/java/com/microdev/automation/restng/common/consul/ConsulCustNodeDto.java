package com.microdev.automation.restng.common.consul;

/**
 * @author wuchao
 */
public class ConsulCustNodeDto {
    private String service;
    private String ip;
    private int port;
    private String tag;

    public ConsulCustNodeDto(String service, String address, int port, String tag) {
        this.service = service;
        this.ip = address;
        this.port = port;
        this.tag = tag;
    }
}
