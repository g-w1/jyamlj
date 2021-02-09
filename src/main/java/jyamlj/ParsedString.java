package jyamlj;

public class ParsedString extends ParsedObject {
	String data;

	public ParsedString(String data) {
		this.data = data;
	}

	ParsedObject parse() {
		return this;
	}

	public String toJsonString(int i) {
		return "\"" + this.data + "\"";
	}
}
