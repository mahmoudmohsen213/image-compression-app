package actionListeners;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.QuantizationInputDialog;


public class QuantizationInputDialog_cancel_btn_ActionListener implements
		ActionListener {
	private QuantizationInputDialog parentDialog;
	
	public QuantizationInputDialog_cancel_btn_ActionListener(
			QuantizationInputDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
