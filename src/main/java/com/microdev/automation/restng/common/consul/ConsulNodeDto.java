package com.microdev.automation.restng.common.consul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microdev.automation.restng.util.basic.ListUtil;

import java.util.List;

/**
 * @author wuchao
 */
@JsonIgnoreProperties({"ID", "TaggedAddresses", "Meta"})
public class ConsulNodeDto {
    @JsonProperty("Node")
    private String node;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Services")
    private List<ConsulServiceDto> services;
    @JsonProperty("Checks")
    private List<ConsulCheckDto> checks;

    public ConsulCustNodeDto toCustNode() {
        if (ListUtil.isNotEmpty(services)) {
            return new ConsulCustNodeDto(services.get(0).getService(), getAddress(), services.get(0).getPort(), services.get(0).getTags().get(0));
        } else {
            return null;
        }
    }

    public String getService() {
        if (ListUtil.isNotEmpty(services)) {
            return getServices().get(0).getService();
        } else {
            return null;
        }
    }

    public String getTag() {
        if (ListUtil.isNotEmpty(services) && ListUtil.isNotEmpty(services.get(0).getTags())) {
            return services.get(0).getTags().get(0);
        } else {
            return null;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ConsulServiceDto> getServices() {
        return services;
    }

    public void setServices(List<ConsulServiceDto> services) {
        this.services = services;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public List<ConsulCheckDto> getChecks() {
        return checks;
    }

    public void setChecks(List<ConsulCheckDto> checks) {
        this.checks = checks;
    }
}
