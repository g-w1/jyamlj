package jyamlj;

import java.util.ArrayList;

public class JsonLexer {
	public enum State {
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

	private String tempTokenizingString;

	public JsonLexer() {
		tempTokenizingString = new String();
	}

	public ArrayList<TokenPair> lex(String input) throws InvalidLexerException {
		ArrayList<TokenPair> tokens = new ArrayList<TokenPair>();
		State state = State.Reg;
		Character c;
		int line = 0, col = 0;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '\n') {
				line++;
				col = 0;
			}
			col++;
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
					if (Character.isDigit(c) || c == '.') {
						state = State.InNum;
						i -= 1; // so that we don't have to do the eating of the number here
						continue;
					}
					throw new InvalidLexerException("Invalid Character: '" + c + "' " + line + ":" + col);
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
				if (Character.isDigit(c) || c == '.') {
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
