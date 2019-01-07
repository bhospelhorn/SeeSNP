package hospelhornbg_snps;

public class Allele {
	
	private AlleleType call;
	
	private int haploblock;
	
	public Allele(AlleleType alleleCall)
	{
		call = alleleCall;
		haploblock = -1;
	}
	
	public AlleleType getCall()
	{
		return call;
	}
	
	public int getHaploblock()
	{
		return haploblock;
	}
	
	public void setCall(AlleleType newcall)
	{
		call = newcall;
	}
	
	public void setHaploblock(int h)
	{
		haploblock = h;
	}

	public int getCopyNumberValue()
	{
		if (call == AlleleType.SMALL_DEL) return 0;
		if (call == AlleleType.SMALL_DUP) return 2;
		return 1;
	}
	
}
