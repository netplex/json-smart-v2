package net.minidev.json.actions.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.TreePath;

import java.util.Collection;
import java.util.Stack;

/**
 * <b>Creates a copy of a {@link JSONObject} containing just the nodes on the paths specified.</b>
 * <p>
 * Specified paths that do not exist in the source object are ignored silently.
 * Specifying an empty list of paths to navigate or only non-existent paths will result in an empty
 * object being returned.
 * <p>
 * See package-info for more details
 * <p>
 * <b>Example:</b>
 * <p>
 * To copy the branch k1.k2 from {k1:{k2:v1}, k3:{k4:v2}} instantiate the copier like so:
 * new JSONObjectCopier("k1.k2")
 * The resulting copy would be {k1:{k2:v1}}
 * <p>
 * See unit tests for more examples
 *
 * @author adoneitan@gmail.com
 * @since 15 March 2016.
 *
 */
public class CopyPathsAction implements JSONNavigateAction
{
	protected JSONObject destTree;
	protected JSONObject destBranch;
	protected Stack<Object> destNodeStack;

	@Override
	public boolean start(JSONObject source, Collection<String> pathsToCopy)
	{
		if (source == null)
		{
			destTree = null;
			return false;
		}

		destTree = new JSONObject();
		if (pathsToCopy == null || pathsToCopy.size() == 0) {
			return false;
		}

		/**
		 * using ARRAY_PUSH which adds any new entry encountered in arrays, effectively
		 * allowing duplicate objects in the array, which is supported by Gigya
		 */
		return true;
	}

	@Override
	public boolean recurInto(TreePath jp, JSONObject o)
	{
		//reached JSONObject node - instantiate it and recur
		handleNewNode(jp, new JSONObject());
		return true;
	}

	private void handleNewNode(TreePath jp, Object node)
	{
		if (!jp.hasPrev()) {
			return;
		}
		if (destNodeStack.peek() instanceof JSONObject) {
			((JSONObject) destNodeStack.peek()).put(jp.curr(), node);
		}
		else if (destNodeStack.peek() instanceof JSONArray) {
			((JSONArray) destNodeStack.peek()).add(node);
		}
		destNodeStack.push(node);
	}

	@Override
	public boolean recurInto(TreePath jp, JSONArray o)
	{
		//reached JSONArray node - instantiate it and recur
		handleNewNode(jp, new JSONArray());
		return true;
	}

	@Override
	public void handlePrematureNavigatedBranchEnd(TreePath jp, Object source) {
		throw new IllegalArgumentException("branch is shorter than path - path not found in source: '" + jp.origin() + "'");
	}

	@Override
	public void handleLeaf(TreePath jp, Object o) {
		((JSONObject) destNodeStack.peek()).put(jp.curr(), o);
	}

	@Override
	public void handleLeaf(TreePath jp, int arrIndex, Object o) {
		((JSONArray) destNodeStack.peek()).add(o);
	}

	@Override
	public void recurEnd(TreePath jp, JSONObject jo) {
		destNodeStack.pop();
	}

	@Override
	public void recurEnd(TreePath jp, JSONArray ja) {
		destNodeStack.pop();
	}

	@Override
	public boolean pathStart(String path)
	{
		destBranch = new JSONObject();
		destNodeStack = new Stack<Object>();
		destNodeStack.push(destBranch);
		return true;
	}

	@Override
	public void pathEnd(String path) {
		destTree.merge(destBranch);
	}

	@Override
	public boolean failSilently(String path, Exception e) {
		return false;
	}

	@Override
	public boolean failFast(String path, Exception e) {
		return false;
	}

	@Override
	public void end() {

	}

	@Override
	public Object result() {
		return destTree;
	}
}
