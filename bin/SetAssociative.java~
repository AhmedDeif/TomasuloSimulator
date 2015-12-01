import java.lang.Math;

enum Write{
	WriteBack,WriteThrough
}

public class SetAssociative {
	//final int MemSize = 65536;		
	boolean [] dirtyBits;
	// cache is 2D array of Lines where the first Dimension is the number of
	// sets and the second dimension is the number of lines per set.
	public Set[] cache;
	public int noOfLines;
	int setSize;
	boolean WB = false;
	boolean WT = false;
	

	// lineSize is the size of line in bits
	public SetAssociative(int setSize, int lineSize, int cacheSize, Write write) {
		if(write == Write.WriteBack)
			WB = true;
		else WT = true;
		
		noOfLines = cacheSize / (setSize * lineSize);
		dirtyBits = new boolean[cacheSize];
		this.setSize = setSize;
		cache = new Set[noOfLines];
		for (int i = 0; i < cache.length; i++) {
			for (int j = 0; j < setSize; j++) {
				cache[i].Lines[j] = new Line(lineSize);
			}
		}
	}

	public Line Read(String data) {
		String tag = data.getTag();
		int index = BinToDec(data.getIndex());
		Line res = null;
		for (int i = 0; i < setSize; i++) {
			
			if (cache[index - 1].Lines[i].tag == tag)
				res = cache[index - 1].Lines[i];
		}
		if(res==null)
			cache[index].Lines[temp] = Memory[data];

			res = Memory[data];
		return res;
	}

	public void update(int index, String [] data, String tag){
		int temp = ((int)Math.random())+1;
		cache[index].Lines[temp].Data=data;
		if(WB)// flag for writeback
			dirtyBits[temp] = true; // in case of WB
		if(WT)// flag for writethrough
			Memory[data] = cache[index].Lines[temp];
	}
	
	public void WriteBack() {
		for (int i = 0; i < dirtyBits.length; i++) {
			if(dirtyBits[i]){
				Memory[data] = cache[((int)i/setSize)].Lines[i%setSize];
				dirtyBits[i] = false;
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
