import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class GUI extends Application {
    public static FXController controller;
    public static GUI fxApp;
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoppingList2.fxml"));
            //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource());
            Parent root = loader.load();
            controller = loader.getController();
            stage.setScene(new Scene(root));
        }
        catch(Exception e){e.printStackTrace();}
        
        stage.setTitle("Shopping List");
        stage.show();
        fxApp = this;
    }    


}