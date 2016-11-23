package br.com.virilcorp.daruma.relatorio.cupom;

public class Rodape {

	private String operador;
	private String data;
	private String hora;
	private String saudacao;
	private String sistema;
	private String fabricante;
	private Integer loja;
	
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getSaudacao() {
		return saudacao;
	}
	public void setSaudacao(String saudacao) {
		this.saudacao = saudacao;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public Integer getLoja() {
		return loja;
	}
	public void setLoja(Integer loja) {
		this.loja = loja;
	}
}