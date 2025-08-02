package net.minidev.json.actions.traverse;

import java.util.Map.Entry;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * A traverse action that prints keys during JSON traversal.
 *
 * @author adoneitan@gmail.com
 * @since 5/24/16.
 */
public class KeysPrintAction implements JSONTraverseAction {
  @Override
  public boolean start(JSONObject object) {
    return true;
  }

  @Override
  public boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry) {
    // System.out.println(entry.getKey());
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
  public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry) {
    return false;
  }

  @Override
  public void end() {}

  @Override
  public Object result() {
    return null;
  }
}
