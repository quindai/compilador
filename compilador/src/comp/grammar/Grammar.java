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
 * http://math.hws.edu/javanotes/c9/s5.html
 */
public class Grammar {

	private Deque<Token> tokens = new ArrayDeque<>();
	Token lookahead;
	String aceita = "";

	//String EPSILON = "Îµ";
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

	private void Formals() throws ParseException{
		System.out.println("\nFormals = TYPE ID FORMALSR | ε\n");
		
		Tipo();		
		VARIABLE();
	}
	
	private void Expr() throws ParseException {
		lookahead = tokens.pollLast();
		
		/*if(lookahead.getOrdinal() == LIT_REAL.ordinal() ||
				lookahead.getOrdinal() == LIT_INT.ordinal() ||
				lookahead.getOrdinal() == LIT_STRING.ordinal() ||
				lookahead.getOrdinal() == LIT_CHAR.ordinal()) {
			*/
		if(lookahead.getOrdinal() >= FIRST_LITERAL_INDEX && 
				lookahead.getOrdinal() <= LAST_LITERAL_INDEX ) {
			System.out.println("\nFA = ID | FuncCall | 'lit_int' | 'lit_char' | 'lit_string' | 'lit_array' | 'lit_real'\n");
			printAccepted();
			
			lookahead = tokens.pollLast(); // verifica se eh funcao
			if(lookahead.getOrdinal() == FUNC.ordinal()){
				printAccepted();
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == IDENTIFIER.ordinal()){
					printAccepted();
					
					lookahead = tokens.pollLast();
					if(lookahead.getOrdinal() == LPAREN.ordinal()){
						printAccepted();
						Formals();
						
						lookahead = tokens.pollLast();
						if(lookahead.getOrdinal() == RPAREN.ordinal()){
							printAccepted();
						} else throw new ParseException(String.format("Simbolo ')' esperado, encontrou '%s'", 
								lookahead.getValue()), 
								1 );
					} else throw new ParseException(String.format("Simbolo '(' esperado, encontrou '%s'", 
							lookahead.getValue()), 
							1 );
				}
			}
			
		} else{
			System.out.println("Expr "+EPSILON);
			tokens.addLast(lookahead);
		}
	} 
	
	private void STMTRA() {
		
	}
	
	private void STMTR() throws ParseException {
		Expr();
		
		lookahead = tokens.pollLast();
		if(lookahead.getOrdinal() == SEMICOLON.ordinal()) {
			STMTRA();
		} else{
			System.out.println("STMTR "+EPSILON);
			tokens.addLast(lookahead);
		}
		
		lookahead = tokens.pollLast();  //PrintStmt
		if(lookahead.getOrdinal() == PRINT.ordinal()){
			System.out.println("\nPrintStmt = 'print' '(' Expr ')' ';'");
			printAccepted();
			lookahead = tokens.pollLast();
			if(lookahead.getOrdinal() == LPAREN.ordinal()){
				printAccepted();
				Expr();
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == RPAREN.ordinal()){
					printAccepted();
					lookahead = tokens.pollLast();
					if(lookahead.getOrdinal() == SEMICOLON.ordinal()){
						printAccepted();
					}
				}
			}
		} else{
			System.out.println("STMTR "+ EPSILON);
			tokens.addLast(lookahead);
		}
	}
	
	private void Decl() throws ParseException {
		VariableDecl();
		FunctionDecl();
	}
	
	private void FunctionDecl() throws ParseException {
		VariableDecl(); 
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
		if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX)
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
				if(lookahead.getOrdinal() == LIT_INT.ordinal()) {
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
				Atrib();
			} else{ //devolve
				System.out.println("VARIABLE "+ EPSILON);
				tokens.addLast(lookahead);
			}
		} else{ //devolve
			System.out.println("VARIABLE "+ EPSILON);
			tokens.addLast(lookahead);
		}
	}
	
	private void Atrib() throws ParseException {
		System.out.println("\nATRB = ID '=' Expr ';'\n");
		printAccepted();
		Expr();
		/*lookahead = tokens.pollLast();
		
		if(lookahead.getOrdinal() == LIT_REAL.ordinal() ||
				lookahead.getOrdinal() == LIT_INT.ordinal() ||
				lookahead.getOrdinal() == LIT_STRING.ordinal() ||
				lookahead.getOrdinal() == LIT_CHAR.ordinal()) {
			
			System.out.println("\nFA = ID | FuncCall | 'lit_int' | 'lit_char' | 'lit_string' | 'lit_array' | 'lit_real'\n");
			printAccepted();
		}*/
	}
	
	private void Stmt(){
		lookahead = tokens.pollLast();
		if(lookahead.getOrdinal() == REPEAT.ordinal()){
			
		}
	}
	
	private void StmtBlock() throws ParseException{
		lookahead = tokens.pollLast();
		
		if(lookahead.getOrdinal() == LBRAC.ordinal()){
			printAccepted();
			VariableDecl();
			
			lookahead = tokens.pollLast();
			if(lookahead.getOrdinal() == SEMICOLON.ordinal()){
				printAccepted();
				
				
				Stmt();
				
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == RBRAC.ordinal()){
					printAccepted();
				}
			}
			
		}
	}
	
	private void VariableDecl() throws ParseException {
		
		lookahead = tokens.pollLast();
		if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX) { // Tipo
			printAccepted();
			
			lookahead = tokens.pollLast();
			if (lookahead.getOrdinal() != FUNC.ordinal()) {
				System.out.println("\nVariableDecl = TYPE ID VARIABLE ';'\n");
				printAccepted();
				while(lookahead.getOrdinal() != SEMICOLON.ordinal()) {
					VARIABLE();
					lookahead = tokens.pollLast();
					if (lookahead.getOrdinal() == COMA.ordinal()) {
						printAccepted();
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
					}/* else { //devolve simbolo
						System.out.println("VariableDEcl EPSILON");
						tokens.addLast(lookahead);
					}*/
				}
			} else if (lookahead.getOrdinal() == FUNC.ordinal()){ //se for funcao
				System.out.println("\nFUNC = FUNCTYPE 'func' ID '(' Formals ')'  StmtBlock\n");
				printAccepted();
				lookahead = tokens.pollLast();
				printAccepted();
				
				lookahead = tokens.pollLast();
				if(lookahead.getOrdinal() == LPAREN.ordinal()){
					printAccepted();
					while(lookahead.getOrdinal() != RPAREN.ordinal())
						Formals();
					
					lookahead = tokens.pollLast();
					if(lookahead.getOrdinal() == RPAREN.ordinal()){
						printAccepted();
						
						StmtBlock();
					}
				}
			} else { //devolve simbolo
				System.out.println("VD "+ EPSILON);
				tokens.addLast(lookahead);
			}
		} else { //devolve simbolo
			System.out.println("VD "+ EPSILON);
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
