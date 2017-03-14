package calc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {

	public static final String PLUS = "+";
	public static final String MINUS = "-";
	public static final String MULTIPLY = "*";
	public static final String DIVIDE = "/";
	public static final String MOD = "%";
	public static final String POWER = "^";
	private static final String matchGroup = "(\\" + DIVIDE + "|\\" + MINUS + "|\\" + MOD + "|\\" + MULTIPLY + "|\\"
			+ PLUS + "|\\" + POWER + ")";

	private Double baseValue;
	private Expression a, b;
	private String operation;

	public static void main(String[] args) {
		String input = "1-(1+3)";
		System.out.println(parse(input).evaluate());
	}

	private static List<String> split(String input) {
		List<String> rtn = new ArrayList<>();
		String buffer = "";
		int level = 0;
		for (char c : input.toCharArray()) {
			if (level == 0) {
				if (c == '(') {
					level++;
					if (!buffer.equals("")) {
						rtn.add(surround(buffer));
						buffer = "";
					}
				} else {
					buffer += c;
				}
			} else {
				if (c == ')') {
					level--;
					if (level == 0) {
						if (!buffer.equals("")) {
							rtn.add(surround(buffer));
							buffer = "";
						}
					} else {
						buffer += c;
					}
				} else if (c == '(') {
					level++;
					buffer += c;
				} else {
					buffer += c;
				}
			}

		}
		if (!buffer.equals("")) {
			rtn.add(surround(buffer));
			buffer = "";
		}
		return rtn;
	}

	private static String surround(String input) {
		Matcher m = Pattern.compile("\\d+" + matchGroup).matcher(input);
		while (m.find()) {
			String match = m.group();
			String replacement = "("
					+ match.replace(DIVIDE, ")" + DIVIDE).replace(MINUS, ")" + MINUS).replace(MOD, ")" + MOD)
							.replace(MULTIPLY, ")" + MULTIPLY).replace(PLUS, ")" + PLUS).replace(POWER, ")" + POWER);
			input = input.replace(match, replacement);
		}
		m = Pattern.compile(matchGroup + "\\d+").matcher(input);
		while (m.find()) {
			String match = m.group();
			String replacement = match.replace(DIVIDE, DIVIDE + "(").replace(MINUS, MINUS + "(").replace(MOD, MOD + "(")
					.replace(MULTIPLY, MULTIPLY + "(").replace(PLUS, PLUS + "(").replace(POWER, POWER + "(") + ")";
			input = input.replace(match, replacement);
		}

		return input;
	}

	public static Expression parse(String input) {
		if (input.matches("\\d*\\.{0,1}\\d*")) {
			return new Expression(Double.parseDouble(input));
		}
		List<String> parts = split(input);
		while (parts.size() != 3) {
			System.out.println(parts);
			if (parts.size() == 1) {
				parts = split(parts.get(0));
			} else {
				parts = applyBIDMAS(parts);
			}
		}
		return new Expression(parse(parts.get(0)), parts.get(1), parse(parts.get(0)));
	}

	private static List<String> applyBIDMAS(List<String> parts) {
		List<String> ops = new ArrayList<>();
		for (int i = 1; (3 * i) - 1 < parts.size(); i++) {
			ops.add(parts.get((3 * i) - 1));
		}
		return null;
	}

	public Expression(Double baseValue) {
		this.baseValue = baseValue;
	}

	public Expression(Expression a, String operation, Expression b) {
		this.a = a;
		this.operation = operation;
		this.b = b;
	}

	public Expression(Double a, String operation, Double b) {
		this.a = new Expression(a);
		this.operation = operation;
		this.b = new Expression(b);
	}

	public Double evaluate() {
		if (baseValue != null) {
			return baseValue;
		}
		Double eval;
		switch (operation) {
		case DIVIDE:
			eval = a.evaluate() / b.evaluate();
			break;
		case MULTIPLY:
			eval = a.evaluate() * b.evaluate();
			break;
		case MINUS:
			eval = a.evaluate() - b.evaluate();
			break;
		case PLUS:
			eval = a.evaluate() + b.evaluate();
			break;
		case MOD:
			eval = a.evaluate() % b.evaluate();
			break;
		case POWER:
			eval = Math.pow(a.evaluate(), b.evaluate());
			break;
		default:
			throw new UnsupportedOperationException("[" + operation + "]");
		}
		return eval;
	}
}
