package br.com.virilcorp.frentelite.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileService {

	public static final String PICTURES_DIRECTORY = "src/produtos" + File.separator;
	public static final String CLASS_PATH_PICTURES_DIRECTORY = "/produtos" + File.separator;
	public static final int MAX_PICTURE_SIZE = 2097152; // 2MB

	public static void delete(String path) {
		File file = new File(path);
		if (file.exists())
			file.delete();
	}
	
	public static File openFileChooser(final Stage stage){
		FileChooser chooser = new FileChooser();
    	
    	FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("Imagens (*.PNG)", "*.png");
    	FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("Imagens (*.JPG)", "*.jpg");
    	
    	chooser.getExtensionFilters().add(jpg);
    	chooser.getExtensionFilters().add(png);
    	
    	return chooser.showOpenDialog(stage);
	}
	
	public static byte[] fileToByteArray(final File file) throws FileNotFoundException, IOException {
		byte[] byteArray  = null;
		
		try (InputStream is = new FileInputStream(file);) {
			byteArray = IOUtils.toByteArray(is);
		}
		
		return byteArray;
	}

	public synchronized static void copy(String fromPath, String toPath) throws FileNotFoundException, IOException {

		File arquivo = new File(fromPath);
		File novoArquivo = new File(toPath);

		FileInputStream is = new FileInputStream(arquivo);

		FileOutputStream os = new FileOutputStream(novoArquivo);

		byte[] bytes = new byte[1024];
		int count = 0;

		while ((count = is.read(bytes)) >= 0) {
			os.write(bytes, 0, count);
		}

		is.close();
		os.flush();
		os.close();
	}

	public static String getPathToRead(String nomeArquivo) {
		return CLASS_PATH_PICTURES_DIRECTORY + nomeArquivo;
	}

	public static String getPathToWrite(String nomeArquivo) {
		return PICTURES_DIRECTORY + nomeArquivo;
	}

	public static Image getJavaFXImage(byte[] rawPixels, int width, int height) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write((RenderedImage) createBufferedImage(rawPixels, width, height), "jpg", out);
			out.flush();
		} catch (IOException ex) {
		
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		return new javafx.scene.image.Image(in);
	}

	private static BufferedImage createBufferedImage(byte[] pixels, int width, int height) {
		SampleModel sm = getIndexSampleModel(width, height);
		DataBuffer db = new DataBufferByte(pixels, width * height, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		IndexColorModel cm = getDefaultColorModel();
		return new BufferedImage(cm, raster, false, null);
	}

	private static  SampleModel getIndexSampleModel(int width, int height) {
		IndexColorModel icm = getDefaultColorModel();
		WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
		SampleModel sampleModel = wr.getSampleModel();
		sampleModel = sampleModel.createCompatibleSampleModel(width, height);
		return sampleModel;
	}

	private static IndexColorModel getDefaultColorModel() {
		byte[] r = new byte[256];
		byte[] g = new byte[256];
		byte[] b = new byte[256];
		for (int i = 0; i < 256; i++) {
			r[i] = (byte) i;
			g[i] = (byte) i;
			b[i] = (byte) i;
		}
		IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
		return defaultColorModel;
	}
}
