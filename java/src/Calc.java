import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calc {
	public static void main(String[] args) {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");
		try {
			String expression = String.join(" ", args);
			engine.eval("print(eval('" + expression + "'))");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

}
