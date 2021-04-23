package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class TestUtf8 extends TestCase {

    @Parameterized.Parameter(0)
    public String language;

    @Parameterized.Parameter(1)
    public String nonLatinText;

    @Parameterized.Parameters(name = "{index}: language=''{0}'', text=''{1}''")
    public static Collection<Object[]> nonLatinTexts() {
        List<Object[]> nonLatinTexts = new ArrayList<Object[]>();
        nonLatinTexts.add(new Object[]{"Sinhala", "සිංහල ජාතිය"});
        nonLatinTexts.add(new Object[]{"Japanese", "日本語"});
        nonLatinTexts.add(new Object[]{"Russian", "Русский"});
        nonLatinTexts.add(new Object[]{"Farsi", "فارسی"});
        nonLatinTexts.add(new Object[]{"Korean", "한국어"});
        nonLatinTexts.add(new Object[]{"Armenian", "Հայերեն"});
        nonLatinTexts.add(new Object[]{"Hindi", "हिन्दी"});
        nonLatinTexts.add(new Object[]{"Hebrew", "עברית"});
        nonLatinTexts.add(new Object[]{"Chinese", "中文"});
        nonLatinTexts.add(new Object[]{"Amharic", "አማርኛ"});
        nonLatinTexts.add(new Object[]{"Malayalam", "മലയാളം"});
        nonLatinTexts.add(new Object[]{"Assyrian Neo-Aramaic", "ܐܬܘܪܝܐ"});
        nonLatinTexts.add(new Object[]{"Georgian", "მარგალური"});
        nonLatinTexts.add(new Object[]{"Emojis", "🐶🐱🐭🐹🐰🦊🐻🐼🐻‍❄🐨🐯🦁🐮🐷🐽🐸🐵🙈🙉🙊🐒🐔🐧🐦🐤🐣🐥🦆🦅🦉🦇🐺🐗🐴🦄🐝🐛"});
        return nonLatinTexts;
    }

    @Test
    public void testString() {
        String s = "{\"key\":\"" + nonLatinText + "\"}";
        JSONObject obj = (JSONObject) JSONValue.parse(s);
        String actual = (String) obj.get("key");
        assertEquals("Parsing String " + language + " text", nonLatinText, actual);
    }

    @Test
    public void testReader() {
        String s = "{\"key\":\"" + nonLatinText + "\"}";
        StringReader reader = new StringReader(s);
        JSONObject obj = (JSONObject) JSONValue.parse(reader);
        String actual = (String) obj.get("key");
        assertEquals("Parsing StringReader " + language + " text", nonLatinText, actual);
    }

    @Test
    public void testInputStream() throws Exception {
        String s = "{\"key\":\"" + nonLatinText + "\"}";
        ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes("utf8"));
        JSONObject obj = (JSONObject) JSONValue.parse(bis);
        String actual = (String) obj.get("key");
        assertEquals("Parsing ByteArrayInputStream " + language + " text", nonLatinText, actual);
    }

    @Test
    public void testBytes() throws Exception {
        String s = "{\"key\":\"" + nonLatinText + "\"}";
        byte[] bs = s.getBytes("utf8");
        JSONObject obj = (JSONObject) JSONValue.parse(bs);
        String actual = (String) obj.get("key");
        assertEquals("Parsing bytes[] " + language + " text", nonLatinText, actual);
    }
}
