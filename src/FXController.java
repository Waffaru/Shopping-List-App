import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is our main controller for our GUI.
 * 
 * This class is used to control what our buttons do
 * using FXML injection.
 * 
 * @author Gonzalo Ortiz
 * @version "%I%, %G%"
 * @since 1.8
 */

public class FXController{
    public static String accessToken = "";
    public static String fileName;
    DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial");
    DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
    DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    public DbxClientV2 client;
    
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    public TextField textField;
    @FXML
    public Button resetButton;
    @FXML
    public TableView<Item> tableView;
    @FXML
    public TableColumn<Item, Integer> amountColumn;
    @FXML
    public TableColumn<Item, String> itemColumn;

    /**
     * Saves the current list to the directory of your choosing.
     * 
     * This opens up a file explorer which lets you choose
     * which directory to save your file in and also the
     * filename for your file
     * 
     * @see MyLinkedList
     * @see Item
     * @since 1.8
     */
    public void saveFile() {
        String shoppingListFile = "";
            for(int i = 0; i < Main.itemList.size(); i++) {
            shoppingListFile = shoppingListFile + Main.itemList.get(i).element.toString() + System.lineSeparator();
           }
        FileChooser fc = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fc.showSaveDialog(null);
                      
        if(file != null){
            fileName = file.getAbsolutePath();
            try {
                FileWriter fileWriter = null;
                 
                fileWriter = new FileWriter(file);
                fileWriter.write(shoppingListFile);
                fileWriter.close();
                System.out.println("Saved Succesfully!");
            } catch (IOException ex) {
            }

            // right now this takes whatever test.txt containts and writes it to the textfile below in the dropbox-folder.
            // We need to change the variable input in uploadAndFinish so that it takes our list.
            /*try (InputStream in = new FileInputStream("test.txt")) {
                FileMetadata metadata = client.files().uploadBuilder("/filetest.txt").withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
            }catch(Exception e){e.printStackTrace();}*/
        }
    }

    /**
     * Opens the current file from the directory of your choosing.
     * 
     * This opens up a file explorer which lets you choose
     * which directory to open a file from.
     * 
     * @see MyLinkedList
     * @see Item
     * @since 1.8
     */    
    public void openFile() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null) {
            fileName = selectedFile.getAbsolutePath();
            MyLinkedList<Item> tmpList = new MyLinkedList();
            try (Stream<String> stream = Files.lines(Paths.get(selectedFile.getAbsolutePath()))) {
                
                            stream.forEach((l) -> {
                                // Add new object from list
                                String[] individualItem = l.split(" ");
                                Item tmp = new Item(Integer.parseInt(individualItem[0]), individualItem[1]);
                                tmpList.add(tmp);
                            });
                            Main.tmpList = tmpList;
                            Main.itemList = tmpList; 
                            Main.printListItems(Main.itemList);
                            System.out.println("Give shopping list (example: 1 milk;2 tomato;3 carrot;)");
                            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
                            itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                            tableView.setItems(getItemList());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
        }
        else {
            // File is null.
        }
    }

    /**
     * Takes the string-input on our textbar and adds it to the list.
     * 
     * @since 1.8
     */
    public void inputItem() {
        String input = textField.getText();
        if(input.equals("exit")) {
            System.exit(0);
        }
        try {
            String[] inputArray = input.split(";");
            for(int i = 0; i < inputArray.length; i++) {
                String[] individualItem = inputArray[i].split(" ");
                Item tmp = new Item(Integer.parseInt(individualItem[0]), individualItem[1]);
                //Adds the item if it doesn't exist
                if(!Main.compareListToItem(Main.tmpList, tmp)) {
                    Main.tmpList.add(tmp);
                }
            }
            Main.itemList = Main.tmpList;
            Main.printListItems(Main.itemList);
            System.out.println("Give shopping list (example: 1 milk;2 tomato;3 carrot;)");
            this.print();

        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Resets the active list to a new one.
     * 
     * @since 1.8
     */
    public void reset() {
        Main.itemList.clear();
        Main.tmpList.clear();
        fileName = null;
        tableView.setItems(FXCollections.observableArrayList());
    }

    /**
     * Saves the current file to the last file saved.
     * 
     * If saveFile-method has been used previously during use of
     * our program, saves the current list to that same file.
     * 
     * @since 1.8
     */    
    public void save() {
      if(fileName == null) {
          saveFile();
      }
      else {
          String shoppingListFile = "";
            for(int i = 0; i < Main.itemList.size(); i++) {
            shoppingListFile = shoppingListFile + Main.itemList.get(i).element.toString() + System.lineSeparator();
           }
        FileChooser fc = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = new File(fileName);
                      
        if(file != null){
            try {
                FileWriter fileWriter = null;
                 
                fileWriter = new FileWriter(fileName);
                fileWriter.write(shoppingListFile);
                fileWriter.close();
                System.out.println("Saved Succesfully!");
            }catch (IOException ex) {ex.printStackTrace();}
        }else {
          // file is null
        }
       }
    }

    /**
     * Shuts down our program.
     * 
     * @since 1.8
     */
    public void shutDown() {
        //TODO: Add persistent saving.
        System.exit(0);
    }

    /*public void dropboxSave() {
        String homeDir = System.getProperty("user.home");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(homeDir + "/dropbox"));

                String shoppingListFile = "";
            for(int i = 0; i < Main.itemList.size(); i++) {
            shoppingListFile = shoppingListFile + Main.itemList.get(i).element.toString() + System.lineSeparator();
           }
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fc.showSaveDialog(null);
                      
        if(file != null){
            System.out.println(file.getName());
            fileName = file.getAbsolutePath();
            // right now this takes whatever test.txt containts and writes it to the textfile below in the dropbox-folder.
            // We need to change the variable input in uploadAndFinish so that it takes our list.
            try (InputStream in = new ByteArrayInputStream(shoppingListFile.getBytes())) {
                FileMetadata metadata = client.files().uploadBuilder("/" + file.getName()).withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
            }catch(Exception e){e.printStackTrace();}
        }
    }*/

    // Under work. Works right now but I want to polish it
    // Before marking it as ready
    public void dropboxSave() {

        //Opens up a web browser to authenticate..
        String authorizeUrl = webAuth.start();
        GUI.fxApp.getHostServices().showDocument(authorizeUrl);

        //Takes the authentication code and checks it
        try {
            String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            DbxAuthFinish authFinish = webAuth.finish(code);
            accessToken = authFinish.getAccessToken();
            client = new DbxClientV2(config, accessToken);
        }catch(Exception e){}

        //opens the new window.
        try{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dropboxSave.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            /*stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);*/
            stage.setTitle("Dropbox Save");
            stage.setScene(new Scene(root1));  
            stage.show();
          } catch(Exception e) {e.printStackTrace();}
          
    }

    // Need to change this to an approach where it loads from the web the user's files instead of
    // checking the local dropbox-folder.
    public void dropboxOpen() {
        String homeDir = System.getProperty("user.home");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(homeDir + "/dropbox"));
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null) {
            MyLinkedList<Item> tmpList = new MyLinkedList();
            try (Stream<String> stream = Files.lines(Paths.get(selectedFile.getAbsolutePath()))) {
                
                            stream.forEach((l) -> {
                                // Add new object from list
                                String[] individualItem = l.split(" ");
                                Item tmp = new Item(Integer.parseInt(individualItem[0]), individualItem[1]);
                                tmpList.add(tmp);
                            });
                            Main.tmpList = tmpList;
                            Main.itemList = tmpList; 
                            Main.printListItems(Main.itemList);
                            System.out.println("Give shopping list (example: 1 milk;2 tomato;3 carrot;)");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
        }
        else {
            // File is null.
        }
    }

    public static ObservableList<Item> getItemList() {
        ObservableList<Item> observableList = FXCollections.observableArrayList();
        MyLinkedList.Alkio tmp = Main.itemList.getFirst();
        Item itemTmp = (Item)tmp.element;
        for(int i = 0; i < Main.itemList.size(); i++) {
                observableList.add(itemTmp);
                tmp = tmp.next;
               //System.out.println(itemTmp.amount);
                if(tmp != null) {
                    itemTmp = (Item)tmp.element;
                }
        }
        return observableList;
    }

    public void print() {

        //Used to fire changEvent
        tableView.getItems().removeAll(tableView.getItems());

        tableView.setItems(FXCollections.observableArrayList());
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.setItems(getItemList());
        amountColumn.setStyle("-fx-alignment: CENTER;");
        itemColumn.setStyle("-fx-alignment: CENTER;");
        textField.clear();
    }
}