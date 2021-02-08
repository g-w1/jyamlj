package jyamlj;

import java.util.ArrayList;

public class JsonLexer {
	enum State {
		Reg, InStrLit, InNum
	}

	class TokenPair {
		String data;
		TokenType token;

		public TokenPair(TokenType t, String s) {
			token = t;
			data = s;
		}

		public String toString() {
			String s = "" + token;
			if (data != null) {
				s += " data: " + data;
			}
			return s;
		}
	}

	enum TokenType {
		OpenBrack, CloseBrack, OpenBrace, CloseBrace, Number, String, Comma, Colon
	}

	class InvalidInputException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidInputException(String message) {
			super(message);
		}
	}

	private ArrayList<TokenPair> tokens;
	private State state;
	private String tempTokenizingString;
	private String input;

	public JsonLexer(String input) {
		this.input = input;
		tokens = new ArrayList<TokenPair>();
		state = State.Reg;
		tempTokenizingString = new String();
	}

	public ArrayList<TokenPair> lex() throws InvalidInputException {
		Character c;
		int line = 0, col = 0;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			col++;
			if (c == '\n') {
				line++;
				col = 0;
			}
			switch (state) {
			case Reg: {
				switch (c) {
				case '\n':
				case ' ':
				case '\t':
					continue;
				case '{':
					// null because we don't have any data
					tokens.add(new TokenPair(TokenType.OpenBrace, null));
					continue;
				case '}':
					tokens.add(new TokenPair(TokenType.CloseBrace, null));
					continue;
				case '[':
					tokens.add(new TokenPair(TokenType.OpenBrack, null));
					continue;
				case ']':
					tokens.add(new TokenPair(TokenType.CloseBrack, null));
					continue;
				case ',':
					tokens.add(new TokenPair(TokenType.Comma, null));
					continue;
				case ':':
					tokens.add(new TokenPair(TokenType.Colon, null));
					continue;
				case '"':
					state = State.InStrLit;
					continue;
				default:
					if (Character.isDigit(c)) {
						state = State.InNum;
						i -= 1; // so that we don't have to do the eating of the number here
						continue;
					}
					throw new InvalidInputException("Invalid Character: '" + c + "' " + line + ":" + col);
				}
			}
			case InStrLit: {
				if (c == '"') {
					tokens.add(new TokenPair(TokenType.String, tempTokenizingString));
					tempTokenizingString = new String();
					state = State.Reg;
					continue;
				}
				tempTokenizingString += c;
				continue;
			}
			case InNum: {
				if (Character.isDigit(c)) {
					tempTokenizingString += c;
					continue;
				}
				i -= 1;
				state = State.Reg;
				tokens.add(new TokenPair(TokenType.Number, tempTokenizingString));
				tempTokenizingString = new String();
				continue;
			}
			}

		}
		return tokens;
	}
}
