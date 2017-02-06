package infinitefire.project.domain;

public enum LabelColor {
	TASK("#8FBE00"), 
	ENHANCEMENT("#00A0B0"),
	PROPOSAL("#EB6841"),
	BUG("#CC333F");	
	
	private String colorHex;
	
	private LabelColor() {
		
	}
	
	private LabelColor(String colorHex) {
		this.colorHex = colorHex;
	}
	
	public String getColorHex() {
		return colorHex;
	}
}
