package jMinder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

class Task {
	String Title;
	Calendar TS;
	int DeadLine;
	int Age;
	public Task(String Title) {
		this.Title = Title; 
		this.TS = Calendar.getInstance();
		this.DeadLine = 11;
		this.Age = 0;
	}
	public String getDeadLine() { return Integer.toString(DeadLine)+"s"; }
	public String getTitle() { return Title; }
	public void tick() {
		Age++;
		DeadLine--; 
		}
}

public class main extends MIDlet implements CommandListener, ItemCommandListener {

	public void destroyApp(boolean unconditional) { notifyDestroyed(); }

	public void pauseApp() { }
	
	Display display = Display.getDisplay(this);
	Form frmMain = new Form("jMinder");
	Command cmdNew = new Command("New", Command.EXIT, 0);
	Command cmdExit = new Command("Exit", Command.OK, 0);
	
	Form frmNew = new Form("new");
	Command cmdOK = new Command("OK", Command.OK, 0);
	Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);

	Task[] Tasks = {new Task("jMinder"),new Task("ARMatura"),new Task("Milling")};
	
	List lstTasks = new List("Tasks",List.EXCLUSIVE);
	Command cmdTasks = new Command("Tasks", Command.OK, 1);
	
	StringItem fldTS = new StringItem("TS", "date&time\n");//, StringItem.BUTTON);
	Calendar calendar = Calendar.getInstance();
	String[] WEEKDAYS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	
	Timer timer = new Timer();
	TimerTask timer1s = new TimerTask() { public void run() { updMain(); }};
	
	private String i2s2(int x) {
		if (x>=10) 
			return ""+x;
		else
			return "0"+x;
	}
	
	private void updTS() {
		calendar = Calendar.getInstance();
		String yy = Integer.toString(calendar.get(Calendar.YEAR));
		String mo = i2s2(calendar.get(Calendar.MONTH));
		String dd = i2s2(calendar.get(Calendar.DATE));
		String wd = WEEKDAYS[calendar.get(Calendar.DAY_OF_WEEK)-1];
		String hh = i2s2(calendar.get(Calendar.HOUR_OF_DAY));
		String mi = i2s2(calendar.get(Calendar.MINUTE));
		String ss = i2s2(calendar.get(Calendar.SECOND));
		fldTS.setLabel(wd+" "+dd+"."+mo+"."+yy);
		fldTS.setText(hh+":"+mi+":"+ss+"\n");
		frmMain.append(fldTS);
	}
	
	private void updMain() {
		frmMain.deleteAll();
		updTS();
		for (int i=0;i<Tasks.length;i++) {
			Tasks[i].tick();
			frmMain.append(new StringItem(Tasks[i].getDeadLine(), Tasks[i].getTitle()+"\n"));
		}
	}
	
	public void startApp() { // throws MIDletStateChangeException {
		// def menus
		// main
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdTasks);
		frmMain.setCommandListener(this);
		// fldTS
		fldTS.setItemCommandListener(this);
		timer.schedule(timer1s, 0, 1000); // ms
		// tasks
		updMain();
		// new
		frmNew.addCommand(cmdOK);
		frmNew.addCommand(cmdCancel);
		frmNew.setCommandListener(this);
		// tasks
		lstTasks.addCommand(cmdOK);
		lstTasks.addCommand(cmdNew);
		lstTasks.setCommandListener(this);
		// show main form
		display.setCurrent(frmMain);
	}

	public void commandAction(Command c, Displayable d) {
		if (d==frmMain) {
			if (c==cmdExit) destroyApp(false);
			if (c==cmdNew) display.setCurrent(frmNew);
			if (c==cmdTasks) display.setCurrent(lstTasks);
		}
		if (d==frmNew) {
			if (c==cmdOK) {}
			if (c==cmdCancel) {}
			display.setCurrent(frmMain);
		}
		if (d==lstTasks) {
			if (c==cmdOK) display.setCurrent(frmMain);
			if (c==cmdNew) display.setCurrent(frmNew);
		}
	}

	public void commandAction(Command c, Item item) {
		System.out.println(item.toString()+"-->"+c.toString());
		if (item==fldTS) {
			
		}
	}

}
	