package Gui.AdminGui;

import resources.TworzenieGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class BudynekGui extends JFrame {
    private JPanel budynekGui;
    private JPanel panelGorny;
    private JPanel panelSrodkowy;
    private JTable tabela;
    private JButton dodajBudynek;
    private JButton edytujBudynek;
    private JButton usunBudynek;
    private JButton powrot;
    private JComboBox<String> comboSortowanie;
    private TableRowSorter<DefaultTableModel> sortowanie;
    private DefaultTableModel model;
    private JTextField poleWyszukiwania;
    private JComboBox<String> kolumnaFiltrowanie;


    //nazwy kolumn do tabeli i filtrowania
    private String[] nazwyKolumn = {"ID", "Nazwa Budynku", "Adres Budynku","Typ Budynku"};
    TworzenieGUI tworzenieGUI = new TworzenieGUI();

    public BudynekGui() {
        super("Budynek");
        budynekGui = new JPanel(new BorderLayout(5,5));
        budynekGui.setBackground(Color.WHITE);
        budynekGui.setBorder(BorderFactory.createLineBorder(Color.black));

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        this.setContentPane(budynekGui);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void panelGorny() {
        panelGorny = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGorny.setBackground(Color.WHITE);

        //Sortowanie
        String[] opcjeSortowania = {
                "ID (rosnąco)","ID (malejąco)","Nazwa (A-Z)","Nazwa (Z-A)","Adres (A-Z)","Adres (Z-A)",
                "Typ (A-Z)","Typ (Z-A)"
        };
        comboSortowanie = new JComboBox<>(opcjeSortowania);
        comboSortowanie.setSelectedIndex(0);
        comboSortowanie.addActionListener(e -> sortujTabele());
        panelGorny.add(new JLabel("Sortowanie według:"));
        panelGorny.add(comboSortowanie);

        //Wyszukiwanie
        kolumnaFiltrowanie = new JComboBox<>(nazwyKolumn);
        //Pole wyszukiwania
        poleWyszukiwania = new JTextField(20); //jak puste to nie da się kliknąć
        poleWyszukiwania.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrujTabele();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrujTabele();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrujTabele();
            }
        });
        panelGorny.add(new JLabel("Filtrowanie:"));
        panelGorny.add(Box.createHorizontalStrut(20)); //odstęp
        panelGorny.add(new JLabel("Szukaj po:"));
        panelGorny.add(kolumnaFiltrowanie);
        panelGorny.add(poleWyszukiwania);

        budynekGui.add(panelGorny, BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        panelSrodkowy = new JPanel(new GridLayout(1, 4, 15, 0));
        panelSrodkowy.setBorder(BorderFactory.createLineBorder(Color.black));
        panelSrodkowy.setBackground(Color.WHITE);

        Object[][] data = {
                {1, "Budynek A", "ul. Test 1","Biurowiec"},
                {2, "Budynek B", "ul. Test 2","Budynek mieszkalny"}
        };

        model = new DefaultTableModel(data, nazwyKolumn){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //dzięki temu żadna komórka nie jest możliwa do edycji w tabeli
            }
        };

        tabela = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabela.setRowSorter(sortowanie);

        //Opcja scrollowania
        JScrollPane scrollPane = new JScrollPane(tabela);
        panelSrodkowy.add(scrollPane,BorderLayout.CENTER);

        budynekGui.add(panelSrodkowy,BorderLayout.CENTER);
    }

    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        budynekGui.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        dodajBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Dodaj budynek", new Color(37, 37, 37),new Color(255,255,255), "/resources/figures/icons8-building2-100.png");
        edytujBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Edytuj budynek", new Color(2, 80, 253),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        usunBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Usuń budynek", new Color(253, 236, 7),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        powrot = tworzenieGUI.tworzeniePrzycisku("Powrót", new Color(1, 255, 31 ), new Color(255,255,255));

        buttonPanel.add(dodajBudynek);
        buttonPanel.add(edytujBudynek);
        buttonPanel.add(usunBudynek);
        buttonPanel.add(powrot);
    }

    private void ustawienieListenerow() {
        powrot.addActionListener(e -> {
            dispose();
            new PanelAdministratora();
            System.out.println("Przechodzę do panelu administratora");
        });


        dodajBudynek.addActionListener(e -> {
            System.out.println("Dodawanie budynku");
            dodajBudynek();
        });
        edytujBudynek.addActionListener(e -> {
            System.out.println("Edytowanie budynku");
            edytujBudynek();
        });
        usunBudynek.addActionListener(e -> {
            System.out.println("Usuwanie budynku");
            usunBudynek();
        });
    }

    private void sortujTabele() {
        String wybrane = (String) comboSortowanie.getSelectedItem();

        if (wybrane == null || sortowanie == null) return;

        List<RowSorter.SortKey> sortowanieKluczy = new ArrayList<>();

        switch(wybrane) {
            //"ID (rosnąco)","ID (malejąco)","Nazwa (A-Z)","Nazwa (Z-A)","Adres (A-Z)","Adres (Z-A)",
            //"Typ (A-Z)","Typ (Z-A)"
            case "ID (rosnąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "ID (malejąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Nazwa (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Nazwa (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Adres (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Adres (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Typ (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Typ (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            default:
                System.out.println("Wystąpił błąd przy sortowaniu");
                break;
        }
        sortowanie.setSortKeys(sortowanieKluczy);
        sortowanie.sort();
    }

    private void filtrujTabele() {
        if (sortowanie == null) return;

        String tekst = poleWyszukiwania.getText().trim();
        int kolumna = kolumnaFiltrowanie.getSelectedIndex();

        if (tekst.length() == 0) {
            sortowanie.setRowFilter(null); //bez filtra
        } else {
            sortowanie.setRowFilter(RowFilter.regexFilter("(?i)" + tekst, kolumna));
        }
    }

    private void dodajBudynek() {
        JTextField poleNazwa = new JTextField();
        JTextField poleAdres = new JTextField();
        JTextField poleTyp = new JTextField();

        //TODO: odczytanie z bazy danych
        Object[] pola = {
                "Nazwa budynku:", poleNazwa,
                "Adres budynku:", poleAdres,
                "Typ budynku:", poleTyp
        };

        int wynik = JOptionPane.showConfirmDialog(this,pola,"Dodaj informacje o budynku", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                String nazwa = poleNazwa.getText().trim();
                String adres = poleAdres.getText().trim();
                String typ = poleTyp.getText().trim();

                //TODO: dodanie informacji do bazy danych

                System.out.println("Nazwa:"+ nazwa);
                System.out.println("Adres:"+ adres);
                System.out.println("Typ:"+ typ);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Wystąpił błąd: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void edytujBudynek() {
        int wybranyWiersz = tabela.getSelectedRow();
        if (wybranyWiersz == -1) {
            JOptionPane.showMessageDialog(this,"Wybierz budynek do edycji.","Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }
        wybranyWiersz = tabela.convertRowIndexToModel(wybranyWiersz);

        Object id = model.getValueAt(wybranyWiersz, 0);
        String nazwa = (String) model.getValueAt(wybranyWiersz, 1);
        String adres = (String) model.getValueAt(wybranyWiersz, 2);
        String typ = (String) model.getValueAt(wybranyWiersz, 3);

        JTextField poleNazwa = new JTextField(nazwa);
        JTextField poleAdres = new JTextField(adres);
        JTextField poleTyp = new JTextField(typ);

        Object[] pola = {
                "Nazwa budynku:", poleNazwa,
                "Adres budynku:", poleAdres,
                "Typ budynku:", poleTyp
        };

        int wynik = JOptionPane.showConfirmDialog(this,pola,"Edytowanie informacji o budynku", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                model.setValueAt(poleNazwa.getText(), wybranyWiersz, 1);
                model.setValueAt(poleAdres.getText(), wybranyWiersz, 2);
                model.setValueAt(poleTyp.getText(), wybranyWiersz, 3);
                //TODO: aktualizacja bazy danych
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Wystąpił błąd: "+ e.getMessage(),"Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void usunBudynek() {

    }
}
