import java.util.ArrayList;
import java.util.HashMap;



import java.util.Iterator;

import exception.UseUndefinedRegisterException;


public abstract class AAllocator {

	protected int[] prs;
	protected ArrayList<Instruction> instructions;
	private int renameCount;
	protected HashMap<Integer, Integer> useFrequency; // vr:count

	public AAllocator(int numPhysicalRegister, ArrayList<Instruction> instructions) 
	{
		renameCount=-2;
		this.instructions=instructions;
		useFrequency=new HashMap<Integer, Integer>();
		
		prs=new int [numPhysicalRegister];
		for(int i=0;i<prs.length;i++)
		{
			prs[i]=-1;
		}
		
	}
	
	public void run() throws UseUndefinedRegisterException
	{
		calculateLiveRange();
		allocateRegister();
	}
	
	private void rename(Register r, HashMap<Integer, Integer> renameList)
	{
		if(renameList.get(r.getVr())!=null)
		{
			int newName=renameList.get(r.getVr());
			r.setVr(newName);
			rename(r,renameList);
		}
	}

	private void calculateLiveRange() throws UseUndefinedRegisterException
	{
		HashMap<Integer, Integer> liveRegisters=new HashMap<Integer,Integer>();
		ArrayList<Integer> definedVr=new ArrayList<Integer>();
		HashMap<Integer, Integer> renameList=new HashMap<Integer,Integer>();
		HashMap<Integer, Integer> registerUse=new HashMap<Integer, Integer>();

		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);

			if(instruction.getSource1()!=null)
			{
				Register source1=instruction.getSource1();

				rename(source1,renameList);
//				if(renameList.get(source1.getVr())!=null)
//				{
//					int newName=renameList.get(source1.getVr());
//					source1.setVr(newName);
//				}
				
				if(liveRegisters.get(source1.getVr())!=null)
				{
					int lastUse=liveRegisters.get(source1.getVr());
					source1.setLastUse(lastUse);
					source1.setNextUse(registerUse.get(source1.getVr()));
					registerUse.put(source1.getVr(), i);
					int x=useFrequency.get(source1.getVr());
					x++;
					useFrequency.put(source1.getVr(), x);
				}
				else
				{
					if(definedVr.contains(source1.getVr()))
					{
						int availableName=getAvailableName();
						renameList.put(source1.getVr(),availableName);
						source1.setVr(availableName);
					}
					source1.setLastUse(i);
					liveRegisters.put(source1.getVr(), i);
					source1.setNextUse(i);
					registerUse.put(source1.getVr(), i);
					useFrequency.put(source1.getVr(), 1);
				}
			}

			if(instruction.getSource2()!=null)
			{
				Register source2=instruction.getSource2();
				
				rename(source2,renameList);
//				if(renameList.get(source2.getVr())!=null)
//				{
//					int newName=renameList.get(source2.getVr());
//					source2.setVr(newName);
//				}
				
				if(liveRegisters.get(source2.getVr())!=null)
				{
					int lastUse=liveRegisters.get(source2.getVr());
					source2.setLastUse(lastUse);
					source2.setNextUse(registerUse.get(source2.getVr()));
					registerUse.put(source2.getVr(), i);
					int x=useFrequency.get(source2.getVr());
					x++;
					useFrequency.put(source2.getVr(), x);
				}
				else
				{
					if(definedVr.contains(source2.getVr()))
					{
						int availableName=getAvailableName();
						renameList.put(source2.getVr(),availableName);
						source2.setVr(availableName);
					}
					source2.setLastUse(i);
					liveRegisters.put(source2.getVr(), i);
					source2.setNextUse(i);
					registerUse.put(source2.getVr(), i);
					useFrequency.put(source2.getVr(), 1);
				}
				
			}

			if(instruction.getTarget()!=null)
			{
				Register target=instruction.getTarget();
				
				rename(target,renameList);
//				if(renameList.get(target.getVr())!=null)
//				{
//					int newName=renameList.get(target.getVr());
//					target.setVr(newName);
//				}
				
				if(liveRegisters.get(target.getVr())!=null)
				{
					int lastUse=liveRegisters.get(target.getVr());
					target.setLastUse(lastUse);
					liveRegisters.remove(target.getVr());
					target.setNextUse(registerUse.get(target.getVr()));
					registerUse.remove(target.getVr());
					
				}
				else
				{
					if(definedVr.contains(target.getVr()))
					{
						int availableName=getAvailableName();
						renameList.put(target.getVr(),availableName);
						target.setVr(availableName);
					}
					target.setLastUse(-1);
					target.setNextUse(-1);
				}
				
				definedVr.add(target.getVr());
				target.setDefine(i);
			}
			
			i--;
		}
		
		if(!liveRegisters.isEmpty())
		{
			throw new UseUndefinedRegisterException();
		}
	}
	
	public String toString()
	{
		String s="";
		for(int i=0;i<instructions.size();i++)
		{
			s+=instructions.get(i)+"\n";
		}
		
		Iterator<Integer> iter=useFrequency.keySet().iterator();
		while(iter.hasNext())
		{
			int x=iter.next();
			s+="v"+x+": "+useFrequency.get(x)+"\n";
		}
		
		return s;
	}
	
	private int getAvailableName()
	{
		return renameCount--;
	}
	
	public abstract void allocateRegister();
}
