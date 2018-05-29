package com.microdev.automation.restng.validation;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import uk.co.datumedge.hamcrest.json.SameJSONAs;

import java.util.HashMap;
import java.util.Map;

import static com.microdev.automation.restng.util.assertion.AssertUtil.assertEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

/**
 * Created by wuchao on 17/7/5.
 */
public class JsonValidator {

    private JsonValidator() {
    }

    public static SameJSONAs containJson(String json) {
        return sameJSONAs(json).allowingExtraUnexpectedFields().allowingAnyArrayOrdering();
    }

    public static Matcher<HashMap> containJson(Map expected) {
        return new BaseMatcher<HashMap>() {
            @Override
            public boolean matches(final Object target) {
                final String targetString = JSON.toJSONString(target);
                final String expectedString = JSON.toJSONString(expected);
                try {
                    assertEqual(targetString, expectedString);
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("compare fail");
            }
        };
    }

    public static Matcher<Float> is(String expected) {
        return new BaseMatcher<Float>() {
            @Override
            public boolean matches(final Object target) {
                String targetTemp = String.valueOf(target);
                targetTemp = targetTemp.replaceAll("[.]0+?$", "");//去掉多余的0
                try {
                    assertThat(targetTemp, equalTo(expected));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("compare fail");
            }
        };
    }

    /**
     * 测试数组里包含某个对象
     *
     * @param objectJsonString
     * @return
     */
    public static Matcher<Object> contain(String objectJsonString) {
        Preconditions.checkArgument(StringUtils.isNotBlank(objectJsonString), "contain的入参不能为空!");
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(final Object target) {
                String var1;
                if (target instanceof String) {
                    var1 = (String) target;
                } else if (target.getClass().isPrimitive()) {
                    var1 = String.valueOf(target);
                } else {
                    var1 = JSON.toJSONString(target);
                }
                return Validator.valid(var1, objectJsonString).result();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(objectJsonString);
            }

            @Override
            public void describeMismatch(final Object item, final
            Description description) {
                description.appendText("was").appendValue(item.toString());
            }
        };
    }

}
