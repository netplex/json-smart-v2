package net.minidev.json.actions;

import net.minidev.json.JSONObject;
import net.minidev.json.actions.traverse.JSONTraverser;
import net.minidev.json.actions.traverse.RemoveElementsJsonAction;
import net.minidev.json.actions.traverse.JSONTraverseAction;

import java.util.*;

/**
 * <b>Removes key:value elements from every node of a {@link JSONObject} matching the list of user-specified elements.</b>
 * <p>
 * An element to remove must be specified as a key:value pair
 * <p>
 * <b>Usage Example:</b>
 * <p>
 * To remove the element k2:v2 from the {@link JSONObject} {k0:{k2:v2, k3:v3}, k1:{k2:v2, k4:v4}} use the remover like so:
 * <pre>
 * PathRemover pr = new PathRemover("k2.v2");
 * JSONObject cleanObject = pr.remove(new JSONObject(...));
 * </pre>
 * The resulting object 'cleanObject' would be {k0:{k3:v3}, k1:{k4:v4}}
 * <p>
 * See unit tests for more examples
 *
 * @author adoneitan@gmail.com
 *
 */
public class ElementRemover {
	private Map<String, Object> elementsToRemove;

	public ElementRemover(Map<String, Object> elementsToRemove) {
		this.elementsToRemove = elementsToRemove == null ? Collections.<String, Object> emptyMap() : elementsToRemove;
	}

	public ElementRemover(JSONObject elementsToRemove) {
		this.elementsToRemove = elementsToRemove == null ? Collections.<String, Object> emptyMap() : elementsToRemove;
	}

	public JSONObject remove(JSONObject objectToClean) {
		JSONTraverseAction strategy = new RemoveElementsJsonAction(this.elementsToRemove);
		JSONTraverser traversal = new JSONTraverser(strategy);
		traversal.traverse(objectToClean);
		return (JSONObject) strategy.result();
	}
}
