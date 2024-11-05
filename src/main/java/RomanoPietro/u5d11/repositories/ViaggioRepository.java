package RomanoPietro.u5d11.repositories;

import RomanoPietro.u5d11.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
    public Viaggio findByDataAndDestinazione(String data, String destinazione);
}
