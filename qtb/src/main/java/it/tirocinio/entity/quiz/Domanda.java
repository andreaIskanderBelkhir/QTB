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
public class Domanda extends AbstractEntity implements Cloneable {
	@Column(nullable=false)
	private String nomedomanda;
	private String descrizionedomanda;
	private Boolean randomordine;
	

	@ManyToOne
	private Quiz quizapparteneza;

	@OneToMany(mappedBy="domandaApparteneza",fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Risposta> risposte;

	public Boolean getRandomordine() {
		return randomordine;
	}
	public void setRandomordine(Boolean randomordine) {
		this.randomordine = randomordine;
	}
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
