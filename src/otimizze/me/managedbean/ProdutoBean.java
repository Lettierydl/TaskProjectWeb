package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import otimizze.me.Facede;
import otimizze.me.model.Atividade;
import otimizze.me.model.Maquina;
import otimizze.me.model.Produto;
import otimizze.me.util.JSFUiUtil;

@ManagedBean(name = "produtoBean")
@SessionScoped
public class ProdutoBean {

	private Produto newProduto;
	private Atividade newAtividade;
	private List<String> listaMaquinasEscolhidas;
	private List<String> atividadesEscolhidas;
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
	
	public void adicionarAtividadeNoProduto(){
		for(String descricao: listaMaquinasEscolhidas){
			newAtividade.addMaquinas(f.getMaquina(descricao));
		}
		for(String id : atividadesEscolhidas){
			try {//nesta versão apenas a primeira atividade do produto pode preceder uma atividade base//ver isso depois
				newAtividade.addAtividadePrecessora(f.getAtividade(Integer.valueOf(id)));
			} catch (Exception e) {// atividade da lista de produto ainda nao salvo
				try {
					newAtividade.addAtividadePrecessora(newProduto.getSequenciaDeProducao().get(newProduto.getSequenciaDeProducao().size()-1));
				} catch (Exception e1) {
					JSFUiUtil.error(e.getMessage());return;
				}
			}
		}
		newProduto.addAtividadeASequenciaDeProducao(newAtividade);
		newAtividade = new Atividade();
		//refresh na lista de atividade que ta na pagina
	}
	
	public void removerAtividade(Atividade atividade){
		newProduto.removerAtividadeASequenciaDeProducao(atividade);
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
	
	public List<Atividade> getPossiveisAtividadesPrecessoras(){
		if(newProduto.getSequenciaDeProducao().isEmpty()){//so a primeira atividade por ter uma atividade base
			return f.getAtividadesBase();
		}else{
			return newProduto.getSequenciaDeProducao();
		}
	}
	
	public Atividade getNewAtividade() {
		return newAtividade;
	}

	public void setNewAtividade(Atividade newAtividade) {
		this.newAtividade = newAtividade;
	}


	public List<String> getListaMaquinasEscolhidas() {
		return listaMaquinasEscolhidas;
	}


	public void setListaMaquinasEscolhidas(List<String> listaMaquinasEscolhidas) {
		this.listaMaquinasEscolhidas = listaMaquinasEscolhidas;
	}


	public List<String> getAtividadesEscolhidas() {
		return atividadesEscolhidas;
	}


	public void setAtividadesEscolhidas(List<String> atividadesEscolhidas) {
		this.atividadesEscolhidas = atividadesEscolhidas;
	}

	
	
}
