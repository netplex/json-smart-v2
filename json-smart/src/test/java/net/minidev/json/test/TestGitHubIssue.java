package net.minidev.json.test;

import static net.minidev.json.parser.JSONParser.MODE_PERMISSIVE;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGitHubIssue {
  @Test
  public void issue68() {
    Assertions.assertThrows(
        ParseException.class,
        () -> {
          JSONParser parser = new JSONParser(MODE_PERMISSIVE);
          String input = "'1";
          parser.parse(input);
        });
  }
}
