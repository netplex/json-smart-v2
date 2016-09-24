package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.PathDelimiter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
public class LocatePathsJsonAction implements JSONTraverseAction
{
	protected List<String> pathsFound;
	protected List<String> pathsToFind;
	protected PathDelimiter delim;

	/**
	 *
	 * @param pathsToFind A path to a field in the {@link JSONObject} should be specified in n-gram format where keys are chained:
	 * k0[[[.k1].k2]...]
	 */
	public LocatePathsJsonAction(List<String> pathsToFind, PathDelimiter delim)
	{
		this.pathsToFind = pathsToFind;
		this.delim = delim;
		pathsFound = new LinkedList<String>();
	}

	@Override
	public boolean start(JSONObject object)
	{
		return object != null && pathsToFind != null && pathsToFind.size() > 0;
	}

	@Override
	public boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry)
	{
		if (!delim.accept(entry.getKey())) {
			return false;
		}
		locatePath(fullPathToEntry);
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
	public void handleLeaf(String pathToEntry, Entry<String, Object> entry) {

	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem) {

	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry)
	{
		return false;
	}

	@Override
	public void end() {
		//nothing to do
	}

	@Override
	public Object result() {
		return pathsFound;
	}

	private void locatePath(String pathToEntry)
	{
		if (pathsToFind.contains(pathToEntry))
		{
			//reached end of path that is being searched
			pathsFound.add(pathToEntry);
		}
	}
}
