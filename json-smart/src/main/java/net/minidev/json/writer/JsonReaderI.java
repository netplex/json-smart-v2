package net.minidev.json.writer;

/*
 *    Copyright 2011-2025 JSON-SMART authors
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
import java.io.IOException;
import java.lang.reflect.Type;
import net.minidev.json.parser.ParseException;

/**
 * Default datatype mapper use by Json-smart ton store data.
 *
 * @author uriel Chemouni
 * @param <T> result type
 */
public abstract class JsonReaderI<T> {
  public final JsonReader base;

  /**
   * Reader can be link to the JsonReader Base
   *
   * @param base parent reader
   */
  public JsonReaderI(JsonReader base) {
    this.base = base;
  }

  private static String ERR_MSG = "Invalid or non Implemented status";

  /**
   * called when json-smart parser meet an object key
   *
   * @param key key name
   * @return a JsonReaderI to handle the object parsing
   * @throws ParseException if parsing fails
   * @throws IOException if I/O error occurs
   */
  public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
    throw new RuntimeException(
        ERR_MSG + " startObject(String key) in " + this.getClass() + " key=" + key);
  }

  /**
   * called when json-smart parser start an array.
   *
   * @param key the destination key name, or null.
   * @return a JsonReaderI to handle the array parsing
   * @throws ParseException if parsing fails
   * @throws IOException if I/O error occurs
   */
  public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
    throw new RuntimeException(ERR_MSG + " startArray in " + this.getClass() + " key=" + key);
  }

  /**
   * called when json-smart done parsing a value
   *
   * @param current the current object being built
   * @param key the key for the value
   * @param value the parsed value
   * @throws ParseException if parsing fails
   * @throws IOException if I/O error occurs
   */
  public void setValue(Object current, String key, Object value)
      throws ParseException, IOException {
    throw new RuntimeException(ERR_MSG + " setValue in " + this.getClass() + " key=" + key);
  }

  /**
   * Gets a value from the current object
   *
   * @param current the current object
   * @param key the key to get the value for
   * @return the value associated with the key
   */
  public Object getValue(Object current, String key) {
    throw new RuntimeException(
        ERR_MSG + " getValue(Object current, String key) in " + this.getClass() + " key=" + key);
  }

  /**
   * Gets the type for the specified key
   *
   * @param key the key to get the type for
   * @return the Type associated with the key
   */
  public Type getType(String key) {
    throw new RuntimeException(
        ERR_MSG + " getType(String key) in " + this.getClass() + " key=" + key);
  }

  /**
   * add a value in an array json object.
   *
   * @param current the current array object
   * @param value the value to add
   * @throws ParseException if parsing fails
   * @throws IOException if I/O error occurs
   */
  public void addValue(Object current, Object value) throws ParseException, IOException {
    throw new RuntimeException(
        ERR_MSG + " addValue(Object current, Object value) in " + this.getClass());
  }

  /**
   * use to instantiate a new object that will be used as an object
   *
   * @return a new object instance
   */
  public Object createObject() {
    throw new RuntimeException(ERR_MSG + " createObject() in " + this.getClass());
  }

  /**
   * use to instantiate a new object that will be used as an array
   *
   * @return a new array instance
   */
  public Object createArray() {
    throw new RuntimeException(ERR_MSG + " createArray() in " + this.getClass());
  }

  /**
   * Allow a mapper to convert a temporary structure to the final data format.
   *
   * <p>example: convert an List&lt;Integer&gt; to an int[]
   *
   * @param current the current object to convert
   * @return the converted object
   */
  @SuppressWarnings("unchecked")
  public T convert(Object current) {
    return (T) current;
  }
}
