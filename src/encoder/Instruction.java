package encoder;

public class Instruction {
	private String instruction;
	
	public Instruction(String instruction) {
		this.instruction = instruction;
	}
	
	public String toString() {
		return instruction + "\n";
	}
}
