package net.minidev.json.test.actions;

import net.minidev.json.actions.PathsRetainer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.parser.ParseException;

// import org.junit.runner.RunWith;
// import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author adoneitan@gmail.com
 */
public class PathsRetainerTest {
	
	public static Stream<Arguments> params() {
		 return Stream.of(
			//nulls, bad/empty keys
			Arguments.of(null,                                                  null,                           null                                                  ),
			Arguments.of(null,                                                  "",                             null                                                  ),
			Arguments.of(null,                                                  "k1",                           null                                                  ),
			Arguments.of(null,                                                  new String[]{},                 null                                                  ),
			Arguments.of(null,                                                  new JSONArray(),                null                                                  ),
			Arguments.of(null,                                                  new ArrayList<String>(0),       null                                                  ),//5
			//empty json, bad/empty keys
			Arguments.of("{}",                                                  null,                           "{}"                                                  ),
			Arguments.of("{}",                                                  "",                             "{}"                                                  ),
			Arguments.of("{}",                                                  "k1",                           "{}"                                                  ),
			Arguments.of("{}",                                                  new String[]{},                 "{}"                                                  ),
			Arguments.of("{}",                                                  new JSONArray(),                "{}"                                                  ),
			Arguments.of("{}",                                                  new ArrayList<String>(0),       "{}"                                                  ),//11
			//simple json, bad/empty keys
			Arguments.of("{\"k0\":\"v0\"}",                                     null,                           "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     "",                             "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     "k1",                           "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     new String[]{},                 "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     new JSONArray(),                "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     new ArrayList<String>(0),       "{}"                                                  ),//17
			//simple json, valid/invalid keys
			Arguments.of("{\"k0\":\"v0\"}",                                     "k0",                           "{\"k0\":\"v0\"}"                                     ),
			Arguments.of("{\"k0\":\"v0\"}",                                     "v0",                           "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     "k0.k1",                        "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\"}",                                     "k1.k0",                        "{}"                                                  ),
			Arguments.of("{\"k0\":null}",                                       "k0",                           "{\"k0\":null}"                                       ),
			Arguments.of("{\"k0\":null}",                                       "v0",                           "{}"                                                  ),//23
			//key with dot char
			Arguments.of("{\"k0.k1\":\"v0\"}",                                  "k0",                           "{}"                                                  ),
			Arguments.of("{\"k0.k1\":\"v0\"}",                                  "k1",                           "{}"                                                  ),
			Arguments.of("{\"k0.k1\":\"v0\"}",                                  "k0.k1",                        "{}"                                                  ),
			// key with dot ambiguity
			Arguments.of("{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0",                           "{\"k0\":{}}"                                         ),//27
			Arguments.of("{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k1",                           "{}"                                                  ),
			Arguments.of("{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0.k1",                        "{\"k0\":{\"k1\":null}}"                              ),//29
			Arguments.of("{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1",                        "{\"k0\":{\"k1\":{}}}"                                ),
			Arguments.of("{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1.k2",                     "{\"k0\":{\"k1\":{\"k2\":null}}}"                     ),//31
			Arguments.of("{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k1.k2",                        "{}"                                                  ),
			Arguments.of("{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k0",                           "{\"k0\":{}}}"                                        ),
			Arguments.of("{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k1.k2",                        "{\"k1\":{\"k2\":\"v2\"}}}"                           ),
			Arguments.of("{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2.k3.k4",                  "{}"                                                  ),
			Arguments.of("{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2.k3",                     "{}"                                                  ),
			Arguments.of("{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2",                        "{\"k0\":{\"k2\":{}}}"                                ),
			Arguments.of("{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0",                           "{\"k0\":{}}"                                         ),

			//ignore non-existent keys but keep good keys
			Arguments.of("{\"k0\":\"v0\",\"k1\":\"v1\"}",                      new String[]{"k0","k2"},         "{\"k0\":\"v0\"}"                                     ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k2"},         "{\"k0\":\"v0\"}"                                     ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2"},      "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}"              ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k0.k2"},      "{\"k0\":\"v0\"}"                                     ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2","k1"}, "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}"              ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1"},         "{\"k0\":\"v0\",\"k1\":{}}"                           ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1","k2"},    "{\"k0\":\"v0\",\"k1\":{}}"                           ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k2"},           "{\"k1\":{\"k2\":\"v2\"}}"                            ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k2.k3"},        "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0.k1.k2"},        "{}"                                                  ),
			Arguments.of("{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k0"},           "{}"                                                  ),
			//arrays - key inside array treated as child
			Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0",                           "{\"k0\":{}}"                                         ),
			Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1",                        "{\"k0\":{\"k1\":[1,{},3,4]}}"                        ),
			Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1.k2",                     "{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}"           ),
			Arguments.of("{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}", "k0.k1",                        "{\"k0\":{\"k1\":[{},{}]}}"                           ),
			Arguments.of("{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}", "k0.k1.k2",                     "{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}" )
			);
		}

	
	
	@ParameterizedTest
	@MethodSource("params")
	public void test(String jsonToReduce, Object keyToKeep, String expectedReducedJson) throws ParseException {
		JSONObject objectToReduce = jsonToReduce != null ? (JSONObject) JSONValue.parseWithException(jsonToReduce)
				: null;
		JSONObject expectedReducedObj = expectedReducedJson != null
				? (JSONObject) JSONValue.parseWithException(expectedReducedJson)
				: null;
		PathsRetainer retainer = switchKeyToRemove(keyToKeep).with(new DotDelimiter().withAcceptDelimiterInNodeName(false));
		JSONObject reducedObj = retainer.retain(objectToReduce);
		assertEquals(expectedReducedObj, reducedObj);
	}

	@SuppressWarnings("unchecked")
	private PathsRetainer switchKeyToRemove(Object keyToKeep) {
		long m = System.currentTimeMillis();
		if (keyToKeep == null && m % 4 == 0) {
			// System.out.println("cast to String");
			return new PathsRetainer((String) null);
		} else if (keyToKeep == null && m % 4 == 1) {
			// System.out.println("cast to String[]");
			return new PathsRetainer((String[]) null);
		} else if (keyToKeep == null && m % 4 == 2) {
			// System.out.println("cast to JSONArray");
			return new PathsRetainer((JSONArray) null);
		} else if (keyToKeep == null && m % 4 == 3) {
			// System.out.println("cast to List<String>");
			return new PathsRetainer((List<String>) null);
		} else if (keyToKeep instanceof String) {
			return new PathsRetainer((String) keyToKeep);
		} else if (keyToKeep instanceof String[]) {
			return new PathsRetainer((String[]) keyToKeep);
		} else if (keyToKeep instanceof JSONArray) {
			return new PathsRetainer((JSONArray) keyToKeep);
		} else if (keyToKeep instanceof List<?>) {
			return new PathsRetainer((List<String>) keyToKeep);
		} else {
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}
}