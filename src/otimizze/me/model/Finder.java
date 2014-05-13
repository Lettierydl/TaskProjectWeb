package otimizze.me.model;

import java.util.List;

import javax.persistence.Query;

import otimizze.me.util.Persistencia;

public final class Finder {

	
	
	public static List<Maquina> getMaquinas(){
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT m FROM Maquina AS m",
						Maquina.class);
		List<Maquina> maquinas = q.getResultList();
		return maquinas;
	}
	
	public static Maquina getMaquina(String descricao){
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT m FROM Maquina AS m WHERE m.descricao = :desc",
						Maquina.class);
		q.setParameter("desc", descricao);
		Maquina maquina = (Maquina) q.getSingleResult();
		return maquina;
	}
	
	public static List<Atividade> getAtividades() {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT a FROM Atividade AS a ",
						Atividade.class);
		List<Atividade> atividades = q.getResultList();
		return atividades;
	}
	
	public static List<Atividade> getAtividadesBase() {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT a FROM Atividade AS a WHERE a.atividadeBase = :atb",
						Atividade.class);
		q.setParameter("atb", true);
		List<Atividade> atividades = q.getResultList();
		return atividades;
	}

	public static List<Produto> getProdutos() {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT p FROM Produto AS p",
						Produto.class);
		List<Produto> produtos = q.getResultList();
		return produtos;
	}

	public static Produto getProduto(String descricao) {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT p FROM Produto AS p WHERE p.descricao = :desc",
						Produto.class);
		q.setParameter("desc", descricao);
		Produto produto = (Produto) q.getSingleResult();
		return produto;
	}
	
	
	
}
