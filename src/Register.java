
public class Register {
	
	private int pr;
	private int vr;
	private int lastUse;
	private int define;

	public Register(int vr)
	{
		this.vr=vr;
		this.pr=-1;
		this.lastUse=-1;
		this.define=-1;
	}
	
	public String toString()
	{
		return "(vr:"+getVr()+" pr:"+pr+" use:"+getLastUse()+" define:"+getDefine()+")";
	}

	public int getPr() {
		return pr;
	}

	public void setPr(int pr) {
		this.pr = pr;
	}

	public int getVr() {
		return vr;
	}

	public void setVr(int vr) {
		this.vr = vr;
	}

	public int getLastUse() {
		return lastUse;
	}

	public void setLastUse(int lastUse) {
		this.lastUse = lastUse;
	}

	public int getDefine() {
		return define;
	}

	public void setDefine(int define) {
		this.define = define;
	}
}
