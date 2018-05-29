package com.microdev.automation.restng.common.consul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author wuchao
 */
@JsonIgnoreProperties({"EnableTagOverride", "CreateIndex", "ModifyIndex"})
public class ConsulServiceDto {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Service")
    private String service;
    @JsonProperty("Tags")
    private List<String> tags;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Port")
    private int port;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
