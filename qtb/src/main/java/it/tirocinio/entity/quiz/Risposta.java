package it.tirocinio.entity.quiz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import it.tirocinio.entity.AbstractEntity;

@Entity
public class Risposta extends AbstractEntity implements Cloneable  {

	@Column(nullable=false)
	private String risposta ;
	

	@ManyToOne
	private Domanda domandaApparteneza;
	
	private Boolean giusta;
	

	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public Domanda getDomandaApparteneza() {
		return domandaApparteneza;
	}

	public void setDomandaApparteneza(Domanda domandaApparteneza) {
		this.domandaApparteneza = domandaApparteneza;
	}

	public Boolean getGiusta() {
		return giusta;
	}

	public void setGiusta(Boolean giusta) {
		this.giusta = giusta;
	}

}
