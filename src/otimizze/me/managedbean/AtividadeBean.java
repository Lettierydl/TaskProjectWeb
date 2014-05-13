package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import otimizze.me.Facede;
import otimizze.me.model.Atividade;
import otimizze.me.model.Maquina;

@ManagedBean(name = "atividadeBean")
@ViewScoped
// vai ser usada para cadastrar atividades base/sem produtos(Ex: iniciar materia prima)
public class AtividadeBean {
	
	private Atividade newAtividade;
	private Facede f;
	
	public AtividadeBean(){
		newAtividade = new Atividade();
	}
	
	public void cadastrarAtividade(){
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
	
	
}
