import java.util.ArrayList;
import java.util.HashMap;



import java.util.Iterator;


public abstract class AAllocator {

	private int renameCount;
	protected ArrayList<Instruction> instructions;
	private ArrayList<String> newInstructions;

	protected HashMap<Integer, Register> allocatedRegisters; //virtual register: register(physical)
	protected PhysicalRegisters physicalRegisters;

	private int spillCount;
	private HashMap<Integer, Integer> spillMap; //virtual register: memory location

	protected HashMap<Integer, Integer> useFrequencyCount; // vr:count
	protected Register currentlyUsedRegister;

	public AAllocator(int numPhysicalRegisters, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException 
	{
		renameCount=-2;

		useFrequencyCount=new HashMap<Integer, Integer>();


		this.instructions=instructions;

		int maxLive=calculateLiveRange();

		spillCount=0;
		spillMap=new HashMap<Integer, Integer>();
		allocatedRegisters=new HashMap<Integer, Register>();
		newInstructions=new ArrayList<String>();
		
		System.out.println(this);

		int numReserveRegisters;
		if(maxLive<=numPhysicalRegisters)
		{
			//no reserve needed
			numReserveRegisters=0;
		}
		else
		{
			//reserve
			numReserveRegisters=2;
		}

		physicalRegisters=new PhysicalRegisters(numPhysicalRegisters, numReserveRegisters);
	}

	public void run() throws NoFreeRegisterException 
	{
		allocateRegister();
	}

	private int getAvailableName()
	{
		return renameCount--;
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

	private int calculateLiveRange() throws UseUndefinedRegisterException
	{
		int maxLive=0;
		HashMap<Integer, Integer> liveRegisters=new HashMap<Integer,Integer>();
		ArrayList<Integer> definedVr=new ArrayList<Integer>();
		HashMap<Integer, Integer> renameList=new HashMap<Integer,Integer>();
		HashMap<Integer, Integer> registerUse=new HashMap<Integer, Integer>();

		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			
			if(instruction.getTarget()!=null)
			{
				Register target=instruction.getTarget();

				rename(target,renameList);

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

			if(instruction.getSource1()!=null)
			{
				Register source1=instruction.getSource1();

				rename(source1,renameList);

				if(liveRegisters.get(source1.getVr())!=null)
				{
					int lastUse=liveRegisters.get(source1.getVr());
					source1.setLastUse(lastUse);
					source1.setNextUse(registerUse.get(source1.getVr()));
					registerUse.put(source1.getVr(), i);
					int x=useFrequencyCount.get(source1.getVr());
					x++;
					useFrequencyCount.put(source1.getVr(), x);
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
					if(liveRegisters.size()>maxLive)
					{
						maxLive=liveRegisters.size();
					}
					source1.setNextUse(i);
					registerUse.put(source1.getVr(), i);
					useFrequencyCount.put(source1.getVr(), 1);
				}
			}

			if(instruction.getSource2()!=null)
			{
				Register source2=instruction.getSource2();

				rename(source2,renameList);

				if(liveRegisters.get(source2.getVr())!=null)
				{
					int lastUse=liveRegisters.get(source2.getVr());
					source2.setLastUse(lastUse);
					source2.setNextUse(registerUse.get(source2.getVr()));
					registerUse.put(source2.getVr(), i);
					int x=useFrequencyCount.get(source2.getVr());
					x++;
					useFrequencyCount.put(source2.getVr(), x);
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
					if(liveRegisters.size()>maxLive)
					{
						maxLive=liveRegisters.size();
					}
					source2.setNextUse(i);
					registerUse.put(source2.getVr(), i);
					useFrequencyCount.put(source2.getVr(), 1);
				}

			}

			i--;
		}

		System.out.println("MaxLive is "+maxLive);

		if(!liveRegisters.isEmpty())
		{
			throw new UseUndefinedRegisterException();
		}

		return maxLive;
	}

	@Override
	public String toString()
	{
		String s="Output: \n";

		for(int i=0;i<instructions.size();i++)
		{
			s+=i+": "+instructions.get(i);
		}

		s+="\n";

		for (int i=0;i<newInstructions.size();i++)
		{
			s+=newInstructions.get(i)+"\n";
		}

		s+="\n";

		Iterator<Integer> iter=useFrequencyCount.keySet().iterator();
		while(iter.hasNext())
		{
			int x=iter.next();
			s+="v"+x+": "+useFrequencyCount.get(x)+"\n";
		}

		return s;
	}

	public void allocateRegister() throws NoFreeRegisterException {

		for(int i=0;i<instructions.size();i++)
		{
			currentlyUsedRegister=null;
			ArrayList<Register> toBeFreeList=new ArrayList<Register>();

			Instruction currentInstruction=instructions.get(i);

			if(currentInstruction.getOpcode().equals("loadI"))
			{
				Instruction nextInstruction=instructions.get(i+1);
				if(nextInstruction.getOpcode().equals("load"))
				{
					System.out.println("loadI, load pattern recognized");
					//TODO
				}
			}

			if(currentInstruction.getSource1()!=null)
			{
				int prSource1=ensure(currentInstruction.getSource1());
				currentInstruction.getSource1().setPr(prSource1);

				if(currentInstruction.getSource1().getLastUse()<=i)
				{
					//unAllocate(in.getSource1());
					toBeFreeList.add(currentInstruction.getSource1());
					currentlyUsedRegister=currentInstruction.getSource1();
				}

			}

			if(currentInstruction.getSource2()!=null)
			{
				int prSource2=ensure(currentInstruction.getSource2());
				currentInstruction.getSource2().setPr(prSource2);

				if(currentInstruction.getSource2().getLastUse()<=i)
				{
					//unAllocate(in.getSource2());
					toBeFreeList.add(currentInstruction.getSource2());
				}
			}
			
			Iterator<Register> iter=toBeFreeList.iterator();
			while(iter.hasNext())
			{
				unAllocate(iter.next());
			}
			
			currentlyUsedRegister=null;

			if(currentInstruction.getTarget()!=null)
			{
				if(currentInstruction.getTarget().getLastUse()>i)
				{
					int prTarget=allocate(currentInstruction.getTarget());
					currentInstruction.getTarget().setPr(prTarget);
					allocatedRegisters.put(currentInstruction.getTarget().getVr(),currentInstruction.getTarget());
				}
				else
				{
					//TODO check if it is arithmetic operation, if yes, output the line and free register
				}
			}

			String newInstruction=getStringFromInstruction(currentInstruction);
			newInstructions.add(newInstruction);
			System.out.println(newInstruction+" "+physicalRegisters);

			
		}

	}

	private String getStringFromInstruction(Instruction instruction)
	{
		String opcode=instruction.getOpcode();
		String result=opcode;

		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			result+=" r"+instruction.getSource1().getPr()+", r"+instruction.getSource2().getPr()+" => r"+instruction.getTarget().getPr();
			//result+="  ";
			//result+=" r"+instruction.getSource1().getVr()+", r"+instruction.getSource2().getVr()+" => r"+instruction.getTarget().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
		{
			//output
			result+=" "+instruction.getImmediateValue();

		}
		else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
		{
			//loadI
			result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getPr();
			//result+="  ";
			//result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getVr();

		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getSource2().getPr();
			//result+="     ";
			//result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getSource2().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getTarget().getPr();
			//result+="      ";
			//result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getTarget().getVr();
		}

		return result;
	}

	private int ensure(Register unAllocatedRegister) throws NoFreeRegisterException
	{
		int result;

		if(allocatedRegisters.get(unAllocatedRegister.getVr())!=null)//check if instruction is in pr
		{
			result=allocatedRegisters.get(unAllocatedRegister.getVr()).getPr();
		}
		else//else get from spilled
		{
			result=unSpill(unAllocatedRegister);
		}

		return result;
	}

	private int allocate(Register unAllocatedRegister) throws NoFreeRegisterException
	{
		try
		{
			int result=physicalRegisters.getNonReservedRegister(unAllocatedRegister.getVr());
			return result;
		}
		catch(NoNonReservedFreeRegisterException e)
		{
			spill(getToBeSpilledRegister()); //should not be vr, should be immediate

			return allocate(unAllocatedRegister);
		}

	}
	
	protected abstract Register getToBeSpilledRegister();

	protected void unAllocate(Register register)
	{
		physicalRegisters.returnFreeRegister(register.getPr());
		allocatedRegisters.remove(register.getVr());
		//register.setPr(-1);
	}

	protected void spill(Register registerToBeSpill) throws NoFreeRegisterException
	{
		int tempPhysicalRegister=physicalRegisters.getReservedRegister();

		int spillLocation=getSpillMemoryLocation();
		spillMap.put(registerToBeSpill.getVr(), spillLocation);

		String s1="loadI "+spillLocation+ " => r"+tempPhysicalRegister;//loadI spillLocation => pr
		newInstructions.add(s1);
		String s2="store r"+registerToBeSpill.getPr()+ " => r"+tempPhysicalRegister;//load registerToBeSpill => pr
		newInstructions.add(s2);

		physicalRegisters.returnFreeRegister(tempPhysicalRegister);
		unAllocate(registerToBeSpill);
		allocatedRegisters.remove(new Integer(registerToBeSpill.getVr()));

		System.out.println(s1+"   Address in v"+tempPhysicalRegister);
		System.out.println(s2+"   Spill v"+registerToBeSpill.getVr());
	}

	protected int unSpill(Register registerToUnSpill) throws NoFreeRegisterException
	{
		int prDestination=allocate(registerToUnSpill);

		int spillLocation=spillMap.get(registerToUnSpill.getVr());

		String s1="loadI "+spillLocation+ " => r"+prDestination;
		newInstructions.add(s1);
		String s2="load r"+prDestination+ " => r"+prDestination;
		newInstructions.add(s2);

		allocatedRegisters.put(registerToUnSpill.getVr(), registerToUnSpill);

		System.out.println(s1+"   Address in v"+prDestination);
		System.out.println(s2+"   UnSpill v"+registerToUnSpill.getVr());

		return prDestination;
	}

	private int getSpillMemoryLocation()
	{
		//TODO if spillCount>1023, reuse spill
		int result=spillCount;
		spillCount+=4;
		return result;
	}
}
