
public class UnImplementedFeatureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnImplementedFeatureException()
	{
		super("The feature you are trying to use is unimplemented yet. Please wait for the next version to use this feature.");
	}

}
