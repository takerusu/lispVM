import java.util.ArrayList;

public class Parser {

	public int index = 0;
	ArrayList<String> codeList = new ArrayList<String>();
	ConsCell cell;

	public Parser(ArrayList<String> codeList) {
		this.codeList = codeList;
		cell = new ConsCell();
	}

	public int parse(ConsCell cell, int index) {
		if (codeList.get(index).equals("(") != true && index == 0) {
			return -1;
		}
		index++;
		while (codeList.get(index).equals(")") != true) {
			if (index >= codeList.size())
				break;
			if (codeList.get(index).equals("(")) {
				cell.value = "car";
				ConsCell car = new ConsCell("car");
				ConsCell cdr = new ConsCell();
				cell.car = car;
				index = parse(cell.car, index);
				cell.cdr = cdr;
				cell = cell.cdr;
			} else {
				cell.value = codeList.get(index);
				ConsCell cdr = new ConsCell();
				cell.cdr = cdr;
				cell = cell.cdr;
			}
			index++;
		}
		cell.value = "Nil";
		return index;
	}
}
