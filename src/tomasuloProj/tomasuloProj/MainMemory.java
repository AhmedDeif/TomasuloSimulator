package tomasuloProj;

import java.util.HashMap;


public class MainMemory {
	
	static HashMap<String, String> RAM;
	
	public MainMemory()
	{
		RAM = new HashMap<String, String>();
	}
	
	// Must check that address of data is not > 64KB
	public static void Insert(String address , String[] data, int blockSize)
	{	
		int add = Integer.parseInt(address,2);
		for (int i = 0; i < data.length; i++) {
			RAM.put(Integer.toBinaryString(i+add), data[i]);
		}
	}
	
	// This will take address of a word and word itself to add
	// We need this method because each cache has a different block size
	public static void WriteWord(String address, String data)
	{
		RAM.put(address, data);
	}
	
	// In the cache, We find out the starting address of the block that 
	// we are trying to fetch so we copy starting from the given address
	public static String[] Read(int wordAddress, int blockSize)
	{
		
		
		// find where the start of the block that contains this word address
		
		
		
		///////////////////////////////////////////////////////
		String[] block = new String[blockSize];
		for (int i = 0; i < block.length; i++) {
			block[i] = RAM.get(Integer.toBinaryString(wordAddress + i));
		}
		return block;
		
		
		
		
		//////////////////////////////////////////////////////
		// must return block equivilant to block size of cache
		// fetch addresses in order till block is full
//		
//		String block = "";
//		for(int i=0; i<blockSize; i++)
//		{
//			String address = Integer.toBinaryString(wordAddress);
//			block += RAM.get(address);
//			wordAddress++;
//		}
//		
//		return block;
	
	}
	
	
	public static String ReadTemp(String wordAddress)
	{
		return RAM.get(wordAddress);
	}
	
	

}
