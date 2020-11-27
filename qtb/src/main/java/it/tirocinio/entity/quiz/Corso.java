package it.tirocinio.entity.quiz;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import it.tirocinio.entity.AbstractEntity;
import it.tirocinio.entity.Utente;

@Entity
public class Corso extends AbstractEntity implements Cloneable{
	@NotNull
	@Column(nullable=false,unique=true)
	private String nomeCorso;
	@ManyToMany(fetch = FetchType.EAGER,mappedBy="corsifrequentati")
	private List<Utente> utentifreq;

	@ManyToOne
	private Utente docente;
    private String descrizioneCorso;
    
    @OneToMany(mappedBy="corsoAppartenenza",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Quiz> quizDelcorso;
    

	public List<Quiz> getQuizDelcorso() {
		return quizDelcorso;
	}

	public void setQuizDelcorso(List<Quiz> quizDelcorso) {
		this.quizDelcorso = quizDelcorso;
	}

	public String getNomeCorso() {
		return nomeCorso;
	}
    public String getDescrizioneCorso() {
		return descrizioneCorso;
	}

	public void setDescrizioneCorso(String descrizioneCorso) {
	 this.descrizioneCorso = descrizioneCorso;
	}

	public List<Utente> getUtentifreq() {
		return utentifreq;
	}

	public void setUtentifreq(List<Utente> utentifreq) {
		this.utentifreq = utentifreq;
	}

	public Utente getDocente() {
		return docente;
	}

	public void setDocente(Utente docente) {
		this.docente = docente;
	}

	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}

}
