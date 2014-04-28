import java.util.ArrayList;

public class Tokenizer {

	private String lispCode;
	private int index = 0;
	public ArrayList<String> codeList;

	public Tokenizer(String lispCode) {
		this.lispCode = lispCode;
		codeList = new ArrayList<String>();
	}

	public void tokenize() {
		while (lispCode.charAt(index) == ' ') {
			index++;
		}
		if (lispCode.charAt(index) != '(') {
			System.out.println("ERROR");

		} else {

			while (index < lispCode.length()) {
				int letter = 0;
				int pflag = 0;
				while (lispCode.charAt(index) != ' ') {
					letter++;
					index++;
					if (index >= lispCode.length())
						break;
					if (lispCode.charAt(index - 1) == '(') {
						pflag++;
						break;
					}
					if (lispCode.charAt(index) == ')') {
						pflag++;
						break;
					}
				}
				if (letter != 0)
					codeList.add(lispCode.substring(index - letter, index));
				if (pflag == 0)
					index++;
			}
			codeList.add("EOF");
		}

	}

}
