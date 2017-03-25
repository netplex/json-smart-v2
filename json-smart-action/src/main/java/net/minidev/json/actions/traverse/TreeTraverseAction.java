package net.minidev.json.actions.traverse;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An interface for a processing action on the nodes of a {@link M} tree
 * while traversing it. The order in which the callbacks are listed
 * below is the order in which they are called by the {@link TreeTraverser}
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public interface TreeTraverseAction<M extends Map<String, Object>, L extends List<Object>> {
	/**
	 * called before any traversal of the {@link M} tree starts
	 * @return true if traversal should start at all
	 */
	boolean start(M object);

	/**
	 * called when a new entry is encountered and before any processing is performed on it
	 * @return true if the entry should be processed
	 */
	boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry);

	/**
	 * the last callback for each entry in an {@link M} map. if this method returns true
	 * the {@link TreeTraverser} removes the entry from the map. there is no further
	 * handling of the entry.
	 * @return true if the entry and its subtree should be removed from the {@link M} tree
	 */
	boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry);

	/**
	 * called when a non-leaf entry is encountered inside an {@Link M} object
	 * @return true if the non-leaf entry should be recursively traversed
	 */
	boolean recurInto(String fullPathToSubtree, M entryValue);

	/**
	 * called when a non-leaf item is encountered inside an {@Link L} object
	 * @return true if the non-leaf item should be recursively traversed
	 */
	boolean recurInto(String fullPathToContainingList, L entryValue);

	/**
	 * called for each leaf of an {@link M} map is encountered
	 */
	void handleLeaf(String fullPathToEntry, Entry<String, Object> entry);

	/**
	 * called for each leaf of an {@link L} list is encountered
	 * @param listItem - the item
	 * @param listIndex - the ordered location of the item in the list
	 */
	void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem);

	/**
	 * called after the traversal ends,
	 * and just before the {@link #start(M)} method exits
	 */
	void end();

	/**
	 * holds the result of the traversal,
	 * as assigned by the action implementing this interface
	 */
	Object result();
}
