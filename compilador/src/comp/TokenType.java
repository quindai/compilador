package comp;

public enum TokenType {
	
	// codigos para os tokens
		// palavras reservadas
		PGM ("pgm"), RD_INT ("int"), RD_REAL ("real"), RD_STRING ("string"), RD_CHAR ("char"), 
		RD_BOOL ("bool"), 
		RD_ARRAY ("array"), IF ("if"), ELSE ("else"), WHILE ("while"),  RD_RETURN ("return"),
		FROM ("from"), REPEAT ("repeat"), MAIN ("main"),  
		END_PGM ("end_pgm"), AND ("and"), MOD ("mod"), INTDIV ("div"), 
		TO ("to"), TRUE ("true"), FALSE ("false"), OR ("or"), NOT ("not"), 
		PRINT ("print"), FUNC ("func"), IDENTIFIER ("identifier"), STEP ("step"),
		INTCONSTANT ("intconstant"), RD_ERROR ("rd_error"),
		
		// simbolos especiais
		COMMENT ("//"),	EQ("=="), ASSIGN ("="), NE ("<>"), LT ("<"), LE ("<="), GT (">"), 
		GE (">="), PLUS ("+"), MINUS ("-"), UNARY ("~"), MULT ("*"),
		POW ("**"), SRBRAC ("]"), SLBRAC ("["), DIVIDE ("/"), RPAREN (")"), LPAREN ("("), RBRAC ("}"),
		LBRAC ("{"), COLON (":"), SEMICOLON (";"),  COMA (","), DOUBLE_QUOTES ("\"");
		
		private static final int FIRST_RESERVED_INDEX = PGM.ordinal();
		private static final int LAST_RESERVED_INDEX = RD_ERROR.ordinal();
		
		private static final int FIRST_SPECIAL_INDEX = COMMENT.ordinal();
		private static final int LAST_SPECIAL_INDEX = DOUBLE_QUOTES.ordinal();
		 
		String value;

		TokenType(String value){
				this.value = value;
		}
		
		 @Override
	    public String toString() {
	        return value;
	    }
	
}
