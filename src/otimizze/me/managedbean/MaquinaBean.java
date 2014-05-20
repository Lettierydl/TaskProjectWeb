package otimizze.me.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import otimizze.me.Facede;
import otimizze.me.model.Maquina;
import otimizze.me.util.JSFUiUtil;

@ManagedBean(name = "maquinaBean")
@ViewScoped
public class MaquinaBean {

	private Maquina newMaquina;
	private Facede f;

	public MaquinaBean(){
		newMaquina = new Maquina();
		f = new Facede();
	}
	
	public void cadastrarMaquina(){
		f.cadastrarMaquina(newMaquina);
		newMaquina = new Maquina();
	}
	
	public List<Maquina> getMaquinas(){
		return f.getMaquinas();
	}
	
	public Maquina getNewMaquina() {
		return newMaquina;
	}

	public void setNewMaquina(Maquina newMaquina) {
		this.newMaquina = newMaquina;
	}
	
}
