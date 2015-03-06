package managers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import ui.MainFrame;
import encoders.Encoder;
import encoders.EncoderFactory;

public abstract class Manager implements Runnable {
	protected String inputFileName;
	protected String outputFileName;
	protected String encoderID;
	protected BufferedInputStream inputStream;
	protected BufferedOutputStream outputStream;
	protected Object[] encodingParameters;
	protected Encoder encoder;
	protected MainFrame mainFrame;
	protected Thread thread;
	protected boolean isReady;
	
	public Manager(String inputFileName, String outputFileName,
			String encoderID, MainFrame mainFrame){
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.encoderID = encoderID;
		this.mainFrame = mainFrame;
		this.thread = new Thread(this);
		this.isReady = false;
	}
	
	protected boolean initialize() throws Exception{
		inputStream = new BufferedInputStream(
				new FileInputStream(inputFileName));
		
		outputStream = new BufferedOutputStream(
				new FileOutputStream(outputFileName));

		encoder = EncoderFactory.create(encoderID);
		encoder.addObserver(mainFrame);
		
		return true;
	}
	
	public final void start(){
		if(isReady)
			thread.start();
	}
}
