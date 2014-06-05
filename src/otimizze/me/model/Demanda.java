package otimizze.me.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.Lob;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar criacao;
	
	@Column
	private boolean isCalculada;
	
	@Lob
	@Column(length=100000)
	private String observacao;
	
	@ElementCollection
	@Fetch(FetchMode.JOIN)
	@CollectionTable(name = "demanda_de_produtos", joinColumns = @JoinColumn(name="id_demanda",updatable = true))
	@MapKeyJoinColumn(name="id_produto")
	@Column(name="quantidade")
	private Map<Produto, Integer> demandaDeProdutos = new HashMap<Produto, Integer>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getCriacao() {
		return criacao;
	}

	public void setCriacao(Calendar criacao) {
		this.criacao = criacao;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isCalculada() {
		return isCalculada;
	}

	public void setCalculada(boolean isCalculada) {
		this.isCalculada = isCalculada;
	}

	public Map<Produto, Integer> getDemandaDeProdutos() {
		return demandaDeProdutos;
	}
	
	public List<Produto> getProdutoDaDemanda(){
		return new ArrayList<Produto>(this.getDemandaDeProdutos().keySet());
	}
	
	public void putProduto(Produto p, int quantidade){
		getDemandaDeProdutos().put(p, quantidade);
	}
	
	public int removeProdutoDaDemanda(Produto p) {
		this.getDemandaDeProdutos().values();
		return this.demandaDeProdutos.remove(p);
	}
	
	public int getQuantidade(Produto p){
		return getDemandaDeProdutos().get(p);
	}
	
	public void setDemandaDeProdutos(Map<Produto, Integer> demandaDeProdutos) {
		this.demandaDeProdutos = demandaDeProdutos;
	}
	
	public List<MetaAtividade> calcularDemandaDeAtividadesDoProduto(Produto p){
		Atividade f = p.getAtividadeFinal();
		List<MetaAtividade> sequencia = criarFluxoDeAtividade(f,0, new ArrayList<MetaAtividade>(), this.demandaDeProdutos.get(p));
		Collections.sort(sequencia);
		return sequencia;
	}

	private List<MetaAtividade> criarFluxoDeAtividade(Atividade ultima,int sequencia, List<MetaAtividade> l, int necessidadeDeProducao) {
		MetaAtividade m = new MetaAtividade();
		m.setAtividade(ultima);
		m.setSequencia(sequencia++);m.setNecessidadeDeProducao(necessidadeDeProducao);
		m.calculargNecessidadeDeTempo();
		m.setIdPredecessoras(new ArrayList<Integer>());
		if(l.contains(m)){
			return l;
		}
		l.add(m);
		for(Atividade pre: ultima.getAtividadesPrecessoras()){
			m.getIdPredecessoras().add(pre.getId());
			criarFluxoDeAtividade(pre, sequencia, l, necessidadeDeProducao);
		}
		return l;
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

	//Cuidado, esse metodo vai remover as demandas passadas na lista e nao salva a nova demanda
	public static Demanda unirDemandas(List<Demanda> demandas){
		Demanda d = new Demanda();
		d.setObservacao("Demanda Criada Apartir de outras demandas:\n");
		for (Demanda ds : demandas){
			for(Produto p : ds.getProdutoDaDemanda()){
				if(d.getDemandaDeProdutos().containsKey(p)){
					d.putProduto(p, ds.getQuantidade(p) + d.getQuantidade(p));
				}else{
					d.putProduto(p, ds.getQuantidade(p));
				}
			}
			d.setObservacao(d.getObservacao()+ds+"\n");
			Demanda.remover(ds);
		}
		return d;
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

	@Override
	public String toString() {
		return "Demanda criada em " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(criacao.getTime())  + ", observacao:" + observacao;
	}

	

	
	
	
}
