public class Calc {
	public static void main(String[] args) {
		String expression;
		try {
			expression = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n\t calc expression");
			return;
		}
		String nums[] = expression.split("\\D");
		String ops[] = expression.split("\\d");
		Double eval = null;
		int offset = 0;
		for (int i = 1; i < ops.length; i++) {
			String op = ops[i];
			if (op.equals("")) {
				offset++;
				continue;
			}
			Double num2 = Double.parseDouble(nums[i - offset]);
			if (eval == null) {
				eval = Double.parseDouble(nums[(i - offset) - 1]);
			}
			eval = evaluate(eval, op, num2);

		}
		System.out.println(eval);
	}

	private static Double evaluate(Double a, String op, Double b) {
		switch (op) {
		case "/":
			a /= b;
			break;
		case "*":
			a *= b;
			break;
		case "-":
			a -= b;
			break;
		case "+":
			a += b;
			break;
		case "%":
			a %= b;
			break;
		case "^":
			a = Math.pow(a, b);
			break;
		default:
			throw new UnsupportedOperationException("[" + op + "]");
		}
		return a;
	}
}
