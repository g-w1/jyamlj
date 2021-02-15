package jyamlj;

import java.util.ArrayList;
import java.util.List;

import jyamlj.JsonLexer.JsonTokenPair;
import jyamlj.JsonLexer.JsonTokenType;
import jyamlj.YamlLexer.TokenizedLine;

public class ParsedMap extends ParsedObject {

	public ParsedMap() {
		super(0);
		map = new ArrayList<MapPair>();
	}

	public ParsedMap(int indentLevel, List<JsonTokenPair> input, IntWrap i) throws InvalidParserExceptionJson {
		super(indentLevel);
		map = new ArrayList<MapPair>();
		expectJson(input, i, JsonTokenType.OpenBrace);
		if (peekJson(input, i, JsonTokenType.CloseBrace)) {
			i.value++;
			return;
		}
		while (input.get(i.value).token != JsonTokenType.CloseBrace) {
			String key = expectDataJson(input, i, JsonTokenType.String);
			expectJson(input, i, JsonTokenType.Colon);
			ParsedObject val = parseJsonCont(input, i);
			map.add(new MapPair(key, val));
			if (peekJson(input, i, JsonTokenType.CloseBrace)) {
				expectJson(input, i, JsonTokenType.CloseBrace);
				return;
			}
			expectJson(input, i, JsonTokenType.Comma);
		}
	}

	public ParsedMap(int indentLevel, /* TODO kinda hack but who cares XD */ ArrayList<TokenizedLine> input, IntWrap i)
			throws InvalidParserExceptionJson {
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

	public String toYamlString() {
		if (map.size() == 0)
			return "{}";
		if (map.size() == 1) {
			MapPair kv = map.get(0);
			return kv.key + ": " + kv.val.toYamlString() + "\n";
		}
		String l = new String();
		for (int j = 0; j < map.size(); j++) {
			MapPair mp = map.get(j);
			ParsedObject o = mp.val;
			l += mindent(mp.key);
			l += ": ";
			if (o.isYamlMultiline()) {
				l += "\n";
			}
			l += o.toYamlString();
			if (!(j == map.size() - 1))
				l += "\n";
		}
		return l;
	}

	public boolean isYamlMultiline() {
		if (map.size() == 0)
			return false;
		if (map.size() == 1)
			return map.get(0).val.isYamlMultiline();
		return true;
	}

}
