package nohorjo.cli;

public class InvalidCLIArgException extends Exception {

	public InvalidCLIArgException(String message) {
		super(message);
	}

	public InvalidCLIArgException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5886901107788084662L;

	public void printMessage() {
		String message = getMessage();
		if (message != null && !message.trim().equals("")) {
			System.err.println(message);
		}
	}

}
