package it.tirocinio.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.UtentePendingRepository;

import it.tirocinio.entity.UtentePending;

@Service
public class UtentePendingService {
	 private UtentePendingRepository utentPRep;
	 public UtentePendingService(UtentePendingRepository u){
		 this.utentPRep=u;
	 }
	 public List<UtentePending> findDaValidare(Boolean bool){
		 return this.utentPRep.findByAttivato(bool);
	 }
		public void  save(UtentePending u){
			if(u==null){
				return;
			}
			utentPRep.save(u);
		}
		public void setValide(UtentePending item) {
			
			utentPRep.delete(item);
			item.setAttivato(true);		
			utentPRep.save(item);
			
		}
}
