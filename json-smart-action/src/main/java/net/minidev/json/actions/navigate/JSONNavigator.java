package net.minidev.json.actions.navigate;

import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * A navigator that operates on JSON objects and arrays for path-based navigation.
 *
 * @author adoneitan@gmail.com
 * @since 15 June 2016.
 */
public class JSONNavigator extends TreeNavigator<JSONObject, JSONArray> {

  /** Creates a navigator with the specified action and paths */
  public JSONNavigator(JSONNavigateAction action, List<String> pathsToNavigate) {
    super(action, pathsToNavigate);
  }

  /** Creates a navigator with the specified action and paths */
  public JSONNavigator(JSONNavigateAction action, String... pathsToNavigate) {
    super(action, pathsToNavigate);
  }
}
