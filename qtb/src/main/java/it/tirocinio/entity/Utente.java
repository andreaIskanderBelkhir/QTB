package it.tirocinio.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ManyToAny;

import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;


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
	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<Corso> corsifrequentati;
	
	@OneToMany(mappedBy="docente",fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<Corso> corsitenuti;
	
	@ManyToOne
	private Corso corsoSelezione;
	
	@ElementCollection(targetClass=Long.class,fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> quizpassati;
	
	@ElementCollection(targetClass=Double.class,fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Map<Long,Double> valoretesteffetuati;
	
	
 
	
	
	
	
	
	
	public Map<Long, Double> getValoretesteffetuati() {
		return valoretesteffetuati;
	}

	public void setValoretesteffetuati(Map<Long, Double> valoretesteffetuati) {
		this.valoretesteffetuati = valoretesteffetuati;
	}

	public Corso getCorsoSelezione() {
		return corsoSelezione;
	}

	public void setCorsoSelezione(Corso corsoSelezione) {
		this.corsoSelezione = corsoSelezione;
	}

	public List<Long> getQuizpassati() {
		return quizpassati;
	}

	public void setQuizpassati(List<Long> quizpassati) {
		this.quizpassati = quizpassati;
	}

	public List<Corso> getCorsifrequentati() {
		return corsifrequentati;
	}

	public void setCorsifrequentati(List<Corso> corsifrequentati) {
		this.corsifrequentati = corsifrequentati;
	}

	public List<Corso> getCorsitenuti() {
		return corsitenuti;
	}

	public void setCorsitenuti(List<Corso> corsitenuti) {
		this.corsitenuti = corsitenuti;
	}

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
