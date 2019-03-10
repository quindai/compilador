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
	
	public Deque<?> getTokens(){
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
							if(tokenTemp.equals('=') && s.charAt(i) == '='){
								tokenTemp += '=';
								++i;
							}
							
							// capturing double quotes strings
							// Quote? Each pair of adjacent quotes represents a single-quote.
							if(tokenTemp.equals(Token.DOUBLE_QUOTES.value)){
								do{
									tokenTemp += s.charAt(i);
								}while((s.charAt(i) != '"') && (++i < s.length()));

								++i; // corrects the last char read
								if(Pattern.compile("\".*\"").matcher(tokenTemp).find()){
									Token temp = Token.RD_STRING;
									temp.setIdentConstValue(tokenTemp);
									temp.line = count -1;
									temp.column = i-tokenTemp.length();
									tokens.push(temp);
									tokenTemp = "";
									System.out.print(tokens.getFirst().name() +" ");
								} else{
									Token temp = Token.RD_ERROR;
									temp.setIdentConstValue(tokenTemp);
									temp.line = count -1;
									temp.column = i-tokenTemp.length();
									tokens.push(temp);
									tokenTemp = "";
									System.out.print(tokens.getFirst().name() +" ");
								}
							}
							
							if(tokenTemp.length()>0 )
							switch(s.charAt(i)) {
							// capture identifiers
								case ',': case '(': case '=': case ':': case ' ':
									if (!contains(tokenTemp)) { 
										Token temp = Token.IDENTIFIER;
										temp.setIdentConstValue(tokenTemp);
										temp.line = count -1;
										temp.column = i-tokenTemp.length();
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									}
									break;
								case ';': case ')': case '+': case '*': case '/': case '-': case ']': case '[':
								case '>': case '<':
									if( tokenTemp.matches("\\d+")) {
										Token temp = Token.INTCONSTANT;
										temp.setIdentConstValue(tokenTemp);
										temp.line = count -1;
										temp.column = i-tokenTemp.length();
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									} else if(!contains(tokenTemp)){
										Token temp = Token.IDENTIFIER;
										temp.setIdentConstValue(tokenTemp);
										temp.line = count -1;
										temp.column = i-tokenTemp.length();
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									}
									break;
								
							}
						}
						boolean b = contains(tokenTemp);
						
						if (b) {
							Token temp = nextToken(tokenTemp);
							temp.line = count -1;
							temp.column = i-tokenTemp.length();
							tokens.push(temp);
							System.out.print(tokens.getFirst().name() +" ");
							// DAT: desempilha e acessa token
							tokenTemp = "";
							continue;
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
		return Arrays.stream(Token.values()).anyMatch(t -> t.toString().equalsIgnoreCase(lexema) && t.name()!=Token.IDENTIFIER.name());
	}
	
	public Token nextToken(String lexema){
		return  Arrays.stream(Token.values())
				.filter(t -> t.toString().equalsIgnoreCase(lexema))
				.findFirst().get();
	}
	
	public static void main(String[] args) {
		new Scan(args[0]);
	}
}
