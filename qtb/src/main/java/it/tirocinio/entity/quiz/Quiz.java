package it.tirocinio.entity.quiz;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import it.tirocinio.entity.AbstractEntity;

@Entity
public class Quiz extends AbstractEntity implements Cloneable {
	
	
	@Column(nullable=false,unique=true)
	private String nomeQuiz;
	
	
	@ManyToOne
	private Corso corsoAppartenenza;
	
	private Boolean attivato;
	

	@OneToMany(mappedBy="quizapparteneza")
	private List<Domanda> domande;
	
	
	
	
	public Corso getCorsoAppartenenza() {
		return corsoAppartenenza;
	}
	public void setCorsoAppartenenza(Corso corsoAppartenenza) {
		this.corsoAppartenenza = corsoAppartenenza;
	}
	public List<Domanda> getDomande() {
		return domande;
	}
	public void setDomande(List<Domanda> domande) {
		this.domande = domande;
	}
	
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

	} 

