package Gui.AdminGui;

import Gui.GuiCreator;

import javax.swing.*;
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


    GuiCreator guiCreator = new GuiCreator();

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



    private void panelDolny() {

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        budynekGui.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        dodajBudynek = guiCreator.createIconButton("Dodaj budynek", new Color(37, 37, 37),new Color(255,255,255), "/figures/icons8-building2-100.png");
        edytujBudynek = guiCreator.createIconButton("Edytuj budynek", new Color(2, 80, 253),new Color(255,255,255), "/figures/icons8-building-100.png");
        usunBudynek = guiCreator.createIconButton("Usuń budynek", new Color(253, 236, 7),new Color(255,255,255), "/figures/icons8-building-100.png");
        powrot = guiCreator.createButton("Powrót", new Color(1, 255, 31 ), new Color(255,255,255));

        buttonPanel.add(dodajBudynek);
        buttonPanel.add(edytujBudynek);
        buttonPanel.add(usunBudynek);
        buttonPanel.add(powrot);

    }

    private void panelGorny() {
        panelGorny = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGorny.setBackground(Color.WHITE);

        String[] opcjeSortowania = {
                "ID (rosnąco)","ID (malejąco)","Nazwa (A-Z)","Nazwa (Z-A)","Adres (A-Z)","Adres (Z-A)",
                "Typ (A-Z)","Typ (Z-A)"
        };

        comboSortowanie = new JComboBox<>(opcjeSortowania);
        comboSortowanie.setSelectedIndex(0);
        comboSortowanie.addActionListener(e -> sortujTabele());

        panelGorny.add(new JLabel("Sortowanie według:"));
        panelGorny.add(comboSortowanie);

        budynekGui.add(panelGorny, BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        panelSrodkowy = new JPanel(new GridLayout(1, 4, 15, 0));
        panelSrodkowy.setBorder(BorderFactory.createLineBorder(Color.black));
        panelSrodkowy.setBackground(Color.WHITE);


        String[] nazwyKolumn = {"ID", "Nazwa Budynku", "Adres Budynku","Typ Budynku"};

        Object[][] data = {
                {1, "Budynek A", "ul. Test 1","Biurowiec"},
                {2, "Budynek B", "ul. Test 2","Budynek mieszkalny"}
        };

        model = new DefaultTableModel(data, nazwyKolumn);
        tabela = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabela.setRowSorter(sortowanie);

        JScrollPane scrollPane = new JScrollPane(tabela);
        panelSrodkowy.add(scrollPane,BorderLayout.CENTER);

        budynekGui.add(panelSrodkowy,BorderLayout.CENTER);
    }


    private void ustawienieListenerow() {
        powrot.addActionListener(e -> {
            dispose();
            new PanelAdministratora();
            System.out.println("Przechodzę do panelu administratora");
        });
    }

    private void sortujTabele() {
        String wybrane = (String) comboSortowanie.getSelectedItem();

        if (wybrane == null || sortowanie == null) return;

        List<RowSorter.SortKey> sortowanieKluczy = new ArrayList<>();

        switch(wybrane) {
            //"ID (rosnąco)","ID (malejąco)","Nazwa (A-Z)","Nazwa (Z-A)","Adres (A-Z)","Adres (Z-A)",
            //                "Typ (A-Z)","Typ (Z-A)"
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
}
