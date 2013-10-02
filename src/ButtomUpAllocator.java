import java.util.ArrayList;
import java.util.Iterator;

public class ButtomUpAllocator extends AAllocator {

	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
	}

	@Override
	protected Register getToBeSpilledRegister() throws NoUnusedRegisterToSpillException
	{
		Register registerUsedFar=null;
		int maxLine=0;
		Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
		while(iter.hasNext())
		{
			Register r=allocatedRegisters.get(iter.next());
			
			if(currentlyUsedRegister!=null && currentlyUsedRegister.getVr()==r.getVr())
			{
				continue;
			}
				
			if(r.getNextUse()>=maxLine)
			{
				if(r.getNextUse()==maxLine)
				{
					if(spillMap.get(registerUsedFar.getVr())==null)
					{
						registerUsedFar=r;
					}
				}
				else
				{
					maxLine=r.getNextUse();
					registerUsedFar=r;
				}
			}
		}
		
		if(registerUsedFar==null)
		{
//			registerUsedFar=currentlyUsedRegister;
//			System.out.println("Warning: use currently used register.\n");
			throw new NoUnusedRegisterToSpillException();
		}
		
		return registerUsedFar;
	}
}
