package jMinder;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public class jMinder extends MIDlet implements CommandListener {

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

	Form frmEdit = new Form("Edit");

	Vector Tasks = new Vector();
	
	List lstTasks = new List("Tasks",List.EXCLUSIVE);
	Command cmdTasks = new Command("Tasks", Command.OK, 1);
	
	StringItem fldTS = new StringItem("TS", "date&time\n");
	Calendar calendar = Calendar.getInstance();
	String[] WEEKDAYS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	
	Timer timer = new Timer();
	TimerTask timer1s = new TimerTask() { public void run() { updTS(); updMain(); }};
	TimerTask timer1h = new TimerTask() { public void run() { 
		for (int i=0;i<Tasks.size();i++) { ((Task) Tasks.elementAt(i)).tick(); }}
	};
	
	Canvas cnvLigher = new Canvas() {
		protected void paint(Graphics g) {
			g.setColor(0xFFFFFF);
			g.fillRect(0,0,g.getClipWidth(),g.getClipHeight());
		}
	};
	Command cmdLighter = new Command("Lighter", Command.OK, 0);
	
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
	int MS_IN_HOUR   =1000;//*60*60;

	Random random = new Random();
	int RandRange(int max) { return Math.abs(random.nextInt()) % max + 1; }

	public void startApp() {
		// init Tasks
		Tasks.addElement(new Task("jMinder",RandRange(11)));
		Tasks.addElement(new Task("ARMatura",RandRange(11)));
		Tasks.addElement(new Task("GrowerBox",RandRange(11)));
		// main
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdTasks);
		frmMain.setCommandListener(this);
		updMain();
		// new
		frmNew.append(fldTitle);
		frmNew.append(fldDeadLine);
		frmNew.addCommand(cmdNewOK);
		frmNew.setCommandListener(this);
		// edit
		frmEdit.addCommand(cmdOK);
		frmEdit.addCommand(cmdCancel);
		frmEdit.setCommandListener(this);
		// tasks
		lstTasks.addCommand(cmdOKex);
		lstTasks.addCommand(cmdNew);
		lstTasks.addCommand(cmdDelete);
		lstTasks.addCommand(cmdEdit);
		lstTasks.setCommandListener(this);
		updTasks();
		// ligher
		cnvLigher.addCommand(cmdOK);
		cnvLigher.setCommandListener(this);
		frmMain.addCommand(cmdLighter);
		// shed timers
		timer.schedule(timer1s, MS_IN_SECOND, MS_IN_SECOND);
		timer.schedule(timer1h, MS_IN_HOUR, MS_IN_HOUR);
		// show main form
		display.setCurrent(frmMain);
	}

	public void commandAction(Command c, Displayable d) {
		if (d==frmMain) {
			if (c==cmdExit) destroyApp(false);
			if (c==cmdNew) display.setCurrent(frmNew);
			if (c==cmdTasks) display.setCurrent(lstTasks);
			if (c==cmdLighter) display.setCurrent(cnvLigher);
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
		if (d==frmEdit) {
			updTasks(); updMain();
			display.setCurrent(lstTasks);
		}
		if (d==lstTasks) {
			if (c==cmdOKex) display.setCurrent(frmMain);
			if (c==cmdNew) display.setCurrent(frmNew);
			if (c==cmdEdit) display.setCurrent(frmEdit);
			if (c==cmdDelete) { 
				Tasks.removeElementAt(lstTasks.getSelectedIndex());
				updTasks(); updMain();
			}
		}
		if (d==cnvLigher) { display.setCurrent(frmMain); }
	}

}
