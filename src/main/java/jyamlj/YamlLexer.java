package jyamlj;

import java.util.ArrayList;

public class YamlLexer {
	enum LineType {
		ArrayElem, MapElem
	}

	enum YamlTokenType {
		Ident, Num, Colon, Dash, // TODO maybe include JsonTokens in this for using json in yaml
	}

	enum State {
		Reg, InIdent, InNum
	}

	class YamlTokenPair {
		String data;
		YamlTokenType token;

		public YamlTokenPair(YamlTokenType t, String s) {
			token = t;
			data = s;
		}

		public String toString() {
			String s = "" + token;
			if (data != null) {
				s += " " + data + ", ";
			} else {
				s += ", ";
			}
			return s;
		}
	}

	class TokenizedLine {
		public int indent;
		public ArrayList<YamlTokenPair> tokens;

		public TokenizedLine(int i, ArrayList<YamlTokenPair> line) {
			indent = i;
			tokens = line;
		}

		public String toString() {
			String s = "line(" + indent + "): ";
			for (YamlTokenPair t : tokens) {
				s += t;
			}
			s += "\n";
			return s;
		}

	}

	private String tempTokenizingString;

	public YamlLexer() {
		tempTokenizingString = new String();
	}

	public ArrayList<TokenizedLine> lex(String input) throws Exception {
		State state = State.Reg;
		ArrayList<TokenizedLine> lines = new ArrayList<TokenizedLine>();
		Character c;
		int col = 0;
		int indentLevel = 0;
		boolean sawCharOnThisLine;
		sawCharOnThisLine = false;
		ArrayList<YamlTokenPair> line;
		line = new ArrayList<YamlTokenPair>();
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			// TODO validate mixing of tabs and spaces

			col++;

			switch (state) {
			case Reg: {
				if (!sawCharOnThisLine)
					if (c == ' ') {
						indentLevel++;
						continue;
					} else if (c == '\t') {
						indentLevel += 4;
						continue;
					} else
						sawCharOnThisLine = true;
				if (c == '\n') {
					lines.add(new TokenizedLine(indentLevel, line));
					line = new ArrayList<YamlTokenPair>();
					col = 0;
					sawCharOnThisLine = false;
					indentLevel = 0;
					continue;
				}
				switch (c) {
				case '\n':
				case ' ':
				case '\t':
					continue;
				case ':':
					line.add(new YamlTokenPair(YamlTokenType.Colon, null));
					continue;
				case '-':
					line.add(new YamlTokenPair(YamlTokenType.Dash, null));
					continue;
				default:
					if (Character.isDigit(c) || c == '.') {
						state = State.InNum;
						i -= 1; // so that we don't have to do the eating of the number here
						continue;
					}
					if (Character.isAlphabetic(c) || c == '_') { // TODO more cases here <-
						state = State.InIdent;
						i -= 1;
						continue;
					}
					throw new InvalidLexerException("Invalid Character: '" + c + "' " + lines.size() + 1 + ":" + col);
				}
			}
			case InNum: {
				if (Character.isDigit(c) || c == '.') {
					tempTokenizingString += c;
					continue;
				}
				i -= 1;
				state = State.Reg;
				line.add(new YamlTokenPair(YamlTokenType.Num, tempTokenizingString));
				tempTokenizingString = new String();
				continue;
			}
			case InIdent: {
				if (Character.isAlphabetic(c) || c == '_') {
					tempTokenizingString += c;
					continue;
				}
				i -= 1;
				state = State.Reg;
				line.add(new YamlTokenPair(YamlTokenType.Ident, tempTokenizingString));
				tempTokenizingString = new String();
				continue;
			}
			}
		}
		switch (state) {
		case Reg:
			break;
		case InNum:
			line.add(new YamlTokenPair(YamlTokenType.Num, tempTokenizingString));
			break;
		case InIdent:
			line.add(new YamlTokenPair(YamlTokenType.Ident, tempTokenizingString));
			break;
		}
		if (line.size() != 0) {
			lines.add(new TokenizedLine(indentLevel, line));
		}
		return lines;
	}

}
