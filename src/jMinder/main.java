package jMinder;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class main extends MIDlet implements CommandListener {

	Command cmdExit = new Command("Exit", Command.EXIT, 2);
	Command cmdNew = new Command("New", Command.OK, 1);
	Form frmMain = new Form("jMinder");

	public main() {
//		Alert helloAlert = new Alert("Testing", "Hello, world!", null, AlertType.INFO);
//		helloAlert.setTimeout(Alert.FOREVER);
		frmMain.setCommandListener(this);
		frmMain.append("Hello World !");
		// add
		frmMain.addCommand(cmdNew);
		// exit
		frmMain.addCommand(cmdExit);
		// get display
		Display display = Display.getDisplay(this);
		// display form
		display.setCurrent(frmMain);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		notifyDestroyed();
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	public void commandAction(Command cmd, Displayable disp) {
		// TODO Auto-generated method stub
		if (cmd==cmdExit)
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
