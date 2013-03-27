package jMinder;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.StringItem;

public class jMinder extends MIDlet implements CommandListener {

	int MS_IN_SECOND =1000;
	int MS_IN_MINUTE =1000*60;
	int MS_IN_HOUR   =1000*60*60;

	Display display = Display.getDisplay(this);

	Form frmMain = new Form("jMinder");

	Canvas cnvLigher = new Canvas() {
		protected void paint(Graphics g) {
			g.setColor(0xFFFFFF);
			g.fillRect(0,0,g.getClipWidth(),g.getClipHeight());
		}
	};
	
	Command cmdOK 		= new Command("OK", 		Command.OK, 0);
	Command cmdCancel 	= new Command("Cancel", 	Command.CANCEL, 0);
	Command cmdNew 		= new Command("New", 		Command.CANCEL, 0);
	Command cmdEdit 	= new Command("Edit", 		Command.OK, 0);
	Command cmdDelete 	= new Command("Delete", 	Command.OK, 0);
	Command cmdTasks 	= new Command("Tasks", 		Command.OK, 1);
	Command cmdLighter 	= new Command("Lighter",	Command.OK, 2);
	Command cmdExit 	= new Command("Exit", 		Command.OK, 9);

	protected void destroyApp(boolean arg0) { notifyDestroyed(); }
	protected void pauseApp() 				{ }

	Vector Tasks = new Vector();
	private void Tasks_reSort() {
		Task A,B;
		if (Tasks.size()>1) {
			for (int i=0;i<Tasks.size()-1;i++) {
				A = (Task) Tasks.elementAt(i  );
				B = (Task) Tasks.elementAt(i+1);
				if (A.DeadLine > B.DeadLine) {
					Tasks.setElementAt(B,i  );
					Tasks.setElementAt(A,i+1);
				}
			}
		}
	}
	
	Random random = new Random();
	int RandRange(int max) { return Math.abs(random.nextInt()) % max + 1; }

	private void updMain() {
		frmMain.deleteAll();
		frmMain.append(fldTS);
		for (int i=0;i<Tasks.size();i++) {
			Task T = (Task) Tasks.elementAt(i);
			frmMain.append(new StringItem(T.DeadLine+"h", T.Title+"\n"));
		}
	}
	
	private String i2s2(int x) {
		if (x>=10) 
			return ""+x;
		else
			return "0"+x;
	}
	
	Calendar calendar = Calendar.getInstance();
	String[] WEEKDAYS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	
	StringItem fldTS = new StringItem("TS", "date&time\n");
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
	
	Timer timer = new Timer();
	TimerTask timer1s = new TimerTask() { public void run() {
		Tasks_reSort();
		updTS(); updMain();
		}};
	TimerTask timer1h = new TimerTask() { public void run() { 
		for (int i=0;i<Tasks.size();i++) { ((Task) Tasks.elementAt(i)).tick(); }
		}};
		

	protected void startApp() {
		// init Tasks
		Tasks.addElement(new Task("jMinder",RandRange(11)));
		Tasks.addElement(new Task("ARMatura",RandRange(11)));
		Tasks.addElement(new Task("GrowerBox",RandRange(11)));
		Tasks.addElement(new Task("Home hydro",RandRange(11)));
		// canvas
		cnvLigher.addCommand(cmdOK);
		cnvLigher.setCommandListener(this);
		// main/commands
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdLighter);
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdTasks);
		frmMain.setCommandListener(this);
		// shed timers
		timer.schedule(timer1s, MS_IN_SECOND, MS_IN_MINUTE);
		timer.schedule(timer1h, MS_IN_HOUR,   MS_IN_HOUR);
		// display
		display.setCurrent(frmMain);
	}

	public void commandAction(Command c, Displayable d) {
		if (d==cnvLigher) 	display.setCurrent(frmMain);
		if (c==cmdExit) 	destroyApp(true);
		if (c==cmdLighter) 	display.setCurrent(cnvLigher);
	}
}

/*


	public void destroyApp(boolean unconditional) { notifyDestroyed(); }

	public void pauseApp() { }
	
	
	Form frmNew = new Form("new");
	TextField fldTitle = new TextField("Title", "", 1024, TextField.ANY);
	TextField fldDeadLine = new TextField("DeadLine [hours]", "1", 2, TextField.DECIMAL);

	Form frmEdit = new Form("Edit");

	List lstTasks = new List("Tasks",List.EXCLUSIVE);
	
	private void updTasks() {
		lstTasks.deleteAll();
		for (int i=0;i<Tasks.size();i++) {
			Task T = (Task) Tasks.elementAt(i);
			lstTasks.append(T.getTitle(),null);
		}
	}
	
	public void startApp() {
		// main
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdTasks);
		frmMain.setCommandListener(this);
		updMain();
		// new
		frmNew.append(fldTitle);
		frmNew.append(fldDeadLine);
		frmNew.addCommand(cmdNew);
		frmNew.setCommandListener(this);
		// edit
		frmEdit.append(fldTitle);
		frmEdit.append(fldDeadLine);
		frmEdit.addCommand(cmdOK);
		frmEdit.addCommand(cmdCancel);
		frmEdit.setCommandListener(this);
		// tasks
		lstTasks.addCommand(cmdOK);
		lstTasks.addCommand(cmdNew);
		lstTasks.addCommand(cmdDelete);
		lstTasks.addCommand(cmdEdit);
		lstTasks.setCommandListener(this);
		updTasks();
		// ligher
		cnvLigher.addCommand(cmdOK);
		cnvLigher.setCommandListener(this);
		frmMain.addCommand(cmdLighter);
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
			if (c==cmdNew) {
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
			if (c==cmdOK) display.setCurrent(frmMain);
			if (c==cmdNew) display.setCurrent(frmNew);
			if (c==cmdEdit) display.setCurrent(frmEdit);
			if (c==cmdDelete) { 
				Tasks.removeElementAt(lstTasks.getSelectedIndex());
				updTasks(); updMain();
			}
		}
		if (d==cnvLigher) { display.setCurrent(frmMain); }
	}

*/
