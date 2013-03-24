package jMinder;

import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

public class main extends MIDlet implements CommandListener {

	public void destroyApp(boolean unconditional) { notifyDestroyed(); }

	public void pauseApp() { }
	
	Display display = Display.getDisplay(this);
	Form frmMain = new Form("jMinder");
	Command cmdNew = new Command("New", Command.EXIT, 0);
	Command cmdExit = new Command("Exit", Command.OK, 0);
	
	Form frmNew = new Form("new");
	Command cmdOK = new Command("OK", Command.OK, 0);
	Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);

	DateField fldDateTime = new DateField("Now", DateField.DATE_TIME);
	
	String[] Tasks = {"jMinder","Cortex Book","Milling"};
	
	public void startApp() { // throws MIDletStateChangeException {
		// def menus
		// main
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdExit);
		frmMain.setCommandListener(this);
		frmMain.append(fldDateTime);
		fldDateTime.setDate(new Date());
		for (int i=0;i<Tasks.length;i++) frmMain.append(Tasks[i]+"\n");
		// new
		frmNew.addCommand(cmdOK);
		frmNew.addCommand(cmdCancel);
		frmNew.setCommandListener(this);
		// show main form
		display.setCurrent(frmMain);
	}

	public void commandAction(Command c, Displayable d) {
		if (d==frmMain) {
			if (c==cmdExit) destroyApp(false);
			if (c==cmdNew) display.setCurrent(frmNew);
		}
		if (d==frmNew) {
			if (c==cmdOK) {}
			if (c==cmdCancel) {}
			display.setCurrent(frmMain);
		}
	}

}

/*

 // implements ActionListener {

	public void destroyApp(boolean arg0) { notifyDestroyed(); }
	public void pauseApp() { }
	
	Command cmdExit;
	
	public void startApp() { // throws MIDletStateChangeException {
		Display.init(this);
		// main form
		Image bgImage=null;
		try {
			 bgImage = Image.createImage("/lenin-240x320.png");
		} catch (IOException e) {e.printStackTrace();}
		frmMain.getStyle().setBgImage(bgImage);
		// menu
		frmMain.addCommand(cmdExit);
		frmMain.l
		// time bar
		Date date = new Date();
		Label lblTime = new Label(date.toString());
		frmMain.addComponent(lblTime);
		// show form
		frmMain.show();
	}
	
	public void actionPerformed(ActionEvent event) {
		Command c = event.getCommand();
		if (c==cmdExit) destroyApp(true);
	}
}

*/