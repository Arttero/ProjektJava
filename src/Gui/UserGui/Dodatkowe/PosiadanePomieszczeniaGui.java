package Gui.UserGui.Dodatkowe;

import Gui.TworzeniePrzyciskowGui;
import Gui.UserGui.PanelUzytkownika;
import dao.UzytkownikDAO.PosiadanePomieszczeniaDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PosiadanePomieszczeniaGui extends JFrame {
    private JPanel panelPosiadane;
    private JTable tabelaPomieszczen;
    private DefaultTableModel modelPomieszczen;
    private JButton zamknijButton;
    private int idLogowania;
    private Connection connection;
    private JComboBox<String> comboSortowanie;
    private TableRowSorter<DefaultTableModel> sortowanie;
    private JTextField poleWyszukiwania;
    private JComboBox<String> kolumnaFiltrowanie;



    private String[] kolumny = {"ID budynku", "Adres budynku", "Typ budynku", "ID pokoju"};
    TworzeniePrzyciskowGui tworzeniePrzyciskowGui = new TworzeniePrzyciskowGui();

    public PosiadanePomieszczeniaGui(int idLogowania, Connection connection) {
        super("Lista posiadanych pomieszczeń");
        this.idLogowania = idLogowania;
        this.connection = connection;
        panelPosiadane = new JPanel(new BorderLayout(10,10));
        panelPosiadane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelPosiadane.setBackground(Color.LIGHT_GRAY);

        panelGorny();
        panelZTabelaPomieszczen();
        panelDolny();
        ustawieniaListenerow();

        this.setContentPane(panelPosiadane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void panelGorny() {
        JPanel panelGorny = new JPanel(new BorderLayout(10, 10));
        panelGorny.setBackground(Color.WHITE);
        panelGorny.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //panel z ikoną i napisem
        JPanel panelLewy = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLewy.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-building-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);
        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("POSIADANE POMIESZCZENIA");

        tekstLabel.setFont(new Font("Arial", Font.BOLD, 40));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        panelLewy.add(zdjecieLabel);
        panelLewy.add(tekstLabel);

        panelGorny.add(panelLewy, BorderLayout.WEST);

        //panel z sortowaniem i wyszukiwaniem
        JPanel panelPrawy = new JPanel();
        panelPrawy.setLayout(new BoxLayout(panelPrawy, BoxLayout.Y_AXIS)); // ułożenie od góry
        panelPrawy.setBackground(Color.WHITE);
        panelPrawy.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //sortowanie
        JPanel panelSortowanie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSortowanie.setBackground(Color.WHITE);
        panelSortowanie.add(new JLabel("Sortowanie według:"));
        String[] opcjeSortowania = {
                "ID budynku (rosnąco)",
                "ID budynku (malejąco)",
                "Adres (A-Z)",
                "Adres (Z-A)",
                "Typ budynku (A-Z)",
                "Typ budynku (Z-A)",
                "ID pokoju (rosnąco)",
                "ID pokoju (malejąco)"
        };
        comboSortowanie = new JComboBox<>(opcjeSortowania);
        comboSortowanie.setSelectedIndex(0);
        comboSortowanie.addActionListener(e -> sortujTabele());
        panelSortowanie.add(comboSortowanie);
        panelPrawy.add(panelSortowanie);

        //wyszukiwanie
        JPanel panelWyszukiwania = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelWyszukiwania.setBackground(Color.WHITE);
        panelWyszukiwania.add(new JLabel("Szukaj:"));
        kolumnaFiltrowanie = new JComboBox<>(kolumny);
        panelWyszukiwania.add(kolumnaFiltrowanie);
        poleWyszukiwania = new JTextField(15);
        panelWyszukiwania.add(poleWyszukiwania);

        poleWyszukiwania.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrujTabele(); }
            public void removeUpdate(DocumentEvent e) { filtrujTabele(); }
            public void changedUpdate(DocumentEvent e) { filtrujTabele(); }
        });

        panelPrawy.add(panelWyszukiwania);

        panelGorny.add(panelPrawy, BorderLayout.EAST);

        panelPosiadane.add(panelGorny, BorderLayout.NORTH);
    }


    private void panelZTabelaPomieszczen() {
        modelPomieszczen = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        PosiadanePomieszczeniaDAO pomieszczeniaDAO = new PosiadanePomieszczeniaDAO();
        List<Object[]> lista = pomieszczeniaDAO.pobierzPosiadanePomieszczeniaUzytkownika(idLogowania, connection);
        for (Object[] row : lista) modelPomieszczen.addRow(row);

        tabelaPomieszczen = new JTable(modelPomieszczen);
        sortowanie = new TableRowSorter<>(modelPomieszczen);
        tabelaPomieszczen.setRowSorter(sortowanie);

        JScrollPane scrollPane = new JScrollPane(tabelaPomieszczen);
        panelPosiadane.add(scrollPane, BorderLayout.CENTER);

        //wywołanie sortowania na starcie
        sortujTabele();

    }

    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1,1,15,0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        panelPosiadane.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        zamknijButton = tworzeniePrzyciskowGui.tworzeniePrzycisku( "Zamknij", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(zamknijButton);
    }

    private void ustawieniaListenerow() {
        zamknijButton.addActionListener(e -> {
            dispose();
            new PanelUzytkownika(idLogowania, connection);
        });
    }

    private void sortujTabele() {
        if (sortowanie == null || comboSortowanie == null) return;

        String wybrane = (String) comboSortowanie.getSelectedItem();
        List<RowSorter.SortKey> sortowanieKluczy = new ArrayList<>();

        if (wybrane == null) return;

        switch(wybrane) {
            case "ID budynku (rosnąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                break;
            case "ID budynku (malejąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
                break;
            case "Adres (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                break;
            case "Adres (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
                break;
            case "Typ budynku (A-Z)":
                sortowanieKluczy.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                break;
            case "Typ budynku (Z-A)":
                sortowanieKluczy.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
                break;
            case "ID pokoju (rosnąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
                break;
            case "ID pokoju (malejąco)":
                sortowanieKluczy.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
                break;
        }

        sortowanie.setSortKeys(sortowanieKluczy);
        sortowanie.sort();

    }

    private void filtrujTabele() {
        String tekst = poleWyszukiwania.getText();
        int wybranaKolumna = kolumnaFiltrowanie.getSelectedIndex();
        if (tekst.length() == 0) {
            sortowanie.setRowFilter(null);
        } else {
            sortowanie.setRowFilter(RowFilter.regexFilter("(?i)" + tekst, wybranaKolumna));
        }
    }
}