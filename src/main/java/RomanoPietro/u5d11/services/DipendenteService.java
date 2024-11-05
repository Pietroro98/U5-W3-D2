package RomanoPietro.u5d11.services;


import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.exceptions.BadRequestException;
import RomanoPietro.u5d11.exceptions.NotFoundException;
import RomanoPietro.u5d11.payloads.NewDipendenteDTO;
import RomanoPietro.u5d11.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder bcrypt;

    //1.======================================================================================================================

    public Dipendente save(NewDipendenteDTO body) {
        this.dipendenteRepository.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("Email" + body.email() + "giá in uso");
                }
        );
        Dipendente newUser = new Dipendente(body.username(), body.nome(), body.cognome(), body.email(),
                bcrypt.encode(body.password()), "https://ui-avatars.com/api/?name=" + body.nome() + "-" + body.cognome());
        return this.dipendenteRepository.save(newUser);

    }

    //2.======================================================================================================================

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendenteRepository.findAll(pageable);

    }

    //3.======================================================================================================================

    public Dipendente findById(long dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    //4.======================================================================================================================

    public Dipendente findByIdAndUpdate(long dipendenteId, NewDipendenteDTO body) {
        Dipendente found = this.findById(dipendenteId);
        if(found.getEmail().equals(body.email())) {
            this.dipendenteRepository.findByEmail(body.email()).ifPresent(
                    dipendente -> {
                        throw new BadRequestException("Email" + body.email() + "giá in uso");
                    }
            );
        }
        found.setUsername(body.username());
        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());
        found.setPassword(bcrypt.encode(body.password()));
        found.setAvatarURL("https://ui-avatars.com/api/?name=" + body.nome() + "-" + body.cognome());
        return this.dipendenteRepository.save(found);
    }

    //5.======================================================================================================================

    public void findByIdAndDelete(long dipendenteId) {
        Dipendente found = this.findById(dipendenteId);
        this.dipendenteRepository.delete(found);
    }

    //6.===============================================
    public String uploadAvatar(MultipartFile file) {
        try{
            String url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            return url;
        }
        catch (IOException ex){
            return null;
        }
    }

    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }
}
