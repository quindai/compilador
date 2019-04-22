package comp;

public class TestaParser {

	public TestaParser(String file) {
		int count = 0;
		long startTime = System.currentTimeMillis();
		Parser p = new Parser(file);
		
		while(p.nextToken());
		
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		System.out.printf("\n>>>>>>>SCANNER\n%10s%d linhas lidas\n%10s%.2f segundos para extrair os tokens\n\n",
					"",	count-1,"", elapsedTime);
	}
	public static void main(String[] args) {
		new TestaParser(args[0]);
	}
}
