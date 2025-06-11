package Gui.AdminGui.Dodatkowe;

import Gui.AdminGui.PanelAdministratora;
import resources.TworzenieGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PomieszczeniaGui extends JFrame {
    private JButton zaopatrzenieLokaluButton;
    private JPanel panelGlowny;
    private JPanel panelGorny;
    private JPanel panelSrodkowy;
    private JPanel panelDolny;
    private JTable tabela;
    private JButton informacje;
    private JComboBox<String> comboSortowanie;
    private TableRowSorter<DefaultTableModel> sortowanie;
    private JComboBox<String> kolumnaFiltrowanie;
    private JTextField poleWyszukiwania;
    private DefaultTableModel model;
    private JButton dodajPomieszczenie;
    private JButton usunPomieszcznie;
    private JButton powrot;


    TworzenieGUI tworzenieGUI = new TworzenieGUI();
    private String[] kolumny = {
            "ID pokoju", "Budynek", "Adres", "Typ pomieszczenia","Czy zajęte",
            "Cena czynszu","Cena zakupu"
    };


    public PomieszczeniaGui() {
        panelGlowny = new JPanel(new BorderLayout(10,10));
        panelGlowny.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelGlowny.setBackground(Color.WHITE);

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        this.setContentPane(panelGlowny);
        this.setTitle("Lista pomieszczeń");
        this.setSize(1280,960);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void panelGorny() {
        panelGorny = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGorny.setBackground(Color.WHITE);

        //id pokoju, budynek, adres, typ pomieszczenia(opcjonalnie), czy zajęte, cena zakpupu/ czynszu
        String[] opcjeSortowania = {
                "ID (rosnąco)","ID (malejąco)","Budynek (A-Z)","Budynek (Z-A)","Adres (A-Z)","Adres (Z-A)",
                "Typ (A-Z)","Typ (Z-A)","Czy zajęte (Nie)","Czy zajęte (Tak)","Cena czynszu (od najmniejszej)",
                "Cena czynszu (od największej)","Cena zakupu (od największej)","Cena zakupu (od najmniejszej)"
        };
        comboSortowanie = new JComboBox<>(opcjeSortowania);
        comboSortowanie.setSelectedIndex(0);
        comboSortowanie.addActionListener(e -> sortujTabele());
        panelGorny.add(new JLabel("Sortowanie według:"));
        panelGorny.add(comboSortowanie);

        kolumnaFiltrowanie = new  JComboBox<>(kolumny);
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

        panelGlowny.add(panelGorny, BorderLayout.NORTH);
    }
    private void panelSrodkowy() {
        panelSrodkowy = new JPanel(new GridLayout(1,7,15,0));
        panelSrodkowy.setBorder(BorderFactory.createLineBorder(Color.black));
        panelSrodkowy.setBackground(Color.WHITE);

        //TODO: zaladowanie bazy danych -> pomieszczenia
        Object[][] data = {
                {1, "Budynek A", "ul. Przykładowa 1", "Biuro", "Tak", 3000,25300},
                {2, "Budynek A", "ul. Przykładowa 1", "Mieszkanie", "Tak", 26000,200000},
                {2, "Budynek A", "ul. Przykładowa 1", "Mieszkanie", "Nie", 3060,200000},
                {2, "Budynek A", "ul. Przykładowa 1", "Mieszkanie", "Nie", 4000,457000},
        };
        model = new DefaultTableModel(data, kolumny){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //dzięki temu żadna komórka nie jest możliwa do edycji w tabeli
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class; // ID pokoju
                    case 5: return Integer.class; // Cena czynszu
                    case 6: return Integer.class; // Cena zakupu
                    default: return String.class;
                }
            }
        };

        tabela = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabela.setRowSorter(sortowanie);

        //Opcja scrollowania
        JScrollPane scrollPane = new JScrollPane(tabela);
        panelSrodkowy.add(scrollPane,BorderLayout.CENTER);

        panelGlowny.add(panelSrodkowy,BorderLayout.CENTER);
    }
    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        panelGlowny.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        informacje = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Informacje", new Color(37, 37, 37),new Color(255,255,255), "/resources/figures/icons8-building2-100.png");
        dodajPomieszczenie = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Dodaj pokój", new Color(2, 80, 253),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        usunPomieszcznie = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Usuń pokój", new Color(253, 236, 7),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        powrot = tworzenieGUI.tworzeniePrzycisku("Powrót", new Color(1, 255, 31 ), new Color(255,255,255));

        buttonPanel.add(informacje);
        buttonPanel.add(dodajPomieszczenie);
        buttonPanel.add(usunPomieszcznie);
        buttonPanel.add(powrot);
    }
    private void ustawienieListenerow() {
        powrot.addActionListener(e -> {
            dispose();
            new PanelAdministratora();
            System.out.println("Przechodzę do panelu administratora");
        });
        informacje.addActionListener(e -> {
            int wybranyWiersz = tabela.getSelectedRow();
            if(wybranyWiersz==-1){
                JOptionPane.showMessageDialog(this,"Proszę zaznaczyć pokój z tabeli","Informacja",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int wierszModelu = tabela.convertRowIndexToModel(wybranyWiersz);

        });
    }

    private void sortujTabele() {
        String wybrane = (String) comboSortowanie.getSelectedItem();

        if (wybrane == null || sortowanie == null) return;

        List<RowSorter.SortKey> sortowanieKluczy = new ArrayList<>();

        switch (wybrane) {
            //"ID (rosnąco)","ID (malejąco)","Budynek (A-Z)","Budynek (Z-A)","Adres (A-Z)","Adres (Z-A)",
            //"Typ (A-Z)","Typ (Z-A)","Czy zajęte (Nie)","Czy zajęte (Tak)","Cena czynszu (od najmniejszej)",
            //"Cena czynszu (od największej)","Cena zakupu (od największej)","Cena zakupu (od najmniejszej)"
            case "ID (rosnąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "ID (malejąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Budynek (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Budynek (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Adres (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Adres (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Typ (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Typ (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Czy zajęte (Nie)":
                sortowanieKluczy.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Czy zajęte (Tak)":
                sortowanieKluczy.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Cena czynszu (od najmniejszej)":
                sortowanieKluczy.add(new RowSorter.SortKey(5, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Cena czynszu (od największej)":
                sortowanieKluczy.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Cena zakupu (od najmniejszej)":
                sortowanieKluczy.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Cena zakupu (od największej)":
                sortowanieKluczy.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
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
}
