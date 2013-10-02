

public class UseUndefinedRegisterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UseUndefinedRegisterException(String s)
	{
		super("You are trying to use the register "+s+", which is undefined.");
	}

}
