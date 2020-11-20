package it.tirocinio.entity.quiz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import it.tirocinio.entity.AbstractEntity;

@Entity
public class Quiz extends AbstractEntity implements Cloneable {
	
	
	@Column(nullable=false,unique=true)
	private String nomeQuiz;
	
	
	@ManyToOne
	private Corso corsoAppartenenza;
	
	private Boolean attivato;
	
	
	public Boolean getAttivato() {
		return attivato;
	}
	public void setAttivato(Boolean attivato) {
		this.attivato = attivato;
	}
	public String getNomeQuiz() {
		return nomeQuiz;
	}
	public void setNomeQuiz(String nomeQuiz) {
		this.nomeQuiz = nomeQuiz;
	}
	public Corso getCorsoAppertenenza() {
		return corsoAppartenenza;
	}
	public void setCorsoAppertenenza(Corso corsoAppertenenza) {
		this.corsoAppartenenza = corsoAppertenenza;
	} 
}
