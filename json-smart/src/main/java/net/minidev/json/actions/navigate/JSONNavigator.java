package net.minidev.json.actions.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.List;

/**
 * Created by eitanraviv on 6/15/16.
 */
public class JSONNavigator extends TreeNavigator<JSONObject, JSONArray>
{

	public JSONNavigator(JSONNavigateAction action, List<String> pathsToNavigate)
	{
		super(action, pathsToNavigate);
	}

	public JSONNavigator(JSONNavigateAction action, String... pathsToNavigate)
	{
		super(action, pathsToNavigate);
	}
}
