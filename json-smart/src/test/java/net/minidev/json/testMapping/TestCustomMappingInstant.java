package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.Instant;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import net.minidev.json.writer.JsonReaderI;
import org.junit.jupiter.api.Test;

/**
 * Test JDK 8+ java.time.Instant
 *
 * <p>Serialize a custom class Sample 1
 *
 * @author uriel
 */
public class TestCustomMappingInstant {

  @Test
  public void test_dummy() throws IOException {
    @SuppressWarnings("unused")
    ParseException e = null;
    JSONValue.toJSONString(true, JSONStyle.MAX_COMPRESS);
  }

  public void test_instant() {
    JSONValue.registerWriter(
        java.time.Instant.class,
        new net.minidev.json.reader.JsonWriterI<java.time.Instant>() {
          @Override
          public void writeJSONString(
              java.time.Instant value, Appendable out, JSONStyle compression) throws IOException {
            if (value == null) out.append("null");
            else out.append(Long.toString(value.toEpochMilli()));
          }
        });

    JSONValue.registerReader(
        RegularClass.class,
        new JsonReaderI<RegularClass>(JSONValue.defaultReader) {
          @Override
          public void setValue(Object current, String key, Object value)
              throws ParseException, IOException {
            if (key.equals("instant")) {
              Instant inst = Instant.ofEpochMilli((((Number) value).longValue()));
              ((RegularClass) current).setInstant(inst);
            }
          }

          @Override
          public Object createObject() {
            return new RegularClass();
          }
        });
    Instant instant = Instant.now();
    RegularClass regularClass = new RegularClass();
    regularClass.setInstant(instant);
    String data = JSONValue.toJSONString(regularClass);
    RegularClass result = JSONValue.parse(data, RegularClass.class);
    assertEquals(result.getInstant(), instant);
  }

  public static class RegularClass {
    private java.time.Instant instant;

    public java.time.Instant getInstant() {
      return instant;
    }

    public void setInstant(java.time.Instant instant) {
      this.instant = instant;
    }
  }
}
