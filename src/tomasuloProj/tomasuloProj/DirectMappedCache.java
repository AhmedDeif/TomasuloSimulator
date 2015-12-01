package tomasuloProj;

import tomasuloProj.MainMemory;

public class DirectMappedCache extends TheBigCache implements Cache{
	
	// array of data type cache lines, representing individal lines in the cache
	CacheLine[] lines;
	// to be used for write back
	boolean[] DirtyBit;
	
	
	public DirectMappedCache(int s, int l ,int m)
	{
		// since direct mapped needs assosciativity to be equal to 1
		super(s,l,1);
		this.lengthIndex = (int) (Math.log(s/l)/Math.log(2));
		// this assumes that we are always word addressable
		this.lengthOffset = (int) (Math.log(l)/Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new CacheLine[s/l];
		this.DirtyBit = new boolean[s/l];
		hier.add(this);
	}

	// called only when we know that certain line needs to be pushed to cache
	 void addToCache(int wordAddress)
	{
		// get index and place cache line at given index
		// must check for write back and dirty bit
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		// check if cache line is dirty and writeback
		if(this.WriteBack)
		{
			if(DirtyBit[index])
			{
				String memAddress = lines[index].Tag + Integer.toBinaryString(index);
				
				// adding zeroes to adjust for missing offset bits in extracted address
				for(int i=0; i<this.lengthOffset; i++)
					memAddress+="0";
				MainMemory.Insert(memAddress, lines[index].Data, this.BlockSize);
			}
		}
		// the word address passed should be shifted to be the address
		// of the start of the block.
		int blockOffset = Integer.parseInt(offsetBinary,2);
		String[] data = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
		CacheLine temp = new CacheLine(data, tagBinary);
		this.lines[index] = temp;
		 
	}

	@Override
	public String Read(int wordAddress) 
	{
		
		
		// converting wordaddress to binary
		// extract index, tag,offset
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		// get cache line
		// if not valid then its an empty cacheLine
		// if valid but tags are not equal then must fetch from memory and replace cache line
		// before replacing in memory must check if write back then 
		
		if(lines[index].ValidBit)
		{
			if(lines[index].Tag.equals(tagBinary))
				return lines[index].Data[Integer.parseInt(offsetBinary,2)];
			else
			{
				// check in lower level
				
				//this.hier.get(currentCachePosition).;
				
				// check if the data in this location was modified
				// if modified then must write to memory in case of writeBack
				// if modified but writeThrough then i only need to replace cache line
				if(this.WriteBack)
				{
					
					// check levels el ta7t fi el caches awy and remove the instruction from the lower levels
					
					// copy data to memory location if dirty bit set
					// fetch new data from memory and put in cache
					if(DirtyBit[index])
					{
						// address in memory is extracted by concatenating tag..index..offset
						// 
						String memAddress = lines[index].Tag + Integer.toBinaryString(index);
			
						// adding zeroes to adjust for missing offset bits in extracted address
						// this creates the address of the start of the block
						// *********************************************************
						// *********************************************************
						// *********************************************************
						// I need to write the changed data in the lower levels of the cache
						for(int i=0; i<this.lengthOffset; i++)
							memAddress+="0";
						MainMemory.Insert(memAddress, lines[index].Data, this.BlockSize);
						
						
						CacheLine temp = new CacheLine(MainMemory.Read(wordAddress,this.BlockSize), tagBinary);
						lines[index] = temp;
						
					}
					
					else
					{
						// no need to copy data to memory since not modified
						String[] data = MainMemory.Read(wordAddress,this.BlockSize);
						CacheLine temp = new CacheLine(data, tagBinary);
						lines[index] = temp;
						
					}
					
				}
				
				else
				{
					// then its write through so cache and main memory are consistant
					String data = MainMemory.Read(wordAddress,this.BlockSize);
					CacheLine temp = new CacheLine(data, tagBinary);
					lines[index] = temp;
				}
				
			}
		}
		
		// fetching data from main memory and inserting into cache
		
		else
		{
			String data = MainMemory.Read(wordAddress,this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			lines[index] = temp;
		}
		
		return null;
	}

	
	public void Write(int wordAddress, String data) 
	{
		if(this.WriteBack)
			WriteBack(wordAddress,data);
		else
			WriteThrough(wordAddress,data);
		
	}
	
	public void WriteBack(int wordAddress, String data)
	{
		
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);

		CacheLine temp =lines[index];
		
		// Write hit
		if(temp.Tag.equals(tagBinary) && temp.ValidBit)
		{
			temp.Data = data;
			DirtyBit[index] = true;
			temp.ValidBit = true;
			lines[index] = temp;
		}
		
		// Write miss
		else
		{
			// place old data in memory
			if(DirtyBit[index])
			{
				String DataToMemory = lines[index].Data;
				
				// starting block address in memory without offset
				
				String address = lines[index].Tag + Integer.toBinaryString(index);
				
				// adding zeroes to adjust for missing offset bits in extracted address
				for(int i=0; i<this.lengthOffset; i++)
					address+="0";
				MainMemory.Insert(address, DataToMemory, this.BlockSize);
				
			}
			// placing new data in cache
			temp.Data = data;
			temp.Tag = tagBinary;
			lines[index] = temp;
		}

	}
	
	public void WriteThrough(int wordAddress, String data)
	{
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		// inserting new data into cache
		CacheLine temp = new CacheLine(data, tagBinary);
		lines[index] = temp;
		
		// inserting into main memory
		MainMemory.Insert(word, data, this.BlockSize);
	}

}
