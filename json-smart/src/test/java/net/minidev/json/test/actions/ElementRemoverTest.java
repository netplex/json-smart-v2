package net.minidev.json.test.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.actions.ElementRemover;
import net.minidev.json.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link ElementRemover}
 *
 * @author adoneitan@gmail.com
 */
@RunWith(Parameterized.class)
public class ElementRemoverTest
{
	private String jsonToClean;
	private String elementsToRemove;
	private String expectedJson;

	public ElementRemoverTest(String jsonToClean, String elementsToRemove, String expectedJson)
	{
		this.jsonToClean = jsonToClean;
		this.elementsToRemove = elementsToRemove;
		this.expectedJson = expectedJson;
	}

	@Parameterized.Parameters
	public static Collection params()
	{
		return Arrays.asList(new Object[][]{

				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", null,                             "{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}"},
				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", "{}",                             "{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}"},
				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", "{\"k0\":\"v2\"}",                "{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}"},
				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", "{\"k2\":\"v2\"}",                "{\"k0\":{},\"k1\":{\"k3\":\"v3\"}}"},
				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", "{\"k0\":{\"k2\":\"v2\"}}",       "{\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}"},
				{"{\"k0\":{\"k2\":\"v2\"},\"k1\":{\"k2\":\"v2\",\"k3\":\"v3\"}}", "{\"k2\":\"v2\",\"k3\":\"v3\"}",  "{\"k0\":{},\"k1\":{}}"},
				{"{\"k0\":{}}",                                                   "{}",                             "{\"k0\":{}}"},
		});
	}

	@Test
	public void test() throws ParseException
	{
		JSONObject objectToClean = jsonToClean != null ? (JSONObject) JSONValue.parseWithException(jsonToClean) : null;
		JSONObject expectedObject = expectedJson != null ? (JSONObject) JSONValue.parseWithException(expectedJson): null;
		JSONObject toRemove = elementsToRemove != null ? (JSONObject) JSONValue.parseWithException(elementsToRemove): null;
		ElementRemover er = new ElementRemover(toRemove);
		er.remove(objectToClean);
		assertEquals(expectedObject, objectToClean);
	}

//	private ElementRemover switchKeyToRemove()
//	{
//		long m = System.currentTimeMillis();
//		if (elementsToRemove == null && m % 4 == 0)
//		{
//			System.out.println("cast to String");
//			return new ElementRemover((String)null);
//		}
//		else if (elementsToRemove == null && m % 4 == 1)
//		{
//			System.out.println("cast to String[]");
//			return new ElementRemover((String[])null);
//		}
//		else if (elementsToRemove == null && m % 4 == 2)
//		{
//			System.out.println("cast to JSONArray");
//			return new ElementRemover((JSONArray)null);
//		}
//		else if (elementsToRemove == null && m % 4 == 3)
//		{
//			System.out.println("cast to List<String>");
//			return new ElementRemover((List<String>)null);
//		}
//		else if (elementsToRemove instanceof String)
//		{
//			return new ElementRemover((String) elementsToRemove);
//		}
//		else if (elementsToRemove instanceof String[])
//		{
//			return new ElementRemover((String[]) elementsToRemove);
//		}
//		else if (elementsToRemove instanceof JSONArray)
//		{
//			return new ElementRemover((JSONArray) elementsToRemove);
//		}
//		else if (elementsToRemove instanceof List<?>)
//		{
//			return new ElementRemover((List<String>) elementsToRemove);
//		}
//		else
//		{
//			throw new IllegalArgumentException("bad test setup: wrong type of key to remove");
//		}
//	}

}
