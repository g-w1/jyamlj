package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.JsonTokenPair;
import jyamlj.JsonLexer.JsonTokenType;

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
