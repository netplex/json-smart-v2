package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <b>Removes key:value elements from a {@link JSONObject}.</b>
 * <p>
 * An element is not removed from the user-specified list once its processing is over,
 * because it may appear in more than one node.
 * <p>
 * See package-info for more details
 * <p>
 * See unit tests for examples
 *
 * @author adoneitan@gmail.com
 *
 */
public class RemoveElementsAction implements TraverseAction
{
	protected JSONObject result;
	protected final Map<String, Object> elementsToRemove;
	protected final boolean allowDotChar;

	public RemoveElementsAction(Map<String, Object> elementsToRemove, boolean allowDotChar)
	{
		this.elementsToRemove = elementsToRemove;
		this.allowDotChar = allowDotChar;
	}

	public RemoveElementsAction(Map<String, Object> elementsToRemove)
	{
		this(elementsToRemove, false);
	}

	@Override
	public boolean handleStart(JSONObject object)
	{
		result = object;
		return object != null && elementsToRemove != null && elementsToRemove.size() > 0;
	}

	@Override
	public boolean handleDotChar() {
		return allowDotChar;
	}

	@Override
	public boolean handleEntryAndIgnoreChildren(String pathToEntry, Iterator<Map.Entry<String, Object>> it, Map.Entry<String, Object> entry)
	{
		if (elementsToRemove.entrySet().contains(entry))
		{
			it.remove();
		}
		return false;
	}

	@Override
	public boolean handleNext()
	{
		//must traverse the whole object
		return true;
	}

	@Override
	public boolean handleJSONObjectChild() {
		return true;
	}

	@Override
	public boolean handleJSONArrayChild() {
		return true;
	}

	@Override
	public void handleEnd() {
		//nothing to do
	}

	@Override
	public Object result() {
		return result;
	}
}
