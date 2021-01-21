package it.tirocinio.entity.quiz;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	@JoinColumn(name="docente_id")
	private Utente docente;
	
	private String descrizioneCorso;
	private Boolean selezione;

	@OneToMany(mappedBy="corsoAppartenenza",fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval=true)
	private List<Quiz> quizDelcorso;

	@ElementCollection(targetClass=Long.class,fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> utentirischiesta;
	
	@OneToMany(mappedBy="corsoSelezione",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Utente> candidati;







	public List<Utente> getCandidati() {
		return candidati;
	}

	public void setCandidati(List<Utente> candidati) {
		this.candidati = candidati;
	}

	public Boolean getSelezione() {
		return selezione;
	}

	public void setSelezione(Boolean selezione) {
		this.selezione = selezione;
	}

	public List<Long> getUtentirischiesta() {
		return utentirischiesta;
	}

	public void setUtentirischiesta(List<Long> utentirischiesta) {
		this.utentirischiesta = utentirischiesta;
	}

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
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Corso))
			return false;
		Corso altro =(Corso)o;
		
		return this.getID()==altro.getID();
	}
	@Override
	public final int hashCode() {
	    int result = 17;
	    if (this.docente != null) {
	        result = 31 * result + this.docente.getNome().hashCode();
	    }
	    if (this.nomeCorso != null) {
	        result = 31 * result + this.nomeCorso.hashCode();
	    }
	    return result;
	}
}


