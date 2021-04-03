package net.minidev.json.test.actions;

import net.minidev.json.actions.PathReplicator;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author adoneitan@gmail.com
 */
public class PathReplicatorTest {

	@SuppressWarnings("unchecked")
	private static <T> T filter(T obj) {
		if (obj == null)
			return null;
		if (obj instanceof String)
			return (T)(((String)obj).replace("'", "\""));
		return obj;
	}
	
	public static Stream<Arguments> params() {
		 return Stream.of(
				//nulls, bad/empty keys
				Arguments.of(null,                                        null,                           null                                         ),
				Arguments.of(null,                                        "",                             null                                         ),
				Arguments.of(null,                                        "k1",                           null                                         ),
				Arguments.of(null,                                        new String[]{},                 null                                         ),
				Arguments.of(null,                                        new JSONArray(),                null                                         ),
				Arguments.of(null,                                        new ArrayList<String>(0),       null                                         ),//5

				//empty json, bad/empty keys
				Arguments.of("{}",                                        null,                           "{}"                                         ),
				Arguments.of("{}",                                        "",                             "{}"                                         ),
				Arguments.of("{}",                                        "k1",                           "{}"                                         ),
				Arguments.of("{}",                                        new String[]{},                 "{}"                                         ),
				Arguments.of("{}",                                        new JSONArray(),                "{}"                                         ),
				Arguments.of("{}",                                        new ArrayList<String>(0),       "{}"                                         ),//11

				//simple json, bad/empty keys
				Arguments.of("{'k0':'v0'}",                               null,                           "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               "",                             "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               "k1",                           "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               new String[]{},                 "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               new JSONArray(),                "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               new ArrayList<String>(0),       "{}"                                         ),//17

				//simple json, valid/invalid keys
				Arguments.of("{'k0':'v0'}",                               "k0",                           "{'k0':'v0'}"                                ),
				Arguments.of("{'k0':'v0'}",                               "v0",                           "{}"                                         ),
				Arguments.of("{'k0':'v0'}",                               "k0.k1",                        "{}"                                         ),//20
				Arguments.of("{'k0':'v0'}",                               "k1.k0",                        "{}"                                         ),
				Arguments.of("{'k0':null}",                               "k0",                           "{'k0':null}"                                ),
				Arguments.of("{'k0':null}",                               "v0",                           "{}"                                         ),

				//key with dot char
				Arguments.of("{'k0.k1':'v0'}",                            "k0",                           "{}"                                         ),
				Arguments.of("{'k0.k1':'v0'}",                            "k1",                           "{}"                                         ),
				Arguments.of("{'k0.k1':'v0'}",                            "k0.k1",                        "{}"                                         ),

				// key with dot ambiguity
				Arguments.of("{'k0.k1':'withDot','k0':{'k1':null}}",      "k0",                           "{'k0':{}}"                                  ),
				Arguments.of("{'k0.k1':'withDot','k0':{'k1':null}}",      "k1",                           "{}"                                         ),
				Arguments.of("{'k0.k1':'withDot','k0':{'k1':null}}",      "k0.k1",                        "{'k0':{'k1':null}}"                         ),
				Arguments.of("{'k0':{'k1.k2':'dot','k1':{'k2':null}}}",   "k0.k1",                        "{'k0':{'k1':{}}}"                           ),
				Arguments.of("{'k0':{'k1.k2':'dot','k1':{'k2':null}}}",   "k0.k1.k2",                     "{'k0':{'k1':{'k2':null}}}"                  ),
				Arguments.of("{'k0':{'k1.k2':'dot','k1':{'k2':null}}}",   "k1.k2",                        "{}"                                         ),
				Arguments.of("{'k0':{'k1.k2':'dot'},'k1':{'k2':'v2'}}}",  "k0",                           "{'k0':{}}}"                                 ),
				Arguments.of("{'k0':{'k1.k2':'dot'},'k1':{'k2':'v2'}}}",  "k1.k2",                        "{'k1':{'k2':'v2'}}}"                        ),
				Arguments.of("{'k0':{'k1':'v1','k2':{'k3.k4':'dot'}}}",   "k0.k2.k3.k4",                  "{}"                                         ),
				Arguments.of("{'k0':{'k1':'v1','k2':{'k3.k4':'dot'}}}",   "k0.k2.k3",                     "{}"                                         ),
				Arguments.of("{'k0':{'k1':'v1','k2':{'k3.k4':'dot'}}}",   "k0.k2",                        "{'k0':{'k2':{}}}"                           ),
				Arguments.of("{'k0':{'k1':'v1','k2':{'k3.k4':'dot'}}}",   "k0",                           "{'k0':{}}"                                  ),//38

				//ignore non-existent keys but keep good keys
				Arguments.of("{'k0':'v0','k1':'v1'}",                     new String[]{"k0","k2"},         "{'k0':'v0'}"                               ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k2"},         "{'k0':'v0'}"                               ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k1.k2"},      "{'k0':'v0','k1':{'k2':'v2'}}"              ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k0.k2"},      "{'k0':'v0'}"                               ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k1.k2","k1"}, "{'k0':'v0','k1':{'k2':'v2'}}"              ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k1"},         "{'k0':'v0','k1':{}}"                       ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0","k1","k2"},    "{'k0':'v0','k1':{}}"                       ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k1.k2"},           "{'k1':{'k2':'v2'}}"                        ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k1.k2.k3"},        "{}"                                        ),
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k0.k1.k2"},        "{}"                                        ),//48
				Arguments.of("{'k0':'v0','k1':{'k2':'v2'}}",              new String[]{"k1.k0"},           "{}"                                        ),

				//arrays - key inside array treated as child
				Arguments.of("{'k0':{'k1':[1,2,3,4]}}",                                "k0",                                 "{'k0':{}}"                                    ),
				Arguments.of("{'k0':{'k1':[1,2,3,4]}}",                                "k0.k1",                              "{'k0':{'k1':[1,2,3,4]}}"                      ),
				Arguments.of("{'k0':{'k1':[1,{'k2':'v2'},3,4]}}",                      "k0",                                 "{'k0':{}}"                                    ),
				Arguments.of("{'k0':{'k1':[1,{'k2':'v2'},3,4]}}",                      "k0.k1",                              "{'k0':{'k1':[1,{},3,4]}}"                     ),
				Arguments.of("{'k0':{'k1':[1,{'k2':'v2'},3,4]}}",                      "k0.k1.k2",                           "{'k0':{'k1':[{'k2':'v2'}]}}"                  ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'},{'k2':'v2'}]}}",                "k0.k1",                              "{'k0':{'k1':[{},{}]}}"                        ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'},{'k2':'v2'}]}}",                "k0.k1.k2",                           "{'k0':{'k1':[{'k2':'v2'},{'k2':'v2'}]}}"      ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':'v4'}]}}",         "k0.k1.k2",                           "{'k0':{'k1':[{'k2':'v2'}]}}"                  ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':'v4'}]}}",         "k0.k3.k4",                           "{'k0':{'k3':[{'k4':'v4'}]}}"                  ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':{'k5':'v5'}}]}}",  "k0.k1.k2",                           "{'k0':{'k1':[{'k2':'v2'}]}}"                  ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':{'k5':'v5'}}]}}",  "k0.k3.k4",                           "{'k0':{'k3':[{'k4':{}}]}}"                    ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':{'k5':'v5'}}]}}",  "k0.k3.k4.k5",                        "{'k0':{'k3':[{'k4':{'k5':'v5'}}]}}"           ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':{'k5':'v5'}}]}}",  new String[]{"k0.k1", "k0.k3"},       "{'k0':{'k3':[{}],'k1':[{}]}}"                 ),
				Arguments.of("{'k0':{'k1':[{'k2':'v2'}],'k3':[{'k4':{'k5':'v5'}}]}}",  new String[]{"k0.k1", "k0.k3.k4.k5"}, "{'k0':{'k3':[{'k4':{'k5':'v5'}}],'k1':[{}]}}" )
		);
	}

	@ParameterizedTest
	@MethodSource("params")
	public void test(String jsonSource, Object pathsToCopy, Object expected) throws Exception {
		jsonSource = filter(jsonSource);
		pathsToCopy = filter(pathsToCopy);
		expected = filter(expected);

		JSONObject objectSource = jsonSource != null ? (JSONObject) JSONValue.parseWithException(jsonSource) : null;
		PathReplicator copier = switchKeyToCopy(pathsToCopy);
		JSONObject copied = copier.replicate(objectSource);
		JSONObject expectedObj = expected != null ? (JSONObject) JSONValue.parseWithException((String) expected) : null;
		assertEquals(expectedObj, copied);
	}

	@ParameterizedTest
	@MethodSource("params")
	public void test2(String jsonSource, Object pathsToCopy, Object expected) throws Exception {
		jsonSource = filter(jsonSource);
		pathsToCopy = filter(pathsToCopy);
		expected = filter(expected);
		JSONObject objectSource = jsonSource != null ? (JSONObject) JSONValue.parseWithException(jsonSource) : null;
		PathReplicator copier = switchKeyToCopy2(pathsToCopy);
		JSONObject copied = copier.replicate(objectSource);
		JSONObject expectedObj = expected != null ? (JSONObject) JSONValue.parseWithException((String) expected) : null;
		assertEquals(expectedObj, copied);
	}

	@SuppressWarnings("unchecked")
	private PathReplicator switchKeyToCopy(Object pathsToCopy) {
		long m = System.currentTimeMillis();
		if (pathsToCopy == null && m % 4 == 0) {
			// System.out.println("cast to String");
			return new PathReplicator((String) null);
		} else if (pathsToCopy == null && m % 4 == 1) {
			// System.out.println("cast to String[]");
			return new PathReplicator((String[]) null);
		} else if (pathsToCopy == null && m % 4 == 2) {
			// System.out.println("cast to JSONArray");
			return new PathReplicator((JSONArray) null);
		} else if (pathsToCopy == null && m % 4 == 3) {
			// System.out.println("cast to List<String>");
			return new PathReplicator((List<String>) null);
		} else if (pathsToCopy instanceof String) {
			return new PathReplicator((String) pathsToCopy);
		} else if (pathsToCopy instanceof String[]) {
			return new PathReplicator((String[]) pathsToCopy);
		} else if (pathsToCopy instanceof JSONArray) {
			return new PathReplicator((JSONArray) pathsToCopy);
		} else if (pathsToCopy instanceof List<?>) {
			return new PathReplicator((List<String>) pathsToCopy);
		} else {
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}

	@SuppressWarnings("unchecked")
	private PathReplicator switchKeyToCopy2(Object pathsToCopy) {
		long m = System.currentTimeMillis();
		if (pathsToCopy == null && m % 4 == 0) {
			// System.out.println("cast to String");
			return new PathReplicator((String) null);
		} else if (pathsToCopy == null && m % 4 == 1) {
			// System.out.println("cast to String[]");
			return new PathReplicator((String[]) null);
		} else if (pathsToCopy == null && m % 4 == 2) {
			// System.out.println("cast to JSONArray");
			return new PathReplicator((JSONArray) null);
		} else if (pathsToCopy == null && m % 4 == 3) {
			// System.out.println("cast to List<String>");
			return new PathReplicator((List<String>) null);
		} else if (pathsToCopy instanceof String) {
			return new PathReplicator((String) pathsToCopy);
		} else if (pathsToCopy instanceof String[]) {
			return new PathReplicator((String[]) pathsToCopy);
		} else if (pathsToCopy instanceof JSONArray) {
			return new PathReplicator((JSONArray) pathsToCopy);
		} else if (pathsToCopy instanceof List<?>) {
			return new PathReplicator((List<String>) pathsToCopy);
		} else {
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}

}