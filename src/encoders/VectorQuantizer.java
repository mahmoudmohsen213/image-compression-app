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
		System.out.println("VectorQuantizer.encode()  " + args.length);
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
	}

	@Override
	public void decode(BufferedInputStream inputStream,
			BufferedOutputStream outputStream, Object... args) throws Exception {
		System.out.println("VectorQuantizer.decode()  " + args.length);
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
			
			for(int i=0;i<codebook.size();++i){
				ImageVector vector1 = new ImageVector(vectorWidth, vectorHeight);
				ImageVector vector2 = new ImageVector(vectorWidth, vectorHeight);
				
				// splitting the current vector
				ImageVector currentVector = codebook.get(i).first;
				currentVector.split(vector1, vector2);
				
				// removing the current vector
				codebook.remove(i);
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
	}
	
	private void OptimizeVectors(ArrayList<Pair<ImageVector, ArrayList<Integer>>> codebook,
			ArrayList<ImageVector> clusteredImage){
		// optimize each quantization vector according
		// to its image vectors
	}
	
	private void CleanCodebook(ArrayList<Pair<ImageVector,
			ArrayList<Integer>>> codebook){
		// removing unused quantization vector
		for(int i = codebook.size()-1;i>=0;--i)
			if(codebook.get(i).second.size() == 0)
				codebook.remove(i);
	}
}
