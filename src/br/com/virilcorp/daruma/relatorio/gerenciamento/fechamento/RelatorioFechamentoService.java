package br.com.virilcorp.daruma.relatorio.gerenciamento.fechamento;

import static br.com.virilcorp.daruma.text.DarumaText.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.exception.DarumaImpressaoException;
import br.com.virilcorp.daruma.relatorio.cupom.Cabecalho;
import br.com.virilcorp.daruma.relatorio.cupom.Rodape;
import br.com.virilcorp.daruma.text.DarumaText;
import br.com.virilcorp.frentelite.model.FluxoCaixa;
import br.com.virilcorp.frentelite.model.Sangria;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.PagamentoService;
import br.com.virilcorp.frentelite.service.SangriaService;
import br.com.virilcorp.frentelite.service.VendaService;
import br.com.virilcorp.frentelite.ui.DialogMessage;
import br.com.virilcorp.frentelite.util.MonetaryUtils;

public class RelatorioFechamentoService {

	private PagamentoService pagamentoService;
	private VendaService vendaService;
	private SangriaService sangriaService;
	
	public RelatorioFechamentoService() {
		pagamentoService = new PagamentoService();
		vendaService = new VendaService();
		sangriaService = new SangriaService();
	}

	public RelatorioFechamento gerarRelatorio(final FluxoCaixa abertura, final Calendar dataFechamento){
		
		RelatorioFechamento relatorio 	= new RelatorioFechamento();
		Calendar dataAbertura = abertura.getDataAbertura();
		
		try {
			CorpoRelatorioFechamento corpo 	= new CorpoRelatorioFechamento();
			
			List<Venda> vendasCanceladas = vendaService.findVendasCanceladas(dataAbertura, dataFechamento);
			Collection<Sangria> sangrias = sangriaService.findByData(dataAbertura, dataFechamento);
			
			BigDecimal valorDinheiroVendasValidas 	= pagamentoService.findValorDinheiroVendasValidas(dataAbertura, dataFechamento);
			BigDecimal valorCreditoVendasValidas 	= pagamentoService.findValorCreditoVendasValidas(dataAbertura, dataFechamento);
			BigDecimal valorDebitoVendasValidas 	= pagamentoService.findValorDebitoVendasValidas(dataAbertura, dataFechamento);
			
			corpo.setTotalDinheiro(valorDinheiroVendasValidas);
			corpo.setTotalCredito(valorCreditoVendasValidas);
			corpo.setTotalDebito(valorDebitoVendasValidas);
			
			corpo.setVendasCanceladas(vendasCanceladas);
			corpo.setSangrias(sangrias);
			
			corpo.setData(dataFechamento);

			BigDecimal totalSangrias = BigDecimal.ZERO;
			
			for (Sangria s : corpo.getSangrias()){
				totalSangrias = totalSangrias.add( MonetaryUtils.valueOrZero( s.getValor()) );
			}
			
			corpo.setTotalSangrias(totalSangrias);
			
			BigDecimal valorDinheiroNoCaixa = valorDinheiroVendasValidas.subtract(totalSangrias);
			
			corpo.setTotalDinheiroEmCaixa( abertura == null ? valorDinheiroNoCaixa :valorDinheiroNoCaixa.add(abertura.getFundoCaixa()));
			corpo.setFundoCaixa(valorDinheiroNoCaixa.add(abertura.getFundoCaixa()));
			
			Long countVendasValidas = vendaService.countVendasValidas(dataAbertura, dataFechamento);
			corpo.setQuantidadeVendas( countVendasValidas == null ? 0 : countVendasValidas.intValue() );
			
			DarumaDualService daruma = DarumaDualService.getInstance();
			Cabecalho cabecalhoDefault = daruma.criarCabecalhoDefault();
			Rodape rodapeDefault = daruma.criarRodapeDefault();

			relatorio.setCabecalho(cabecalhoDefault);
			relatorio.setCorpo(corpo);
			relatorio.setRodape(rodapeDefault);
			
		} catch ( DarumaImpressaoException e) {
			new DialogMessage().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao gerar relatorio para impressão.");
		}
		
		return relatorio;
	}

	public static DarumaText montaTextoRelatorio(RelatorioFechamento relatorio) {

		DarumaText cabecalho 	= DarumaDualService.montaCabecalho(relatorio.getCabecalho());
		DarumaText corpo 		= RelatorioFechamentoService.montaCorpo(relatorio.getCorpo());
		DarumaText rodape 		= DarumaDualService.montaRodape(relatorio.getRodape());
		
		return new DarumaText().add(cabecalho).add(corpo).add(rodape);
	}
	
	public static DarumaText montaTextoRelatorio(String titulo, RelatorioFechamento relatorio) {

		DarumaText cabecalho 	= DarumaDualService.montaCabecalho(relatorio.getCabecalho());
		DarumaText corpo 		= RelatorioFechamentoService.montaCorpo(titulo, relatorio.getCorpo());
		DarumaText rodape 		= DarumaDualService.montaRodape(relatorio.getRodape());
		
		return new DarumaText().add(cabecalho).add(corpo).add(rodape);
	}
	
	public static DarumaText montaCorpo(CorpoRelatorioFechamento corpo){
		return montaCorpo("====== FECHAMENTO DE CAIXA ======", corpo );
	}
	
	public static DarumaText montaCorpo(String titulo, CorpoRelatorioFechamento corpo){
		DarumaText text = new DarumaText();
		
		if(corpo == null){
			return text;
		}

		text.add( centralizado(titulo) );
		text.add( centralizado("EM " + DateTimeUtils.format(corpo.getData()) + " às " + DateTimeUtils.format(corpo.getData(), CalendarConverter.HOUR_PATTERN)  ));

		BigDecimalMonetaryConverter bigDecimalMonetaryConverter = new BigDecimalMonetaryConverter();
		
		text.add( centralizado("============ TOTALIZADORES ============="));
		text.add( ("VENDAS REALIZADAS: " + corpo.getQuantidadeVendas()) );
		text.add( ("DINHEIRO: R$ " + bigDecimalMonetaryConverter.toString(corpo.getTotalDinheiro()) ));
		text.add( ("CREDITO: R$ " + bigDecimalMonetaryConverter.toString(corpo.getTotalCredito()) ));
		text.add( ("DEBITO: R$ " + bigDecimalMonetaryConverter.toString(corpo.getTotalDebito()) ));
		
		if(corpo.getVendasCanceladas() == null || corpo.getVendasCanceladas().isEmpty()){
			text.add( "VENDAS CANCELADAS: 0 ");
		}else{
			for (Venda v : corpo.getVendasCanceladas()){
				text.add(
						  tabulacao("Nº " + v.getId())
						+ tabulacao(DateTimeUtils.format(v.getDataVenda(), CalendarConverter.HOUR_PATTERN))
						+ tabulacao(bigDecimalMonetaryConverter.toString(v.getValorTotal())) 
				);
			}
		}
		
		text.add( preencherLinhaCom('=') );
		text.add( centralizado("============ SANGRIAS ==============") );
		text.add( (" TOTAL: R$ " + bigDecimalMonetaryConverter.toString(corpo.getTotalSangrias())) );
		
		text.add( ("FUNDO DE CAIXA RESTANTE: R$ " +bigDecimalMonetaryConverter.toString(corpo.getFundoCaixa())) );
		
		
		text.add( preencherLinhaCom('-') );
		
		return text;
	}
}