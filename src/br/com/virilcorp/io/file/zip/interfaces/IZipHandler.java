package br.com.virilcorp.io.file.zip.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;

public interface IZipHandler {
	
	List<File> unzipInCurrentDir(File zipFile,boolean markAsTemporary) throws ZipException, IOException;
	List<File> unzipInTempFolder(File zipFile, boolean markAsTemporary) throws ZipException, IOException;
	List<File> unzip(File zipFile, String destinationPath, boolean markAsTemporary) throws ZipException, IOException;
	List<File> unzip(File zipFile, File destinationPath, boolean markAsTemporary) throws ZipException, IOException;
	
	//TODO ZIP METHODS -> UNECESSARY RIGHT NOW (21/05/16)
	
}
