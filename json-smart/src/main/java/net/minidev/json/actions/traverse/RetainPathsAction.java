package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <b>Retain branches or parts of branches matching a specified list of paths.</b>
 * <p>
 * Paths are matched from the root down. If a user-specified path ends at a non-leaf node,
 * the rest of the branch from that node to the leaf is not retained.
 * <p>
 * A path is not removed from the user-specified list once its processing is over,
 * because identical objects in the same array are supported by this action.
 * <p>
 * See package-info for more details
 * <p>
 * See unit tests for examples
 *
 * @author adoneitan@gmail.com
 *
 */
public class RetainPathsAction implements TraverseAction
{
	protected JSONObject result;
	protected List<String> pathsToRetain;

	public RetainPathsAction(List<String> pathsToRetain)
	{
		this.pathsToRetain = new ArrayList<String>(pathsToRetain);
	}

	@Override
	public boolean handleStart(JSONObject object)
	{
		if (object == null)
		{
			result = null;
			return false;
		}
		if (pathsToRetain == null || pathsToRetain.size() == 0)
		{
			result = new JSONObject();
			return false;
		}
		result = object;
		return true;
	}

	@Override
	public boolean handleEntryAndIgnoreChildren(String pathToEntry, Iterator<Map.Entry<String, Object>> it, Map.Entry<String, Object> entry)
	{
		/**
		 * if the full path to the entry is not contained in any of the paths to retain - remove it from the object
		 *  this step does not remove entries whose full path is contained in a path to retain but are not equal to an
		 *  entry to retain
		 */
		if (!foundStartsWith(pathToEntry) || entry.getKey().contains("."))
		{
			it.remove();
			//the entry has been removed from the traversal iterator, no point in traversing its children
			return true;
		}
		return false;
	}

	@Override
	public boolean handleDotChar()
	{
		//need to reach handleEntryAndIgnoreChildren() in order to remove the path containing the dot char
		return true;
	}

	@Override
	public boolean handleNext()
	{
		//must traverse the whole object
		return true;
	}

	@Override
	public boolean handleJSONObjectChild() {
		return true;
	}

	@Override
	public boolean handleJSONArrayChild(){
		return true;
	}

	@Override
	public void handleEnd()
	{
		// nothing to do
	}

	@Override
	public Object result() {
		return result;
	}

	protected boolean foundStartsWith(String path)
	{
		for (String p : pathsToRetain) {
			if (p == path || (p != null && path != null && p.startsWith(path))) {
				return true;
			}
		}
		return false;
	}
}
