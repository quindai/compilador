package comp.grammar;

import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;

import comp.Lexer;
import comp.Token;
import static comp.TokenType.*;

/**
 * 
 * @author quindai LL1 descent predictive recursive
 * @see https://www.craftinginterpreters.com/parsing-expressions.html
 */
public class Grammar {

	private Deque<Token> tokens = new ArrayDeque<>();
	Token lookahead;
	String aceita = "";

	String EPSILON = "ε";

	private void S() throws ParseException {
		System.out.println("S = 'pgm' Decl MainDecl 'end_pgm'");
		
		lookahead = tokens.pollLast();
		if(lookahead.getOrdinal() == 0) {
			printAccepted();
			Decl();
			MainDecl();
			lookahead = tokens.pollLast();
			if(lookahead.getOrdinal() == END_PGM.ordinal())
				printAccepted();
			else throw new ParseException(String.format("Simbolo 'end_pgm' esperado, encontrou '%s'", 
					lookahead.getValue()), 
					1 );
		} else {
			throw new ParseException(String.format("Simbolo 'pgm' esperado, encontrou '%s'", 
					lookahead.getValue()), 
					1 );
		}
	}
	
	private void printAccepted() {
		System.out.printf("%10s"+lookahead.getValue() +" ", "");
	}
	
	private void MainDecl() throws ParseException {
		System.out.println("\nMainDecl = 'main' '{' MainBody '}'");
		
		lookahead = tokens.pollLast();
		if(lookahead.getOrdinal() == MAIN.ordinal()) {
			printAccepted();
			lookahead = tokens.pollLast();
			if(lookahead.getOrdinal() == LBRAC.ordinal()) {
				printAccepted();
				
				MainBody();
				
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == RBRAC.ordinal())
					printAccepted();
				else throw new ParseException(String.format("Simbolo '}' esperado, encontrou '%s'", 
						lookahead.getValue()), 
						1 );
			} else throw new ParseException(String.format("Simbolo '{' esperado, encontrou '%s'", 
					lookahead.getValue()), 
					1 );
		} else throw new ParseException(String.format("Simbolo 'main' esperado, encontrou '%s'", 
				lookahead.getValue()), 
				1 );
	}
	
	private void MainBody() throws ParseException {
		VariableDecl();
		STMTR();
	}

	private void STMTR() {
		
	}
	
	private void Decl() throws ParseException {
		VariableDecl();
		FunctionDecl();
	}
	
	private void FunctionDecl() {
		/*while (tokens.iterator().hasNext()) {
			if (tokens.isEmpty())
				lookahead = new Token(RD_NULL);
			else {
				lookahead = tokens.pollLast();

				if (lookahead.getType().ordinal() == 0)
					System.out.println("S");

			}
			System.out.println(lookahead);
		}*/
	}

	private void Tipo() throws ParseException {
		lookahead = tokens.pollLast();
		if (lookahead.getOrdinal() > 0 && lookahead.getOrdinal() < 6)
			printAccepted();
		else throw new ParseException(String
				.format("Simbolo 'int', 'real', 'char', 'string' esperado, encontrou '%s'", 
				lookahead.getValue()), 
				1 );
	}
	
	private void VARIABLE() throws ParseException {
		lookahead = tokens.pollLast();
		if(lookahead.getOrdinal() == IDENTIFIER.ordinal()) {
			printAccepted();
			lookahead = tokens.pollLast();
			if(lookahead.getOrdinal() == SLBRAC.ordinal()) {//vetor [
				printAccepted();
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == LIT_INT.ordinal() ||
						lookahead.getOrdinal() == LIT_REAL.ordinal()) {
					printAccepted();
					
					lookahead = tokens.pollLast();
					if(lookahead.getOrdinal() == SRBRAC.ordinal()) {//vetor ]
						printAccepted();
					} else throw new ParseException(String
							.format("Simbolo ']' esperado, encontrou '%s'", 
									lookahead.getValue()), 
									1 );
				} else throw new ParseException(String
						.format("Constante literal esperada, encontrou '%s'", 
								lookahead.getValue()), 
								1 ); 
			} else if (lookahead.getOrdinal() == ASSIGN.ordinal()) {
				printAccepted();
			}
		}
	}
	
	private void VariableDecl() throws ParseException {
		
		lookahead = tokens.pollLast();
		if (lookahead.getOrdinal() > 0 && lookahead.getOrdinal() < 6) { // Tipo
			System.out.println("\nVariableDecl = TYPE ID VARIABLE ';'\n");
			if (tokens.getLast().getOrdinal() != FUNC.ordinal()) {
				printAccepted();
				while(lookahead.getOrdinal() != SEMICOLON.ordinal() || !tokens.isEmpty()) {
					VARIABLE();
					lookahead = tokens.pollLast();
					if (lookahead.getOrdinal() == COMA.ordinal()) {
						VARIABLE(); 
					} else if(lookahead.getOrdinal() == RD_ERROR.ordinal()) {
						printAccepted();
						
						throw new ParseException(String
								.format("Expressao aritmetica mal formada '%s'", 
										lookahead.getValue()), 
										3 ); 
					}
					else if(lookahead.getOrdinal() == SEMICOLON.ordinal()) {
						printAccepted();
					}
				}
			} else { //devolve simbolo
				tokens.addLast(lookahead);
			}
		} else {
			tokens.addLast(lookahead);
		}
	}
	
	public Grammar(String args) throws ParseException {
		tokens = new Lexer(args).getTokens();
		System.out.printf("\n%10s---LL1 Grammar---\n%10s=================\n\n", "", "");
		
		S();
	}

	public static void main(String[] args) throws ParseException {
		new Grammar(args[0]);
	}
}
