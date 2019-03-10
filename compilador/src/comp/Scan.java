package comp;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static comp.TokenType.*;

//https://www.baeldung.com/java-array-deque
//http://java-regex-tester.appspot.com/
//https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
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
//		System.out.println(Pattern.matches("\\.\\d+((E|e)?(\\+|-)?\\d)*", ".1"));
/*		Token.MyTokens m = Arrays.stream(Token.MyTokens.values())
				.filter(t -> t.toString().equalsIgnoreCase("int"))
				.findFirst().get();
				*/
	//	System.out.println( m.name() );
		long startTime = System.currentTimeMillis();
		try(Stream<String> str = Files.lines(Paths.get(args))){
			str.forEach(s-> 
				{
					int i = 0;
					String lexema = "";
					System.out.printf("%4d  ", count++);
					while(i<s.length()){
						
						lexema += s.charAt(i++);
						lexema = lexema.replaceAll("(\\s+)", ""); // removes all whitespace, tab, newline
						
						if (i < s.length()) {
							//verifications:: comments, **, <= ... composed signals
							if(lexema.equals("/") && s.charAt(i) == '/') break;
							if(lexema.equals("*") && s.charAt(i) == '*') {
								lexema += "*";
								++i ;
							}
							if(lexema.equals("<") && s.charAt(i) == '='){
								lexema += "=";
								++i;
							}
							if(lexema.equals(">") && s.charAt(i) == '='){
								lexema += "=";
								++i;
							}
							if(lexema.equals("=") && s.charAt(i) == '='){
								lexema += '=';
								++i;
							}
							
							// capturing double quotes strings
							// Quote? Each pair of adjacent quotes represents a single-quote.
							if(lexema.equals(TokenType.DOUBLE_QUOTES.value)){
								do{
									lexema += s.charAt(i);
								}while((s.charAt(i) != '"') && (++i < s.length()));

								++i; // corrects the last char read
								if(Pattern.compile("\".*\"").matcher(lexema).find()){
									Token temp = new Token(RD_STRING,
											lexema, RD_STRING.ordinal(), count -1, i-lexema.length());		
										
									tokens.push(temp);
									lexema = "";
									System.out.print(tokens.getFirst().type.name() +" ");
								} else{
									Token temp = new Token(RD_ERROR,
											lexema, RD_ERROR.ordinal(), count -1, i-lexema.length());		
								
									tokens.push(temp);
									lexema = "";
									System.out.print(tokens.getFirst().type.name() +" ");
								}
							}
							
							if(lexema.length()>0 )
							switch(s.charAt(i)) {
							// capture identifiers
								case ',': case '(': case '=': case ':': 
									if (!contains(lexema)) { 
										Token temp = new Token(IDENTIFIER,
												lexema, IDENTIFIER.ordinal(), count -1, i-lexema.length());		
								
										tokens.push(temp);
										lexema = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
							// get float numbers
								case '.':
									if( lexema.matches("\\d+") ) {
										if(Character.toString(s.charAt(i+1)).matches("\\d")){
											String fraction = ""+s.charAt(i) + s.charAt(++i);
											// reads next char if its number
											//Pattern p = Pattern.compile("(\\d+|E?|e?)(+|-)?\\d+");
											//Matcher m;
											while((i+1 != s.length()) && (Pattern.matches("\\.\\d+((E|e)?(\\+|-)?\\d)*", fraction) || 
													Character.toString(s.charAt(i+1)).matches("(\\+|-)?\\d?"))){
												fraction += s.charAt(++i);
											}
											Token temp = new Token(RD_REAL, 
													lexema+fraction, RD_REAL.ordinal(), count -1, i-lexema.length());
											tokens.push(temp);
											
											lexema = "";
											System.out.print(tokens.getFirst().type.name() +" ");
										}
									}
									break;
							
								case ';': case ')': case '+': case '*': case '/': case '-': case ']': case '[':
								case '>': case '<': case ' ': 
									if( lexema.matches("\\d+")) {
										Token temp = new Token(INTCONSTANT,
												lexema, INTCONSTANT.ordinal(), count -1, i-lexema.length());		
								
										tokens.push(temp);
										lexema = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									} else if(!contains(lexema)){
										Token temp = new Token(IDENTIFIER,
												lexema, IDENTIFIER.ordinal(), count -1, i-lexema.length());		
								
										tokens.push(temp);
										lexema = "";
										System.out.print(tokens.getFirst().type.name() +" ");
									}
									break;
								
							}
						}
						boolean b = contains(lexema);
						
						if (b) {
							TokenType type = nextToken(lexema);
							Token temp = new Token(type,
									lexema, type.ordinal(), count -1, i-lexema.length());		
					
							tokens.push(temp);
							// DAT: desempilha e acessa token
							lexema = "";
							System.out.print(tokens.getFirst().type.name() +" ");
							continue;
						} else if((i == s.length()) && !lexema.isEmpty()){
							// capture some malformed line, identify the category
							Token temp = new Token(RD_ERROR,
									lexema, RD_ERROR.ordinal(), count -1, i-lexema.length());		
						
							tokens.push(temp);
							lexema = "";
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
		
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		System.out.printf("\n>>>>>>>SCANNER\n%10s%d linhas lidas\n%10s%.2f segundos para extrair os tokens\n\n",
				"",	count-1,"", elapsedTime);
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
