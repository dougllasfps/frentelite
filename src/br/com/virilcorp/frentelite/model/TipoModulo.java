package br.com.virilcorp.frentelite.model;

public enum TipoModulo {
	VENDA("VENDAS"),
	CONSULTA_PRODUTO("CONSULTA PRODUTO"),
	CADASTRO_PRODUTO("CADASTRO PRODUTO"),
	USUARIOS("USUARIOS"),
	RELATORIOS("RELATORIOS");
	
	private String desc;
	
	private TipoModulo(String desc) {
		this.desc = desc;
	}
	
	public String getDescricao() {
		return this.desc;
	}
}
