package RomanoPietro.u5d11.services;


import RomanoPietro.u5d11.entities.Viaggio;
import RomanoPietro.u5d11.enums.StatoViaggio;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.exceptions.NotFoundException;
import RomanoPietro.u5d11.exceptions.ViaggioNotFoundException;
import RomanoPietro.u5d11.payloads.NewViaggioDTO;
import RomanoPietro.u5d11.repositories.ViaggioRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    //1.======================================================================================================================

    public Viaggio save(NewViaggioDTO body) {
        Viaggio newViaggio = new Viaggio(body.destinazione(),
                body.data(),
                StatoViaggio.valueOf(body.stato()));
        return this.viaggioRepository.save(newViaggio);
    }

    //2.======================================================================================================================

    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    //3.======================================================================================================================

    public Viaggio findById(long viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    //4.======================================================================================================================

    public Viaggio findByIdAndUpdate(long viaggioId, NewViaggioDTO body) {
        Viaggio found = this.findById(viaggioId);

        if(found.getData().equals(body.data())
                && found.getDestinazione().equals(body.destinazione()))
            this.viaggioRepository.findByDataAndDestinazione(body.data(), body.destinazione());

        found.setDestinazione(body.destinazione());
        found.setData(body.data());

        if (body.stato() != null && !body.stato().isEmpty()) {
                found.setStatoViaggio(StatoViaggio.valueOf(body.stato()));
                throw new BadRequestException("StatoViaggio non valido: " + body.stato());

        }

        return this.viaggioRepository.save(found);
    }

    //5.======================================================================================================================

    public void findByIdAndDelete(long viaggioId) {
        Viaggio found = this.findById(viaggioId);
        this.viaggioRepository.delete(found);
    }

    //6.======================================================================================================================

    public Viaggio updateNuovoStato(long viaggioId, String nuovoStato) {
        Viaggio found = viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new ViaggioNotFoundException("Viaggio non trovato con ID: " + viaggioId));

        if (!nuovoStato.equals("IN_PROGRAMMA") && !nuovoStato.equals("COMPLETATO")) {
            throw new BadRequestException("Lo stato deve essere 'IN_PROGRAMMA' o 'COMPLETATO'.");
        }

        found.setStatoViaggio(StatoViaggio.valueOf(nuovoStato));
        return viaggioRepository.save(found);
    }
}
