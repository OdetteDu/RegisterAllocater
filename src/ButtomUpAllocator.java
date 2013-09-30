import java.util.ArrayList;
import java.util.HashMap;

import exception.ExtraTokenException;


public class ButtomUpAllocator extends AAllocator {
	
	private int numFreeRegister;
	private HashMap<Integer, Integer> spillMap;
	private int spillCount;
	private ArrayList<String> newInstructions;
	private HashMap<Integer, Register> assignedVirtualRegister;
	
	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions)
	{
		super(numPhysicalRegister,instructions);
		numFreeRegister=prs.length;
		spillCount=0;
		spillMap=new HashMap<Integer, Integer>();
		assignedVirtualRegister=new HashMap<Integer, Register>();
		newInstructions=new ArrayList<String>();
	}

	@Override
	public void allocateRegister() {
		
		for(int i=0;i<instructions.size();i++)
		{
			Instruction in=instructions.get(i);
			
			if(in.getSource1()!=null)
			{
				int prSource1=ensure(in.getSource1().getVr());
				in.getSource1().setPr(prSource1);
				
				if(in.getSource1().getLastUse()==i)
				{
//					if(assignedVirtualRegister.get(in.getSource1().getVr())!=null)
//					prs[assignedVirtualRegister.get(in.getSource1().getVr()).getPr()]=-1;
					prs[in.getSource1().getPr()]=-1;
					numFreeRegister++;
					assignedVirtualRegister.remove(in.getSource1().getVr());
				}
				
			}
			
			if(in.getSource2()!=null)
			{
				int prSource2=ensure(in.getSource2().getVr());
				in.getSource2().setPr(prSource2);
				
				if(in.getSource2().getLastUse()==i)
				{
//					if(assignedVirtualRegister.get(in.getSource2().getVr())!=null)
//						prs[assignedVirtualRegister.get(in.getSource2().getVr()).getPr()]=-1;
					prs[in.getSource2().getPr()]=-1;
					numFreeRegister++;
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
			newInstructions.add(newInstruction);
		}
		
	}
	
	private String getNewInstruction(Instruction instruction)
	{
		String opcode=instruction.getOpcode();
		String result=opcode;
		
		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			result+=" r"+instruction.getSource1().getPr()+", r"+instruction.getSource2().getPr()+" => r"+instruction.getTarget().getPr();
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
			result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getPr();
			result+="  ";
			result+=" "+instruction.getImmediateValue()+" => r"+instruction.getTarget().getVr();
			
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			//store
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getSource2().getPr();
			result+="     ";
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getSource2().getVr();
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			//load
			result+=" r"+instruction.getSource1().getPr()+" => r"+instruction.getTarget().getPr();
			result+="      ";
			result+=" r"+instruction.getSource1().getVr()+" => r"+instruction.getTarget().getVr();
		}
		
		return result;
	}
	
	private int ensure(int vr)
	{
		int result=-1;
		//if instruction is in controll
		for(int i=0;i<prs.length;i++)
		{
			if(prs[i]==vr)
			{
				//return its physical register
				result=i;
			}
		}
		
		//else get from spilled 
		if(result==-1)
		{
			//allocate
			result=allocate(vr);
		}
		
		return result;
	}
	
	private int allocate(int vr)
	{
		int result=-1;
		
		if(numFreeRegister<1)
		{
			//spill
		}
		else
		{
			for(int i=0;i<prs.length;i++)
			{
				if(prs[i]==-1)
				{
					result=i;
					numFreeRegister--;
					prs[i]=vr;
					break;
				}
			}
		}
			
		return result;
	}
	
	private void spill(int registerToBeSpill, int pr, int indexToInsert)
	{
		int spillLocation=getSpillMemoryLocation();
		//loadI spillLocation => pr
		newInstructions.add("loadI "+spillLocation+ " => r"+pr);
		//load registerToBeSpill => pr
		newInstructions.add("load r"+registerToBeSpill+ " => r"+pr);
		prs[pr]=-1;
		prs[registerToBeSpill]=-1;
	}
	
	private int getSpillMemoryLocation()
	{
		//TODO if spillCount>1023, reuse spill
		return spillCount+=4;
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
