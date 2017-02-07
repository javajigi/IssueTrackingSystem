package infinitefire.project.domain;

public enum IssueState {	
	OPEN("Checked"),
	CLOSE("");
	
	private String stateCheck;
	
	private IssueState() {
		
	}
	
	private IssueState(String stateCheck) {
		this.stateCheck = stateCheck;
	}
	
	public String getStateCheck() {
		return stateCheck;
	}
}
