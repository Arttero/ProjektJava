package Gui.UserGui.Dodatkowe;

import Gui.TworzeniePrzyciskowGui;
import Gui.UserGui.PanelUzytkownika;
import dao.RachunkiDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class OplatyUserGui extends JFrame {

    private JPanel panelOplat;
    private JButton oplacRachunekButton;
    private JButton powrotButton;
    private JTable tabelaOplat;
    private DefaultTableModel modelOplat;
    private int idUzytkownika;
    private Connection connection;
    private RachunkiDAO rachunkiDAO;

    TworzeniePrzyciskowGui tworzeniePrzyciskowGui = new TworzeniePrzyciskowGui();

    public OplatyUserGui(int idUzytkownika, Connection connection) {
        super("Opłaty");
        this.idUzytkownika = idUzytkownika;
        this.connection = connection;
        this.rachunkiDAO = new RachunkiDAO(connection);
        panelOplat = new JPanel(new BorderLayout(10,10));
        panelOplat.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelOplat.setBackground(Color.LIGHT_GRAY);

        panelZNapisem();
        panelZTabelaOplat();
        panelDolny();
        ustawienieListenerow();


        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(panelOplat);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        System.out.println("ID użytkownika: " + idUzytkownika);
    }

    private void panelZNapisem() {
        JPanel panelNapis = new JPanel();
        panelNapis.setLayout(new BoxLayout(panelNapis,BoxLayout.X_AXIS));
        panelNapis.setBackground(Color.WHITE);
        panelNapis.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-money-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);

        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("OPŁATY");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 50));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        panelNapis.add(zdjecieLabel);
        panelNapis.add(tekstLabel);


        panelOplat.add(panelNapis,BorderLayout.NORTH);
    }

    private void panelZTabelaOplat(){
        String[] kolumny = {"ID","ID Pomieszczenia","Opis","Kwota","Termin płatności","Status"};

        modelOplat = new DefaultTableModel(kolumny, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;//brak możliwości edycji tabeli
            }
        };
        List<Object[]> daneOplat = rachunkiDAO.pobierzOplatyDlaUzytkownika(idUzytkownika);
        for(Object[] rzad : daneOplat) {
            modelOplat.addRow(rzad);
        }

        tabelaOplat = new JTable(modelOplat);
        JScrollPane scroll = new JScrollPane(tabelaOplat);
        panelOplat.add(scroll, BorderLayout.CENTER);


    }

    private void panelDolny(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        panelOplat.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        oplacRachunekButton = tworzeniePrzyciskowGui.tworzeniePrzyciskuZeZdjeciem("Opłać wybrany rachunek", new Color(5, 189, 13),new Color(255,255,255), "/resources/icons/icons8-money-100.png");
        powrotButton = tworzeniePrzyciskowGui.tworzeniePrzycisku("Powrót", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(oplacRachunekButton);
        buttonPanel.add(powrotButton);
    }

    private void ustawienieListenerow() {
        powrotButton.addActionListener(e -> {
            dispose();
            System.out.println("Przechodzę do panelu użytkownika");
            new PanelUzytkownika(idUzytkownika, connection);
        });
        oplacRachunekButton.addActionListener(e -> {
            int zaznaczony = tabelaOplat.getSelectedRow();
            if(zaznaczony == -1) {
                JOptionPane.showMessageDialog(this, "Zaznacz rachunek do opłacenia!");
                return;
            }
            String status = (String) modelOplat.getValueAt(zaznaczony, 5);
            if("OPŁACONY".equals(status)) {
                JOptionPane.showMessageDialog(this, "Ten rachunek jest już opłacony.","Informacja",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int idRachunku = (int) modelOplat.getValueAt(zaznaczony, 0);

            rachunkiDAO.oplacRachunek(idRachunku); //zapis do bazy danych
            modelOplat.setValueAt("OPŁACONY", zaznaczony, 5); //zapis do GUI
        });
    }
}
