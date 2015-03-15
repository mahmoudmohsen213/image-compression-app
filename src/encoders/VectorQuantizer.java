package encoders;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import utilities.BitInputStream;
import utilities.BitOutputStream;
import utilities.ImageVector;
import utilities.Pair;

public class VectorQuantizer extends Encoder {

	static{
		try {
			EncoderFactory.register("VectorQuantizer",
					VectorQuantizer.class.getConstructor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int vectorWidth;
	private int vectorHeight;
	private int bitsNumber;
	private int codebookSize;
	private int imageWidth;
	private int imageHeight;
	
	@Override
	public void encode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		setChanged();
		notifyObservers(0);
		BufferedImage inputImage = ImageIO.read(inputStream);
		BitOutputStream bitOutputStream = new BitOutputStream(outputStream);
		
		vectorWidth = (Integer)args[1];
		vectorHeight = (Integer)args[2];
		imageWidth = (inputImage.getWidth()/vectorWidth)*vectorWidth;
		imageHeight = (inputImage.getHeight()/vectorHeight)*vectorHeight;
		bitsNumber = (Integer)args[3];
		codebookSize = 1<<bitsNumber;
		
		// dividing the input image into a list of clusters
		ArrayList<ImageVector> clusteredImage = clusterImage(inputImage);
		
		// releasing unnecessary memory usage
		inputImage = null;
		
		// array containing the quantization vectors and list of clusters
		// to which each quantization vector map
		ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook =
				this.initializeCodebook();
		
		// building the codebook
		this.quantize(codebook, clusteredImage);
		
		// removing unused quantization vector
		this.cleanCodebook(codebook);
		codebookSize = codebook.size();
		
		// array to store the code of each vector in the image
		int codeArray[] = new int[clusteredImage.size()];
		for(int i=0;i<codebook.size();++i)
			for(Integer index : codebook.get(i).second)
				codeArray[index] = i;
		
		int counter = 0;
		bitOutputStream.writeInt(imageWidth);
		bitOutputStream.writeInt(imageHeight);
		bitOutputStream.writeInt(vectorWidth);
		bitOutputStream.writeInt(vectorHeight);
		bitOutputStream.writeInt(bitsNumber);
		bitOutputStream.writeInt(codebook.size());
		for(Pair<ImageVector, ArrayList<Integer>> entry : codebook){
			for(int i=0;i<vectorWidth;++i)
				for(int j=0;j<vectorHeight;++j)
					bitOutputStream.writeChar((char) entry.first.getPixel(i, j));
			
			setChanged();
			notifyObservers(80 + (((++counter)*10)*codebookSize));
		}
		
		for(int i=0;i<codeArray.length;++i){
			for(int j=bitsNumber-1;j>=0;--j)
				bitOutputStream.write((codeArray[i]>>j)&1);
			
			setChanged();
			notifyObservers(90 + ((i*10)*codeArray.length));
		}
		
		inputStream.close();
		bitOutputStream.close();
		setChanged();
		notifyObservers(100);
	}

	@Override
	public void decode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		setChanged();
		notifyObservers(0);
		
		BitInputStream bitInputStream = new BitInputStream(inputStream);
		imageWidth = bitInputStream.readInt();
		imageHeight = bitInputStream.readInt();
		vectorWidth = bitInputStream.readInt();
		vectorHeight = bitInputStream.readInt();
		bitsNumber = bitInputStream.readInt();
		codebookSize = bitInputStream.readInt();
		BufferedImage image = new BufferedImage(imageWidth,
				imageHeight, BufferedImage.TYPE_INT_RGB);
		
		ImageVector codebook[] = new ImageVector[codebookSize];
		for(int k=0;k<codebookSize;++k){
			ImageVector entry = new ImageVector(vectorWidth, vectorHeight);
			for(int i=0;i<vectorWidth;++i)
				for(int j=0;j<vectorHeight;++j)
					entry.setPixel(i, j, bitInputStream.readChar());
			codebook[k] = entry;
		}
		
		for(int clusterXCoord = 0; 
				clusterXCoord<imageWidth; 
				clusterXCoord+=vectorWidth){
			for(int clusterYCoord = 0; 
					clusterYCoord<imageHeight; 
					clusterYCoord+=vectorHeight){
				String bitString = bitInputStream.readBitString(bitsNumber);
				int index = Integer.parseInt(bitString, 2);
				for(int i = 0; i<vectorWidth;++i)
					for(int j=0;j<vectorHeight;++j){
						int rgb = (int) codebook[index].getPixel(i, j);
						rgb = ((rgb<<16)|(rgb<<8)|(rgb));
						image.setRGB(i+clusterXCoord, j+clusterYCoord,rgb);
					}
			}
		}
		
		ImageIO.write(image, "jpg", outputStream);
		bitInputStream.close();
		outputStream.close();
		setChanged();
		notifyObservers(100);
	}
	
	private ArrayList<ImageVector> clusterImage(BufferedImage inputImage){
		// dividing the input image into a list of clusters
		ArrayList<ImageVector> clusteredImage = new ArrayList<ImageVector>();
		for(int clusterXCoord = 0; 
				clusterXCoord<imageWidth; 
				clusterXCoord+=vectorWidth){
			for(int clusterYCoord = 0; 
					clusterYCoord<imageHeight; 
					clusterYCoord+=vectorHeight){
				ImageVector cluster = new ImageVector(vectorWidth, vectorHeight);
				for(int i = 0; i<vectorWidth;++i){
					for(int j=0;j<vectorHeight;++j){
						int rgb = inputImage.getRGB(
								i+clusterXCoord, j+clusterYCoord);
						// converting to grey scale
						rgb = ((((rgb>>16)&255)+((rgb>>8)&255)+(rgb&255))/3);
						cluster.setPixel(i, j, rgb);
					}
				}
				clusteredImage.add(cluster);
			}
		}
		
		return clusteredImage;
	}
	
	private ArrayList<Pair<ImageVector, ArrayList<Integer>>> initializeCodebook(){
		// array containing the quantization vectors and list of coordinates
		// to which each quantization vector map
		ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook =
				new ArrayList<Pair<ImageVector, ArrayList<Integer>>>();
		
		ImageVector tempVector = new ImageVector(vectorWidth, vectorHeight);
		codebook.add(new Pair<ImageVector, ArrayList<Integer>>(
				tempVector, new ArrayList<Integer>()));
		return codebook;
	}
	
	private void quantize(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook,
			ArrayList<ImageVector> clusteredImage){
		
		while(codebook.size()<codebookSize){
			setChanged();
			notifyObservers((codebook.size()*60)/codebookSize);
			// mapping the quantization vectors to the image vectors
			this.distributeVectors(codebook, clusteredImage);
			// optimize each quantization vector according
			// to its image vectors
			this.optimizeVectors(codebook, clusteredImage);
			
			int oldSize = codebook.size();
			for(int i=0;i<oldSize;++i){
				ImageVector vector1 = new ImageVector(vectorWidth, vectorHeight);
				ImageVector vector2 = new ImageVector(vectorWidth, vectorHeight);
				
				// splitting the current vector
				ImageVector currentVector = codebook.get(0).first;
				currentVector.split(vector1, vector2);
				
				// removing the current vector
				codebook.remove(0);
				// adding the new vectors
				codebook.add(new Pair<ImageVector, ArrayList<Integer>>(
						vector1, new ArrayList<Integer>()));
				codebook.add(new Pair<ImageVector, ArrayList<Integer>>(
						vector2, new ArrayList<Integer>()));
			}
		}
		
		for(int i=0;i<5;i++){
			// mapping the quantization vectors to the image vectors
			this.distributeVectors(codebook, clusteredImage);
			// optimize each quantization vector according
			// to its image vectors
			this.optimizeVectors(codebook, clusteredImage);
			setChanged();
			notifyObservers(61+(i<<2));
		}
	}
	
	private void distributeVectors(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook,
			ArrayList<ImageVector> clusteredImage){
		// mapping the quantization vectors to the image vectors
		for(int i=0;i<codebook.size();++i)
			codebook.get(i).second.clear();
		
		for(int i=0;i<clusteredImage.size();++i){
			long error = Long.MAX_VALUE;
			int bestVectorIndex = 0;
			for(int j=0;j<codebook.size();j++){
				long tempError = ImageVector.calculateError(
						clusteredImage.get(i),codebook.get(j).first);
				if(tempError < error){
					error = tempError;
					bestVectorIndex = j;
				}
			}
			
			codebook.get(bestVectorIndex).second.add(i);
		}
	}
	
	private void optimizeVectors(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook,
			ArrayList<ImageVector> clusteredImage){
		// optimize each quantization vector according
		// to its image vectors
		for(int i=0;i<codebook.size();++i){
			ImageVector tempImageVector = 
					new ImageVector(vectorWidth, vectorHeight);
			for(int j=0;j<codebook.get(i).second.size();++j){
				ImageVector subImage = clusteredImage.get(codebook.get(i).second.get(j));
				for(int x=0;x<vectorWidth;++x){
					for(int y=0;y<vectorHeight;++y){
						tempImageVector.setPixel(x, y,
								tempImageVector.getPixel(x, y) +
								subImage.getPixel(x, y));
					}
				}
			}
			
			if(codebook.get(i).second.size() == 0)
				continue;
			
			for(int x=0;x<vectorWidth;++x){
				for(int y=0;y<vectorHeight;++y){
					codebook.get(i).first.setPixel(x, y,
							tempImageVector.getPixel(x, y) /
							codebook.get(i).second.size());
				}
			}
		}
	}
	
	private void cleanCodebook(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook){
		// removing unused quantization vector
		for(int i = codebook.size()-1;i>=0;--i)
			if(codebook.get(i).second.size() == 0)
				codebook.remove(i);
	}
	
	@SuppressWarnings("unused")
	private void dumpCodebook(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook, String filePath) throws Exception{
		FileWriter writer = new FileWriter(filePath);
		for(int i=0;i<codebook.size();++i){
			writer.write("vector " + i + ":\n");
			for(int x=0;x<vectorWidth;++x){
				for(int y=0;y<vectorHeight;++y)
					writer.write(codebook.get(i).first.getPixel(x, y) + "\t");
				writer.write("\n");
			}
			writer.write("\n\n\n");
		}
		writer.close();
	}
	
	@SuppressWarnings("unused")
	private void dumpCodebook(ImageVector codebook[], String filePath) throws Exception{
		FileWriter writer = new FileWriter(filePath);
		for(int i=0;i<codebook.length;++i){
			writer.write("vector " + i + ":\n");
			for(int x=0;x<vectorWidth;++x){
				for(int y=0;y<vectorHeight;++y)
					writer.write(codebook[i].getPixel(x, y) + "\t");
				writer.write("\n");
			}
			writer.write("\n\n\n");
		}
		writer.close();
	}
	
	@SuppressWarnings("unused")
	private void dumpClusteredImage(ArrayList<ImageVector> clusteredImage,
			String filePath) throws Exception{
		FileWriter writer = new FileWriter(filePath);
		for(int i=0;i<clusteredImage.size();++i){
			writer.write("vector " + i + ":\n");
			for(int x=0;x<vectorWidth;++x){
				for(int y=0;y<vectorHeight;++y)
					writer.write(clusteredImage.get(i).getPixel(x, y) + "\t");
				writer.write("\n");
			}
			writer.write("\n\n\n");
		}
		
		writer.close();
	}
}
