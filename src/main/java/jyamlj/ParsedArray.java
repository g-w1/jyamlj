package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedArray extends ParsedObject {

	public List<ParsedObject> arr;

	public ParsedObject parseJson(final List<TokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		arr = new ArrayList<ParsedObject>();
		expectJson(input, i, TokenType.OpenBrack);
		if (peekJson(input, i, TokenType.CloseBrack)) {
			i.value++;
			return this;
		}
		while (input.get(i.value).token != TokenType.CloseBrack) {
			ParsedObject val = new ParsedObject().parseJson(input, i);
			arr.add(val);
			if (peekJson(input, i, TokenType.Comma)) {
				i.value++;
			} else {
				expectJson(input, i, TokenType.CloseBrack);
				return this;
			}
		}
		return this;
	}

	public String toString() {
		String s = "[";
		for (int i = 0; i < arr.size(); i++) {
			s += " " + arr.get(i) + ((i == arr.size() - 1) ? " " : ",");
		}
		s += "]";
		return s;
	}
}
