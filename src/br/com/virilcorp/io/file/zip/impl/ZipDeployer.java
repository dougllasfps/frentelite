package br.com.virilcorp.io.file.zip.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.virilcorp.io.file.util.InternalFileResolver;
import br.com.virilcorp.io.file.zip.interfaces.IZipDeployer;
import br.com.virilcorp.io.file.zip.interfaces.IZipHandler;
import net.lingala.zip4j.exception.ZipException;

public final class ZipDeployer implements IZipDeployer {
	
	private IZipHandler zipHandler;
	
	//XXX CONSTRUCTORS
	public ZipDeployer() {
		this.zipHandler = new ZipHandler();
	}

	//XXX OVERRIDE METHODS
	public List<File> deployZipOnTempFolder(File bundleFile, boolean markAsTemporary) throws IOException {
		try {
			return zipHandler.unzipInTempFolder(bundleFile, true);
		} catch (IOException e) {
			throw e;
		} catch (ZipException e){
			IOException ex = new IOException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}
	
	@Override
	public List<File> deployZip(File bundleFile, String destinationPath, boolean markAsTemporary) throws IOException {
		File destination = new File(destinationPath);
		return deployZip(bundleFile, destination, markAsTemporary);
	}
	
	@Override
	public List<File> deployZip(File bundleFile, File destination, boolean markAsTemporary) throws IOException {
		try {
			return zipHandler.unzip(bundleFile, destination, markAsTemporary);
		} catch (IOException e) {
			throw e;
		} catch (ZipException e){
			IOException ex = new IOException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}
	
	@Override
	public List<File> deployInternalZipOnTempFolder(String internalPackagePath, String fileName, boolean markAsTemporary) throws IOException {
		File zipFile = InternalFileResolver.getInternalFileInstance(internalPackagePath, fileName);
		return deployZipOnTempFolder(zipFile, markAsTemporary);
	}
	
	@Override
	public List<File> deployInternalZip(String internalPackagePath, String fileName, String destinationPath, boolean markAsTemporary) throws IOException {
		File zipFile = InternalFileResolver.getInternalFileInstance(internalPackagePath, fileName);
		return deployZip(zipFile, destinationPath, markAsTemporary);
	}
	
	@Override
	public List<File> deployInternalZip(String internalPackagePath, String fileName, File destinationPath, boolean markAsTemporary) throws IOException {
		File zipFile = InternalFileResolver.getInternalFileInstance(internalPackagePath, fileName);
		return deployZip(zipFile, destinationPath, markAsTemporary);
	}
	
	
	public static void main(String[] args) throws IOException {
		ZipDeployer zd = new ZipDeployer();
		zd.deployInternalZipOnTempFolder("br.com.virilcorp.fisco.impressao.natives.dll.urmetdaruma.win64", "files", true);
	}
}
