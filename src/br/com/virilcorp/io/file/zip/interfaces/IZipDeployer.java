package br.com.virilcorp.io.file.zip.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IZipDeployer {
	
	List<File> deployZipOnTempFolder(File bundleFile, boolean markAsTemporary) throws IOException;
	List<File> deployZip(File bundleFile, String destinationPath, boolean markAsTemporary) throws IOException;
	List<File> deployZip(File bundleFile, File destination, boolean markAsTemporary) throws IOException;

	List<File> deployInternalZipOnTempFolder(String internalPackagePath, String fileName, boolean markAsTemporary) throws IOException;
	List<File> deployInternalZip(String internalPackagePath, String fileName, String destinationPath, boolean markAsTemporary) throws IOException;
	List<File> deployInternalZip(String internalPackagePath, String fileName, File detinationPath, boolean markAsTemporary) throws IOException;
}
