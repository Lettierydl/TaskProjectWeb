package otimizze.me.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;

import otimizze.me.model.Atividade;
import otimizze.me.model.Demanda;
import otimizze.me.model.Finder;
import otimizze.me.model.Maquina;
import otimizze.me.model.Produto;


public class Persistencia {
	public static final String UNIDADE_DE_PERSISTENCIA = "TaskProjectPU";

	public static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory(UNIDADE_DE_PERSISTENCIA);
	public static EntityManager em = emf.createEntityManager();
	
	public static void iniciarTrascao(){
		restartConnection();
		em.getTransaction().begin();
	}
	
	public static void finalizarTrascao(){
		if(em.getTransaction().isActive()){
			em.getTransaction().commit();
		}
		em.close();
	}
	
	public static void rollBackTrascao(){
		em.getTransaction().rollback();
	}
	
	public static void restartConnection(){
		 try{
			 em.close();
		 }catch(Exception e){}
		 em = emf.createEntityManager();
	}
	
	
	public static void limparBancoDeDados(){
		List <Maquina> ma = Finder.getMaquinas();
		List<Produto> pr = Finder.getProdutos();
		List<Atividade> at = Finder.getAtividades();
		List<Demanda> de = Finder.getDemandas();
		iniciarTrascao();
		
		for(Maquina m : ma){
			em.remove(em.getReference(m.getClass(),
					m.getId()));
		}
		for(Produto p : pr){
			em.remove(em.getReference(p.getClass(),
					p.getId()));
		}
		for(Demanda d : de){
			em.remove(em.getReference(d.getClass(),
					d.getId()));
		}
		for(Atividade a : at){
			try{
				em.remove(em.getReference(a.getClass(),
					a.getId()));
			}catch(EntityNotFoundException ne){}
		}
		finalizarTrascao();
	}

	private static void executeNativeQuery(String sql) {
		iniciarTrascao();
		em.createNativeQuery(sql).executeUpdate();
		finalizarTrascao();
	}
	
	private static void executeQuery(String sql) {
		iniciarTrascao();
			try{
				em.createNativeQuery(sql).executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				finalizarTrascao();
			}
	}

	public static void flush() {
		iniciarTrascao();
		em.flush();
	}
	
}
