package it.tirocinio.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tirocinio.entity.UtentePending;

public interface UtentePendingRepository extends JpaRepository<UtentePending,Long>{
			List<UtentePending> findByAttivato(Boolean s);
}
