package jMinder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

class Task {
	String Title;
	Calendar TS;
	int DeadLine;
	int Age;
	public Task(String Title, int DeadLine) {
		this.Title = Title; 
		this.TS = Calendar.getInstance();
		this.DeadLine = DeadLine;
		this.Age = 0;
	}
	public String getDeadLine() { return Integer.toString(DeadLine)+"h"; }
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
	Command cmdDelete = new Command("Delete", Command.EXIT, 0);
	Command cmdEdit = new Command("Edit", Command.EXIT, 0);
	Command cmdExit = new Command("Exit", Command.OK, 0);
	
	Form frmNew = new Form("new");
	Command cmdOK = new Command("OK", Command.OK, 0);
	Command cmdOKex = new Command("OK", Command.EXIT, 0);
	Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);
	TextField fldTitle = new TextField("Title", "", 1024, TextField.ANY);
	TextField fldDeadLine = new TextField("DeadLine [hours]", "1", 2, TextField.DECIMAL);
	Command cmdNewOK = new Command("Ok", Command.CANCEL, 0);

	Vector Tasks = new Vector();
	
	List lstTasks = new List("Tasks",List.EXCLUSIVE);
	Command cmdTasks = new Command("Tasks", Command.OK, 1);
	
	StringItem fldTS = new StringItem("TS", "date&time\n");//, StringItem.BUTTON);
	Calendar calendar = Calendar.getInstance();
	String[] WEEKDAYS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	
	Timer timer = new Timer();
	TimerTask timer1s = new TimerTask() { public void run() { updTS(); updMain(); }};
	TimerTask timer1h = new TimerTask() { public void run() { 
		for (int i=0;i<Tasks.size();i++) { ((Task) Tasks.elementAt(i)).tick(); }}
	};
	
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
	}
	
	private void updMain() {
		frmMain.deleteAll();
		frmMain.append(fldTS);
		for (int i=0;i<Tasks.size();i++) {
			Task T = (Task) Tasks.elementAt(i);
			frmMain.append(new StringItem(T.getDeadLine(), T.getTitle()+"\n"));
		}
	}
	
	private void updTasks() {
		lstTasks.deleteAll();
		for (int i=0;i<Tasks.size();i++) {
			Task T = (Task) Tasks.elementAt(i);
			lstTasks.append(T.getTitle(),null);
		}
	}
	
	int MS_IN_SECOND =1000;
	int MS_IN_HOUR   =1000*60*60;

	public void startApp() { // throws MIDletStateChangeException {
		// init Tasks
		Tasks.addElement(new Task("jMinder",0));
		Tasks.addElement(new Task("ARMatura",0));
		// def menus
		// main
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdTasks);
		frmMain.setCommandListener(this);
		// fldTS
		fldTS.setItemCommandListener(this);
		timer.schedule(timer1s, MS_IN_SECOND, MS_IN_SECOND);
		timer.schedule(timer1h, MS_IN_HOUR, MS_IN_HOUR);
		// tasks
		updMain();
		// new
		frmNew.append(fldTitle);
		frmNew.append(fldDeadLine);
		frmNew.addCommand(cmdNewOK);
		//frmNew.addCommand(cmdCancel);
		frmNew.setCommandListener(this);
		// tasks
		updTasks();
		lstTasks.addCommand(cmdOKex);
		lstTasks.addCommand(cmdNew);
		lstTasks.addCommand(cmdDelete);
		lstTasks.addCommand(cmdEdit);
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
			if (c==cmdNewOK) {
				Tasks.addElement(
						new Task(
								fldTitle.getString(),
								Integer.parseInt(fldDeadLine.getString())
								)
						);
				updMain(); updTasks();
			}
			display.setCurrent(frmMain);
		}
		if (d==lstTasks) {
			if (c==cmdOKex) display.setCurrent(frmMain);
			if (c==cmdNew) display.setCurrent(frmNew);
			if (c==cmdDelete) {
				Tasks.removeElementAt(lstTasks.getSelectedIndex());
				updTasks();
			}
		}
	}

	public void commandAction(Command c, Item item) {
		System.out.println(item.toString()+"-->"+c.toString());
		if (item==fldTS) {
			
		}
	}

}
	