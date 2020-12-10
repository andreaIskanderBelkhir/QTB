package it.tirocinio.entity.quiz;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.tirocinio.entity.AbstractEntity;
import it.tirocinio.entity.Utente;

@Entity
public class Quiz extends AbstractEntity implements Cloneable {


	@Column(nullable=false,unique=true)
	private String nomeQuiz;


	@ManyToOne
	private Corso corsoAppartenenza;

	private double tempo;
	private Boolean attivato;

	@OneToMany(mappedBy="quizapparteneza",fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Set<Domanda> domande;

	private double valoreGiusta;
	private double valoreSbagliata;
	private Boolean modalitaPercentuale;
	private double soglia;
	private double sogliaPercentuale;
	


	public Boolean getModalitaPercentuale() {
		return modalitaPercentuale;
	}
	public void setModalitaPercentuale(Boolean modalitaPercentuale) {
		this.modalitaPercentuale = modalitaPercentuale;
	}
	public double getSoglia() {
		return soglia;
	}
	public void setSoglia(double soglia) {
		this.soglia = soglia;
	}
	public double getSogliaPercentuale() {
		return sogliaPercentuale;
	}
	public void setSogliaPercentuale(double sogliaPercentuale) {
		this.sogliaPercentuale = sogliaPercentuale;
	}
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public double getValoreGiusta() {
		return valoreGiusta;
	}
	public void setValoreGiusta(double valoreGiusta) {
		this.valoreGiusta = valoreGiusta;
	}
	public double getValoreSbagliata() {
		return valoreSbagliata;
	}
	public void setValoreSbagliata(double valoreSbagliata) {
		this.valoreSbagliata = valoreSbagliata;
	}
	public double getTempo() {
		return tempo;
	}
	public void setTempo(Double double1) {
		this.tempo = double1;
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

