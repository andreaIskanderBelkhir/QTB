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
	private String nomedomanda;

	private String descrizionedomanda;
	@ManyToOne
	private Quiz quizapparteneza;

	@OneToMany(mappedBy="domandaApparteneza")
	private List<Risposta> risposte;


	public List<Risposta> getRisposte() {
		return risposte;
	}
	public String getNomedomanda() {
		return nomedomanda;
	}
	public void setNomedomanda(String nomedomanda) {
		this.nomedomanda = nomedomanda;
	}
	public String getDescrizionedomanda() {
		return descrizionedomanda;
	}
	public void setDescrizionedomanda(String descrizionedomanda) {
		this.descrizionedomanda = descrizionedomanda;
	}
	public void setRisposte(List<Risposta> risposte) {
		this.risposte = risposte;
	}

	public Quiz getQuizapparteneza() {
		return quizapparteneza;
	}
	public void setQuizapparteneza(Quiz quizapparteneza) {
		this.quizapparteneza = quizapparteneza;
	}

}
