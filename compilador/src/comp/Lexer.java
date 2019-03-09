package comp;

public class Lexer {

	public Lexer(String args) {
		Scan sc = new Scan(args);
		Token.MyTokens s;
		while (sc.getTokens().iterator().hasNext()){
			s = (Token.MyTokens)sc.getTokens().pollLast();
			System.out.printf("%8s[%04d, %04d] (%04d, %20s) {%s}\n",
					"",	s.line, s.column, s.ordinal(), s.name(), s.toString());
		}
	}
	
	public static void main(String[] args) {
		new Lexer(args[0]);
	}
}
