package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.sql.DataSource;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

import static application.database.*;


public class Controller implements Initializable {


    String algorithm = "AES/CBC/PKCS5Padding";
    int saltLength = 16;
    String salt = application.encryption.generateSalt(saltLength).toString();
    SecretKey key = application.encryption.getKeyFromPassword("password", salt);
    IvParameterSpec ivParameterSpec = application.encryption.generateIv();




    @FXML
    private ListView<String> websiteList;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    String currentWebsite;

    public Controller() throws NoSuchAlgorithmException, InvalidKeySpecException {
    }

    public Vector<String> listOfWebsite() throws SQLException{
        Vector<String> list = new Vector();
        DataSource dataSource2 = createDataSource();

        Connection conn2 = dataSource2.getConnection();

        PreparedStatement stmt = conn2.prepareStatement("SELECT * FROM userINFO");

        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            list.add(rs.getString("website"));
        }

        return list;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        try {
            websiteList.getItems().addAll(listOfWebsite());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        websiteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    DataSource dataSource3 = createDataSource();
                    Connection conn3 = dataSource3.getConnection();
                    currentWebsite = websiteList.getSelectionModel().getSelectedItem();
                    String[] returnedQuery = query(conn3, currentWebsite);

                    usernameLabel.setText(returnedQuery[0]);

                    passwordLabel.setText(application.encryption.decrypt(algorithm,returnedQuery[1], key, ivParameterSpec));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public void add(ActionEvent e) throws Exception{

        final String[] websiteInput = new String[1];
        final String[] usernameInput = new String[1];
        final String[] passwordInput = new String[1];

        DataSource dataSource = createDataSource();
        Connection conn = dataSource.getConnection();

        //Add user input validation
        //Add a way to save the data that was given by the user
        TextInputDialog addWebsiteDialog = new TextInputDialog();
        TextInputDialog addPasswordDialog = new TextInputDialog();
        TextInputDialog addUsernameDialog = new TextInputDialog();

        addWebsiteDialog.setTitle("Website Name");
        addWebsiteDialog.setHeaderText("Please Enter The Websites Name:");
        addWebsiteDialog.setContentText("Website:");

        Optional<String> result = addWebsiteDialog.showAndWait();
        result.ifPresent(name ->{
            websiteInput[0] = name;
            websiteList.getItems().add(name);
            System.out.println("Entered website: " + name);
        });

        addUsernameDialog.setTitle("Username");
        addUsernameDialog.setHeaderText("Please Enter Your Username:");
        addUsernameDialog.setContentText("Username:");

        Optional<String> username = addUsernameDialog.showAndWait();
        username.ifPresent(name ->{
            usernameInput[0] = name;
            System.out.println("Entered username: " + name);
        });

        addPasswordDialog.setTitle("Set Password");
        addPasswordDialog.setHeaderText("Please Enter Your Password");
        addPasswordDialog.setContentText("Password:");


        Optional<String> password = addPasswordDialog.showAndWait();
        password.ifPresent(name ->{
            try {
                passwordInput[0] = encryption.encrypt(algorithm,name,key,ivParameterSpec);
            } catch (NoSuchPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidAlgorithmParameterException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            } catch (BadPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalBlockSizeException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Entered password: " + name);
        });

        insertUserData(conn, websiteInput[0], usernameInput[0], passwordInput[0]);

        System.out.println("add password");
    }

    public void remove(ActionEvent e){
        System.out.println("remove password");
    }
}
