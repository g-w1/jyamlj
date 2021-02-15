package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.JsonTokenPair;
import jyamlj.JsonLexer.JsonTokenType;
import jyamlj.YamlLexer.TokenizedLine;
import jyamlj.YamlLexer.YamlTokenPair;
import jyamlj.YamlLexer.YamlTokenType;

public abstract class ParsedObject {

	private int indentLevel;

	public abstract String toJsonString();

	public abstract String toYamlString();

	protected abstract boolean isYamlMultiline();

	public ParsedObject(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	protected String indent(String s) {
		String r = "";
		for (int i = 0; i < indentLevel + 1; i++) {
			r += "\t";
		}
		r += s;
		return r;
	}

	protected String mindent(String s) {
		String r = "";
		for (int i = 0; i < indentLevel; i++) {
			r += "\t";
		}
		r += s;
		return r;
	}

	protected static ParsedObject parseYamlRoot(final ArrayList<TokenizedLine> input)
			throws InvalidParserExceptionJson {
		IntWrap i = new IntWrap(0);
		if (input.size() == 0) {
			return new ParsedMap();
		}
		int startIndex = 0;
		TokenizedLine line;
		for (int j = 0; j < input.size(); j++) {
			if (input.get(j).tokens.size() != 0) {
				startIndex = j;
				break;
			}
		}
		if (startIndex == 0)
			return new ParsedMap();
		i.value = startIndex;
		boolean isMap = true;
		isMap = !(input.get(startIndex).tokens.get(0).token == jyamlj.YamlLexer.YamlTokenType.Dash);
		if (isMap)
			return new ParsedMap(0, input, i);
		else
			return new ParsedArray(0, input, i);
	}

	protected static ParsedObject parseJsonRoot(final List<JsonTokenPair> input) throws InvalidParserExceptionJson {
		IntWrap i = new IntWrap(0);
		JsonTokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap(0, input, i);
		case OpenBrack:
			return new ParsedArray(0, input, i);
		default:
			throw new InvalidParserExceptionJson("Expected '{' '[' , found " + t);
		}
	}

	protected ParsedObject parseJsonCont(final List<JsonTokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		JsonTokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap(indentLevel + 1, input, i);
		case OpenBrack:
			return new ParsedArray(indentLevel + 1, input, i);
		case Number:
			i.value++;
			return new ParsedNumber(t.data, indentLevel + 1);
		case String:
			i.value++;
			return new ParsedString(t.data, indentLevel + 1);
		default:
			throw new InvalidParserExceptionJson("Expected '{' '[' Number or String, found " + t);
		}
	}

	public static void expectJson(final List<JsonTokenPair> input, IntWrap i, JsonTokenType t)
			throws InvalidParserExceptionJson {
		JsonTokenType gotten;
		try {
			gotten = input.get(i.value).token;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionJson("A paren or bracket is not balenced");
		}
		if (gotten != t) {
			throw new InvalidParserExceptionJson(t, gotten);
		}
		i.value++;
	}

	protected static String expectDataJson(final List<JsonTokenPair> input, IntWrap i, JsonTokenType t)
			throws InvalidParserExceptionJson {
		JsonTokenPair gotten;
		try {
			gotten = input.get(i.value);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionJson("A paren or bracket is not balenced");
		}
		if (gotten.token != t) {
			throw new InvalidParserExceptionJson(t, gotten.token);
		}
		i.value++;
		return gotten.data;
	}

	protected static Boolean peekJson(final List<JsonTokenPair> input, IntWrap i, JsonTokenType t)
			throws InvalidParserExceptionJson {
		JsonTokenPair gotten;
		try {
			gotten = input.get(i.value);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionJson("A paren or bracket is not balenced");
		}
		return gotten.token == t;
	}

	public String toString(boolean isJson) {
		if (isJson)
			return this.toJsonString();
		else {
			return this.toYamlString();
		}
	}

	protected static String expectDataYaml(final TokenizedLine input, YamlTokenType t, int i)
			throws InvalidParserExceptionYaml {
		YamlTokenPair gotten;
		try {
			gotten = input.tokens.get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionYaml("A paren or bracket is not balenced");
		}
		if (gotten.token != t) {
			throw new InvalidParserExceptionYaml(t, gotten.token);
		}
		return gotten.data;
	}

	public static void expectYaml(final TokenizedLine input, YamlTokenType t, int i) throws InvalidParserExceptionYaml {
		YamlTokenType gotten;
		try {
			gotten = input.tokens.get(i).token;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionYaml("A paren or bracket is not balenced");
		}
		if (gotten != t) {
			throw new InvalidParserExceptionYaml(t, gotten);
		}
	}

	protected static Boolean peekYaml(final TokenizedLine input, YamlTokenType t, int i)
			throws InvalidParserExceptionYaml {
		YamlTokenPair gotten;
		try {
			gotten = input.tokens.get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionYaml("A paren or bracket is not balenced");
		}
		return gotten.token == t;
	}

}

class InvalidParserExceptionJson extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidParserExceptionJson(JsonTokenType expected, JsonTokenType found) {
		super("Expected: " + expected + ", found: " + found);
	}

	public InvalidParserExceptionJson(String s) {
		super(s);
	}
}

class InvalidParserExceptionYaml extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidParserExceptionYaml(YamlTokenType expected, YamlTokenType found) {
		super("Expected: " + expected + ", found: " + found);
	}

	public InvalidParserExceptionYaml(String s) {
		super(s);
	}
}
