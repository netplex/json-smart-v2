package net.minidev.json.test.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

public class TestWriteFeatures {

  @Test
  public void testS1() throws Exception {
    Beans beans = new Beans();
    String s = JSONValue.toJSONString(beans, JSONStyle.MAX_COMPRESS);
    assertEquals("{}", s);
    s = JSONValue.toJSONString(beans, JSONStyle.NO_COMPRESS);
    if (s.startsWith("{\"b")) {
      assertEquals("{\"b\":null,\"a\":null}", s);
    } else {
      assertEquals("{\"a\":null,\"b\":null}", s);
    }
    beans.a = "a";
    s = JSONValue.toJSONString(beans, JSONStyle.MAX_COMPRESS);
    assertEquals("{a:a}", s);
  }

  public static class Beans {
    public String a;
    public String b;
  }
}
