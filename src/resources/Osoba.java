package resources;

import java.util.Date;

public class Osoba {
    private int id;
    private String imie;
    private String nazwisko;
    private String pesel;
    private Date najblizszaZaplata;
    private Date ostatniaZaplata;
    private int idPokoju;

    public Osoba(int id, String imie, String nazwisko, String pesel, Date najblizszaZaplata, Date ostatniaZaplata, int idPokoju) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.najblizszaZaplata = najblizszaZaplata;
        this.ostatniaZaplata = ostatniaZaplata;
        this.idPokoju = idPokoju;
    }

    public int getId() {
        return id;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getPesel() {
        return pesel;
    }

    public Date getNajblizszaZaplata() {
        return najblizszaZaplata;
    }

    public Date getOstatniaZaplata() {
        return ostatniaZaplata;
    }

    public int getIdPokoju() {
        return idPokoju;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setNajblizszaZaplata(Date najblizszaZaplata) {
        this.najblizszaZaplata = najblizszaZaplata;
    }

    public void setOstatniaZaplata(Date ostatniaZaplata) {
        this.ostatniaZaplata = ostatniaZaplata;
    }

    public void setIdPokoju(int idPokoju) {
        this.idPokoju = idPokoju;
    }
}

