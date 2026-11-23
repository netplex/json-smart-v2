package net.minidev.json.writer;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

class BeanValidationTest {
  public enum Choice {
    ONE
  }

  public static class Detail {
    public String label;
  }

  public static class Model {
    public String name;
    public boolean flag;
    public char character;
    public byte tiny;
    public short small;
    public int count;
    public long large;
    public float fraction;
    public double decimal;
    public Boolean boxedFlag;
    public Character boxedCharacter;
    public Byte boxedTiny;
    public Short boxedSmall;
    public Integer boxedCount;
    public Long boxedLarge;
    public Float boxedFraction;
    public Double boxedDecimal;
    public BigInteger integer;
    public BigDecimal precision;
    public Choice choice;
    public Detail detail;
    public int[] numbers;
    public List<Detail> items;
    public Map<String, Detail> entries;
    public Date date;
  }

  private final JsonReader readers = new JsonReader();

  private void validate(String key, char shape) throws ParseException {
    BeanValidation.validate(readers.getMapper(Model.class), key, shape, 17);
  }

  @Test
  void rejectsRootArrayWithCheckedLocation() {
    ParseException error = assertThrows(ParseException.class, () -> validate(null, '['));
    assertEquals(17, error.getPosition());
    assertEquals(ParseException.ERROR_UNEXPECTED_TOKEN, error.getErrorType());
  }

  @Test
  void rejectsUncachedScalarsWithoutCreatingMappers() throws Exception {
    for (Field field : Model.class.getFields()) {
      Class<?> type = field.getType();
      if (type == Detail.class
          || type == int[].class
          || type == List.class
          || type == Map.class
          || type == Date.class) continue;
      assertNull(readers.getCachedMapper(type));
      for (char shape : new char[] {'{', '['}) {
        assertThrows(ParseException.class, () -> validate(field.getName(), shape));
        assertNull(readers.getCachedMapper(type));
      }
    }
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  void rejectsExactDefaultBeanAlreadyInScalarCache() throws Exception {
    // A Bean built from ordinary metadata supplies a pure cache state without scalar reflection.
    JsonReaderI cached = new BeansMapper.Bean<Detail>(readers, Detail.class);
    readers.registerReader((Class) String.class, cached);
    assertSame(cached, readers.getCachedMapper(String.class));
    assertThrows(ParseException.class, () -> validate("name", '{'));
    assertThrows(ParseException.class, () -> validate("name", '['));
    assertSame(cached, readers.getCachedMapper(String.class));
  }

  @Test
  void acceptsRegisteredScalarReaderWithoutLookup() throws Exception {
    JsonReaderI<String> custom = new JsonReaderI<String>(readers) {};
    readers.registerReader(String.class, custom);
    validate("name", '{');
    validate("name", '[');
    assertSame(custom, readers.getCachedMapper(String.class));
  }

  @Test
  void leavesReaderOverridesAndBeanSubclassesInControl() throws Exception {
    JsonReader custom =
        new JsonReader() {
          @Override
          public <T> JsonReaderI<T> getMapper(Type type) {
            throw new AssertionError("Policy must not resolve a mapper");
          }

          @Override
          public <T> JsonReaderI<T> getMapper(Class<T> type) {
            throw new AssertionError("Policy must not resolve a mapper");
          }
        };
    JsonReaderI<Model> bean = new BeansMapper.Bean<Model>(custom, Model.class);
    BeanValidation.validate(bean, "name", '[', 17);
    BeanValidation.validate(bean, null, '[', 17);
    BeanValidation.validate(new BeansMapper.Bean<Model>(readers, Model.class) {}, "name", '[', 17);
    BeanValidation.validate(new BeansMapper.Bean<Model>(readers, Model.class) {}, null, '[', 17);
    BeanValidation.validate(readers.DEFAULT, null, '[', 17);
  }

  @Test
  void leavesNormalBeanAndContainerMetadataUntouched() throws Exception {
    validate(null, '{');
    validate("detail", '{');
    validate("numbers", '[');
    validate("items", '[');
    validate("entries", '{');
    validate("date", '[');
    assertNull(readers.getCachedMapper(Detail.class));
    assertNull(readers.getCachedMapper(List.class));
    assertNull(readers.getCachedMapper(Map.class));
    assertSame(BeansMapper.MAPPER_DATE, readers.getCachedMapper(Date.class));
  }
}
