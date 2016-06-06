package net.minidev.json.actions.traverse;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import java.util.Map;

/**
 * @author adoneitan@gmail.com
 * @since  5/24/16.
 */
public class KeysPrintAction implements JSONTraverseAction
{
	@Override
	public boolean start(JSONObject object)
	{
		return true;
	}

	@Override
	public boolean traverseEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		System.out.println(entry.getKey());
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, Object entryValue) {
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, int listIndex, Object entryValue) {
		return true;
	}

	@Override
	public void handleLeaf(String pathToEntry, Object entryValue)
	{

	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem)
	{

	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		return false;
	}

	@Override
	public void end()
	{

	}

	@Override
	public Object result()
	{
		return null;
	}

}
