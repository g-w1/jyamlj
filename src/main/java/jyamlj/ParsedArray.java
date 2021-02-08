package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedArray extends ParsedObject {

	public List<ParsedObject> arr;

	public ParsedObject parse(final List<TokenPair> input, IntWrap i) throws InvalidParserException {
		arr = new ArrayList<ParsedObject>();
		expect(input, i, TokenType.OpenBrack);
		if (peek(input, i, TokenType.CloseBrack)) {
			i.value++;
			return this;
		}
		while (input.get(i.value).token != TokenType.CloseBrack) {
			ParsedObject val = new ParsedObject().parse(input, i);
			arr.add(val);
			if (peek(input, i, TokenType.Comma)) {
				i.value++;
			} else {
				expect(input, i, TokenType.CloseBrack);
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
