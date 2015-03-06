package inputHandlers;

import ui.QuantizationInputDialog;

public class VectorQuantizationInputHandler implements InputHandler {
	static{
		try {
			InputHandlerFactory.register("VectorQuantizer",
					VectorQuantizationInputHandler.class.getConstructor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object[] getInput() {
		QuantizationInputDialog inputDialog = new QuantizationInputDialog();
		if(inputDialog.showDialog() == QuantizationInputDialog.OK_OPTION){
			Object[] input = new Object[3];
			input[0] = new Integer(inputDialog.getParsedWidth());
			input[1] = new Integer(inputDialog.getParsedHeight());
			input[2] = new Integer(inputDialog.getParsedBitsNumber());
		}
		
		return new Object[0];
	}
}
