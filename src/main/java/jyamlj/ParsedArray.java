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
			ParsedObject val = ParsedObject.parseJsonCont(input, i);
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

	// TODO use i
	public String toJsonString(int i) {
		String s = "[";
		for (int j = 0; j < arr.size(); j++) {
			s += " " + arr.get(j).toJsonString(i+1) + ((j == arr.size() - 1) ? " " : ",");
		}
		s += "]";
		return s;
	}
}
