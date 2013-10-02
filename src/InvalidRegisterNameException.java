

public class InvalidRegisterNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidRegisterNameException(String registerName)
	{
		super("The register name '"+registerName+"' is not valid.\n"
				+ "The register name shoud be r followed by an integer. ");
	}

}
