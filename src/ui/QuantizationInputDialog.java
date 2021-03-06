package ui;

import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class QuantizationInputDialog extends JDialog {
	public final static int CANCEL_OPTION = 0;
	public final static int OK_OPTION = 1;
	private JTextField width_txt;
	private JTextField height_txt;
	private JTextField bitsNumber_txt;
	private JButton cancel_btn;
	private JButton ok_btn;
	int parsedWidth;
	int parsedHeight;
	int parsedBitsNumber;
	int selectedOption;

	public QuantizationInputDialog() {
		setBounds(100, 100, 215, 150);
		getContentPane().setLayout(null);
		this.setTitle("Enter vector dimentions");
		selectedOption = 0;
		
		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(10, 11, 60, 14);
		
		JLabel lblHight = new JLabel("Hight:");
		lblHight.setBounds(10, 36, 60, 14);
		
		JLabel lblBitNumber = new JLabel("Bits number:");
		lblBitNumber.setBounds(10, 61, 60, 14);
		
		width_txt = new JTextField();
		width_txt.setBounds(80, 8, 118, 20);
		width_txt.setColumns(10);
		
		height_txt = new JTextField();
		height_txt.setBounds(80, 33, 118, 20);
		height_txt.setColumns(10);
		
		bitsNumber_txt = new JTextField();
		bitsNumber_txt.setBounds(80, 58, 118, 20);
		bitsNumber_txt.setColumns(2);
		
		ok_btn = new JButton("ok");
		ok_btn.setBounds(10, 86, 89, 23);
		ok_btn.addActionListener(new QuantizationInputDialog_ok_btn_ActionListener(this));
		
		cancel_btn = new JButton("cancel");
		cancel_btn.setBounds(109, 86, 89, 23);
		cancel_btn.addActionListener(new QuantizationInputDialog_cancel_btn_ActionListener(this));
		
		getContentPane().add(lblWidth);
		getContentPane().add(lblHight);
		getContentPane().add(lblBitNumber);
		getContentPane().add(width_txt);
		getContentPane().add(height_txt);
		getContentPane().add(bitsNumber_txt);
		getContentPane().add(ok_btn);
		getContentPane().add(cancel_btn);
		this.setResizable(false);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	}
	
	public int showDialog(){
		this.setVisible(true);
		return selectedOption;
	}
	
	public int getParsedWidth(){
		return parsedWidth;
	}
	
	public String getEnteredWidth(){
		return width_txt.getText();
	}
	
	public int getParsedHeight(){
		return parsedHeight;
	}
	
	public String getEnteredHeight(){
		return height_txt.getText();
	}
	
	public int getParsedBitsNumber(){
		return parsedBitsNumber;
	}
	
	public String getEnteredBitsNumber(){
		return bitsNumber_txt.getText();
	}
}
