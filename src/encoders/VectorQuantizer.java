package encoders;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
	private int levelsNumber;
	private int clusteredImageWidth;
	private int clusteredImageHeight;
	
	@Override
	public void encode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		System.out.println("VectorQuantizer.encode()::begin");
		BufferedImage inputImage = ImageIO.read(inputStream);
		
		long inputSize = (Long)args[0];
		vectorWidth = (Integer)args[1];
		vectorHeight = (Integer)args[2];
		bitsNumber = (Integer)args[3];
		levelsNumber = 1<<bitsNumber;
		
		// dividing the input image into a list of clusters
		ArrayList<ImageVector> clusteredImage = clusterImage(inputImage);
		clusteredImageWidth = inputImage.getWidth()/vectorWidth;
		clusteredImageHeight = inputImage.getHeight()/vectorHeight;
		
		// releasing unnecessary memory usage
		inputImage = null;
		
		// array containing the quantization vectors and list of clusters
		// to which each quantization vector map
		ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook =
				this.initializeCodebook();
		// building the codebook
		this.Quantize(codebook, clusteredImage);
		// removing unused quantization vector
		this.CleanCodebook(codebook);
		System.out.println("VectorQuantizer.encode()::end");
	}

	@Override
	public void decode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		System.out.println("VectorQuantizer.decode()::begin");
		System.out.println("VectorQuantizer.decode()::end");
	}
	
	private ArrayList<ImageVector> clusterImage(BufferedImage inputImage){
		// dividing the input image into a list of clusters
		ArrayList<ImageVector> clusteredImage = new ArrayList<ImageVector>();
		for(int clusterXCoord = 0; 
				clusterXCoord<inputImage.getWidth(); 
				clusterXCoord+=vectorWidth){
			for(int clusterYCoord = 0; 
					clusterYCoord<inputImage.getHeight(); 
					clusterYCoord+=vectorHeight){
				ImageVector cluster = new ImageVector(vectorWidth, vectorHeight);
				for(int i = 0; i<vectorWidth;++i){
					for(int j=0;j<vectorHeight;++j){
						cluster.setPixel(i, j, inputImage.getRGB(
								i+clusterXCoord, j+clusterYCoord));
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
	
	private void Quantize(ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook,
			ArrayList<ImageVector> clusteredImage){
		
		while(codebook.size()<levelsNumber){
			// mapping the quantization vectors to the image vectors
			this.DistributeVectors(codebook, clusteredImage);
			// optimize each quantization vector according
			// to its image vectors
			this.OptimizeVectors(codebook, clusteredImage);
			
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
		
		for(int i=0;i<10;i++){
			// mapping the quantization vectors to the image vectors
			this.DistributeVectors(codebook, clusteredImage);
			// optimize each quantization vector according
			// to its image vectors
			this.OptimizeVectors(codebook, clusteredImage);
		}
	}
	
	private void DistributeVectors(ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook,
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
	
	private void OptimizeVectors(ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook,
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
			
			for(int x=0;x<vectorWidth;++x){
				for(int y=0;y<vectorHeight;++y){
					codebook.get(i).first.setPixel(x, y,
							tempImageVector.getPixel(x, y) /
							codebook.get(i).second.size());
				}
			}
		}
	}
	
	private void CleanCodebook(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook){
		// removing unused quantization vector
		for(int i = codebook.size()-1;i>=0;--i)
			if(codebook.get(i).second.size() == 0)
				codebook.remove(i);
	}
}
