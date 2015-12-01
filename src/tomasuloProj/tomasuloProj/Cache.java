package tomasuloProj;

public interface Cache {

	public String Read(int wordAddress);
	public void Write(int wordAddress, String data);
	
}
