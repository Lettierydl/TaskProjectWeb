package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import otimizze.me.Facede;
import otimizze.me.model.Demanda;
import otimizze.me.model.Produto;

@ManagedBean(name = "demandaBean")
@ViewScoped
public class DemandaBean {
	
	private Facede f;
	private Demanda newDemanda;
	private int quantidade = 0;
	
	public DemandaBean(){
		newDemanda = new Demanda();
		f = new Facede();
	}
	
	public void addProdutoNaDemanda(Produto p){
		newDemanda.putProduto(p, Integer.valueOf(quantidade));
		quantidade = 0;
	}
	
	public void removerProdutoDaDemanda(Produto p){
		newDemanda.removeProdutoDaDemanda(p);
	}

	public List<Produto> getProdutosNaoUtilizados(){
		List<Produto> pro = f.getProdutos();
		for(Produto ut: newDemanda.getDemandaDeProdutos().keySet()){
			pro.remove(ut);
		}
		return pro;
	}
	
	public Demanda getNewDemanda() {
		return newDemanda;
	}

	public void setNewDemanda(Demanda newDemanda) {
		this.newDemanda = newDemanda;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	
	
	
	
}
