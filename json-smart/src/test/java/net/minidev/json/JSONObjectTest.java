package net.minidev.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JSONObjectTest {

    @Test
    void mergeInteger() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", 1);
        Assertions.assertEquals("{\"k1\":1}", jsonObject1.toJSONString());

        // replace with new value by Map merge
        jsonObject1.merge("k1", 11, (oldValue, newValue) -> newValue);
        Assertions.assertEquals("{\"k1\":11}", jsonObject1.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k2", 2);
        jsonObject1.merge(jsonObject3);
        Assertions.assertEquals("{\"k1\":11,\"k2\":2}", jsonObject1.toJSONString());

        // replace with new value by JSONObject merge will failed
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.appendField("k1", 101);
            jsonObject1.merge(jsonObject2);
        });

        Assertions.assertEquals(exception.getMessage(), "JSON merge can not merge two java.lang.Integer Object together");
    }

    @Test
    void mergeString() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", "v1");
        Assertions.assertEquals("{\"k1\":\"v1\"}", jsonObject1.toJSONString());

        // replace with new value by Map merge
        jsonObject1.merge("k1", "vNew1", (oldValue, newValue) -> newValue);
        Assertions.assertEquals("{\"k1\":\"vNew1\"}", jsonObject1.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k2", "v2");
        jsonObject1.merge(jsonObject3);
        Assertions.assertEquals("{\"k1\":\"vNew1\",\"k2\":\"v2\"}", jsonObject1.toJSONString());

        // replace with new value by JSONObject merge will failed
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.appendField("k1", "vNew2");
            jsonObject1.merge(jsonObject2);
            System.out.println(jsonObject1.toJSONString());
        });

        Assertions.assertEquals(exception.getMessage(), "JSON merge can not merge two java.lang.String Object together");
    }

    @Test
    void mergeIntegerWithOverride() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", 1);
        Assertions.assertEquals("{\"k1\":1}", jsonObject1.toJSONString());

        // replace with new value by Map merge
        jsonObject1.merge("k1", 11, (oldValue, newValue) -> newValue);
        Assertions.assertEquals("{\"k1\":11}", jsonObject1.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k2", 2);
        jsonObject1.merge(jsonObject3);
        Assertions.assertEquals("{\"k1\":11,\"k2\":2}", jsonObject1.toJSONString());

        // replace with new value by JSONObject merge with override success
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.appendField("k1", 101);
        jsonObject1.merge(jsonObject2, true);
        Assertions.assertEquals("{\"k1\":101,\"k2\":2}", jsonObject1.toJSONString());
    }

    @Test
    void mergeStringWithOverride() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", "v1");
        Assertions.assertEquals("{\"k1\":\"v1\"}", jsonObject1.toJSONString());

        // replace with new value by Map merge
        jsonObject1.merge("k1", "vNew1", (oldValue, newValue) -> newValue);
        Assertions.assertEquals("{\"k1\":\"vNew1\"}", jsonObject1.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k2", "v2");
        jsonObject1.merge(jsonObject3);
        Assertions.assertEquals("{\"k1\":\"vNew1\",\"k2\":\"v2\"}", jsonObject1.toJSONString());

        // replace with new value by JSONObject merge with override success
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.appendField("k1", "vNew2");
        jsonObject1.merge(jsonObject2, true);
        Assertions.assertEquals("{\"k1\":\"vNew2\",\"k2\":\"v2\"}", jsonObject1.toJSONString());
    }
}