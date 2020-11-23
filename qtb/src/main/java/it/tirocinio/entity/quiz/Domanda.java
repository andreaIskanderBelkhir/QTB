package it.tirocinio.entity.quiz;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import it.tirocinio.entity.AbstractEntity;

@Entity
public class Domanda extends AbstractEntity implements Cloneable {
	@Column(nullable=false,unique=true)
	private String descrizione;
	@ManyToOne
	private Quiz quizapparteneza;

	@OneToMany(mappedBy="domandaApparteneza")
	private List<Risposta> risposte;


	public List<Risposta> getRisposte() {
		return risposte;
	}
	public void setRisposte(List<Risposta> risposte) {
		this.risposte = risposte;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Quiz getQuizapparteneza() {
		return quizapparteneza;
	}
	public void setQuizapparteneza(Quiz quizapparteneza) {
		this.quizapparteneza = quizapparteneza;
	}

}
