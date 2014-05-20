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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import otimizze.me.util.Persistencia;

@Table(name = "atividade")
@Entity
public class Atividade implements Serializable, Comparable<Atividade> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private int sequencia;

	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name="atividades_maquinas",
		joinColumns = @JoinColumn(name = "atividade_id") ,
		inverseJoinColumns = @JoinColumn(name = "maquina_id"))
	private List<Maquina> maquinas;

	@Column(nullable = false)
	private int tempo;

	@Column
	private int quantidadeDeProducao;// quantidade de produto que sera produzido
										// quando for uma atividade base esse
										// valor varia e deve ser perguntado ao
										// usuario por que ele vai difinir o
										// tempo

	@Column(nullable = false)
	private boolean atividadeBase;// atividade sem produto

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Produto produtoDaAtividade;

	@OneToMany(cascade = CascadeType.DETACH)
	//@Fetch(FetchMode.SUBSELECT)
	private List<Atividade> atividadesPrecessoras;// atividades que precedem
													// esta atividade

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

	public List<Maquina> getMaquinas() {
		return maquinas;
	}

	public void setMaquinas(List<Maquina> maquinas) {
		this.maquinas = maquinas;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public int getQuantidadeDeProducao() {
		return quantidadeDeProducao;
	}

	public void setQuantidadeDeProducao(int quantidadeDeProducao) {
		this.quantidadeDeProducao = quantidadeDeProducao;
	}

	public int getSequencia() {
		return sequencia;
	}

	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}

	public boolean isAtividadeBase() {
		return atividadeBase;
	}

	public void setAtividadeBase(boolean atividadeDeProduto) {
		this.atividadeBase = atividadeDeProduto;
	}

	public List<Atividade> getAtividadesPrecessoras() {
		return atividadesPrecessoras;
	}

	public void addMaquinas(Maquina maquina) {
		if (this.maquinas == null) {
			this.maquinas = new LinkedList<Maquina>();
		}
		this.maquinas.add(maquina);
		maquina.addPossivelAtividade(this);
	}

	public void addAtividadePrecessora(Atividade precessora) throws Exception {
		if (this.atividadesPrecessoras == null) {
			this.atividadesPrecessoras = new LinkedList<Atividade>();
		}
		if (precessora.equals(this)) {
			throw new Exception("Loop de atividade");
		}
		this.atividadesPrecessoras.add(precessora);
	}

	public Produto getProdutoDaAtividade() {
		return produtoDaAtividade;
	}

	public void setProdutoDaAtividade(Produto produtoDaAtividade) {
		this.produtoDaAtividade = produtoDaAtividade;
	}

	@Override
	public int compareTo(Atividade o) {
		return Integer.compare(sequencia, o.sequencia);
	}

	public static void salvar(Atividade a) {
		persistirMaquinasNaoSalvas(a);
		Persistencia.iniciarTrascao();
		try {
			Persistencia.em.persist(a);
		} catch (Exception ex) {
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	private static void persistirMaquinasNaoSalvas(Atividade a) {
		for (Maquina m : a.getMaquinas()) {
			try {
				try {
					m = Finder.getMaquina(m.getDescricao());
				} catch (NoResultException nr) {
					Maquina.salvar(m);
				}
			} catch (Exception ex) {
				Persistencia.restartConnection();
			}
		}
	}

	public static void atualizar(Atividade a) {
		Persistencia.iniciarTrascao();
		try {
			Persistencia.em.merge(a);
		} catch (Exception ex) {
			ex.printStackTrace();
			Persistencia.restartConnection();
			return;
		}
		Persistencia.finalizarTrascao();
	}

	public static void remover(Atividade a) {
		Persistencia.iniciarTrascao();
		try {
			Persistencia.em.remove(Persistencia.em.getReference(a.getClass(),
					a.getId()));
		} catch (Exception ex) {
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
		result = prime * result + (atividadeBase ? 1231 : 1237);
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
		Atividade other = (Atividade) obj;
		if (atividadeBase != other.atividadeBase)
			return false;
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
