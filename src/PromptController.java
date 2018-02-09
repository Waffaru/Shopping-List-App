import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import java.util.stream.*;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.*;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * This is the controller class for our Dropbox Prompt.
 * 
 * 
 * @author Gonzalo Ortiz
 * @version "%I%, %G%"
 * @since 1.8
 */


public class PromptController {

    @FXML
    private TextField dropboxText;
    @FXML
    private Button dropBoxCancelBtn;


    /**
     * Saves the current list of Items to the specified file in Dropbox.
     * 
     * 
     * @see             Item
     * @see             MyLinkedList
     * @since           1.8
     */

    public void dropboxSave() {
        //GUI.fxApp.getHostServices().showDocument("http://www.google.fi");
        String shoppingListFile = "";
        for(int i = 0; i < Main.itemList.size(); i++) {
        shoppingListFile = shoppingListFile + Main.itemList.get(i).element.toString() + System.lineSeparator();
       }
       
       try (InputStream in = new ByteArrayInputStream(shoppingListFile.getBytes())) {
        FileMetadata metadata = GUI.controller.client.files().uploadBuilder("/" + dropboxText.getText() + ".txt").withMode(WriteMode.OVERWRITE)
            .uploadAndFinish(in);
       }catch(Exception e){e.printStackTrace();}
       finally {
        //GUI.controller.textFlow.getChildren().add(new Text("Succesfully saved to Dropbox!"));
        Stage stage = (Stage) dropBoxCancelBtn.getScene().getWindow();
        stage.close();
       }
    }


    /**
     * Cancels current prompt and shuts it down
     * 
     * @since 1.8
     */
    public void cancel() {
        Stage stage = (Stage) dropBoxCancelBtn.getScene().getWindow();
        stage.close();
    }




}