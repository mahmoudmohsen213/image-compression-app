package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import ui.QuantizationInputDialog;

public class QuantizationInputDialog_ok_btn_ActionListeners implements
		ActionListener {
	private QuantizationInputDialog parentDialog;
	
	public QuantizationInputDialog_ok_btn_ActionListeners(
			QuantizationInputDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try{ 
			parentDialog.setParsedHeight(
					Integer.parseInt(
							parentDialog.getEnteredHeight()));
		}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid parameter");
			return;
		}
		
		try{ 
			parentDialog.setParsedHeight(
					Integer.parseInt(
							parentDialog.getEnteredHeight()));
		}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid parameter");
			return;
		}
		
		parentDialog.setVisible(false);
	}
}
