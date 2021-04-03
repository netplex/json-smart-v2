package net.minidev.json.test.actions;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.actions.ElementRemover;
import net.minidev.json.parser.ParseException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link ElementRemover}
 *
 * @author adoneitan@gmail.com
 */
// @RunWith(Parameterized.class)
public class ElementRemoverTest {

	public ElementRemoverTest() {
	}

	private static String filter(String test) {
		if (test == null)
			return null;
		return test.replace("'", "\"");
	}
	
	public static Stream<Arguments> params() {
		return Stream.of(
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", null,                     "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"),
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{}",                     "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"),
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k0':'v2'}",            "{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}"),
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k2':'v2'}",            "{'k0':{},'k1':{'k3':'v3'}}"),
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k0':{'k2':'v2'}}",     "{'k1':{'k2':'v2','k3':'v3'}}"),
				Arguments.of("{'k0':{'k2':'v2'},'k1':{'k2':'v2','k3':'v3'}}", "{'k2':'v2','k3':'v3'}",  "{'k0':{},'k1':{}}"),
				Arguments.of("{'k0':{}}",                                     "{}",                     "{'k0':{}}")
				);
			};

	@ParameterizedTest
	@MethodSource("params")
	public void test(String jsonToClean, String elementsToRemove, String expectedJson) throws ParseException {
		jsonToClean = filter(jsonToClean);
		elementsToRemove = filter(elementsToRemove);
		expectedJson = filter(expectedJson);
		
		JSONObject objectToClean = jsonToClean != null ? (JSONObject) JSONValue.parseWithException(jsonToClean) : null;
		JSONObject expectedObject = expectedJson != null ? (JSONObject) JSONValue.parseWithException(expectedJson) : null;
		JSONObject toRemove = elementsToRemove != null ? (JSONObject) JSONValue.parseWithException(elementsToRemove) : null;
		ElementRemover er = new ElementRemover(toRemove);
		er.remove(objectToClean);
		assertEquals(expectedObject, objectToClean);
	}
}
