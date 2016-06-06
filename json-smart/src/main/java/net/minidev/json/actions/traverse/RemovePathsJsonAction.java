package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;

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
public class RemovePathsJsonAction implements JSONTraverseAction
{
	protected JSONObject result;
	protected List<String> pathsToRemove;

	public RemovePathsJsonAction(List<String> pathsToRemove)
	{
		this.pathsToRemove = pathsToRemove;
	}

	@Override
	public boolean start(JSONObject object)
	{
		result = object;
		return object != null && pathsToRemove != null && pathsToRemove.size() > 0;
	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		return pathsToRemove.contains(fullPathToEntry);
	}

	@Override
	public boolean traverseEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		//must traverse the whole object
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, Object entryValue) {
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, int listIndex, Object entryValue) {
		return true;
	}

	@Override
	public void handleLeaf(String pathToEntry, Object entryValue)
	{

	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem)
	{

	}

	@Override
	public void end() {
		//nothing to do
	}

	@Override
	public Object result() {
		return result;
	}
}
