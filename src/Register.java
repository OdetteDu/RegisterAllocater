
public class Register {
	
	private int pr;
	private int vr;
	private int lastUse;
	private int define;

	public Register(int vr)
	{
		this.vr=vr;
	}
	
	public String toString()
	{
		return "(vr:"+vr+" pr:"+pr+" use:"+lastUse+" define:"+define+")";
	}
}
