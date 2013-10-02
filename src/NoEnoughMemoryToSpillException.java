
public class NoEnoughMemoryToSpillException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoEnoughMemoryToSpillException()
	{
		super("There are to many virtual registers to spill so that there is no enough memory reserved for spill to use.");
	}

}
