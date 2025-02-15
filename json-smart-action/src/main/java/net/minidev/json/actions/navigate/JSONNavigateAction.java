package net.minidev.json.actions.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * An interface for a processing action on the nodes of a {@link JSONObject} while navigating its
 * branches.
 *
 * <p>See package-info for more details
 *
 * @author adoneitan@gmail.com
 * @since 15 June 2016.
 */
public interface JSONNavigateAction extends NavigateAction<JSONObject, JSONArray> {}
