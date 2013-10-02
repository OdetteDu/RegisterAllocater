
public class NoNonReservedFreeRegisterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoNonReservedFreeRegisterException()
	{
		super("There is no non reserved free register to use at this time. You may use either a reserved register or get a machine with more registers.");
	}

}
