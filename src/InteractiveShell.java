import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class InteractiveShell {

	public void run() throws IOException {
		String s;

		ArrayList<Code> codeList = new ArrayList<Code>();
		HashMap<String, Integer> variable = new HashMap<String, Integer>();
		HashMap<String, Integer> function = new HashMap<String, Integer>();
		HashMap<String, Integer> functionLabel = new HashMap<String, Integer>();
		ArrayList<HashMap<String, Integer>> functionVariable = new ArrayList<HashMap<String, Integer>>();

		do {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			s = input.readLine();
			if (s.isEmpty())
				break;
			Tokenizer tk = new Tokenizer(s);
			tk.tokenize();
			Parser pa = new Parser(tk.codeList);
			ConsCell cell = new ConsCell();
			pa.parse(cell, 0);
			int functionNum = function.size();
			int pc = codeList.size();
			Compiler cp = new Compiler();
			cp.compile(cell, codeList, variable, function, functionLabel,
					functionVariable, 0);
			if (functionNum != function.size()) {
				System.out.println("defun");
			} else {
				Evaluator eval = new Evaluator(codeList);
				codeList.add(new Code(Func.PRINTLN));
				codeList.add(new Code(Func.RET));
				eval.evaluate(0, 0, pc);
			}
		} while (true);

	}
}
