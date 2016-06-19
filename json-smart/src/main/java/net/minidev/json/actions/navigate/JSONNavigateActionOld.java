package net.minidev.json.actions.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.TreePath;

import java.util.Collection;

/**
 * An interface for a processing action on the nodes of a {@link JSONObject} while navigating its branches.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public interface JSONNavigateActionOld
{
	/**
	 * called before any navigation of the {@link JSONObject} starts
	 * @return true if navigation should start at all
	 */
	boolean handleNavigationStart(JSONObject objectToNavigate, Collection<String> pathsToNavigate);

	/**
	 * called before navigation of a new path starts
	 * @return true if the specified path should be navigated
	 */
	boolean handleNextPath(String path);

	/**
	 * reached end of branch in source before end of specified json path -
	 * the specified path does not exist in the source.
	 */
	void handlePrematureNavigatedBranchEnd(TreePath jp, Object source);

	/**
	 * called after the navigation of a path ends
	 */
	void handlePathEnd(String path);

	/**
	 * called if navigation of a path throws an exception
	 * @return true if the failure on this path should not abort the rest of the navigation
	 */
	boolean failPathSilently(String path, Exception e);

	/**
	 * called if navigation of a path throws an exception
	 * @return true if the failure on this path should abort the rest of the navigation
	 */
	boolean failPathFast(String path, Exception e);

	/**
	 * called when an object node is encountered on the path
	 * @return true if the navigator should navigate into the object
	 */
	boolean handleJSONObject(TreePath jp, JSONObject sourceNode);

	/**
	 * called when an array node is encountered on the path
	 * @return true if the navigator should navigate into the array
	 */
	boolean handleJSONArrray(TreePath jp, JSONArray sourceNode);

	/**
	 * called when a leaf node is reached in a JSONObject.
	 * a leaf in a JSONObject is a key-value pair where the value is not a container itself
	 * (it is not a JSONObject nor a JSONArray)
	 * @param jp - the JsonPath pointing to the leaf
	 */
	void handleJSONObjectLeaf(TreePath jp, Object value);

	/**
	 * called when a leaf in a JSONArray is reached.
	 * a leaf in a JSONArray is a non-container item
	 * (it is not a JSONObject nor a JSONArray)
	 * @param arrIndex - the index of the item in the JSONArray
	 * @param arrItem - the item
	 */
	void handleJSONArrayLeaf(int arrIndex, Object arrItem);

	/**
	 * called after all the items of an array have been visited
	 * @param jp - the JsonPath pointing to the array
	 */
	void handleJSONArrayEnd(TreePath jp);

	/**
	 * called after all the entries of a JSONObject have been visited
	 * @param jp - the JsonPath pointing to the object
	 */
	void handleObjectEnd(TreePath jp);

	/**
	 * called after all navigation ends, and just before the navigation method exits
	 */
	void handleNavigationEnd();

	/**
	 * holds the result of the navigation, as assigned by the action implementing this interface
	 */
	Object result();
}
