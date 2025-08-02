package net.minidev.json.test.parser;

import static net.minidev.json.parser.JSONParser.DEFAULT_PERMISSIVE_MODE;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.MultipleJsonParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultipleJsonParserTest {

  @Test
  public void testMultipleJsonsFromSingleJsonArraySuccess()
      throws ParseException, UnsupportedEncodingException {
    String json =
        "[{\"friends\":[{\"id\":0,\"name\":\"test1\"},{\"id\":1,\"name\":\"test2\"}]}] other data";
    MultipleJsonParser parser = new MultipleJsonParser(json, DEFAULT_PERMISSIVE_MODE);
    JSONArray root = (JSONArray) parser.parseNext();
    JSONObject rootObj = (JSONObject) root.get(0);
    JSONArray array = (JSONArray) rootObj.get("friends");
    for (int idx = 0; idx < array.size(); idx++) {
      JSONObject cap = (JSONObject) array.get(idx);
      String first = (String) cap.get("name");
      Assertions.assertEquals("test" + (idx + 1), first);
    }
  }

  @Test
  public void testMultipleJsonsFromMultipleJsonArraySuccess()
      throws ParseException, UnsupportedEncodingException {
    String json =
        "[{\"friends\":[{\"id\":0,\"name\":\"test1\"},{\"id\":1,\"name\":\"test2\"}]}] "
            + "[{\"friends\":[{\"id\":2,\"name\":\"test3\"},{\"id\":3,\"name\":\"test4\"}]}]";
    MultipleJsonParser parser = new MultipleJsonParser(json, DEFAULT_PERMISSIVE_MODE);

    // first
    JSONArray root = (JSONArray) parser.parseNext();
    JSONObject rootObj = (JSONObject) root.get(0);
    JSONArray array = (JSONArray) rootObj.get("friends");
    for (int idx = 0; idx < array.size(); idx++) {
      JSONObject cap = (JSONObject) array.get(idx);
      String first = (String) cap.get("name");
      Assertions.assertEquals("test" + (idx + 1), first);
    }

    // second
    JSONArray root2 = (JSONArray) parser.parseNext();
    JSONObject rootObj2 = (JSONObject) root2.get(0);
    JSONArray array2 = (JSONArray) rootObj2.get("friends");
    for (int idx = 0; idx < array2.size(); idx++) {
      JSONObject cap = (JSONObject) array2.get(idx);
      String first = (String) cap.get("name");
      Assertions.assertEquals("test" + (idx + 3), first);
    }
  }

  @Test
  public void testMultipleJsonsFromByteSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + "\n{tranid:\"1213\", \"user\":{\"name\":\"345\",\"addr\":\"4234 iu\"}}";
    MultipleJsonParser parser =
        new MultipleJsonParser(json.getBytes(StandardCharsets.UTF_8), DEFAULT_PERMISSIVE_MODE);

    JSONObject root1 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1212", root1.get("tranid"));

    Assertions.assertTrue(parser.hasNext());
    JSONObject root2 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1213", root2.get("tranid"));

    Assertions.assertFalse(parser.hasNext());
  }

  @Test
  public void testMultipleJsonsFromInputStreamSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + "\n{tranid:\"1213\", \"user\":{\"name\":\"345\",\"addr\":\"4234 iu\"}}";
    ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    MultipleJsonParser parser = new MultipleJsonParser(stream, DEFAULT_PERMISSIVE_MODE);

    JSONObject root1 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1212", root1.get("tranid"));

    Assertions.assertTrue(parser.hasNext());
    JSONObject root2 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1213", root2.get("tranid"));

    Assertions.assertFalse(parser.hasNext());
  }

  @Test
  public void testMultipleJsonsFromInputStreamReaderSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + "\n{tranid:\"1213\", \"user\":{\"name\":\"345\",\"addr\":\"4234 iu\"}}";
    StringReader reader = new StringReader(json);
    MultipleJsonParser parser = new MultipleJsonParser(reader, DEFAULT_PERMISSIVE_MODE);

    JSONObject root1 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1212", root1.get("tranid"));

    Assertions.assertTrue(parser.hasNext());
    JSONObject root2 = (JSONObject) parser.parseNext();
    Assertions.assertEquals("1213", root2.get("tranid"));

    Assertions.assertFalse(parser.hasNext());
  }

  @Test
  public void testMultipleJsonsFromStringSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + " {tranid:\"1213\", \"user\":{\"name\":\"343\",\"addr\":\"4233 iu\"}}"
            + "\r{tranid:\"1214\", \"user\":{\"name\":\"344\",\"addr\":\"4234 iu\"}}"
            + "\t{tranid:\"1215\", \"user\":{\"name\":\"345\",\"addr\":\"4235 iu\"}}"
            + "\n{tranid:\"1216\", \"user\":{\"name\":\"346\",\"addr\":\"4236 iu\"}}";
    MultipleJsonParser parser = new MultipleJsonParser(json, DEFAULT_PERMISSIVE_MODE);
    JSONObject root1 = (JSONObject) parser.parseNext(JSONValue.defaultReader.DEFAULT);
    Assertions.assertEquals("1212", root1.get("tranid"));

    int count = 3;
    while (parser.hasNext()) {
      JSONObject root2 = (JSONObject) parser.parseNext(JSONValue.defaultReader.DEFAULT);
      Assertions.assertEquals("121" + count, root2.get("tranid"));
      count++;
    }
    Assertions.assertEquals(7, count);
  }

  @Test
  public void testMultipleJsonsParseClassFromStringSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + " {tranid:\"1213\", \"user\":{\"name\":\"343\",\"addr\":\"4233 iu\"}}"
            + "\r{tranid:\"1214\", \"user\":{\"name\":\"344\",\"addr\":\"4234 iu\"}}"
            + "\t{tranid:\"1215\", \"user\":{\"name\":\"345\",\"addr\":\"4235 iu\"}}"
            + "\n{tranid:\"1216\", \"user\":{\"name\":\"346\",\"addr\":\"4236 iu\"}}";
    MultipleJsonParser parser = new MultipleJsonParser(json, DEFAULT_PERMISSIVE_MODE);
    JSONObject root1 = (JSONObject) parser.parseNext(JSONValue.defaultReader.DEFAULT);
    Assertions.assertEquals("1212", root1.get("tranid"));

    int count = 3;
    while (parser.hasNext()) {
      Transaction root2 = parser.parseNext(Transaction.class);
      Assertions.assertEquals("121" + count, root2.getTranid());
      Assertions.assertEquals("34" + count, root2.getUser().getName());
      count++;
    }
    Assertions.assertEquals(7, count);
  }

  @Test
  public void testMultipleJsonsFromInvalidStringFailed() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}} bbb "
            + "\n{tranid:\"1213\", \"user\":{\"name\":\"343\",\"addr\":\"4233 iu\"}} ";
    MultipleJsonParser parser = new MultipleJsonParser(json, DEFAULT_PERMISSIVE_MODE);
    JSONObject root1 = (JSONObject) parser.parseNext(JSONValue.defaultReader.DEFAULT);
    Assertions.assertEquals("1212", root1.get("tranid"));

    int count = 3;
    while (parser.hasNext()) {
      // return invalid input when parse failed
      Object root2 = parser.parseNext(JSONValue.defaultReader.DEFAULT);
      Assertions.assertEquals(
          "bbb \n" + "{tranid:\"1213\", \"user\":{\"name\":\"343\",\"addr\":\"4233 iu\"}}", root2);
      count++;
    }
    Assertions.assertEquals(4, count);
  }

  @Test
  public void testMultipleJsonsWithJsonParserBySubStringSuccess() throws Exception {
    String json =
        "{tranid:\"1212\", \"user\":{\"name\":\"123\",\"addr\":\"786 rt\"}}"
            + "\n{tranid:\"1213\", \"user\":{\"name\":\"345\",\"addr\":\"4234 iu\"}}";
    JSONParser parser = new JSONParser(DEFAULT_PERMISSIVE_MODE);
    JSONObject root1 = (JSONObject) parser.parse(json, JSONValue.defaultReader.DEFAULT);
    Assertions.assertEquals("1212", root1.get("tranid"));

    Field field = Class.forName("net.minidev.json.parser.JSONParserBase").getDeclaredField("pos");
    field.setAccessible(true);
    Field pStringField = JSONParser.class.getDeclaredField("pString");
    pStringField.setAccessible(true);
    Object parser2 = pStringField.get(parser);
    Integer ipos = (Integer) field.get(parser2);
    Assertions.assertEquals(54, ipos);

    JSONObject root2 =
        (JSONObject) parser.parse(json.substring(ipos), JSONValue.defaultReader.DEFAULT);
    Assertions.assertEquals("1213", root2.get("tranid"));
    ipos = (Integer) field.get(parser2);
    Assertions.assertEquals(56, ipos);
  }
}
