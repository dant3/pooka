package net.suberic.pooka.gui;
import net.suberic.pooka.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.TextAction;
import java.util.*;
import net.suberic.util.gui.*;
import net.suberic.util.event.*;
import net.suberic.util.thread.*;
import net.suberic.util.swing.*;

/**
 * This is a JPanel which contains a FolderDisplayPanel for showing
 * the messages in the current folder.  It also can be updated dynamically
 * with a new folder if the selected folder changes.
 */

public class PreviewFolderPanel extends JPanel implements FolderDisplayUI {

    private PreviewContentPanel contentPanel;

    private FolderDisplayPanel folderDisplay = null;
    private FolderInfo displayedFolder = null;

    private FolderStatusBar folderStatusBar = null;

    private boolean enabled;

    /**
     * Creates an empty PreviewFolderPanel.
     */
    public PreviewFolderPanel(PreviewContentPanel newContentPanel) {
	contentPanel = newContentPanel;

	this.setSize(new java.awt.Dimension(newContentPanel.getSize().width, Integer.parseInt(Pooka.getProperty("Pooka.previewPanel.folderSize", "200"))));

	folderDisplay = new FolderDisplayPanel();
    }

    /**
     * Creates a new PreviewFolderPanel for the given Folder.
     */
    public PreviewFolderPanel(PreviewContentPanel newContentPanel, FolderInfo folder) {
	contentPanel = newContentPanel;

	this.setSize(newContentPanel.getSize());

	displayedFolder = folder;
	folderDisplay = new FolderDisplayPanel(folder);
	folderStatusBar = new FolderStatusBar(folder);

	this.setLayout(new java.awt.BorderLayout());

	this.add("Center", folderDisplay);
	this.add("South", folderStatusBar);
    }

    /**
     * Opens the display for the given Folder.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public void openFolderDisplay() {
	if (displayedFolder != null) 
	    contentPanel.showFolder(displayedFolder.getFolderID());
    }

    /**
     * Closes the display for the given Folder.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public void closeFolderDisplay() {
	folderDisplay.removeMessageTable();
	if (displayedFolder != null && displayedFolder.getFolderDisplayUI() == this)
	    displayedFolder.setFolderDisplayUI(null);
	displayedFolder = null;
	setEnabled(false);
    }

    /**
     * Gets the FolderInfo for the currently displayed Folder.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public FolderInfo getFolderInfo() {
	return displayedFolder;
    }

    /**
     * Sets the FolderInfo to be displayed by this PreviewFolderPanel.
     */
    public void setFolderInfo(FolderInfo fi) {
	getFolderDisplay().removeMessageTable();
	getFolderDisplay().setFolderInfo(fi);
	getFolderDisplay().createMessageTable();

	displayedFolder = fi;
    }

    /**
     * Sets the panel enabled or disabled.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public void setEnabled(boolean newValue) {
	enabled = newValue;
    }

    /**
     * Sets the busy property of the panel.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public void setBusy(boolean newValue) {
	if (newValue)
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	else
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Shows an Input dialog for this panel.
     *
     * As defined in interface net.suberic.pooka.gui.FolderDisplayUI.
     */
    public String showInputDialog(String inputMessage, String title) {
	return JOptionPane.showInputDialog(this, inputMessage, title, JOptionPane.QUESTION_MESSAGE);
    }	

    /**
     * Shows an Input Dialog with the given Input Panels and title.
     * 
     * As defined in interface net.suberic.pooka.gui.MessageUI.
     */
    public String showInputDialog(Object[] inputPanels, String title) {
	return JOptionPane.showInputDialog(this, inputPanels, title, JOptionPane.QUESTION_MESSAGE);
    }   

    /**
     * Shows a Confirm dialog.
     * 
     * As defined in interface net.suberic.pooka.gui.MessageUI.
     */
    public int showConfirmDialog(String message, String title, int optionType, int messageType) {
	return JOptionPane.showConfirmDialog(this, message, title, messageType);
    }

    /**
     * Gets the default UserProfile for this component.
     *
     * As defined in interface net.suberic.pooka.UserProfileContainer.
     */
    public UserProfile getDefaultProfile() {
	if (getFolderInfo() != null)
	    return getFolderInfo().getDefaultProfile();
	else return null;
    }

    /**
     * Shows an Error with the given parameters.
     *
     * As defined in interface net.suberic.pooka.gui.ErrorHandler.
     */
    public void showError(String errorMessage) {
	showError(errorMessage, Pooka.getProperty("Error", "Error"));
    }

    /**
     * Shows an Error with the given parameters.
     *
     * As defined in interface net.suberic.pooka.gui.ErrorHandler.
     */
    public void showError(String errorMessage, String title) {
	JOptionPane.showMessageDialog(this, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an Error with the given parameters.
     *
     * As defined in interface net.suberic.pooka.gui.ErrorHandler.
     */
    public void showError(String errorMessage, String title, Exception e) {
	showError(errorMessage + e.getMessage(), title);
	e.printStackTrace();
    }


    // MessageLoadedListener
    
    /**
     * 
     * Defined in net.suberic.pooka.event.MessageLoadedListener.
     */
    public void handleMessageLoaded(net.suberic.pooka.event.MessageLoadedEvent e) {
	if (getFolderStatusBar() != null && getFolderStatusBar().getTracker() != null)
	    getFolderStatusBar().getTracker().handleMessageLoaded(e);
    }

    // ConnectionListener
    
    /**
     *
     */
    public void closed(ConnectionEvent e) {

    }

    /**
     *
     */
    public void disconnected(ConnectionEvent e) {

    }

    /**
     *
     */
    public void opened(ConnectionEvent e) {

    }

    // MessageCounteListener
    /**
     *
     */
    public void messagesAdded(MessageCountEvent e) {
	getFolderStatusBar().messagesAdded(e);
    }

    public void messagesRemoved(MessageCountEvent e) { 
	System.out.println("previewfolderpanel got the messaesremoved.");
	getFolderStatusBar().messagesRemoved(e);
    }

    /**
     * Gets the folderStatusBar.
     */
    public FolderStatusBar getFolderStatusBar() {
	return folderStatusBar;
    }

    /**
     * Gets the currently available Actions for this component.
     *
     * As defined in interface net.suberic.pooka.gui.ActionContainer.
     */
    public Action[] getActions() {
	if (enabled) {
	    Action[] returnValue = null;

	    if (getFolderDisplay() != null)
		returnValue = getFolderDisplay().getActions();
	    
	    return returnValue;

	} else
	    return null;
    }

    public FolderDisplayPanel getFolderDisplay() {
	return folderDisplay;
    }
}
