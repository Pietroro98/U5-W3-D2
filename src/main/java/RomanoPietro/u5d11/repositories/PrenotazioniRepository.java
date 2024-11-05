package RomanoPietro.u5d11.repositories;


import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.entities.Prenotazioni;
import org.springframework.data.jpa.repository.JpaRepository;





public interface PrenotazioniRepository extends JpaRepository<Prenotazioni, Long> {

    boolean existsByDipendenteAndDataRichiesta(Dipendente dipendente, String dataRichiesta);
}
