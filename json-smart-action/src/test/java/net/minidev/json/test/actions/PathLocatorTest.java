package net.minidev.json.test.actions;

import net.minidev.json.actions.PathLocator;
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
 * @author adoneitan@gmail.com
 */
@RunWith(Parameterized.class)
public class PathLocatorTest
{
	private String jsonToSearch;
	private Object keysToFind;
	private String[] expectedFound;

	public PathLocatorTest(String jsonToSearch, Object keysToFind, String[] expectedFound)
	{
		this.jsonToSearch = jsonToSearch;
		this.keysToFind = keysToFind;
		this.expectedFound = expectedFound;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> params() {
		return Arrays.asList(new Object[][]{

				//nulls, bad/empty keys
				{null,                                                  null,                           new String[]{}                       },
				{null,                                                  "",                             new String[]{}                       },
				{null,                                                  "k1",                           new String[]{}                       },
				{null,                                                  new String[]{},                 new String[]{}                       },
				{null,                                                  new JSONArray(),                new String[]{}                       },
				{null,                                                  new ArrayList<String>(0),       new String[]{}                       },//5

				//empty json, bad/empty keys
				{"{}",                                                  null,                           new String[]{}                       },
				{"{}",                                                  "",                             new String[]{}                       },
				{"{}",                                                  "k1",                           new String[]{}                       },
				{"{}",                                                  new String[]{},                 new String[]{}                       },
				{"{}",                                                  new JSONArray(),                new String[]{}                       },//10
				{"{}",                                                  new ArrayList<String>(0),       new String[]{}                       },

				//simple json, bad/empty keys
				{"{\"k0\":\"v0\"}",                                     null,                           new String[]{}                       },
				{"{\"k0\":\"v0\"}",                                     "",                             new String[]{}                       },
				{"{\"k0\":\"v0\"}",                                     "k1",                           new String[]{}                       },
				{"{\"k0\":\"v0\"}",                                     new String[]{},                 new String[]{}                       },//15
				{"{\"k0\":\"v0\"}",                                     new JSONArray(),                new String[]{}                       },
				{"{\"k0\":\"v0\"}",                                     new ArrayList<String>(0),       new String[]{}                       },

				//simple json, valid/invalid keys
				{"{\"k0\":\"v0\"}",                                     "k0",                           new String[]{"k0"}                   },
				{"{\"k0\":\"v0\"}",                                     "v0",                           new String[]{}                       },
				{"{\"k0\":\"v0\"}",                                     "k0.k1",                        new String[]{}                       },//20
				{"{\"k0\":\"v0\"}",                                     "k1.k0",                        new String[]{}                       },
				{"{\"k0\":null}",                                       "k0",                           new String[]{"k0"}                   },
				{"{\"k0\":null}",                                       null,                           new String[]{}                       },

				//key with dot char
				{"{\"k0.k1\":\"v0\"}",                                  "k0",                           new String[]{}                       },
				{"{\"k0.k1\":\"v0\"}",                                  "k1",                           new String[]{}                       },//25
				{"{\"k0.k1\":\"v0\"}",                                  "k0.k1",                        new String[]{}                       },

				// key with dot ambiguity
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0",                           new String[]{"k0"}                   },
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k1",                           new String[]{}                       },
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0.k1",                        new String[]{"k0.k1"}                },
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1",                        new String[]{"k0.k1"}                },//30
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1.k2",                     new String[]{"k0.k1.k2"}             },
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k1.k2",                        new String[]{}                       },
				{"{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k0",                           new String[]{"k0"}                   },
				{"{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k1.k2",                        new String[]{"k1.k2"}                },

				//ignore non-existent keys but keep good keys
				{"{\"k0\":\"v0\",\"k1\":\"v1\"}",                      new String[]{"k0","k2"},         new String[]{"k0"}                   },//35
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k2"},         new String[]{"k0"}                   },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2"},      new String[]{"k0", "k1.k2"}          },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2.k3"},   new String[]{"k0"}                   },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2","k1"}, new String[]{"k0","k1","k1.k2"}      },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1","k0.k2"}, new String[]{"k0","k1"}              },//40
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1","k2"},    new String[]{"k0","k1"}              },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k2"},           new String[]{"k1.k2"}                },

				//arrays - key inside array treated as child
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0",                           new String[]{"k0"}                   },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1",                        new String[]{"k0.k1"}                },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1.k2",                     new String[]{"k0.k1.k2"}             },//45
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}", "k0.k1.k2",                     new String[]{"k0.k1.k2", "k0.k1.k2"} },
		});
	}

	@Test
	public void test() throws ParseException
	{
		JSONObject objectToSearch = jsonToSearch != null ? (JSONObject) JSONValue.parseWithException(jsonToSearch) : null;
		PathLocator locator = switchKeyToRemove();
		List<String> found = locator.locate(objectToSearch);
		assertEquals(Arrays.asList(expectedFound), found);
	}

	private PathLocator switchKeyToRemove()
	{
		long m = System.currentTimeMillis();
		if (keysToFind == null && m % 4 == 0) {
			// System.out.println("cast to String");
			return new PathLocator((String)null);
		} else if (keysToFind == null && m % 4 == 1) {
			// System.out.println("cast to String[]");
			return new PathLocator((String[])null);
		} else if (keysToFind == null && m % 4 == 2) {
			// System.out.println("cast to JSONArray");
			return new PathLocator((JSONArray)null);
		} else if (keysToFind == null && m % 4 == 3) {
			// System.out.println("cast to List<String>");
			return new PathLocator((List<String>)null);
		} else if (keysToFind instanceof String) {
			return new PathLocator((String) keysToFind);
		} else if (keysToFind instanceof String[]) {
			return new PathLocator((String[]) keysToFind);
		} else if (keysToFind instanceof JSONArray) {
			return new PathLocator((JSONArray) keysToFind);
		} else if (keysToFind instanceof List<?>) {
			return new PathLocator((List<String>) keysToFind);
		} else {
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}
}