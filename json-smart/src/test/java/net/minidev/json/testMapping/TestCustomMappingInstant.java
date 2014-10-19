package net.minidev.json.testMapping;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

/**
 * Test JDK 8+ java.time.Instant
 *
 * Serialize a custom class Sample 1
 * 
 * @author uriel
 *
 */
public class TestCustomMappingInstant extends TestCase {
	
	public void test_dummy() throws IOException {
		@SuppressWarnings("unused")
		ParseException e = null;
		
		JSONValue.toJSONString(true, JSONStyle.MAX_COMPRESS);
		Assert.assertEquals(true, true);
	}
	
// Need JDK 1.8
//	public void test_instant() {
//		JSONValue.registerWriter(java.time.Instant.class, new net.minidev.json.reader.JsonWriterI<java.time.Instant>() {
//			@Override
//			public void writeJSONString(java.time.Instant value, Appendable out, JSONStyle compression)
//					throws IOException {
//				if (value == null)
//					out.append("null");
//				else
//					out.append(Long.toString(value.toEpochMilli()));
//			}
//		});
// 
//		JSONValue.registerReader(RegularClass.class, new net.minidev.json.writer.JsonReaderI<RegularClass>(JSONValue.defaultReader) {
//			@Override
//			public void setValue(Object current, String key, Object value) throws ParseException, IOException {
//				if (key.equals("instant")) {
//					java.time.Instant inst = java.time.Instant.ofEpochMilli((((Number)value).longValue()));
//					((RegularClass)current).setInstant(inst);
//				}
//			}
//			@Override
//			public Object createObject() {
//				return new RegularClass();
//			}
//		});
//		java.time.Instant instant = java.time.Instant.now();
//		RegularClass regularClass = new RegularClass();
//		regularClass.setInstant(instant);
//		String data = JSONValue.toJSONString(regularClass);
//		RegularClass result = JSONValue.parse(data, RegularClass.class);
//		Assert.assertEquals(result.getInstant(), instant);
//	}
//
//	public static class RegularClass {
//		private java.time.Instant instant;
//		public java.time.Instant getInstant() {
//			return instant;
//		}
//		public void setInstant(java.time.Instant instant) {
//			this.instant = instant;
//		}
//	}
}
