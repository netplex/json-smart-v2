package net.minidev.json.test.actions;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.actions.ElementRemover;
import net.minidev.json.parser.ParseException;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link ElementRemover}
 *
 * @author adoneitan@gmail.com
 */
@RunWith(Parameterized.class)
public class ElementRemoverTest {
	private String jsonToClean;
	private String elementsToRemove;
	private String expectedJson;

	public ElementRemoverTest(String jsonToClean, String elementsToRemove, String expectedJson) {
		this.jsonToClean = filter(jsonToClean);
		this.elementsToRemove = filter(elementsToRemove);
		this.expectedJson = filter(expectedJson);
	}

	private static String filter(String test) {
		if (test == null)
			return null;
		return test.replace("'", "\"");
	}
	
	@Parameterized.Parameters
	public static Collection<String[]> params() {
		List<String[]>  list = Arrays.asList(new String[][]{
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", null,                     "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"},
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{}",                     "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"},
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k0':'v2'}",            "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"},
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k2':'v2'}",            "{'k0':{},'k1':{'k3':'v3'}}"},
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k0':{'k2':'v2'}}",     "{'k1':{'k2':'v2','k3':'v3'}}"},
				{"{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k2':'v2','k3':'v3'}",  "{'k0':{},'k1':{}}"},
				{"{'k0':{}}",                                     "{}",                     "{'k0':{}}"},
		});
		return list;
	}

	@Test
	public void test() throws ParseException {
		JSONObject objectToClean = jsonToClean != null ? (JSONObject) JSONValue.parseWithException(jsonToClean) : null;
		JSONObject expectedObject = expectedJson != null ? (JSONObject) JSONValue.parseWithException(expectedJson) : null;
		JSONObject toRemove = elementsToRemove != null ? (JSONObject) JSONValue.parseWithException(elementsToRemove) : null;
		ElementRemover er = new ElementRemover(toRemove);
		er.remove(objectToClean);
		assertEquals(expectedObject, objectToClean);
	}
}
