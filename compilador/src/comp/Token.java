package comp;

public class Token {
	public void nextToken(){}
	// codigos para os tokens
	public static enum MyTokens{
		// palavras reservadas
		MYINT ("int"), 	MYREAL ("real"), MYSTRING ("string"), MYCHAR ("char"), MYBOOL ("bool"), 
		MYARRAY ("array"), IF ("if"), ELSE ("else"), WHILE ("while"),  MYRETURN ("return"),
		FROM ("from"), REPEAT ("repeat"), MAIN ("main"), PGM ("pgm"), 
		END_PGM ("end_pgm"), AND ("and"), MOD ("mod"), INTDIV ("div"), 
		TO ("to"), TRUE ("true"), FALSE ("false"), OR ("or"), NOT ("not"), 
		PRINT ("print"), FUNC ("func"), IDENTIFIER ("identifier"),
		INTCONSTANT ("intconstant"),
		
		// simbolos especiais
		COMMENT ("//"),	EQ("=="), ASSIGN ("="), NE ("<>"), LT ("<"), LE ("<="), GT (">"), 
		GE (">="), PLUS ("+"), MINUS ("-"), UNARY ("~"), MULT ("*"),
		POW ("**"), SRBRAC ("]"), SLBRAC ("["), DIVIDE ("/"), RPAREN (")"), LPAREN ("("), RBRAC ("}"),
		LBRAC ("{"), COLON (":"), SEMICOLON (";"),  COMA (",");
		
		
		private static final int FIRST_RESERVED_INDEX = MYINT.ordinal();
		private static final int LAST_RESERVED_INDEX = INTCONSTANT.ordinal();
		
		private static final int FIRST_SPECIAL_INDEX = COMMENT.ordinal();
		private static final int LAST_SPECIAL_INDEX = COMA.ordinal();
		 
		String value;
		int line, column;

		MyTokens(String value){
				//this.code = code;
				this.value = value;
		}
	
		public void setIdentConstValue(String value) {
			this.value = value;
		}
		
		private MyTokens() {// TODO apagar construtor
		}
		 @Override
		    public String toString() {
		        return value;
		    }
	}
	
	
}
