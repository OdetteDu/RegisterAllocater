

public class ExtraTokenException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtraTokenException()
	{
		super("Discover an extra token at the end of the line. You may have some uncommented out staff at the end of the line. \n");
	}
	
}
