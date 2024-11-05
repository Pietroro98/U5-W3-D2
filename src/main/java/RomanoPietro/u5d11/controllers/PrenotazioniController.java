package RomanoPietro.u5d11.controllers;


import RomanoPietro.u5d11.entities.Prenotazioni;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.payloads.NewPrenotazioniDTO;
import RomanoPietro.u5d11.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/*
*****************************CRUD******************************
*
1. GET http://localhost:3005/prenotazioni
2. POST http://localhost:3005/prenotazioni (+ req.body) --> 201
3. GET http://localhost:3005/prenotazioni/{prenotazioneId}
4. PUT http://localhost:3005/prenotazioni/{prenotazioneId} (+ req.body)
5. DELETE http://localhost:3005/prenotazioni/{prenotazioneId} --> 204
*
* **************************************************************
*/


@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    @Autowired
    private PrenotazioniService prenotazioniService;

    //1. GET http://localhost:3005/prenotazioni
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Prenotazioni> findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioniService.findAll(page, size, sortBy);
    }

    //2. POST http://localhost:3005/prenotazioni (+ req.body) --> 201
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazioni save(@RequestBody @Validated NewPrenotazioniDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult
                    .getAllErrors()
                    .stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("ci sono stati errori nel payload!" + message);
        }
        return this.prenotazioniService.save(body);
    }

    //3. GET http://localhost:3005/prenotazioni/{prenotazioneId}
    @GetMapping("/{prenotazioneId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Prenotazioni findById(@PathVariable long prenotazioneId) {
        return this.prenotazioniService.findById(prenotazioneId);
    }

    //4. PUT http://localhost:3005/prenotazioni/{prenotazioneId} (+ req.body)
    @PutMapping("/{prenotazioneId}")
    public Prenotazioni findByIdAndUpdate(@PathVariable long prenotazioneId, @RequestBody NewPrenotazioniDTO body) {
        return this.prenotazioniService.findByIdAndUpdate(prenotazioneId, body);
    }

    //5. DELETE http://localhost:3005/prenotazioni/{prenotazioneId} --> 204
    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long prenotazioneId) {
        this.prenotazioniService.findByIdAndDelete(prenotazioneId);
    }
}
