package Gui.AdminGui.Dodatkowe;

import Gui.AdminGui.PanelAdministratora;
import dao.AdministratorDAO.BudynekDAO;
import Gui.TworzeniePrzyciskowGui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class BudynekGui extends JFrame {
    private JPanel budynekGui;
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
    private String[] kolumny = {"ID", "Nazwa Budynku", "Adres Budynku","Typ Budynku"};
    TworzeniePrzyciskowGui tworzenieGUI = new TworzeniePrzyciskowGui();
    BudynekDAO dao = new BudynekDAO();

    public BudynekGui() {
        super("Lista budynków");
        budynekGui = new JPanel(new BorderLayout(10,10));
        budynekGui.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        budynekGui.setBackground(Color.LIGHT_GRAY);

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(budynekGui);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void panelGorny() {
        JPanel panelGorny = new JPanel(new BorderLayout(10, 10));
        panelGorny.setBackground(Color.WHITE);
        panelGorny.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //panel z logo i tytułem
        JPanel panelLewy = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLewy.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-building-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);
        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("LISTA BUDYNKÓW");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 38));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        panelLewy.add(zdjecieLabel);
        panelLewy.add(tekstLabel);

        //panel z sortowaniem i wyszukiwaniem
        JPanel panelPrawy = new JPanel();
        panelPrawy.setLayout(new BoxLayout(panelPrawy, BoxLayout.Y_AXIS));
        panelPrawy.setBackground(Color.WHITE);
        panelPrawy.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //sortowanie
        JPanel panelSortowanie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSortowanie.setBackground(Color.WHITE);
        panelSortowanie.add(new JLabel("Sortowanie według: "));
        String[] opcjeSortowania = {
                "ID (rosnąco)", "ID (malejąco)",
                "Nazwa (A-Z)", "Nazwa (Z-A)",
                "Adres (A-Z)", "Adres (Z-A)",
                "Typ (A-Z)", "Typ (Z-A)"
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
            @Override
            public void insertUpdate(DocumentEvent e) { filtrujTabele(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrujTabele(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrujTabele(); }
        });

        //dodawanie wszystkiego na swoje miejsce
        panelPrawy.add(panelWyszukiwania);

        panelGorny.add(panelLewy, BorderLayout.WEST);
        panelGorny.add(panelPrawy, BorderLayout.EAST);

        budynekGui.add(panelGorny, BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        panelSrodkowy = new JPanel(new GridLayout(1, 4, 15, 0));
        panelSrodkowy.setBorder(BorderFactory.createLineBorder(Color.black));
        panelSrodkowy.setBackground(Color.WHITE);

        model = new DefaultTableModel(kolumny,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //dzięki temu żadna komórka nie jest możliwa do edycji w tabeli
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Integer.class : String.class;
            }
        };

        tabela = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabela.setRowSorter(sortowanie);

        //opcja scrollowania
        JScrollPane scrollPane = new JScrollPane(tabela);
        panelSrodkowy.add(scrollPane,BorderLayout.CENTER);
        budynekGui.add(panelSrodkowy,BorderLayout.CENTER);

        zaladujDaneZTabeli();
    }

    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        budynekGui.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        dodajBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Dodaj budynek", new Color(5, 189, 13),new Color(255,255,255), "/resources/icons/icons8-add-100.png");
        edytujBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Edytuj budynek", new Color(223, 163, 57),new Color(255,255,255), "/resources/icons/icons8-edit-100.png");
        usunBudynek = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Usuń budynek", new Color(204, 61, 61),new Color(255,255,255), "/resources/icons/icons8-delete-100.png");
        powrot = tworzenieGUI.tworzeniePrzycisku("Powrót", new Color(129, 129, 129), new Color(255,255,255));

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
                sortowanieKluczy.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                System.out.println("Sortowanie poprzez: " + wybrane);
                break;
            case "Nazwa (Z-A)":
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
            sortowanie.setRowFilter(RowFilter.regexFilter("(?i)" + tekst, kolumna)); //ignoruje wielkość liter
        }
    }

    private void dodajBudynek() {
        JTextField poleNazwa = new JTextField();
        JTextField poleAdres = new JTextField();
        JTextField poleTyp = new JTextField();

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
                if (nazwa.isEmpty() || adres.isEmpty() || typ.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Wypełnij wszystkie wymagane pola!", "Brak danych", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dao.dodajBudynek(nazwa, adres, typ);
                zaladujDaneZTabeli();

            } catch (SQLException e) {
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
                if(nazwa.isEmpty() || adres.isEmpty() || typ.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Wypełnij wszystkie wymagane pola!", "Brak danych", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dao.aktualizujBudynek((Integer) id,poleNazwa.getText(),poleAdres.getText(),poleTyp.getText());
                zaladujDaneZTabeli();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,"Wystąpił błąd: "+ e.getMessage(),"Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void usunBudynek() {
        int wybranyWiersz = tabela.getSelectedRow();
        if (wybranyWiersz == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz budynek do usunięcia.","Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }
        wybranyWiersz = tabela.convertRowIndexToModel(wybranyWiersz);

        int wynik = JOptionPane.showConfirmDialog(this, "Czy napewno chcesz usunąć ten budynek?", "Potwierdzenie", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                int id = (int) model.getValueAt(wybranyWiersz, 0);

                dao.usunBudynek(id);
                model.removeRow(wybranyWiersz);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd: "+ e.getMessage(),"Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void zaladujDaneZTabeli() {
        model.setRowCount(0); //usunięcie starych danych
        try{
            for (Object[] rzad : dao.pobierzWszystkieBudynki()) {
                model.addRow(rzad);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych:", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}