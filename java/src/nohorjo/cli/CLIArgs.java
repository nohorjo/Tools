package nohorjo.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIArgs {
	Map<String, String> args;

	public CLIArgs(String... args) {
		this.args = new HashMap<>();
		for (int i = 0, j = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.contains("=")) {
				String[] argParts = arg.split("=", 2);
				this.args.put(argParts[0], argParts[1]);
			} else {
				this.args.put("$" + ++j, arg);
			}
		}
	}

	public String getString(String arg, String defaultValue) {
		try {
			return getString(arg);
		} catch (InvalidCLIArgException e) {
			return defaultValue;
		}
	}

	public long getLong(String arg, long defaultValue) {
		try {
			return Long.parseLong(getString(arg));
		} catch (InvalidCLIArgException e) {
			return defaultValue;
		}
	}

	public double getDouble(String arg, double defaultValue) {
		try {
			return Double.parseDouble(getString(arg));
		} catch (InvalidCLIArgException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String arg, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(getString(arg));
		} catch (InvalidCLIArgException e) {
			return defaultValue;
		}
	}

	public String getString(String arg) throws InvalidCLIArgException {
		try {
			return args.get(arg).trim();
		} catch (NullPointerException e) {
			throw new InvalidCLIArgException();
		}
	}

	public long getLong(String arg) throws NumberFormatException, InvalidCLIArgException {
		return Long.parseLong(getString(arg));
	}

	public double getDouble(String arg) throws NumberFormatException, InvalidCLIArgException {
		return Double.parseDouble(getString(arg));
	}

	public boolean getBoolean(String arg) throws InvalidCLIArgException {
		return Boolean.parseBoolean(getString(arg));
	}

	public List<String> getList(String arg, String delim) throws InvalidCLIArgException {
		return new ArrayList<>(Arrays.asList(getString(arg).split(delim)));
	}
}
