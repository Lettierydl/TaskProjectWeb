package otimizze.me.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import otimizze.me.util.Persistencia;

@Table(name = "demanda")
@Entity
public class Demanda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="id_demanda")
	private int id;

	@Column
	private Date criacao;
	
	@Column
	private String observacao;
	
	@ElementCollection
	@Fetch(FetchMode.JOIN)
	@CollectionTable(name = "demanda_de_produtos", joinColumns =@JoinColumn(name="id_demanda",updatable = true))
	@MapKeyJoinColumn(name="id_produto")
	@Column(name="quantidade")
	private Map<Produto, Integer> demandaDeProdutos = new HashMap<Produto, Integer>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCriacao() {
		return criacao;
	}

	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Map<Produto, Integer> getDemandaDeProdutos() {
		return demandaDeProdutos;
	}
	
	public void putProduto(Produto p, int quantidade){
		getDemandaDeProdutos().put(p, quantidade);
	}
	
	public int removeProdutoDaDemanda(Produto p) {
		this.getDemandaDeProdutos().values();
		return this.demandaDeProdutos.remove(p);
	}
	
	public int getProduto(Produto p){
		return getDemandaDeProdutos().get(p);
	}
	
	public void setDemandaDeProdutos(Map<Produto, Integer> demandaDeProdutos) {
		this.demandaDeProdutos = demandaDeProdutos;
	}

	public static void salvar(Demanda d)  {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.persist(d);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void atualizar(Demanda d) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(d);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void remover(Demanda d) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove( Persistencia.em.getReference(d.getClass(), d.getId()) );
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((demandaDeProdutos == null) ? 0 : demandaDeProdutos
						.hashCode());
		result = prime * result + id;
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
		Demanda other = (Demanda) obj;
		if (demandaDeProdutos == null) {
			if (other.demandaDeProdutos != null)
				return false;
		} else if (!demandaDeProdutos.equals(other.demandaDeProdutos))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	

	
	
	
}
