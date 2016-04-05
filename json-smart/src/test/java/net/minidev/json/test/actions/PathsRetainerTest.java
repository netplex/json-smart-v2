package net.minidev.json.test.actions;

import net.minidev.json.actions.PathsRetainer;
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

/**
 * @author adoneitan@gmail.com
 */
@RunWith(Parameterized.class)
public class PathsRetainerTest
{
	private String jsonToReduce;
	private Object keyToKeep;
	private String expectedReducedJson;

	public PathsRetainerTest(String jsonToReduce, Object keyToKeep, String expectedReducedJson)
	{
		this.jsonToReduce = jsonToReduce;
		this.keyToKeep = keyToKeep;
		this.expectedReducedJson = expectedReducedJson;
	}

	@Parameterized.Parameters
	public static Collection params()
	{
		return Arrays.asList(new Object[][]{

				//nulls, bad/empty keys
				{null,                                                  null,                           null                                                  },
				{null,                                                  "",                             null                                                  },
				{null,                                                  "k1",                           null                                                  },
				{null,                                                  new String[]{},                 null                                                  },
				{null,                                                  new JSONArray(),                null                                                  },
				{null,                                                  new ArrayList<String>(0),       null                                                  },//5

				//empty json, bad/empty keys
				{"{}",                                                  null,                           "{}"                                                  },
				{"{}",                                                  "",                             "{}"                                                  },
				{"{}",                                                  "k1",                           "{}"                                                  },
				{"{}",                                                  new String[]{},                 "{}"                                                  },
				{"{}",                                                  new JSONArray(),                "{}"                                                  },
				{"{}",                                                  new ArrayList<String>(0),       "{}"                                                  },//11

				//simple json, bad/empty keys
				{"{\"k0\":\"v0\"}",                                     null,                           "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     "",                             "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     "k1",                           "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     new String[]{},                 "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     new JSONArray(),                "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     new ArrayList<String>(0),       "{}"                                                  },//17

				//simple json, valid/invalid keys
				{"{\"k0\":\"v0\"}",                                     "k0",                           "{\"k0\":\"v0\"}"                                     },
				{"{\"k0\":\"v0\"}",                                     "v0",                           "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     "k0.k1",                        "{}"                                                  },
				{"{\"k0\":\"v0\"}",                                     "k1.k0",                        "{}"                                                  },
				{"{\"k0\":null}",                                       "k0",                           "{\"k0\":null}"                                       },
				{"{\"k0\":null}",                                       "v0",                           "{}"                                                  },//23

				//key with dot char
				{"{\"k0.k1\":\"v0\"}",                                  "k0",                           "{}"                                                  },
				{"{\"k0.k1\":\"v0\"}",                                  "k1",                           "{}"                                                  },
				{"{\"k0.k1\":\"v0\"}",                                  "k0.k1",                        "{}"                                                  },

				// key with dot ambiguity
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0",                           "{\"k0\":{}}"                                         },//27
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k1",                           "{}"                                                  },
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0.k1",                        "{\"k0\":{\"k1\":null}}"                              },
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1",                        "{\"k0\":{\"k1\":{}}}"                                },
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k0.k1.k2",                     "{\"k0\":{\"k1\":{\"k2\":null}}}"                     },
				{"{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",   "k1.k2",                        "{}"                                                  },
				{"{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k0",                           "{\"k0\":{}}}"                                        },
				{"{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}","k1.k2",                        "{\"k1\":{\"k2\":\"v2\"}}}"                           },
				{"{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2.k3.k4",                  "{}"                                                  },
				{"{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2.k3",                     "{}"                                                  },
				{"{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0.k2",                        "{\"k0\":{\"k2\":{}}}"                                },
				{"{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0",                           "{\"k0\":{}}"                                         },

				//ignore non-existent keys but keep good keys
				{"{\"k0\":\"v0\",\"k1\":\"v1\"}",                      new String[]{"k0","k2"},         "{\"k0\":\"v0\"}"                                     },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k2"},         "{\"k0\":\"v0\"}"                                     },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2"},      "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}"              },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k0.k2"},      "{\"k0\":\"v0\"}"              },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1.k2","k1"}, "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}"              },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1"},         "{\"k0\":\"v0\",\"k1\":{}}"                           },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0","k1","k2"},    "{\"k0\":\"v0\",\"k1\":{}}"                           },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k2"},           "{\"k1\":{\"k2\":\"v2\"}}"                            },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k2.k3"},        "{}"                                                  },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0.k1.k2"},        "{}"                                                  },
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k0"},           "{}"                                                  },

				//arrays - key inside array treated as child
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0",                           "{\"k0\":{}}"                                         },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1",                        "{\"k0\":{\"k1\":[1,{},3,4]}}"                        },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",           "k0.k1.k2",                     "{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}"           },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}", "k0.k1",                        "{\"k0\":{\"k1\":[{},{}]}}"                           },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}", "k0.k1.k2",                     "{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}" },
		});
	}

	@Test
	public void test() throws ParseException
	{
		JSONObject objectToReduce = jsonToReduce != null ? (JSONObject) JSONValue.parseWithException(jsonToReduce) :null;
		JSONObject expectedReducedObj = expectedReducedJson != null ? (JSONObject) JSONValue.parseWithException(expectedReducedJson):null;
		PathsRetainer reducer = switchKeyToRemove();
		JSONObject reducedObj = reducer.retain(objectToReduce);
		assertEquals(expectedReducedObj, reducedObj);
	}

	private PathsRetainer switchKeyToRemove()
	{
		long m = System.currentTimeMillis();
		if (keyToKeep == null && m % 4 == 0)
		{
			System.out.println("cast to String");
			return new PathsRetainer((String)null);
		}
		else if (keyToKeep == null && m % 4 == 1)
		{
			System.out.println("cast to String[]");
			return new PathsRetainer((String[])null);
		}
		else if (keyToKeep == null && m % 4 == 2)
		{
			System.out.println("cast to JSONArray");
			return new PathsRetainer((JSONArray)null);
		}
		else if (keyToKeep == null && m % 4 == 3)
		{
			System.out.println("cast to List<String>");
			return new PathsRetainer((List<String>)null);
		}
		else if (keyToKeep instanceof String)
		{
			return new PathsRetainer((String) keyToKeep);
		}
		else if (keyToKeep instanceof String[])
		{
			return new PathsRetainer((String[]) keyToKeep);
		}
		else if (keyToKeep instanceof JSONArray)
		{
			return new PathsRetainer((JSONArray) keyToKeep);
		}
		else if (keyToKeep instanceof List<?>)
		{
			return new PathsRetainer((List<String>) keyToKeep);
		}
		else
		{
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}
}