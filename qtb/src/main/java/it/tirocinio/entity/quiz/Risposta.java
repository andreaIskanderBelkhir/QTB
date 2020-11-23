package it.tirocinio.entity.quiz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import it.tirocinio.entity.AbstractEntity;

@Entity
public class Risposta extends AbstractEntity implements Cloneable  {

	@Column(nullable=false,unique=true)
	private String descrizione ;
	

	@ManyToOne
	private Domanda domandaApparteneza;
	
	private Boolean giusta;
	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
