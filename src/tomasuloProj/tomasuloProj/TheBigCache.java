package tomasuloProj;

import java.util.ArrayList;
import java.util.LinkedList;

import tomasuloProj.MainMemory;

public abstract class TheBigCache implements Cache{
	int Size, BlockSize, assosciativity, lengthIndex, lengthOffset, lengthTag;
	boolean WriteBack, WriteThrough;
	
	static LinkedList<TheBigCache> hier;
	static int currentCachePosition = 0;
	
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		this.BlockSize = BlockSize;
		this.assosciativity = m;
	}

	public String Read(int wordAddress){
		for (int i = 0; i < hier.size(); i++) 
		{
			if(hier.get(i).Read(wordAddress)!= null)
				return hier.get(i).Read(wordAddress);
		}
		// this address is in main memory so we need to fetch it from there
		// the block size is different for each cache so we need to pass block size
		// and call this recursively till we are at the top level.
		addToCache(wordAddress);
		
		return MainMemory.ReadTemp(Integer.toBinaryString(wordAddress));
	}

	@Override
	// this should take only the byte of data that needs to be changed
	// because the size of blocks is different from level to level.
	public void Write(int wordAddress, String data) {
		// TODO Auto-generated method stub
		
	}
	
	void addToCache(int wordAddress)
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}
	
}
