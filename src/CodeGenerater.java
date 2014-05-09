import java.util.ArrayList;
import java.util.HashMap;

public class CodeGenerater {
	ConsCell cell;
	ArrayList<Code> codeList;
	int functionNum;
	boolean defunFlug = false;

	public CodeGenerater() {
		functionNum = 0;
	}

	public void generate(
			ConsCell cell,
			ArrayList<Code> codeList,
			HashMap<String, Integer> variable,
			HashMap<String, Integer> function, // 0,1,2...
			HashMap<String, Integer> functionLabel,
			ArrayList<HashMap<String, Integer>> functionVariables,
			int functionNum) {
		ConsCell firstCell;
		firstCell = cell;
		while (!cell.value.equals("Nil")) {
			if (cell.value.equals("car")) {
				generate(cell.car, codeList, variable, function, functionLabel,
						functionVariables, functionNum);
			} else if (cell.value.equals("if")) {
				cell = cell.cdr;
				if (cell.value.equals("car")) {
					generate(cell.car, codeList, variable, function,
							functionLabel, functionVariables, functionNum);
				} else if (cell.value.equals("1") || cell.value.equals("0")) {
					codeList.add(new Code(Func.PUSH));
					codeList.add(new Code(Integer.valueOf(cell.value)));
				} else {
					return;
				}
				codeList.add(new Code(Func.BEQ0));
				codeList.add(new Code(null));
				int label = codeList.size() - 1;
				cell = cell.cdr;
				if (cell.value.equals("car")) {
					generate(cell.car, codeList, variable, function,
							functionLabel, functionVariables, label);
				} else {
					if (Character.isDigit(cell.value.charAt(0))) {
						codeList.add(new Code(Func.PUSH));
						codeList.add(new Code(Integer.valueOf(cell.value)));
					} else {
						codeList.add(new Code(Func.LOADA));
						codeList.add(new Code(functionVariables.get(
								functionNum - 1).get(cell.value)));
					}
				}
				codeList.add(new Code(Func.PUSH));
				codeList.add(new Code(0));
				codeList.add(new Code(Func.BEQ0));
				codeList.add(new Code(null));
				codeList.get(label).value = codeList.size(); // beq0のラベルの位置を代入
				label = codeList.size() - 1;
				cell = cell.cdr;
				if (cell.value.equals("car")) {
					generate(cell.car, codeList, variable, function,
							functionLabel, functionVariables, label);
				} else {
					codeList.add(new Code(Func.PUSH));
					codeList.add(new Code(Integer.valueOf(cell.value)));
				}
				codeList.get(label).value = codeList.size(); // beq0のラベルの位置を代入
				return;

			} else if (cell.value.equals("setq")) {
				cell = cell.cdr;
				if (cell.cdr.equals("car")) {
					generate(cell.cdr.car, codeList, variable, function,
							functionLabel, functionVariables, functionNum);
				}
				variable.put(cell.value, Integer.valueOf(cell.cdr.value));
			} else if (cell.value.equals("defun")) {
				cell = cell.cdr;
				function.put(cell.value, function.size());
				functionLabel.put(cell.value, codeList.size());
				ConsCell tmpCell;
				tmpCell = cell;
				cell = cell.cdr.car;
				HashMap<String, Integer> functionVariable = new HashMap<String, Integer>();
				while (!cell.value.equals("Nil")) {
					functionVariable.put(cell.value,
							functionVariable.size() + 1);
					cell = cell.cdr;
				}
				functionVariables.add(functionVariable);
				cell = tmpCell.cdr.cdr;
				if (cell.value.equals("car")) {
					generate(cell.car, codeList, variable, function,
							functionLabel, functionVariables, function.size());
				} else {
					if (functionVariables.get(functionNum - 1).containsKey(
							cell.value)) {
						codeList.add(new Code(Func.LOADA));
						codeList.add(new Code(functionVariables.get(
								functionNum - 1).get(cell.value)));
					} else {
						codeList.add(new Code(Func.PUSH));
						codeList.add(new Code(Integer.valueOf(cell.value)));
					}
				}
				codeList.add(new Code(Func.RET));

			} else if (function.containsKey(cell.value)) {
				int label = functionLabel.get(cell.value);
				HashMap<String, Integer> functionVariable = functionVariables
						.get(function.get(cell.value));
				cell = cell.cdr;
				for (int i = 0; i < functionVariable.size(); i++) {
					if (cell.value.equals("car")) {
						generate(cell.car, codeList, variable, function,
								functionLabel, functionVariables,
								function.size());
					} else {
						if (Character.isDigit(cell.value.charAt(0))) {
							codeList.add(new Code(Func.PUSH));
							codeList.add(new Code(Integer.valueOf(cell.value)));
						} else {
							codeList.add(new Code(Func.LOADA));
							codeList.add(new Code(functionVariables.get(
									functionNum - 1).get(cell.value)));
						}
					}
					if (!cell.cdr.value.equals("Nil"))
						cell = cell.cdr;
				}
				codeList.add(new Code(Func.PUSH));
				codeList.add(new Code(functionVariable.size()));
				codeList.add(new Code(Func.CALL));
				codeList.add(new Code(label));
				return;
			}
			cell = cell.cdr;
		}
		cell = firstCell;
		switch (cell.value) {
		case "+":
			codeCreate(codeList, cell, Func.ADD, functionVariables, functionNum);
			break;
		case "-":
			codeCreate(codeList, cell, Func.SUB, functionVariables, functionNum);
			break;
		case "*":
			codeCreate(codeList, cell, Func.MUL, functionVariables, functionNum);
			break;
		case "/":
			codeCreate(codeList, cell, Func.DIV, functionVariables, functionNum);
			break;
		case "=":
			codeCreate(codeList, cell, Func.EQ, functionVariables, functionNum);
			break;
		case "/=":
			codeCreate(codeList, cell, Func.NEQ, functionVariables, functionNum);
			break;
		case ">":
			codeCreate(codeList, cell, Func.GT, functionVariables, functionNum);
			break;
		case ">=":
			codeCreate(codeList, cell, Func.GTEQ, functionVariables,
					functionNum);
			break;
		case "<":
			codeCreate(codeList, cell, Func.LT, functionVariables, functionNum);
			break;
		case "<=":
			codeCreate(codeList, cell, Func.LTEQ, functionVariables,
					functionNum);
			break;
		default:

			if (function.containsKey(cell.value)) {
				HashMap<String, Integer> functionVariable = functionVariables
						.get(function.get(cell.value) - 1);
				for (int i = 0; i < functionVariable.size(); i++) {

				}

			}
			break;
		}
	}

	public void codeCreate(ArrayList<Code> codeList, ConsCell cell, Func func,
			ArrayList<HashMap<String, Integer>> functionVariables,
			int functionNum) {
		try {
			cell = cell.cdr;
			if (cell.value.equals("car")) {
			} else if (functionNum > 0) {
				if (functionVariables.get(functionNum - 1).containsKey(
						cell.value)) {
					codeList.add(new Code(Func.LOADA));
					codeList.add(new Code(functionVariables
							.get(functionNum - 1).get(cell.value)));
				} else {
					codeList.add(new Code(Func.PUSH));
					codeList.add(new Code(Integer.valueOf(cell.value)));
				}
			} else {
				codeList.add(new Code(Func.PUSH));
				codeList.add(new Code(Integer.valueOf(cell.value)));
			}
			cell = cell.cdr;
			while (!cell.value.equals("Nil")) {
				if (cell.value.equals("car")) {
					codeList.add(new Code(func));
				} else if (functionNum > 0) {
					if (functionVariables.get(functionNum - 1).containsKey(
							cell.value)) {
						codeList.add(new Code(Func.LOADA));
						codeList.add(new Code(functionVariables.get(
								functionNum - 1).get(cell.value)));
						codeList.add(new Code(func));
					} else {
						codeList.add(new Code(Func.PUSH));
						codeList.add(new Code(Integer.valueOf(cell.value)));
						codeList.add(new Code(func));
					}
				} else {
					codeList.add(new Code(Func.PUSH));
					codeList.add(new Code(Integer.valueOf(cell.value)));
					codeList.add(new Code(func));
				}
				cell = cell.cdr;
			}
		} catch (Exception e) {
			codeList.add(new Code(func));
		}
	}
}
