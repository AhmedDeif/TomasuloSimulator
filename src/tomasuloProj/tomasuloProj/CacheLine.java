package tomasuloProj;

public class CacheLine {

	boolean ValidBit;
	String Tag;
	String[] Data;
	
	public CacheLine(String[] Data, String Tag)
	{
		this.Tag = Tag;
		this.Data = Data;
		this.ValidBit = true;
	}
}
