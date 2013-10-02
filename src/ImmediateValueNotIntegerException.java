
public class ImmediateValueNotIntegerException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ImmediateValueNotIntegerException(String immediateValue)
	{
		super("The immediate value "+immediateValue+" is not an integer. All immediate value should be integers. \n");
	}

}
