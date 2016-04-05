package net.minidev.json.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.traverse.JSONTraverser;
import net.minidev.json.actions.traverse.RemoveAction;
import net.minidev.json.actions.traverse.TraverseAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <b>Removes branches of nodes from a {@link JSONObject} matching the list of user-specified paths.</b>
 * <p>
 * A path to remove must be specified in the n-gram format - a list of keys from the root down separated by dots:
 * K0[[[[.K1].K2].K3]...]
 * <br>
 * A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot in their name are
 * not supported.
 * <p>
 * <b>Usage Example:</b>
 * <p>
 * To remove the field k1.k2 from the {@link JSONObject} {k1:{k2:v2}, k3:{k4:v4}} use the remover like so:
 * <pre>
 * PathRemover pr = new PathRemover("k1.k2");
 * JSONObject cleanObject = pr.remove(new JSONObject(...));
 * </pre>
 * The resulting object 'cleanObject' would be {k1:{k3:{k4:v4}}}
 * <p>
 * See unit tests for more examples
 *
 * @author adoneitan@gmail.com
 *
 */
public class PathRemover
{
	private List<String> pathsToRemove;

	public PathRemover(JSONArray pathsToRemove)
	{
		if (pathsToRemove == null || pathsToRemove.isEmpty()) {
			this.pathsToRemove = Collections.emptyList();
		}
		else
		{
			this.pathsToRemove = new ArrayList<String>();
			for (Object s : pathsToRemove) {
				this.pathsToRemove.add((String) s);
			}
		}
	}

	public PathRemover(List<String> pathsToRemove)
	{
		this.pathsToRemove = pathsToRemove == null || pathsToRemove.size() == 0 ?
				Collections.<String>emptyList() : pathsToRemove;
	}

	public PathRemover(String... pathsToRemove)
	{
		this.pathsToRemove = pathsToRemove == null || pathsToRemove.length == 0 ?
				Collections.<String>emptyList() : Arrays.asList(pathsToRemove);
	}

	public JSONObject remove(JSONObject objectToClean)
	{
		TraverseAction strategy = new RemoveAction(this.pathsToRemove);
		JSONTraverser traversal = new JSONTraverser(strategy);
		traversal.traverse(objectToClean);
		return (JSONObject) strategy.result();
	}
}
