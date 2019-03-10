package comp;

import java.util.Deque;

public class Lexer {

	private Deque<Token> tokens;
	public Lexer(String args) {
		tokens = new Scan(args).getTokens();
/*		while (sc.getTokens().iterator().hasNext()){
			s = (TokenType)sc.getTokens().pollLast();
			System.out.printf("%8s[%04d, %04d] (%04d, %20s) {%s}\n",
					"",	s.line, s.column, s.ordinal(), s.name(), s.toString());
		}*/
		while(tokens.iterator().hasNext())
			System.out.print(tokens.pollLast());
	}
	
	public static void main(String[] args) {
		new Lexer(args[0]);
	}
}
