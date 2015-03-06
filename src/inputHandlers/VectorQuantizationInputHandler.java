package inputHandlers;

import encoders.EncoderFactory;

public class VectorQuantizationInputHandler implements InputHandler {
	static{
		try {
			EncoderFactory.register("VectorQuantizer",
					VectorQuantizationInputHandler.class.getConstructor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object[] getInput() {
		// TODO Auto-generated method stub
		return null;
	}
}
