package jyamlj;

import java.util.ArrayList;

import jyamlj.JsonLexer.TokenPair;

public class Main {

	public static void main(String[] args) throws JsonLexer.InvalidLexerException, InvalidParserExceptionJson {
		String someJson = "[{\"jeff\":\"test\", \"jacob\":[1,2, 3, {\"a\": 1.1, \"b\":5}, [1,2,3]], \"thing\":[1,5.1,[{}]]}]";

		System.out.println("input: " + someJson);
		JsonLexer l = new JsonLexer(someJson);
		ArrayList<TokenPair> tpl = l.lex();
		ParsedObject o = ParsedObject.parseJsonRoot(tpl);
		System.out.println(o.toString(true));
		System.out.println(o.toString(false));
	}
}
