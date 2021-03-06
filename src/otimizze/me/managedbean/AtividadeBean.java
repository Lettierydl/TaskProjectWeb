package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import otimizze.me.Facede;
import otimizze.me.model.Atividade;
import otimizze.me.model.Maquina;
import otimizze.me.util.JSFUiUtil;
import otimizze.me.util.Persistencia;

@ManagedBean(name = "atividadeBean")
@ViewScoped
// vai ser usada para cadastrar atividades base/sem produtos(Ex: iniciar materia prima)
public class AtividadeBean {
	
	private Atividade newAtividade;
	private Facede f;
	private List<String> listaMaquinasEscolhidas;
	private List<String> atividadesEscolhidas;
	
	public AtividadeBean(){
		newAtividade = new Atividade();
		f = new Facede();
	}
	
	public void cadastrarAtividade(){
		for(String descricao: listaMaquinasEscolhidas){
			newAtividade.addMaquinas(f.getMaquina(descricao));
		}
		for(String id : atividadesEscolhidas){
			try {
				newAtividade.addAtividadePrecessora(f.getAtividade(Integer.valueOf(id)));
			} catch (Exception e) {
				JSFUiUtil.error(e.getMessage());return;
			}
		}
		f.cadastrarAtividadeBase(newAtividade);
		newAtividade = new Atividade();
	}
	
	public List<Atividade> getAtividadesBase(){
		return f.getAtividadesBase();
	}
	
	public List<Maquina> getMaquinas(){
		return f.getMaquinas();
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
