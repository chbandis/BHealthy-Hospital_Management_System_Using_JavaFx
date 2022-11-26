package Classes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Class for creating a message window containing a text message and an OK button
public class MessageBox {

    public static void SimpleMBox (String message,String title){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(330);
        stage.setMinHeight(150);
        Label txt=new Label();
        txt.setText(message);
        Button btnOK=new Button();
        btnOK.setText("OK");
        btnOK.setOnAction(e->stage.close());
        btnOK.setStyle("-fx-background-color: #325096; -fx-cursor: hand; -fx-text-fill: white");

        VBox pane=new VBox(30);
        pane.getChildren().addAll(txt,btnOK);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10,0,10,0));
        pane.setStyle("-fx-background-color: #d7e5ee; -fx-font-family: 'Comfortaa'; -fx-font-size:15px");

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
}
