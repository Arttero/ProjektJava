package resources;

public class Uzytkownik {
    private String login;
    private String haslo;
    private String telefon;
    private String email;

    public Uzytkownik(String login, String haslo, String telefon, String email) {
        this.login = login;
        this.haslo = haslo;
        this.telefon = telefon;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

