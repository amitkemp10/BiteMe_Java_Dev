package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.User;

/**
 * Class that Describes screen of choose supplier to register by BM manager
 * 
 * @author Tal Derai
 *
 */
public class SearchIdBeforeRegisterSupplierController implements Initializable {
	/**
	 * userResult: user result by search id userList: for update combo box branch:
	 * branch of user user_id: id of user
	 */
	private static User userResult;
	// * userList: ObservableList of users.*/
	ObservableList<String> userList;
	/**
	 * branch: get the relvant branch
	 */
	public static String branch;
	// * user_id: get the relvant userId.*/
	String user_id;

	@FXML
	private AnchorPane AnchorScreen;

	@FXML
	private ComboBox<String> cmbUser;

	@FXML
	private AnchorPane ScreenSearchUser;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnBack;

	@FXML
	private Label lblTitleSearch;

	@FXML
	private Label lblID;

	@FXML
	private Label lblErrorMsg;

	/**
	 * @return selected user object
	 */
	public User getUser() {
		return userResult;
	}

	@FXML
	void selectUser(ActionEvent event) {

	}

	@FXML
	void cmbUser(ActionEvent event) {

	}

	/**
	 * When clicked on Back button, return to BM manager page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		ClientUI.chat.accept("getNumberManagerMsgNotRead:" + "\t" + String.valueOf(ChatClient.u1.getIdNumber()));
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/BmManagerPage.fxml").openStream());
		BmManagerController bmManagerController = loader.getController();
		bmManagerController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Branch Manager");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * When clicked, on next button, upload screen registration supplier
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws Exception {
		if (cmbUser.getSelectionModel().getSelectedItem() == null) {
			lblErrorMsg.setText("please choose user");
			return;
		}
		userResult = searchPrivateCustomerbyId(Integer.parseInt(cmbUser.getValue().split(",")[0]));
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		RegisterNewSupplierController registerNewSupplierController = new RegisterNewSupplierController();
		registerNewSupplierController.start(primaryStage);
	}

	/**
	 * Method that search specific user by id
	 * 
	 * @param id: id of user
	 * @return selected user by id
	 */
	private User searchPrivateCustomerbyId(Integer id) {
		for (User user : ChatClient.dataUsers)
			if (user.getIdNumber().equals(id))
				return user;
		return null;
	}

	/**
	 * Method that found users that role is supplier with proper branch
	 * 
	 */
	private void setUserDetails() {
		branch = ChatClient.u1.getRole().split(" ")[2];
		ArrayList<String> details = new ArrayList<String>();
		for (User user : ChatClient.dataUsers) {
			if (user.getRole().contains("Supplier") && user.getRole().contains(branch) && user.getType().equals("-"))
				details.add(user.getIdNumber() + ", " + user.getFirstName() + " " + user.getLastName());
		}
		loadDetailsComboBox(details);
	}

	/**
	 * Update combo box with selected users
	 * 
	 * @param details: list of user details(id, first name, last name)
	 */
	public void loadDetailsComboBox(ArrayList<String> details) {
		userList = FXCollections.observableArrayList(details);
		cmbUser.setItems(userList);

	}

	/**
	 * Initialize screen of Search before register
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setUserDetails();
	}

	/**
	 * Upload screen of Search Id Before Register Supplier
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/SearchIdBeforeRegisterSupplierPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Search supplier");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
