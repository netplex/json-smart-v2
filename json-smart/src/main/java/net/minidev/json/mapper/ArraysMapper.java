package net.minidev.json.mapper;

/*
 *    Copyright 2011 JSON-SMART authors
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArraysMapper<T> extends AMapper<T> {
	@Override
	public Object createArray() {
		return new ArrayList<Object>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addValue(Object current, Object value) {
		((List<Object>) current).add(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T convert(Object current) {
		return (T) current;
	}

	public static class GenericMapper<T> extends ArraysMapper<T> {
		final Class<?> componentType;
		AMapper<?> subMapper;

		public GenericMapper(Class<T> type) {
			this.componentType = type.getComponentType();
		}

		@SuppressWarnings("unchecked")
		@Override
		public T convert(Object current) {
			int p = 0;

			Object[] r = (Object[]) Array.newInstance(componentType, ((List<?>) current).size());
			for (Object e : ((List<?>) current))
				r[p++] = e;
			return (T) r;
		}

		@Override
		public AMapper<?> startArray(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(componentType);
			return subMapper;
		}

		@Override
		public AMapper<?> startObject(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(componentType);
			return subMapper;
		}
	};

	public static AMapper<int[]> MAPPER_PRIM_INT = new ArraysMapper<int[]>() {
		@Override
		public int[] convert(Object current) {
			int p = 0;
			int[] r = new int[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).intValue();
			return r;
		}
	};

	public static AMapper<Integer[]> MAPPER_INT = new ArraysMapper<Integer[]>() {
		@Override
		public Integer[] convert(Object current) {
			int p = 0;
			Integer[] r = new Integer[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Integer)
					r[p] = (Integer) e;
				else
					r[p] = ((Number) e).intValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<short[]> MAPPER_PRIM_SHORT = new ArraysMapper<short[]>() {
		@Override
		public short[] convert(Object current) {
			int p = 0;
			short[] r = new short[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).shortValue();
			return r;
		}
	};

	public static AMapper<Short[]> MAPPER_SHORT = new ArraysMapper<Short[]>() {
		@Override
		public Short[] convert(Object current) {
			int p = 0;
			Short[] r = new Short[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Short)
					r[p] = (Short) e;
				else
					r[p] = ((Number) e).shortValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<byte[]> MAPPER_PRIM_BYTE = new ArraysMapper<byte[]>() {
		@Override
		public byte[] convert(Object current) {
			int p = 0;
			byte[] r = new byte[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).byteValue();
			return r;
		}
	};

	public static AMapper<Byte[]> MAPPER_BYTE = new ArraysMapper<Byte[]>() {
		@Override
		public Byte[] convert(Object current) {
			int p = 0;
			Byte[] r = new Byte[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Byte)
					r[p] = (Byte) e;
				else
					r[p] = ((Number) e).byteValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<char[]> MAPPER_PRIM_CHAR = new ArraysMapper<char[]>() {
		@Override
		public char[] convert(Object current) {
			int p = 0;
			char[] r = new char[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = e.toString().charAt(0);
			return r;
		}
	};

	public static AMapper<Character[]> MAPPER_CHAR = new ArraysMapper<Character[]>() {
		@Override
		public Character[] convert(Object current) {
			int p = 0;
			Character[] r = new Character[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				r[p] = e.toString().charAt(0);
				p++;
			}
			return r;
		}
	};

	public static AMapper<long[]> MAPPER_PRIM_LONG = new ArraysMapper<long[]>() {
		@Override
		public long[] convert(Object current) {
			int p = 0;
			long[] r = new long[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).intValue();
			return r;
		}
	};

	public static AMapper<Long[]> MAPPER_LONG = new ArraysMapper<Long[]>() {
		@Override
		public Long[] convert(Object current) {
			int p = 0;
			Long[] r = new Long[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Float)
					r[p] = ((Long) e);
				else
					r[p] = ((Number) e).longValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<float[]> MAPPER_PRIM_FLOAT = new ArraysMapper<float[]>() {
		@Override
		public float[] convert(Object current) {
			int p = 0;
			float[] r = new float[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).floatValue();
			return r;
		}
	};

	public static AMapper<Float[]> MAPPER_FLOAT = new ArraysMapper<Float[]>() {
		@Override
		public Float[] convert(Object current) {
			int p = 0;
			Float[] r = new Float[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Float)
					r[p] = ((Float) e);
				else
					r[p] = ((Number) e).floatValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<double[]> MAPPER_PRIM_DOUBLE = new ArraysMapper<double[]>() {
		@Override
		public double[] convert(Object current) {
			int p = 0;
			double[] r = new double[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Number) e).doubleValue();
			return r;
		}
	};

	public static AMapper<Double[]> MAPPER_DOUBLE = new ArraysMapper<Double[]>() {
		@Override
		public Double[] convert(Object current) {
			int p = 0;
			Double[] r = new Double[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Double)
					r[p] = ((Double) e);
				else
					r[p] = ((Number) e).doubleValue();
				p++;
			}
			return r;
		}
	};

	public static AMapper<boolean[]> MAPPER_PRIM_BOOL = new ArraysMapper<boolean[]>() {
		@Override
		public boolean[] convert(Object current) {
			int p = 0;
			boolean[] r = new boolean[((List<?>) current).size()];
			for (Object e : ((List<?>) current))
				r[p++] = ((Boolean) e).booleanValue();
			return r;
		}
	};

	public static AMapper<Boolean[]> MAPPER_BOOL = new ArraysMapper<Boolean[]>() {
		@Override
		public Boolean[] convert(Object current) {
			int p = 0;
			Boolean[] r = new Boolean[((List<?>) current).size()];
			for (Object e : ((List<?>) current)) {
				if (e == null)
					continue;
				if (e instanceof Boolean)
					r[p] = ((Boolean) e).booleanValue();
				else if (e instanceof Number)
					r[p] = ((Number) e).intValue() != 0;
				else
					throw new RuntimeException("can not convert " + e + " toBoolean");
				p++;
			}
			return r;
		}
	};
}
