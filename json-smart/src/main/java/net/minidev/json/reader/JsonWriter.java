package net.minidev.json.reader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minidev.asm.Accessor;
import net.minidev.asm.BeansAccess;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStreamAware;
import net.minidev.json.JSONStreamAwareEx;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONUtil;
import net.minidev.json.JSONValue;

public class JsonWriter {
	private ConcurrentHashMap<Class<?>, JsonWriterI<?>> data;
	private LinkedList<WriterByInterface> writerInterfaces;

	public JsonWriter() {
		data = new ConcurrentHashMap<Class<?>, JsonWriterI<?>>();
		writerInterfaces = new LinkedList<WriterByInterface>();
		init();
	}

	static class WriterByInterface {
		public Class<?> _interface;
		public JsonWriterI<?> _writer;

		public WriterByInterface(Class<?> _interface, JsonWriterI<?> _writer) {
			this._interface = _interface;
			this._writer = _writer;
		}
	}

	@SuppressWarnings("rawtypes")
	public JsonWriterI getWriterByInterface(Class<?> clazz) {
		for (WriterByInterface w : writerInterfaces) {
			if (w._interface.isAssignableFrom(clazz))
				return w._writer;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public JsonWriterI getWrite(Class cls) {
		return data.get(cls);
	}

	final static public JsonWriterI<JSONStreamAwareEx> JSONStreamAwareWriter = new JsonWriterI<JSONStreamAwareEx>() {
		public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression)
				throws IOException {
			value.writeJSONString(out);
		}
	};

	final static public JsonWriterI<JSONStreamAwareEx> JSONStreamAwareExWriter = new JsonWriterI<JSONStreamAwareEx>() {
		public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression)
				throws IOException {
			value.writeJSONString(out, compression);
		}
	};

	final static public JsonWriterI<JSONAwareEx> JSONJSONAwareExWriter = new JsonWriterI<JSONAwareEx>() {
		public <E extends JSONAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression)
				throws IOException {
			out.append(value.toJSONString(compression));
		}
	};

	final static public JsonWriterI<JSONAware> JSONJSONAwareWriter = new JsonWriterI<JSONAware>() {
		public <E extends JSONAware> void writeJSONString(E value, Appendable out, JSONStyle compression)
				throws IOException {
			out.append(value.toJSONString());
		}
	};

	final static public JsonWriterI<Iterable<? extends Object>> JSONIterableWriter = new JsonWriterI<Iterable<? extends Object>>() {
		public <E extends Iterable<? extends Object>> void writeJSONString(E list, Appendable out, JSONStyle compression)
				throws IOException {
			boolean first = true;
			compression.arrayStart(out);
			for (Object value : list) {
				if (first) {
					first = false;
					compression.arrayfirstObject(out);
				} else {
					compression.arrayNextElm(out);
				}
				if (value == null)
					out.append("null");
				else
					JSONValue.writeJSONString(value, out, compression);
				compression.arrayObjectEnd(out);
			}
			compression.arrayStop(out);
		}
	};

	final static public JsonWriterI<Enum<?>> EnumWriter = new JsonWriterI<Enum<?>>() {
		public <E extends Enum<?>> void writeJSONString(E value, Appendable out, JSONStyle compression)
				throws IOException {
			@SuppressWarnings("rawtypes")
			String s = ((Enum) value).name();
			compression.writeString(out, s);
		}
	};

	final static public JsonWriterI<Map<String, ? extends Object>> JSONMapWriter = new JsonWriterI<Map<String, ? extends Object>>() {
		public <E extends Map<String, ? extends Object>> void writeJSONString(E map, Appendable out,
				JSONStyle compression) throws IOException {
			boolean first = true;
			compression.objectStart(out);
			/**
			 * do not use <String, Object> to handle non String key maps
			 */
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				Object v = entry.getValue();
				if (v == null && compression.ignoreNull())
					continue;
				if (first) {
					compression.objectFirstStart(out);
					first = false;
				} else {
					compression.objectNext(out);
				}
				JsonWriter.writeJSONKV(entry.getKey().toString(), v, out, compression);
				// compression.objectElmStop(out);
			}
			compression.objectStop(out);
		}
	};

	/**
	 * Json-Smart V2 Beans serialiser
	 * 
	 * Based on ASM
	 */
	final static public JsonWriterI<Object> beansWriterASM = new JsonWriterI<Object>() {
		public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
			try {
				Class<?> cls = value.getClass();
				boolean needSep = false;
				@SuppressWarnings("rawtypes")
				BeansAccess fields = BeansAccess.get(cls, JSONUtil.JSON_SMART_FIELD_FILTER);
				out.append('{');
				for (Accessor field : fields.getAccessors()) {
					@SuppressWarnings("unchecked")
					Object v = fields.get(value, field.getIndex());
					if (v == null && compression.ignoreNull())
						continue;
					if (needSep)
						out.append(',');
					else
						needSep = true;
					JSONObject.writeJSONKV(field.getName(), v, out, compression);
				}
				out.append('}');
			} catch (IOException e) {
				throw e;
			}
		}
	};
	/**
	 * Json-Smart V1 Beans serialiser
	 */
	final static public JsonWriterI<Object> beansWriter = new JsonWriterI<Object>() {
		public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
			try {
				Class<?> nextClass = value.getClass();
				boolean needSep = false;
				compression.objectStart(out);
				while (nextClass != Object.class) {
					Field[] fields = nextClass.getDeclaredFields();
					for (Field field : fields) {
						int m = field.getModifiers();
						if ((m & (Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL)) > 0)
							continue;
						Object v = null;
						if ((m & Modifier.PUBLIC) > 0) {
							v = field.get(value);
						} else {
							String g = JSONUtil.getGetterName(field.getName());
							Method mtd = null;

							try {
								mtd = nextClass.getDeclaredMethod(g);
							} catch (Exception e) {
							}
							if (mtd == null) {
								Class<?> c2 = field.getType();
								if (c2 == Boolean.TYPE || c2 == Boolean.class) {
									g = JSONUtil.getIsName(field.getName());
									mtd = nextClass.getDeclaredMethod(g);
								}
							}
							if (mtd == null)
								continue;
							v = mtd.invoke(value);
						}
						if (v == null && compression.ignoreNull())
							continue;
						if (needSep)
							compression.objectNext(out);
						else
							needSep = true;
						JsonWriter.writeJSONKV(field.getName(), v, out, compression);
						// compression.objectElmStop(out);
					}
					nextClass = nextClass.getSuperclass();
				}
				compression.objectStop(out);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	};

	final static public JsonWriterI<Object> arrayWriter = new JsonWriterI<Object>() {
		public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
			compression.arrayStart(out);
			boolean needSep = false;
			for (Object o : ((Object[]) value)) {
				if (needSep)
					compression.objectNext(out);
				else
					needSep = true;
				JSONValue.writeJSONString(o, out, compression);
			}
			compression.arrayStop(out);
		}
	};

	public void init() {
		registerWriter(new JsonWriterI<String>() {
			public void writeJSONString(String value, Appendable out, JSONStyle compression) throws IOException {
				compression.writeString(out, (String) value);
			}
		}, String.class);

		registerWriter(new JsonWriterI<Boolean>() {
			public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
				out.append(value.toString());
			}
		}, Boolean.class);

		registerWriter(new JsonWriterI<Double>() {
			public void writeJSONString(Double value, Appendable out, JSONStyle compression) throws IOException {
				if (value.isInfinite())
					out.append("null");
				else
					out.append(value.toString());
			}
		}, Double.class);

		registerWriter(new JsonWriterI<Date>() {
			public void writeJSONString(Date value, Appendable out, JSONStyle compression) throws IOException {
				out.append('"');
				JSONValue.escape(value.toString(), out, compression);
				out.append('"');
			}
		}, Date.class);

		registerWriter(new JsonWriterI<Float>() {
			public void writeJSONString(Float value, Appendable out, JSONStyle compression) throws IOException {
				if (value.isInfinite())
					out.append("null");
				else
					out.append(value.toString());
			}
		}, Float.class);

		registerWriter(new JsonWriterI<Number>() {
			public void writeJSONString(Number value, Appendable out, JSONStyle compression) throws IOException {
				out.append(value.toString());
			}
		}, Integer.class, Long.class, Byte.class, Short.class, BigInteger.class);

		registerWriter(new JsonWriterI<Boolean>() {
			public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
				out.append(value.toString());
			}
		}, Boolean.class);

		registerWriter(new JsonWriterI<Boolean>() {
			public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
				out.append(value.toString());
			}
		}, Boolean.class);

		/**
		 * Array
		 */

		registerWriter(new JsonWriterI<int[]>() {
			public void writeJSONString(int[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (int b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Integer.toString(b));
				}
				compression.arrayStop(out);
			}
		}, int[].class);

		registerWriter(new JsonWriterI<short[]>() {
			public void writeJSONString(short[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (short b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Short.toString(b));
				}
				compression.arrayStop(out);
			}
		}, short[].class);

		registerWriter(new JsonWriterI<long[]>() {
			public void writeJSONString(long[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (long b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Long.toString(b));
				}
				compression.arrayStop(out);
			}
		}, long[].class);

		registerWriter(new JsonWriterI<float[]>() {
			public void writeJSONString(float[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (float b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Float.toString(b));
				}
				compression.arrayStop(out);
			}
		}, float[].class);

		registerWriter(new JsonWriterI<double[]>() {
			public void writeJSONString(double[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (double b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Double.toString(b));
				}
				compression.arrayStop(out);
			}
		}, double[].class);

		registerWriter(new JsonWriterI<boolean[]>() {
			public void writeJSONString(boolean[] value, Appendable out, JSONStyle compression) throws IOException {
				boolean needSep = false;
				compression.arrayStart(out);
				for (boolean b : value) {
					if (needSep)
						compression.objectNext(out);
					else
						needSep = true;
					out.append(Boolean.toString(b));
				}
				compression.arrayStop(out);
			}
		}, boolean[].class);
		
		addInterfaceWriterLast(JSONStreamAwareEx.class, JsonWriter.JSONStreamAwareExWriter);
		addInterfaceWriterLast(JSONStreamAware.class, JsonWriter.JSONStreamAwareWriter);
		addInterfaceWriterLast(JSONAwareEx.class, JsonWriter.JSONJSONAwareExWriter);
		addInterfaceWriterLast(JSONAware.class, JsonWriter.JSONJSONAwareWriter);
		addInterfaceWriterLast(Map.class, JsonWriter.JSONMapWriter);
		addInterfaceWriterLast(Iterable.class, JsonWriter.JSONIterableWriter);
		addInterfaceWriterLast(Enum.class, JsonWriter.EnumWriter);
	}

	public void addInterfaceWriterFirst(Class<?> cls, JsonWriterI<?> writer) {
		writerInterfaces.addFirst(new WriterByInterface(cls, writer));
	}

	public void addInterfaceWriterLast(Class<?> cls, JsonWriterI<?> writer) {
		writerInterfaces.addLast(new WriterByInterface(cls, writer));
	}
	
	public <T> void registerWriter(JsonWriterI<T> writer, Class<?>... cls) {
		for (Class<?> c : cls)
			data.put(c, writer);
	}

	/**
	 * Write a Key : value entry to a stream
	 */
	public static void writeJSONKV(String key, Object value, Appendable out, JSONStyle compression) throws IOException {
		if (key == null)
			out.append("null");
		else if (!compression.mustProtectKey(key))
			out.append(key);
		else {
			out.append('"');
			JSONValue.escape(key, out, compression);
			out.append('"');
		}
		compression.objectEndOfKey(out);
		if (value instanceof String) {
			compression.writeString(out, (String) value);
		} else
			JSONValue.writeJSONString(value, out, compression);
		compression.objectElmStop(out);
	}
}
