package comp;

public class Token {
	TokenType type;
	Object value;
	int ordinal;
	int line;
	int col;
	public Token(TokenType type, Object value, int ordinal, int line, int col) {
		super();
		this.type = type;
		this.value = value;
		this.ordinal = ordinal;
		this.line = line;
		this.col = col;
	}
	@Override
	public String toString() {
		return String.format("%8s[%04d, %04d] (%04d, %20s) {%s}\n",
					"",	line, col, ordinal, type.name(), value);
	}

}
