package jMinder;

import javax.microedition.midlet.MIDlet;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;

public class main extends MIDlet {

	public void destroyApp(boolean unconditional) { 
//		throws MIDletStateChangeException {
		notifyDestroyed(); 
	} 

	public void pauseApp() {}

	public void startApp() { // throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		Display.init(this);
		Form frmMain = new Form("jMinder");
		frmMain.show();
	}
}

/*
public class main extends MIDlet { // implements CommandListener {

	Display display = Display.getDisplay(this);

	Command cmdNew    = new Command("New", Command.EXIT, 0);
	Command cmdDone   = new Command("Done", Command.OK, 9);
	Command cmdEdit   = new Command("Edit", Command.OK, 7);
	Command cmdRemove = new Command("Remove", Command.OK, 5);
	Command cmdSync   = new Command("Sync", Command.OK, 3);
	Command cmdExit   = new Command("Exit", Command.OK, 0);
	
	String[] pool = {
			"X","Y","J",
			"X","Y","J",
			"X","Y","J",
			"X","Y","J",
			"X","Y","J",
			"X","Y","J",
			"X","Y","J"
			};
	
	Calendar calendar = Calendar.getInstance();
	DateField datefield = new DateField("DF", DateField.DATE_TIME);
	
	public main() {
	// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean unconditional)
		throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		notifyDestroyed();
	}


	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		// add menu commands
		frmMain.addCommand(cmdNew);
		frmMain.addCommand(cmdDone);
		frmMain.addCommand(cmdEdit);
		frmMain.addCommand(cmdSync);
		frmMain.addCommand(cmdRemove);
		frmMain.addCommand(cmdExit);
		// forming screen header
		datefield.setDate(calendar.getTime());
		frmMain.append(datefield);
		frmMain.append(calendar.getTime().toString());
		
		frmMain.append("\n"+cv.w()+"x"+cv.h()+"\n");
		
		// forming task list
		for (int i=0;i<11;i++) frmMain.append(pool[i]+"\n");
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
*/