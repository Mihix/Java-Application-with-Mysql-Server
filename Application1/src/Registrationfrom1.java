import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Registrationfrom1 extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPasswordField pfConfirmPassword;
    private JPasswordField pfPassword;
    private JPanel RegisterPanel;

    public Registrationfrom1(JFrame parent) {
        super (parent);
        setTitle("create new Account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void RegisterUser() {
         String Name=tfName.getText();
         String Email=tfEmail.getText();
         String Phone=tfPhone.getText();
         String Address=tfAddress.getText();
         String Password=String.valueOf(pfPassword.getPassword());
         String ConfirmPassword=String.valueOf(pfConfirmPassword.getPassword());

         if (Name.isEmpty() || Email.isEmpty() || Phone.isEmpty() || Address.isEmpty() || Password.isEmpty()){
             JOptionPane.showMessageDialog(this,
                   "please enter all friends",
                     "Try again",
                     JOptionPane.ERROR_MESSAGE);
             return;

         }
         user = addUserToDatabase(Name,Email,Phone,Address,Password);
         if (user !=null){
             dispose();
         }
         else {
             JOptionPane.showMessageDialog(this,
                     "Failed to register new user",
            "Try again",
                     JOptionPane.ERROR_MESSAGE  );

         }
    }
    public User user;
    private User addUserToDatabase(String Name,String Email,String Phone,String Address,String Password){
        User user=null;
     final String DB_URL="jdbc:mysql://localhost/mystore?serverTimezone=UTC";
     final String USERNAME="root";
     final String PASSWORD="";

     try {
         Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
         Statement stmt=conn.createStatement();
         String sql="INSERT INTO users(name,email,phone,address,password)"+
                 "VALUES(?,?,?,?,?)";
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         preparedStatement.setString(1,Name);
         preparedStatement.setString(2,Email);
         preparedStatement.setString(3,Phone);
         preparedStatement.setString(4,Address);
         preparedStatement.setString(5,Password);


         int addeRows=preparedStatement.executeUpdate();
         if (addeRows>0){
             user=new User();
             user.Name=Name;
             user.Email=Email;
             user.Phone=Phone;
             user.Address=Address;
             user.Password=Password;


         }
         stmt.close();
         conn.close();


        }catch (Exception e){
         e.printStackTrace();
     }

        return user;
    }

    public static void main(String[] args) {
        Registrationfrom1 myfrom=new Registrationfrom1(null);
        User user=myfrom.user;
        if (user!=null){
            System.out.println("Successful regestration of :" + user.Name);
        }
        else {
            System.out.println("Regestration cancelled");
        }
    }
}
