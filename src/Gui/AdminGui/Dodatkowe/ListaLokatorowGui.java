package Gui.AdminGui.Dodatkowe;

import Gui.AdminGui.PanelAdministratora;
import Gui.TworzeniePrzyciskowGui;
import dao.AdministratorDAO.LokatorDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ListaLokatorowGui extends JFrame {

    private JPanel panelGlowny;
    private JTable tabelaLokatorow;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sortowanie;
    private JTextField polePesel;
    private JButton zamknijButton;

    private String[] kolumny = {"Imię", "Nazwisko", "PESEL"};
    LokatorDAO dao = new LokatorDAO();
    TworzeniePrzyciskowGui tworzeniePrzyciskowGui = new TworzeniePrzyciskowGui();

    public ListaLokatorowGui() {
        super("Lista Lokatorów");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1280, 960);
        setLocationRelativeTo(null);

        panelGlowny = new JPanel(new BorderLayout(10, 10));
        panelGlowny.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelGlowny.setBackground(Color.WHITE);

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawieniaListenerow();

        zaladujDaneZTabeli();

        setContentPane(panelGlowny);
        setVisible(true);
    }

    private void panelGorny() {
        JPanel panelGorny = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGorny.setBackground(Color.WHITE);
        panelGorny.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelLewy = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLewy.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-building-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);
        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("LISTA LOKATORÓW");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 38));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        panelLewy.add(zdjecieLabel);
        panelLewy.add(tekstLabel);

        JPanel panelPrawy = new JPanel();
        panelPrawy.setLayout(new BoxLayout(panelPrawy, BoxLayout.Y_AXIS));
        panelPrawy.setBackground(Color.WHITE);
        panelPrawy.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelPrawy.add(new JLabel("Wyszukaj po PESEL:"));
        polePesel = new JTextField(20);
        panelPrawy.add(polePesel);

        //dynamiczne filtrowanie tabeli
        polePesel.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrujTabelePoPesel(); }
            public void removeUpdate(DocumentEvent e) { filtrujTabelePoPesel(); }
            public void changedUpdate(DocumentEvent e) { filtrujTabelePoPesel(); }
        });

        panelGorny.add(panelLewy, BorderLayout.WEST);
        panelGorny.add(panelPrawy, BorderLayout.EAST);

        panelGlowny.add(panelGorny, BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        model = new DefaultTableModel(kolumny, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };


        tabelaLokatorow = new JTable(model);
        sortowanie = new TableRowSorter<>(model);
        tabelaLokatorow.setRowSorter(sortowanie);

        JScrollPane scrollPane = new JScrollPane(tabelaLokatorow);
        panelGlowny.add(scrollPane, BorderLayout.CENTER);
    }

    private void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1,1,15,0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        panelGlowny.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        zamknijButton = tworzeniePrzyciskowGui.tworzeniePrzycisku( "Zamknij", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(zamknijButton);
    }
    private void ustawieniaListenerow() {
        zamknijButton.addActionListener(e -> {
            dispose();
            new PanelAdministratora();
        });
    }

    private void filtrujTabelePoPesel() {
        String pesel = polePesel.getText();
        if (pesel.isEmpty()) {
            sortowanie.setRowFilter(null);
        } else {
            sortowanie.setRowFilter(RowFilter.regexFilter("^" + pesel, 2));
        }
    }

    private void zaladujDaneZTabeli() {
        model.setRowCount(0); //usunięcie starych danych
        try{
            List<Object[]> dane = dao.pobierzLokatorowZBazy();
            for (Object[] rzad : dane) {
                model.addRow(rzad);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych:", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}