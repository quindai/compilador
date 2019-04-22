package comp;

import static comp.TokenType.IDENTIFIER;
import static comp.TokenType.LIT_INT;
import static comp.TokenType.LIT_REAL;
import static comp.TokenType.LIT_STRING;
import static comp.TokenType.RD_ERROR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

//https://www.regextester.com/15

public class Parser {
	
	Scanner scan;
	int count = 0;
	public Parser(String args) {
		try {
			scan = new Scanner(new File(args));
		}catch (FileNotFoundException e) {
			System.out.printf("Arquivo '%s' nao encontrado.", args);
		}
	}
	
	public boolean nextToken() {
		if(scan.hasNextLine()) {
			String linha = scan.nextLine();
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
							dAT(LIT_STRING, lexema, count, i-lexema.length());		
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
										dAT(LIT_REAL, lexema, count, i-lexema.length());										
										lexema = "";
										//System.out.print(tokens.getFirst().type.name() +" ");
									}
								}
							}
							break;
					
						case ';': case ')': case '+': case '*': case '/': case '-': case ']': case '[':
						case '>': case '<': case ' ': 
							if( lexema.matches("\\d+")) {
								dAT(LIT_INT, lexema, count, i-lexema.length());		
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
			return true;
		} else return false;
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
		//tokens.push(temp);
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
	
	public static void main(String[] args) throws IOException {
		new Parser(args[0]);
	}

}
