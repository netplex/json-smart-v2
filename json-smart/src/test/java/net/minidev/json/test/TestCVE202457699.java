package net.minidev.json.test;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCVE202457699 {

    private static final String MALICIOUS_STRING = createMaliciousString();

    @Test
    public void jsonSimpleParserShouldRestrictDepth() {
        JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        assertThrows(ParseException.class,
                () -> p.parse(MALICIOUS_STRING),
                "Malicious payload, having non natural depths");
    }

    @Test
    public void strictestParserShouldRestrictDepth() {
        JSONParser p = new JSONParser(JSONParser.MODE_STRICTEST);
        assertThrows(ParseException.class,
                () -> p.parse(MALICIOUS_STRING),
                "Malicious payload, having non natural depths");
    }

    @Test
    public void rfc4627ParserShouldRestrictDepth() {
        JSONParser p = new JSONParser(JSONParser.MODE_RFC4627);
        assertThrows(ParseException.class,
                () -> p.parse(MALICIOUS_STRING),
                "Malicious payload, having non natural depths");
    }

    @Test
    public void permissiveParserShouldRestrictDepth() {
        JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
        assertThrows(ParseException.class,
                () -> p.parse(MALICIOUS_STRING),
                "Malicious payload, having non natural depths");
    }

    private static String createMaliciousString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000 ; i++) {
            sb.append("{\"a\":");
        }
        sb.append("1");
        for (int i = 0; i < 10000 ; i++) {
            sb.append("}");
        }
        return sb.toString();
    }
}
