import java.util.ArrayList;
import java.util.HashMap;

import exception.ExtraTokenException;


public class ButtomUpAllocator extends AAllocator {
	
	private int numFreeRegister;
	private HashMap<Integer, Integer> spillMap;
	private int spillCount;
	private ArrayList<String> newInstructions;
	
	public ButtomUpAllocator(int numPhysicalRegister,ArrayList<Instruction> instructions)
	{
		super(numPhysicalRegister,instructions);
		numFreeRegister=prs.length;
		spillCount=0;
		spillMap=new HashMap<Integer, Integer>();
		newInstructions=new ArrayList<String>();
	}

	@Override
	public void allocateRegister() {
		
		for(int i=0;i<instructions.size();i++)
		{
			Instruction in=instructions.get(i);
			if(in.getTarget()!=null)
			{
				int prTarget=allocate(in.getTarget().getVr());
				in.getTarget().setPr(prTarget);
			}
			
			if(in.getSource1()!=null)
			{
				int prSource1=ensure(in.getSource1().getVr());
				in.getSource1().setPr(prSource1);
			}
			
			if(in.getSource2()!=null)
			{
				int prSource2=ensure(in.getSource2().getVr());
				in.getSource2().setPr(prSource2);
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
		
		//else
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
		
		if(numFreeRegister<2)
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
