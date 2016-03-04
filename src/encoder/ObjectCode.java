package encoder;

public class ObjectCode {
	private StringBuffer extern, data, text;
	
	public ObjectCode() { 
		extern = new StringBuffer();
		data = new StringBuffer();
		text = new StringBuffer();
	}
	
	public void addInstruction(String section, Instruction instruction) { 
		if (section.equals("data")) {
			data.append(instruction.toString());
		} else if (section.equals("extern")) {
			extern.append(instruction.toString());
		} else if (section.equals("text")) {
			text.append(instruction.toString());
		}
	}
	
	public void generateCode() {
		StringBuffer objectCode = new StringBuffer();
		objectCode.append(extern);
		objectCode.append("\n");
		objectCode.append("SECTION .data\n");
		objectCode.append(data);
		objectCode.append("\n");
		objectCode.append("SECTION .text\n");
		objectCode.append("global _main\n\n"); // talvez mudar para _WinMain@16
		objectCode.append(text);
		objectCode.append("\n");
		System.out.println(objectCode.toString());
	}
}
