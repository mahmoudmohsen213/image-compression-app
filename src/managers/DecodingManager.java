package managers;

import ui.MainFrame;

public class DecodingManager extends Manager {

	public DecodingManager(String inputFileName, String outputFileName,
			String encoderID, MainFrame mainFrame) throws Exception {
		super(inputFileName, outputFileName, encoderID, mainFrame);
		isGood = this.initialize();
	}

	@Override
	public void run() {
		try {
			this.encoder.decode(inputStream, outputStream, encodingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
