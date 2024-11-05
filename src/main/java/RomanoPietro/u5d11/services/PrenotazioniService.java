package RomanoPietro.u5d11.services;


import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.entities.Prenotazioni;
import RomanoPietro.u5d11.entities.Viaggio;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.exceptions.NotFoundException;
import RomanoPietro.u5d11.payloads.NewPrenotazioniDTO;
import RomanoPietro.u5d11.repositories.PrenotazioniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioniService {

    @Autowired
    private PrenotazioniRepository prenotazioniRepository;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private ViaggioService viaggioService;

    //1.======================================================================================================================

    public Page<Prenotazioni> findAll(int page, int size, String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioniRepository.findAll(pageable);

    }

    //2.======================================================================================================================

    public Prenotazioni save(NewPrenotazioniDTO body) {
        Dipendente dipendente = dipendenteService.findById(body.dipendenteId());
        Viaggio viaggio = viaggioService.findById(body.viaggioId());

        if (prenotazioniRepository.existsByDipendenteAndDataRichiesta(dipendente, body.dataRichiesta())) {
            throw new BadRequestException("Dipendente ha già una prenotazione per questa data!");
        }

        Prenotazioni newPrenotazione = new Prenotazioni();
        newPrenotazione.setDipendente(dipendente);
        newPrenotazione.setViaggio(viaggio);
        newPrenotazione.setDataRichiesta(body.dataRichiesta());
        newPrenotazione.setPreferenze(body.preferenze());

        return prenotazioniRepository.save(newPrenotazione);
    }
    //3.======================================================================================================================

    public Prenotazioni findById(long prenotazioneId) {
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    //4.======================================================================================================================

    public Prenotazioni findByIdAndUpdate(long prenotazioneId, NewPrenotazioniDTO body) {
        Prenotazioni found = this.findById(prenotazioneId);

        found.setViaggio(viaggioService.findById(body.viaggioId()));
        return prenotazioniRepository.save(found);
    }

    //5.======================================================================================================================
        public void findByIdAndDelete(long prenotazioneId) {
        Prenotazioni found = this.findById(prenotazioneId);
        this.prenotazioniRepository.delete(found);
        }

}
