import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class RegisterAllocator {

	private boolean useTopDown;
	private int numRegister;
	private String filePath;
	private ArrayList<Instruction> instructions;
	private AAllocator allocator;

	public RegisterAllocator(boolean useTopDown, int numRegister, String filePath)
	{
		instructions=new ArrayList<Instruction>();
		this.useTopDown=useTopDown;
		this.numRegister=numRegister;
		this.filePath=filePath;
	}

	public void readFile() throws ImmediateValueNotIntegerException, InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, InvalidCommandLineArgumentException
	{
		try
		{
			FileReader fr=new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			Scanner scanner=new Scanner();
			Parser parser=new Parser();

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
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			throw new InvalidCommandLineArgumentException("The filePath is invalid or the file can not be found.");
		}
		catch (IOException e) {
			throw new InvalidCommandLineArgumentException("The file is unavailable to open correctly. ");
		} 
	}

	public String allocate() throws UseUndefinedRegisterException, NoFreeRegisterException, NoEnoughMemoryToSpillException
	{
		if(useTopDown)
		{
			allocator=new TopDownAllocator(numRegister,instructions);
		}
		else
		{
			allocator=new ButtomUpAllocator(numRegister,instructions);
		}

		return allocator.getOutput();
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

		s+=allocator;

		return s;
	}

	public String run() throws ImmediateValueNotIntegerException, UseUndefinedRegisterException, NoFreeRegisterException, NoEnoughMemoryToSpillException, InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, InvalidCommandLineArgumentException
	{
		readFile();
		return allocate();
	}

	public static void main(String args[])
	{
		try{
			if(args.length!=3)
			{
				throw new InvalidCommandLineArgumentException();
			}

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
				throw new InvalidCommandLineArgumentException();
			}

			int numRegisters=Integer.parseInt(args[1]);
			String filePath=args[2];
			RegisterAllocator registerAllocator=new RegisterAllocator(useTopDown, numRegisters,filePath);
			System.out.println(registerAllocator.run());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}





}
