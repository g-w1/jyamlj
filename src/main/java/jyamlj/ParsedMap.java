package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedMap extends ParsedObject {
	public ParsedMap(int indentLevel) {
		super(indentLevel);
	}

	class MapPair {
		public String key;
		public ParsedObject val;

		public MapPair(String key, ParsedObject val) {
			this.key = key;
			this.val = val;
		}
	}

	public List<MapPair> map;

	public ParsedObject parseJson(final List<TokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		map = new ArrayList<MapPair>();
		expectJson(input, i, TokenType.OpenBrace);
		if (peekJson(input, i, TokenType.CloseBrace)) {
			i.value++;
			return this;
		}
		while (input.get(i.value).token != TokenType.CloseBrace) {
			String key = expectDataJson(input, i, TokenType.String);
			expectJson(input, i, TokenType.Colon);
			ParsedObject val = parseJsonCont(input, i);
			map.add(new MapPair(key, val));
			if (peekJson(input, i, TokenType.CloseBrace)) {
				expectJson(input, i, TokenType.CloseBrace);
				return this;
			}
			expectJson(input, i, TokenType.Comma);
		}
		return this;
	}

	public String toJsonString() {
		String s = "{\n";
		for (int j = 0; j < map.size(); j++) {
			s += indent("\"");
			s += map.get(j).key + "\": " + map.get(j).val.toJsonString();
			s += ((j == map.size() - 1) ? "" : ",") + "\n";
		}
		s += mindent("}");
		return s;
	}
}
