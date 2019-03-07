package comp;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Stream;

//https://www.baeldung.com/java-array-deque
/**
 * 
 * @author rquindai
 * @category loop and switch scanner
 * @return a list of tokens from source code
 */
public class Scan {

	private Deque<Token.MyTokens> tokens = new ArrayDeque<>();
	int count = 1;
	public Scan(String args) {
		System.out.println(Token.MyTokens.values()[0]);
		System.out.println("10".matches("\\d+"));
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
					System.out.printf("Linha %d: ", count++);
					while(i<s.length()){
						
						tokenTemp += s.charAt(i++);
						tokenTemp = tokenTemp.replaceAll("(\\s+)", ""); // removes all whitespace, tab, newline
						
						if (i < s.length()) {
							//verifications:: comments, **, <= ... composed signals
							if(tokenTemp.equals("/") && s.charAt(i) == '/') break;
							if(tokenTemp.equals("*") && s.charAt(i) == '*') {
								tokenTemp += "*";
								++i;
							}
							
							switch(s.charAt(i)) {
							// capture identifiers
								case '[': case ',': case '(': case '=': case ':':
									if (!contains(tokenTemp)) {
										Token.MyTokens temp = Token.MyTokens.IDENTIFIER;
										temp.setIdentConstValue(tokenTemp);
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									}
									break;
								case ';':
									if( tokenTemp.matches("\\d+")) {
										Token.MyTokens temp = Token.MyTokens.INTCONSTANT;
										temp.setIdentConstValue(tokenTemp);
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									} else {
										Token.MyTokens temp = Token.MyTokens.IDENTIFIER;
										temp.setIdentConstValue(tokenTemp);
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									}
									break;
								case ']': 
									if( tokenTemp.matches("\\d+")) {
										Token.MyTokens temp = Token.MyTokens.INTCONSTANT;
										temp.setIdentConstValue(tokenTemp);
										tokens.push(temp);
										tokenTemp = "";
										System.out.print(tokens.getFirst().name() +" ");
									}
							}
						}
						boolean b = contains(tokenTemp);
						
						if (b) {
							tokens.push(nextToken(tokenTemp));
							System.out.print(tokens.getFirst().name() +" ");
							// DAT: desempilha e acessa token
							tokenTemp = "";
							continue;
						}
				}
					System.out.println();
			});
		} catch (NoSuchFileException e) {
			System.out.printf("Arquivo '%s' n�o encontrado.", args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean contains(String lexema){
		return Arrays.stream(Token.MyTokens.values()).anyMatch(t -> t.toString().equalsIgnoreCase(lexema));
	}
	
	public Token.MyTokens nextToken(String lexema){
		return  Arrays.stream(Token.MyTokens.values())
				.filter(t -> t.toString().equalsIgnoreCase(lexema))
				.findFirst().get();
	}
	
	public static void main(String[] args) {
		new Scan(args[0]);
	}
}
