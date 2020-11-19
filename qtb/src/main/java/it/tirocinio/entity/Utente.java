package it.tirocinio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
public class Utente  extends AbstractEntity implements Cloneable{


	@NotNull
	@Column(nullable=false,unique=true)
	private String username;
	@NotBlank
	@Column(nullable=false)
	private String password;
	@NotNull
	private String ruolo;

	public Utente(){
		
	}
	
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getNome() {
		return username;
	}
	public void setNome(String nome) {
		this.username = nome;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


}
