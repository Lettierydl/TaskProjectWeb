package otimizze.me;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import otimizze.me.model.Atividade;
import otimizze.me.model.Demanda;
import otimizze.me.model.Maquina;
import otimizze.me.model.MetaAtividade;
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
		a.setDescricao("Polir a materia prima "+Math.random());
		a.addMaquinas(criarMaquina("Maquina x"+Math.random()));
		a.setTempo(3);
		a.setQuantidadeDeProducao(300);
		a.setAtividadeBase(true);
		return a;
	}
	
	private Atividade criarAtividade() {
		Atividade a = new Atividade();
		a.setDescricao("Embalar Produto "+Math.random());
		a.addMaquinas(criarMaquina("Maquina x "+Math.random()));
		a.setTempo(1);
		a.setQuantidadeDeProducao(250);
		a.setAtividadeBase(false);
		return a;
	}
	
	private Demanda criarDemanda() {
		Demanda d = new Demanda();
		d.setCriacao(Calendar.getInstance());
		return d;
	}
	
	private Demanda criarDemandaParaTeste(int quantidadeDeProduto) {
		Demanda d = criarDemanda();
		for(int i = 1;i <= quantidadeDeProduto;i++ ){
			Produto p = criarProduto("Produto "+i+Math.random());
			Atividade b1 = criarAtividadeBase();Atividade b2 = criarAtividadeBase();
			Atividade b3 = criarAtividadeBase();
			b3.setMaquinas(new ArrayList<Maquina>());
			b3.addMaquinas(b1.getMaquinas().get(0));
			Atividade a = criarAtividade();
			a.addMaquinas(b1.getMaquinas().get(0));
			a.addAtividadePrecessora(b1);a.addAtividadePrecessora(b2);
			f.cadastrarAtividadeBase(b3);
			b1.addAtividadePrecessora(b3);b2.addAtividadePrecessora(b3);
			f.cadastrarAtividadeBase(b1);
			f.cadastrarAtividadeBase(b2);
			
			
			//f.cadastrarProduto(p);
			f.cadastrarAtividade(a, p);
			p.addAtividadeASequenciaDeProducao(a);
			f.atualizarProduto(p);
			d.putProduto(p, 2*300);
		}
		f.cadastrarDemanda(d);
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
	public void testCadastrarAtividadePrecessora() {
		Atividade a = criarAtividade();
		Atividade a2 = criarAtividade();
		f.cadastrarAtividadeBase(a2);
		a.addAtividadePrecessora(a2);
		f.cadastrarAtividade(a, criarProduto("Produto zz"+Math.random()));
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
		Produto p = criarProduto("Produto Demanda"+Math.random());
		f.cadastrarProduto(p);
		Demanda d = criarDemanda();
		d.putProduto(p, 100);
		f.cadastrarDemanda(d);
		assertEquals("Nao esta salvando a demanda", d.getDemandaDeProdutos(), f.getDemanda(d.getId()).getDemandaDeProdutos());
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
	
	@Test
	public void testPossiveisMaquinasDaDemanda(){
		Atividade a = criarAtividade();
		a.addMaquinas(criarMaquina("Maquina x "+Math.random()));
		f.cadastrarAtividade(a, criarProduto("Produto zz"+Math.random()));
		Produto p = a.getProdutoDaAtividade();
		
		Demanda d = criarDemanda();
		d.putProduto(p, 90);
		f.cadastrarDemanda(d);
		
		Assert.assertArrayEquals("Nao esta recuperando as maquinas da demanda",a.getMaquinas().toArray(), f.getPossiveisMaquinasDaDemanda(d).toArray());
	}
	
	@Test
	public void testUnirDemandas(){
		testPossiveisMaquinasDaDemanda();testPossiveisMaquinasDaDemanda();
		testPossiveisMaquinasDaDemanda();
		Demanda d2 = f.getDemandas().get(1);
		Demanda d3 = f.getDemandas().get(0);
		d3.putProduto(d2.getProdutoDaDemanda().get(0), 121);
		f.atualizarDemanda(d3);
		
		Demanda d = Demanda.unirDemandas(f.getDemandasNaoCalculadas());
		f.cadastrarDemanda(d);
		assertEquals("Nao esta unindo as demandas", d ,f.getDemandas().get(0) );
	}
	
	@Test
	public void testCriarFluxoDeAtividade(){
		Demanda d = criarDemandaParaTeste(1);
		Produto p = d.getProdutoDaDemanda().get(0);
		List<MetaAtividade> met = d.calcularDemandaDeAtividadesDoProduto(p);
		Assert.assertEquals(met.size(), 4);
	}
	
	@Test
	public void testConstruirMatrizDeDemanda(){
		Demanda d = criarDemandaParaTeste(5);
		
		for(Produto p : d.getProdutoDaDemanda()){
			
			printarArvoreDeAtividade( p.getAtividadeFinal());
			
			System.out.println("___");
		}
		
		//mao unir atividades semelhantes, separar por demanda de produto// implementar uniao dessas atividades depois e deixar o usuario escolher
		//desenha um exemplo que se torma mais facil
		
		//saida de 10 atividades e 10 maquinas
		//uma matriz onde cada linha da matriz diz o par (numero da maquinaX, e quanta aquela atividade gasta na maquinaX)
		//ex: primeira linha da matriz 10X10
		// (4 88) (8 68) 6 94 5 99 1 67 2 89 9 77 7 99 0 86 3 92
		// essa linha diz que a primeira atividade na maquina 4 gasta 88 periodos (4 88)
		// e na maquina 8 gasta 68 periodos (8 68) ...
	}
	
	public void printarArvoreDeAtividade(Atividade at){
		System.out.print(at.getId()+" <- ");
		for(Atividade a : at.getAtividadesPrecessoras()){
			printarArvoreDeAtividade(a);
		}
	}

}
