package net.minidev.json.parser;

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static net.minidev.json.parser.ParseException.ERROR_UNEXPECTED_LEADING_0;

import java.math.BigInteger;

/**
 * JSONParserBase is the common code between {@link JSONStringParser} and
 * {@link JSONStreamParser}
 * 
 * @see JSONStringParser
 * @see JSONStreamParser
 * 
 * @author Uriel Chemouni <uchemouni@gmail.com>
 */
abstract class JSONBaseParser {
	protected static final char MAX_STOP = 126;

	protected static boolean[] stopArray = new boolean[MAX_STOP];
	protected static boolean[] stopKey = new boolean[MAX_STOP];
	protected static boolean[] stopValue = new boolean[MAX_STOP];
	protected static boolean[] stopX = new boolean[MAX_STOP];
	protected static boolean[] stopAll = new boolean[MAX_STOP];

	static {
		byte EOI = 0x1A;
		stopKey[':'] = stopKey[EOI] = true;
		stopValue[','] = stopValue['}'] = stopValue[EOI] = true;
		stopArray[','] = stopArray[']'] = stopArray[EOI] = true;

		stopAll[','] = stopAll[':'] = true;
		stopAll[']'] = stopAll['}'] = stopAll[EOI] = true;
	}
	/*
	 * End of static declaration
	 */
	//
	//
	protected final MSB sb = new MSB(15);
	protected String xs;
	protected Object xo;
	protected int pos;

	/*
	 * Parssing flags
	 */
	protected final boolean acceptSimpleQuote;
	protected final boolean acceptNonQuote;
	protected final boolean acceptNaN;
	protected final boolean ignoreControlChar;
	protected final boolean useIntegerStorage;
	protected final boolean acceptLeadinZero;
	protected final boolean acceptUselessComma;
	protected final boolean useHiPrecisionFloat;

	public JSONBaseParser(int permissiveMode) {
		this.acceptNaN = (permissiveMode & JSONParser.ACCEPT_NAN) > 0;
		this.acceptNonQuote = (permissiveMode & JSONParser.ACCEPT_NON_QUOTE) > 0;
		this.acceptSimpleQuote = (permissiveMode & JSONParser.ACCEPT_SIMPLE_QUOTE) > 0;
		this.ignoreControlChar = (permissiveMode & JSONParser.IGNORE_CONTROL_CHAR) > 0;
		this.useIntegerStorage = (permissiveMode & JSONParser.USE_INTEGER_STORAGE) > 0;
		this.acceptLeadinZero = (permissiveMode & JSONParser.ACCEPT_LEADING_ZERO) > 0;
		this.acceptUselessComma = (permissiveMode & JSONParser.ACCEPT_USELESS_COMMA) > 0;
		this.useHiPrecisionFloat = (permissiveMode & JSONParser.USE_HI_PRECISION_FLOAT) > 0;
	}

	public void checkLeadinZero() throws ParseException {
		int len = xs.length();
		if (len == 1)
			return;
		if (len == 2) {
			if (xs.equals("00"))
				throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
			return;
		}
		char c1 = xs.charAt(0);
		char c2 = xs.charAt(1);
		if (c1 == '-') {
			char c3 = xs.charAt(2);
			if (c2 == '0' && c3 >= '0' && c3 <= '9')
				throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
			return;
		}
		if (c1 == '0' && c2 >= '0' && c2 <= '9')
			throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
	}

	public void checkControleChar() throws ParseException {
		if (ignoreControlChar)
			return;
		int l = xs.length();
		for (int i = 0; i < l; i++) {
			char c = xs.charAt(i);
			if (c < 0)
				continue;
			if (c <= 31)
				throw new ParseException(pos + i, ParseException.ERROR_UNEXPECTED_CHAR, c);
			if (c == 127)
				throw new ParseException(pos + i, ParseException.ERROR_UNEXPECTED_CHAR, c);
		}
	}

	protected Number parseNumber(String s) throws ParseException {
		// pos
		int p = 0;
		// len
		int l = s.length();
		// max pos long base 10 len
		int max = 19;
		boolean neg;

		if (s.charAt(0) == '-') {
			p++;
			max++;
			neg = true;
			if (!acceptLeadinZero && l >= 3 && s.charAt(1) == '0')
				throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, s);
		} else {
			neg = false;
			if (!acceptLeadinZero && l >= 2 && s.charAt(0) == '0')
				throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, s);
		}

		boolean mustCheck;
		if (l < max) {
			max = l;
			mustCheck = false;
		} else if (l > max) {
			return new BigInteger(s, 10);
		} else {
			max = l - 1;
			mustCheck = true;
		}

		long r = 0;
		while (p < max) {
			r = (r * 10L) + ('0' - s.charAt(p++));
		}
		if (mustCheck) {
			boolean isBig;
			if (r > -922337203685477580L) {
				isBig = false;
			} else if (r < -922337203685477580L) {
				isBig = true;
			} else {
				if (neg)
					isBig = (s.charAt(p) > '8');
				else
					isBig = (s.charAt(p) > '7');
			}
			if (isBig)
				return new BigInteger(s, 10);
			r = r * 10L + ('0' - s.charAt(p));
		}
		if (neg) {
			if (this.useIntegerStorage && r >= Integer.MIN_VALUE)
				return (int) r;
			return r;
		}
		r = -r;
		if (this.useIntegerStorage && r <= Integer.MAX_VALUE)
			return (int) r;
		return r;
	}

	public static class MSB {
		char b[];
		int p;

		public MSB(int size) {
			b = new char[size];
			p = -1;
		}

		public void append(char c) {
			p++;
			if (b.length <= p) {
				char[] t = new char[b.length * 2 + 1];
				System.arraycopy(b, 0, t, 0, b.length);
				b = t;
			}
			b[p] = c;
		}

		public void append(int c) {
			p++;
			if (b.length <= p) {
				char[] t = new char[b.length * 2 + 1];
				System.arraycopy(b, 0, t, 0, b.length);
				b = t;
			}
			b[p] = (char) c;
		}

		public String toString() {
			return new String(b, 0, p + 1);
		}

		public void clear() {
			p = -1;
		}
	}
}
