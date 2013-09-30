import java.util.ArrayList;
import java.util.Iterator;

public class ButtomUpAllocator extends AAllocator {

	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
	}

	protected int allocate(Register unAllocatedRegister) throws NoFreeRegisterException
	{
		try
		{
			int result=physicalRegisters.getNonReservedRegister(unAllocatedRegister.getVr());
			return result;
		}
		catch(NoNonReservedFreeRegisterException e)
		{

			Register registerUsedFar=null;
			int maxLine=0;
			Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
			while(iter.hasNext())
			{
				Register r=allocatedRegisters.get(iter.next());

				if(r.getNextUse()>maxLine)
				{
					maxLine=r.getNextUse();
					registerUsedFar=r;
				}
			}

			spill(registerUsedFar); //should not be vr, should be immediate

			return allocate(unAllocatedRegister);
		}

	}
}
