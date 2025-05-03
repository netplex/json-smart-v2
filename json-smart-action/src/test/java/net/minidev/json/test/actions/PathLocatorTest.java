package net.minidev.json.test.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.actions.PathLocator;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author adoneitan@gmail.com
 */
// @ExtendWith(Parameterized.class)
public class PathLocatorTest {

  public static Stream<Arguments> params() {
    return Stream.of(
        // nulls, bad/empty keys
        Arguments.of(null, null, new String[] {}),
        Arguments.of(null, "", new String[] {}),
        Arguments.of(null, "k1", new String[] {}),
        Arguments.of(null, new String[] {}, new String[] {}),
        Arguments.of(null, new JSONArray(), new String[] {}),
        Arguments.of(null, new ArrayList<String>(0), new String[] {}), // 5
        // empty json, bad/empty keys
        Arguments.of("{}", null, new String[] {}),
        Arguments.of("{}", "", new String[] {}),
        Arguments.of("{}", "k1", new String[] {}),
        Arguments.of("{}", new String[] {}, new String[] {}),
        Arguments.of("{}", new JSONArray(), new String[] {}), // 10
        Arguments.of("{}", new ArrayList<String>(0), new String[] {}),
        // simple json, bad/empty keys
        Arguments.of("{\"k0\":\"v0\"}", null, new String[] {}),
        Arguments.of("{\"k0\":\"v0\"}", "", new String[] {}),
        Arguments.of("{\"k0\":\"v0\"}", "k1", new String[] {}),
        Arguments.of("{\"k0\":\"v0\"}", new String[] {}, new String[] {}), // 15
        Arguments.of("{\"k0\":\"v0\"}", new JSONArray(), new String[] {}),
        Arguments.of("{\"k0\":\"v0\"}", new ArrayList<String>(0), new String[] {}),
        // simple json, valid/invalid keys
        Arguments.of("{\"k0\":\"v0\"}", "k0", new String[] {"k0"}),
        Arguments.of("{\"k0\":\"v0\"}", "v0", new String[] {}),
        Arguments.of("{\"k0\":\"v0\"}", "k0.k1", new String[] {}), // 20
        Arguments.of("{\"k0\":\"v0\"}", "k1.k0", new String[] {}),
        Arguments.of("{\"k0\":null}", "k0", new String[] {"k0"}),
        Arguments.of("{\"k0\":null}", null, new String[] {}),
        // key with dot char
        Arguments.of("{\"k0.k1\":\"v0\"}", "k0", new String[] {}),
        Arguments.of("{\"k0.k1\":\"v0\"}", "k1", new String[] {}), // 25
        Arguments.of("{\"k0.k1\":\"v0\"}", "k0.k1", new String[] {}),
        // key with dot ambiguity
        Arguments.of("{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}", "k0", new String[] {"k0"}),
        Arguments.of("{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}", "k1", new String[] {}),
        Arguments.of(
            "{\"k0.k1\":\"withDot\",\"k0\":{\"k1\":null}}", "k0.k1", new String[] {"k0.k1"}),
        Arguments.of(
            "{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",
            "k0.k1",
            new String[] {"k0.k1"}), // 30
        Arguments.of(
            "{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}",
            "k0.k1.k2",
            new String[] {"k0.k1.k2"}),
        Arguments.of("{\"k0\":{\"k1.k2\":\"dot\",\"k1\":{\"k2\":null}}}", "k1.k2", new String[] {}),
        Arguments.of(
            "{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}", "k0", new String[] {"k0"}),
        Arguments.of(
            "{\"k0\":{\"k1.k2\":\"dot\"},\"k1\":{\"k2\":\"v2\"}}}",
            "k1.k2",
            new String[] {"k1.k2"}),
        // ignore non-existent keys but keep good keys
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":\"v1\"}", new String[] {"k0", "k2"}, new String[] {"k0"}), // 35
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k2"},
            new String[] {"k0"}),
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k1.k2"},
            new String[] {"k0", "k1.k2"}),
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k1.k2.k3"},
            new String[] {"k0"}),
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k1.k2", "k1"},
            new String[] {"k0", "k1", "k1.k2"}),
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k1", "k0.k2"},
            new String[] {"k0", "k1"}), // 40
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k0", "k1", "k2"},
            new String[] {"k0", "k1"}),
        Arguments.of(
            "{\"k0\":\"v0\",\"k1\":{\"k2\":\"v2\"}}",
            new String[] {"k1.k2"},
            new String[] {"k1.k2"}),
        // arrays - key inside array treated as child
        Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}", "k0", new String[] {"k0"}),
        Arguments.of("{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}", "k0.k1", new String[] {"k0.k1"}),
        Arguments.of(
            "{\"k0\":{\"k1\":[1,{\"k2\":\"v2\"},3,4]}}",
            "k0.k1.k2",
            new String[] {"k0.k1.k2"}), // 45
        Arguments.of(
            "{\"k0\":{\"k1\":[{\"k2\":\"v2\"},{\"k2\":\"v2\"}]}}",
            "k0.k1.k2",
            new String[] {"k0.k1.k2", "k0.k1.k2"}));
  }

  @ParameterizedTest
  @MethodSource("params")
  public void test(String jsonToSearch, Object keysToFind, String[] expectedFound)
      throws ParseException {
    JSONObject objectToSearch =
        jsonToSearch != null ? (JSONObject) JSONValue.parseWithException(jsonToSearch) : null;
    PathLocator locator = switchKeyToRemove(keysToFind);
    List<String> found = locator.locate(objectToSearch);
    assertEquals(Arrays.asList(expectedFound), found);
  }

  @SuppressWarnings("unchecked")
  private PathLocator switchKeyToRemove(Object keysToFind) {
    long m = System.currentTimeMillis();
    if (keysToFind == null && m % 4 == 0) {
      // System.out.println("cast to String");
      return new PathLocator((String) null);
    } else if (keysToFind == null && m % 4 == 1) {
      // System.out.println("cast to String[]");
      return new PathLocator((String[]) null);
    } else if (keysToFind == null && m % 4 == 2) {
      // System.out.println("cast to JSONArray");
      return new PathLocator((JSONArray<Object>) null);
    } else if (keysToFind == null && m % 4 == 3) {
      // System.out.println("cast to List<String>");
      return new PathLocator((List<String>) null);
    } else if (keysToFind instanceof String) {
      return new PathLocator((String) keysToFind);
    } else if (keysToFind instanceof String[]) {
      return new PathLocator((String[]) keysToFind);
    } else if (keysToFind instanceof JSONArray) {
      return new PathLocator((JSONArray<Object>) keysToFind);
    } else if (keysToFind instanceof List<?>) {
      return new PathLocator((List<String>) keysToFind);
    } else {
      throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
    }
  }
}
