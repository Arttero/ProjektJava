import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class rejestracja extends JFrame{
    private JPanel rejestracjaJPanel;
    private JLabel ImieJLabel;
    private JLabel NazwiskoJLabel;
    private JLabel DatUrJLabel;
    private JLabel TelJLabel;
    private JLabel EmailJLabel;
    private JTextField ImieJTF;
    private JTextField NazwJTF;
    private JTextField DataUrJTF;
    private JTextField TelJTF;
    private JTextField EmailJTF;
    private JButton zajerestrujButton;
    private JTextField LoginJTF;
    private JPasswordField HasloJTF;
    private JLabel LoginJLabel;
    private JLabel HasloJLabel;


    public rejestracja() {
        super("Rejestracja");
        this.setContentPane(this.rejestracjaJPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 400, height = 300;
        this.setSize(width, height);
        zajerestrujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sprawdzenie czy pola nie są puste
                if (ImieJTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Wpisz swoje imię!","Błąd",JOptionPane.WARNING_MESSAGE);
                }
                else if (NazwJTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Wpisz swoje nazwisko!","Błąd",JOptionPane.WARNING_MESSAGE);
                }
                else if (DataUrJTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Podaj date urodzenia!","Błąd",JOptionPane.WARNING_MESSAGE);
                }
                else if (TelJTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Wpisz swój numer telefonu!",
                            "Błąd", JOptionPane.WARNING_MESSAGE);
                }
                else if (EmailJTF.getText().isEmpty()) {

                }
                else if (LoginJTF.getText().isEmpty()) {

                }
                else if (new String(HasloJTF.getPassword()).isEmpty()){

                }
                try {

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Błąd: " + ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
                }
                setVisible(false);
                logowanie logowanie = new logowanie();
                logowanie.setVisible(true);
            }
        });
    }
}
