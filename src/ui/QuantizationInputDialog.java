package ui;

import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import actionListeners.QuantizationInputDialog_ok_btn_ActionListeners;

@SuppressWarnings("serial")
public class QuantizationInputDialog extends JDialog {
	private JTextField width_txt;
	private JTextField height_txt;
	private JButton ok_btn;
	private int parsedWidth;
	private int parsedHeight;

	public QuantizationInputDialog() {
		setBounds(100, 100, 200, 120);
		getContentPane().setLayout(null);
		this.setTitle("Enter vector dimentions");
		
		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(10, 11, 44, 14);
		
		JLabel lblHight = new JLabel("Hight:");
		lblHight.setBounds(10, 36, 44, 14);
		
		width_txt = new JTextField();
		width_txt.setBounds(64, 8, 115, 20);
		width_txt.setColumns(10);
		
		height_txt = new JTextField();
		height_txt.setBounds(64, 33, 115, 20);
		height_txt.setColumns(10);
		
		ok_btn = new JButton("OK");
		ok_btn.setBounds(90, 64, 89, 23);
		ok_btn.addActionListener(new QuantizationInputDialog_ok_btn_ActionListeners(this));
		
		getContentPane().add(lblWidth);
		getContentPane().add(lblHight);
		getContentPane().add(width_txt);
		getContentPane().add(height_txt);
		getContentPane().add(ok_btn);
		this.setResizable(false);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	}
	
	public void setParsedWidth(int width){
		this.parsedWidth = width;
	}
	
	public int getParsedWidth(){
		return parsedWidth;
	}
	
	public String getEnteredWidth(){
		return width_txt.getText();
	}
	
	public void setParsedHeight(int Height){
		this.parsedHeight = Height;
	}
	
	public int getParsedHeight(){
		return parsedHeight;
	}
	
	public String getEnteredHeight(){
		return height_txt.getText();
	}
}
