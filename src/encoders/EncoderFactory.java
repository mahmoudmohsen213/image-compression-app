package encoders;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

public final class EncoderFactory {
	private static HashMap<String,Constructor<?> > encoderMap = 
			new HashMap<String, Constructor<?>>();
	
	private EncoderFactory(){}
	
	public static void register(String encoderID,
			Constructor<?> encoderConstructor){
		encoderMap.put(encoderID, encoderConstructor);
	}
	
	public static Encoder create(String encoderID,
			Object... constructorArgs) throws Exception{
		if(!encoderMap.containsKey(encoderID))
			throw new UnsupportedClassVersionError(encoderID + " is not supported");
		return (Encoder) encoderMap.get(encoderID).newInstance(constructorArgs);
	}
	
	public static Set<String> getEncodersIDs(){
		return encoderMap.keySet();
	}
}
