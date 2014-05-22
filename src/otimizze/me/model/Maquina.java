package otimizze.me.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import otimizze.me.util.Persistencia;

@Table(name = "maquina")
@Entity
public class Maquina implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
	@ManyToMany(mappedBy = "maquinas")
	private List<Atividade> possiveisAtividades;
	
	@Column(nullable = false, unique = true)
	private String descricao;

	
	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public List<Atividade> getPossiveisAtividades() {
		return possiveisAtividades;
	}

	public void addPossivelAtividade(Atividade possivelAtividade) {
		if(this.possiveisAtividades == null){
			this.possiveisAtividades = new ArrayList<Atividade>();
		}
		this.possiveisAtividades.add(possivelAtividade);
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
		Maquina other = (Maquina) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	public static void salvar(Maquina m)  {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.persist(m);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void atualizar(Maquina m) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(m);
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void remover(Maquina m) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove( Persistencia.em.getReference(m.getClass(), m.getId()) );
		}catch(Exception ex){
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	@Override
	public String toString() {
		return descricao + " Identificador: "+id;
	}
	
	
}
