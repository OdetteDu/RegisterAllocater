import java.util.ArrayList;


public class Scanner {
	
	public ArrayList<String> scanLine(String line)
	{
		ArrayList<String> tokens=new ArrayList<String>();
		String temp="";
		for(int i=0;i<line.length();i++)
		{
			char c=line.charAt(i);
			if(c==' ' || c=='\t'|| c==',')
			{
				tokens.add(temp);
				temp="";
			}
			else if(c=='/' && line.charAt(i+1)=='/')
			{
				break;
			}
			else if(c=='=' && line.charAt(i+1)=='>')
			{
				if(temp.length()!=0)
				{
					tokens.add(temp);
				}
				tokens.add("=>");
				i+=2;
			}
			else
			{
				temp+=c;
			}
		}
		tokens.add(temp);
		return tokens;
	}
}
