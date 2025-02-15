package net.minidev.json.actions.traverse;

import java.util.Map;
import java.util.Map.Entry;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * <b>Removes key:value elements from a {@link JSONObject}.</b>
 *
 * <p>An element is not removed from the user-specified list once its processing is over, because it
 * may appear in more than one node.
 *
 * <p>See package-info for more details
 *
 * <p>See unit tests for examples
 *
 * @author adoneitan@gmail.com
 */
public class RemoveElementsJsonAction implements JSONTraverseAction {
  protected JSONObject result;
  protected final Map<String, Object> elementsToRemove;
  protected final boolean allowDotChar;

  public RemoveElementsJsonAction(Map<String, Object> elementsToRemove, boolean allowDotChar) {
    this.elementsToRemove = elementsToRemove;
    this.allowDotChar = allowDotChar;
  }

  public RemoveElementsJsonAction(Map<String, Object> elementsToRemove) {
    this(elementsToRemove, false);
  }

  @Override
  public boolean start(JSONObject object) {
    result = object;
    return object != null && elementsToRemove != null && elementsToRemove.size() > 0;
  }

  @Override
  public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry) {
    return elementsToRemove.entrySet().contains(entry);
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
