import java.util.ArrayList;
import java.util.Iterator;

public class ButtomUpAllocator extends AAllocator {

	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
	}

	@Override
	protected Register getToBeSpilledRegister()
	{
		Register registerUsedFar=currentlyUsedRegister;
		int maxLine=0;
		Iterator<Integer> iter=allocatedRegisters.keySet().iterator();
		while(iter.hasNext())
		{
			Register r=allocatedRegisters.get(iter.next());
			
			if(currentlyUsedRegister!=null && currentlyUsedRegister.getVr()==r.getVr())
			{
				continue;
			}
				
			if(r.getNextUse()>maxLine)
			{
				maxLine=r.getNextUse();
				registerUsedFar=r;
			}
		}
		
		return registerUsedFar;
	}
}
