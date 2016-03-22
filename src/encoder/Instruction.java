package encoder;

/**
 * Instruction class
 * 
 * @version 2016-march-22
 * @course Compiladores
 * @author Pedro H Q Santos
 * @email phqs@ecomp.poli.br
 */
public class Instruction {
	private String instruction;
	
	public Instruction(String instruction) {
		this.instruction = instruction;
	}
	
	public String toString() {
		return instruction + "\n";
	}
}
