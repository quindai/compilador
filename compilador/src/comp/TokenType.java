package comp;

public enum TokenType {
	
	// codigos para os tokens
		// palavras reservadas
		PGM ("pgm"), 
		RD_INT ("int"), RD_REAL ("real"), RD_STRING ("string"), RD_CHAR ("char"), 
		RD_BOOL ("bool"), RD_VOID("void"),
		RD_ARRAY ("array"), 
		
		// comandos
		IF ("if"), WHILE ("while"), REPEAT ("repeat"), ELSE ("else"),   RD_RETURN ("return"),
		FROM ("from"),  MAIN ("main"),  
		END_PGM ("end_pgm"),  
		TO ("to"), TRUE ("true"), FALSE ("false"),  
		PRINT ("print"), READ("read"), FUNC ("func"), STEP ("step"),
		RD_ERROR ("rd_error"),
		
		//identificador
		IDENTIFIER ("identifier"),
		
		//Literais
		LIT_INT ("lit_int"), LIT_CHAR("lit_char"), LIT_STRING("lit_string"), LIT_BOOL("lit_bool"), 
		LIT_REAL ("lit_real"),
		
		//Operadores aritmeticos
		EQ("=="),UNARY ("~"), MULT ("*"), POW ("**"), PLUS ("+"), MINUS ("-"),
		MOD ("mod"), INTDIV ("div"), 
		
		//Operadores logicos
		OR ("or"), NOT ("not"),AND ("and"),
		
		//Operadores relacionais
		NE ("<>"), LT ("<"), LE ("<="), GT (">"),GE (">="),
		
		// simbolos especiais
		COMMENT ("//"),	ASSIGN ("="),  
		SRBRAC ("]"), SLBRAC ("["), DIVIDE ("/"), RPAREN (")"), LPAREN ("("), RBRAC ("}"),
		LBRAC ("{"), COLON (":"), SEMICOLON (";"),  COMA (","), DOUBLE_QUOTES ("\""),
	
		RD_NULL("ε");
		
		private static final int FIRST_RESERVED_INDEX = PGM.ordinal();
		private static final int LAST_RESERVED_INDEX = RD_ERROR.ordinal();
		
		private static final int FIRST_SPECIAL_INDEX = COMMENT.ordinal();
		private static final int LAST_SPECIAL_INDEX = DOUBLE_QUOTES.ordinal();
		
		public static final int FIRST_LITERAL_INDEX = LIT_INT.ordinal();
		public static final int LAST_LITERAL_INDEX = LIT_REAL.ordinal();
		
		public static final int FIRST_TYPE_INDEX = RD_INT.ordinal();
		public static final int LAST_TYPE_INDEX = RD_VOID.ordinal();
		 
		String value;

		TokenType(String value){
				this.value = value;
		}
		
		 @Override
	    public String toString() {
	        return value;
	    }
	
}
