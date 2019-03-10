package comp;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static comp.TokenType.*;

//https://www.baeldung.com/java-array-deque
/**
 * 
 * @author rquindai
 * @category loop and switch scanner
 * @return a list of tokens from source code
 */
public class Scan {

	private Deque<Token> tokens = new ArrayDeque<>();
	int count = 1;
	
	public Deque<Token> getTokens(){
		return tokens;
	}
	
	public Scan(String args) {
		//System.out.println(Token.values()[0]);
		//System.out.println("10".matches("\\d+"));
		//System.out.println(Pattern.compile("\".*\"").matcher("\"Ola mundo").find() );
//		System.out.println(Token.MyTokens.valueOf("INT"));
/*		Token.MyTokens m = Arrays.stream(Token.MyTokens.values())
				.filter(t -> t.toString().equalsIgnoreCase("int"))
				.findFirst().get();
				*/
	//	System.out.println( m.name() );
		try(Stream<String> str = Files.lines(Paths.get(args))){
			str.forEach(s-> 
				{
					int i = 0;
					String tokenTemp = "";
					System.out.printf("%4d  ", count++);
					while(i<s.length()){
						
						tokenTemp += s.charAt(i++);
						tokenTemp = tokenTemp.replaceAll("(\\s+)", ""); // removes all whitespace, tab, newline
						
						if (i < s.length()) {
							//verifications:: comments, **, <= ... composed signals
							if(tokenTemp.equals("/") && s.charAt(i) == '/') break;
							if(tokenTemp.equals("*") && s.charAt(i) == '*') {
								tokenTemp += "*";
								++i ;
							}
							if(tokenTemp.equals("<") && s.charAt(i) == '='){
								tokenTemp += "=";
								++i;
							}
							if(tokenTemp.equals(">") && s.charAt(i) == '='){
								tokenTemp += "=";
								++i;
							}
							if(tokenTemp.equals("=") && s.charAt(i) == '='){
								tokenTemp += '=';
								++i;
							}
							
							// capturing double quotes strings
							// Quote? Each pair of adjacent quotes represents a single-quote.
							if(tokenTemp.equals(TokenType.DOUBLE_QUOTES.value)){
								do{
									tokenTemp += s.charAt(i);
								}while((s.charAt(i) != '"') && (++i < s.length()));

								++i; // corrects the last char read
								if(Pattern.compile("\".*\"").matcher(tokenTemp).find()){
									Token temp = new Token(RD_STRING,
											tokenTemp, RD_STRING.ordinal(), count -1, i-tokenTemp.length());		
										
									tokens.push(temp);
									tokenTemp = "";
									System.out.print(tokens.getFirst().type.name() +" ");
								} else{
									Token temp = new Token(RD_ERROR,
											tokenTemp, RD_ERROR.ordinal(), count -1, i-tokenTemp.length());		
								
									tokens.push(temp);
									tokenTemp = "";
									System.out.print(tokens.getFirst().type.name() +" ");
								}
							}
							
							if(tokenTemp.length()>0 )
							switch(s.charAt(i)) {
							// capture identifiers
								case ',': case '(': case '=': case ':': 
									if (!contains(tokenTemp)) { 
										Token temp = new Token(IDENTIFIER,
												tokenTemp, IDENTIFIER.ordinal(), count -1, i-tokenTemp.length());		
								
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
							// get float numbers
							/*	case '.':
									if( tokenTemp.matches("\\d+")) {
										
										Token temp = new Token(RD_REAL, 
												value, RD_REAL.ordinal(), count -1, i-tokenTemp.length());
									}
									break;
							*/
								case ';': case ')': case '+': case '*': case '/': case '-': case ']': case '[':
								case '>': case '<': case ' ': 
									if( tokenTemp.matches("\\d+")) {
										Token temp = new Token(INTCONSTANT,
												tokenTemp, INTCONSTANT.ordinal(), count -1, i-tokenTemp.length());		
								
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									} else if(!contains(tokenTemp)){
										Token temp = new Token(IDENTIFIER,
												tokenTemp, IDENTIFIER.ordinal(), count -1, i-tokenTemp.length());		
								
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
								
							}
						}
						boolean b = contains(tokenTemp);
						
						if (b) {
							TokenType type = nextToken(tokenTemp);
							Token temp = new Token(type,
									tokenTemp, type.ordinal(), count -1, i-tokenTemp.length());		
					
							tokens.push(temp);
							// DAT: desempilha e acessa token
							tokenTemp = "";
							System.out.print(tokens.getFirst().type.name() +" ");
							continue;
						} else if((i == s.length()) && !tokenTemp.isEmpty()){
							// capture some malformed line, identify the category
							Token temp = new Token(RD_ERROR,
									tokenTemp, RD_ERROR.ordinal(), count -1, i-tokenTemp.length());		
						
							tokens.push(temp);
							tokenTemp = "";
							System.out.print(tokens.getFirst().type.name() +" ");
						}
				}

					
					System.out.println();
			});
		} catch (NoSuchFileException e) {
			System.out.printf("Arquivo '%s' nao encontrado.", args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean contains(String lexema){
		return Arrays.stream(TokenType.values()).anyMatch(t -> t.toString().equalsIgnoreCase(lexema) && t.name()!=TokenType.IDENTIFIER.name());
	}
	
	public TokenType nextToken(String lexema){
		return  Arrays.stream(TokenType.values())
				.filter(t -> t.toString().equalsIgnoreCase(lexema))
				.findFirst().get();
	}
	
	public static void main(String[] args) {
		new Scan(args[0]);
	}
}
