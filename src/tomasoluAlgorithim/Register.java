package tomasoluAlgorithim;

public class Register {
	public RegisterName Name;
	public String Value;

	public Register(RegisterName Name, String Value) {
		this.Name = Name;
		this.Value = Value;
	}
	
	public String toString(){
		return " Name ===> " + this.Name +
				" \nValue ===> " + this.Value;
	}
}
