package br.com.virilcorp.io.system.util;

import org.apache.commons.lang3.SystemUtils;

import br.com.virilcorp.io.system.enums.OSAlias;
import br.com.virilcorp.io.system.exceptions.UnknowSystemException;

public final class SystemInfo {
	
	//XXX CONSTRUCTOR
	private SystemInfo() {	}
	
	//XXX METHODS
	public static boolean isWindows(){
		return SystemUtils.IS_OS_WINDOWS;
	}
	
	public static boolean isLinux(){
		return SystemUtils.IS_OS_LINUX;
	}
	
	public static boolean isUnix(){
		return SystemUtils.IS_OS_UNIX;
	}
	
	public static boolean isLinuxOrUnix(){
		return isLinux() || isUnix();
	}
	
	public static boolean isMac(){
		return SystemUtils.IS_OS_MAC;
	}
	
	public static boolean isJVM64Bits(){
		return SystemUtils.OS_ARCH.indexOf("64") != -1;
	}
	
	public static boolean isJVM32Bits(){
		return SystemUtils.OS_ARCH.indexOf("32") != -1;
	}
	
	public static OSAlias getOSAlias() throws UnknowSystemException{
		if(isJVM64Bits()){
			return determine64BitsOsAlias();
		} else if(isJVM32Bits()) {
			return determine32BitsOsAlias();
		} else {
			throw new UnknowSystemException("Unknow system arch");
		}
	}

	private static OSAlias determine64BitsOsAlias() throws UnknowSystemException {
		if(isWindows()){
			return OSAlias.WIN64;
		} else if(isLinux()){
			return OSAlias.LIN64;
		} else if(isUnix()){
			return OSAlias.UNI64;
		} else if(isMac()){
			return OSAlias.MAC64;
		} else {
			throw new UnknowSystemException("Unknow 64 bit OS");
		}
	}
	
	private static OSAlias determine32BitsOsAlias() throws UnknowSystemException {
		if(isWindows()){
			return OSAlias.WIN32;
		} else if(isLinux()){
			return OSAlias.LIN32;
		} else if(isUnix()){
			return OSAlias.UNI32;
		} else if(isMac()){
			return OSAlias.MAC32;
		} else {
			throw new UnknowSystemException("Unknow 32 bit OS");
		}
	}
}
