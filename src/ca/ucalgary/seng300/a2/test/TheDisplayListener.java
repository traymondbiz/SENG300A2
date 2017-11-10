package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * Listens for events emanating from a display device.
 */
public class TheDisplayListener implements DisplayListener {
	String message = null;
	
	public TheDisplayListener(){
	}
	 
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// Nothing for now
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// Nothing for now
	}

	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		message = newMessage;
	}
	
	public String returnMsg(){
		return message;
	}
}
