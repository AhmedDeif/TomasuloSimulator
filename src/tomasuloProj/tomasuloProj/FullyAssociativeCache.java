package tomasuloProj;

import java.util.ArrayList;
import tomasuloProj.MainMemory;
import tomasuloProj.TheBigCache;


public class FullyAssociativeCache extends TheBigCache implements Cache {
	ArrayList<CacheLine> lines = new ArrayList<CacheLine>();
	int maxNoLines;
	int LineSize;
	boolean [] dirtyBits;
	
	public FullyAssociativeCache(int s, int l, int m) {
		super(s,l,3);
		this.lengthOffset = (int) (Math.log(l)/Math.log(2));
		this.lengthTag = 16  - lengthOffset;
		maxNoLines = s / l;
		this.LineSize=LineSize;
		dirtyBits = new boolean[s/l];
	}

	public void Write (int wordAddress, String[] data){
		if(this.WriteBack)
			WriteBack(wordAddress,data);
		else
			WriteThrough(wordAddress,data);
	}
	
	private void WriteThrough(int wordAddress, String[] data) {
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);

		if(lines.size()<maxNoLines){
			CacheLine temp = new CacheLine(data, tagBinary);
			lines.add(temp);
			//call recursive the big cache
		}else{
			lines.remove(0);
			CacheLine temp = new CacheLine(data, tagBinary);
			lines.add(temp);
			//call recursive big cache
			
		}
	}



	private void WriteBack(int wordAddress, String[] data) {
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		CacheLine temp = new CacheLine(data, tagBinary);
		boolean cont=false;
		
		for(int i=0;i<lines.size();i++){
			CacheLine temp3 = lines.get(i);
			if(temp.Tag.equals(temp3.Tag)){ 
				cont =true;
				int temp2=lines.indexOf(temp);
				if(dirtyBits[temp2]){
					MainMemory.Insert(temp3.Tag, temp3.Data, this.BlockSize);
					lines.add(temp2, temp);
					dirtyBits[temp2]=false;
				}else{
					lines.add(temp2, temp);
					dirtyBits[temp2]=true;
				}
			}
			
		}if(!cont){
			if(lines.size()<maxNoLines){
				lines.add(temp);
				dirtyBits[lines.size()-1]=true;
			}else{
				lines.remove(0);
				lines.add(temp);
				dirtyBits[lines.size()-1]=true;
			}
		}
	}



	@Override
	public String Read(int wordAddress) {
		// TODO Auto-generated method stub
		return null;
	}

}
