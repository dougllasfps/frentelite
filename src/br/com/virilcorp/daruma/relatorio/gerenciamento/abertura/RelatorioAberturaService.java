package br.com.virilcorp.daruma.relatorio.gerenciamento.abertura;

import static br.com.virilcorp.daruma.text.DarumaText.alinhadoDireita;
import static br.com.virilcorp.daruma.text.DarumaText.centralizado;
import static br.com.virilcorp.daruma.text.DarumaText.preencherLinhaCom;

import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.relatorio.cupom.Cabecalho;
import br.com.virilcorp.daruma.relatorio.cupom.Rodape;
import br.com.virilcorp.daruma.text.DarumaText;
import br.com.virilcorp.frentelite.model.FluxoCaixa;

public class RelatorioAberturaService {
	
	public static DarumaText montaTextoRelatorio(FluxoCaixa aberturaCaixa){
		RelatorioAbertura relatorio = montaRelatorio(aberturaCaixa);
		
		DarumaText cabecalho = DarumaDualService.montaCabecalho(relatorio.getCabecalho());
		DarumaText corpo 	 = montaCorpo(relatorio.getCorpo());
		DarumaText rodape 	 = DarumaDualService.montaRodape(relatorio.getRodape());
		
		DarumaText text = new DarumaText().add( cabecalho ).add( corpo ).add( rodape );
		return text;
	}

	public static RelatorioAbertura montaRelatorio(FluxoCaixa aberturaCaixa){
		
		DarumaDualService daruma = DarumaDualService.getInstance();

		RelatorioAbertura relatorio = new RelatorioAbertura();
		Rodape rodape = daruma.criarRodapeDefault();
		Cabecalho criarCabecalhoDefault = daruma.criarCabecalhoDefault();
		
		CorpoRelatorioAbertura corpoRelatorioAbertura = new CorpoRelatorioAbertura();
		corpoRelatorioAbertura.setAbertura(aberturaCaixa);

		relatorio.setCabecalho(criarCabecalhoDefault);
		relatorio.setCorpo(corpoRelatorioAbertura);
		relatorio.setRodape(rodape);
		
		return relatorio;
	}
	
	public static DarumaText montaCorpo(CorpoRelatorioAbertura corpo){
		DarumaText text = new DarumaText();
		
		if(corpo == null){
			return text;
		}
		
		FluxoCaixa abertura = corpo.getAbertura();
		
		text.add( centralizado("====== ABERTURA DE CAIXA ======") );
		text.add( centralizado("EM " + DateTimeUtils.format(abertura.getData()) + " às " + DateTimeUtils.format(abertura.getDataAbertura(), CalendarConverter.HOUR_PATTERN)  ));
		text.add( alinhadoDireita(new BigDecimalMonetaryConverter().toString(abertura.getFundoCaixa())));
		
		text.add( preencherLinhaCom('-') );
		
		return text;
	}
}
