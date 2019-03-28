package comp;

import static comp.TokenType.IDENTIFIER;
import static comp.TokenType.INTCONSTANT;
import static comp.TokenType.RD_ERROR;
import static comp.TokenType.RD_REAL;
import static comp.TokenType.RD_STRING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//https://www.baeldung.com/java-array-deque
//http://java-regex-tester.appspot.com/
//https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html

public class Lexer {
	int count = 0;
	private Deque<Token> tokens = new ArrayDeque<>();
	
	public Lexer(String args) {
		long startTime = System.currentTimeMillis();
		try(Stream<String> str = Files.lines(Paths.get(args))){
			str.forEach(linha-> 
				{
					int i = 0;
					String lexema = "";
					System.out.printf("%4d  %s\n", ++count, linha);
					while(i<linha.length()){
						
						lexema += linha.charAt(i++);
						lexema = lexema.replaceAll("(\\s+)", ""); // removes all whitespace, tab, newline
						
						if (i < linha.length()) {
							//verifications:: comments, **, <= ... composed signals
							if(lexema.equals("/") && linha.charAt(i) == '/') break;
							if(lexema.equals("*") && linha.charAt(i) == '*') {
								lexema += "*";
								++i ;
							}
							if(lexema.equals("<") && linha.charAt(i) == '='){
								lexema += "=";
								++i;
							}
							if(lexema.equals(">") && linha.charAt(i) == '='){
								lexema += "=";
								++i;
							}
							if(lexema.equals("=") && linha.charAt(i) == '='){
								lexema += '=';
								++i;
							}
							
							// capturing double quotes strings
							// Quote? Each pair of adjacent quotes represents a single-quote.
							if(lexema.equals(TokenType.DOUBLE_QUOTES.value)){
								do{
									lexema += linha.charAt(i);
								}while((linha.charAt(i) != '"') && (++i < linha.length()));

								++i; // corrects the last char read
								//if(Pattern.compile("\".*\"").matcher(lexema).find()){
								if(lexema.charAt(lexema.length()-1) == '"') {
									dAT(RD_STRING, lexema, count, i-lexema.length());		
									lexema = "";
								} else{
									dAT(RD_ERROR, lexema, count, i-lexema.length());		
									lexema = "";
								}
							}
							
							if(lexema.length()>0 )
							switch(linha.charAt(i)) {
							// capture identifiers
								case ',': case '(': case '=': case ':': case '{': 
									if (!contains(lexema) && !Character.isDigit(lexema.charAt(0))) { 
										dAT(IDENTIFIER,	lexema, count, i-lexema.length());		
										lexema = "";
										//System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
							// get float numbers
								case '.':
									if( lexema.matches("\\d+") ) {
										if(Character.toString(linha.charAt(i+1)).matches("\\d")){
											String fraction = ""+linha.charAt(i);// + s.charAt(++i);
											
											while ((++i < linha.length()) && Character.isDigit(linha.charAt(i)))
												fraction += linha.charAt(i);
											if ((i < linha.length()) && (Character.toUpperCase(linha.charAt(i)) == 'E')) {
													fraction += linha.charAt(i++);
													//verifies if currentChar is '-'|'+' and reads only if nextChar is number
												if ( (linha.charAt(i) =='-' ||linha.charAt(i) =='+') && 
														Character.isDigit( linha.charAt(i+1)) )
											        fraction += linha.charAt(i); //+""+ s.charAt(++i); // read nextChar 
												while ((++i < linha.length()) && Character.isDigit(linha.charAt(i))) //capture subsequent char if are digits
												        fraction += linha.charAt(i);
											}

											lexema +=fraction;
											//verifies if float was extracted properly
											if(Pattern.matches("\\.\\d+((E|e)(\\+|-)?\\d+)?", fraction)){
												dAT(RD_REAL, lexema, count, i-lexema.length());										
												lexema = "";
												//System.out.print(tokens.getFirst().type.name() +" ");
											}
										}
									}
									break;
							
								case ';': case ')': case '+': case '*': case '/': case '-': case ']': case '[':
								case '>': case '<': case ' ': 
									if( lexema.matches("\\d+")) {
										dAT(INTCONSTANT, lexema, count, i-lexema.length());		
										lexema = "";
									} else if(!contains(lexema) && !Character.isDigit(lexema.charAt(0))){
										dAT(IDENTIFIER,	lexema, count, i-lexema.length());		
										lexema = "";
										//System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
								
							}
						}
						boolean b = contains(lexema);
						
						if (b) {
							dAT(getTokenType(lexema), lexema, count, i-lexema.length());		

							lexema = "";
							//System.out.print(tokens.getFirst().type.name() +" ");
							continue;
						} else if((i == linha.length()) && !lexema.isEmpty()){
							// capture some malformed line, identify as error category
							dAT(RD_ERROR, lexema, count, i-lexema.length());		
						
							lexema = "";
							//System.out.print(tokens.getFirst().type.name() +" ");
						}
				}

					
					//System.out.println();
			});
		} catch (NoSuchFileException e) {
			System.out.printf("Arquivo '%s' nao encontrado.", args);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		System.out.printf("\n>>>>>>>SCANNER\n%10s%d linhas lidas\n%10s%.2f segundos para extrair os tokens\n\n",
				"",	count-1,"", elapsedTime);
	}
	
	/**
	 * dAT: busca e imprime token
	 * @param t   	: category tokens
	 * @param lex 	: lexeme
	 * @param line	: line
	 * @param col	: col
	 */
	private void dAT(TokenType t, String lex, int line, int col) {
		Token temp = new Token(t, lex, t.ordinal(), line, col+1);		
		tokens.push(temp);
		System.out.println(temp);
	}
	
	public boolean contains(String lexema){
		return Arrays.stream(TokenType.values()).
				anyMatch(t -> 
				t.toString().equalsIgnoreCase(lexema));// && t.name()!=TokenType.IDENTIFIER.name());
	}
	
	public TokenType getTokenType(String lexema){
		return  Arrays.stream(TokenType.values())
				.filter(t -> t.toString().equalsIgnoreCase(lexema))
				.findFirst().get();
	}
	
	public static void main(String[] args) {
		new Lexer(args[0]);
	}
}
