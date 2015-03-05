package encoders;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import javax.imageio.ImageIO;

public class VectorQuantizer extends Encoder {

	static{
		try {
			EncoderFactory.register("VectorQuantizer",
					VectorQuantizer.class.getConstructor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void encode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		// TODO Auto-generated method stub
		
//		BufferedImage inputImage = ImageIO.read(inputStream);
//		
//		long inputSize = (Long)args[0];
//		int vectorWidth = (Integer)args[1];
//		int vectorHeight = (Integer)args[2];
//		int bitsNumber = (Integer)args[3];
//		int levelsNumber = 1<<bitsNumber;
//		int vectorsNum = ((inputImage.getWidth()/vectorWidth)*
//				(inputImage.getHeight()/vectorHeight));
		
	}

	@Override
	public void decode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
