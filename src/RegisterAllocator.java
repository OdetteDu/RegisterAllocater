import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExtraTokenException;
import exception.InvalidArrowException;
import exception.InvalidOpcodeException;
import exception.InvalidRegisterNameException;
import exception.UseUndefinedRegisterException;


public class RegisterAllocator {
	
	private boolean useTopDown;
	private int numRegister;
	private String filePath;
	private ArrayList<Instruction> instructions;
	
	public RegisterAllocator(boolean useTopDown, int numRegister, String filePath)
	{
		instructions=new ArrayList<Instruction>();
		this.useTopDown=useTopDown;
		this.numRegister=numRegister;
		this.filePath=filePath;
	}
	
	public String toString()
	{
		String s="";
		s+="File: "+filePath+"\n";
		
		if(useTopDown)
		{
			s+="Use Top Down Mode\n";
		}
		else
		{
			s+="Use Buttom Up Mode\n";
		}
		
		s+="Number of Registers: "+numRegister+"\n";
		
		s+="Instructions: \n";
		
		for(int i=0;i<instructions.size();i++)
		{
			s+=instructions.get(i)+"\n";
		}
		
		return s;
	}
	
	public void allocate() throws UseUndefinedRegisterException
	{
		AAllocator allocator;
		if(useTopDown)
		{
			allocator=new TopDownAllocator(numRegister,instructions);
		}
		else
		{
			allocator=new ButtomUpAllocator(numRegister,instructions);
		}
		
		allocator.run();
	}
	
	public void run()
	{
		try
		{
			FileReader fr=new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			Scanner scanner=new Scanner();
			Parser parser=new Parser();
			
			try {
				String temp=br.readLine();
				while(temp!=null)
				{
					ArrayList<String> tokens=scanner.scanLine(temp);
					if(!tokens.isEmpty())
					{
						Instruction instruction=parser.parseLine(tokens);
						instructions.add(instruction);
					}
					
					temp=br.readLine();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidOpcodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidRegisterNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArrowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExtraTokenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws UseUndefinedRegisterException
	{
		String mode=args[0];
		boolean useTopDown=false;

		if(mode.equals("t"))
		{
			useTopDown=true;
		}
		else if(mode.equals("b"))
		{
			useTopDown=false;
		}
		else
		{
			System.out.println("InvalidArgumentException: the argument entered is not valid, should be either b for buttom up, or t for top down.\n");
		}
		
		int numRegisters=Integer.parseInt(args[1]);
		
		String filePath=args[2];
		
		RegisterAllocator ra=new RegisterAllocator(useTopDown, numRegisters,filePath);
		ra.run();
		ra.allocate();
		System.out.println(ra);
	}
	


		

}
