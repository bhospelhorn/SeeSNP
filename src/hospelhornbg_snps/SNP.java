package hospelhornbg_snps;

import hospelhornbg_genomeBuild.Contig;

public class SNP implements Comparable<SNP>{
	
	private static SNPSortOrder sortorder;
	
	private String id;
	
	private Contig chrom;
	private int pos;
	
	private Allele ref_allele;
	
	private char[] alleles;
	
	public SNP(String ID)
	{
		id = ID;
		chrom = null;
		pos = -1;
		alleles = null;
	}
	
	public String getID()
	{
		return id;
	}
	
	public Contig getChrom()
	{
		return chrom;
	}
	
	public void setChrom(Contig c)
	{
		chrom = c;
	}
	
	public int getPosition()
	{
		return pos;
	}
	
	public void setPosition(int p)
	{
		pos = p;
	}
	
	public int getAlleleCount()
	{
		if (alleles == null) return 0;
		else return alleles.length;
	}
	
	public char getAllele(int index)
	{
		if (alleles == null) return '\0';
		if (index < 0) return '\0';
		if (index >= alleles.length) return '\0';
		return alleles[index];
	}
	
	public boolean isHomozygous()
	{
		if (alleles == null) return true;
		if (alleles.length == 1) return true;
		char ref = alleles[0];
		for (int i = 1; i < alleles.length; i++)
		{
			if (alleles[i] != ref) return false;
		}
		return true;
	}
	
	public void parseAlleles(String genofield)
	{
		if (genofield == null || genofield.isEmpty())
		{
			alleles = null;
			return;
		}
		if (genofield.equals("--") || genofield.equals("NC"))
		{
			alleles = null;
			return;
		}
		
		int len = genofield.length();
		alleles = new char[len];
		for (int i = 0; i < len; i++) alleles[i] = genofield.charAt(i);
		
	}

	public static void setSortOrder(SNPSortOrder so)
	{
		if (so == null) so = SNPSortOrder.POSITION;
		sortorder = so;
	}

	public int hashCode()
	{
		int n1 = 0;
		if (id != null) n1 = id.hashCode();
		int n2 = 0;
		if (chrom != null) n2 = chrom.hashCode();
		return n1 ^ n2 ^ pos;
	}
	
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof SNP)) return false;
		
		SNP other = (SNP)o;
		if (this.pos != other.pos) return false;
		
		if(this.id != null && other.id != null)
		{
			if (!this.id.equals(other.id)) return false;
		}
		else
		{
			if(this.id == null && other.id != null) return false;
			if(this.id != null && other.id == null) return false;	
		}
		
		if(this.chrom != null && other.chrom != null)
		{
			if (!this.chrom.equals(other.chrom)) return false;
		}
		else
		{
			if(this.chrom == null && other.chrom != null) return false;
			if(this.chrom != null && other.chrom == null) return false;	
		}
		
		if(this.alleles != null && other.alleles != null)
		{
			if (this.alleles.length != other.alleles.length) return false;
			for (int i = 0; i < alleles.length; i++)
			{
				if (this.alleles[i] != other.alleles[i]) return false;
			}
		}
		else
		{
			if(this.alleles == null && other.alleles != null) return false;
			if(this.alleles != null && other.alleles == null) return false;	
		}
		
		return true;
	}

	private int comparePosition(SNP o)
	{
		//Compare chrom
		if(this.chrom == null)
		{
			if(o.chrom != null) return -1;
		}
		else
		{
			int ccomp = this.chrom.compareTo(o.chrom);
			if (ccomp != 0) return ccomp;
		}
		
		//Compare position
		return this.pos - o.pos;
	}
	
	@Override
	public int compareTo(SNP o) 
	{
		if (o == null) return 1;
		if (o == this) return 0;
		
		if(sortorder == null) sortorder = SNPSortOrder.POSITION;
		
		switch(sortorder)
		{
		case ID_ALPHA: 
			if (this.id == null)
			{
				if (o.id == null) return comparePosition(o);
				else return -1;
			}
			else 
			{
				int scomp = this.id.compareTo(o.id);
				if (scomp != 0) return scomp;
				else return comparePosition(o);
			}
		case POSITION: return comparePosition(o);
		}
		
		return 0;
	}
	
}
