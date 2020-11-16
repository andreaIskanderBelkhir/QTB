package it.tirocinio.data;
import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="utente")
public class Utente {
	private Integer id;
	private String nome;
	private String password;
	private Integer id_ruolo;
	
	public Utente(String string, String string2, int int1) {
		this.nome=string;
		this.password=string;
		this.id_ruolo=int1;
	}
	public Utente() {
		// TODO Auto-generated constructor stub
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="nome",nullable=false)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="id_ruolo")
	public Integer getId_ruolo() {
		return id_ruolo;
	}
	public void setId_ruolo(Integer id_ruolo) {
		this.id_ruolo = id_ruolo;
	}
}
