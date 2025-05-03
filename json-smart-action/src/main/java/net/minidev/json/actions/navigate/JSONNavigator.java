package net.minidev.json.actions.navigate;

import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author adoneitan@gmail.com
 * @since 15 June 2016.
 */
public class JSONNavigator extends TreeNavigator<JSONObject<Object>, JSONArray<Object>> {

  public JSONNavigator(JSONNavigateAction action, List<String> pathsToNavigate) {
    super(action, pathsToNavigate);
  }

  public JSONNavigator(JSONNavigateAction action, String... pathsToNavigate) {
    super(action, pathsToNavigate);
  }
}
