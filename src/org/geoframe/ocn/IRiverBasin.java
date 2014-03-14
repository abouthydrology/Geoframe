package org.geoframe.ocn;

public interface IRiverBasin {

//	public static int noValue = 9;
//	public  ReadEsriASCIIHeader esriHeader=null;
//	public int rows=0,cols=0;
	
	public static final int[][] neighbors = { { 0, 0 }, { 0, 1 }, { -1, 1 },
			{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
	public static final int[][] flowCodes = { { 4, 3, 2 }, { 5, 0, 1 },
			{ 6, 7, 8 } };

	//	private static final char EOF = 0;

	/**
	 * print is a -non static- method to print a river basin structure to the console video.
	 * 
	 */
	public abstract void print();

	/**
	 * Takes a river network and writes it to disk as a ESRI ASCII file: 
	 * http://resources.esri.com/help/9.3/arcgisengine/java/GP_ToolRef/spatial_analyst_tools/esri_ascii_raster_format.htm 
	 * 
	 */
	public abstract void asciiRasterWriter();
	
//	/**
//	 * asciiRasterReader reads a File in ESRI ASCII format
//	 * @throws EsriAsciiHeaderException 
//	 * @throws EofException 
//	 * 
//	 */
	
//	public Flow asciiRasterReader() throws EsriAsciiHeaderException, EofException;
	

}