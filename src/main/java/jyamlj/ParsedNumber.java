package jyamlj;

public class ParsedNumber extends ParsedObject {
	String data;

	public ParsedNumber(String data) {
		this.data = data;
	}

	ParsedObject parse() {
		return this;
	}

	public String toString() {
		return this.data;
	}

}
