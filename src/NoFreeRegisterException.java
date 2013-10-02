
public class NoFreeRegisterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoFreeRegisterException()
	{
		super("There is no free register to use at this time. Please get a machine with more registers.");
	}

}
