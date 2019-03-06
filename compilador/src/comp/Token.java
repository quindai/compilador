package comp;

public class Token {
	public void nextToken(){}
	// códigos para os tokens
	public static enum MyTokens{
		EQ("=="), ASSIGN ("="), NE ("<>"), LT ("<"), LE ("<="), GT (">"), 
		GE (">="), PLUS ("+"), MINUS ("-"), UNARY ("~"), MULT ("*"),
		POW ("**"), DIVIDE ("/"), RPAREN (")"), LPAREN ("("), RBRAC ("}"),
		LBRAC ("{"), COLON (":"), SEMICOLON (";"), MYINT ("int"), 
		MYREAL ("real"), MYSTRING ("string"), MYCHAR ("char"), MYBOOL ("bool"), 
		MYARRAY ("array"), IF ("if"), ELSE ("else"), WHILE ("while"), 
		FROM ("from"), REPEAT ("repeat"), MAIN ("main"), PGM ("pgm"), 
		END_PGM ("end_pgm"), AND ("and"), MOD ("mod"), INTDIV ("div"), 
		TO ("to"), TRUE ("true"), FALSE ("false"), OR ("or"), NOT ("not"), 
		PRINT ("print"), COMMENT ("//"), FUNC ("func");
		
		private int code;
		private String value;

		MyTokens(String value){
				//this.code = code;
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
