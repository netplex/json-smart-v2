package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.PathDelimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
public class RetainPathsJsonAction implements JSONTraverseAction
{
	protected final PathDelimiter delim;
	protected JSONObject result;
	protected List<String> pathsToRetain;

	/**
	 * @param pathsToRetain TODO
	 * @param delim TODO
	 */
	public RetainPathsJsonAction(List<String> pathsToRetain, PathDelimiter delim)
	{
		this.pathsToRetain = new ArrayList<String>(pathsToRetain);
		this.delim = delim;
	}

	@Override
	public boolean start(JSONObject object)
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
	public boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry) {
		return true;
	}

	@Override
	public boolean recurInto(String fullPathToSubtree, JSONObject entryValue) {
		return true;
	}

	@Override
	public boolean recurInto(String fullPathToArrayItem, JSONArray entryValue) {
		return true;
	}

	@Override
	public void handleLeaf(String pathToEntry, Entry<String, Object> entry) {
	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem) {
	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry) {
		return discardPath(fullPathToEntry, entry);
	}

	@Override
	public void end()
	{
		// nothing to do
	}

	@Override
	public Object result() {
		return result;
	}

	/**
	 * if the full path to the entry is not contained in any of the paths to retain - remove it from the object
	 *  this step does not remove entries whose full path is contained in a path to retain but are not equal to an
	 *  entry to retain
	 * 
	 * @param pathToEntry  TODO
	 * @param entry  TODO
	 * @return  TODO
	 */
	protected boolean discardPath(String pathToEntry, Entry<String, Object> entry)
	{
		if (!foundAsPrefix(pathToEntry) || !delim.accept(entry.getKey()))
		{
			//skip traversal of subtree and remove from the traversal iterator
			return true;
		}
		return false;
	}

	/**
	 * @param path TODO
	 * @return TODO
	 */
	protected boolean foundAsPrefix(String path)
	{
		for (String p : pathsToRetain) {
			if (p == path || (p != null && path != null && p.startsWith(path))) {
				return true;
			}
		}
		return false;
	}
}
