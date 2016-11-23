package br.com.virilcorp.daruma;

import static br.com.virilcorp.daruma.text.DarumaText.centralizado;
import static br.com.virilcorp.daruma.text.DarumaText.preencherLinhaCom;
import static br.com.virilcorp.daruma.text.DarumaText.tabulacao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;

import org.jboss.logging.Logger;

import br.com.daruma.jna.DUAL;
import br.com.virilcorp.converter.CalendarConverter;
import br.com.virilcorp.converter.DateTimeUtils;
import br.com.virilcorp.daruma.exception.DarumaImpressaoException;
import br.com.virilcorp.daruma.relatorio.cupom.Cabecalho;
import br.com.virilcorp.daruma.relatorio.cupom.Cupom;
import br.com.virilcorp.daruma.relatorio.cupom.CupomService;
import br.com.virilcorp.daruma.relatorio.cupom.Rodape;
import br.com.virilcorp.daruma.text.DarumaText;
import br.com.virilcorp.fisco.impressao.enums.FabricanteImpressora;
import br.com.virilcorp.fisco.impressao.impl.ImpressoraDualProvider;
import br.com.virilcorp.fisco.impressao.interfaces.IImpressoraDual;
import br.com.virilcorp.frentelite.context.ApplicationContext;
import br.com.virilcorp.frentelite.model.Estabelecimento;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.service.EstabelecimentoService;
import gnu.io.CommPortIdentifier;

public class DarumaDualService implements IImpressoraDual {
	
	private static DarumaDualService instance;
	private boolean inicializada = false;
	
	private Estabelecimento estabelecimento;
	private EstabelecimentoService estabelecimentoService;
	
	public Estabelecimento getEstabelecimento() {
		if(estabelecimento == null)
			estabelecimento = estabelecimentoService.findFirst();
		return estabelecimento;
	}
	
	public DarumaDualService() throws DarumaImpressaoException{
		estabelecimentoService = new EstabelecimentoService();
		estabelecimento = getEstabelecimento();
		
//		inicializarImpressora();
	}
	
	public static DarumaDualService getInstance(){
		if(instance == null){
			try {
				instance = new ImpressoraDualProvider().getInstance( FabricanteImpressora.URMET_DARUMA );
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}

	public boolean isInicializada() {
		return inicializada;
	}
	
	@SuppressWarnings("rawtypes")
	public void inicializarImpressora() throws DarumaImpressaoException{
		if(!inicializada){

			Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier nextElement = null;
			Logger logger = Logger.getLogger(getClass());
			
			logger.info("INICIALIZANDO REGISTRO DA PORTA DA IMPRESSORA");
			
			while(!inicializada && portIdentifiers.hasMoreElements()){
				nextElement = (CommPortIdentifier) portIdentifiers.nextElement();
				int statusRegistro = DUAL.regPortaComunicacao(nextElement.getName()); 
				
				logger.info("Registro impressora, PORTA: " + nextElement.getName() + ", STATUS"+ statusRegistro);
				
				inicializada = StatusImpressoraDual.ONLINE.equals(getStatusImpressora());
			}
			
			if(!inicializada){
				throw new DarumaImpressaoException("Não foi possivel iniciar a Impressora.");
			}

			DUAL.regTabulacao("10");
			DUAL.regVelocidade("115200");
		}
	}
	
	public static DarumaText montaCabecalho(Cabecalho cabecalho){
		
		DarumaText text = new DarumaText();
		
		if(cabecalho == null){
			return text;
		}
		
		String razaoSocial = centralizado(cabecalho.getRazaoSocial());
		
		text.add(razaoSocial);
		
		if(cabecalho.getEndereco() != null){
			String endereco = centralizado(cabecalho.getEndereco());
			text.add(endereco);
		}
		
		if(cabecalho.getCidade() != null){
			String cidade = centralizado(cabecalho.getCidade());
			text.add(cidade);
		}
		
		if(cabecalho.getTelefone() != null){
			String tel = centralizado(cabecalho.getTelefone());
			text.add(tel);
		}
		
		if(cabecalho.getCnpj() != null){
			String cnpj = centralizado("CNPJ - " + cabecalho.getCnpj());
			text.add(cnpj);
		}

		text.add( preencherLinhaCom('-') );
		text.add( centralizado("DOCUMENTO SEM VALOR FISCAL"));
		text.add( preencherLinhaCom('-') );

		return text;
	}
	
	public static DarumaText montaRodape(Rodape rodape){
		DarumaText text = new DarumaText();
		
		if(rodape == null){
			return text;
		}
		
		text.add( centralizado(rodape.getSaudacao()) );
		text.add( (tabulacao("Data/Hora: ")) + tabulacao(rodape.getData()) + " " +(tabulacao(rodape.getHora())) );
		text.add( (tabulacao("Operador: ")) + tabulacao(rodape.getOperador() )) ;
		text.add( (tabulacao("Loja: " )) + tabulacao(String.valueOf(rodape.getLoja()) ));
		text.add( (tabulacao("Fabricante: ")) + tabulacao(rodape.getFabricante() )) ;
		text.add( (centralizado("Sistema: " + rodape.getSistema()))) ;
		
		text.add( DarumaText.linhas(3));
		
		return text;
	}
	
	public DarumaText imprimirCupom( final Cupom cupom ) throws DarumaImpressaoException {
		
		if(this.estabelecimento != null){
			Cabecalho cabecalho = criarCabecalhoDefault();
			cupom.setCabecalho(cabecalho);
		}
		
		DarumaText cabecalho = montaCabecalho(cupom.getCabecalho());
		DarumaText corpo = CupomService.montaCorpo(cupom);
		DarumaText rodape = CupomService.montaRodape(cupom);
		
		DarumaText text = new DarumaText()
				.add(cabecalho)
				.add(corpo)
				.add(rodape)
				.add(DarumaText.guilhotina());
		
		int result = imprimir( text );

		if(result != 1 ){
			StatusImpressoraDual statusImpressora = getStatusImpressora();
			throw new DarumaImpressaoException("Erro ao imprimir Texto: impressora "+ statusImpressora.name());
		}
		
		return text;
	}

	public Cabecalho criarCabecalhoDefault() {
		estabelecimento = getEstabelecimento();
		
		if(estabelecimento == null){
			return new Cabecalho();
		}
		
		Cabecalho cabecalho = new Cabecalho();
		cabecalho.setCidade(estabelecimento.getCidade());
		cabecalho.setCnpj(estabelecimento.getCnpj());
		cabecalho.setEndereco(estabelecimento.getEndereco());
		cabecalho.setRazaoSocial(estabelecimento.getRazaoSocial());
		cabecalho.setTelefone(estabelecimento.getTelefone());
		return cabecalho;
	}
	
	public Rodape criarRodapeDefault() {
		Rodape rodape = new Rodape();
		Calendar hoje = Calendar.getInstance();
		
		rodape.setData(DateTimeUtils.format(hoje));
		rodape.setHora(DateTimeUtils.format(hoje, CalendarConverter.HOUR_PATTERN));
		rodape.setLoja(01);
		
		Usuario usuario = ApplicationContext.getInstance().getAtributoSessao("usuario");
		
		if(usuario != null){
			rodape.setOperador(usuario.getNome());
		}
		
		rodape.setFabricante("Daruma");
		rodape.setSaudacao("Boas Vendas!");
		rodape.setSistema("FrenteLite v 1.0");
		return rodape;
	}
	
	public int imprimir(final DarumaText text){
		if(text == null){
			return 0;
		}
		return imprimir(text.build());
	}
	
	public int imprimir(final Collection<String> textList){
		if(textList == null){
			return 0;
		}
		
		int response = 0;
		
		for (String string : textList) {
			response =  DUAL.iImprimirTexto(string, 0);
		}
		
		return response;
	}
	
	public StatusImpressoraDual getStatusImpressora(){
		int rStatusImpressora = DUAL.rStatusImpressora();
		return StatusImpressoraDual.getStatus(rStatusImpressora);
	}
	
}