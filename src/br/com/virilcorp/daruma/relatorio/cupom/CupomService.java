package br.com.virilcorp.daruma.relatorio.cupom;

import static br.com.virilcorp.daruma.text.DarumaText.alinhadoDireita;
import static br.com.virilcorp.daruma.text.DarumaText.centralizado;
import static br.com.virilcorp.daruma.text.DarumaText.preencherLinhaCom;
import static br.com.virilcorp.daruma.text.DarumaText.tabulacao;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.lang3.time.DateFormatUtils;

import br.com.virilcorp.converter.BigDecimalMonetaryConverter;
import br.com.virilcorp.converter.NumericConverter;
import br.com.virilcorp.daruma.text.DarumaText;
import br.com.virilcorp.frentelite.context.ApplicationContext;
import br.com.virilcorp.frentelite.model.Cliente;
import br.com.virilcorp.frentelite.model.Delivery;
import br.com.virilcorp.frentelite.model.ItemVenda;
import br.com.virilcorp.frentelite.model.Pagamento;
import br.com.virilcorp.frentelite.model.Usuario;
import br.com.virilcorp.frentelite.model.Venda;

public class CupomService {
	
	public static Cupom criarCupom(Venda venda){
		Cupom cupom = new Cupom();
		CorpoCupom corpo = new CorpoCupom();
		corpo.setVenda(venda);
		cupom.setCorpo(corpo);
		return cupom;
	}
	
	public static Cupom criarCupom(Delivery delivery){
		Cupom cupom = new Cupom();
		CorpoCupom corpo = new CorpoCupom(delivery);
		cupom.setCorpo(corpo);
		return cupom;
	}

	public static DarumaText montaCorpo(Cupom cupom){
		DarumaText text = new DarumaText();
		
		CorpoCupom corpo = cupom.getCorpo();
		
		if(corpo == null){
			return text;
		}
		
		if(corpo.getVenda() != null){
			montaVenda( text, corpo );
			montaPagamento( text, corpo, corpo.getVenda().getId() );
		}
		
		if(corpo.getDelivery() != null){
			montaDelivery(text, corpo);
		}
		
		text.add( preencherLinhaCom('-') );
		
		return text;
	}

	private static void montaDelivery(DarumaText text, CorpoCupom corpo) {
		Delivery delivery = corpo.getDelivery();
		
		if(delivery == null){
			return;
		}
		
		Cliente cliente = delivery.getCliente();
		
		text.add( centralizado("----------- Dados da Entrega ------------") ) ;
		text.add( tabulacao("Cliente: ")  + tabulacao(cliente.getNome() ) ) ;
		text.add( tabulacao("Endereço: ") +tabulacao(cliente.getEndereco() ));
		text.add( tabulacao("Telefone: ") + tabulacao(cliente.getTelefone() ));
		
		if(delivery.getTaxa() != null)
			text.add( tabulacao("Taxa Entrega: ") + tabulacao(new BigDecimalMonetaryConverter().toString(delivery.getTaxa()) ));
		
		if(delivery.getDetalhesPedido() != null){
			text.add( tabulacao("Detalhes do Pedido: ") + tabulacao(delivery.getDetalhesPedido()));
		}
	}

	private static void montaPagamento(DarumaText text, CorpoCupom corpo, Integer idVenda) {
		Pagamento pagamento = corpo.getVenda().getPagamento();
		
		if(pagamento != null){
			
			text.add(centralizado("=========  Pagamento ==========".toUpperCase()));
			
			if(pagamento.getValorCartao() != null && pagamento.getValorCartao().compareTo(BigDecimal.ZERO) != 0){
				String valorCartao = NumericConverter.formatCurrent(pagamento.getValorCartao());
				text.add(tabulacao("Cartão Crédito ") + tabulacao("R$ " +valorCartao));
			}
			
			if(pagamento.getValorDebito() != null && pagamento.getValorDebito().compareTo(BigDecimal.ZERO) != 0){
				String valorDebito = NumericConverter.formatCurrent(pagamento.getValorDebito());
				text.add(tabulacao("Cartão Débito ") + tabulacao("R$ " + valorDebito));
			}
			
			if(pagamento.getValorDinheiro() != null && pagamento.getValorDinheiro().compareTo(BigDecimal.ZERO) != 0){
				String valorDinheiro = NumericConverter.formatCurrent(pagamento.getValorDinheiro());
				text.add(tabulacao("Dinheiro ") + tabulacao("R$ " + valorDinheiro));
			}
		}
		
		if(idVenda != null){
			text.add(String.format(" Número Venda: %d " , idVenda) );
		}
	}

	private static void montaVenda(DarumaText text, CorpoCupom corpo) {
		
//		text.add(
//			  tabulacao(StringUtils.rightPad("N°", PADDING_ORDEM) )
//			+ tabulacao(StringUtils.rightPad(" CODIGO ", PADDING_COD_PRODUTO) )
//			+ tabulacao(StringUtils.rightPad(" PRODUTO ",  PADDING_DESC_PRODUTO))
//			+ tabulacao(" UND ")
//			+ tabulacao(" QTD ")
//			+ tabulacao(" TOTAL ")
//		);
		
		text.add(
				  tabulacao("N°")
//				 tabulacao(" CODIGO ")
				+ tabulacao(" PRODUTO ")
				+ tabulacao(" UND ")
				+ tabulacao(" QTD ")
				+ tabulacao(" TOTAL ")
			);
		
		text.add( preencherLinhaCom('-') );
		
		Collection<ItemVenda> itensVenda = corpo.getVenda().getItensVenda();
		
		int order = 1;
		
		for (ItemVenda itemVenda : itensVenda) {
			
//			String codigo = String.valueOf(itemVenda.getProduto().getCodigo());
//			codigo = StringUtils.leftPad(codigo, 6, '0');
			
			String descricao = String.valueOf(itemVenda.getProduto().getDescricao());
//			if(descricao.length() < PADDING_DESC_PRODUTO)
//				descricao = StringUtils.rightPad(descricao, PADDING_DESC_PRODUTO);
//			else if(descricao.length() > PADDING_DESC_PRODUTO)
//				descricao = descricao.substring(0, PADDING_DESC_PRODUTO);
			
			String valorUnid = NumericConverter.formatCurrent(itemVenda.getValorUnitario());
//			valorUnid = StringUtils.rightPad(valorUnid, PADDING_UNITARIO_PRODUTO);
			
			String quantidade = "x".concat(NumericConverter.formatWeight(itemVenda.getQuantidade()));
//			quantidade = StringUtils.rightPad(quantidade, PADDING_QUANTIDADE);
			
			String totalItem = NumericConverter.formatCurrent(itemVenda.getValorVenda());
			
			String linhaTabulada = tabulacao(String.valueOf(order)) +  tabulacao(descricao) + tabulacao(valorUnid) + tabulacao(quantidade) + tabulacao(totalItem);
			
			text.add( linhaTabulada );
			
			order++;
		}
		
		String totalVenda = "Total: R$ " + NumericConverter.formatCurrent(corpo.getVenda().getValorTotal() );
		
		text.add( preencherLinhaCom('-') );
		text.add( alinhadoDireita( totalVenda));

		if(corpo.getVenda().getValorDesconto() != null){
			String desconto = "Desconto: R$ " + NumericConverter.formatCurrent(corpo.getVenda().getValorDesconto() );
			text.add( alinhadoDireita( desconto ));
		}
		
	}
	
	public static DarumaText montaRodape(Cupom cupom){
		
		Venda venda = cupom.getCorpo().getVenda();
		
		Rodape r = new Rodape();
		r.setData(DateFormatUtils.format(venda.getDataVenda(), "dd/MM/yyyy"));
		r.setFabricante("Daruma");
		r.setHora(DateFormatUtils.format(venda.getDataVenda(), "HH:mm:ss"));
		r.setLoja(1);
		Usuario usuario = ApplicationContext.getInstance().getAtributoSessao("usuario");
		r.setOperador( usuario.getNome()  );
		r.setSaudacao("Volte Sempre!");
		r.setSistema("FrenteLite v1.0");
		
		cupom.setRodape(r);
		
		DarumaText text = new DarumaText();
		Rodape rodape = cupom.getRodape();
		
		if(rodape == null){
			return text;
		}
		
		text.add( centralizado(rodape.getSaudacao()) );
		text.add( (tabulacao("Data/Hora: ")) + tabulacao(rodape.getData()) + " " +(tabulacao(rodape.getHora())) );
		text.add( (tabulacao("Operador: ")) + tabulacao(rodape.getOperador() )) ;
		text.add( (tabulacao("Loja: " )) + tabulacao(String.valueOf(rodape.getLoja()) ));
		text.add( (tabulacao("Fabricante: ")) + tabulacao(rodape.getFabricante() )) ;
		text.add( (centralizado("Sistema: " + r.getSistema()))) ;
		
		text.add( DarumaText.linhas(3));
		
		return text;
	}
}