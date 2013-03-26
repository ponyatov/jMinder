package jMinder;

class Task {
	public Task(String Title, int DeadLine) {
		this.Title = Title; 
		this.DeadLine = DeadLine;
	}

	String Title;
	public String getTitle() { return Title; }
	
	int DeadLine;
	public String getDeadLine() { return Integer.toString(DeadLine)+"h"; }
	public void tick() { DeadLine--; }
}
