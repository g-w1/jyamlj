package jyamlj;

public class ParsedString extends ParsedObject {
	String data;

	public ParsedString(String data, int indentLevel) {
		super(indentLevel);
		this.data = data;
	}

	public String toJsonString() {
		return "\"" + this.data + "\"";
	}
}
