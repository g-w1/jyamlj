package jyamlj;

public class Parser {
	public enum Type { FromJson, FromYaml };
	private ParsedObject rootObject;
	private Type convertTo;
	private String input;
	private String output;
	public Parser(Type to, String input) {
		this.input = input;
		this.convertTo = to;
	}
	public String getOutput() {
		return output;
	}
	public void parse() {
		switch (convertTo) {
		case FromJson: 
			parseFromJson();
			break;
		case FromYaml:
			parseFromYaml();
			break;
		}
		
	}
	private void parseFromJson() {
		
	}
	private void parseFromYaml() {
		
	}

}
