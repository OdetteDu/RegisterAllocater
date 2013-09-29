import java.util.ArrayList;


public class ButtomUpAllocator extends AAllocator {
	
	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions)
	{
		super(numPhysicalRegister,instructions);
	}

	@Override
	public void allocateRegister() {
		
		for(int i=0;i<instructions.size();i++)
		{
			
		}
		
	}
	
	public int ensure(Instruction instruction)
	{
		//if instruction is in controll
		//return its physical register
		//else
		//allocate
		return 0;
	}
	
	public int allocate()
	{
		return 0;
	}

}
