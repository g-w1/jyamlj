package jyamlj;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws Exception {
		// JSON
		String someJson = "[{\"jeff\":\"test\", \"jacob\":[1,2, 3, {\"a\": 1.1, \"b\":5}, [1,2,3]], \"thing\":[1,5.1,[{}]]}]";
		System.out.println("input: " + someJson);
		JsonLexer l = new JsonLexer();
		ArrayList<jyamlj.JsonLexer.TokenPair> tpl = l.lex(someJson);
		ParsedObject o = ParsedObject.parseJsonRoot(tpl);
		System.out.println(o.toString(true));

		// YAML:
//		String someYaml = "a: thing \n" + "v:\n" + "\t- v:\n" + "\t\t- 1\n";
//		System.out.println(someYaml);
//		YamlLexer l = new YamlLexer();
//		ArrayList<TokenizedLine> tpl = l.lex(someYaml);
//		System.out.println("lexed: " + tpl);

	}
}
