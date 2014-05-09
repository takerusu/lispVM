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
				while (lispCode.charAt(index) != ' '
						&& lispCode.charAt(index) != '('
						&& lispCode.charAt(index) != ')') {
					letter++;
					index++;
					if (index >= lispCode.length())
						break;
				}
				if (letter != 0) {
					if (lispCode.charAt(index) == '(') {
						codeList.add(lispCode.substring(index - letter, index));
						codeList.add("(");
					} else if (lispCode.charAt(index) == ')') {
						codeList.add(lispCode.substring(index - letter, index));
						codeList.add(")");
					} else {
						codeList.add(lispCode.substring(index - letter, index));
					}
				} else {
					if (lispCode.charAt(index) == '(') {
						codeList.add("(");
					}
					if (lispCode.charAt(index) == ')') {
						codeList.add(")");
					}
				}
				index++;
			}
			codeList.add("EOF");
		}

	}

}
