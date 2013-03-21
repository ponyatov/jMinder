package jMinder;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class main extends MIDlet implements CommandListener {

	Display display = Display.getDisplay(this);

	Form frmMain = new Form("jMinder");
	
	Command cmdExit   = new Command("Exit", Command.EXIT, 0);
	Command cmdNew    = new Command("New", Command.OK, 0);
	Command cmdEdit   = new Command("Edit", Command.OK, 0);
	Command cmdGet    = new Command("Get", Command.OK, 0);
	Command cmdDone   = new Command("Done", Command.OK, 0);
	Command cmdRemove = new Command("Remove", Command.OK, 0);
	
	public main() {
	// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean unconditional)
		throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		notifyDestroyed();
	}


	protected void pauseApp() {
		// TODO Auto-generated method stub
	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		// add menu commands
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdEdit);
		frmMain.addCommand(cmdGet);
		frmMain.addCommand(cmdDone);
		frmMain.addCommand(cmdRemove);
		frmMain.addCommand(cmdExit);
		// forming task list
		
		// set listener
		frmMain.setCommandListener(this);
		// show form
		display.setCurrent(frmMain);
	}

	public void commandAction(Command cmd, Displayable disp) {
		// TODO Auto-generated method stub
		try {
		if (cmd==cmdExit) destroyApp(false);
		} catch (MIDletStateChangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
