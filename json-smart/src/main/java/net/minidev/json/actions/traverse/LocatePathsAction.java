package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <b>Searches for paths in a {@link JSONObject} and returns those found</b>
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
public class LocatePathsAction implements TraverseAction
{
	protected List<String> pathsFound;
	protected List<String> pathsToFind;

	/**
	 *
	 * @param pathsToFind A path to a field in the {@link JSONObject} should be specified in n-gram format where keys are chained:
	 * k0[[[.k1].k2]...]
	 */
	public LocatePathsAction(List<String> pathsToFind)
	{
		this.pathsToFind = pathsToFind;
		pathsFound = new LinkedList<String>();
	}

	@Override
	public boolean handleStart(JSONObject object)
	{
		return object != null && pathsToFind != null && pathsToFind.size() > 0;
	}

	@Override
	public boolean handleEntryAndIgnoreChildren(String pathToEntry, Iterator<Map.Entry<String, Object>> it, Map.Entry<String, Object> entry)
	{
		if (pathsToFind.contains(pathToEntry))
		{
			//reached end of path that is being searched
			pathsFound.add(pathToEntry);
		}
		return false;
	}

	@Override
	public boolean handleDotChar() {
		return false;
	}

	@Override
	public boolean handleNext() {
		return true;
	}

	@Override
	public boolean handleJSONObjectChild() {
		return true;
	}

	@Override
	public boolean handleJSONArrayChild() {
		return true;
	}

	@Override
	public void handleEnd() {
		//nothing to do
	}

	@Override
	public Object result() {
		return pathsFound;
	}
}
