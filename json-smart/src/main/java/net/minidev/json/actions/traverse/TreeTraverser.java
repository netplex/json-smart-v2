package net.minidev.json.actions.traverse;

import net.minidev.json.actions.path.PathDelimiter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>Traverses every node of a tree made up of a combination of {@link Map}s and {@link List}s</b>
 * <p>
 * {@link TreeTraverser} accepts an action and provides callback hooks for it to act
 * on the traversed nodes at each significant step. See {@link TreeTraverseAction}.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 *
 */
public class TreeTraverser<M extends Map<String, Object>, L extends List<Object>>
{
	protected TreeTraverseAction<M, L> action;
	protected PathDelimiter delim;
	protected String pathPrefix = "";

	public TreeTraverser(TreeTraverseAction action, PathDelimiter delim)
	{
		this.action = action;
		this.delim = delim;
	}

	public TreeTraverser with(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		return this;
	}

	public void traverse(M map)
	{
		if (action.start(map)){
			depthFirst(pathPrefix, map);
		}
		action.end();
	}

	private void depthFirst(String fullPath, M map)
	{
		if (map == null || map.entrySet() == null || !action.recurInto(fullPath, map)) {
			return;
		}
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, Object> entry = it.next();
			String fullPathToEntry = buildPath(fullPath, entry.getKey());

			if (!action.traverseEntry(fullPathToEntry, entry)) {
				continue;
			}
			else if (action.removeEntry(fullPathToEntry, entry))
			{
				it.remove();
				continue;
			}

			if (entry.getValue() instanceof Map)
			{
				depthFirst(fullPathToEntry, (M) entry.getValue());
			}
			else if (entry.getValue() instanceof List)
			{
				depthFirst(fullPathToEntry, (L) entry.getValue());
			}
			else
			{
				action.handleLeaf(fullPathToEntry, entry);
			}
		}
	}

	private void depthFirst(String fullPath, L list)
	{
		if (!action.recurInto(fullPath, (L) list)) {
			return;
		}
		int listIndex = 0;
		for (Object listItem : list.toArray())
		{
			if (listItem instanceof Map)
			{
				depthFirst(fullPath, (M) listItem);
			}
			else if (listItem instanceof List)
			{
				depthFirst(fullPath, (L) listItem);
			}
			else
			{
				action.handleLeaf(fullPath, listIndex, listItem);
			}
			listIndex++;
		}
	}

	private String buildPath(String fullPath, String entryKey) {
		return pathPrefix.equals(fullPath) ? pathPrefix + entryKey : fullPath + delim.str() + entryKey;
	}

}
