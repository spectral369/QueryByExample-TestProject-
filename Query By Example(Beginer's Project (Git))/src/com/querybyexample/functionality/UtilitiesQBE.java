/**
 * 
 */
package com.querybyexample.functionality;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * @author spectral369
 *
 */
public class UtilitiesQBE {
	private static FileHandler fh;
	public static boolean isLogAcctive = true;

	public static synchronized Logger getLogger(Object obj) throws SecurityException,
			IOException {
		if(isLogAcctive){
		final Logger log = Logger.getLogger(obj.toString());
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.FINER);
		Logger.getAnonymousLogger().addHandler(consoleHandler);
		log.setLevel(Level.FINER);
		fh = new FileHandler("Log.log", true);
		fh.setFormatter(new XMLFormatter());
		log.addHandler(fh);
		return log;
		}else return null;
	}

}
