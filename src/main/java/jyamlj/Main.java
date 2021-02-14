package jyamlj;

import java.util.ArrayList;

import jyamlj.YamlLexer.TokenizedLine;

public class Main {

	public static void main(String[] args) throws Exception {
//		String someJson = "[{\"jeff\":\"test\", \"jacob\":[1,2, 3, {\"a\": 1.1, \"b\":5}, [1,2,3]], \"thing\":[1,5.1,[{}]]}]";
		String someYaml = "a: thing \n" + "v:\n" + "\t- v:\n" + "\t\t- 1\n";
//		 System.out.println("input: " + someJson);
		System.out.println(someYaml);
		YamlLexer l = new YamlLexer();
		ArrayList<TokenizedLine> tpl = l.lex(someYaml);
		System.out.println("lexed: " + tpl);
//		JsonLexer l = new JsonLexer();
//		ArrayList<TokenPair> tpl = l.lex(someJson);
//		ParsedObject o = ParsedObject.parseJsonRoot(tpl);
//		System.out.println(o.toString(true));
//		System.out.println(o.toString(false));

	}
}
