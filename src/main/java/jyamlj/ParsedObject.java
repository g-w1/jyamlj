package jyamlj;

import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public abstract class ParsedObject {

	public static ParsedObject parseJsonRoot(final List<TokenPair> input) throws InvalidParserExceptionJson {
		IntWrap i = new IntWrap(0);
		TokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap().parseJson(input, i);
		case OpenBrack:
			return new ParsedArray().parseJson(input, i);
		default:
			throw new InvalidParserExceptionJson("Expected '{' '[' , found " + t);
		}
	}

	public static ParsedObject parseJsonCont(final List<TokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		TokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap().parseJson(input, i);
		case OpenBrack:
			return new ParsedArray().parseJson(input, i);
		case Number:
			i.value++;
			return new ParsedNumber(t.data).parse();
		case String:
			i.value++;
			return new ParsedString(t.data).parse();
		default:
			throw new InvalidParserExceptionJson("Expected '{' '[' Number or String, found " + t);
		}
	}

	public static void expectJson(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserExceptionJson {
		TokenType gotten;
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

	public static String expectDataJson(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserExceptionJson {
		TokenPair gotten;
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

	public static Boolean peekJson(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserExceptionJson {
		TokenPair gotten;
		try {
			gotten = input.get(i.value);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserExceptionJson("A paren or bracket is not balenced");
		}
		return gotten.token == t;
	}

	public String toJsonString(int i) {
    	return null;
	}

	public String toString(boolean isJson) {
    	if (isJson)
        	return this.toJsonString(0);
    	else
        	return null;
	}
}

class InvalidParserExceptionJson extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidParserExceptionJson(TokenType expected, TokenType found) {
		super("Expected: " + expected + ", found: " + found);
	}

	public InvalidParserExceptionJson(String s) {
		super(s);
	}
}
