import java.util.ArrayList;


public class Parser {

	public Instruction parseLine(ArrayList<String> line) throws InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, ImmediateValueNotIntegerException
	{
		Instruction instruction;
		
		String opcode=line.get(0);
		
		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			if(line.size()>5)
			{
				throw new ExtraTokenException();
			}
			Register source1=getRegister(line.get(1));
			Register source2=getRegister(line.get(2));
			checkArrow(line.get(3));
			Register target=getRegister(line.get(4));
			instruction=new Instruction(opcode);
			instruction.setSource1(source1);
			instruction.setSource2(source2);
			instruction.setTarget(target);
			
		}
		else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
		{
			//output
			if(line.size()>2)
			{
				throw new ExtraTokenException();
			}
			instruction=new Instruction(opcode);
			
			try
			{
				int iv=Integer.parseInt(line.get(1));
				instruction.setImmediateValue(iv);
			}
			catch(NumberFormatException e)
			{
				throw new ImmediateValueNotIntegerException();
			}
			
			
		}
		else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
		{
			//loadI
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register target=getRegister(line.get(3));
			checkArrow(line.get(2));
			instruction=new Instruction(opcode);
			instruction.setTarget(target);
	
			try
			{
				int iv=Integer.parseInt(line.get(1));
				instruction.setImmediateValue(iv);
			}
			catch(NumberFormatException e)
			{
				throw new ImmediateValueNotIntegerException();
			}
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register source1=getRegister(line.get(1));
			Register source2=getRegister(line.get(3));
			checkArrow(line.get(2));
			instruction=new Instruction(opcode);
			instruction.setSource1(source1);
			instruction.setSource2(source2);
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register source1=getRegister(line.get(1));
			checkArrow(line.get(2));
			Register target=getRegister(line.get(3));
			instruction=new Instruction(opcode);
			instruction.setSource1(source1);
			instruction.setTarget(target);
		}
		else
		{
			throw new InvalidOpcodeException();
		}
		
		return instruction;
	}
	
	private Register getRegister(String registerName) throws InvalidRegisterNameException
	{
		if(registerName.charAt(0)!='r')
		{
			throw new InvalidRegisterNameException();
		}
		
		try
		{
			int registerNumber=Integer.parseInt(registerName.substring(1, registerName.length()));
			return new Register(registerNumber);
		}
		catch(NumberFormatException e)
		{
			throw new InvalidRegisterNameException();
		}
	}
	
	private void checkArrow(String s) throws InvalidArrowException
	{
		if(!s.equals("=>"))
		{
			throw new InvalidArrowException();
		}
	}
}
