package net.minidev.json.test.writer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import net.minidev.json.parser.ParseException;
import net.minidev.json.writer.FakeMapper;
import net.minidev.json.writer.JsonReaderI;
import org.junit.jupiter.api.Test;

/** Test for FakeMapper class */
public class FakeMapperTest {

  @Test
  public void testDefaultInstance() {
    assertNotNull(FakeMapper.DEFAULT);
    assertSame(FakeMapper.DEFAULT, FakeMapper.DEFAULT);
  }

  @Test
  public void testStartObject() throws ParseException, IOException {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    JsonReaderI<?> result = mapper.startObject("testKey");
    assertSame(mapper, result);
  }

  @Test
  public void testStartArray() throws ParseException, IOException {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    JsonReaderI<?> result = mapper.startArray("testKey");
    assertSame(mapper, result);
  }

  @Test
  public void testSetValue() throws ParseException, IOException {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    Object current = new Object();
    mapper.setValue(current, "key", "value");
  }

  @Test
  public void testAddValue() throws ParseException, IOException {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    Object current = new Object();
    mapper.addValue(current, "value");
  }

  @Test
  public void testCreateObject() {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    Object result = mapper.createObject();
    assertNull(result);
  }

  @Test
  public void testCreateArray() {
    JsonReaderI<Object> mapper = FakeMapper.DEFAULT;
    Object result = mapper.createArray();
    assertNull(result);
  }
}
