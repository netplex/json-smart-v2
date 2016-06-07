package net.minidev.json.actions.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.PathDelimiter;

/**
 * <b>Traverses every node of a {@link JSONObject}</b>
 * <p>
 * {@link JSONTraverser} accepts an action and provides callback hooks for it to act
 * on the traversed nodes at each significant step. See {@link JSONTraverseAction}.
 * <p>
 * A key to the right of a dot is a direct child of a key to the left of a dot.
 * Keys with a dot in their name are not supported.
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 *
 */
public class JSONTraverser extends TreeTraverser<JSONObject, JSONArray>
{
	private JSONTraverseAction action;

	public JSONTraverser(JSONTraverseAction action)
	{
		super(action, new DotDelimiter());
	}

	public JSONTraverser with(PathDelimiter delim) {
		super.delim = delim;
		return this;
	}
}
