package encoders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

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
		
	}

	@Override
	public void decode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
