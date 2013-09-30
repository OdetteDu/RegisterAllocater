import java.util.ArrayList;
import java.util.Iterator;


public class TopDownAllocator extends AAllocator {
	
	public TopDownAllocator(int numPhysicalRegister, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
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

			Register lowestFrequency=null;
			int minFrequency=instructions.size();
			Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
			while(iter.hasNext())
			{
				int vr=iter.next();
				if(useFrequencyCount.get(vr)<minFrequency)
				{
					minFrequency=useFrequencyCount.get(vr);
					lowestFrequency=allocatedRegisters.get(vr);
				}
				
			}
			
			spill(lowestFrequency); //should not be vr, should be immediate

			return allocate(unAllocatedRegister);
		}
	}
	
}
