package comp;

public class TestaParser {

	public TestaParser(String file) {
		long startTime = System.currentTimeMillis();
		Parser p = new Parser(file);
		
		while(p.nextToken() != null);
		
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		System.out.printf("\n>>>>>>>SCANNER\n%10s%d linhas lidas\n%10s%.2f segundos para extrair os tokens\n\n",
					"",	p.getLineCount()-1,"", elapsedTime);
	}
	public static void main(String[] args) {
		new TestaParser(args[0]);
	}
}
