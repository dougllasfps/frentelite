package br.com.virilcorp.daruma.text;

public enum DarumaTextMarker {
	
	SALTO_LINHA("<l></l>",null),
	
	CENTRALIZAR("<ce>","</ce>"),
	
	NEGRITO("<b>","</b>"),
	
	ITALICO("<i>","</i>"),
	
	SUBLINHADO("<s>","</s>"),
	
	SINAL_SONORO("<sn>","</sn>"),
	
	TEXTO_NORMAL("<n>","</n>"),
	
	CONDENSADO("<c>","</c>"),
	
	EXPANDIDO("<e>","</e>"),
	
	DATA_ATUAL("<dt></dt>", null),
	
	HORA_ATUAL("<hr></hr>",null),	
	
	ALINHAMENTO_DIREITA("<ad>","</ad>"),
	
	TABULACAO("<tb>","</tb>"),
	
	ESPACO("<sp></sp>",  null),
	
	FONTE_GRANDE("<xl>","</xl>"),
	
	GUILHOTINA("<gui></gui>", null),
	
	PREENCHEDOR("<tc>","</tc>"),
	
	SALTO_N_LINHAS("<sl>", "</sl>")

	;
	
	private String marker;
	private String closeMarker;

	private DarumaTextMarker(String marker, String closeMarker) {
		this.marker = marker;
		this.closeMarker = closeMarker;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public String getCloseMarker() {
		return closeMarker;
	}
	
	public String format(String text){
		if(text == null){
			text = "";
		}
		
		if(TABULACAO.equals(this)){
			return text + this.getMarker();
		}
		
		if(this.getCloseMarker() == null){
			return this.getMarker() + text;
		}
		
		return this.getMarker() + text + this.getCloseMarker();
	}
}
