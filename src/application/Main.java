package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.image.Image;

/*TODO LIST:
* Authentication through JAAS or Spring Sec
* Complete the Add Password function
* Complete the Remove Password function
* Add Encryption/Decryption of Passwords
* Storage of Passwords through postgres database
* Add Password Generation
* */

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primarystage.setTitle("Password Manager");
        primarystage.setScene(new Scene(root));

        primarystage.show();
    }
}