package br.com.virilcorp.frentelite.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.virilcorp.frentelite.exception.FotoGrandeException;
import br.com.virilcorp.frentelite.model.Foto;
import br.com.virilcorp.frentelite.model.FotoHolder;
import br.com.virilcorp.frentelite.ui.util.ImageViewHandler;
import javafx.scene.image.Image;

public class FotoService {
	
	public static final String EMPTY_PICTURE_LABEL = "Sem Foto, Clique para adicionar.";

	public static Foto createFoto(File file) throws FileNotFoundException, IOException, FotoGrandeException{
		
		if(file == null){
			throw new IllegalArgumentException("Arquivo de foto nulo.");
		}
		
		byte[] byteArray = FileService.fileToByteArray(file);
		
		if(byteArray.length > FileService.MAX_PICTURE_SIZE){
			throw new FotoGrandeException();
		}
		
		Foto foto = new Foto();
		foto.setBytes(byteArray);
		foto.setNome(file.getName());
		
		return foto;
	}
	
	public static void loadFoto(FotoHolder fotoHolder) throws IOException{
		if(fotoHolder == null || fotoHolder.getFoto() == null){
			throw new IllegalArgumentException("Sem foto.");
		}
		
		Foto foto = fotoHolder.getFoto();
		Image extractedImage = ImageViewHandler.extractImage(foto);
		fotoHolder.setImage(extractedImage);
	}
	
}
