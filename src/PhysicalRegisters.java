import java.util.ArrayList;


public class PhysicalRegisters {
	
	private ArrayList<Integer> freeRegisters; //Of physical register
	private int[] phsicalRegisters;
	private int numReservedRegisters;
	
	public PhysicalRegisters(int numPhysicalRegisters, int numReservedRegisters)
	{
		this.numReservedRegisters=numReservedRegisters;
		phsicalRegisters=new int [numPhysicalRegisters];
		for(int i=0;i<phsicalRegisters.length;i++)
		{
			phsicalRegisters[i]=-1;
		}
		
		freeRegisters=new ArrayList<Integer>();
		for(int i=0; i<phsicalRegisters.length; i++)
		{
			freeRegisters.add(i);
		}
	}
	
	public String toString()
	{
		String s="Physical Registers status: \n";
		for(int i=0;i<phsicalRegisters.length;i++)
		{
			s+="p"+i+":v"+phsicalRegisters[i]+" ";
		}
		return s;
	}
	
	public int capacity()
	{
		return phsicalRegisters.length;
	}
	
	public boolean hasFreeRegister()
	{
		return freeRegisters.size()>=numReservedRegisters;
	}
	
	public int getNonReservedRegister(int virtualRegisterNumber) throws NoNonReservedFreeRegisterException, NoFreeRegisterException
	{
		if(hasFreeRegister())
		{
			return getRegister(virtualRegisterNumber);
		}
		else
		{
			throw new NoNonReservedFreeRegisterException();
		}
	}
	
	public int getRegister(int virtualRegisterNumber) throws NoFreeRegisterException
	{
		if(!freeRegisters.isEmpty())
		{
			int result=freeRegisters.remove(0);
			phsicalRegisters[result]=virtualRegisterNumber;
			return result;
		}
		else
		{
			throw new NoFreeRegisterException();
		}
	}

	public void returnFreeRegister(int physicalRegister)
	{
		freeRegisters.add(physicalRegister);
		phsicalRegisters[physicalRegister]=-1;
	}

}
