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
import net.minidev.json.JSONArray;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONObject;

public class DefaultMapper<T> extends AMapper<T> {
	private DefaultMapper() {
	}

	public static AMapper<JSONAwareEx> DEFAULT = new DefaultMapper<JSONAwareEx>();

	@Override
	public AMapper<JSONAwareEx> startObject(String key) {
		return DEFAULT;
	}

	@Override
	public AMapper<JSONAwareEx> startArray(String key) {
		return DEFAULT;
	}

	@Override
	public Object createObject() {
		return new JSONObject();
	}

	@Override
	public Object createArray() {
		return new JSONArray();
	}

	@Override
	public void setValue(Object current, String key, Object value) {
		((JSONObject) current).put(key, value);
	}

	@Override
	public void addValue(Object current, Object value) {
		((JSONArray) current).add(value);
	}

}
