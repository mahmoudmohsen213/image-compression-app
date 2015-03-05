package actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import ui.MainFrame;

public class InputFile_browse_btn_ActionListener implements ActionListener {
	private MainFrame parentFrame;
	
	public InputFile_browse_btn_ActionListener(MainFrame parentFrame){
		this.parentFrame = parentFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("C:");
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			parentFrame.setInputFileName(fileChooser.getSelectedFile().getAbsolutePath());
	}
}
