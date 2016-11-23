package br.com.virilcorp.frentelite.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FLUXO_CAIXA", schema = "ECF")
public class FluxoCaixa implements BaseModel{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Calendar data;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_abertura")
	private Calendar dataAbertura;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
	private TipoFluxoCaixa tipoFluxo;
	
	@Column(name = "fundo_caixa")
	private BigDecimal fundoCaixa;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@OneToOne
	@JoinColumn(name = "id_fechamento")
	private FluxoCaixa fechamento;
	
	public FluxoCaixa() {}
	
	public FluxoCaixa(Calendar data, TipoFluxoCaixa tipoFluxo, BigDecimal fundoCaixa, Usuario usuario) {
		super();
		this.data = data;
		this.dataAbertura = data;
		this.tipoFluxo = tipoFluxo;
		this.fundoCaixa = fundoCaixa;
		this.usuario = usuario;
	}
	@Override
	public String toString() {
		return "FluxoCaixa [id=" + id + ", data=" + data + ", tipoFluxo=" + tipoFluxo + ", fundoCaixa=" + fundoCaixa
				+ "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Calendar getData() {
		return data;
	}
	public Calendar getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(Calendar dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public TipoFluxoCaixa getTipoFluxo() {
		return tipoFluxo;
	}
	public void setTipoFluxo(TipoFluxoCaixa tipoFluxo) {
		this.tipoFluxo = tipoFluxo;
	}
	public BigDecimal getFundoCaixa() {
		return fundoCaixa;
	}
	public void setFundoCaixa(BigDecimal fundoCaixa) {
		this.fundoCaixa = fundoCaixa;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((fundoCaixa == null) ? 0 : fundoCaixa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tipoFluxo == null) ? 0 : tipoFluxo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluxoCaixa other = (FluxoCaixa) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (fundoCaixa == null) {
			if (other.fundoCaixa != null)
				return false;
		} else if (!fundoCaixa.equals(other.fundoCaixa))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipoFluxo != other.tipoFluxo)
			return false;
		return true;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public FluxoCaixa getFechamento() {
		return fechamento;
	}

	public void setFechamento(FluxoCaixa fechamento) {
		this.fechamento = fechamento;
	}
}