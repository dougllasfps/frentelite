package br.com.virilcorp.frentelite.ui.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.com.virilcorp.frentelite.model.Foto;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageViewHandler {

	public static Image extractImage(byte[] bytes) throws IOException {
		try(ByteArrayInputStream is = new ByteArrayInputStream(bytes);){
			BufferedImage im = ImageIO.read(is);
			return SwingFXUtils.toFXImage(im, null);
		}
	}
	
	public static Image extractImage(Foto foto) throws IOException {
		if(foto == null || foto.getBytes() == null)
			throw new IllegalArgumentException("Foto inválida.");
		return extractImage(foto.getBytes());
	}
}
