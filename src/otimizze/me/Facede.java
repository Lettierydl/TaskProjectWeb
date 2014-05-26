package otimizze.me;

import java.util.List;

import otimizze.me.model.Atividade;
import otimizze.me.model.Demanda;
import otimizze.me.model.Finder;
import otimizze.me.model.Maquina;
import otimizze.me.model.Produto;

public class Facede {

	public List<Maquina> getMaquinas() {
		return Finder.getMaquinas();
	}

	public Maquina getMaquina(String descricao){
		return Finder.getMaquina(descricao);
	}
	
	public void cadastrarMaquina(Maquina newMaquina) {
		Maquina.salvar(newMaquina);
	}
	
	public void atualizarMaquina(Maquina m) {
		Maquina.atualizar(m);
	}
	
	public List<Atividade> getAtividades() {
		return Finder.getAtividades();
	}
	
	public List<Atividade> getAtividadesBase() {
		return Finder.getAtividadesBase();
	}
	
	public Atividade getAtividade(int id) {
		return Finder.getAtividade(id);
	}
	
	public void cadastrarAtividade(Atividade newAtividade, Produto newProduto) {
		newAtividade.setProdutoDaAtividade(newProduto);
		Atividade.salvar(newAtividade);
	}
	
	public void cadastrarAtividadeBase(Atividade newAtividade) {
		newAtividade.setAtividadeBase(true);
		Atividade.salvar(newAtividade);
	}
	
	public void removerAtividade(Atividade atividae) {
		Atividade.remover(atividae);
	}
	
	public void atualizarAtividade(Atividade a) {
		Atividade.atualizar(a);
	}

	public List<Produto> getProdutos() {
		return Finder.getProdutos();
	}
	
	public Produto getProduto(String descricao) {
		return Finder.getProduto(descricao);
	}
	
	public void cadastrarProduto(Produto newProduto) {
		Produto.salvar(newProduto);
	}
	
	public void atualizarProduto(Produto p) {
		Produto.atualizar(p);
	}

	public void cadastrarDemanda(Demanda d) {
		Demanda.salvar(d);
	}
	
	public List<Demanda> getDemandas() {
		return Finder.getDemandas();
	}

	public void atualizarDemanda(Demanda d) {
		Demanda.atualizar(d);
	}


}
