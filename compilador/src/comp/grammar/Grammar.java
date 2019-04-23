package comp.grammar;

import static comp.TokenType.ASSIGN;
import static comp.TokenType.COMA;
import static comp.TokenType.END_PGM;
import static comp.TokenType.FIRST_LITERAL_INDEX;
import static comp.TokenType.FIRST_TYPE_INDEX;
import static comp.TokenType.FUNC;
import static comp.TokenType.IDENTIFIER;
import static comp.TokenType.LAST_LITERAL_INDEX;
import static comp.TokenType.LAST_TYPE_INDEX;
import static comp.TokenType.LBRAC;
import static comp.TokenType.LIT_INT;
import static comp.TokenType.LPAREN;
import static comp.TokenType.MAIN;
import static comp.TokenType.PGM;
import static comp.TokenType.PRINT;
import static comp.TokenType.RBRAC;
import static comp.TokenType.RD_ERROR;
import static comp.TokenType.REPEAT;
import static comp.TokenType.RPAREN;
import static comp.TokenType.SEMICOLON;
import static comp.TokenType.SLBRAC;
import static comp.TokenType.SRBRAC;

import java.text.ParseException;
import java.util.Stack;

import comp.Token;

/**
 * 
 * @author quindai LL1 descent predictive recursive
 * @see https://www.craftinginterpreters.com/parsing-expressions.html
 * http://math.hws.edu/javanotes/c9/s5.html
 */
public class Grammar {

	private Stack<Token> tokens = new Stack<>();
	Token lookahead;
	String aceita = "";

	//String EPSILON = "Îµ";
	String EPSILON = "ε";

	ParserForGrammar parser;
	
	public Grammar(String args) throws ParseException {
		long startTime = System.currentTimeMillis();
		
		//tokens = new Lexer(args).getTokens();
		parser = new ParserForGrammar(args);
		System.out.printf("\n%10s---LL1 Grammar---\n%10s=================\n\n", "", "");		
		S();
		
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		System.out.printf("\n>>>>>>>SINTATICO\n%10s%d linhas lidas\n%10s%.2f segundos\n\n",
					"",	parser.getLineCount()-1,"", elapsedTime);
	}

	public static void main(String[] args) throws ParseException {
		new Grammar(args[0]);
	}
	
	private void next(){
		if(tokens.isEmpty()){
			tokens = parser.nextToken();
		}
		
		lookahead = tokens.remove(0);
	}
	
	private void S() throws ParseException {
		System.out.println("S = 'pgm' Decl MainDecl 'end_pgm'");
		
		next();
		//lookahead = tokens.remove(0);
		if(lookahead.getOrdinal() == PGM.ordinal()) {
			printAccepted();
			Decl();
			MainDecl();
			next();
			//lookahead = tokens.pop();
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
		//System.out.printf("%10s"+lookahead.getValue() +" ", "");
		System.out.println(lookahead);
	}
	
	private void Decl() throws ParseException {
		next();
		//lookahead = tokens.pop();
		if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX) { // Tipo
			//printAccepted();
			VariableDecl();
			FunctionDecl();
			Decl();
		} else if (lookahead.getOrdinal() == MAIN.ordinal()) {
			//devolve
			//System.out.println("Decl "+ EPSILON);
			tokens.insertElementAt(lookahead, 0);
			//addLast(lookahead);
		} else throw new ParseException(String
				.format("'main' ou tipo esperado, encontrou '%s'", 
				lookahead.getValue()), 
				1 );
	}
	
	private void MainDecl() throws ParseException {
		System.out.println("\nMainDecl = 'main' '{' MainBody '}'");
		
		next();
		//lookahead = tokens.pop();
		if(lookahead.getOrdinal() == MAIN.ordinal()) {
			printAccepted();
			next();
			//lookahead = tokens.pop();
			if(lookahead.getOrdinal() == LBRAC.ordinal()) {
				printAccepted();
				
				MainBody();
				
				next();
				//lookahead = tokens.pop();
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
	
	private void Expr() throws ParseException {
		next();
		//lookahead = tokens.pop();
		
		/*if(lookahead.getOrdinal() == LIT_REAL.ordinal() ||
				lookahead.getOrdinal() == LIT_INT.ordinal() ||
				lookahead.getOrdinal() == LIT_STRING.ordinal() ||
				lookahead.getOrdinal() == LIT_CHAR.ordinal()) {
			*/
		if(lookahead.getOrdinal() >= FIRST_LITERAL_INDEX && 
				lookahead.getOrdinal() <= LAST_LITERAL_INDEX ) {
			System.out.println("\nFA = ID | FuncCall | 'lit_int' | 'lit_char' | 'lit_string' | 'lit_array' | 'lit_real'\n");
			printAccepted();
			
			next();
			//lookahead = tokens.pop(); // verifica se eh funcao
			if(lookahead.getOrdinal() == FUNC.ordinal()){
				printAccepted();
				next();
				//lookahead = tokens.pop();
				if(lookahead.getOrdinal() == IDENTIFIER.ordinal()){
					printAccepted();
					
					next();
					//lookahead = tokens.pop();
					if(lookahead.getOrdinal() == LPAREN.ordinal()){
						printAccepted();
						Formals();
						
						next();
						//lookahead = tokens.pop();
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
			tokens.insertElementAt(lookahead, 0);
			//tokens.addLast(lookahead);
		}
	} 
	
	private void STMTRA() {
		
	}
	
	private void STMTR() throws ParseException {
		Expr();
		
		next();
		//lookahead = tokens.pop();
		if(lookahead.getOrdinal() == SEMICOLON.ordinal()) {
			STMTRA();
		} else{
			System.out.println("STMTR "+EPSILON);
			tokens.insertElementAt(lookahead, 0);
		}
		
		next();
		//lookahead = tokens.pop();  //PrintStmt
		if(lookahead.getOrdinal() == PRINT.ordinal()){
			System.out.println("\nPrintStmt = 'print' '(' Expr ')' ';'");
			printAccepted();
			next();
			//lookahead = tokens.pop();
			if(lookahead.getOrdinal() == LPAREN.ordinal()){
				printAccepted();
				Expr();
				next();
				//lookahead = tokens.pop();
				if(lookahead.getOrdinal() == RPAREN.ordinal()){
					printAccepted();
					next();
					//lookahead = tokens.pop();
					if(lookahead.getOrdinal() == SEMICOLON.ordinal()){
						printAccepted();
					}
				}
			}
		} else{
			System.out.println("STMTR "+ EPSILON);
			tokens.insertElementAt(lookahead, 0);
			//tokens.addLast(lookahead);
		}
	}
	
	
	
	private void Formals() throws ParseException{
		
		if(lookahead.getOrdinal() == RPAREN.ordinal()) {
			System.out.println("Formals "+ EPSILON);
		} else {
			Tipo();		
			VARIABLE();
			Formals();
		}
	}
	
	private void FunctionDecl() throws ParseException {
		next();
		//lookahead = tokens.pop();
		if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX) {
			if (tokens.peek().getOrdinal() == FUNC.ordinal()){ //se for funcao
				System.out.println("\nFUNC = FUNCTYPE 'func' ID '(' Formals ')'  StmtBlock\n");
				printAccepted();
				next();
				//lookahead = tokens.pop();
				printAccepted();
				
				next();
				//lookahead = tokens.pop();
				if(lookahead.getOrdinal() == IDENTIFIER.ordinal()) {
					printAccepted();
					
					next();
					//lookahead = tokens.pop();
					if(lookahead.getOrdinal() == LPAREN.ordinal()){
						printAccepted();
						//while(lookahead.getOrdinal() != RPAREN.ordinal())
						System.out.println("\nFormals = TYPE ID FORMALSR | ε\n");
						Formals();
						
						//lookahead = tokens.pollLast();
						if(lookahead.getOrdinal() == RPAREN.ordinal()){
							printAccepted();
							
							StmtBlock();
						}
					}
				}	
			}
		}
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
	
	private void VariableDecl() throws ParseException {
		//lookahead = tokens.pollLast();
		//if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX) { // Tipo
		//if
		//printAccepted();
			
		/*	lookahead = tokens.pollLast();
			if (lookahead.getOrdinal() == IDENTIFIER.ordinal()) {
		*/
				//while(lookahead.getOrdinal() != SEMICOLON.ordinal()) {
			System.out.println("\nVariableDecl = TYPE ID VARIABLE ';'\n");
			printAccepted();
			VARIABLE();
			
			
//			if (lookahead.getOrdinal() == SEMICOLON.ordinal()) {
//				printAccepted();
//			}else throw new ParseException(String.
//							format("';' esperado, encontrou '%s'", 
//							lookahead.getValue()), 
//							1 );
					/*
					if (lookahead.getOrdinal() == COMA.ordinal()) {
						printAccepted();
						VARIABLE(); 
					} else if(lookahead.getOrdinal() == RD_ERROR.ordinal()) {
						printAccepted();
						
						throw new ParseException(String
								.format("Expressao aritmetica mal formada '%s'", 
										lookahead.getValue()), 
										3 ); 
					}*/
				
				
				//lookahead = tokens.pollLast();
				 
				
				/* else { //devolve simbolo
						System.out.println("VariableDEcl EPSILON");
						tokens.addLast(lookahead);
					}*/
				//}
			
	/*}  else throw new ParseException(String
					.format("Identificador ou funcao esperada, encontrou '%s'", 
							lookahead.getValue()), 
							1 ); 
			*/
	}

	private void Tipo() throws ParseException {
		next();
		//lookahead = tokens.pop();
		if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX)
			printAccepted();
		else throw new ParseException(String
				.format("Simbolo 'int', 'real', 'char', 'string' esperado, encontrou '%s'", 
				lookahead.getValue()), 
				1 );
	}
	
	private void VARIABLE() throws ParseException {
		next();
		//lookahead = tokens.pop();
		if(lookahead.getOrdinal() == IDENTIFIER.ordinal()) {
			printAccepted();
			next();
			//lookahead = tokens.pop();
			if(lookahead.getOrdinal() == SLBRAC.ordinal()) {//vetor [
				printAccepted();
				next();
				//lookahead = tokens.pop();
				if(lookahead.getOrdinal() == LIT_INT.ordinal()) {
					printAccepted();
					
					next();
					//lookahead = tokens.pop();
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
				VARIABLE(); //verificar
				
			} else if (lookahead.getOrdinal() == COMA.ordinal()) {
				printAccepted();
				VARIABLE(); 
			} else if(lookahead.getOrdinal() == SEMICOLON.ordinal()) {
				printAccepted();
			} 
			/*else{ //devolve
				System.out.println("VARIABLE "+ EPSILON);
				tokens.addLast(lookahead);
				VARIABLE();
			}*/
		}
		 else if(lookahead.getOrdinal() == RD_ERROR.ordinal()) {
			printAccepted();
			
			throw new ParseException(String
					.format("Expressao mal formada '%s'", 
							lookahead.getValue()), 
							3 ); 
		}
		
		else{ //devolve
			System.out.println("VARIABLE "+ EPSILON);
			tokens.insertElementAt(lookahead, 0);
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
		next();
		//lookahead = tokens.pop();
		if(lookahead.getOrdinal() == REPEAT.ordinal()){
			
		}
	}
	
	private void StmtBlock() throws ParseException{
		next();
		//lookahead = tokens.pop();
		
		if(lookahead.getOrdinal() == LBRAC.ordinal()){
			printAccepted();
			
			next();
			//lookahead = tokens.pop();
			if (lookahead.getOrdinal() >= FIRST_TYPE_INDEX && lookahead.getOrdinal() <= LAST_TYPE_INDEX) { // Tipo
				printAccepted();
			    VariableDecl();
			}
			
			next();
			//lookahead = tokens.pop();
			if(lookahead.getOrdinal() == SEMICOLON.ordinal()){
				printAccepted();
				
				Stmt();
				
				next();
				//lookahead = tokens.pop();
				if(lookahead.getOrdinal() == RBRAC.ordinal()){
					printAccepted();
				}
			}
			
		}
	}
}
