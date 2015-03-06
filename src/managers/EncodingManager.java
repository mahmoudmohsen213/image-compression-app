package managers;

import inputHandlers.InputHandler;
import inputHandlers.InputHandlerFactory;

import java.io.File;

import ui.MainFrame;

public class EncodingManager extends Manager  {

	public EncodingManager(String inputFileName, String outputFileName,
			String encoderID, MainFrame mainFrame) throws Exception {
		super(inputFileName, outputFileName, encoderID, mainFrame);
		isReady = this.initialize();
	}

	@Override
	protected boolean initialize() throws Exception {
		Object[] input = new Object[0];
		InputHandler inputHandler = InputHandlerFactory.create(encoderID + ":Encode");
		if(inputHandler != null)
			input = inputHandler.getInput();
		if(input == null)
			return false;
		
		File tempFile = new File(inputFileName);
		long inputSize = tempFile.length();
		encodingParameters = new Object[input.length + 1];
		encodingParameters[0] = new Long(inputSize);
		for(int i=1;i<encodingParameters.length;++i)
			encodingParameters[i] = input[i-1];
		
		return super.initialize();
	}
	
	@Override
	public void run() {
		try {
			this.encoder.encode(inputStream, outputStream, encodingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
