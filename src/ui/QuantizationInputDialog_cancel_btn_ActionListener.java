package ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class QuantizationInputDialog_cancel_btn_ActionListener implements
		ActionListener {
	private QuantizationInputDialog parentDialog;
	
	public QuantizationInputDialog_cancel_btn_ActionListener(
			QuantizationInputDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		parentDialog.selectedOption = QuantizationInputDialog.CANCEL_OPTION;
		parentDialog.setVisible(false);
	}
}
