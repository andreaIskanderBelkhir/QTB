package it.tirocinio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UtentePending extends AbstractEntity implements Cloneable {

private String nome;
 private String email;
 private String descizione;
 private Boolean attivato;
 
 public Boolean getAttivato() {
	return attivato;
}

public void setAttivato(Boolean attivato) {
	this.attivato = attivato;
}

public UtentePending(){
	 
 }
 
 @Column(nullable=false)
 public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescizione() {
		return descizione;
	}
	public void setDescizione(String descizione) {
		this.descizione = descizione;
	}
}
