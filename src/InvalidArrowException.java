

public class InvalidArrowException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidArrowException(String s)
	{
		super("The error '"+s+"' is not a valid arrow. The arrow should be like '=>'");
	}

}
