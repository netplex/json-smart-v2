package net.minidev.json.actions.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.TreePath;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>Navigates only the branches of a {@link JSONObject} corresponding to the paths specified.</b>
 * <p>
 * For each specified path to navigate, the {@link JSONNavigatorOld} only traverses the matching
 * branch.
 * <p>
 * The navigator accepts an action and provides callback hooks for it to act on the traversed
 * nodes at each significant step. See {@link JSONNavigateActionOld}.
 * <p>
 * See package-info for more details
 * <p>
 * <b>Example:</b>
 * <p>
 * To navigate the branch k1.k2 of the object {"k1":{"k2":"v1"}, "k3":{"k4":"v2"}} instantiate
 * the navigator like so: new JSONNavigator("k1.k2")
 *
 * @author adoneitan@gmail.com
 *
 */
public class JSONNavigatorOld
{
	protected List<String> pathsToNavigate;
	protected JSONNavigateActionOld action;

	public JSONNavigatorOld(JSONNavigateActionOld action, JSONArray pathsToNavigate)
	{
		if (action == null) {
			throw new IllegalArgumentException("JSONNavigateActionOld cannot be null");
		}
		this.action = action;
		if (pathsToNavigate == null || pathsToNavigate.isEmpty()) {
			this.pathsToNavigate = Collections.emptyList();
		}
		else
		{
			this.pathsToNavigate = new LinkedList<String>();
			for (Object s : pathsToNavigate) {
				this.pathsToNavigate.add((String) s);
			}
		}
	}

	public JSONNavigatorOld(JSONNavigateActionOld action, List<String> pathsToNavigate)
	{
		if (action == null) {
			throw new IllegalArgumentException("JSONNavigateActionOld cannot be null");
		}
		this.action = action;
		this.pathsToNavigate = pathsToNavigate == null || pathsToNavigate.size() == 0 ?
				Collections.<String>emptyList() : pathsToNavigate;
	}

	public JSONNavigatorOld(JSONNavigateActionOld action, String... pathsToNavigate)
	{
		if (action == null) {
			throw new IllegalArgumentException("JSONNavigateActionOld cannot be null");
		}
		this.action = action;
		this.pathsToNavigate = pathsToNavigate == null || pathsToNavigate.length == 0 ?
				Collections.<String>emptyList() : new LinkedList<String>(Arrays.asList(pathsToNavigate));
	}

	public void nav(JSONObject object) throws Exception
	{
		if (action.handleNavigationStart(object, pathsToNavigate))
		{
			for (String path: pathsToNavigate)
			{
				try
				{
					if (path != null && !path.equals("") && action.handleNextPath(path))
					{
						TreePath jp = new TreePath(path, new DotDelimiter().withAcceptDelimiterInNodeName(true));
						nav(object, jp);
						action.handlePathEnd(path);
					}
				}
				catch (Exception e)
				{
					if (action.failPathSilently(path ,e)) {
						break;
					}
					else if (action.failPathFast(path, e)) {
						throw e;
					}
				}
			}
		}
		action.handleNavigationEnd();
	}

	private void nav(JSONObject source, TreePath jp)
	{
		if (jp.hasNext())
		{
			if (source == null)
			{
				//source is null - navigation impossible
				return;
			}
			String next = jp.next();
			if (!source.containsKey(next))
			{
				// reached end of branch in source before end of specified json path -
				// the specified path is illegal because it does not exist in the source.
				action.handlePrematureNavigatedBranchEnd(jp, source);
			}
			else if (source.get(next) instanceof JSONObject && action.handleJSONObject(jp, (JSONObject) source.get(next)))
			{
				//reached JSONObject node - handle it and recur into it
				nav((JSONObject) source.get(next), jp);
			}
			else if (source.get(next) instanceof JSONArray && action.handleJSONArrray(jp, (JSONArray) source.get(next)))
			{
				//reached JSONArray node - handle it and recur into it
				nav((JSONArray) source.get(next), jp);
			}
			else if (jp.hasNext())
			{
				// reached leaf node (not a container) in source but specified path expects children -
				// the specified path is illegal because it does not exist in the source.
				action.handlePrematureNavigatedBranchEnd(jp, source.get(next));
			}
			else if (!jp.hasNext())
			{
				//reached leaf in source and specified path is also at leaf -> handle it
				action.handleJSONObjectLeaf(jp, source.get(next));
			}
			else
			{
				throw new IllegalStateException("fatal: unreachable code reached at '" + jp.origin() + "'");
			}
		}
		action.handleObjectEnd(jp);
	}

	private void nav(JSONArray source, TreePath jp)
	{
		if (source == null)
		{
			//array is null - navigation impossible
			return;
		}
		int arrIndex = 0;
		for (Object arrItem : source.toArray())
		{
			if (arrItem instanceof JSONObject && action.handleJSONObject(jp, (JSONObject) arrItem))
			{
				// clone the path so that for each JSONObject in the array,
				// the iterator continues from the same position in the path
				TreePath jpClone = getClone(jp);
				nav((JSONObject) arrItem, jpClone);
			}
			else if (arrItem instanceof JSONArray)
			{
				throw new IllegalArgumentException("illegal json - found array nested inside array at: '" + jp.origin() + "'");
			}
			else if (!jp.hasNext())
			{
				//reached leaf - handle it
				action.handleJSONArrayLeaf(arrIndex, arrItem);
			}
			arrIndex++;
		}
		action.handleJSONArrayEnd(jp);

	}

	private TreePath getClone(TreePath jp)
	{
		try
		{
			return jp.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException("failed to clone json path", e);
		}
	}
}
