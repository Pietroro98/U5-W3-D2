package RomanoPietro.u5d11.controllers;

import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.payloads.NewDipendenteDTO;
import RomanoPietro.u5d11.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

/*
*****************************CRUD******************************
*
1. GET http://localhost:3005/dipendente
2. POST http://localhost:3005/dipendente (+ req.body) --> 201
3. GET http://localhost:3005/dipendente/{dipendenteId}
4. PUT http://localhost:3005/dipendente/{dipendenteId} (+ req.body)
5. DELETE http://localhost:3005/dipendente/{dipendenteId} --> 204
6. PATCH http://localhost:3005/dipendente/{dipendenteID}/avatar
*
* **************************************************************
*/

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    @Autowired
    private DipendenteService dipendenteService;

    //1. GET http://localhost:3003/dipendenti
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }

    //2. POST http://localhost:3003/dipendenti (+ req.body) --> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente save(@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult
                    .getAllErrors()
                    .stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("ci sono stati errori nel payload!" + message);
        }
        return this.dipendenteService.save(body);
    }
// ================== ME ENDPOINT==================================================
    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }
    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser, @RequestBody @Validated NewDipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        this.dipendenteService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }
//========================================================================



    //3. GET http://localhost:3001/dipendenti/{dipendenteId}
    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable long dipendenteId) {
        return this.dipendenteService.findById(dipendenteId);
    }

    //4. PUT http://localhost:3001/dipendenti/{dipendenteId} (+ req.body)
    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable long dipendenteId, @RequestBody NewDipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdate(dipendenteId, body);
    }

    //5. DELETE http://localhost:3001/dipendenti/{dipendenteId} --> 204
    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long dipendenteId) {
        this.dipendenteService.findByIdAndDelete(dipendenteId);
    }

    //6. PATCH http://localhost:3005/dipendente/{dipendenteID}/avatar
    @PatchMapping("/{dipendenteId}/avatar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String updloadAvatar(@RequestParam("avatar") MultipartFile file) throws IOException {
        return this.dipendenteService.uploadAvatar(file);
    }
}
