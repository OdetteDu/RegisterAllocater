import java.util.ArrayList;
import java.util.Iterator;


public class TopDownAllocator extends AAllocator {
	
	public TopDownAllocator(int numPhysicalRegister, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
	}
	
	@Override
	protected Register getToBeSpilledRegister()
	{
		Register lowestFrequency=currentlyUsedRegister;
		int minFrequency=instructions.size();
		Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
		while(iter.hasNext())
		{
			int vr=iter.next();
			if(useFrequencyCount.get(vr)<minFrequency)
			{
				if(currentlyUsedRegister!=null && currentlyUsedRegister.getVr()==allocatedRegisters.get(vr).getVr())
				{
					continue;
				}
				
				minFrequency=useFrequencyCount.get(vr);
				lowestFrequency=allocatedRegisters.get(vr);
			}
			
		}
		return lowestFrequency;
	}
	
}
