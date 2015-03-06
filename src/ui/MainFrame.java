package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import encoders.EncoderFactory;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements Observer  {

	private JPanel pane;
	private JTextField inputFileName_txt;
	private JTextField outputFileName_txt;
	private JProgressBar progress_bar;
	private JButton inputFile_browse_btn;
	private JButton outputFile_browse_btn;
	private JButton encode_btn;
	private JButton decode_btn;
	private JComboBox<String> encodingSelectionBox;
	
	public MainFrame() {
		setTitle("Image Compression App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 250, 470, 155);
		
		inputFileName_txt = new JTextField("choose the input file");
		inputFileName_txt.setEditable(true);
		inputFileName_txt.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		inputFileName_txt.setBounds(5, 6, 338, 20);
		outputFileName_txt = new JTextField("choose the output file");
		outputFileName_txt.setEditable(true);
		outputFileName_txt.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		outputFileName_txt.setBounds(5, 40, 338, 20);
		
		inputFile_browse_btn = new JButton("browse");
		inputFile_browse_btn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		inputFile_browse_btn.setBounds(353, 5, 101, 23);
		//inputFile_browse_btn.addActionListener(new InputFile_browse_btn_ActionListener(this));
		
		outputFile_browse_btn = new JButton("browse");
		outputFile_browse_btn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		outputFile_browse_btn.setBounds(353, 39, 101, 23);
		//outputFile_browse_btn.addActionListener(new OutputFile_browse_btn_ActionListener(this));
		
		encode_btn = new JButton("encode file");
		encode_btn.setFont(new Font("Tahoma", Font.BOLD, 11));
		encode_btn.setBounds(242, 71, 101, 23);
		//encode_btn.addActionListener(new Encode_btn_ActionListener(this));
		
		decode_btn = new JButton("decode file");
		decode_btn.setFont(new Font("Tahoma", Font.BOLD, 11));
		decode_btn.setBounds(353, 71, 101, 23);
		//decode_btn.addActionListener(new Decode_btn_ActionListener(this));
		
		progress_bar = new JProgressBar(0,100);
		progress_bar.setFocusable(false);
		progress_bar.setBackground(Color.WHITE);
		progress_bar.setFont(new Font("Tahoma", Font.BOLD, 11));
		progress_bar.setForeground(Color.BLUE);
		progress_bar.setBounds(5, 99, 449, 17);
		progress_bar.setStringPainted(true);
		
		encodingSelectionBox = new JComboBox<String>();
		encodingSelectionBox.setBounds(5, 71, 227, 20);
		setEncodingSelectionBoxItems();
		
		pane = new JPanel();
		pane.setLayout(null);
		pane.add(inputFileName_txt);
		pane.add(inputFile_browse_btn);
		pane.add(outputFileName_txt);
		pane.add(outputFile_browse_btn);
		pane.add(encode_btn);
		pane.add(decode_btn);
		pane.add(progress_bar);
		pane.add(encodingSelectionBox);

		getContentPane().add(pane);
		this.setResizable(false);
	}
	
	private void setEncodingSelectionBoxItems(){
		Set<String> set = EncoderFactory.getEncodersIDs();
		for(String encoderID : set)
			encodingSelectionBox.addItem(encoderID);
	}
	
	void setInputFileName(String inputFileName){
		inputFileName_txt.setText(inputFileName);
	}
	
	public String getInputFileName(){
		return inputFileName_txt.getText();
	}
	
	void setOutputFileName(String outputFileName){
		outputFileName_txt.setText(outputFileName);
	}
	
	public String getOutputFileName(){
		return outputFileName_txt.getText();
	}
	
	public String getEncodingSelection(){
		return (String) encodingSelectionBox.getSelectedItem();
	}

	@Override
	public void update(Observable o, Object arg) {
		progress_bar.setValue((Integer)arg);
	}
}
