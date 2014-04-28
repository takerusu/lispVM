import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException {
		File file = null;
		if (args.length != 0) {
			file = new File(args[0]);
			String str = " ";
			if (file != null) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String toRead = br.readLine();
					while (toRead != null) {
						str = str + toRead + " ";
						toRead = br.readLine();
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (!str.equals(" ")) {
				long f = System.currentTimeMillis();
				int index = 0;
				ArrayList<Code> codeList = new ArrayList<Code>();
				String lispCode = str;
				Tokenizer tk = new Tokenizer(lispCode);
				tk.tokenize();
				ArrayList<ConsCell> list = new ArrayList<ConsCell>();
				ConsCell cell;
				do {
					cell = new ConsCell();
					Parser pa = new Parser(tk.codeList);
					index = pa.parse(cell, index) + 1;
					if (index == -1)
						return;
					list.add(cell);
				} while (tk.codeList.get(index).equals("EOF") != true);

				HashMap<String, Integer> variable = new HashMap<String, Integer>();
				HashMap<String, Integer> function = new HashMap<String, Integer>();
				HashMap<String, Integer> functionLabel = new HashMap<String, Integer>();
				ArrayList<HashMap<String, Integer>> functionVariable = new ArrayList<HashMap<String, Integer>>();
				int functionNum, pc = 0;
				int[] pcList = new int[1024];
				index = 0;
				for (int i = 0; i < list.size(); i++) {
					functionNum = function.size();
					pc = codeList.size();
					Compiler cp = new Compiler();
					cp.compile(list.get(i), codeList, variable, function,
							functionLabel, functionVariable, 0);
					if (functionNum == function.size()) {
						pcList[index++] = pc;
						codeList.add(new Code(Func.PRINTLN));
						codeList.add(new Code(Func.RET));
					}
				}

				Evaluator eval = new Evaluator(codeList);
				for (int i = 0; i < index; i++) {
					eval.evaluate(0, 0, pcList[i]);
				}
				long l = System.currentTimeMillis();
				System.out.println(l - f);
			}
		} else {
			InteractiveShell is = new InteractiveShell();
			is.run();
		}
		// ArrayList<Code> testCodeList = new ArrayList<Code>();
		// for (int i = 0; i <= 37; i++) {
		// Code code = new Code();
		// testCodeList.add(code);
		// }
		// testCodeList.get(0).func = Func.LOADA;
		// testCodeList.get(1).value = 1;
		// testCodeList.get(2).func = Func.PUSH;
		// testCodeList.get(3).value = 3;
		// testCodeList.get(4).func = Func.LT;
		// testCodeList.get(5).func = Func.BEQ0;
		// testCodeList.get(6).value = 10;
		// testCodeList.get(7).func = Func.PUSH;
		// testCodeList.get(8).value = 1;
		// testCodeList.get(9).func = Func.RET;
		// testCodeList.get(10).func = Func.LOADA;
		// testCodeList.get(11).value = 1;
		// testCodeList.get(12).func = Func.PUSH;
		// testCodeList.get(13).value = 1;
		// testCodeList.get(14).func = Func.SUB;
		// testCodeList.get(15).func = Func.PUSH;
		// testCodeList.get(16).value = 1;
		// testCodeList.get(17).func = Func.CALL;
		// testCodeList.get(18).value = 0;
		// testCodeList.get(19).func = Func.LOADA;
		// testCodeList.get(20).value = 1;
		// testCodeList.get(21).func = Func.PUSH;
		// testCodeList.get(22).value = 2;
		// testCodeList.get(23).func = Func.SUB;
		// testCodeList.get(24).func = Func.PUSH;
		// testCodeList.get(25).value = 1;
		// testCodeList.get(26).func = Func.CALL;
		// testCodeList.get(27).value = 0;
		// testCodeList.get(28).func = Func.ADD;
		// testCodeList.get(29).func = Func.RET;
		// testCodeList.get(30).func = Func.PUSH;
		// testCodeList.get(31).value = 36;
		// testCodeList.get(32).func = Func.PUSH;
		// testCodeList.get(33).value = 1;
		// testCodeList.get(34).func = Func.CALL;
		// testCodeList.get(35).value = 0;
		// testCodeList.get(36).func = Func.PRINTLN;
		// testCodeList.get(37).func = Func.RET;

	}
}
