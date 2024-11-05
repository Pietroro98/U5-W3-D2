package RomanoPietro.u5d11.exceptions;



public class NotFoundException extends RuntimeException{
    public NotFoundException(long id) {
        super("Il record con id: " + id + " non é stato trovato");
    }

    public NotFoundException(String msg) {
        super(msg);
    }

}
