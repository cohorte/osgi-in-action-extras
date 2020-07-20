package org.foo.fundation.gogo;

import org.foo.fundation.ELogLevel;
import org.foo.fundation.ILogger;
import org.foo.fundation.UtilsArray;

/**
 * 
 * @author ogattaz (cohorte-technologies)
 *
 */
public abstract class GoGoCommand {

	public abstract ILogger getLogger();

	/**
	 * One of Gogo’s most simplifying features: System.out is the preferable way to
	 * create output.
	 * 
	 * However, Gogo uses Threadio, which is a service that multiplexes System.out
	 * and System.err (and also System.in). Each thread is associated with its own
	 * triplet of streams. So as long as you print to sysout inside a command then
	 * any Gogo user will get the information even if they run the shell remotely.
	 * 
	 * @ see http://enroute.osgi.org/appnotes/gogo-cmd.html #Console Output
	 * 
	 * 
	 * @param aLevel
	 * @param aWhat
	 * @param format
	 * @param args
	 */
	public void log(final ELogLevel aLevel, final String aWhat, final String format, final Object... args) {
		String wLine = String.format(format, args);
//		@SuppressWarnings("resource")
//		PrintStream wGoGoStream = (ELogLevel.ERROR.equals(aLevel)) ? System.err : System.out;
//		wGoGoStream.println(wLine);
		getLogger().log(aLevel.getValue(), this, aWhat, wLine);
	}

	/**
	 * @param aWhat
	 * @param format
	 * @param args
	 */
	public void logInfo(final String aWhat, final String format, final Object... args) {
		log(ELogLevel.INFO, aWhat, format, args);
	}

	/**
	 * @param aWhat
	 * @param format
	 * @param args
	 */
	public void logSevere(final String aWhat, final String format, final Object... args) {
		log(ELogLevel.SEVERE, aWhat, format, args);
	}

	/**
	 * @param aWhat
	 * @param format
	 * @param args
	 */
	public void logSevere(final String aWhat, final Throwable aThrowable) {
		log(ELogLevel.SEVERE, aWhat, "ERROR: %s", aThrowable);
	}

	/**
	 * @param aWhat
	 * @param aThrowable
	 * @param format
	 * @param args
	 */
	public void logSevere(final String aWhat, final Throwable aThrowable, final String format,
			final Object... args) {
		log(ELogLevel.SEVERE, aWhat, format + " ERROR: %s", UtilsArray.appendOneObject(args, aThrowable));
	}

	/**
	 * @param aWhat
	 * @param format
	 * @param args
	 */
	public void logTwiceWarn(final String aWhat, final String format, final Object... args) {
		log(ELogLevel.WARNING, aWhat, format, args);
	}
}
