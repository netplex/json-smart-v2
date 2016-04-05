package net.minidev.json.test.actions;

import net.minidev.json.actions.PathReplicator;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
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
public class PathReplicatorTest
{
	private String jsonSource;
	private Object pathsToCopy;
	private Object expected;

	public PathReplicatorTest(String jsonSource, Object pathsToCopy, Object expected)
	{
		this.jsonSource = jsonSource;
		this.pathsToCopy = pathsToCopy;
		this.expected = expected;
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
				{"{\"k0\":\"v0\"}",                                     "k0.k1",                        "{}"                                                  },//20
				{"{\"k0\":\"v0\"}",                                     "k1.k0",                        "{}"                                                  },
				{"{\"k0\":null}",                                       "k0",                           "{\"k0\":null}"                                       },
				{"{\"k0\":null}",                                       "v0",                           "{}"                                                  },

				//key with dot char
				{"{\"k0.k1\":\"v0\"}",                                  "k0",                           "{}"                                                  },
				{"{\"k0.k1\":\"v0\"}",                                  "k1",                           "{}"                                                  },
				{"{\"k0.k1\":\"v0\"}",                                  "k0.k1",                        "{}"                                                  },

				// key with dot ambiguity
				{"{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}",        "k0",                           "{\"k0\":{}}"                                         },
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
				{"{\"k0\":{\"k1\":\"v1\",\"k2\":{\"k3.k4\":\"dot\"}}}", "k0",                           "{\"k0\":{}}"                                         },//38

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
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k0.k1.k2"},        "{}"                                                  },//48
				{"{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",             new String[]{"k1.k0"},           "{}"                                                  },

				//arrays - key inside array treated as child
				{"{\"k0\":{\"k1\":[1,2,3,4]}}",                                            "k0",                                 "{\"k0\":{}}"                                              },
				{"{\"k0\":{\"k1\":[1,2,3,4]}}",                                            "k0.k1",                              "{\"k0\":{\"k1\":[1,2,3,4]}}"                              },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                              "k0",                                 "{\"k0\":{}}"                                              },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                              "k0.k1",                              "{\"k0\":{\"k1\":[1,{},3,4]}}"                             },
				{"{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",                              "k0.k1.k2",                           "{\"k0\":{\"k1\":[{\"k2\":\"v2\"}]}}"                },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}",                    "k0.k1",                              "{\"k0\":{\"k1\":[{},{}]}}"                                },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}",                    "k0.k1.k2",                           "{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}"      },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":\"v4\"}]}}",           "k0.k1.k2",                           "{\"k0\":{\"k1\":[{\"k2\":\"v2\"}]}}"                      },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":\"v4\"}]}}",           "k0.k3.k4",                           "{\"k0\":{\"k3\":[{\"k4\":\"v4\"}]}}"                      },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}",  "k0.k1.k2",                           "{\"k0\":{\"k1\":[{\"k2\":\"v2\"}]}}"                      },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}",  "k0.k3.k4",                           "{\"k0\":{\"k3\":[{\"k4\":{}}]}}"                          },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}",  "k0.k3.k4.k5",                        "{\"k0\":{\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}"             },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}",  new String[]{"k0.k1", "k0.k3"},       "{\"k0\":{\"k3\":[{}],\"k1\":[{}]}}"                       },
				{"{\"k0\":{\"k1\":[{\"k2\":\"v2\"}],\"k3\":[{\"k4\":{\"k5\":\"v5\"}}]}}",  new String[]{"k0.k1", "k0.k3.k4.k5"}, "{\"k0\":{\"k3\":[{\"k4\":{\"k5\":\"v5\"}}],\"k1\":[{}]}}" },
		});
	}

	@Test
	public void test() throws Exception
	{
		JSONObject objectSource = jsonSource != null ? (JSONObject) JSONValue.parseWithException(jsonSource) :null;
		PathReplicator copier = switchKeyToCopy();
		JSONObject copied = copier.replicate(objectSource);
		JSONObject expectedObj = expected != null ? (JSONObject) JSONValue.parseWithException((String) expected) : null;
		assertEquals(expectedObj, copied);
	}

	@Test
	public void test2() throws Exception
	{
		JSONObject objectSource = jsonSource != null ? (JSONObject) JSONValue.parseWithException(jsonSource) :null;
		PathReplicator copier = switchKeyToCopy2();
		JSONObject copied = copier.replicate(objectSource);
		JSONObject expectedObj = expected != null ? (JSONObject) JSONValue.parseWithException((String) expected) : null;
		assertEquals(expectedObj, copied);
	}

	private PathReplicator switchKeyToCopy()
	{
		long m = System.currentTimeMillis();
		if (pathsToCopy == null && m % 4 == 0)
		{
			System.out.println("cast to String");
			return new PathReplicator((String)null);
		}
		else if (pathsToCopy == null && m % 4 == 1)
		{
			System.out.println("cast to String[]");
			return new PathReplicator((String[])null);
		}
		else if (pathsToCopy == null && m % 4 == 2)
		{
			System.out.println("cast to JSONArray");
			return new PathReplicator((JSONArray)null);
		}
		else if (pathsToCopy == null && m % 4 == 3)
		{
			System.out.println("cast to List<String>");
			return new PathReplicator((List<String>)null);
		}
		else if (pathsToCopy instanceof String)
		{
			return new PathReplicator((String) pathsToCopy);
		}
		else if (pathsToCopy instanceof String[])
		{
			return new PathReplicator((String[]) pathsToCopy);
		}
		else if (pathsToCopy instanceof JSONArray)
		{
			return new PathReplicator((JSONArray) pathsToCopy);
		}
		else if (pathsToCopy instanceof List<?>)
		{
			return new PathReplicator((List<String>) pathsToCopy);
		}
		else
		{
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}

	private PathReplicator switchKeyToCopy2()
	{
		long m = System.currentTimeMillis();
		if (pathsToCopy == null && m % 4 == 0)
		{
			System.out.println("cast to String");
			return new PathReplicator((String)null);
		}
		else if (pathsToCopy == null && m % 4 == 1)
		{
			System.out.println("cast to String[]");
			return new PathReplicator((String[])null);
		}
		else if (pathsToCopy == null && m % 4 == 2)
		{
			System.out.println("cast to JSONArray");
			return new PathReplicator((JSONArray)null);
		}
		else if (pathsToCopy == null && m % 4 == 3)
		{
			System.out.println("cast to List<String>");
			return new PathReplicator((List<String>)null);
		}
		else if (pathsToCopy instanceof String)
		{
			return new PathReplicator((String) pathsToCopy);
		}
		else if (pathsToCopy instanceof String[])
		{
			return new PathReplicator((String[]) pathsToCopy);
		}
		else if (pathsToCopy instanceof JSONArray)
		{
			return new PathReplicator((JSONArray) pathsToCopy);
		}
		else if (pathsToCopy instanceof List<?>)
		{
			return new PathReplicator((List<String>) pathsToCopy);
		}
		else
		{
			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
		}
	}

}