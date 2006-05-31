package net.suberic.util.gui.propedit;
import javax.swing.*;
import java.awt.Dimension;
import java.util.logging.Logger;

/**
 * This provides the base class for a simple, label-on-the-left 
 * value-on-the-right property editor.
 */
public abstract class LabelValuePropertyEditor extends SwingPropertyEditor {
  // the label component.  this is used for a default implementation
  // of the sizing code we have below.
  protected java.awt.Container labelComponent;
  
  // the value component.  this is used for a default implementation
  // of the sizing code we have below.
  protected java.awt.Container valueComponent;

  /**
   * A default implementation of setEnabled.  This simply sets the
   * enabled flag to the newValue.  If the labelComponent and 
   * valueComponent attributes are set, it will also call setEnabled
   * on those.
   *
   * Subclasses which do not use the default labelComponent and 
   * valueComponent attributes, or which require additional functionality,
   * should override this method.
   */
  public void setEnabled(boolean newValue) {
    enabled=newValue;
    if (valueComponent != null)
      valueComponent.setEnabled(newValue);
    if (labelComponent != null)
      labelComponent.setEnabled(newValue);
  }
  
  /**
   * Creates a JLabel for this component.
   */
  public JLabel createLabel() {
    String defaultLabel;
    int dotIndex = property.lastIndexOf(".");
    if (dotIndex == -1) 
      defaultLabel = new String(property);
    else
      defaultLabel = property.substring(dotIndex+1);
    
    JLabel returnValue = new JLabel(manager.getProperty(editorTemplate + ".label", defaultLabel));
    
    return returnValue;
  }

  /**
   * Gets the current valueComponent.
   */
  public java.awt.Container getValueComponent() {
    return valueComponent;
  }

  /**
   * Gets the current labelComponent.
   */
  public java.awt.Container getLabelComponent() {
    return labelComponent;
  }

  /**
   * Gets the minimum size for the labelComponent.
   */
  public Dimension getMinimumLabelSize() {
    if (labelComponent != null) {
      return labelComponent.getMinimumSize();
    } else {
      return new Dimension(0,0);
    }
  }
  
  /**
   * Gets the minimum size for the valueComponent.
   */
  public Dimension getMinimumValueSize() {
    if (valueComponent != null) {
      return valueComponent.getMinimumSize();
    } else {
      return new Dimension(0,0);
    }
  }
  
  /**
   * Returns the calculated minimum size for this component.
   */
  public Dimension getMinimumTotalSize() {
    return this.getMinimumSize();
  }

  /**
   * Sets the size for the label component and the value component.
   */
  public void setSizes(Dimension labelSize, Dimension valueSize) {
    if (labelComponent != null)
      labelComponent.setSize(labelSize);
    if (valueComponent != null)
      valueComponent.setSize(valueSize);
  }
  
  /**
   * Sets the widths for the label component and the value component.
   */
  public void setWidths(int labelWidth, int valueWidth) {
    if (labelComponent != null)
      labelComponent.setSize(new Dimension(labelWidth, labelComponent.getSize().height));
    if (valueComponent != null)
      valueComponent.setSize(new Dimension(valueWidth, valueComponent.getSize().height));
  }
  
  /**
   * Sets the heights for the label component and the value component.
   */
  public void setHeights(int labelHeight, int valueHeight) {
    if (labelComponent != null)
      labelComponent.setSize(new Dimension(labelComponent.getSize().width, labelHeight));
    if (valueComponent != null)
      valueComponent.setSize(new Dimension(valueComponent.getSize().width, valueHeight));
  }
  
  /**
   * Gets the parent PropertyEditorPane for the given component.
   */
  public PropertyEditorPane getPropertyEditorPane() {
    return getPropertyEditorPane(valueComponent);
  }
}
