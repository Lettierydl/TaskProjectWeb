package otimizze.me;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import otimizze.me.model.Atividade;
import otimizze.me.model.Demanda;
import otimizze.me.model.Maquina;
import otimizze.me.model.Produto;
import otimizze.me.util.Persistencia;

public class TestFacede {

	Facede f;
	
	
	@Before
	public void iniciarTeste(){
		f = new Facede();
		Persistencia.limparBancoDeDados();
	}
	
	@After
	public void finalizarTestes(){
		//Persistencia.limparBancoDeDados();
	}
	
	private Maquina criarMaquina(String descricao) {
		Maquina m = new Maquina();
		m.setDescricao(descricao);
		return m;
	}
	
	private Produto criarProduto(String descricao) {
		Produto p = new Produto();
		p.setDescricao(descricao);
		return p;
	}
	
	private Atividade criarAtividadeBase() {
		Atividade a = new Atividade();
		a.setDescricao("Polir a materia prima");
		a.addMaquinas(criarMaquina("Maquina x"));
		a.setTempo(3);
		a.setSequencia(1);
		a.setQuantidadeDeProducao(300);
		a.setAtividadeBase(true);
		return a;
	}
	
	private Atividade criarAtividade() {
		Atividade a = new Atividade();
		a.setDescricao("Embalar Produto "+Math.random());
		a.addMaquinas(criarMaquina("Maquina x "+Math.random()));
		a.setTempo(3);
		a.setSequencia(2);
		a.setQuantidadeDeProducao(300);
		a.setAtividadeBase(false);
		return a;
	}
	
	private Demanda criarDemanda() {
		Demanda d = new Demanda();
		d.setCriacao(Calendar.getInstance());
		return d;
	}
	
	@Test
	public void testCadastrarMaquina() {
		Maquina m = criarMaquina("Maquina X");
		f.cadastrarMaquina(m);
		assertEquals("Nao salvou no banco", m, f.getMaquina("Maquina X"));
	}
	
	@Test
	public void testGetMaquinas() {
		testCadastrarMaquina();
		assertEquals("Nao esta recuperando do banco", 1, f.getMaquinas().size());
	}


	@Test
	public void testCadastrarAtividadeBase() {
		Atividade a = criarAtividadeBase();
		f.cadastrarAtividadeBase(a);
		assertEquals("Nao esta salvando a atividade", a, f.getAtividadesBase().get(0));	
	}
	
	@Test
	public void testGetAtividadesBase() {
		testCadastrarAtividadeBase();
		assertEquals("Nao esta recuperando a atividade", 1, f.getAtividadesBase().size());	
	}
	
	@Test
	public void testCadastrarAtividade() {
		Atividade a = criarAtividade();
		f.cadastrarAtividade(a, criarProduto("Produto zz"));
		assertEquals("Nao esta salvando a atividade", a, f.getAtividades().get(0));	
	}
	
	@Test
	public void testCadastrarAtividadeComMaquinaJaCriada() {
		Maquina m = criarMaquina("Maquina " + Math.random());
		f.cadastrarMaquina(m);
		Atividade a = criarAtividade();
		a.addMaquinas(m);
		f.cadastrarAtividadeBase(a);
		assertEquals("Nao esta salvando a atividade", a, f.getAtividades().get(0));	
	}
	
	@Test
	public void testCadastrarAtividadePrecessora() throws Exception {
		Atividade a = criarAtividade();
		Atividade a2 = criarAtividade();
		f.cadastrarAtividadeBase(a2);
		a.addAtividadePrecessora(a2);
		f.cadastrarAtividade(a, criarProduto("Produto zz"));
		assertEquals("Nao esta salvando a atividade precessora", a.getAtividadesPrecessoras().get(0), a2);
	}
	
	@Test
	public void testGetAtividades() {
		testCadastrarAtividade();
		assertEquals("Nao esta recuperando a atividade", 1, f.getAtividades().size());	
	}

	@Test
	public void testRemoverAtividade() {
		testCadastrarAtividade();
		Atividade a = f.getAtividades().get(0);
		Produto p = a.getProdutoDaAtividade();
		f.removerAtividade(a);
		assertEquals("Nao removeu a atividade", 0, f.getAtividades().size());
		assertEquals("Removeu o produto junto", p, f.getProduto(p.getDescricao()));
	}

	@Test
	public void testGetProdutos() {
		testCadastrarProduto();
		assertEquals("Nao esta recuperando o produto", 1, f.getProdutos().size());	
	
	}

	@Test
	public void testCadastrarProduto() {
		Produto p = criarProduto("Produto xx");
		f.cadastrarProduto(p);
		assertEquals("Nao esta salvando o produto", p, f.getProdutos().get(0));	
	}
	
	@Test
	public void testCadastrarProdutoComAtividade() {
		Produto p = criarProduto("Produto xx");
		Atividade a = criarAtividade();
		f.cadastrarMaquina(a.getMaquinas().get(0));
		Atividade a2 = criarAtividade();
		f.cadastrarMaquina(a2.getMaquinas().get(0));
		p.addAtividadeASequenciaDeProducao(a);
		p.addAtividadeASequenciaDeProducao(a2);
		f.cadastrarProduto(p);
		assertEquals("Nao esta salvando o produto", p, f.getProdutos().get(0));
		Assert.assertArrayEquals("Nao esta salvando o produto com as atividades", p.getSequenciaDeProducao().toArray(), f.getProdutos().get(0).getSequenciaDeProducao().toArray());
	}
	
	
	@Test
	public void testCriarDemanda() {
		Demanda d = criarDemanda();
		f.cadastrarDemanda(d);
		assertEquals("Nao esta salvando a demanda", d, f.getDemandas().get(0));
	}
	
	@Test
	public void testCriarDemandaComProduto() {
		Produto p = criarProduto("Produto Demanda");
		f.cadastrarProduto(p);
		Demanda d = criarDemanda();
		d.putProduto(p, 100);
		f.cadastrarDemanda(d);
		assertEquals("Nao esta salvando a demanda", d.getDemandaDeProdutos(), f.getDemandas().get(0).getDemandaDeProdutos());
	}
	
	@Test
	public void testCriarDemandaERemoverProdutoDaDemanda() {
		testCriarDemandaComProduto();
		Demanda d = f.getDemandas().get(0);
		Produto p = f.getProdutos().get(0);
		d.removeProdutoDaDemanda(p);
		f.atualizarDemanda(d);
		assertEquals("Nao esta salvando a alteracao na demanda", f.getDemandas().get(0).getDemandaDeProdutos().size(), 0 );
	}

	

}
