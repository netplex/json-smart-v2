package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.UUID;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import net.minidev.json.reader.JsonWriterI;
import net.minidev.json.writer.JsonReaderI;
import org.junit.jupiter.api.Test;

public class TestUUID {
  @Test
  void testUUID() throws ParseException {
    JSONObject obj = new JSONObject();
    UUID uuid = new UUID(123, 456);
    JSONValue.registerWriter(
        UUID.class,
        new JsonWriterI<UUID>() {
          @Override
          public void writeJSONString(UUID value, Appendable out, JSONStyle compression)
              throws IOException {
            out.append(value.toString());
          }
        });

    JSONValue.registerReader(
        UUIDHolder.class,
        new JsonReaderI<UUIDHolder>(JSONValue.defaultReader) {
          @Override
          public void setValue(Object current, String key, Object value)
              throws ParseException, IOException {
            if ("v".equals(key)) {
              ((UUIDHolder) current).setV(UUID.fromString((String) value));
              return;
            }
            super.setValue(current, key, value);
          }

          @Override
          public Object createObject() {
            return new UUIDHolder();
          }
        });

    obj.put("v", uuid);
    String asText = obj.toJSONString();
    assertEquals("{\"v\":00000000-0000-007b-0000-0000000001c8}", asText);
    UUIDHolder rebuild = JSONValue.parseWithException(asText, UUIDHolder.class);
    assertEquals(uuid, rebuild.getV());
  }

  public static class UUIDHolder {
    private UUID v;

    public UUID getV() {
      return v;
    }

    public void setV(UUID uuid) {
      this.v = uuid;
    }
  }
}
