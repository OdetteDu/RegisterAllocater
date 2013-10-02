
public class NoUnusedRegisterToSpillException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoUnusedRegisterToSpillException()
	{
		super("There is no unused register to spill at this time. \n"
				+ "You may spill a register that is used on the current line and restore it back before this line gets executed."
				+ "Or you may consider rewrite your code or get a machine with more registers");
	}

}
