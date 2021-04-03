package net.minidev.json.test.actions;

import net.minidev.json.actions.PathRemover;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link PathRemover}
 *
 * @author adoneitan@gmail.com
 */
// @RunWith(Parameterized.class)
public class PathRemoverTest {
	public static Stream<Arguments> params() {
		 return Stream.of(
				Arguments.of(null,                                                             "key",                    null                                             ), // null json
				Arguments.of("{}",                                                             "key",                    "{}"                                             ), // empty json
				Arguments.of("{\"first\": null}",                                              null,                     "{\"first\": null}"                              ), // null key
				Arguments.of("{\"first\": null}",                                              "",                       "{\"first\": null}"                              ), // empty string key
				Arguments.of("{\"first\": null}",                                              new String[]{},           "{\"first\": null}"                              ), // empty string array key
				Arguments.of("{\"first\": null}",                                              new JSONArray(),          "{\"first\": null}"                              ), // empty json array key
				Arguments.of("{\"first\": null}",                                              new ArrayList<String>(0), "{\"first\": null}"                              ), // empty list key
				Arguments.of("{\"first\": null}",                                              "first",                  "{}"                                             ), // remove root key
				Arguments.of("{\"first.f1\": null}",                                           "first.f1",               "{}"                                             ), // key with dot
				Arguments.of("{\"first.f1\": \"withDot\", \"first\":{\"f1\": null}}",          "first.f1",               "{\"first\":{}}"                                 ), //9 key with dot ambiguity
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}",                 "first.f2.f3.id",         "{\"first\":{\"f2\":{\"f3\":{}}}}"               ), // nested object remove single leaf
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}",                 "notfound",               "{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}" ), // nested object key does not exist
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3.id",         "{\"first\":{\"f2\":{\"f3\":{\"name\":\"me\"}}}}"), // nested object remove first leaf
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3.name",       "{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}" ), //13 nested object remove last leaf
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3",            "{\"first\":{\"f2\":{}}}"                        ), // nested object remove intermediate node
				Arguments.of("{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first",                  "{}"                                             ), // nested object remove root
				Arguments.of("{\"first\":{\"f2\":[[1,{\"id\":\"id1\"},3],4]}}",                "first.f2.id",            "{\"first\":{\"f2\":[[1,{},3],4]}}"              ), // double nested array remove leaf
				Arguments.of("{\"first\":{\"f2\":[[1,{\"id\":\"id1\"},3],4]}}",                "first.f2",               "{\"first\":{}}"                                 ), // double nested array remove array
				Arguments.of("{\"first\":[[1,{\"id\":\"id1\"},3],4]}",                         "first",                  "{}"                                             ), // double nested array remove root
				//arrays
				Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1",                    "{\"k0\":{}}"                                    ), // value is array
				Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1.k2",                 "{\"k0\":{\"k1\":[1,{},3,4]}}"                   ), // full path into array object
				Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1.3" ,                 "{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}"      ), // full path into array primitive
				Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},{\"k2\":\"v2\"},3,4]}}",    "k0.k1.k2",                 "{\"k0\":{\"k1\":[1,{},{},3,4]}}"                ), // full path into array with identical items
				// composite json remove all roots
				Arguments.of("{\"first\": {\"f2\":{\"id\":\"id1\"}}, \"second\": [{\"k1\":{\"id\":\"id1\"}}, 4, 5, 6, {\"id\": 123}], \"third\": 789, \"id\": null}",
						(JSONArray) JSONValue.parse("[\"first\",\"second\",\"third\",\"id\"]"),
						"{}" ),
				// composite json remove all leaves
				Arguments.of("{\"first\": {\"f2\":{\"id\":\"id1\"}}, \"second\": [{\"k1\":{\"id\":\"id1\"}}, 4, 5, 6, {\"id\": 123}], \"third\": 789, \"id\": null}",
						(List<String>) Arrays.asList("first.f2.id", "second.k1.id", "second.id", "third", "id"),
						"{\"first\": {\"f2\":{}}, \"second\": [{\"k1\":{}}, 4, 5, 6, {}]}" )
		);
	}

	@ParameterizedTest
	@MethodSource("params")
	public void test(String jsonToClean, Object keyToRemove, String expectedJson) throws ParseException
	{
		JSONObject objectToClean = jsonToClean != null ? (JSONObject) JSONValue.parseWithException(jsonToClean) : null;
		JSONObject expectedObject = expectedJson != null ? (JSONObject) JSONValue.parseWithException(expectedJson): null;
		PathRemover cl = switchKeyToRemove(keyToRemove);
		cl.remove(objectToClean);
		assertEquals(expectedObject, objectToClean);
	}

	@SuppressWarnings("unchecked")
	private PathRemover switchKeyToRemove(Object keyToRemove)
	{
		long m = System.currentTimeMillis();
		if (keyToRemove == null && m % 4 == 0) {
			// System.out.println("cast to String");
			return new PathRemover((String)null);
		} else if (keyToRemove == null && m % 4 == 1) {
			// System.out.println("cast to String[]");
			return new PathRemover((String[])null);
		} else if (keyToRemove == null && m % 4 == 2) {
			// System.out.println("cast to JSONArray");
			return new PathRemover((JSONArray)null);
		} else if (keyToRemove == null && m % 4 == 3) {
			// System.out.println("cast to List<String>");
			return new PathRemover((List<String>)null);
		} else if (keyToRemove instanceof String) {
			return new PathRemover((String)keyToRemove);
		} else if (keyToRemove instanceof String[]) {
			return new PathRemover((String[])keyToRemove);
		} else if (keyToRemove instanceof JSONArray) {
			return new PathRemover((JSONArray)keyToRemove);
		} else if (keyToRemove instanceof List<?>) {
			return new PathRemover((List<String>)keyToRemove);
		} else {
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}

}
