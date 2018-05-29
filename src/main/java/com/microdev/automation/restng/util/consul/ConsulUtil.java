package com.microdev.automation.restng.util.consul;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcabi.aspects.Cacheable;
import com.microdev.automation.restng.common.consul.ConsulNodeDto;
import com.microdev.automation.restng.util.log.LOGGER;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

/**
 * @author wuchao
 */
public class ConsulUtil {

    private final static String CONSUL_SERVICE_KEY_NODE = "Node";
    private final static String CONSUL_URL_HEALTH_SERVICE;
    private final static String CONSUL_URL_NODES;

    static {
        CONSUL_URL_HEALTH_SERVICE = "http://consulserver/v1/health/service/{service}";
        CONSUL_URL_NODES = "http://consulserver/v1/internal/ui/nodes?dc=stable";
    }

    private ConsulUtil() {
    }

    @Cacheable(lifetime = 30, unit = TimeUnit.SECONDS)
    public static String getIpByServicenameTag(String serviceName, String tag) {
        try {
            JSONArray nodes = given().pathParam("service", serviceName).queryParam("passing", true).queryParam("tag", tag).when().get(CONSUL_URL_HEALTH_SERVICE).thenReturn().as(JSONArray.class);
            if (null != nodes && !nodes.isEmpty()) {
                JSONObject node = new JSONObject((Map<String, Object>) nodes.get(0));
                if (null != node.getJSONObject(CONSUL_SERVICE_KEY_NODE)) {
                    return node.getJSONObject(CONSUL_SERVICE_KEY_NODE).getString("Address") + ":" + node.getJSONObject("Service").getString("Port");
                }
            }
        } catch (Exception ex) {
            LOGGER.error("获取服务地址失败：{}", ex);
        }
        LOGGER.warn("获取不到服务{}地址，请检查服务健康状态", serviceName);
        return null;
    }

    @Cacheable(lifetime = 60, unit = TimeUnit.SECONDS)
    public static ConsulNodeDto[] getAllNodes() {
        try {
            return get(CONSUL_URL_NODES).thenReturn().as(ConsulNodeDto[].class);
        } catch (Exception ex) {
            LOGGER.error("获取consul服务失败：{}", ex);
        }
        return null;
    }

    public static String getTagByServiceIp(String serviceName, String ip) {
        if (StringUtils.isNotBlank(serviceName) && StringUtils.isNotBlank(ip)) {
            ConsulNodeDto[] nodes = getAllNodes();
            if (ArrayUtils.isNotEmpty(nodes)) {
                for (ConsulNodeDto node : nodes) {
                    if (serviceName.equals(node.getService()) && ip.equals(node.getAddress())) {
                        return node.getTag();
                    }
                }
            }
        }
        return null;
    }


}
