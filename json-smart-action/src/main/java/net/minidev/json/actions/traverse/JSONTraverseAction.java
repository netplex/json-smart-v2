package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * An interface for a processing action on the nodes of a {@link JSONObject} while traversing it.
 *
 * <p>See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public interface JSONTraverseAction extends TreeTraverseAction<JSONObject, JSONArray> {}
