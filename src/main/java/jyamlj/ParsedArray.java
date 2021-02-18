package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.JsonTokenPair;
import jyamlj.JsonLexer.JsonTokenType;

public class ParsedArray extends ParsedObject {

	public List<ParsedObject> arr;

	public ParsedArray(int indentLevel, List<JsonTokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		super(indentLevel);
		arr = new ArrayList<ParsedObject>();
		expectJson(input, i, JsonTokenType.OpenBrack);
		if (peekJson(input, i, JsonTokenType.CloseBrack)) {
			i.value++;
			return;
		}
		while (input.get(i.value).token != JsonTokenType.CloseBrack) {
			ParsedObject val = parseJsonCont(input, i);
			arr.add(val);
			if (peekJson(input, i, JsonTokenType.CloseBrack)) {
				expectJson(input, i, JsonTokenType.CloseBrack);
				return;
			}
			expectJson(input, i, JsonTokenType.Comma);
		}
		return;
	}

	public String toJsonString() {
		String s = "[\n";
		for (int j = 0; j < arr.size(); j++) {
			s += indent(arr.get(j).toJsonString()) + ((j == arr.size() - 1) ? "\n" : ",\n");
		}
		s += mindent("]");
		return s;
	}

	public String toYamlString() {
		if (arr.size() == 0)
			return "[]";
		String l = new String();
		for (int j = 0; j < arr.size(); j++) {
			ParsedObject o = arr.get(j);
			l += mindent("- ");
			if (o.isYamlMultiline())
				l += "\n";
			l += o.toYamlString();
			if (!(j == arr.size() - 1))
				l += "\n";
		}
		return l;
	}

	public boolean isYamlMultiline() {
		if (arr.size() == 0)
			return false;

		return true;
	}
}
