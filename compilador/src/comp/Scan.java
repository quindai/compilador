package comp;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Scan {

	public Scan(String args) {
		System.out.println(Token.MyTokens.values()[0]);
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
					while(i<s.length()){
						//System.out.println(s.charAt(i));
						tokenTemp += s.charAt(i++);
						boolean b = contains(tokenTemp);
						
						//verifications:: comments, **, <= ... composed signals
						if(tokenTemp.equals("/") && s.charAt(i) == '/') break;
						
						tokenTemp = tokenTemp.replaceAll("(\\s+)", ""); // removes all whitespace, tab, newline
						if (b) {
							System.out.println( nextToken(tokenTemp).name() );

							// DAT: desempilha e acessa token
							tokenTemp = "";
							continue;
						}
				}
			});
		} catch (NoSuchFileException e) {
			System.out.printf("Arquivo '%s' não encontrado.", args);
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
