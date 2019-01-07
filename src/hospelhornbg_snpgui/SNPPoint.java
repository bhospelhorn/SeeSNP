package hospelhornbg_snpgui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import hospelhornbg_snps.SNP;

public class SNPPoint {
	
	public static final int DEFAULT_RADIUS_NORMAL = 3;
	public static final int DEFAULT_RADIUS_SELECT = 6;
	
	private SNP iSNP;
	private PointShape iShape;
	
	private int iRed;
	private int iGreen;
	private int iBlue;
	
	private int x; //relative to panel
	private int y; //relative to panel
	
	private int radius_normal;
	private int radius_selected;
	
	private boolean bSelected;
	
	private Shape renderedUnselected;
	private Shape renderedSelected;
	
	public SNPPoint(SNP snp, PointShape shape)
	{
		iSNP = snp;
		iShape = shape;
		iRed = 0;
		iGreen = 0;
		iBlue = 0;
		x = -1;
		y = -1;
		radius_normal = DEFAULT_RADIUS_NORMAL;
		radius_selected = DEFAULT_RADIUS_SELECT;
		bSelected = false;
		renderedUnselected = null;
		renderedSelected = null;
	}
	
	public boolean drawable()
	{
		if (iSNP == null) return false;
		if (iShape == null) return false;
		return ((x >= 0) && (y >= 0)) && radius_normal >= 1;
	}

	public SNP getSNP()
	{
		return iSNP;
	}
	
	public PointShape getShape()
	{
		return iShape;
	}
	
	public int getColor_red()
	{
		return iRed;
	}
	
	public int getColor_green()
	{
		return iGreen;
	}
	
	public int getColor_blue()
	{
		return iBlue;
	}
	
	public Color getColor()
	{
		return new Color(iRed, iGreen, iBlue);
	}
	
	public int getRelativeX()
	{
		return x;
	}
	
	public int getRelativeY()
	{
		return y;
	}
	
	public int getNormalRadius()
	{
		return radius_normal;
	}
	
	public int getSelectedRadius()
	{
		return radius_selected;
	}
	
	public boolean isSelected()
	{
		return bSelected;
	}
	
	private void clearPrerenders()
	{
		renderedUnselected = null;
		renderedSelected = null;
	}
	
	public void setShape(PointShape shape)
	{
		iShape = shape;
		clearPrerenders();
	}
	
	public void setColor(int r, int g, int b)
	{
		iRed = r;
		iGreen = g;
		iBlue = b;
	}
	
	public void setX(int X)
	{
		x = X;
		clearPrerenders();
	}
	
	public void setY(int Y)
	{
		y = Y;
		clearPrerenders();
	}
	
	public void setNormalRadius(int r)
	{
		radius_normal = r;
		clearPrerenders();
	}
	
	public void setSelectedRadius(int r)
	{
		radius_selected = r;
		clearPrerenders();
	}
	
	public void setSelected(boolean b)
	{
		bSelected = b;
	}
	
	private int recalculateCoord(int max, int minpos, int maxpos)
	{
		//Contig chrom = iSNP.getChrom();
		//long clen = chrom.getLength();
		int spos = iSNP.getPosition();
		int difft = maxpos - minpos;
		if (difft < 0) return -1;
		int diffp = spos - minpos;
		if (diffp < 0) return -1;
		int c = (int)Math.round((double)max * ((double)diffp/(double)difft));
		return c;
	}
	
	public void recalculateX(int maxX, int minpos, int maxpos)
	{
		//For horizontal plot
		x = recalculateCoord(maxX, minpos, maxpos);
		clearPrerenders();
	}
	
	public void recalculateY(int maxY, int minpos, int maxpos)
	{
		//For vertical plot
		y = recalculateCoord(maxY, minpos, maxpos);
		clearPrerenders();
	}
	
	private Shape drawAsCircle()
	{
		int r = radius_normal;
		if(bSelected) r = radius_selected;
		Ellipse2D.Double c = new Ellipse2D.Double(x-r, y-r, r << 1, r << 1);
		return c;
	}
	
	private Shape drawAsSquare()
	{
		int r = radius_normal;
		if(bSelected) r = radius_selected;
		Rectangle s = new Rectangle(x-r, y-r, r << 1, r << 1);
		return s;
	}
	
	private Shape drawAsTriangle()
	{
		int r = radius_normal;
		if(bSelected) r = radius_selected;
		int x1 = x;
		int y1 = y-r;
		int x2 = x-r;
		int y2 = y+r;
		int x3 = x+r;
		int y3 = y+r;
		int[] xpts = {x1, x2, x3};
		int[] ypts = {y1, y2, y3};
		Polygon t = new Polygon(xpts, ypts, 3);
		return t;
	}
	
	private void renderMe()
	{
		Shape s = null;
		switch(iShape)
		{
		case CIRCLE:
			s = drawAsCircle();
			break;
		case SQUARE:
			s = drawAsSquare();
			break;
		case TRIANGLE:
			s = drawAsTriangle();
			break;
		}
		if (bSelected) renderedSelected = s;
		else renderedUnselected = s;
	}
	
	public void paintMe(Graphics2D canvas)
	{
		canvas.setColor(getColor());
		//Get shape
		if(bSelected)
		{
			if (renderedSelected == null) renderMe();
			canvas.fill(renderedSelected);
		}
		else
		{
			if (renderedUnselected == null) renderMe();
			canvas.fill(renderedUnselected);
		}
	}
	
	public boolean contains(int xCoord, int yCoord)
	{
		if (!drawable()) return false;

		if(bSelected)
		{
			if (renderedSelected == null) renderMe();
			return renderedSelected.contains(xCoord, yCoord);
		}
		else
		{
			if (renderedUnselected == null) renderMe();
			return renderedUnselected.contains(xCoord, yCoord);
		}

	}
	
}
