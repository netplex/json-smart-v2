package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

public class TestMapPublic2 {
  String s = "{\"data\":{\"a\":\"b\"}}";

  @Test
  public void testMapPublicInterface() throws Exception {
    T5 r = JSONValue.parse(s, T5.class);
    assertEquals(1, r.data.size());
  }

  @Test
  public void testMapPublicMapClass() throws Exception {
    T6 r = JSONValue.parse(s, T6.class);
    assertEquals(1, r.data.size());
  }

  String MultiTyepJson =
      "{\"name\":\"B\",\"age\":120,\"cost\":12000,\"flag\":3,\"valid\":true,\"f\":1.2,\"d\":1.5,\"l\":12345678912345}";

  public static class T5 {
    public Map<String, String> data;
  }

  public static class T6 {
    public TreeMap<String, String> data;
  }
}
