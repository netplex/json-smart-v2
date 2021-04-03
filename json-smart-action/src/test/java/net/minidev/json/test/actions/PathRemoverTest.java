package net.minidev.json.test.actions;

import net.minidev.json.actions.PathRemover;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link PathRemover}
 *
 * @author adoneitan@gmail.com
 */
@RunWith(Parameterized.class)
public class PathRemoverTest
{
	private String jsonToClean;
	private Object keyToRemove;
	private String expectedJson;

	public PathRemoverTest(String jsonToClean, Object keyToRemove, String expectedJson)
	{
		this.jsonToClean = jsonToClean;
		this.keyToRemove = keyToRemove;
		this.expectedJson = expectedJson;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> params() {
		return Arrays.asList(new Object[][]{
				{null,                                                             "key",                    null                                             }, // null json
				{"{}",                                                             "key",                    "{}"                                             }, // empty json
				{"{\"first\": null}",                                              null,                     "{\"first\": null}"                              }, // null key
				{"{\"first\": null}",                                              "",                       "{\"first\": null}"                              }, // empty string key
				{"{\"first\": null}",                                              new String[]{},           "{\"first\": null}"                              }, // empty string array key
				{"{\"first\": null}",                                              new JSONArray(),          "{\"first\": null}"                              }, // empty json array key
				{"{\"first\": null}",                                              new ArrayList<String>(0), "{\"first\": null}"                              }, // empty list key
				{"{\"first\": null}",                                              "first",                  "{}"                                             }, // remove root key
				{"{\"first.f1\": null}",                                           "first.f1",               "{}"                                             }, // key with dot
				{"{\"first.f1\": \"withDot\", \"first\":{\"f1\": null}}",          "first.f1",               "{\"first\":{}}"                                 }, //9 key with dot ambiguity
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}",                 "first.f2.f3.id",         "{\"first\":{\"f2\":{\"f3\":{}}}}"               }, // nested object remove single leaf
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}",                 "notfound",               "{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}" }, // nested object key does not exist
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3.id",         "{\"first\":{\"f2\":{\"f3\":{\"name\":\"me\"}}}}"}, // nested object remove first leaf
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3.name",       "{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\"}}}}" }, //13 nested object remove last leaf
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first.f2.f3",            "{\"first\":{\"f2\":{}}}"                        }, // nested object remove intermediate node
				{"{\"first\":{\"f2\":{\"f3\":{\"id\":\"id1\",\"name\":\"me\"}}}}", "first",                  "{}"                                             }, // nested object remove root
				{"{\"first\":{\"f2\":[[1,{\"id\":\"id1\"},3],4]}}",                "first.f2.id",            "{\"first\":{\"f2\":[[1,{},3],4]}}"              }, // double nested array remove leaf
				{"{\"first\":{\"f2\":[[1,{\"id\":\"id1\"},3],4]}}",                "first.f2",               "{\"first\":{}}"                                 }, // double nested array remove array
				{"{\"first\":[[1,{\"id\":\"id1\"},3],4]}",                         "first",                  "{}"                                             }, // double nested array remove root

				//arrays
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1",                    "{\"k0\":{}}"                                    }, // value is array
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1.k2",                 "{\"k0\":{\"k1\":[1,{},3,4]}}"                   }, // full path into array object
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                    "k0.k1.3" ,                 "{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}"      }, // full path into array primitive
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},{\"k2\":\"v2\"},3,4]}}",    "k0.k1.k2",                 "{\"k0\":{\"k1\":[1,{},{},3,4]}}"                }, // full path into array with identical items

				// composite json remove all roots
				{"{\"first\": {\"f2\":{\"id\":\"id1\"}}, \"second\": [{\"k1\":{\"id\":\"id1\"}}, 4, 5, 6, {\"id\": 123}], \"third\": 789, \"id\": null}",
						(JSONArray) JSONValue.parse("[\"first\",\"second\",\"third\",\"id\"]"),
						"{}" },
				// composite json remove all leaves
				{"{\"first\": {\"f2\":{\"id\":\"id1\"}}, \"second\": [{\"k1\":{\"id\":\"id1\"}}, 4, 5, 6, {\"id\": 123}], \"third\": 789, \"id\": null}",
						(List<String>) Arrays.asList("first.f2.id", "second.k1.id", "second.id", "third", "id"),
						"{\"first\": {\"f2\":{}}, \"second\": [{\"k1\":{}}, 4, 5, 6, {}]}" },

		});
	}

	@Test
	public void test() throws ParseException
	{
		JSONObject objectToClean = jsonToClean != null ? (JSONObject) JSONValue.parseWithException(jsonToClean) : null;
		JSONObject expectedObject = expectedJson != null ? (JSONObject) JSONValue.parseWithException(expectedJson): null;
		PathRemover cl = switchKeyToRemove();
		cl.remove(objectToClean);
		assertEquals(expectedObject, objectToClean);
	}

	private PathRemover switchKeyToRemove()
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
