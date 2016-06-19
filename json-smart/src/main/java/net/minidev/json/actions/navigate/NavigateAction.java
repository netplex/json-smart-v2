package net.minidev.json.actions.navigate;

import net.minidev.json.actions.path.TreePath;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An interface for a processing action on the nodes of a {@link M} while navigating its branches.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public interface NavigateAction<M extends Map<String, Object>, L extends List<Object>>
{
	/**
	 * called before any navigation of the {@link M} starts
	 * @return true if navigation should start at all
	 */
	boolean start(M objectToNavigate, Collection<String> pathsToNavigate);

	/**
	 * called before navigation of a new path starts
	 * @return true if the specified path should be navigated
	 */
	boolean pathStart(String path);

	/**
	 * reached end of branch in source before end of specified json path -
	 * the specified path does not exist in the source.
	 */
	void handlePrematureNavigatedBranchEnd(TreePath tp, Object source);

	/**
	 * called after the navigation of a path ends
	 */
	void pathEnd(String path);

	/**
	 * called if navigation of a path throws an exception
	 * @return true if the failure on this path should not abort the rest of the navigation
	 */
	boolean failSilently(String path, Exception e);

	/**
	 * called if navigation of a path throws an exception
	 * @return true if the failure on this path should abort the rest of the navigation
	 */
	boolean failFast(String path, Exception e);

	/**
	 * called when an object node is encountered on the path
	 * @return true if the navigator should navigate into the object
	 */
	boolean recurInto(TreePath tp, M sourceNode);

	/**
	 * called when an array node is encountered on the path
	 * @return true if the navigator should navigate into the array
	 */
	boolean recurInto(TreePath tp, L sourceNode);

	/**
	 * called when a leaf node is reached in a M.
	 * a leaf in a M is a key-value pair where the value is not a container itself
	 * (it is not a M nor a L)
	 * @param tp - the JsonPath pointing to the leaf
	 */
	void handleLeaf(TreePath tp, Object value);

	/**
	 * called when a leaf in a L is reached.
	 * a leaf in a L is a non-container item
	 * (it is not a M nor a L)
	 * @param arrIndex - the index of the item in the L
	 * @param arrItem - the item
	 */
	void handleLeaf(TreePath tp, int arrIndex, Object arrItem);

	/**
	 * called when navigation of an {@link M} type object ends
	 * @param tp the path pointing to the object
	 */
	void recurEnd(TreePath tp, M m);

	/**
	 * called when navigation of an {@link L} type object ends
	 * @param tp the path pointing to the object
	 */
	void recurEnd(TreePath tp, L l);

	/**
	 * called after all navigation ends, and just before the navigation method exits
	 */
	void end();

	/**
	 * holds the result of the navigation, as assigned by the action implementing this interface
	 */
	Object result();
}
