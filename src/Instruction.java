


public class Instruction {
	
	public static String [] validOpcode={"load","store","add","sub","mult","lshift","rshift","loadI","output"};
	public static String [] validOpcodeWithSource1Source2Target={"add","sub","mult","lshift","rshift"};
	public static String validOpcodeWithSource1Target="load";
	public static String validOpcodeWithSource1Source2="store";
	public static String validOpcodeWithTargetImmediateValue="loadI";
	public static String validOpcodeWithImmediateValue="output";

	private String opcode;
	private Register source1, source2;
	private Register target;
	private String immediateValue;
	
	public Instruction(String opcode)
	{
		this.opcode=opcode;
	}
	
	public String toString()
	{
		String s="Opcode: "+opcode+" ";
		
		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			s+="Source1: "+source1+" Source2: "+source2+" Target: "+target;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
		{
			s+="Immediate: "+immediateValue;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
		{
			s+="Immediate: "+immediateValue+" Target: "+target;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			s+="Source1: "+source1+" Source2: "+source2;
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			s+="Source1: "+source1+" Target: "+target;	
		}
		
		s+="\n";
		
		return s;
	}
	
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public Register getSource1() {
		return source1;
	}
	public void setSource1(Register source1) {
		this.source1 = source1;
	}
	public Register getSource2() {
		return source2;
	}
	public void setSource2(Register source2) {
		this.source2 = source2;
	}
	public Register getTarget() {
		return target;
	}
	public void setTarget(Register target) {
		this.target = target;
	}
	public String getImmediateValue() {
		return immediateValue;
	}
	public void setImmediateValue(String immediateValue) {
		this.immediateValue = immediateValue;
	}
	
	public static boolean isValidOpcodeWithSource1Source2Target(String opcode)
	{
		boolean b=false;
		for(int i=0;i<validOpcodeWithSource1Source2Target.length;i++)
		{
			if(opcode.equals(validOpcodeWithSource1Source2Target[i]))
			{
				b=true;
			}
		}
		
		return b;
	}

}
