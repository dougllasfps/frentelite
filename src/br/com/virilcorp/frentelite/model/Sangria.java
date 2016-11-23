package br.com.virilcorp.frentelite.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(name="intervaloFluxo", parameters={ 
		@ParamDef(name = "dataInicial" , type = "java.util.Calendar"), 
		@ParamDef(name = "dataFinal" , type = "java.util.Calendar") 
})
@Filter(name = "intervaloFluxo", condition=" data_sangria between :dataInicial and :dataFinal ")
@Entity
@Table (schema = "ecf")
public class Sangria implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name ="data")
	private Calendar data;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_sangria")
	private Calendar dataSangria;
	
	@Column(name = "valor")
	private BigDecimal valor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Calendar getDataSangria() {
		return dataSangria;
	}

	public void setDataSangria(Calendar dataSangria) {
		this.dataSangria = dataSangria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dataSangria == null) ? 0 : dataSangria.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Sangria other = (Sangria) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (dataSangria == null) {
			if (other.dataSangria != null)
				return false;
		} else if (!dataSangria.equals(other.dataSangria))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
}