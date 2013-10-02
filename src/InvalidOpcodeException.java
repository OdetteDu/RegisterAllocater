

public class InvalidOpcodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidOpcodeException(String opcode)
	{
		super("The opcode '"+opcode+"' is not a valid opcode.\n"
				+ "The valid opcodes are: load, store, add, sub, mult, lshift, rshift, loadI, output");
	}

}
