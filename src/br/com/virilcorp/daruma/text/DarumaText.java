package br.com.virilcorp.daruma.text;

import static br.com.virilcorp.daruma.text.DarumaTextMarker.CENTRALIZAR;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.CONDENSADO;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.DATA_ATUAL;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.ITALICO;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.NEGRITO;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.SALTO_LINHA;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.SINAL_SONORO;
import static br.com.virilcorp.daruma.text.DarumaTextMarker.SUBLINHADO;

import java.util.ArrayList;
import java.util.Collection;

import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.exception.DarumaImpressaoException;
import br.com.virilcorp.daruma.relatorio.cupom.CorpoCupom;
import br.com.virilcorp.daruma.relatorio.cupom.Cupom;
import br.com.virilcorp.daruma.relatorio.cupom.Rodape;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.DeliveryService;
import br.com.virilcorp.frentelite.service.VendaService;

public class DarumaText {

	private Collection<String> textList;
	
	public DarumaText() {
		this.textList = new ArrayList<>();
	}
	
	public void limparTexto(){
		this.textList.clear();
	}
	
	public static String linhas(int linhas){
		return DarumaTextMarker.SALTO_N_LINHAS.format(String.valueOf(linhas));
	}
	
	public static String preencherLinhaCom(char caractere){
		return DarumaTextMarker.PREENCHEDOR.format(String.valueOf(caractere));
	}
	
	public static String italico(String text){
		return ITALICO.format(text);
	}
	
	public static String fonteGrande(String text){
		return DarumaTextMarker.FONTE_GRANDE.format(text);
	}
	
	public static String normal(String text){
		return DarumaTextMarker.TEXTO_NORMAL.format(text);
	}
	
	public static String expandido(String text){
		return DarumaTextMarker.EXPANDIDO.format(text);
	}
	
	public static String guilhotina(){
		return DarumaTextMarker.GUILHOTINA.format(null);
	}
	
	public static String alinhadoDireita(String text){
		return DarumaTextMarker.ALINHAMENTO_DIREITA.format(text);
	}
	
	public static String dataAtual(){
		return DATA_ATUAL.format(null);
	}
	
	public static String condensado(String text){
		return CONDENSADO.format(text);
	}
	
	public static String centralizado(String text){
		return CENTRALIZAR.format(text);
	}
	
	public static String negrito(String text){
		return NEGRITO.format(text);
	}
	
	public static String sublinhado(String text){
		return SUBLINHADO.format(text);
	}
	
	public static String linha(String text){
		return SALTO_LINHA.format(text);
	}
	
	public static String tabulacao(String text){
		return DarumaTextMarker.TABULACAO.format(text);
	}
	
	public static String linha(){
		return SALTO_LINHA.format(null);
	}
	
	public static String sinalSonoro(){
		return SINAL_SONORO.format(null);
	}
	
	public Collection<String> build() {
		return textList;
	}
	
	public DarumaText add(String text){
		textList.add(text);
		return this;
	}
	
	public DarumaText add(DarumaText text){
		textList.addAll(text.build());
		return this;
	}
	
	public static void main(String[] args) throws DarumaImpressaoException {
		
		VendaService service = new VendaService();
		DeliveryService dService = new DeliveryService();
		Delivery delivery = dService.findById(1);
		
		CorpoCupom corpo = new CorpoCupom(delivery);
		Venda venda = corpo.getVenda();
		venda.setItensVenda(service.findItensVenda(venda));
		
		Rodape rodape = new Rodape();
		rodape.setData("01/01/2016");
		rodape.setFabricante("VirilCorp");
		rodape.setHora("11:44 hs");
		rodape.setLoja(1);
		rodape.setSaudacao("Volte sempre!!");
		rodape.setSistema("FrenteLite");
		
		Cupom cupom = new Cupom();
		cupom.setCorpo(corpo);
		cupom.setRodape(rodape);
		
		DarumaText text = DarumaDualService.getInstance().imprimirCupom(cupom);

		Collection<String> list = text.build();
		
		for (String string : list) {
			System.out.println(string);
		}
		
		System.exit(0);
	}
}