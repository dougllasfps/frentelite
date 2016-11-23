package br.com.virilcorp.io.file.zip.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import br.com.virilcorp.io.file.zip.interfaces.IZipHandler;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public final class ZipHandler implements IZipHandler {

	private static final String EXECUTION_DIR = System.getProperty("user.dir");
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static int BUFFER_SIZE = 4096;

	// XXX CONSTRUCTOR
	public ZipHandler() {
	}

	@Override
	public List<File> unzipInCurrentDir(File zipFile, boolean markAsTemporary) throws ZipException, IOException {
		return unzip(zipFile, EXECUTION_DIR, markAsTemporary);
	}

	@Override
	public List<File> unzipInTempFolder(File zipFile, boolean markAsTemporary) throws ZipException, IOException {
		return unzip(zipFile, TEMP_DIR, markAsTemporary);
	}

	@Override
	public List<File> unzip(File zipFile, File destinationPath, boolean markAsTemporary) throws ZipException, IOException {
		return unzip(zipFile, destinationPath.getAbsolutePath(), markAsTemporary);
	}

	@Override
	public List<File> unzip(File zipFile, String destinationPath, boolean markAsTemporaryFiles) throws ZipException, IOException {
		if (zipFile == null) {
			throw new NullPointerException("null parameter zipFile");
		}
		
		List<File> extractedFilesList = new ArrayList<>();
		try {
			ZipFile zf = new ZipFile(zipFile);
			List<?> fileHeadersList = zf.getFileHeaders();
			if (fileHeadersList == null || fileHeadersList.isEmpty()) {
				throw new ZipException("Empty zip file");
			}

			for (int i = 0; i < fileHeadersList.size(); i++) {
				FileHeader fileHeader = (FileHeader) fileHeadersList.get(i);
				File extractedFile = extractFile(zf, fileHeader, destinationPath, markAsTemporaryFiles);
				if(!extractedFile.isDirectory()){
					extractedFilesList.add(extractedFile);
				}
			}
			
			return extractedFilesList;
		} catch (ZipException | IOException e) {
			throw e;
		} 
	}
	
	private File extractFile(ZipFile zipFile, FileHeader fileHeader, String destinationPath, boolean markAsTemporaryFiles) throws ZipException, IOException {
		InputStream is = null;
		OutputStream os = null;
		
		if (fileHeader == null) {
			throw new ZipException("Null zipHeader");
		}
		
		try{
			String outFilePath = destinationPath + FILE_SEPARATOR + fileHeader.getFileName();
			File outFile = new File(outFilePath);
			
			if(outFile.exists()){
				return outFile;
			}
			
			if (fileHeader.isDirectory()) {
				outFile.mkdirs();
				return outFile;
			}
			
			if(markAsTemporaryFiles){
				outFile.deleteOnExit();
			}

			File parentDir = outFile.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
			
			is = zipFile.getInputStream(fileHeader);
            os = new FileOutputStream(outFile);
            int readLen = -1;
            byte[] buff = new byte[BUFFER_SIZE];
            while ((readLen = is.read(buff)) != -1) {
                os.write(buff, 0, readLen);
            }
            
            //the order of close streams here is important ==> CRC checkings
            if (os != null) {
            	os.flush();
                os.close();
                os = null;
            }
            
            if (is != null) {
                is.close();
                is = null;
            }
            
            return outFile;
            
		} catch (ZipException | IOException e) {
			throw e;
		} finally {
			if(os != null){
				os.flush();
				IOUtils.closeQuietly(os);
			}
			
			if(is != null){
				IOUtils.closeQuietly(os);
			}
		}
	}

}
