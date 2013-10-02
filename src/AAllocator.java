import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class AAllocator {

	private int renameCount;
	protected ArrayList<Instruction> instructions;
	private ArrayList<String> newInstructions;

	protected HashMap<Integer, Register> allocatedRegisters; //virtual register: register(physical)
	protected PhysicalRegisters physicalRegisters;

	protected HashMap<Integer, Integer> spillMap; //virtual register: memory location
	protected HashMap<Integer, Integer> useFrequencyCount; // vr:count
	protected HashMap<Integer, Integer> constantMap; //virtual register: immediateValue
	protected Register currentlyUsedRegister;

	private SpillLocationGenerator spillLocationGenerator;

	public AAllocator(int numPhysicalRegisters, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException 
	{
		spillLocationGenerator=new SpillLocationGenerator();

		renameCount=-2;

		useFrequencyCount=new HashMap<Integer, Integer>();


		this.instructions=instructions;

		calculateLiveRange();
		int maxLive=calculateMaxLive();

		spillMap=new HashMap<Integer, Integer>();
		allocatedRegisters=new HashMap<Integer, Register>();
		constantMap=new HashMap<Integer, Integer>();
		newInstructions=new ArrayList<String>();

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

//		System.out.println("MaxLive is: "+maxLive);
//		System.out.println("Number of Reserved Register is: "+numReserveRegisters);

		physicalRegisters=new PhysicalRegisters(numPhysicalRegisters, numReserveRegisters);
	}

	public String getOutput() throws NoFreeRegisterException, NoEnoughMemoryToSpillException 
	{
		allocateRegister();
		
		String s="";
		for (int i=0;i<newInstructions.size();i++)
		{
			s+=newInstructions.get(i)+"\n";
		}
		return s;
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
					source2.setNextUse(i);
					registerUse.put(source2.getVr(), i);
					useFrequencyCount.put(source2.getVr(), 1);
				}

			}

			i--;
		}

		if(!liveRegisters.isEmpty())
		{
			String exceptionMessage="";
			Iterator<Integer> iter=liveRegisters.keySet().iterator();
			while(iter.hasNext())
			{
				exceptionMessage+="r"+iter.next()+" ";
			}
			throw new UseUndefinedRegisterException(exceptionMessage);
		}
	}

	private int calculateMaxLive()
	{
		int maxLive=0;
		ArrayList<Integer> liveRegisters=new ArrayList<Integer>();

		for(int i=0;i<instructions.size();i++)
		{
			Instruction currentInstruction=instructions.get(i);

			if(currentInstruction.getSource1()!=null)
			{
				Register source1=currentInstruction.getSource1();
				if(source1.getLastUse()==i)
				{
					liveRegisters.remove(new Integer(source1.getVr()));
				}
			}

			if(currentInstruction.getSource2()!=null)
			{
				Register source2=currentInstruction.getSource2();
				if(source2.getLastUse()==i)
				{
					liveRegisters.remove(new Integer(source2.getVr()));
				}
			}

			if(currentInstruction.getTarget()!=null)
			{
				Register target=currentInstruction.getTarget();
				liveRegisters.add(new Integer(target.getVr()));
			}

			if(maxLive<liveRegisters.size())
			{
				maxLive=liveRegisters.size();
			}
		}

		return maxLive;
	}

	@Override
	public String toString()
	{
		String s="";
		
		s+="Parsed Instructions: \n";

		for(int i=0;i<instructions.size();i++)
		{
			s+=i+": "+instructions.get(i);
		}

		s+="\n";

		s+="Renamed VR: \n";
		for (int i=0;i<instructions.size();i++)
		{
			s+=this.getStringVRFromInstruction(instructions.get(i))+"\n";
		}

		s+="\n";

		s+="Output instructions: \n";
		for (int i=0;i<newInstructions.size();i++)
		{
			s+=newInstructions.get(i)+"\n";
		}

		s+="\n";

		s+="Constant Map: \n";
		Iterator<Integer> iter=constantMap.keySet().iterator();
		while(iter.hasNext())
		{
			Integer key=iter.next();
			s+="v"+key+": "+constantMap.get(key)+"\n";
		}
		s+="\n";

		return s;
	}

	public void allocateRegister() throws NoFreeRegisterException, NoEnoughMemoryToSpillException {

		for(int i=0;i<instructions.size();i++)
		{
			currentlyUsedRegister=null;
			ArrayList<Register> toBeFreeList=new ArrayList<Register>();
			Instruction currentInstruction=instructions.get(i);

			if(currentInstruction.getOpcode().equals("loadI"))
			{

				int immediateValue=currentInstruction.getImmediateValue();
				constantMap.put(currentInstruction.getTarget().getVr(),immediateValue);

			}

			if(currentInstruction.getSource1()!=null)
			{
				int prSource1=ensure(currentInstruction.getSource1());
				currentInstruction.getSource1().setPr(prSource1);
				currentlyUsedRegister=currentInstruction.getSource1();

				if(currentInstruction.getSource1().getLastUse()<=i)
				{
					toBeFreeList.add(currentInstruction.getSource1());

				}

			}

			if(currentInstruction.getSource2()!=null)
			{
				if(currentInstruction.getSource1().getVr()==currentInstruction.getSource2().getVr())
				{
					int prSource2=currentInstruction.getSource1().getPr();
					currentInstruction.getSource2().setPr(prSource2);
				}
				else
				{
					int prSource2=ensure(currentInstruction.getSource2());
					currentInstruction.getSource2().setPr(prSource2);

					if(currentInstruction.getSource2().getLastUse()<=i)
					{
						toBeFreeList.add(currentInstruction.getSource2());
					}
				}
			}

			while(!toBeFreeList.isEmpty())
			{
				unAllocate(toBeFreeList.remove(toBeFreeList.size()-1));
			}

			if(currentlyUsedRegister!=null)
			{
				currentInstruction.setSource1(currentlyUsedRegister);
				currentlyUsedRegister=null;
			}

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
					if(Instruction.isValidOpcodeWithSource1Source2Target(currentInstruction.getOpcode()))
					{
						int prTarget=allocate(currentInstruction.getTarget());
						currentInstruction.getTarget().setPr(prTarget);
						allocatedRegisters.put(currentInstruction.getTarget().getVr(),currentInstruction.getTarget());
						toBeFreeList.add(currentInstruction.getTarget());
					}
					else
					{
						continue;
					}
				}
			}

			String newInstruction=getStringPRFromInstruction(currentInstruction);
			newInstructions.add(newInstruction);
			//System.out.println(newInstruction+physicalRegisters);

			while(!toBeFreeList.isEmpty())
			{
				unAllocate(toBeFreeList.remove(toBeFreeList.size()-1));
			}
		}
	}

	public String getStringPRFromInstruction(Instruction instruction)
	{
		String opcode=instruction.getOpcode();
		String result=opcode;

		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			result+=" r"+instruction.getSource1().getPr()+", r"+instruction.getSource2().getPr()+" => r"+instruction.getTarget().getPr();
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

		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getSource2().getPr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getTarget().getPr();
		}

		return result;
	}

	public String getStringVRFromInstruction(Instruction instruction)
	{
		String opcode=instruction.getOpcode();
		String result=opcode;

		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			result+=" r"+instruction.getSource1().getVr()+", r"+instruction.getSource2().getVr()+" => r"+instruction.getTarget().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
		{
			//output
			result+=" "+instruction.getImmediateValue();

		}
		else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
		{
			//loadI
			result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getVr();

		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getSource2().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getTarget().getVr();
		}

		return result;
	}

	private int ensure(Register unAllocatedRegister) throws NoFreeRegisterException, NoEnoughMemoryToSpillException
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

	private int allocate(Register unAllocatedRegister) throws NoFreeRegisterException, NoEnoughMemoryToSpillException
	{
		try
		{
			int result=physicalRegisters.getNonReservedRegister(unAllocatedRegister.getVr());
			return result;
		}
		catch(NoNonReservedFreeRegisterException e)
		{
			try 
			{
				spill(getToBeSpilledRegister());
			} 
			catch (NoUnusedRegisterToSpillException ex) 
			{
				if(currentlyUsedRegister!=null)
				{
					try 
					{
						spill(currentlyUsedRegister);
						int result=physicalRegisters.getNonReservedRegister(unAllocatedRegister.getVr());
						int newPhysicalRegister=physicalRegisters.getRegister(currentlyUsedRegister.getVr());
						unSpill(currentlyUsedRegister, newPhysicalRegister);
						currentlyUsedRegister.setPr(newPhysicalRegister);
						return result;
					} 
					catch (NoNonReservedFreeRegisterException e1) 
					{
						throw new NoFreeRegisterException();
					}
				}
			} 

			return allocate(unAllocatedRegister);
		}

	}

	protected abstract Register getToBeSpilledRegister() throws NoUnusedRegisterToSpillException;

	protected void unAllocate(Register register)
	{
		physicalRegisters.returnFreeRegister(register.getPr());
		allocatedRegisters.remove(register.getVr());
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return false if should use A
	 *         true  if should use B
	 */
	protected boolean breakTie(Register a,Register b)
	{
		int vrA=a.getVr();
		int vrB=b.getVr();
		if(constantMap.containsKey(vrA) && (!constantMap.containsKey(vrB)))
		{
			return false;
		}
		else if(constantMap.containsKey(vrB) && (!constantMap.containsKey(vrA)))
		{
			return true;
		}
		else
		{
			if(spillMap.containsKey(vrA) && (!spillMap.containsKey(vrB)))
			{
				return false;
			}
			else if(spillMap.containsKey(vrB) && (!spillMap.containsKey(vrA)))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	protected void spill(Register registerToBeSpill) throws NoFreeRegisterException, NoEnoughMemoryToSpillException
	{
		if(constantMap.get(registerToBeSpill.getVr())!=null)
		{
//			System.out.println("r"+registerToBeSpill.getVr()+" is in constant map. No spill necessary. Free p"+registerToBeSpill.getPr());
		}
		else if(spillMap.get(registerToBeSpill.getVr())!=null)
		{
//			System.out.println("r"+registerToBeSpill.getVr()+" is in spill map. No spill necessary. Free p"+registerToBeSpill.getPr());
		}
		else
		{
			int spillLocation=spillLocationGenerator.getSpillMemoryLocation();
			spillMap.put(registerToBeSpill.getVr(), spillLocation);

			int tempPhysicalRegister=physicalRegisters.getReservedRegister();

			String s1="loadI "+spillLocation+ " => r"+tempPhysicalRegister;//loadI spillLocation => pr
			newInstructions.add(s1);
			String s2="store r"+registerToBeSpill.getPr()+ " => r"+tempPhysicalRegister;//load registerToBeSpill => pr
			newInstructions.add(s2);

			physicalRegisters.returnFreeRegister(tempPhysicalRegister);

			//			System.out.println(s1+"   Address in v"+tempPhysicalRegister);
			//			System.out.println(s2+"   Spill v"+registerToBeSpill.getVr());
		}

		unAllocate(registerToBeSpill);
	}

	protected int unSpill(Register registerToUnSpill) throws NoFreeRegisterException, NoEnoughMemoryToSpillException
	{
		return unSpill(registerToUnSpill, allocate(registerToUnSpill));
	}

	protected int unSpill(Register registerToUnSpill, int prDestination) throws NoFreeRegisterException
	{
		if(constantMap.get(registerToUnSpill.getVr())!=null)
		{
			int constantLocation=constantMap.get(registerToUnSpill.getVr());
			String s1="loadI "+constantLocation+ " => r"+prDestination;
			newInstructions.add(s1);
//			System.out.println(s1+"   UnSpill v"+registerToUnSpill.getVr()+" in "+prDestination);
		}
		else
		{
			int spillLocation=spillMap.get(registerToUnSpill.getVr());

			String s1="loadI "+spillLocation+ " => r"+prDestination;
			newInstructions.add(s1);
			String s2="load r"+prDestination+ " => r"+prDestination;
			newInstructions.add(s2);
		}
			allocatedRegisters.put(registerToUnSpill.getVr(), registerToUnSpill);

			//		System.out.println(s1+"   Address in v"+prDestination);
			//		System.out.println(s2+"   UnSpill v"+registerToUnSpill.getVr());

			return prDestination;
		
	}
}
