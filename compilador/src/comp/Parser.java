package comp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//https://www.regextester.com/15

public class Parser {
	private Deque<Token> tokens;
	
	Parser(String args, boolean b) throws IOException{
		Pattern p = Pattern.compile("(//).*|([\"'])(?:[^\"']+|(?!\\1)[\"'])*\\1|\\|\\||<=|&&|\\-|[()\\[\\]{};=#]|[\\w.-]+");
		//
		try(Stream<String> str = Files.lines(Paths.get(args))){
			str.forEach(s -> {
				Matcher m = p.matcher(s);
				while (m.find()) {
					System.out.println(m.group());
				}
			});
			
		}
	}
	
	public Parser(String args) {
		tokens = new Scan(args).getTokens();
				while(tokens.iterator().hasNext())
					System.out.print(tokens.pollLast());
	}
	
	public static void main(String[] args) throws IOException {
		new Parser(args[0]);
	}

}
