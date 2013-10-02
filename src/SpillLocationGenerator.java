
public class SpillLocationGenerator {
	
	private int spillCount;
	
	public int getSpillMemoryLocation() throws NoEnoughMemoryToSpillException
	{
		if(spillCount>1023)
		{
			throw new NoEnoughMemoryToSpillException();
		}
		
		int result=spillCount;
		spillCount+=4;
		return result;
	}

}