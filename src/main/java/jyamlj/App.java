/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package jyamlj;

import java.util.ArrayList;

import jyamlj.JsonLexer.TokenPair;

public class App {

	public static void main(String[] args) throws JsonLexer.InvalidInputException {
		String someJson = "{\"arst\": \"bruh\", \"vlang\": 123}";
		JsonLexer l = new JsonLexer(someJson);
		ArrayList<TokenPair> tpl = l.lex();
		for (TokenPair t : tpl) {
			System.out.println(t);
		}
	}
}
