package br.com.virilcorp.daruma;

public enum StatusImpressoraDual {
	
	DESLIGADA(0),
	ONLINE(1),
	ERRO(-27),
	OFFLINE(-50),
	SEM_PAPEL(-51),
	INICIALIZANDO(-52);
	
	private int status;
	
	private StatusImpressoraDual(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public static StatusImpressoraDual getStatus(int status){
		for (StatusImpressoraDual stat : values() ){
			if(status == stat.getStatus()){
				return stat;
			}
		}
		return null;
	}
}
