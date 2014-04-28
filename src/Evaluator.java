import java.util.ArrayList;

public class Evaluator {
	ArrayList<Code> codeList;
	int[] intStack;
	int sp;
	int fp;
	int pc;
	int arg1 = 0, arg2 = 0;
	int top, num;

	public Evaluator(ArrayList<Code> codeList) {
		this.codeList = codeList;
		this.sp = 0;
		this.fp = 0;
		this.pc = 0;
		intStack = new int[1024];
	}

	public void evaluate(int sp, int fp, int pc) {
		while (true) {
			switch (codeList.get(pc).func) {
			case RET:
				if (fp == 0) {
					return;
				} else {
					top = intStack[--sp];
					int argNum = intStack[fp - 2];
					sp = fp - 2 - argNum;
					pc = intStack[fp - 1];
					fp = intStack[fp];
					intStack[sp++] = top;

				}
				break;
			case POP:
				sp--;
				break;
			case PUSH:
				intStack[sp++] = codeList.get(++pc).value;
				break;
			case ADD:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				intStack[sp++] = arg1 + arg2;
				break;
			case SUB:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				intStack[sp++] = arg1 - arg2;
				break;
			case MUL:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				intStack[sp++] = arg1 * arg2;
				break;
			case DIV:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				intStack[sp++] = arg1 / arg2;
				break;
			case EQ:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				if (arg1 == arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case NEQ:
				arg1 = intStack[--sp];
				arg2 = intStack[--sp];
				if (arg1 != arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case GT:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				if (arg1 > arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case GTEQ:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				if (arg1 >= arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case LT:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				if (arg1 < arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case LTEQ:
				arg2 = intStack[--sp];
				arg1 = intStack[--sp];
				if (arg1 <= arg2) {
					intStack[sp++] = 1;
				} else {
					intStack[sp++] = 0;
				}
				break;
			case STOREA:
				num = codeList.get(++pc).value;

				break;
			case LOADA:
				num = codeList.get(++pc).value;
				if (num > intStack[fp - 2]) {
					return;
				}
				intStack[sp++] = intStack[fp - 3 - intStack[fp - 2] + num];
				break;
			case BEQ0:
				top = intStack[--sp];
				if (top == 0) {
					pc = codeList.get(++pc).value - 1;
				} else {
					pc++;
				}
				break;

			case CALL:
				intStack[sp++] = ++pc; // CALL x のxのアドレスを保存
				pc = codeList.get(pc).value - 1; // pcを呼び出された関数の先頭アドレスに変更
				intStack[sp] = fp; // 現在のfpを保存
				fp = sp++;
				break;
			case PRINTLN:
				top = intStack[--sp];
				System.out.println(top);
			default:
				break;
			}
			pc++;
		}
	}
}
