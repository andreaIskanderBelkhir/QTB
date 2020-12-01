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
	@Column(nullable=false,unique=true)
	private String nomedomanda;

	private String descrizionedomanda;
	
	@ManyToOne
	private Quiz quizapparteneza;

	@OneToMany(mappedBy="domandaApparteneza",fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Set<Risposta> risposte;


	public Set<Risposta> getRisposte() {
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
	public void setRisposte(Set<Risposta> risposte) {
		this.risposte = risposte;
	}

	public Quiz getQuizapparteneza() {
		return quizapparteneza;
	}
	public void setQuizapparteneza(Quiz quizapparteneza) {
		this.quizapparteneza = quizapparteneza;
	}

}
