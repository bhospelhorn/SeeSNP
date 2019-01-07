package hospelhornbg_snps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SNPGeno {
	
	//private SNP iSNP; //The SNP info
	
	private List<Allele> parent1_alleles;
	private List<Allele> parent2_alleles;
	private List<Allele> unphased_alleles;
	
	private boolean pc_err_p1;
	private boolean pc_err_p2;
	private boolean ppc_err;
	private boolean phaseFlag;
	
	private boolean upid_flag;
	private boolean uphd_flag;
	
	public SNPGeno()
	{
		//All lists start out null
		pc_err_p1 = false;
		pc_err_p2 = false;
		ppc_err = false;
		phaseFlag = false;
	}
	
	public int countUnphasedAlleles()
	{
		if (unphased_alleles == null) return 0;
		return unphased_alleles.size();
	}
	
	public int countParent1Alleles()
	{
		if (parent1_alleles == null) return 0;
		return parent1_alleles.size();
	}

	public int countParent2Alleles()
	{
		if (parent2_alleles == null) return 0;
		return parent2_alleles.size();
	}

	public int getAlleleCount()
	{
		return countUnphasedAlleles() + countParent1Alleles() + countParent2Alleles();
	}
	
	public int getCopyNumber()
	{
		int cn = 0;
		if(unphased_alleles != null){
			for(Allele a : unphased_alleles) cn += a.getCopyNumberValue();
		}
		if(parent1_alleles != null){
			for(Allele a : parent1_alleles) cn += a.getCopyNumberValue();
		}
		if(parent2_alleles != null){
			for(Allele a : parent2_alleles) cn += a.getCopyNumberValue();
		}
		return cn;
	}
	
	public boolean hasDuplicationAllele()
	{
		if(unphased_alleles != null){
			for(Allele a : unphased_alleles)
			{
				if (a.getCall() == AlleleType.SMALL_DUP) return true;
			}
		}
		if(parent1_alleles != null){
			for(Allele a : parent1_alleles){
				if (a.getCall() == AlleleType.SMALL_DUP) return true;
			}
		}
		if(parent2_alleles != null){
			for(Allele a : parent2_alleles){
				if (a.getCall() == AlleleType.SMALL_DUP) return true;
			}
		}
		return false;
	}
	
	public boolean hasDeletionAllele()
	{
		if(unphased_alleles != null){
			for(Allele a : unphased_alleles)
			{
				if (a.getCall() == AlleleType.SMALL_DEL) return true;
			}
		}
		if(parent1_alleles != null){
			for(Allele a : parent1_alleles){
				if (a.getCall() == AlleleType.SMALL_DEL) return true;
			}
		}
		if(parent2_alleles != null){
			for(Allele a : parent2_alleles){
				if (a.getCall() == AlleleType.SMALL_DEL) return true;
			}
		}
		return false;
	}
	
	public List<Allele> getUnphasedAlleles()
	{
		int sz = 1 + this.countUnphasedAlleles();
		List<Allele> copy = new ArrayList<Allele>(sz);
		if (unphased_alleles != null) copy.addAll(unphased_alleles);
		return copy;
	}
	
	public List<Allele> getParent1Alleles()
	{
		int sz = 1 + this.countParent1Alleles();
		List<Allele> copy = new ArrayList<Allele>(sz);
		if (parent1_alleles != null) copy.addAll(parent1_alleles);
		return copy;
	}
	
	public List<Allele> getPaternalAlleles()
	{
		int sz = 1 + this.countParent2Alleles();
		List<Allele> copy = new ArrayList<Allele>(sz);
		if (parent2_alleles != null) copy.addAll(parent2_alleles);
		return copy;
	}
	
	public List<Allele> getAllAlleles()
	{
		int sz = 1 + getAlleleCount();
		List<Allele> copy = new ArrayList<Allele>(sz);
		if (unphased_alleles != null) copy.addAll(unphased_alleles);
		if (parent1_alleles != null) copy.addAll(parent1_alleles);
		if (parent2_alleles != null) copy.addAll(parent2_alleles);
		return copy;
	}
	
	public void addUnphasedAllele(Allele a)
	{
		if (unphased_alleles == null) unphased_alleles = new LinkedList<Allele>();
		unphased_alleles.add(a);
	}
	
	public void addParent1Allele(Allele a)
	{
		if (parent1_alleles == null) parent1_alleles = new LinkedList<Allele>();
		parent1_alleles.add(a);
	}
	
	public void addParent2Allele(Allele a)
	{
		if (parent2_alleles == null) parent2_alleles = new LinkedList<Allele>();
		parent2_alleles.add(a);
	}
		
	public boolean pcErrorFlagged_p1()
	{
		return this.pc_err_p1;
	}
	
	public boolean pcErrorFlagged_p2()
	{
		return this.pc_err_p2;
	}
	
	public boolean ppcErrorFlagged()
	{
		return this.ppc_err;
	}
	
	public boolean phaseFlag()
	{
		return this.phaseFlag;
	}
	
	private void generateNewDEL()
	{
		//If the individual is CN1 at this locus, generate a 
		//	SMALL_DEL allele object to mark the deletion so that
		//	diploid Mendelian logic can be applied for phasing and haplotyping
		Allele del = new Allele(AlleleType.SMALL_DEL);
		addUnphasedAllele(del);
	}

	private void ppcPhase_CN3Plus(SNPGeno p1Geno, SNPGeno p2Geno, boolean ignoreUPD)
	{
		
	}
	
	private void pcPhase_CN3Plus(SNPGeno p1Geno, boolean ignoreUPD)
	{
		
	}
	
	public void phasePPC(SNPGeno p1Geno, SNPGeno p2Geno, boolean ignoreUPD)
	{
		//null checks...
		if (p1Geno == null && p2Geno == null)
		{
			unphase();
			return;
		}
		if (p1Geno == null && p2Geno != null)
		{
			phasePC(p2Geno, ignoreUPD);
			return;
		}
		if (p2Geno == null && p1Geno != null)
		{
			phasePC(p1Geno, ignoreUPD);
			return;
		}
		
		phaseFlag = true;
		//First and foremost, check CN
		int mycn = getCopyNumber();
		//Unphase
		this.unphase();
		if (mycn > 2)
		{
			ppcPhase_CN3Plus(p1Geno, p2Geno, ignoreUPD); //Delegate to special method to try to merge dups
			return;
		}
		if (mycn < 2)
		{
			//Check for DEL allele. If not there, generate deletion.
			if (!this.hasDeletionAllele()){
				generateNewDEL();
				if (mycn < 1) generateNewDEL();
			}
		}
		
		//Diploid logic
		Map<AlleleType, List<Allele>> myup = this.mapUnphasedAlleles();
		Set<AlleleType> aset = myup.keySet();
		
		Map<AlleleType, List<Allele>> p1a = p1Geno.mapMatchingAlleles(aset);
		Map<AlleleType, List<Allele>> p2a = p2Geno.mapMatchingAlleles(aset);
		
		if (p1a.isEmpty())
		{
			ppc_err = true;
			if(p2a.isEmpty()) return; //There's nothing else to do...
			else
			{
				//If not ignoring UPD, check for possible P2 UPD
				//If ignoring UPD or there are alleles that can't be inherited, leave unphased...
				Set<AlleleType> p2set = p2a.keySet();
				int p2ct = p2set.size();
				if (p2ct == 1)
				{
					//Only one allele type is shared
					AlleleType call = null;
					for (AlleleType t : p2set) call = t;
					int ccount = myup.get(call).size();
					int pcount = p2a.get(call).size();
					
				}
				else
				{
					for (AlleleType t : p2set)
					{
						int ccount = myup.get(t).size();
						int pcount = p2a.get(t).size();
						
					}	
				}
			}
		}
		else
		{
			
		}
 	}
	
	public void phasePC(SNPGeno p1Geno, boolean ignoreUPD)
	{
		phaseFlag = true;
	}
	
	public void unphase()
	{
		//Reset phasing
		phaseFlag = false;
		pc_err_p1 = false;
		pc_err_p2 = false;
		ppc_err = false;
		//Move all phased alleles back to unphased...
		if (unphased_alleles == null) unphased_alleles = new LinkedList<Allele>();
		if (parent1_alleles != null) unphased_alleles.addAll(parent1_alleles);
		if (parent2_alleles != null) unphased_alleles.addAll(parent2_alleles);
		if (unphased_alleles.isEmpty()) unphased_alleles = null;
	}
	
	public List<Allele> getMatchingAlleles(Allele a)
	{
		List<Allele> list = null;
		
		if(unphased_alleles != null)
		{
			for(Allele l : unphased_alleles)
			{
				if (l == a || l.getCall() == a.getCall())
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		if(parent1_alleles != null){
			for(Allele l : parent1_alleles)
			{
				if (l == a || l.getCall() == a.getCall())
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		if(parent2_alleles != null){
			for(Allele l : parent2_alleles)
			{
				if (l == a || l.getCall() == a.getCall())
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		
		return list;
	}
	
	public List<Allele> getMatchingAlleles(AlleleType call)
	{
		List<Allele> list = null;
		
		if(unphased_alleles != null)
		{
			for(Allele l : unphased_alleles)
			{
				if (l.getCall() == call)
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		if(parent1_alleles != null){
			for(Allele l : parent1_alleles)
			{
				if (l.getCall() == call)
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		if(parent2_alleles != null){
			for(Allele l : parent2_alleles)
			{
				if (l.getCall() == call)
				{
					if (list == null) list = new LinkedList<Allele>();
					list.add(l);
				}
			}
		}
		
		return list;
	}
	
	public Map<AlleleType, List<Allele>> mapUnphasedAlleles()
	{
		Map<AlleleType, List<Allele>> map = new HashMap<AlleleType, List<Allele>>();
		if (unphased_alleles != null)
		{
			for (Allele a : unphased_alleles)
			{
				AlleleType call = a.getCall();
				List<Allele> alist = map.get(call);
				if (alist == null)
				{
					alist = new LinkedList<Allele>();
					map.put(call, alist);
				}
				alist.add(a);
			}
		}
		
		return map;
	}

	public Map<AlleleType, List<Allele>> mapMatchingAlleles(Set<AlleleType> alleleSet)
	{
		Map<AlleleType, List<Allele>> map = new HashMap<AlleleType, List<Allele>>();
		if (alleleSet == null) return map;
		for(AlleleType t : alleleSet)
		{
			List<Allele> m = getMatchingAlleles(t);
			if (m != null && !m.isEmpty()) map.put(t, m);
		}
		
		return map;
	}
}
