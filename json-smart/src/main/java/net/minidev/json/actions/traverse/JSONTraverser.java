package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * <b>Traverses every node of a {@link JSONObject}</b>
 * <p>
 * {@link JSONTraverser} accepts an action and provides callback hooks for it to act
 * on the traversed nodes at each significant step. See {@link TraverseAction}.
 * <p>
 * A key to the right of a dot is a direct child of a key to the left of a dot.
 * Keys with a dot in their name are not supported.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 *
 */
public class JSONTraverser
{
	private TraverseAction action;

	public JSONTraverser(TraverseAction action)
	{
		this.action = action;
	}

	public void traverse(JSONObject object)
	{
		if (action.handleStart(object)){
			breadthFirst("", object);
		}
		action.handleEnd();
	}

	private void breadthFirst(String fullPathToObject, JSONObject jsonObject)
	{
		if (jsonObject == null || jsonObject.entrySet() == null) {
			return;
		}
		Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
		while (it.hasNext() && action.handleNext())
		{
			Map.Entry<String, Object> entry = it.next();
			if (entry.getKey().contains(".") && !action.handleDotChar())
			{
				//a dot char '.' in the key is not supported by the action, abandon this path
				continue;
			}
			String fullPathToEntry = "".equals(fullPathToObject) ? entry.getKey() : fullPathToObject + "." + entry.getKey();
			if (action.handleEntryAndIgnoreChildren(fullPathToEntry, it, entry))
			{
				continue;
			}
			else if (entry.getValue() instanceof JSONObject && action.handleJSONObjectChild())
			{
				breadthFirst(fullPathToEntry, (JSONObject) entry.getValue());
			}
			else if (entry.getValue() instanceof JSONArray && action.handleJSONArrayChild())
			{
				breadthFirst(fullPathToEntry, (JSONArray) entry.getValue());
			}
		}
	}

	private void breadthFirst(String fullPathToObject, JSONArray jsonArray)
	{
		for (Object arrItem : jsonArray.toArray())
		{
			if (arrItem instanceof JSONObject)
			{
				breadthFirst(fullPathToObject, (JSONObject) arrItem);
			}
			else if (arrItem instanceof JSONArray)
			{
				breadthFirst(fullPathToObject, (JSONArray) arrItem);
			}
		}
	}
}
