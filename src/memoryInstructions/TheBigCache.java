package memoryInstructions;

import java.util.ArrayList;
import java.util.LinkedList;

import memory.*;

public class TheBigCache implements Cache{
	int Size, BlockSize, assosciativity, lengthIndex,
	lengthOffset, lengthTag; 
	double accessTime, numberOfMisses, numberOfAccesses;
	boolean WriteBack, WriteThrough;
	
	static int TotalCycles = 0;
	static LinkedList<TheBigCache> hier = new LinkedList<TheBigCache>();
	static int currentCachePosition = 0;

	
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		// assuming we are given number of words not bits
		this.BlockSize = L;
		this.assosciativity = m;
	}

	
	public void writeBlock(int wordAddress, String[] data)
	{
		
	}
	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsException{
		// Loop through all levels of cache till we find the word is found
		for (int i = 0; i < hier.size(); i++) 
		{			 
			if(hier.get(i).Read(wordAddress)!= null)
			{
				// adding required word address to levels of cache that didnot 
				// contain the word.
				// when I add the required address to the cache I need to check
				// before removing blocks if they are write back, then I need to 
				// copy the data contained in them to the lower levels till I hit 
				// a write through cache
				
				for(int k=0; k<i; k++){
					if(hier.get(k).WriteBack){
						// put the data in the removed cache to lower levels
						// I will keep in going to lower levels until I reach
						// a write through level
						boolean stop = false;
						for(int j=k; j<i && !stop;j++)
						{
							
						}
					}
					hier.get(k).addToCache(wordAddress);
				}
				return hier.get(i).Read(wordAddress);
			}
				
		}		
		// this address is in main memory so we need to fetch it from there
		// the block size is different for each cache so we need to pass block size
		// and call this recursively till we are at the top level.
		addToCache(wordAddress);
		String address = Integer.toBinaryString(wordAddress);
		while(address.length() < 16)
			address = "0" + address;
		return MainMemory.ReadTemp(address);
	}

	@Override
	// this should take only the byte of data that needs to be changed
	// because the size of blocks is different from level to level.
	
	
	public boolean Write(int wordAddress, String data) throws IndexOutOfMemoryBoundsException {
		
		for (int i = 0; i < hier.size(); i++) {
			if(hier.get(i).Write(wordAddress, data))
			{
				// add data to lower levels to ensure consistency
				// add updated blocks to upper levels that did not contain the block
				// if this level is writeBack stop
				// if write through
				boolean stop = false;
				for(int k =i+1; k < hier.size() && !stop; k++){
					if(hier.get(k).WriteBack)
						stop = true;
					hier.get(k).Write(wordAddress, data);
				}
				// copy data to upper levels that didnot contain the data
				for(int j = i-1; j >= 0; j--)
					hier.get(j).addToCache(wordAddress, data);
					
				//if(!hier.get(i).WriteBack)
				//{
					//MainMemory.RAM.put(Integer.toBinaryString(wordAddress), data);
				//}
				// need to loop over all levels copying the new data
				
				
				return true;
			}
		}
		
		// #jolly
		// must add to cache and then write 
		// do i add it to cache then write or write mn el awel
		
		//add to all levels altering the data i want
		// I need use add to cache but i have to make sure the data i add is the new 
		addToCache(wordAddress,data);
		return false;
		
	}
	

	void addToCache(int wordAddress,String data) throws IndexOutOfMemoryBoundsException
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress,data);
	}
	
	void addToCache(int wordAddress) throws IndexOutOfMemoryBoundsException
	{
		// I need to check when I remove data if this is a write back then
		// 
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}
	
	double getHitRatio()
	{
		return (numberOfAccesses - numberOfMisses)/numberOfAccesses;
	}
	
	public void WriteData(int wordAddress, String[] data){
		
	}
	
	public double getAverageMemoryAccessTime() {
		// loop over caches applying the formula
		// AMAT = hitTime + (missRate * AverageMissPenalty[n])
		// AverageMissPenalty[n] = hitTime[n] + (missRate[n] *
		// AverageMissPenalty[n+1])

		double amat = 0;
		double penalty = hier.get(hier.size() - 1).accessTime
				+ ((hier.get(hier.size() - 1).numberOfMisses / hier.get(hier
						.size() - 1).numberOfAccesses) * MainMemory.accessTime);
		for (int i = hier.size() - 2; i > 0; i--) {

			penalty = hier.get(i).accessTime
					+ ((hier.get(i).numberOfMisses / hier.get(i).numberOfAccesses) * penalty);
		}
		amat = hier.get(0).accessTime
				+ ((hier.get(0).numberOfMisses / hier.get(0).numberOfAccesses) * penalty);

		return amat;
	}
	
	
}
