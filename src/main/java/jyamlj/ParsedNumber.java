package jyamlj;

public class ParsedNumber extends ParsedObject {
	String data;

	public ParsedNumber(String data, int indentLevel) {
		super(indentLevel);
		this.data = data;
	}

	public String toJsonString() {
		return this.data;
	}

}
