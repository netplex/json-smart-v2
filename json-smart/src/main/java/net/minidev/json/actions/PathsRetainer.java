package net.minidev.json.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.PathDelimiter;
import net.minidev.json.actions.traverse.JSONTraverseAction;
import net.minidev.json.actions.traverse.JSONTraverser;
import net.minidev.json.actions.traverse.LocatePathsJsonAction;
import net.minidev.json.actions.traverse.RetainPathsJsonAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>Retains branches of nodes of a {@link JSONObject} matching the list of user-specified paths.</b>
 * <p>
 * A path to copy must be specified in the n-gram format - a list of keys from the root down separated by dots:
 * K0[[[[.K1].K2].K3]...]
 * <br>
 * A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot in their name are
 * not supported.
 * <p>
 * <b>Example:</b>
 * <p>
 * to retain the field k1.k2 in the {@link JSONObject} {k1:{k2:v1}, k3:{k4:v2}} instantiate the retainer like so:
 * new JSONObjectCleaner("k1.k2")
 * The resulting object would be {k1:{k2:v1}}
 * <p>
 * See unit tests in JSONObjectRetainerTest for more examples
 *
 * @author adoneitan@gmail.com
 */
public class PathsRetainer {
	protected List<String> pathsToRetain;
	protected PathDelimiter pathDelimiter = new DotDelimiter().withAcceptDelimiterInNodeName(false);

	public PathsRetainer(JSONArray pathsToRetain) {
		if (pathsToRetain == null || pathsToRetain.isEmpty()) {
			this.pathsToRetain = Collections.emptyList();
		} else {
			this.pathsToRetain = new LinkedList<String>();
			for (Object s : pathsToRetain) {
				this.pathsToRetain.add((String) s);
			}
		}
	}

	public PathsRetainer(List<String> pathsToRetain) {
		this.pathsToRetain = pathsToRetain == null || pathsToRetain.size() == 0 ? Collections.<String> emptyList() : pathsToRetain;
	}

	public PathsRetainer(String... pathsToRetain) {
		this.pathsToRetain = pathsToRetain == null || pathsToRetain.length == 0 ? Collections.<String> emptyList()
				: new LinkedList<String>(Arrays.asList(pathsToRetain));
	}

	public PathsRetainer with(PathDelimiter pathDelimiter) {
		this.pathDelimiter = pathDelimiter;
		return this;
	}

	public JSONObject retain(JSONObject object) {
		/**
		 * a path to retain which contains a path in the object, but is not itself a path in the object,
		 * will cause the sub-path to be retained although it shouldn't:
		 * object = {k0:v0}     retain = {k0.k1}
		 * so the false path to retain has to be removed from the pathsToRetain list.
		 *
		 * The {@link LocatePathsJsonAction} returns only paths which exist in the object.
		 */
		JSONTraverseAction locateAction = new LocatePathsJsonAction(pathsToRetain, pathDelimiter);
		JSONTraverser t1 = new JSONTraverser(locateAction);
		t1.traverse(object);
		List<String> realPathsToRetain = (List<String>) locateAction.result();

		//now reduce the object using only existing paths
		JSONTraverseAction retainer = new RetainPathsJsonAction(realPathsToRetain, pathDelimiter);
		JSONTraverser t2 = new JSONTraverser(retainer).with(pathDelimiter);
		t2.traverse(object);
		return (JSONObject) retainer.result();
	}
}
