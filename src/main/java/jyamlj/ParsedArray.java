package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedArray extends ParsedObject {

	public ParsedArray(int indentLevel) {
		super(indentLevel);
	}

	public List<ParsedObject> arr;

	public ParsedObject parseJson(final List<TokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		arr = new ArrayList<ParsedObject>();
		expectJson(input, i, TokenType.OpenBrack);
		if (peekJson(input, i, TokenType.CloseBrack)) {
			i.value++;
			return this;
		}
		while (input.get(i.value).token != TokenType.CloseBrack) {
			ParsedObject val = parseJsonCont(input, i);
			arr.add(val);
			if (peekJson(input, i, TokenType.CloseBrack)) {
				expectJson(input, i, TokenType.CloseBrack);
				return this;
			}
			expectJson(input, i, TokenType.Comma);
		}
		return this;
	}

	public String toJsonString() {
		String s = "[\n";
		for (int j = 0; j < arr.size(); j++) {
			s += indent(arr.get(j).toJsonString()) + ((j == arr.size() - 1) ? "\n" : ",\n");
		}
		s += mindent("]");
		return s;
	}
}
