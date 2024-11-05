package RomanoPietro.u5d11.controllers;

/*
*****************************CRUD******************************
*
1. GET http://localhost:3005/viaggi
2. POST http://localhost:3005/viaggi (+ req.body) --> 201
3. GET http://localhost:3005/viaggi/{viaggioId}
4. PUT http://localhost:3005/viaggi/{viaggioId} (+ req.body)
5. DELETE http://localhost:3005/viaggi/{viaggioId} --> 204
*
* **************************************************************
*/

import RomanoPietro.u5d11.entities.Viaggio;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.payloads.NewViaggioDTO;
import RomanoPietro.u5d11.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;


    //1. GET http://localhost:3005/viaggi
    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        return this.viaggioService.findAll(page, size, sortBy);
    }

    //2. POST http://localhost:3005/viaggi (+ req.body) --> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio save(@RequestBody @Validated NewViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult
                    .getAllErrors()
                    .stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("ci sono stati errori nel payload!" + message);
        }
        return this.viaggioService.save(body);
    }

    // 3. GET http://localhost:3005/viaggi/{viaggioId}
    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable long viaggioId) {
        return this.viaggioService.findById(viaggioId);
    }

    // 4. PUT http://localhost:3005/viaggi/{viaggioId} (+ req.body)
    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable long viaggioId, @RequestBody NewViaggioDTO body) {
        return this.viaggioService.findByIdAndUpdate(viaggioId, body);
    }

    // 5. DELETE http://localhost:3005/viaggi/{viaggioId} --> 204
    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long viaggioId) {
        this.viaggioService.findByIdAndDelete(viaggioId);
    }

    // 6. PATCH http://localhost:3005/viaggi/{viaggioId}/stato (+ req.body)
    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStato(@PathVariable long viaggioId, @RequestBody String nuovoStato) {
        return this.viaggioService.updateNuovoStato(viaggioId, nuovoStato);
    }
}
