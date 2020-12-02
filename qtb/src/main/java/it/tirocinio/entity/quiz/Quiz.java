package it.tirocinio.entity.quiz;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.tirocinio.entity.AbstractEntity;

@Entity
public class Quiz extends AbstractEntity implements Cloneable {
	
	
	@Column(nullable=false,unique=true)
	private String nomeQuiz;
	
	
	@ManyToOne
	private Corso corsoAppartenenza;

	private String tempo;
	private Boolean attivato;
	
	@OneToMany(mappedBy="quizapparteneza",fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Set<Domanda> domande;
	
	
	
	
	public String getTempo() {
		return tempo;
	}
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	public Corso getCorsoAppartenenza() {
		return corsoAppartenenza;
	}
	public void setCorsoAppartenenza(Corso corsoAppartenenza) {
		this.corsoAppartenenza = corsoAppartenenza;
	}
	public Set<Domanda> getDomande() {
		return domande;
	}
	public void setDomande(Set<Domanda> domande) {
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

