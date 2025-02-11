package net.minidev.json.test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JSONObjectTest {

    @Test
    void mergeIntegerFailed() {
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

        // replace with new value by JSONObject merge will fail
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.appendField("k1", 101);
            jsonObject1.merge(jsonObject2);
        });

        Assertions.assertEquals(exception.getMessage(), "JSON merge can not merge two java.lang.Integer Object together");
    }

    @Test
    void mergeStringFailed() {
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

        // replace with new value by JSONObject merge will fail
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.appendField("k1", "vNew2");
            jsonObject1.merge(jsonObject2);
            System.out.println(jsonObject1.toJSONString());
        });

        Assertions.assertEquals(exception.getMessage(), "JSON merge can not merge two java.lang.String Object together");
    }

    @Test
    void mergeJsonObjectFailed() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", "v1");
        Assertions.assertEquals("{\"k1\":\"v1\"}", jsonObject1.toJSONString());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.appendField("k2", jsonObject1);
        Assertions.assertEquals("{\"k2\":{\"k1\":\"v1\"}}", jsonObject2.toJSONString());

        // replace with new value by JSONObject merge will fail
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k1", "vNew1");

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.appendField("k2", jsonObject3);
        Assertions.assertEquals("{\"k2\":{\"k1\":\"vNew1\"}}", jsonObject4.toJSONString());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jsonObject4.merge(jsonObject2);
        });

        Assertions.assertEquals(exception.getMessage(), "JSON merge can not merge two java.lang.String Object together");
    }

    @Test
    void mergeJsonArraySuccess() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", "v1");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.appendField("k2", "v2");

        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.add(jsonObject1);
        jsonArray1.add(jsonObject2);
        Assertions.assertEquals("[{\"k1\":\"v1\"},{\"k2\":\"v2\"}]", jsonArray1.toJSONString());

        // replace with new value by JSONObject merge will fail
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k1", "vNew1");
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.appendField("k2", "vNew2");

        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.add(jsonObject3);
        jsonArray2.add(jsonObject4);
        Assertions.assertEquals("[{\"k1\":\"vNew1\"},{\"k2\":\"vNew2\"}]", jsonArray2.toJSONString());

        jsonArray2.merge(jsonArray1);
        Assertions.assertEquals("[{\"k1\":\"vNew1\"},{\"k2\":\"vNew2\"},{\"k1\":\"v1\"},{\"k2\":\"v2\"}]", jsonArray2.toJSONString());
    }

    @Test
    void mergeIntegerWithOverwriteSuccess() {
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
    void mergeStringWithOverwriteSuccess() {
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

    @Test
    void mergeJsonObjectWithOverwriteSuccess() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.appendField("k1", "v1");
        Assertions.assertEquals("{\"k1\":\"v1\"}", jsonObject1.toJSONString());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.appendField("k2", jsonObject1);
        Assertions.assertEquals("{\"k2\":{\"k1\":\"v1\"}}", jsonObject2.toJSONString());

        // JSONObject merge will overwrite jsonObject3 by jsonObject2
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.appendField("k1", "vNew1");

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.appendField("k2", jsonObject3);
        Assertions.assertEquals("{\"k2\":{\"k1\":\"vNew1\"}}", jsonObject4.toJSONString());

        jsonObject4.merge(jsonObject2, true);
        Assertions.assertEquals("{\"k2\":{\"k1\":\"v1\"}}", jsonObject4.toJSONString());
    }
}