package jyamlj;

public class ParsedString extends ParsedObject {
	String data;

	public ParsedString(String data) {
		this.data = data;
	}

	ParsedObject parse() {
		return this;
	}

	public String toString() {
		return "\"" + this.data + "\"";
	}
}
