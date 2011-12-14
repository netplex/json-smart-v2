package net.minidev.json;

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
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.minidev.json.mapper.AMapper;
import net.minidev.json.mapper.DefaultMapperOrdered;
import net.minidev.json.mapper.Mapper;

/**
 * A JQuery like Json editor, accessor.
 * 
 * @since 1.0.9
 * 
 * @author Uriel Chemouni <uchemouni@gmail.com>
 */
public class JSONNavi<T> {
	private AMapper<? super T> mapper;
	private Object root;

	private Stack<Object> stack = new Stack<Object>();
	private Stack<Object> path = new Stack<Object>();

	private Object current;
	private boolean failure = false;
	private String failureMessage;

	private boolean readonly = false;
	private Object missingKey = null;

	public static JSONNavi<JSONAwareEx> newInstance() {
		return new JSONNavi<JSONAwareEx>(DefaultMapperOrdered.DEFAULT);
	}

	public static JSONNavi<JSONObject> newInstanceObject() {
		JSONNavi<JSONObject> o = new JSONNavi<JSONObject>(Mapper.getMapper(JSONObject.class));
		o.object();
		return o;
	}

	public JSONNavi(AMapper<? super T> mapper) {
		this.mapper = mapper;
	}

	public JSONNavi(String json) {
		this.root = JSONValue.parse(json);
		this.current = this.root;
		readonly = true;
	}

	public JSONNavi(String json, AMapper<T> mapper) {
		this.root = JSONValue.parse(json, mapper);
		this.mapper = mapper;
		this.current = this.root;
		readonly = true;
	}

	public JSONNavi(String json, Class<T> mapTo) {
		this.root = JSONValue.parse(json, mapTo);
		this.mapper = Mapper.getMapper(mapTo);
		this.current = this.root;
		readonly = true;
	}

	public JSONNavi<T> root() {
		this.current = root;
		this.stack.clear();
		this.path.clear();
		this.failure = false;
		this.missingKey = null;
		this.failureMessage = null;
		return this;
	}

	public boolean hasFailure() {
		return failure;
	}

	public Object getCurrentObject() {
		return current;
	}

	public JSONNavi<?> at(String key) {
		if (failure)
			return this;
		if (!isObject(current))
			object();
		if (!(current instanceof Map))
			return failure("current node is not an Object", key);
		if (!o(current).containsKey(key)) {
			if (readonly)
				return failure("current Object have no key named " + key, key);
			stack.add(current);
			path.add(key);
			current = null;
			missingKey = key;
			return this;
		}
		Object next = o(current).get(key);
		stack.add(current);
		path.add(key);
		current = next;
		return this;
	}

	public JSONNavi<T> set(String key, String value) {
		object();
		if (failure)
			return this;
		o(current).put(key, value);
		return this;
	}

	public JSONNavi<T> set(String key, Number value) {
		object();
		if (failure)
			return this;
		o(current).put(key, value);
		return this;
	}

	/**
	 * write an value in the current object
	 * 
	 * @param key
	 *            key to access
	 * @param value
	 *            new value
	 * @return this
	 */
	public JSONNavi<T> set(String key, long value) {
		return set(key, Long.valueOf(value));
	}

	/**
	 * write an value in the current object
	 * 
	 * @param key
	 *            key to access
	 * @param value
	 *            new value
	 * @return this
	 */
	public JSONNavi<T> set(String key, int value) {
		return set(key, Integer.valueOf(value));
	}

	/**
	 * write an value in the current object
	 * 
	 * @param key
	 *            key to access
	 * @param value
	 *            new value
	 * @return this
	 */
	public JSONNavi<T> set(String key, double value) {
		return set(key, Double.valueOf(value));
	}

	/**
	 * write an value in the current object
	 * 
	 * @param key
	 *            key to access
	 * @param value
	 *            new value
	 * @return this
	 */
	public JSONNavi<T> set(String key, float value) {
		return set(key, Float.valueOf(value));
	}

	/**
	 * add value to the current arrays
	 * 
	 * @param values
	 *            to add
	 * @return this
	 */
	public JSONNavi<T> add(Object... values) {
		array();
		if (failure)
			return this;
		List<Object> list = a(current);
		for (Object o : values)
			list.add(o);
		return this;
	}

	/**
	 * get the current object value as String if the current Object is null
	 * return null.
	 */
	public String asString() {
		if (current == null)
			return null;
		if (current instanceof String)
			return (String) current;
		return current.toString();
	}

	/**
	 * get the current value as double if the current Object is null return
	 * Double.NaN
	 */
	public double asDouble() {
		if (current instanceof Number)
			return ((Number) current).doubleValue();
		return Double.NaN;
	}

	/**
	 * get the current object value as Double if the current Double can not be
	 * cast as Integer return null.
	 */
	public Double asDoubleObj() {
		if (current == null)
			return null;
		if (current instanceof Number) {
			if (current instanceof Double)
				return (Double) current;
			return Double.valueOf(((Number) current).doubleValue());
		}
		return Double.NaN;
	}

	/**
	 * get the current value as float if the current Object is null return
	 * Float.NaN
	 */
	public double asFloat() {
		if (current instanceof Number)
			return ((Number) current).floatValue();
		return Float.NaN;
	}

	/**
	 * get the current object value as Float if the current Float can not be
	 * cast as Integer return null.
	 */
	public Float asFloatObj() {
		if (current == null)
			return null;
		if (current instanceof Number) {
			if (current instanceof Float)
				return (Float) current;
			return Float.valueOf(((Number) current).floatValue());
		}
		return Float.NaN;
	}

	/**
	 * get the current value as int if the current Object is null return 0
	 */
	public int asInt() {
		if (current instanceof Number)
			return ((Number) current).intValue();
		return 0;
	}

	/**
	 * get the current object value as Integer if the current Object can not be
	 * cast as Integer return null.
	 */
	public Integer asIntegerObj() {
		if (current == null)
			return null;
		if (current instanceof Number) {
			if (current instanceof Integer)
				return (Integer) current;
			if (current instanceof Long) {
				Long l = (Long) current;
				if (l.longValue() == l.intValue()) {
					return Integer.valueOf(l.intValue());
				}
			}
			return null;
		}
		return null;
	}

	/**
	 * get the current value as long if the current Object is null return 0
	 */
	public long asLong() {
		if (current instanceof Number)
			return ((Number) current).longValue();
		return 0L;
	}

	/**
	 * get the current object value as Long if the current Object can not be
	 * cast as Long return null.
	 */
	public Long asLongObj() {
		if (current == null)
			return null;
		if (current instanceof Number) {
			if (current instanceof Long)
				return (Long) current;
			if (current instanceof Integer)
				return Long.valueOf(((Number) current).longValue());
			return null;
		}
		return null;
	}

	/**
	 * get the current value as boolean if the current Object is null or is not
	 * a boolean return false
	 */
	public boolean asBoolean() {
		if (current instanceof Boolean)
			return ((Boolean) current).booleanValue();
		return false;
	}

	/**
	 * get the current object value as Boolean if the current Object is not a
	 * Boolean return null.
	 */
	public Boolean asBooleanObj() {
		if (current == null)
			return null;
		if (current instanceof Boolean)
			return (Boolean) current;
		return null;
	}

	/**
	 * Set current value as Json Object You can also skip this call, Objects can
	 * be create automatically.
	 */
	public JSONNavi<T> object() {
		if (failure)
			return this;
		if (current == null && readonly)
			failure("Can not create Object child in readonly", null);
		if (current != null) {
			if (isObject(current))
				return this;
			if (isArray(current))
				failure("can not use Object feature on Array.", null);
			failure("Can not use current possition as Object", null);
		} else {
			current = mapper.createObject();
		}
		if (root == null)
			root = current;
		else
			store();
		return this;
	}

	/**
	 * Set current value as Json Array You can also skip this call Arrays can be
	 * create automatically.
	 */
	public JSONNavi<T> array() {
		if (failure)
			return this;
		if (current == null && readonly)
			failure("Can not create Array child in readonly", null);
		if (current != null) {
			if (isArray(current))
				return this;
			if (isObject(current))
				failure("can not use Object feature on Array.", null);
			failure("Can not use current possition as Object", null);
		} else {
			current = mapper.createArray();
		}
		if (root == null)
			root = current;
		else
			store();
		return this;
	}

	/**
	 * set current value as Number
	 */
	public JSONNavi<T> set(Number num) {
		if (failure)
			return this;
		current = num;
		store();
		return this;
	}

	/**
	 * set current value as Boolean
	 */
	public JSONNavi<T> set(Boolean bool) {
		if (failure)
			return this;
		current = bool;
		store();
		return this;
	}

	/**
	 * set current value as String
	 */
	public JSONNavi<T> set(String text) {
		if (failure)
			return this;
		current = text;
		store();
		return this;
	}

	public T getRoot() {
		return (T) root;
	}

	/**
	 * internal store current Object in current non existing localization
	 */
	private void store() {
		Object parent = stack.peek();
		if (isObject(parent))
			o(parent).put((String) missingKey, current);
		else if (isArray(parent)) {
			int index = ((Number) missingKey).intValue();
			List<Object> lst = a(parent);
			while (lst.size() <= index)
				lst.add(null);
			lst.set(index, current);
		}
	}

	/**
	 * check if Object is an Array
	 */
	private boolean isArray(Object obj) {
		if (obj == null)
			return false;
		return (obj instanceof List);
	}

	/**
	 * check if Object is an Map
	 */
	private boolean isObject(Object obj) {
		if (obj == null)
			return false;
		return (obj instanceof Map);
	}

	/**
	 * internal cast to List
	 */
	@SuppressWarnings("unchecked")
	private List<Object> a(Object obj) {
		return (List<Object>) obj;
	}

	/**
	 * internal cast to Map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> o(Object obj) {
		return (Map<String, Object>) obj;
	}

	/**
	 * Access to the index position.
	 * 
	 * If index is less than 0 access element index from the end like in python.
	 * 
	 * @param index
	 *            0 based desired position in Array
	 */
	public JSONNavi<?> at(int index) {
		if (failure)
			return this;
		if (!(current instanceof List))
			return failure("current node is not an Array", index);
		@SuppressWarnings("unchecked")
		List<Object> lst = ((List<Object>) current);
		if (index < 0) {
			index = lst.size() + index;
			if (index < 0)
				index = 0;
		}
		if (index >= lst.size())
			if (readonly)
				return failure("Out of bound exception for index", index);
			else {
				stack.add(current);
				path.add(index);
				current = null;
				missingKey = index;
				return this;
			}
		Object next = lst.get(index);
		stack.add(current);
		path.add(index);
		current = next;
		return this;
	}

	/**
	 * Access to last + 1 the index position.
	 * 
	 * this method can only be used in writing mode.
	 */
	public JSONNavi<?> atNext() {
		if (failure)
			return this;
		if (!(current instanceof List))
			return failure("current node is not an Array", null);
		@SuppressWarnings("unchecked")
		List<Object> lst = ((List<Object>) current);
		return at(lst.size());
	}

	/**
	 * call up() level times.
	 * 
	 * @param level
	 *            number of parent move.
	 */
	public JSONNavi<?> up(int level) {
		while (level-- > 0) {
			if (stack.size() > 0) {
				current = stack.pop();
				path.pop();
			} else
				break;
		}
		return this;
	}

	/**
	 * Move one level up in Json tree. if no more level up is available the
	 * statement had no effect.
	 */
	public JSONNavi<?> up() {
		if (stack.size() > 0) {
			current = stack.pop();
			path.pop();
		}
		return this;
	}

	private final static JSONStyle ERROR_COMPRESS = new JSONStyle(JSONStyle.FLAG_PROTECT_4WEB);

	/**
	 * return the Object as a Json String
	 */
	public String toString() {
		if (failure)
			return JSONValue.toJSONString(failureMessage, ERROR_COMPRESS);
		return JSONValue.toJSONString(root);
	}

	/**
	 * return the Object as a Json String
	 * 
	 * @param compression
	 */
	public String toString(JSONStyle compression) {
		if (failure)
			return JSONValue.toJSONString(failureMessage, compression);
		return JSONValue.toJSONString(root, compression);
	}

	/**
	 * Internally log errors.
	 */
	private JSONNavi<?> failure(String err, Object jPathPostfix) {
		failure = true;
		StringBuilder sb = new StringBuilder();
		sb.append("Error: ");
		sb.append(err);
		sb.append(" at ");
		sb.append(getJPath());
		if (jPathPostfix != null)
			if (jPathPostfix instanceof Integer)
				sb.append('[').append(jPathPostfix).append(']');
			else
				sb.append('/').append(jPathPostfix);
		this.failureMessage = sb.toString();
		return this;
	}

	/**
	 * @return JPath to the current position
	 */
	public String getJPath() {
		StringBuilder sb = new StringBuilder();
		for (Object o : path) {
			if (o instanceof String)
				sb.append('/').append(o.toString());
			else
				sb.append('[').append(o.toString()).append(']');
		}
		return sb.toString();
	}
}