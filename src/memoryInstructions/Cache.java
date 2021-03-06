package memoryInstructions;
import memory.*;

public interface Cache {

	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsException;
	public boolean Write(int wordAddress, String data) throws IndexOutOfMemoryBoundsException;
	
}
