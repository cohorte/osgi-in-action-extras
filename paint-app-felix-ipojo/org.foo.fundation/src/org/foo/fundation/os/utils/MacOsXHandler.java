package org.foo.fundation.os.utils;

import java.awt.Color;

/**
 * To load the AWT jni library : "libawt.jnilib"
 * 
 * Le probleme qui survient est un blocage du Thread qui charge la librairie
 * native "libawt.jnilib"
 * 
 * Avec le debogger on peut voir l'absolutePath de la librairie :
 * 
 * <pre>
 * /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Libraries/libawt.jnilib
 * </pre>
 * 
 * @author ogattaz (cohorte-technologies)
 * 
 */
class AWTLoader {

	/**
	 * Retrieves the RGB value of {@link Color#BLACK}. This call forces AWT
	 * libraries to be loaded and avoids access problems on Mac OS.
	 * 
	 * @return The RGB value of {@link Color#BLACK}
	 */
	public int getBlackRgb() {

		return Color.BLACK.getRGB();
	}
}

/**
 * 
 * @author ogattaz
 * 
 */
public class MacOsXHandler implements IOsHandler {

	/**
	 * 
	 */
	public MacOsXHandler() {
		super();
	}

	/**
	 * Special treatment for Mac OS X, if needed.
	 * 
	 * Loads the AWT library in the main thread.
	 * 
	 * os.name=[Mac OS X]
	 */
	@Override
	public void handleOs() {

		final String wOsName = System.getProperty("os.name").toLowerCase();

		if (!wOsName.contains("os x")) {
			// Not a Mac OS X host
			return;
		}

		// Load AWT
		final AWTLoader wAWTLoader = new AWTLoader();

		String wColorHexValue = Integer.toHexString(wAWTLoader.getBlackRgb());

		String wMessage = String.format("OsName=[%s] BlackColorHexValue=[%s]", wOsName, wColorHexValue);

		System.out.println(wMessage);
	}
}