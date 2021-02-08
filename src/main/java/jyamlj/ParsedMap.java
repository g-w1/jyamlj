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

	public ParsedObject parse(final List<TokenPair> input, IntWrap i) throws InvalidParserException {
		map = new ArrayList<MapPair>();
		expect(input, i, TokenType.OpenBrace);
		if (peek(input, i, TokenType.CloseBrace)) {
			i.value++;
			return this;
		}
		while (input.get(i.value).token != TokenType.CloseBrace) {
			String key = expectData(input, i, TokenType.String);
			expect(input, i, TokenType.Colon);
			ParsedObject val = new ParsedObject().parse(input, i);
			map.add(new MapPair(key, val));
			if (peek(input, i, TokenType.Comma)) {
				i.value++;
			} else {
				expect(input, i, TokenType.CloseBrace);
				return this;
			}
		}
		return this;
	}

	public String toString() {
		String s = "{";
		for (int i = 0; i < map.size(); i++) {
			s += " \"" + map.get(i).key + "\" : " + map.get(i).val.toString() + ((i == map.size() - 1) ? " " : " ,");
		}
		s += "}";
		return s;
	}
}
