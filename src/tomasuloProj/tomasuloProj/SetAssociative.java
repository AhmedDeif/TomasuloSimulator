package tomasuloProj;
import java.lang.Math;

public class SetAssociative extends TheBigCache implements Cache {
	// final int MemSize = 65536;
	boolean[] DirtyBits;
	// cache is 2D array of Lines where the first Dimension is the number of
	// sets and the second dimension is the number of lines per set.
	public Set[] cache;
	public int noOfLines;
	int setSize;

	// lineSize is the size of line in bits
	public SetAssociative(int s, int l, int m) {
		super(s, l, m);
		DirtyBits = new boolean[s / l];
		noOfLines = s / l;
		this.setSize = m;
		cache = new Set[(s / l) / m];
		for (int i = 0; i < cache.length; i++) {
			for (int j = 0; j < (s / l) / m; j++) {
				cache[i].Lines[j] = new CacheLine(l);
			}
		}
	}

	void addToCache(int wordAddress) {
		// get index and place cache line at given index
		// must check for write back and dirty bit
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);

		// check if cache line is dirty and writeback
		if (this.WriteBack) {
			if (DirtyBits[index]) {
				String memAddress = cache[index].Lines[0].Tag
						+ Integer.toBinaryString(index);

				// adding zeroes to adjust for missing offset bits in extracted
				// address
				for (int i = 0; i < this.lengthOffset; i++)
					memAddress += "0";
				for (int i = 0; i < cache[index].Lines.length; i++)
					MainMemory.Insert(memAddress,
							cache[index].Lines[i].Data[BinToDec(offsetBinary)],
							this.BlockSize);
			}
		}
		String data = MainMemory.Read(wordAddress, this.BlockSize);
		CacheLine temp = new CacheLine(data, tagBinary);
		cache[index].Lines[(int) Math.random() * cache[0].Lines.length + 1] = temp;

	}

	public String[] Read(int wordAddress) {
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		CacheLine res = null;
		for (int i = 0; i < setSize; i++) {
			
			if (cache[index - 1].Lines[i].Tag == tagBinary)
				res = cache[index - 1].Lines[i];
		}
		int temp = ((int)Math.random())*cache[0].Lines.length +1;
		if(res==null)
			cache[index].Lines[temp].Data = MainMemory.Read(wordAddress,this.BlockSize);

			res = MainMemory.Read(wordAddress,this.BlockSize);
		return res;
	}

	
	//shofha ba3den
	public void update(int index, String[] data, String tag) {
		int temp = ((int) Math.random()) + 1;
		cache[index].Lines[temp].Data = data;
		if (WriteBack)// flag for writeback
			DirtyBits[temp] = true; // in case of WB
		if (WriteThrough)// flag for writethrough
			MainMemory.Read(wordAddress,this.BlockSize) = cache[index].Lines[temp];
	}

	public void WriteBack() {
		for (int i = 0; i < DirtyBits.length; i++) {
			if (DirtyBits[i]) {
				Memory[data] = cache[((int) i / setSize)].Lines[i % setSize];
				DirtyBits[i] = false;
			}
		}
	}

	public int BinToDec(String bin) {
		int i = 0;
		int result = 0;
		while (bin != "") {
			result += ((int) (bin.charAt(bin.length() - 1))) * Math.pow(2, i++);
			bin = bin.substring(0, bin.length());
		}
		return result;
	}
}

