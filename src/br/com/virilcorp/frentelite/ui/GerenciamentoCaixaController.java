package br.com.virilcorp.frentelite.ui;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.daruma.DarumaDualService;
import br.com.virilcorp.daruma.exception.DarumaImpressaoException;
import br.com.virilcorp.daruma.relatorio.gerenciamento.abertura.RelatorioAberturaService;
import br.com.virilcorp.daruma.relatorio.gerenciamento.fechamento.RelatorioFechamento;
import br.com.virilcorp.daruma.relatorio.gerenciamento.fechamento.RelatorioFechamentoService;
import br.com.virilcorp.daruma.text.DarumaText;
import br.com.virilcorp.frentelite.model.FluxoCaixa;
import br.com.virilcorp.frentelite.model.Sangria;
import br.com.virilcorp.frentelite.model.TipoFluxoCaixa;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.model.Venda;
import br.com.virilcorp.frentelite.service.DialogFactory;
import br.com.virilcorp.frentelite.service.FluxoCaixaService;
import br.com.virilcorp.frentelite.service.PagamentoService;
import br.com.virilcorp.frentelite.service.ResourceBundleService;
import br.com.virilcorp.frentelite.service.SangriaService;
import br.com.virilcorp.frentelite.service.VendaService;
import br.com.virilcorp.frentelite.util.MonetaryUtils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

public class GerenciamentoCaixaController extends Controller {
	
	private SangriaService sagriaService;
	private FluxoCaixaService fluxoCaixaService;
	private PagamentoService pagamentoService;
	private	VendaService vendaService;
	
	@Override
	public void postInitialize() {
		sagriaService 	  = new SangriaService();
		fluxoCaixaService = new FluxoCaixaService();
		pagamentoService  = new PagamentoService();
		vendaService	  = new VendaService();
	}
	
	public void handleNovaSangria(ActionEvent evt){
		try {
			FluxoCaixa abertura = fluxoCaixaService.findUltimaAberturaSemFechamento();
			
			if(abertura == null){
				addErrorMessage("msg.caixa_nao_aberto");
				return;
			}

			String mensagem = getBundleMessage( "msg.informar_valor" );
			BigDecimalMonetaryConverter converter = new BigDecimalMonetaryConverter();
			BigDecimal valor = DialogFactory.showInputMonetaryDialog( converter, mensagem );

			Calendar agora = Calendar.getInstance();
			
			if(valor == null || valor.compareTo(BigDecimal.ZERO) == 0){
				addErrorMessage("msg.valor_invalido");
				return;
			}

			BigDecimal valorAtualizado = MonetaryUtils.valueOrZero( pagamentoService.findValorDinheiroAtualizado(abertura.getDataAbertura(), agora) );
			
			if(valor.compareTo(valorAtualizado) != -1){
				addErrorMessage("msg.sangria_maior");
				return;
			}
			
			Sangria sangria = new Sangria();
			sangria.setData(agora);
			sangria.setDataSangria(agora);
			sangria.setValor(valor);
			sagriaService.save(sangria);
			
			addInfoMessage( "msg.sangria_sucesso" );
			
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}
	
	public void handleAberturaCaixa(ActionEvent evt) throws DarumaImpressaoException{
		
		Calendar hoje = Calendar.getInstance();
		hoje.setTimeInMillis(System.currentTimeMillis());
		FluxoCaixa abertura = fluxoCaixaService.findUltimaAberturaSemFechamento();
		
		if(abertura != null){
			addErrorMessage("msg.caixa_ja_aberto");
			return;
		}
		
		BigDecimalMonetaryConverter converter = new BigDecimalMonetaryConverter();
		String message = getBundleMessage("msg.informar_valor_abertura");
		BigDecimal valorAbertura = DialogFactory.showInputDialog( converter, message );
		
		if(valorAbertura == null){
			addErrorMessage("msg.valor_invalido");
			return;
		}
		
		boolean confirm = DialogFactory.showConfirmDialog( ResourceBundleService.getMessage("msg.confirma_acao") );
		
		if(!confirm){
			return;
		}
		
		Usuario usuario = getApplicationContext().getAtributoSessao("usuario");
		FluxoCaixa fluxo = new FluxoCaixa(hoje, TipoFluxoCaixa.ABERTURA, valorAbertura, usuario);
		
		try {
			fluxoCaixaService.save(fluxo);
			addInfoMessage("msg.caixa_aberto");
			
			Task<Void> task = new Task<Void>() {
				
				@Override
				protected Void call() throws Exception {
					DarumaText report = RelatorioAberturaService.montaTextoRelatorio(fluxo);
					DarumaDualService darumaDualService = DarumaDualService.getInstance();
					darumaDualService.imprimir(report);
					return null;
				}
			};
			
			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();
			
		} catch (RuntimeException  e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}

	public void handleFechamentoCaixa(ActionEvent evt){
		Calendar agora = Calendar.getInstance();
		FluxoCaixa abertura = fluxoCaixaService.findUltimaAberturaSemFechamento();
		
		if(abertura == null){
			addErrorMessage("msg.caixa_nao_aberto");
			return;
		}

		boolean confirm = DialogFactory.showConfirmDialog(ResourceBundleService.getMessage( "msg.confirma_acao") );
		
		if(!confirm){
			return;
		}
		
		Usuario usuario = getApplicationContext().getAtributoSessao("usuario");
		FluxoCaixa fechamento = new FluxoCaixa( agora, TipoFluxoCaixa.FECHAMENTO, null, usuario );
		
		abertura.setFechamento(fechamento);

		try {
			
			fluxoCaixaService.save(fechamento);
			fluxoCaixaService.update(abertura);
			addInfoMessage("msg.caixa_fechado");
			
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					RelatorioFechamento relatorio = new RelatorioFechamentoService().gerarRelatorio(abertura, agora);
					DarumaText report = RelatorioFechamentoService.montaTextoRelatorio(relatorio);
					DarumaDualService darumaDualService = DarumaDualService.getInstance();
					darumaDualService.imprimir(report);
					return null;
				}
			};

			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();
	

		} catch (RuntimeException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}

	public void handleRelatorioParcial(ActionEvent evt){
		Calendar agora = Calendar.getInstance();
		FluxoCaixa abertura = fluxoCaixaService.findUltimaAberturaSemFechamento();
		
		if(abertura == null){
			addErrorMessage("msg.caixa_nao_aberto");
			return;
		}

		try {
			
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					RelatorioFechamento relatorio = new RelatorioFechamentoService().gerarRelatorio(abertura, agora);
					DarumaText report = RelatorioFechamentoService
							.montaTextoRelatorio("======= RELATORIO PARCIAL ===========", relatorio);
					DarumaDualService darumaDualService = DarumaDualService.getInstance();
					darumaDualService.imprimir(report);
					return null;
				}
			};

			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();

		} catch (RuntimeException e) {
			addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}
	
	public void handleCancelamentoVenda(ActionEvent evt){
		FluxoCaixa abertura = fluxoCaixaService.findUltimaAberturaSemFechamento();
		
		if(abertura == null){
			addErrorMessage("msg.caixa_nao_aberto");
			return;
		}

		boolean confirm = DialogFactory.showConfirmDialog("msg.confirma_acao");
		
		if(!confirm){
			return;
		}
		
		Venda ultimaVenda = vendaService.findUltimaVenda(abertura.getDataAbertura(), Calendar.getInstance());
		
		if(ultimaVenda == null){
			addErrorMessage("msg.venda_vazia");
			return;
		}
		
		if(ultimaVenda.isCancelada()){
			addErrorMessage("msg.ultima_venda_cancelada");
			return;
		}

		try {
			ultimaVenda.setCancelada(true);
			vendaService.update(ultimaVenda);
			addInfoMessage("msg.sucess_cancelamento_venda");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("msg.error_default");
		}
	}
}