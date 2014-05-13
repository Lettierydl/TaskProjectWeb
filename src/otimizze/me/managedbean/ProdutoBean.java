package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import otimizze.me.Facede;
import otimizze.me.model.Atividade;
import otimizze.me.model.Maquina;
import otimizze.me.model.Produto;

@ManagedBean(name = "produtoBean")
@ViewScoped
public class ProdutoBean {

	private Produto newProduto;
	private Atividade newAtividade;
	private Facede f;
	
	public ProdutoBean(){
		newProduto = new Produto();
		newAtividade = new Atividade();
		f = new Facede();
	}

	
	public void cadastrarProduto(){
		f.cadastrarProduto(newProduto);
		newProduto = new Produto();
		newAtividade = new Atividade();
	}
	
	public void cadastrarAtividade(){
		f.cadastrarAtividade(newAtividade, newProduto);
		newAtividade = new Atividade();
		//refresh na lista de atividade que ta na pagina
	}
	
	public void removerAtividade(Atividade atividae){
		f.removerAtividade(atividae);
		//refresh na lista de atividade que ta na pagina
	}
	
	public Produto getNewProduto() {
		return newProduto;
	}

	public void setNewProduto(Produto newProduto) {
		this.newProduto = newProduto;
	}
	
	public List<Maquina> getMaquinas(){
		return f.getMaquinas();
	}
	
	public List<Produto> getProdutos(){
		return f.getProdutos();
	}
	
	public List<Atividade> getAtividadeDoNewProduto(){
		//dar um sort na lista se ela ainda nao tiver ordenada
		return newProduto.getSequenciaDeProducao();
	}
	
	public List<Atividade> getAtividadesBase(){
		return f.getAtividadesBase();
	}
	
	public Atividade getNewAtividade() {
		return newAtividade;
	}

	public void setNewAtividade(Atividade newAtividade) {
		this.newAtividade = newAtividade;
	}

	
	
}
