import ui.MainFrame;
import inputHandlers.VectorQuantizationEncodingInputHandler;
import encoders.VectorQuantizer;



public class Program {

	public static void main(String[] args) {
		Program.loadClasses();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}
	
	private static void loadClasses(){
		new VectorQuantizer();
		new VectorQuantizationEncodingInputHandler();
	}
}
