package jMinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;

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
	Command cmdSave 	= new Command("Save", 		Command.OK, 0);
	Command cmdLoad 	= new Command("Load", 		Command.OK, 0);
	Command cmdLighter 	= new Command("Lighter",	Command.OK, 2);
	Command cmdExit 	= new Command("Exit", 		Command.OK, 9);

	protected void destroyApp(boolean arg0) { notifyDestroyed(); }
	protected void pauseApp() 				{ }

	Vector Tasks = new Vector();
	boolean lockTasks=false; // mutex
	private void Tasks_reSort() { // multipass bubble sort
		Task A,B;
		if (Tasks.size()>1)
			for (int i=0;i<Tasks.size()-1;i++) {
				A = (Task) Tasks.elementAt(i  );
				B = (Task) Tasks.elementAt(i+1);
				if (A.DeadLine > B.DeadLine) {
					Tasks.setElementAt(B,i  );
					Tasks.setElementAt(A,i+1);
				}
			}
	}
	
	Random random = new Random();
	int RandRange(int max) { return Math.abs(random.nextInt()) % max + 1; }
	
	String i2sN(int x,int N) {
		String S = Integer.toString(x);
		int L = S.length();
		String F; if (N<0) F="0"; else F="_";
		for (int i=0;i<Math.abs(N)-L;i++) S=F+S;
		return S;
	}

	private void updMain() {
		frmMain.deleteAll();
		frmMain.append(fldTS);
		Task T;
		for (int i=0;i<Tasks.size();i++) {
			T = (Task) Tasks.elementAt(i);
			frmMain.append("["+i2sN(T.DeadLine,4)+"h] "+T.Title+"\n");
		}
	}

	Calendar calendar = Calendar.getInstance();
	String[] WEEKDAYS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	
	String fldTS = new String("date&time\n");
	private void updTS() {
		calendar = Calendar.getInstance();
		String yy = Integer.toString(calendar.get(Calendar.YEAR));
		String mo = i2sN(calendar.get(Calendar.MONTH),-2);
		String dd = i2sN(calendar.get(Calendar.DATE),-2);
		String wd = WEEKDAYS[calendar.get(Calendar.DAY_OF_WEEK)-1];
		String hh = i2sN(calendar.get(Calendar.HOUR_OF_DAY),-2);
		String mi = i2sN(calendar.get(Calendar.MINUTE),-2);
		String ss = i2sN(calendar.get(Calendar.SECOND),-2);
		fldTS = wd+" "+dd+"."+mo+"."+yy+" "+hh+":"+mi+":"+ss+"\n";
	}
	
	Timer timer = new Timer();
	TimerTask timer1s = new TimerTask() { public void run() {
		if (!lockTasks) 
			Tasks_reSort();
		updTS(); updMain();
		}};
	TimerTask timer1h = new TimerTask() { public void run() {
		if (!lockTasks) 
			for (int i=0;i<Tasks.size();i++) 
				((Task) Tasks.elementAt(i)).tick();
		}};
  		
	List frmSelect = new List("Select", List.EXCLUSIVE);
	Command frmSelect_caller=null;
	private void updSelect(String Title) {
		frmSelect.setTitle(Title);
		frmSelect.deleteAll();
		Task T;
		for (int i=0;i<Tasks.size();i++) {
			T = (Task) Tasks.elementAt(i);
			frmSelect.append(T.Title, null);
		}
	}
	
	Form frmEdit = new Form("edit");
	Command frmEdit_caller=null;
	Task frmEdit_item=null;
	TextField fldTitle = new TextField("Title", "", 64, TextField.ANY);
	TextField fldDeadLine = new TextField("DeadLine [hours]", "1", 3, TextField.DECIMAL);
	private void updEdit(String Title,Task T) {
		frmEdit.setTitle(Title);
		frmEdit_item=T;
		fldTitle.setString(T.Title);
		fldDeadLine.setString(Integer.toString(T.DeadLine));
	}
	
	protected void startApp() {
		/*
		// init Tasks
		Tasks.addElement(new Task("jMinder",RandRange(11)));
		Tasks.addElement(new Task("ARMatura",RandRange(11)));
		Tasks.addElement(new Task("GrowerBox",RandRange(11)));
		*/
		// canvas
		cnvLigher.addCommand(cmdOK);
		cnvLigher.setCommandListener(this);
		// edit
		frmEdit.append(fldTitle);
		frmEdit.append(fldDeadLine);
		frmEdit.addCommand(cmdOK);
		frmEdit.setCommandListener(this);
		// select
		frmSelect.addCommand(cmdOK);
		frmSelect.addCommand(cmdCancel);
		frmSelect.setCommandListener(this);
		// main/commands
		frmMain.addCommand(cmdExit);
		frmMain.addCommand(cmdLighter);
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdEdit);
		frmMain.addCommand(cmdDelete);
		frmMain.addCommand(cmdSave);
		frmMain.addCommand(cmdLoad);
		frmMain.setCommandListener(this);
		// shed timers
		timer.schedule(timer1s, MS_IN_SECOND, MS_IN_MINUTE);
		timer.schedule(timer1h, MS_IN_HOUR,   MS_IN_HOUR);
		// display
		display.setCurrent(frmMain);
	}
	
	protected void reMain() {
		lockTasks=false; updMain();
		display.setCurrent(frmMain);
	}
	
	public void commandAction(Command c, Displayable d) {
		if (c==cmdSave) {
			lockTasks=true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			int N = Tasks.size();
			Task T;
			try {
				dos.writeInt(N);
				for (int i=0;i<N;i++) {
					T = (Task) Tasks.elementAt(i);
					dos.writeUTF(T.Title);
					dos.writeInt(T.DeadLine);
				}
				dos.flush(); dos.close();
			} catch (IOException e) { }
			byte[] record=baos.toByteArray();
			try {
				RecordStore.deleteRecordStore("jMinder");
			} catch (RecordStoreException e) { }
			RecordStore rms;
			try {
				rms = RecordStore.openRecordStore("jMinder", true);
				rms.addRecord(record, 0, record.length);
				rms.closeRecordStore();
			} catch (RecordStoreException e) { }
			reMain();
		}
		if (c==cmdLoad) {
			lockTasks=true;
			RecordStore rms;
			byte[] record=null;
			try {
				rms = RecordStore.openRecordStore("jMinder", false);
				record = rms.getRecord(1);
				rms.closeRecordStore();
			} catch (RecordStoreException e) { }
			ByteArrayInputStream bais = new ByteArrayInputStream(record);
			DataInputStream dis = new DataInputStream(bais);
			Tasks.removeAllElements();
			try {
				int N = dis.readInt();
				for (int i=0;i<N;i++) {
					Tasks.addElement(new Task(dis.readUTF(),dis.readInt()));
				}
			} catch (IOException e) { }
			reMain();
		}
		if (d==cnvLigher) 	reMain();
		if (c==cmdExit) 	destroyApp(true);
		if (c==cmdLighter) 	display.setCurrent(cnvLigher);
		if (c==cmdNew)		{
			lockTasks=true;
			Task T = new Task("",0);
			Tasks.addElement(T);
			updEdit("new",T);
			display.setCurrent(frmEdit);
		}
		if (d==frmEdit) {
			frmEdit_item.Title = fldTitle.getString();
			frmEdit_item.DeadLine = Integer.parseInt(fldDeadLine.getString());
			reMain();
		}
		if (c==cmdDelete) {
			lockTasks=true; frmSelect_caller=c;
			updSelect("delete"); display.setCurrent(frmSelect); 
			}
		if (c==cmdEdit) {
			lockTasks=true; frmSelect_caller=c;
			updSelect("edit"); display.setCurrent(frmSelect);
		}
		if (d==frmSelect) {
			if (c==cmdOK) {
				int SelectedIndex = frmSelect.getSelectedIndex();
				if (frmSelect_caller==cmdDelete) {
					Tasks.removeElementAt(SelectedIndex);
					reMain();
				}
				if (frmSelect_caller==cmdEdit) {
					updEdit("edit",(Task) Tasks.elementAt(SelectedIndex)); 
					display.setCurrent(frmEdit);
				}
			} else 
				reMain();
		}
	}
}
