package Gui.AdminGui.Dodatkowe;

import Gui.AdminGui.PanelAdministratora;
import dao.AdministratorDAO.PomieszczeniaDAO;
import Gui.TworzeniePrzyciskowGui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PomieszczeniaGui extends JFrame {
    private JPanel panelGlowny;
    private JPanel panelGorny;
    private JPanel panelSrodkowy;
    private JTable tabela;
    private JButton informacje;
    private JComboBox<String> comboSortowanie;
    private TableRowSorter<DefaultTableModel> sortowanie;
    private JComboBox<String> kolumnaFiltrowanie;
    private JTextField poleWyszukiwania;
    private DefaultTableModel model;
    private JButton dodajPomieszczenie;
    private JButton usunPomieszcznie;
    private JButton edytujPomieszczenie;
    private JButton powrot;


    TworzeniePrzyciskowGui tworzenieGUI = new TworzeniePrzyciskowGui();
    private String[] kolumny = {
            "ID pokoju", "ID budynku", "Typ pomieszczenia","Czy zajęte",
            "Cena czynszu","Cena zakupu"
    };
    PomieszczeniaDAO dao = new PomieszczeniaDAO();
    InformacjeOLokatorzeGui lokatorGui = new InformacjeOLokatorzeGui();


    public PomieszczeniaGui() {
        panelGlowny = new JPanel(new BorderLayout(10,10));
        panelGlowny.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelGlowny.setBackground(Color.LIGHT_GRAY);

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(panelGlowny);
        this.setTitle("Lista pomieszczeń");
        this.setSize(1280,960);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void panelGorny() {
        //panel główny
        panelGorny = new JPanel(new BorderLayout(10, 10));
        panelGorny.setBackground(Color.WHITE);
        panelGorny.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //panel z ikoną i napisem
        JPanel panelLewy = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLewy.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-room-100.png"));
        Image zeskalowane = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel zdjecieLabel = new JLabel(new ImageIcon(zeskalowane));
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("LISTA POMIESZCZEŃ ");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 40));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        panelLewy.add(zdjecieLabel);
        panelLewy.add(tekstLabel);

        //panel z wyszukiwaniem i sortowaniem
        JPanel panelPrawy = new JPanel();
        panelPrawy.setLayout(new BoxLayout(panelPrawy, BoxLayout.Y_AXIS));
        panelPrawy.setBackground(Color.WHITE);
        panelPrawy.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //sortowanie
        JPanel panelSortowanie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSortowanie.setBackground(Color.WHITE);

        panelSortowanie.add(new JLabel("Sortowanie według:"));
        String[] opcjeSortowania = {
                "ID (rosnąco)", "ID (malejąco)",
                "Nazwa Budynku (A-Z)", "Nazwa Budynku (Z-A)",
                "Adres Budynku (A-Z)", "Adres Budynku (Z-A)",
                "Typ Budynku (A-Z)", "Typ Budynku (Z-A)"
        };
        comboSortowanie = new JComboBox<>(opcjeSortowania);
        comboSortowanie.setSelectedIndex(0);
        comboSortowanie.addActionListener(e -> sortujTabele());

        panelSortowanie.add(comboSortowanie);
        panelPrawy.add(panelSortowanie);

        //wyszukiwanie
        JPanel panelWyszukiwania = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelWyszukiwania.setBackground(Color.WHITE);

        kolumnaFiltrowanie = new JComboBox<>(kolumny);
        panelWyszukiwania.add(new JLabel("Szukaj po:"));
        panelWyszukiwania.add(kolumnaFiltrowanie);
        poleWyszukiwania = new JTextField(15);
        panelWyszukiwania.add(poleWyszukiwania);

        poleWyszukiwania.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrujTabele(); }
            public void removeUpdate(DocumentEvent e) { filtrujTabele(); }
            public void changedUpdate(DocumentEvent e) { filtrujTabele(); }
        });

        panelPrawy.add(panelWyszukiwania);

        //scalenie wszystkiego w 1
        panelGorny.add(panelLewy, BorderLayout.WEST);
        panelGorny.add(panelPrawy, BorderLayout.EAST);

        panelGlowny.add(panelGorny, BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        panelSrodkowy = new JPanel(new GridLayout(1,7,15,0));
        panelSrodkowy.setBorder(BorderFactory.createLineBorder(Color.black));
        panelSrodkowy.setBackground(Color.WHITE);


        model = new DefaultTableModel(kolumny,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //dzięki temu żadna komórka nie jest możliwa do edycji w tabeli
            }
        };

        tabela = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabela.setRowSorter(sortowanie);

        //opcja scrollowania
        JScrollPane scrollPane = new JScrollPane(tabela);
        panelSrodkowy.add(scrollPane,BorderLayout.CENTER);

        panelGlowny.add(panelSrodkowy,BorderLayout.CENTER);

        zaladujDaneZTabeli();
    }
    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        panelGlowny.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        informacje = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Informacje", new Color(57, 122, 255),new Color(255,255,255), "/resources/icons/icons8-info-100.png");
        dodajPomieszczenie = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Dodaj pokój", new Color(5, 189, 13),new Color(255,255,255), "/resources/icons/icons8-add-100.png");
        edytujPomieszczenie = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Edytuj pokój", new Color(223, 163, 57),new Color(255,255,255), "/resources/icons/icons8-edit-100.png");
        usunPomieszcznie = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Usuń pokój", new Color(204, 12, 12),new Color(255,255,255), "/resources/icons/icons8-delete-100.png");
        powrot = tworzenieGUI.tworzeniePrzycisku("Powrót", new Color(129, 129, 129), new Color(255,255,255));

        buttonPanel.add(informacje);
        buttonPanel.add(dodajPomieszczenie);
        buttonPanel.add(edytujPomieszczenie);
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
            int idPokoju = (int) tabela.getModel().getValueAt(wierszModelu,0);

            lokatorGui.zarzadzajLokatorem(idPokoju);
        });
        dodajPomieszczenie.addActionListener(e -> {
            System.out.println("Dodawanie pomieszczenia");
            dodajPomieszczenie();
        });
        edytujPomieszczenie.addActionListener(e -> {
            System.out.println("Edytowanie pomieszczenia");
            edytujPomieszczenie();
        });
        usunPomieszcznie.addActionListener(e -> {
            System.out.println("Usuwanie pomieszczenia");
            usunPomieszczenie();
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

    private void dodajPomieszczenie() {
        JTextField poleIdBudynek = new JTextField();
        JTextField poleTyp = new JTextField();
        JTextField poleCzyZajete = new JTextField();
        JTextField poleCenaCzynszu = new JTextField();
        JTextField poleCenaZakupu = new JTextField();

        Object[] pola = {
                "ID budynku", poleIdBudynek,
                "Typ pomieszczenia:", poleTyp,
                "Czy zajęte:", poleCzyZajete,
                "Cena czynszu:", poleCenaCzynszu,
                "Cena zakupu:", poleCenaZakupu
        };

        int wynik = JOptionPane.showConfirmDialog(this,pola,"Dodaj informacje o budynku", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                String tekstIdBudynek = poleIdBudynek.getText().trim();
                String typ = poleTyp.getText().trim();
                String czyZajete = poleCzyZajete.getText().trim();
                String tekstCenaCzynszu = poleCenaCzynszu.getText().trim();
                String tekstCenaZakupu = poleCenaZakupu.getText().trim();

                if (tekstIdBudynek.isEmpty() || typ.isEmpty() || czyZajete.isEmpty() || tekstCenaCzynszu.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Wypełnij wszystkie wymagane pola!", "Brak danych", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int idBudynek = Integer.parseInt(tekstIdBudynek);
                int cenaCzynszu = Integer.parseInt(tekstCenaCzynszu);
                Integer cenaZakupu = tekstCenaZakupu.isEmpty() ? null : Integer.parseInt(tekstCenaZakupu);

                dao.dodajPomieszczenie(idBudynek, typ, czyZajete, cenaCzynszu, cenaZakupu);
                zaladujDaneZTabeli();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void edytujPomieszczenie() {
        int wybranyWiersz = tabela.getSelectedRow();
        if (wybranyWiersz == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz pomieszczenie do edycji.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }
        wybranyWiersz = tabela.convertRowIndexToModel(wybranyWiersz);

        Object id = model.getValueAt(wybranyWiersz, 0);
        int idBudynek = (int) model.getValueAt(wybranyWiersz, 1);
        String typ = (String) model.getValueAt(wybranyWiersz, 2);
        String czyZajete = (String) model.getValueAt(wybranyWiersz, 3);
        int cenaCzynszu = (int) model.getValueAt(wybranyWiersz, 4);
        Object cenaZakupuObj = model.getValueAt(wybranyWiersz, 5);
        String cenaZakupuStr = (cenaZakupuObj == null) ? "" : cenaZakupuObj.toString();

        JTextField poleIdBudynek = new JTextField(String.valueOf(idBudynek));
        JTextField poleTyp = new JTextField(typ);
        JTextField poleCzyZajete = new JTextField(czyZajete);
        JTextField poleCenaCzynszu = new JTextField(String.valueOf(cenaCzynszu));
        JTextField poleCenaZakupu = new JTextField(cenaZakupuStr);

        Object[] pola = {
                "ID budynku:", poleIdBudynek,
                "Typ pomieszczenia:", poleTyp,
                "Czy zajęte:", poleCzyZajete,
                "Cena czynszu:", poleCenaCzynszu,
                "Cena zakupu:", poleCenaZakupu
        };

        int wynik = JOptionPane.showConfirmDialog(this, pola, "Edytowanie informacji o pomieszczeniu", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                String tekstIdBudynek = poleIdBudynek.getText().trim();
                String nowyTyp = poleTyp.getText().trim();
                String noweCzyZajete = poleCzyZajete.getText().trim();
                String tekstCenaCzynszu = poleCenaCzynszu.getText().trim();
                String tekstCenaZakupu = poleCenaZakupu.getText().trim();

                if (tekstIdBudynek.isEmpty() || nowyTyp.isEmpty() || noweCzyZajete.isEmpty() || tekstCenaCzynszu.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Wypełnij wszystkie wymagane pola!", "Brak danych", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int nowyIdBudynek = Integer.parseInt(tekstIdBudynek);
                int nowaCenaCzynszu = Integer.parseInt(tekstCenaCzynszu);
                Integer nowaCenaZakupu = tekstCenaZakupu.isEmpty() ? null : Integer.parseInt(tekstCenaZakupu);

                //aktualizacja w modelu tabeli
                model.setValueAt(nowyIdBudynek, wybranyWiersz, 1);
                model.setValueAt(nowyTyp, wybranyWiersz, 2);
                model.setValueAt(noweCzyZajete, wybranyWiersz, 3);
                model.setValueAt(nowaCenaCzynszu, wybranyWiersz, 4);
                model.setValueAt(nowaCenaZakupu, wybranyWiersz, 5);

                dao.aktualizujPomieszczenie((Integer) id, nowyIdBudynek, nowyTyp, noweCzyZajete, nowaCenaCzynszu, nowaCenaZakupu);
                zaladujDaneZTabeli();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void usunPomieszczenie() {
        int wybranyWiersz = tabela.getSelectedRow();
        if (wybranyWiersz == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz pomieszczenie do usunięcia.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }
        wybranyWiersz = tabela.convertRowIndexToModel(wybranyWiersz);

        int wynik = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć to pomieszczenie?", "Potwierdzenie", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                int id = (int) model.getValueAt(wybranyWiersz, 0);
                dao.usunPomieszczenie(id);
                model.removeRow(wybranyWiersz);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private void zaladujDaneZTabeli() {
        model.setRowCount(0); //usunięcie starych danych
        try{
            for (Object[] row : dao.pobierzWszystkiePomieszczenia()) {
                model.addRow(row);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych:", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

}
