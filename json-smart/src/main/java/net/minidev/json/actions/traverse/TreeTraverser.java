package net.minidev.json.actions.traverse;

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
	private TreeTraverseAction<M, L> action;

	public TreeTraverser(TreeTraverseAction action)
	{
		this.action = action;
	}

	public void traverse(M map)
	{
		if (action.start(map)){
			depthFirst("", map);
		}
		action.end();
	}

	private void depthFirst(String fullPathToMap, M map)
	{
		if (map == null || map.entrySet() == null || !action.recurInto(fullPathToMap, map)) {
			return;
		}
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, Object> entry = it.next();
			String fullPathToEntry = buildPath(fullPathToMap, entry.getKey());

			if (!action.traverseEntry(fullPathToEntry, entry)) {
				continue;
			}
			else if (action.removeEntry(fullPathToEntry, entry))
			{
				it.remove();
				continue;
			}

			if (instanceOfMap(entry.getValue()))
			{
				depthFirst(fullPathToEntry, (M) entry.getValue());
			}
			else if (instanceOfList(entry.getValue()))
			{
				depthFirst(fullPathToEntry, (L) entry.getValue());
			}
			else if (!instanceOfMap(entry) && !instanceOfList(entry))
			{
				action.handleLeaf(fullPathToEntry, entry);
			}
		}
	}

	private void depthFirst(String fullPathToList, L list)
	{
		if (!action.recurInto(fullPathToList, list)) {
			return;
		}
		int listIndex = 0;
		for (Object listItem : list.toArray())
		{
			if (instanceOfMap(listItem) && action.recurInto(fullPathToList, listIndex, listItem))
			{
				depthFirst(fullPathToList, (M) listItem);
			}
			else if (instanceOfList(listItem) && action.recurInto(fullPathToList, listIndex, listItem))
			{
				depthFirst(fullPathToList, (L) listItem);
			}
			else
			{
				action.handleLeaf(fullPathToList, listIndex, listItem);
			}
			listIndex++;
		}
	}

	private String buildPath(String fullPathToObject, String entryKey) {
		return "".equals(fullPathToObject) ? entryKey : fullPathToObject + "." + entryKey;
	}

	private boolean instanceOfList(Object obj) {
		return obj instanceof List;
	}

	private boolean instanceOfMap(Object obj) {
		return obj instanceof Map;
	}
}
