/**
 * <b>Traverse all the nodes in a {@link net.minidev.json.JSONObject} and process them</b>
 *
 * <p>The traversal starts at the root and moves breadth-first down the branches. The {@link
 * net.minidev.json.actions.traverse.TreeTraverser} accepts a {@link
 * net.minidev.json.actions.traverse.JSONTraverseAction} and provides callback hooks at each
 * significant step which the {@link net.minidev.json.actions.traverse.JSONTraverseAction} may use
 * to process the nodes.
 *
 * <p>The {@link net.minidev.json.actions.traverse.TreeTraverser} assumes that paths in the tree
 * which the {@link net.minidev.json.JSONObject} represents are specified in the n-gram format - a
 * list of keys from the root down separated by dots:
 *
 * <p>K0[[[[.K1].K2].K3]...]
 *
 * <p>A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot
 * in their name are not supported.
 *
 * <p>Sample usage:
 *
 * <pre>
 * TraverseAction tAction = new TraverseAction(){...};
 * JSONTraverser jsonTrv = new JSONTraverser(tAction);
 * jsonTrv.traverse(new JSONObject(...));
 * Object result = tAction.result();
 * </pre>
 *
 * @author adoneitan@gmail.com
 */
package net.minidev.json.actions.traverse;
