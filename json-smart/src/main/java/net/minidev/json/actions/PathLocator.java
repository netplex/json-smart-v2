package net.minidev.json.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.PathDelimiter;
import net.minidev.json.actions.traverse.JSONTraverser;
import net.minidev.json.actions.traverse.LocatePathsJsonAction;
import net.minidev.json.actions.traverse.JSONTraverseAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <b>Searches for paths in a {@link JSONObject} and returns those found.</b>
 * <p>
 * Traverses the specified {@link JSONObject} searching for nodes whose paths (from the root down) match
 * any of the user-specified paths. The paths that match are returned.
 * <p>
 * A path to locate must be specified in the n-gram format - a list of keys from the root down separated by dots:
 * K0[[[[.K1].K2].K3]...]
 * <br>
 * A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot in their name are
 * not supported.
 * <p>
 *
 * @author adoneitan@gmail.com
 */
public class PathLocator
{
	protected List<String> pathsToFind;
	protected PathDelimiter pathDelimiter = new DotDelimiter().withAcceptDelimiterInNodeName(false);

	public PathLocator(JSONArray pathsToFind)
	{
		if (pathsToFind == null || pathsToFind.isEmpty()) {
			this.pathsToFind = Collections.emptyList();
		}
		else
		{
			this.pathsToFind = new ArrayList<String>();
			for (Object s : pathsToFind) {
				this.pathsToFind.add((String) s);
			}
		}
	}

	public PathLocator(List<String> pathsToFind)
	{
		this.pathsToFind = pathsToFind == null || pathsToFind.size() == 0 ?
				Collections.<String>emptyList() : pathsToFind;
	}

	public PathLocator(String... pathsToFind)
	{
		this.pathsToFind = pathsToFind == null || pathsToFind.length == 0 ?
				Collections.<String>emptyList() : Arrays.asList(pathsToFind);
	}

	public PathLocator with(PathDelimiter pathDelimiter) {
		this.pathDelimiter = pathDelimiter;
		return this;
	}

	public List<String> locate(JSONObject object)
	{
		JSONTraverseAction action = new LocatePathsJsonAction(this.pathsToFind, pathDelimiter);
		JSONTraverser traversal = new JSONTraverser(action).with(pathDelimiter);
		traversal.traverse(object);
		return (List<String>) action.result();
	}
}
