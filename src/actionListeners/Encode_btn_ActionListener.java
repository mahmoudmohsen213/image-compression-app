package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import managers.EncodingManager;
import ui.MainFrame;

public class Encode_btn_ActionListener implements ActionListener {
private MainFrame parentFrame;
	
	public Encode_btn_ActionListener(MainFrame parentFrame){
		this.parentFrame = parentFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		try{
//			EncodingManager encodingManager = new EncodingManager(
//					parentFrame.getInputFileName(), parentFrame.getOutputFileName(),
//					parentFrame.getEncodingSelection(), parentFrame);
//			encodingManager.start();
//		} catch(Exception ex){
//			JOptionPane.showMessageDialog(null, ex.getMessage());
//		}
	}
}
