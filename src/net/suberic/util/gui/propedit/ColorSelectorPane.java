package net.suberic.util.gui.propedit;
import net.suberic.util.gui.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;


/**
 * This displays the currently selected file (if any), along with a 
 * button which will bring up a JColorChooser to choose any other file(s).
 *
 * If property._enabledBox is set to true, then this also adds a 
 * checkbox to show whether or not to use this property, or just to use
 * the defaults.
 * 
 * Note that the value that gets set is actually property.rgb (which is
 * the rgb value of the color selected), and, if the enabled checkbox is
 * there, property.enabled.  the property value itself is not set.
 */

public class ColorSelectorPane extends LabelValuePropertyEditor {

  JLabel label;
  JButton inputButton;

  int originalRgb = -1;
  Color currentColor;

  boolean useEnabledBox = false;
  JCheckBox enabledBox = null;
  boolean origEnabled = false;

  /**
   * @param propertyName The property to be edited.  
   * @param template The property that will define the layout of the 
   *                 editor.
   * @param manager The PropertyEditorManager that will manage the
   *                   changes.
   * @param isEnabled Whether or not this editor is enabled by default. 
   */
  public void configureEditor(String propertyName, String template, PropertyEditorManager newManager, boolean isEnabled) {
    property=propertyName;
    manager=newManager;
    editorTemplate = template;

    label = createLabel();

    inputButton = createInputButton();
    
    int defaultValue = inputButton.getBackground().getRGB();

    originalValue = manager.getProperty(property + ".rgb", Integer.toString(defaultValue));
    originalRgb = Integer.parseInt(originalValue);

    setCurrentColor(new Color(originalRgb));
    
    inputButton.setPreferredSize(new java.awt.Dimension(150, label.getMinimumSize().height));
    
    this.add(label);
    labelComponent = label;
    
    JPanel tmpPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0,0));
    tmpPanel.add(inputButton);
    
    useEnabledBox = manager.getProperty(editorTemplate + "._enabledBox", "false").equalsIgnoreCase("true");
    if (useEnabledBox) {
      enabledBox = new JCheckBox();
      origEnabled = manager.getProperty(property + "._enabled", "false").equalsIgnoreCase("true");
      enabledBox.setSelected(origEnabled);
      enabledBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent e) {
	    enabledBoxUpdated(enabledBox.isSelected());
	  }
	});
      tmpPanel.add(enabledBox);
    }

    valueComponent = tmpPanel;
    this.add(tmpPanel);

    this.setEnabled(isEnabled);
    
    manager.registerPropertyEditor(property, this);
  }
  
  /**
   * Creates a button that will bring up a way to select a new Color.
   */
  public JButton createInputButton() {
    JButton newButton = new JButton();
    
    newButton.addActionListener(new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	  selectNewColor();
	}
      });
    
    return newButton;
  }
  
  /**
   * This actually brings up a JColorChooser to select a new Color for 
   * the value of the property.
   */
  public void selectNewColor() {
    Color newColor = JColorChooser.showDialog(this, "title", currentColor);
    if (newColor != null) {
      setCurrentColor(newColor);
    }
  }
  
  /**
   * Sets the currently selected color to the given Color.
   */
  public void setCurrentColor(Color newColor) {
    String newValue = Integer.toString(newColor.getRGB());
    try {
      if (currentColor != newColor) {
	firePropertyChangingEvent(newValue);
	firePropertyChangedEvent(newValue);
	currentColor = newColor;
	inputButton.setBackground(currentColor);
      }
    } catch (PropertyValueVetoException pvve) {
      manager.getFactory().showError(this, "Error changing value " + label.getText() + " to " + newColor.toString() + ":  " + pvve.getReason());

    }
  }
  
  /**
   * Returns the current color.
   */
  public Color getCurrentColor() {
    return currentColor;
  }
  
  //  as defined in net.suberic.util.gui.propedit.PropertyEditorUI
  

  /**
   * This writes the currently configured value in the PropertyEditorUI
   * to the source VariableBundle.
   */
  public void setValue() {
    if (isEnabled() && isChanged()) {
      manager.setProperty(property + ".rgb", Integer.toString(currentColor.getRGB()));
      if (useEnabledBox) {
	if (enabledBox.isSelected())
	  manager.setProperty(property + "._enabled", "true");
	else
	  manager.setProperty(property + "._enabled", "false");
      }
    }
  }
  
  /**
   * Returns the current values of the edited properties as a 
   * java.util.Properties object.
   */
  public java.util.Properties getValue() {
    java.util.Properties retProps = new java.util.Properties();
    
    retProps.setProperty(property + ".rgb", Integer.toString(currentColor.getRGB()));
    if (useEnabledBox) {
      if (enabledBox.isSelected())
	retProps.setProperty(property + "._enabled", "true");
      else
	retProps.setProperty(property + "._enabled", "false");
    }
    return retProps;
  }

  /**
   * This resets the editor to the original (or latest set, if setValue() 
   * has been called) value of the edited property.
   */
  public void resetDefaultValue() {
    setCurrentColor(new Color(originalRgb));
    
    if (useEnabledBox)
      enabledBox.setSelected(origEnabled);
  }
  
  /**
   * Returns whether or not we've changed the original setting.
   */
  public boolean isChanged() {
    if (useEnabledBox) {
      return (! (enabledBox.isSelected() == origEnabled && originalValue.equals(Integer.toString(currentColor.getRGB()))));
    } else {
      return (!(originalValue.equals(Integer.toString(currentColor.getRGB()))));
    }
  }
  
  /**
   * Sets whether or not this editor is enabled.
   */
  public void setEnabled(boolean newValue) {
    if (useEnabledBox) {
      enabledBox.setEnabled(newValue);
      if (inputButton != null) {
	inputButton.setEnabled(newValue && enabledBox.isSelected());
      }
    } else {
      if (inputButton != null) {
	inputButton.setEnabled(newValue);
      }
    }
    enabled=newValue;
  }

  /**
   * Called when the enabledBox's value is updated.
   */
  private void enabledBoxUpdated(boolean newValue) {
    inputButton.setEnabled(newValue);
  }
  
}
