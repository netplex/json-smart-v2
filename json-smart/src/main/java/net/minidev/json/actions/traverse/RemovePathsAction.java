package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <b>Removes branches from a {@link JSONObject}.</b>
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
public class RemovePathsAction implements TraverseAction
{
	protected JSONObject result;
	protected List<String> pathsToRemove;

	public RemovePathsAction(List<String> pathsToRemove)
	{
		this.pathsToRemove = pathsToRemove;
	}

	@Override
	public boolean handleStart(JSONObject object)
	{
		result = object;
		return object != null && pathsToRemove != null && pathsToRemove.size() > 0;
	}

	@Override
	public boolean handleDotChar() {
		return true;
	}

	@Override
	public boolean handleEntryAndIgnoreChildren(String pathToEntry, Iterator<Map.Entry<String, Object>> it, Map.Entry<String, Object> entry)
	{
		if (pathsToRemove.contains(pathToEntry))
		{
			it.remove();
			//the entry has been removed from the traversal iterator, no point in traversing its children
			return true;
		}
		return false;
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
	public boolean handleJSONArrayChild() {
		return true;
	}

	@Override
	public void handleEnd() {
		//nothing to do
	}

	@Override
	public Object result() {
		return result;
	}
}
