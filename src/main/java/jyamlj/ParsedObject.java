package jyamlj;

import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedObject {

	public ParsedObject() {
	}

	public ParsedObject parse(final List<TokenPair> input) throws InvalidParserException {
		IntWrap i = new IntWrap(0);
		TokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap().parse(input, i);
		case OpenBrack:
			return new ParsedArray().parse(input, i);
		default:
			throw new InvalidParserException("Expected { [ , found " + t);
		}
	}

	public ParsedObject parse(final List<TokenPair> input, IntWrap i) throws InvalidParserException {
		TokenPair t;
		t = input.get(i.value);
		switch (t.token) {
		case OpenBrace:
			return new ParsedMap().parse(input, i);
		case OpenBrack:
			return new ParsedArray().parse(input, i);
		case Number:
			i.value++;
			return new ParsedNumber(t.data).parse();
		case String:
			i.value++;
			return new ParsedString(t.data).parse();
		default:
			throw new InvalidParserException("Expected { [ Number or String, found " + t);
		}
	}

	public static void expect(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserException {
		TokenType gotten;
		try {
			gotten = input.get(i.value).token;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserException("A paren or bracket is not balenced");
		}
		if (gotten != t) {
			throw new InvalidParserException(t, gotten);
		}
		i.value++;
	}

	public static String expectData(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserException {
		TokenPair gotten;
		try {
			gotten = input.get(i.value);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserException("A paren or bracket is not balenced");
		}
		if (gotten.token != t) {
			throw new InvalidParserException(t, gotten.token);
		}
		i.value++;
		return gotten.data;
	}

	public static Boolean peek(final List<TokenPair> input, IntWrap i, TokenType t) throws InvalidParserException {
		TokenPair gotten;
		try {
			gotten = input.get(i.value);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidParserException("A paren or bracket is not balenced");
		}
		return gotten.token == t;
	}

}

class InvalidParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidParserException(TokenType expected, TokenType found) {
		super("Expected: " + expected + ", found: " + found);
	}

	public InvalidParserException(String s) {
		super(s);
	}
}
