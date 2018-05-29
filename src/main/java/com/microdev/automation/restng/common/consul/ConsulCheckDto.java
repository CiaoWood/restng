package com.microdev.automation.restng.common.consul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wuchao
 */
@JsonIgnoreProperties({"CheckID", "Name", "Notes", "Output", "ServiceID", "ServiceName", "CreateIndex", "ModifyIndex"})
public class ConsulCheckDto {
    @JsonProperty("Node")
    private String node;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Port")
    private int port;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
