package net.suberic.pooka;
import net.suberic.pooka.gui.*;
import java.awt.*;
import javax.swing.*;

public class Pooka {
    static public net.suberic.util.VariableBundle resources;
    static public String localrc;
    static public DateFormatter dateFormatter;
    static public javax.activation.CommandMap mailcap;
    static public javax.activation.MimetypesFileTypeMap mimeTypesMap = new javax.activation.MimetypesFileTypeMap();
    static public net.suberic.pooka.gui.MainPanel panel;

    static public javax.mail.Session defaultSession;
    static public net.suberic.pooka.thread.FolderTracker folderTracker;

    static public void main(String argv[]) {
	localrc = new String (System.getProperty("user.home") + System.getProperty("file.separator") + ".pookarc"); 

	try {
	    resources = new net.suberic.util.VariableBundle(new java.io.FileInputStream(localrc), new net.suberic.util.VariableBundle(new Object().getClass().getResourceAsStream("/net/suberic/pooka/Pookarc"), "net.suberic.pooka.Pooka"));
       	} catch (Exception e) {
	    resources = new net.suberic.util.VariableBundle(new Object().getClass().getResourceAsStream("/net/suberic/pooka/Pookarc"), "net.suberic.pooka.Pooka");
	}

	dateFormatter = new DateFormatter();

	UserProfile.createProfiles(resources);
	resources.addValueChangeListener(UserProfile.vcl, "UserProfile");

	mailcap = new FullMailcapCommandMap();
	folderTracker = new net.suberic.pooka.thread.FolderTracker();
	folderTracker.start();

	javax.activation.CommandMap.setDefaultCommandMap(mailcap);
	javax.activation.FileTypeMap.setDefaultFileTypeMap(mimeTypesMap);

	JFrame frame = new JFrame("Pooka");
	SimpleAuthenticator auth = new SimpleAuthenticator(frame);
	defaultSession = javax.mail.Session.getDefaultInstance(System.getProperties(), auth);

	frame.setBackground(Color.lightGray);
	frame.getContentPane().setLayout(new BorderLayout());
	panel = new MainPanel(frame);
	panel.configureMainPanel();
	frame.getContentPane().add("North", panel.getMainToolbar());
	frame.getContentPane().add("Center", panel);
	frame.setJMenuBar(panel.getMainMenu());
	frame.pack();
	frame.setSize(Integer.parseInt(Pooka.getProperty("Pooka.hsize", "800")), Integer.parseInt(Pooka.getProperty("Pooka.vsize", "600")));
        frame.show();
	if (getProperty("Store", "").equals("")) {
	    NewAccountPooka nap = new NewAccountPooka(panel.getMessagePanel());
	    nap.start();
	}
    }

    static public String getProperty(String propName, String defVal) {
	return (resources.getProperty(propName, defVal));
    }

    static public String getProperty(String propName) {
	return (resources.getProperty(propName));
    }

    static public void setProperty(String propName, String propValue) {
	resources.setProperty(propName, propValue);
    }

    static public net.suberic.util.VariableBundle getResources() {
	return resources;
    }

    static public boolean isDebug() {
	if (resources.getProperty("Pooka.debug").equals("true"))
	    return true;
	else
	    return false;
    }

    static public DateFormatter getDateFormatter() {
	return dateFormatter;
    }

    static public javax.activation.CommandMap getMailcap() {
	return mailcap;
    }

    static public javax.activation.MimetypesFileTypeMap getMimeTypesMap() {
	return mimeTypesMap;
    }

    static public javax.mail.Session getDefaultSession() {
	return defaultSession;
    }

    static public net.suberic.pooka.thread.FolderTracker getFolderTracker() {
	return folderTracker;
    }

    static public MainPanel getMainPanel() {
	return panel;
    }
}










