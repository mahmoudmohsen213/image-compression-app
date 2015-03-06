package managers;

import ui.MainFrame;

public class EncodingManager extends Manager  {

	public EncodingManager(String inputFileName, String outputFileName,
			String encoderID, MainFrame mainFrame) throws Exception {
		super(inputFileName, outputFileName, encoderID, mainFrame);
		isGood = this.initialize();
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
