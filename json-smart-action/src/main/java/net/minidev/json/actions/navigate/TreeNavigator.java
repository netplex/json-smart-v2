package net.minidev.json.actions.navigate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.TreePath;

/**
 * <b>Navigates only the branches of a {@link JSONObject} corresponding to the paths specified.</b>
 *
 * <p>For each specified path to navigate, the {@link TreeNavigator} only traverses the matching
 * branch.
 *
 * <p>The navigator accepts an action and provides callback hooks for it to act on the traversed
 * nodes at each significant step. See {@link NavigateAction}.
 *
 * <p>See package-info for more details
 *
 * <p><b>Example:</b>
 *
 * <p>To navigate the branch k1.k2 of the object {"k1":{"k2":"v1"}, "k3":{"k4":"v2"}} instantiate
 * the navigator like so: new JSONNavigator("k1.k2")
 *
 * @author adoneitan@gmail.com
 * @since 15 June 2016.
 */
public class TreeNavigator<M extends Map<String, Object>, L extends List<Object>> {
  protected List<String> pathsToNavigate;
  protected NavigateAction<M, L> action;
  protected String pathPrefix = "";

  public TreeNavigator(NavigateAction<M, L> action, List<String> pathsToNavigate) {
    if (action == null) {
      throw new IllegalArgumentException("NavigateAction cannot be null");
    }
    this.action = action;
    this.pathsToNavigate = pathsToNavigate;
  }

  public TreeNavigator<M, L> with(String pathPrefix) {
    this.pathPrefix = pathPrefix;
    return this;
  }

  public TreeNavigator(NavigateAction<M, L> action, String... pathsToNavigate) {
    this(action, Arrays.asList(pathsToNavigate));
  }

  public void nav(M object) throws Exception {
    if (action.start(object, pathsToNavigate)) {
      for (String path : pathsToNavigate) {
        try {
          if (path != null && !path.equals("") && action.pathStart(path)) {
            TreePath jp =
                new TreePath(path, new DotDelimiter().withAcceptDelimiterInNodeName(true));
            nav(jp, object);
            action.pathEnd(path);
          }
        } catch (Exception e) {
          if (action.failSilently(path, e)) {
            break;
          } else if (action.failFast(path, e)) {
            throw e;
          }
        }
      }
    }
    action.end();
  }

  @SuppressWarnings("unchecked")
  public void nav(TreePath jp, M map) {
    if (map == null || !action.recurInto(jp, map)) {
      // source is null - navigation impossible
      return;
    }

    if (jp.hasNext()) {
      String key = jp.next();
      if (!map.containsKey(key)) {
        // cannot find next element of path in the source -
        // the specified path does not exist in the source
        action.pathTailNotFound(jp, map);
      } else if (map.get(key) instanceof Map) {
        // reached Map type node - handle it and recur into it
        nav(jp, (M) map.get(key));
      } else if (map.get(key) instanceof List) {
        // reached List type node - handle it and recur into it
        nav(jp, (L) map.get(key));
      } else if (jp.hasNext()) {
        // reached leaf node (not a container) in source but specified path expects children -
        // the specified path is illegal because it does not exist in the source.
        action.foundLeafBeforePathEnd(jp, map.get(key));
      } else if (!jp.hasNext()) {
        // reached leaf in source and specified path is also at leaf -> handle it
        action.handleLeaf(jp, map.get(key));
      } else {
        throw new IllegalStateException("fatal: unreachable code reached at '" + jp.origin() + "'");
      }
    }
    action.recurEnd(jp, (M) map);
  }

  @SuppressWarnings("unchecked")
  public void nav(TreePath jp, L list) {
    if (list == null || !action.recurInto(jp, (L) list)) {
      // list is null - navigation impossible
      return;
    }
    int arrIndex = 0;
    for (Object arrItem : list.toArray()) {
      if (arrItem instanceof Map) {
        // clone the path so that for each object in the array,
        // the iterator continues from the same position in the path
        TreePath jpClone = getClone(jp);
        nav(jpClone, (M) arrItem);
      } else if (arrItem instanceof List) {
        nav(jp, (L) arrItem);
      } else if (!jp.hasNext()) {
        // reached leaf - handle it
        action.handleLeaf(jp, arrIndex, arrItem);
      }
      arrIndex++;
    }
    action.recurEnd(jp, (L) list);
  }

  private TreePath getClone(TreePath jp) {
    try {
      return jp.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("failed to clone path", e);
    }
  }
}
