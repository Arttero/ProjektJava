package Gui.AdminGui;

public class DodawanieUzytkownika {
    private String nazwaUzytkownika;
    private String haslo;


    public DodawanieUzytkownika(String nazwaUzytkownika, String haslo) {
        this.nazwaUzytkownika = nazwaUzytkownika;
        this.haslo = haslo;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getNazwaUzytkownika() {
        return nazwaUzytkownika;
    }

    public void setNazwaUzytkownika(String nazwaUzytkownika) {
        this.nazwaUzytkownika = nazwaUzytkownika;
    }
}
