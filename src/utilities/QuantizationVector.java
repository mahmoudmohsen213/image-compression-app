package utilities;


public class QuantizationVector {
	private int width;
	private int height;
	private double vector[][];
	
	public QuantizationVector(int width, int height){
		this.width = width;
		this.height = height;
		this.vector = new double[width][height];
	}
	
	public void setPixel(int x, int y, double value){
		vector[x][y] = value;
	}
	
	public double getPixel(int x, int y){
		return vector[x][y];
	}
	
	public void split(QuantizationVector v1, QuantizationVector v2){
		if(this.width != v1.width || this.height != v1.height)
			throw new RuntimeException("invalid arguments");
		
		if(this.width != v2.width || this.height != v2.height)
			throw new RuntimeException("invalid arguments");
		
		for(int i=0;i<v1.width;++i){
			for(int j=0;j<v1.height;++j){
				v1.setPixel(i, j, this.getPixel(i, j)-1);
				v2.setPixel(i, j, this.getPixel(i, j)+1);
			}
		}
	}
	
	public static double calculateError(
			QuantizationVector v1, QuantizationVector v2){
		if(v1.width != v2.width || v1.height != v2.height)
			throw new RuntimeException("invalid arguments");
		
		double meanError = 0.0;
		for(int i=0;i<v1.width;++i)
			for(int j=0;j<v1.height;++j)
				meanError += (v1.getPixel(i, j)-v2.getPixel(i, j));
		return meanError;
	}
}
