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
	
	public Token(TokenType type) {
		this.type = type;
		this.value = type.value;
		this.ordinal = type.ordinal();
		this.line = -1;
		this.col = -1;
	}
	
	public String nextToken() {
		return toString();
	}
	
	@Override
	public String toString() {
		return String.format("%8s[%04d, %04d] (%04d, %20s) {%s}",
					"",	line, col, ordinal, type.name(), value);
	}

	public TokenType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	
	/*public static void main(String[] args) {
		for(TokenType t: TokenType.values())
			System.out.println(t.ordinal() + " "+ t +" "+t.name() );
	}*/
}
