package net.minidev.json.test;

import org.junit.jupiter.api.Test;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import static net.minidev.json.parser.JSONParser.MODE_PERMISSIVE;
import org.junit.jupiter.api.Assertions;

public class TestGitHubIssue {
	@Test
	public void issue68() {
		Assertions.assertThrows(ParseException.class, () -> {
			JSONParser parser = new JSONParser(MODE_PERMISSIVE);
			String input = "'1";
			parser.parse(input);
		});
	}
}
