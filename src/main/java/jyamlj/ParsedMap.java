package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.TokenPair;
import jyamlj.JsonLexer.TokenType;

public class ParsedMap extends ParsedObject {
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
			ParsedObject val = ParsedObject.parseJsonCont(input, i);
			map.add(new MapPair(key, val));
			if (peekJson(input, i, TokenType.Comma)) {
				i.value++;
			} else {
				expectJson(input, i, TokenType.CloseBrace);
				return this;
			}
		}
		return this;
	}

	public String toJsonString(int i) {
		String s = "{";
		for (int j = 0; j < map.size(); j++) {
			s += " \"" + map.get(j).key + "\" : " + map.get(j).val.toJsonString(j + 1) + ((j == map.size() - 1) ? " " : " ,");
		}
		s += "}";
		return s;
	}
}
