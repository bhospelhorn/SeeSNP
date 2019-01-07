package hospelhornbg_snpgui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import hospelhornbg_genomeBuild.Contig;
import hospelhornbg_snps.SNP;

public class AllelePanel extends JPanel{

	private static final long serialVersionUID = 816272529564277024L;
	
	private Contig chrom;
	
	private List<SNP> snps;
	
	
	public AllelePanel()
	{
		chrom = null;
		snps = null;
	}
	
	public void setChrom(Contig c)
	{
		chrom = c;
	}
	
	public void loadSNPs(Collection<SNP> snpcoll)
	{
		if (snpcoll == null){
			snps = null;
			//index = null;
			return;
		}
		int sz = snpcoll.size();
		snps = new ArrayList<SNP>(sz);
		for (SNP s : snpcoll)
		{
			if (s.getChrom() == null) continue;
			if (s.getChrom().equals(chrom)) snps.add(s);
		}
		Collections.sort(snps);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
	}
	
}
