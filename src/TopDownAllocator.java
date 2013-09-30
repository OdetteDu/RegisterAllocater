import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import exception.UseUndefinedRegisterException;


public class TopDownAllocator extends AAllocator {
	
	private ArrayList<Integer> freeRegister; //Of physical register
	private HashMap<Integer, Integer> spillMap; //virtual register: memory location
	private int spillCount;
	private ArrayList<String> newInstructions;
	private HashMap<Integer, Register> assignedVirtualRegister; //virtual register: register(physical)

	public TopDownAllocator(int numPhysicalRegister, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException
	{
		super(numPhysicalRegister,instructions);
		spillCount=0;
		spillMap=new HashMap<Integer, Integer>();
		assignedVirtualRegister=new HashMap<Integer, Register>();
		newInstructions=new ArrayList<String>();
		freeRegister=new ArrayList<Integer>();
		for(int i=0; i<prs.length; i++)
		{
			freeRegister.add(i);
		}
	}

	@Override
	public void allocateRegister() {
		
		for(int i=0;i<instructions.size();i++)
		{
			Instruction in=instructions.get(i);
			
			if(in.getSource1()!=null)
			{
				int prSource1=ensure(in.getSource1());
				in.getSource1().setPr(prSource1);
				
				if(in.getSource1().getLastUse()==i)
				{
					prs[in.getSource1().getPr()]=-1;
					freeRegister.add(new Integer(in.getSource1().getPr()));
					assignedVirtualRegister.remove(in.getSource1().getVr());
				}
				
			}
			
			if(in.getSource2()!=null)
			{
				int prSource2=ensure(in.getSource2());
				in.getSource2().setPr(prSource2);
				
				if(in.getSource2().getLastUse()==i)
				{
					prs[in.getSource2().getPr()]=-1;
					freeRegister.add(new Integer(in.getSource2().getPr()));
					assignedVirtualRegister.remove(in.getSource2().getVr());
				}
			}
			
			if(in.getTarget()!=null)
			{
				int prTarget=allocate(in.getTarget().getVr());
				in.getTarget().setPr(prTarget);
				assignedVirtualRegister.put(in.getTarget().getVr(),in.getTarget());
			}
			
			String newInstruction=getNewInstruction(in);
			System.out.println(newInstruction+" "+getPhysicalRegisterStatus());
			newInstructions.add(newInstruction);
			//prStatus[i]=getPhysicalRegisterStatus();
		}
		
	}
	
	private String getPhysicalRegisterStatus()
	{
		String s=" ";
		for(int i=0;i<prs.length;i++)
		{
			s+="p"+i+":v"+prs[i]+" ";
		}
		return s;
	}
	
	private String getNewInstruction(Instruction instruction)
	{
		String opcode=instruction.getOpcode();
		String result=opcode;
		
		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			result+=" p"+instruction.getSource1().getPr()+", p"+instruction.getSource2().getPr()+" => p"+instruction.getTarget().getPr();
			result+="  ";
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
			result+=" "+instruction.getImmediateValue()+" => p"+instruction.getTarget().getPr();
			result+="  ";
			result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getVr();
			
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			result+=" p"+instruction.getSource1().getPr()+" => p"+instruction.getSource2().getPr();
			result+="     ";
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getSource2().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			result+=" p"+instruction.getSource1().getPr()+" => p"+instruction.getTarget().getPr();
			result+="      ";
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getTarget().getVr();
		}
		
		return result;
	}
	
	private int ensure(Register vr)
	{
		int result=-1;
		//if instruction is in pr
		for(int i=0;i<prs.length;i++)
		{
			if(prs[i]==vr.getVr())
			{
				//return its physical register
				result=i;
				assignedVirtualRegister.put(vr.getVr(), vr);
			}
		}
		
		//else get from spilled 
		if(result==-1)
		{
			//allocate
			result=unSpill(vr,allocate(vr.getVr()),allocate(vr.getVr()));
		}
		
		return result;
	}
	
	private int allocate(int virtualRegister)
	{
		int result=-1;
		
		if(freeRegister.size()<2)//spill
		{
			Register lowestFrequency=null;
			int minFrequency=instructions.size();
			Iterator<Integer> iter=assignedVirtualRegister.keySet().iterator();
			while(iter.hasNext())
			{
				int vr=iter.next();
				if(useFrequency.get(vr)<minFrequency)
				{
					minFrequency=useFrequency.get(vr);
					lowestFrequency=assignedVirtualRegister.get(vr);
				}
				
			}
			
			spill(lowestFrequency, allocateRegister(virtualRegister)); //should not be vr, should be immediate
		}
		
		result=allocateRegister(virtualRegister);
						
		return result;
	}
	
	private int allocateRegister(int vr)
	{
		int result=freeRegister.remove(0);
		prs[result]=vr;
		return result;
	}
	
	private void unAllocateRegister(int pr)
	{
		freeRegister.add(pr);
		prs[pr]=-1;
	}
	
	private void spill(Register registerToBeSpill, int pr)
	{
		int spillLocation=getSpillMemoryLocation();
		spillMap.put(registerToBeSpill.getVr(), spillLocation);
		//loadI spillLocation => pr
		String s1="loadI "+spillLocation+ " => p"+pr;
		newInstructions.add(s1);
		//load registerToBeSpill => pr
		String s2="store p"+registerToBeSpill.getPr()+ " => p"+pr;
		newInstructions.add(s2);
		System.out.println(s1);
		System.out.println(s2);
		unAllocateRegister(pr);
		unAllocateRegister(registerToBeSpill.getPr());
		assignedVirtualRegister.remove(new Integer(registerToBeSpill.getVr()));
		//registerToBeSpill.setPr(-1);
		//assignedVirtualRegister.put(registerToBeSpill.getVr(), registerToBeSpill);
	}
	
	private int unSpill(Register registerToUnSpill, int prTemp, int prDestination)
	{
		int spillLocation=spillMap.get(registerToUnSpill.getVr());
		String s1="loadI "+spillLocation+ " => p"+prTemp;
		newInstructions.add(s1);
		String s2="load p"+prTemp+ " => p"+prDestination;
		newInstructions.add(s2);
		System.out.println(s1);
		System.out.println(s2);
		//prs[prDestination]=registerToUnSpill.getVr();
		assignedVirtualRegister.put(registerToUnSpill.getVr(), registerToUnSpill);
		unAllocateRegister(prTemp);
		return prDestination;
	}
	
	private int getSpillMemoryLocation()
	{
		//TODO if spillCount>1023, reuse spill
		int result=spillCount;
		spillCount+=4;
		return result;
	}
	
	public String toString()
	{
		String s="Output: \n";
		for (int i=0;i<newInstructions.size();i++)
		{
			s+=newInstructions.get(i)+"\n";
		}
		return s;
	}
}
