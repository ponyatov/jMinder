package jMinder;

public class Task {
	public String Title;
	public int DeadLine;
	public boolean Alarm;

	public void tick() {
		DeadLine--;
	}

	public Task(String Title, int DeadLine, boolean Alarm) {
		this.Title = Title;
		this.DeadLine = DeadLine;
		this.Alarm = Alarm;
	}
}
