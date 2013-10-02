import java.util.ArrayList;
import java.util.Iterator;


public class TopDownAllocator extends AAllocator {
	
	public TopDownAllocator(int numPhysicalRegister, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
	}
	
	@Override
	protected Register getToBeSpilledRegister() throws NoUnusedRegisterToSpillException
	{
		Register lowestFrequency=null;
		int minFrequency=instructions.size();
		Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
		
		while(iter.hasNext())
		{
			int virtualRegisterNumber=iter.next();
			Register registerToCompare=allocatedRegisters.get(virtualRegisterNumber);
			
			if(currentlyUsedRegister!=null && currentlyUsedRegister.getVr()==registerToCompare.getVr())
			{
				continue;
			}
			
			if(useFrequencyCount.get(virtualRegisterNumber)<minFrequency)
			{
				minFrequency=useFrequencyCount.get(virtualRegisterNumber);
				lowestFrequency=registerToCompare;
			}
			
		}
		
		if(lowestFrequency==null)
		{
			throw new NoUnusedRegisterToSpillException();
		}
		
		return lowestFrequency;
	}
	
}
