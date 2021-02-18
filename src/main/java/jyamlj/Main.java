package jyamlj;

import java.util.ArrayList;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
        String help = "jyamlj:\n"+
        "usage: jyamlj [-j] [-y]\n"+
        "-j: output json (default)\n"+
        "-y: output yaml\n"+
        "pipe the json you want to parse and render to stdin\n";
        boolean toJson = true;
        try {
            Scanner scanner = new Scanner(System.in);
            String someJson = "";
            while (scanner.hasNextLine()) {
                someJson += scanner.nextLine();
            }
            someJson = someJson.trim();
            if (someJson.length() == 0) {
                System.out.println("invalid Json, please enter something to stdin");
                return;
            }
            if (args.length > 1)
                System.out.println("Invalid amount of arguments. Expected 0 or 1");
            if (args.length == 1) {
                if (args[0].trim().equals("-y")) {
                    toJson = false;
                }
            }
        	JsonLexer l = new JsonLexer();
        	ArrayList<jyamlj.JsonLexer.JsonTokenPair> tpl = l.lex(someJson);
        	ParsedObject o = ParsedObject.parseJsonRoot(tpl);
        	System.out.println(o.toString(toJson));
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
	}
}
