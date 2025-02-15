package net.minidev.json.actions.traverse;

import java.util.List;
import java.util.Map.Entry;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * <b>Removes branches from a {@link JSONObject}.</b>
 *
 * <p>A path is not removed from the user-specified list once its processing is over, because
 * identical objects in the same array are supported by this action.
 *
 * <p>See package-info for more details
 *
 * <p>See unit tests for examples
 *
 * @author adoneitan@gmail.com
 */
public class RemovePathsJsonAction implements JSONTraverseAction {
  protected JSONObject result;
  protected List<String> pathsToRemove;

  public RemovePathsJsonAction(List<String> pathsToRemove) {
    this.pathsToRemove = pathsToRemove;
  }

  @Override
  public boolean start(JSONObject object) {
    result = object;
    return object != null && pathsToRemove != null && pathsToRemove.size() > 0;
  }

  @Override
  public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry) {
    return pathsToRemove.contains(fullPathToEntry);
  }

  @Override
  public boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry) {
    // must traverse the whole object
    return true;
  }

  @Override
  public boolean recurInto(String pathToEntry, JSONObject entryValue) {
    return true;
  }

  @Override
  public boolean recurInto(String pathToEntry, JSONArray entryValue) {
    return true;
  }

  @Override
  public void handleLeaf(String pathToEntry, Entry<String, Object> entry) {}

  @Override
  public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem) {}

  @Override
  public void end() {
    // nothing to do
  }

  @Override
  public Object result() {
    return result;
  }
}
