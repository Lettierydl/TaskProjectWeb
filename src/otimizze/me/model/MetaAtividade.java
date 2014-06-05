package otimizze.me.model;

import java.util.ArrayList;
import java.util.List;

public class MetaAtividade implements Comparable<MetaAtividade>{

	private Atividade atividade;
	private int sequencia;
	private List<Integer> idPredecessoras;
	private int necessidadeDeProducao;
	private float necessidadeDeTempo;
	
	
	public Atividade getAtividade() {
		return atividade;
	}
	public void setAtividade(Atividade a) {
		this.atividade = a;
	}
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
	public List<Integer> getIdPredecessoras() {
		return idPredecessoras;
	}
	public void setIdPredecessoras(List<Integer> idPredecessoras) {
		this.idPredecessoras = idPredecessoras;
	}
	// ver se metodo vai ser usado, por causa da funcao recursiva
	public void calcularIdPredecessoras() {
		idPredecessoras = new ArrayList<Integer>();
		for(Atividade pre: atividade.getAtividadesPrecessoras()){
			idPredecessoras.add(pre.getId());
		}
	}
	public int getNecessidadeDeProducao() {
		return necessidadeDeProducao;
	}
	public void setNecessidadeDeProducao(int necessidadeDeProducao) {
		this.necessidadeDeProducao = necessidadeDeProducao;
	}
	public float getNecessidadeDeTempo() {
		return necessidadeDeTempo;
	}
	public float calculargNecessidadeDeTempo(){
		necessidadeDeTempo = (necessidadeDeProducao/atividade.getQuantidadeDeProducao())*atividade.getTempo();
		return getNecessidadeDeTempo();
	}
	
	@Override
	public int compareTo(MetaAtividade o) {
		return Integer.compare(this.sequencia, o.sequencia);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((atividade == null) ? 0 : atividade.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaAtividade other = (MetaAtividade) obj;
		if (atividade == null) {
			if (other.atividade != null)
				return false;
		} else if (!atividade.equals(other.atividade))
			return false;
		return true;
	}
	
}
