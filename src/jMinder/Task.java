package jMinder;

public class Task {
	public String Title;
	public int DeadLine;
	public void tick() { DeadLine--; }
	public Task(String Title, int DeadLine) {
		this.Title = Title; 
		this.DeadLine = DeadLine;
	}
}
