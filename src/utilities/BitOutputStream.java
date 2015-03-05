package utilities;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream {
	private BufferedOutputStream outputStream;
	private byte currentByte;
	private byte currentIndex;
	
	public BitOutputStream(BufferedOutputStream outputStream){
		this.outputStream = outputStream;
		currentByte = 0;
		currentIndex = 0;
	}
	
	@Override
	public void write(int bit) throws IOException {
		if(currentIndex == 8){
			outputStream.write(currentByte);
			currentByte = 0;
			currentIndex = 0;
		}
		
		currentByte |= ((bit&1)<<(currentIndex++));
	}
	
	public void writeByte(byte outByte) throws IOException{
		for(int i=0;i<8;++i)
			this.write((outByte>>i)&1);
	}
	
	public void writeChar(char outChar) throws IOException{
		for(int i=0;i<16;++i)
			this.write((outChar>>i)&1);
	}
	
	public void writeBitString(String outStr) throws IOException{
		char tempChar;
		for(int i=0;i<outStr.length();++i){
			tempChar = outStr.charAt(i);
			if(tempChar == '0')
				this.write(0);
			else if(tempChar == '1')
				this.write(1);
			else throw new RuntimeException("Invalid argument");
		}
	}
	
	public void close() throws IOException{
		outputStream.write(currentByte);
		outputStream.close();
	}
}
