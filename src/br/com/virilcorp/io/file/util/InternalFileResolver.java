package br.com.virilcorp.io.file.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class InternalFileResolver {

	//XXX CONSTRUCTOR
	private InternalFileResolver() { }
	
	public static File getInternalFileInstance(String internalBasePackage, String fileName) throws IOException {
		String cleanStr = internalBasePackage.replace(".", "/");
		cleanStr = cleanStr.replace("\\", "/");
		
		URL reference = InternalFileResolver.class.getClass().getResource("/" +cleanStr);
		if(reference == null){
			throw new IllegalArgumentException("cant locate internalBasePackage");
		}
		
		URI uri = null;
		try {
			String filePathStr = reference.toString() + "/" + fileName + ".zip";
			uri = new URI(filePathStr);
		} catch (URISyntaxException e){
			IOException ex = new IOException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
		
		File fileInstance = new File(uri);
		if(!fileInstance.exists()){
			throw new IOException("File not exists : " + fileInstance.getAbsolutePath());
		}
		
		return fileInstance;
	}
}