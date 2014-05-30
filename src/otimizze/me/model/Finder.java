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
	
	public static Atividade getAtividade(int id) {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT a FROM Atividade AS a WHERE a.id = :aid",
						Atividade.class);
		q.setParameter("aid", id);
		Atividade atividade = (Atividade) q.getSingleResult();
		return atividade;
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

	public static List<Demanda> getDemandas() {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT d FROM Demanda AS d",
						Demanda.class);
		List<Demanda> demandas = q.getResultList();
		return demandas;
	}

	public static List<Demanda> getDemandasNaoCalculadas() {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT d FROM Demanda AS d WHERE d.isCalculada = false",
						Demanda.class);
		List<Demanda> demandas = q.getResultList();
		return demandas;
	}

	public static Demanda getDemanda(int id) {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createQuery(
						"SELECT d FROM Demanda AS d WHERE d.id = :idD",
						Demanda.class);
		q.setParameter("idD", id);
		Demanda demanda = (Demanda) q.getSingleResult();
		return demanda;
	}

	public static List<Maquina> getPossiveisMaquinasDaDemanda(Demanda n) {
		Persistencia.restartConnection();
		Query q = Persistencia.em.createNativeQuery("select maquina.id, maquina.descricao from maquina , atividade , atividades_maquinas, demanda_de_produtos "
				+ "where maquina.id = atividades_maquinas.maquina_id and atividades_maquinas.atividade_id = atividade.id and atividade.produtoDaAtividade_id_produto = demanda_de_produtos.id_produto and "
				+ "demanda_de_produtos.id_demanda = :idDemanda", Maquina.class);
		q.setParameter("idDemanda", n.getId());
		List<Maquina> maquinas = q.getResultList();
		return maquinas;
	}

	
	
	
	
}
