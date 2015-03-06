package inputHandlers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

public class InputHandlerFactory {
	private static HashMap<String,Constructor<?> > inputHandlerMap = 
			new HashMap<String, Constructor<?>>();
	
	private InputHandlerFactory(){}
	
	public static void register(String inputHandlerID,
			Constructor<?> inputHandlerConstructor){
		inputHandlerMap.put(inputHandlerID, inputHandlerConstructor);
	}
	
	public static InputHandler create(String encoderID,
			Object... constructorArgs) throws Exception{
		if(!inputHandlerMap.containsKey(encoderID)) return null;
		return (InputHandler) inputHandlerMap.get(encoderID).newInstance(constructorArgs);
	}
	
	public static Set<String> getEncodersIDs(){
		return inputHandlerMap.keySet();
	}
}
