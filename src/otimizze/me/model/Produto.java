package otimizze.me.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import otimizze.me.util.Persistencia;

@Table(name = "produto")
@Entity
public class Produto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="id_produto")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String descricao;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "produtoDaAtividade" )
	private List<Atividade> sequenciaDeProducao;//sequencia de atividades para a produ����o deste produto
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public List<Atividade> getSequenciaDeProducao() {
		if(this.sequenciaDeProducao == null){
			this.sequenciaDeProducao = new LinkedList<Atividade>();
		}
		return sequenciaDeProducao;
	}
	
	
	public void addAtividadeASequenciaDeProducao(Atividade atividade) {
		if(this.sequenciaDeProducao == null){
			this.sequenciaDeProducao = new LinkedList<Atividade>();
		}
		this.sequenciaDeProducao.add(atividade);
		atividade.setProdutoDaAtividade(this);
	}
	
	public void removerAtividadeASequenciaDeProducao(Atividade atividade) {
		this.sequenciaDeProducao.remove(atividade);
	}
	
	public Atividade getAtividadeFinal() {
		Atividade u = getSequenciaDeProducao().get(0);
		for(Atividade a : getSequenciaDeProducao()){
			if(a.isAtividadePrecessora(u)){
				u = a;
			}
		}
		return u;
	}
	
	public static void salvar(Produto p)  {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.persist(p);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void atualizar(Produto p) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(p);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void remover(Produto p) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove( Persistencia.em.getReference(p.getClass(), p.getId()) );
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
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
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
		Produto other = (Produto) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
