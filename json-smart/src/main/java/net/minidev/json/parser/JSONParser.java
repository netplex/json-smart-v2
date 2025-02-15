package net.minidev.json.parser;

/*
 *    Copyright 2011-2024 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReaderI;

public class JSONParser {
  /** allow simple quote as String quoting char */
  public static final int ACCEPT_SIMPLE_QUOTE = 1;

  /** allow non quoted test */
  public static final int ACCEPT_NON_QUOTE = 2;

  /** Parse NaN as Float.NaN */
  public static final int ACCEPT_NAN = 4;

  /** Ignore control char in input text. */
  public static final int IGNORE_CONTROL_CHAR = 8;

  /**
   * Use int datatype to store number when it's possible.
   *
   * @since 1.0.7
   */
  public static final int USE_INTEGER_STORAGE = 16;

  /**
   * Throws exception on excessive 0 leading in digits
   *
   * @since 1.0.7
   */
  public static final int ACCEPT_LEADING_ZERO = 32;

  /**
   * Throws exception on useless comma in object and array
   *
   * @since 1.0.8
   */
  public static final int ACCEPT_USELESS_COMMA = 64;

  /**
   * Allow Json-smart to use Double or BigDecimal to store floating point value
   *
   * <p>You may need to disable HI_PRECISION_FLOAT feature on 32bit to improve parsing performances.
   *
   * @since 1.0.9
   */
  public static final int USE_HI_PRECISION_FLOAT = 128;

  /**
   * If enabled json-smart will throws exception if datas are present after the end of the Json
   * data.
   *
   * @since 1.0.9-2
   */
  public static final int ACCEPT_TAILLING_DATA = 256;

  /**
   * smart mode, fastest parsing mode. accept lots of non standard json syntax
   *
   * @since 2.0.1
   */
  public static final int ACCEPT_TAILLING_SPACE = 512;

  /**
   * smart mode, fastest parsing mode. accept lots of non standard json syntax
   *
   * @since 2.2.2
   */
  public static final int REJECT_127_CHAR = 1024;

  /**
   * Use double if possible for big digits, if no precision lost is observed
   *
   * @since 2.4
   */
  public static final int BIG_DIGIT_UNRESTRICTED = 2048;

  /**
   * If limit the max depth of json size
   *
   * @since 2.5
   */
  public static final int LIMIT_JSON_DEPTH = 4096;

  /**
   * smart mode, fastest parsing mode. accept lots of non standard json syntax
   *
   * @since 1.0.6
   */
  public static final int MODE_PERMISSIVE = -1;

  /**
   * strict RFC4627 mode.
   *
   * <p>slower than PERMISSIVE MODE.
   *
   * @since 1.0.6
   */
  public static final int MODE_RFC4627 =
      USE_INTEGER_STORAGE | USE_HI_PRECISION_FLOAT | ACCEPT_TAILLING_SPACE | LIMIT_JSON_DEPTH;

  /**
   * Parse Object like json-simple
   *
   * <p>Best for an iso-bug json-simple API port.
   *
   * @since 1.0.7
   */
  public static final int MODE_JSON_SIMPLE =
      ACCEPT_USELESS_COMMA
          | USE_HI_PRECISION_FLOAT
          | ACCEPT_TAILLING_DATA
          | ACCEPT_TAILLING_SPACE
          | REJECT_127_CHAR
          | BIG_DIGIT_UNRESTRICTED
          | LIMIT_JSON_DEPTH;

  /**
   * Strictest parsing mode
   *
   * @since 2.0.1
   */
  public static final int MODE_STRICTEST =
      USE_INTEGER_STORAGE | USE_HI_PRECISION_FLOAT | REJECT_127_CHAR | LIMIT_JSON_DEPTH;

  /** Default json-smart processing mode */
  public static int DEFAULT_PERMISSIVE_MODE =
      (System.getProperty("JSON_SMART_SIMPLE") != null) ? MODE_JSON_SIMPLE : MODE_PERMISSIVE;

  /*
   * internal fields
   */
  private final int mode;

  private JSONParserInputStream pBinStream;
  private JSONParserByteArray pBytes;
  private JSONParserReader pStream;
  private JSONParserString pString;

  private JSONParserReader getPStream() {
    if (pStream == null) pStream = new JSONParserReader(mode);
    return pStream;
  }

  /**
   * cached constructor
   *
   * @return instance of JSONParserInputStream
   */
  private JSONParserInputStream getPBinStream() {
    if (pBinStream == null) pBinStream = new JSONParserInputStream(mode);
    return pBinStream;
  }

  /**
   * cached constructor
   *
   * @return instance of JSONParserString
   */
  private JSONParserString getPString() {
    if (pString == null) pString = new JSONParserString(mode);
    return pString;
  }

  /**
   * cached constructor
   *
   * @return instance of JSONParserByteArray
   */
  private JSONParserByteArray getPBytes() {
    if (pBytes == null) pBytes = new JSONParserByteArray(mode);
    return pBytes;
  }

  /**
   * @deprecated prefer usage of new JSONParser(JSONParser.MODE_*)
   */
  public JSONParser() {
    this.mode = DEFAULT_PERMISSIVE_MODE;
  }

  public JSONParser(int permissifMode) {
    this.mode = permissifMode;
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public Object parse(byte[] in) throws ParseException {
    return getPBytes().parse(in);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(byte[] in, JsonReaderI<T> mapper) throws ParseException {
    return getPBytes().parse(in, mapper);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(byte[] in, Class<T> mapTo) throws ParseException {
    return getPBytes().parse(in, JSONValue.defaultReader.getMapper(mapTo));
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   *
   * @throws UnsupportedEncodingException
   */
  public Object parse(InputStream in) throws ParseException, UnsupportedEncodingException {
    return getPBinStream().parse(in);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(InputStream in, JsonReaderI<T> mapper)
      throws ParseException, UnsupportedEncodingException {
    return getPBinStream().parse(in, mapper);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(InputStream in, Class<T> mapTo)
      throws ParseException, UnsupportedEncodingException {
    return getPBinStream().parse(in, JSONValue.defaultReader.getMapper(mapTo));
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public Object parse(Reader in) throws ParseException {
    return getPStream().parse(in);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(Reader in, JsonReaderI<T> mapper) throws ParseException {
    return getPStream().parse(in, mapper);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(Reader in, Class<T> mapTo) throws ParseException {
    return getPStream().parse(in, JSONValue.defaultReader.getMapper(mapTo));
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public Object parse(String in) throws ParseException {
    return getPString().parse(in);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(String in, JsonReaderI<T> mapper) throws ParseException {
    return getPString().parse(in, mapper);
  }

  /**
   * use to return Primitive Type, or String, Or JsonObject or JsonArray generated by a
   * ContainerFactory
   */
  public <T> T parse(String in, Class<T> mapTo) throws ParseException {
    return getPString().parse(in, JSONValue.defaultReader.getMapper(mapTo));
  }
}
