package br.com.virilcorp.fisco.impressao.abstractions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.fisco.impressao.enums.FabricanteImpressora;
import br.com.virilcorp.fisco.impressao.interfaces.IImpressoraDual;
import br.com.virilcorp.fisco.impressao.interfaces.IImpressoraProvider;
import br.com.virilcorp.io.file.zip.impl.ZipDeployer;
import br.com.virilcorp.io.file.zip.interfaces.IZipDeployer;
import br.com.virilcorp.io.system.util.SystemInfo;

public abstract class AbstractImpressoraDualProvider implements IImpressoraProvider {

	//XXX PROPERTIES
	private final IZipDeployer zipDeployer;
	private final Map<FabricanteImpressora, List<File>> deployedFilesMap;
	
	protected AbstractImpressoraDualProvider() {
		this.zipDeployer = new ZipDeployer();
		this.deployedFilesMap = new LinkedHashMap<>();
	}

	//XXX OVERRIDE METHODS
	@SuppressWarnings("unchecked")
	@Override
	public < I extends IImpressoraDual> I  getInstance(FabricanteImpressora fabImp) throws IOException {
		if(!isDeployed(fabImp)){
			deploy(fabImp);
		}
		
		if(fabImp.equals(FabricanteImpressora.URMET_DARUMA)){
			return (I) new DarumaDualService();
		}
		
		return null;
	}
	
	protected abstract IImpressoraDual createInstance(FabricanteImpressora fabImp) throws IOException;

	//XXX METHODS
	private boolean isDeployed(FabricanteImpressora fabImp){
		return deployedFilesMap.containsKey(fabImp);
	}
	
	public void deploy(FabricanteImpressora fabImp) throws IOException {
		if(isDeployed(fabImp)){
			return;
		}
		
		List<File> deployedFiles = deployFiles(fabImp);
		registerDeployedFilesAsLibraries(fabImp, deployedFiles);			
	}
	
	private List<File> deployFiles(FabricanteImpressora fabImp) throws IOException {
		String basePath = "br/com/virilcorp/fisco/impressao/natives/dll/";
		String finalPath = basePath + fabImp.getInternalPackagePath() + File.separator + SystemInfo.getOSAlias().getAlias();
		return zipDeployer.deployInternalZip(finalPath, "files", "" , false);
	}
	
	private void registerDeployedFilesAsLibraries(FabricanteImpressora fabImp, List<File> deployedFilesList) throws IOException {
		List<File> alreadyDeployedFilesList = deployedFilesMap.get(fabImp);
		if(alreadyDeployedFilesList == null){
			alreadyDeployedFilesList = new ArrayList<>();
		}
		
		List<File> lista = new ArrayList<>();
		
		for (File depFile : deployedFilesList) {
			if(!alreadyDeployedFilesList.contains(depFile)){
				System.load(depFile.getAbsolutePath());
				lista.add(depFile);
			}
		}
		
		deployedFilesList.addAll(lista);
		
		deployedFilesMap.put(fabImp, deployedFilesList);
	}
	
}
