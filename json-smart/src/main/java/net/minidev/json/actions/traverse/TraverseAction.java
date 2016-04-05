package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * An interface for a processing action on the nodes of a {@link JSONObject} while traversing it.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public interface TraverseAction
{
	/**
	 * called before any traversal of the {@link JSONObject} starts
	 * @return true if traversal should start at all
	 */
	boolean handleStart(JSONObject object);

	/**
	 * @return false if encountering a key containing a '.' char on the path should abort the traversal of the path
	 */
	boolean handleDotChar();

	/**
	 * called for each entry in given level of the {@link JSONObject}
	 * @return false if children of the specified entry should not be traversed
	 */
	boolean handleEntryAndIgnoreChildren(String fullPathToObject, Iterator<Map.Entry<String, Object>> it, Map.Entry<String, Object> entry);

	/**
	 * called before the next entry in a given level of the {@link JSONObject} is processed
	 * @return true if siblings of the last entry should be processed
	 */
	boolean handleNext();

	/**
	 * called if the child of the currently processed entry is a {@link JSONObject}
	 * @return true if a {@link JSONObject} child of the current entry should be processed
	 */
	boolean handleJSONObjectChild();

	/**
	 * called if the child of the currently processed entry is a {@link JSONArray}
	 * @return true if a {@link JSONArray} child of the current entry should be processed
	 */
	boolean handleJSONArrayChild();

	/**
	 * called after the traversal ends, and just before the traversal method exits
	 */
	void handleEnd();

	/**
	 * holds the result of the traversal, as assigned by the action implementing this interface
	 */
	Object result();
}
