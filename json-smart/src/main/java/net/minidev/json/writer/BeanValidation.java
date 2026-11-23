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

import java.math.BigDecimal;
import java.math.BigInteger;
import net.minidev.asm.Accessor;
import net.minidev.json.parser.ParseException;

/** Structural policy for the default Bean reader, independent of parser input. */
final class BeanValidation {
  private BeanValidation() {}

  static void validate(JsonReaderI<?> mapper, String key, char structure, int position)
      throws ParseException {
    if (mapper.getClass() != BeansMapper.Bean.class
        || mapper.base == null
        || mapper.base.getClass() != JsonReader.class) return;
    BeansMapper.Bean<?> bean = (BeansMapper.Bean<?>) mapper;
    if (key == null) {
      if (structure == '[') reject(bean.clz, structure, position);
      return;
    }
    if (structure != '{' && structure != '[') return;
    Accessor property = bean.index.get(key);
    if (property == null || !isScalar(property.getType())) return;
    JsonReaderI<?> cached = bean.base.getCachedMapper(property.getGenericType());
    if (cached == null || cached.getClass() == BeansMapper.Bean.class)
      reject(property.getType(), structure, position);
  }

  private static boolean isScalar(Class<?> type) {
    return (type.isPrimitive() && type != void.class)
        || type == String.class
        || type == Boolean.class
        || type == Character.class
        || type == Byte.class
        || type == Short.class
        || type == Integer.class
        || type == Long.class
        || type == Float.class
        || type == Double.class
        || type == BigInteger.class
        || type == BigDecimal.class
        || type.isEnum();
  }

  private static void reject(Class<?> type, char structure, int position) throws ParseException {
    throw new ParseException(
        position,
        ParseException.ERROR_UNEXPECTED_TOKEN,
        "Can not bind " + (structure == '{' ? "object" : "array") + " to " + type.getName());
  }
}
