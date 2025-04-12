package net.minidev.json.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.navigate.CopyPathsAction;
import net.minidev.json.actions.navigate.JSONNavigator;

/**
 * <b>Creates a copy of a {@link JSONObject} consisting only of the nodes on the user-specified
 * paths.</b>
 *
 * <p>Paths that do not exist in the specified object are ignored silently. Specifying an empty list
 * of paths to copy or only non-existent paths will result in an empty object being returned.
 *
 * <p>A path to copy must be specified in the n-gram format - a list of keys from the root down
 * separated by dots: K0[[[[.K1].K2].K3]...] <br>
 * A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot in
 * their name are not supported.
 *
 * <p><b> Sample usage:</b>
 *
 * <p>To replicate the branch k1.k2 from {k1:{k2:v2}, k3:{k4:v4}} use the {@link PathReplicator}
 * like so:
 *
 * <pre>
 * PathReplicator pr = new {@link PathReplicator}("k1.k2")
 * JSONObject copiedObject = pr.copy(new JSONObject(...))
 * </pre>
 *
 * The resulting object 'copiedObject' would be {k1:{k2:v2}}
 *
 * <p>see unit tests for more examples
 *
 * @author adoneitan@gmail.com
 * @since 15 March 2016.
 */
public class PathReplicator {
  protected List<String> pathsToCopy;

  public PathReplicator(JSONArray<Object> pathsToCopy) {
    if (pathsToCopy == null || pathsToCopy.isEmpty()) {
      this.pathsToCopy = Collections.emptyList();
    } else {
      this.pathsToCopy = new LinkedList<String>();
      for (Object s : pathsToCopy) {
        this.pathsToCopy.add((String) s);
      }
    }
  }

  public PathReplicator(List<String> pathsToCopy) {
    this.pathsToCopy =
        pathsToCopy == null || pathsToCopy.size() == 0
            ? Collections.<String>emptyList()
            : pathsToCopy;
  }

  public PathReplicator(String... pathsToCopy) {
    this.pathsToCopy =
        pathsToCopy == null || pathsToCopy.length == 0
            ? Collections.<String>emptyList()
            : new LinkedList<String>(Arrays.asList(pathsToCopy));
  }

  public JSONObject replicate(JSONObject sourceObj) throws Exception {
    CopyPathsAction s = new CopyPathsAction();
    JSONNavigator n = new JSONNavigator(s, pathsToCopy);
    n.nav(sourceObj);
    return (JSONObject) s.result();
  }
}
