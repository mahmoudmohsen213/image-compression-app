package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class QuantizationInputDialog_ok_btn_ActionListener implements
		ActionListener {
	private QuantizationInputDialog parentDialog;
	
	public QuantizationInputDialog_ok_btn_ActionListener(
			QuantizationInputDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try{ 
			parentDialog.parsedWidth = Integer.parseInt(
					parentDialog.getEnteredWidth());
		}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid parameter");
			return;
		}
		
		try{ 
			parentDialog.parsedHeight =Integer.parseInt(
					parentDialog.getEnteredHeight());
		}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid parameter");
			return;
		}
		
		try{ 
			parentDialog.parsedBitsNumber = Integer.parseInt(
					parentDialog.getEnteredBitsNumber());
		}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid parameter");
			return;
		}
		
		parentDialog.selectedOption = QuantizationInputDialog.OK_OPTION;
		parentDialog.setVisible(false);
	}
}
