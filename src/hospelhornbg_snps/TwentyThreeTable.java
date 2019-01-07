package hospelhornbg_snps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hospelhornbg_genomeBuild.Contig;
import hospelhornbg_genomeBuild.GenomeBuild;

//Looks like some of the SNPs have I/D for insertions and deletions. Neat.

public class TwentyThreeTable {
	
	private Map<Contig, List<SNP>> snps;
	
	public TwentyThreeTable(String filepath) throws IOException
	{
		GenomeBuild build37 = GenomeBuild.loadStandardBuild("grch37");
		if (build37 == null){
			System.err.println("TwentyThreeTable.<init> || ERROR: Build GRCh37 file not found!");
			throw new IOException();
		}
		snps = new HashMap<Contig, List<SNP>>();
		
		FileReader fr = new FileReader(filepath);
		BufferedReader br = new BufferedReader(fr);
		
		String line = null;
		while((line = br.readLine()) != null)
		{
			if(!line.startsWith("#"))
			{
				SNP snp = parseSNP(line, build37);
				if (snp != null)
				{
					Contig c = snp.getChrom();
					if (c != null)
					{
						List<SNP> slist = snps.get(c);
						if (slist == null){
							slist = new LinkedList<SNP>();
							snps.put(c, slist);
						}
						slist.add(snp);
					}
				}
			}
		}
		
		br.close();
		
		//Sort SNPs
		SNP.setSortOrder(SNPSortOrder.POSITION);
		List<Contig> allcontigs = build37.getChromosomes();
		for (Contig c : allcontigs)
		{
			List<SNP> slist = snps.get(c);
			if (slist != null) Collections.sort(slist);
		}
	}
	
	private SNP parseSNP(String line, GenomeBuild gb)
	{
		String[] fields = line.split("\t");
		if (fields == null) return null;
		if (fields.length != 4) return null;
		
		//ID
		SNP snp = new SNP(fields[0]);
		
		//Chrom
		Contig c = gb.getContig(fields[1]);
		if (c == null) return null;
		snp.setChrom(c);
		
		//Pos
		try{snp.setPosition(Integer.parseInt(fields[2]));}
		catch (NumberFormatException e){return null;}
		
		//Alleles
		snp.parseAlleles(fields[3]);
		
		return snp;
	}

}
