package net.minidev.json.test.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import net.minidev.json.JSONStyle;
import net.minidev.json.reader.BeansWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BeansWriterTest {

  private BeansWriter beansWriter;

  @BeforeEach
  public void setUp() {
    beansWriter = new BeansWriter();
  }

  public static class SimpleBean {
    public String name;
    public int age;
    private String city;

    public SimpleBean() {}

    public SimpleBean(String name, int age, String city) {
      this.name = name;
      this.age = age;
      this.city = city;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }
  }

  public static class BooleanBean {
    private boolean active;
    private Boolean enabled;

    public BooleanBean(boolean active, Boolean enabled) {
      this.active = active;
      this.enabled = enabled;
    }

    public boolean isActive() {
      return active;
    }

    public Boolean getEnabled() {
      return enabled;
    }
  }

  public static class BeanWithStaticFields {
    public static final String CONSTANT = "test";
    public static int staticField = 42;
    public String normalField = "normal";
    private transient String transientField = "transient";
  }

  public static class BeanWithNullValues {
    public String name;
    public String nullField;

    public BeanWithNullValues(String name, String nullField) {
      this.name = name;
      this.nullField = nullField;
    }
  }

  public static class InheritedBean extends SimpleBean {
    public double salary;

    public InheritedBean(String name, int age, String city, double salary) {
      super(name, age, city);
      this.salary = salary;
    }
  }

  @Test
  public void testWriteSimpleBean() throws IOException {
    SimpleBean bean = new SimpleBean("John", 30, "New York");
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"name\":\"John\""));
    assertTrue(result.contains("\"age\":30"));
    assertTrue(result.contains("\"city\":\"New York\""));
  }

  @Test
  public void testWriteBooleanBean() throws IOException {
    BooleanBean bean = new BooleanBean(true, false);
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"active\":true"));
    assertTrue(result.contains("\"enabled\":false"));
  }

  @Test
  public void testWriteBeanWithStaticAndTransientFields() throws IOException {
    BeanWithStaticFields bean = new BeanWithStaticFields();
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"normalField\":\"normal\""));
    assertTrue(!result.contains("CONSTANT"));
    assertTrue(!result.contains("staticField"));
    assertTrue(!result.contains("transientField"));
  }

  @Test
  public void testWriteBeanWithNullValuesIgnoreNull() throws IOException {
    BeanWithNullValues bean = new BeanWithNullValues("Test", null);
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.MAX_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("name:Test"));
    assertTrue(!result.contains("nullField"));
  }

  @Test
  public void testWriteBeanWithNullValuesIncludeNull() throws IOException {
    BeanWithNullValues bean = new BeanWithNullValues("Test", null);
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"name\":\"Test\""));
    assertTrue(result.contains("\"nullField\":null"));
  }

  @Test
  public void testWriteInheritedBean() throws IOException {
    InheritedBean bean = new InheritedBean("Alice", 25, "Boston", 75000.0);
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"name\":\"Alice\""));
    assertTrue(result.contains("\"age\":25"));
    assertTrue(result.contains("\"city\":\"Boston\""));
    assertTrue(result.contains("\"salary\":75000.0"));
  }

  @Test
  public void testWriteEmptyBean() throws IOException {
    SimpleBean bean = new SimpleBean();
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.NO_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("\"name\":null"));
    assertTrue(result.contains("\"age\":0"));
    assertTrue(result.contains("\"city\":null"));
  }

  @Test
  public void testWriteCompressed() throws IOException {
    SimpleBean bean = new SimpleBean("John", 30, "New York");
    StringWriter writer = new StringWriter();

    beansWriter.writeJSONString(bean, writer, JSONStyle.MAX_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("name:John"));
    assertTrue(result.contains("age:30"));
    assertTrue(result.contains("city:New York"));
    assertTrue(!result.contains("\"name\""));
  }

  @Test
  public void testWriteNullObject() {
    assertThrows(RuntimeException.class, () -> {
      beansWriter.writeJSONString(null, new StringWriter(), JSONStyle.NO_COMPRESS);
    });
  }
}