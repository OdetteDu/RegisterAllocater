
public class Register {
	
	private int pr;
	private int vr;
	private int nextUse;
	private int lastUse;
	private int define;

	public Register(int vr)
	{
		this.vr=vr;
		this.pr=-1;
		this.nextUse=-1;
		this.lastUse=-1;
		this.define=-1;
	}
	
	public String toString()
	{
		//return "(vr:"+getVr()+" pr:"+pr+" nextUse:"+getNextUse()+" lastUse:"+getLastUse()+" define:"+getDefine()+")";
		return "(vr:"+getVr()+" pr:"+pr+" nu:"+getNextUse()+" lu:"+getLastUse()+" d:"+getDefine()+")";
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

	public int getNextUse() {
		return nextUse;
	}

	public void setNextUse(int nextUse) {
		this.nextUse = nextUse;
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
