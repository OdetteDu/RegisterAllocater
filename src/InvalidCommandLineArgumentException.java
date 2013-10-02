
public class InvalidCommandLineArgumentException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidCommandLineArgumentException()
	{
		super("The command you entered is not valid."
				+ "The command shoud be <flag> <numberOfRegister> <FilePath>."
				+ "The flage can be either b for buttom up, or t for top down.\n");
	}
	
	public InvalidCommandLineArgumentException(String message)
	{
		super(message);
	}

}
