package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import managers.DecodingManager;
import ui.MainFrame;

public class Decode_btn_ActionListener implements ActionListener {
private MainFrame parentFrame;
	
	public Decode_btn_ActionListener(MainFrame parentFrame){
		this.parentFrame = parentFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		try{
//			DecodingManager decodingManager = new DecodingManager(
//					parentFrame.getInputFileName(), parentFrame.getOutputFileName(),
//					parentFrame.getEncodingSelection(), parentFrame);
//			decodingManager.start();
//		} catch(Exception ex){
//			JOptionPane.showMessageDialog(null, ex.getMessage());
//		}
	}
}
