import java.sql.*;
import java.awt.Desktop;
import java.awt.Desktop.*;
import java.io.*;
import java.util.*;
import javafx.collections.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * @author Donovan Pate "Nyefari"
 * 
 * This is the main file for the GUI for viewin ticketing data from an old ticketing system.
 * 
 * I heavily referenced the demo gui and my gui from finalcs1 for the javafx portions, though most of it came back easily enough
 */


public class IncidentViewer extends Application{
	// Variables for the login screen
	private static Stage loginStage;
	private static Scene loginScene;
	private static TextField userText;
	private static TextField passwordText;
	
	// Variables relating to mainly the List View
	private static Stage listStage;
	private static Scene listScene;
	private static ListView filtersView;
	private static ObservableList<FilterNode> filtersList;
	private static FilterQueue filters;
	private static ChoiceBox filterColumn;
	private static ChoiceBox filterType;
	private static TextField filterText;
	private static TableView incidentTable;
	private static ObservableList<Incident> incidentList;
	private static Connection conn;
	
	// Variables relating to the Incident View
	private static Stage incStage;
	private static Scene incScene;
	private static TextField incNumber;
	private static TextField incCaller;
	private static TextField incLocation;
	private static TextField incCategory;
	private static TextField incSubcat;
	private static TextField incCloseCode;
	private static TextArea incCloseNotes;
	private static TextField incShort;
	private static TextArea incDesc;
	private static TextField incCreated;
	private static TextField incState;
	private static TextField incCI;
	private static TextField incAssignGroup;
	private static TextField incAssignee;
	private static TextField incOpener;
	private static TextArea incWatchList;
	private static TextArea incCAWN;
	private static String incSysID;
	private static CheckBox attCheck;
	
	
	/**
	 * Launch method
	 * 
	 * @param listStage parameter from javafx
	 * @throws Exception when it it can't load or when an SQL exception happens
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		// load listView gui from fxml
		FXMLLoader listLoader = new FXMLLoader(getClass().getResource("IncidentListView.fxml"));
		VBox listPane = listLoader.<VBox>load();
		Map<String, Object> listNamespace = listLoader.getNamespace();
		Scene primaryScene = new Scene(listPane);
		listScene = primaryScene; 
		listStage = primaryStage;
		
		// set up those choice boxes and text field
		filterColumn = (ChoiceBox)listNamespace.get("filterColumn");
		filterColumn.getItems().addAll("number","short_description","description","caller_email","comments","category","subcategory","city","close_code","close_notes","comments_and_work_notes","work_notes","watch_list","assignment_group","closer_email","opener_email","keywords");
		filterType = (ChoiceBox)listNamespace.get("filterType");
		filterType.getItems().addAll("is","is not","contains","does not contain","is one of","is not one of");
		filterText = (TextField)listNamespace.get("filterText");
		
		// Sets up the filtersView listView
		// using https://stackoverflow.com/questions/36769899/javafx-node-lookup-returning-null-only-for-some-elements-in-parent-loaded-with
		// like I did in cs1
		filtersView = (ListView)listNamespace.get("filtersView");
		filtersList = FXCollections.observableArrayList();
		filtersView.setItems(filtersList);
		
		// Sets up the incidentTable tableView
		incidentTable = (TableView)listNamespace.get("incidentTable");
		incidentList = FXCollections.observableArrayList();
		incidentTable.setItems(incidentList);
		TableColumn<Incident, String> createdCol = new TableColumn<Incident,String>("Created");
		createdCol.setCellValueFactory(new PropertyValueFactory("createdAt"));
		createdCol.setPrefWidth(100.0);
		TableColumn<Incident,String> numberCol = new TableColumn<Incident,String>("Number");
		numberCol.setCellValueFactory(new PropertyValueFactory("number"));
		numberCol.setPrefWidth(80.0);
		TableColumn<Incident,String> openerCol = new TableColumn<Incident,String>("Opened By");
		openerCol.setCellValueFactory(new PropertyValueFactory("openerEmail"));
		openerCol.setPrefWidth(120.0);
		TableColumn<Incident,String> callerCol = new TableColumn<Incident,String>("Impacted User");
		callerCol.setCellValueFactory(new PropertyValueFactory("callerEmail"));
		callerCol.setPrefWidth(120.0);
		TableColumn<Incident,String> cityCol = new TableColumn<Incident,String>("Location");
		cityCol.setCellValueFactory(new PropertyValueFactory("city"));
		cityCol.setPrefWidth(100.0);
		TableColumn<Incident,String> assignmentGroupCol = new TableColumn<Incident,String>("Assignment Group");
		assignmentGroupCol.setCellValueFactory(new PropertyValueFactory("assignmentGroupName"));
		assignmentGroupCol.setPrefWidth(165.0);
		TableColumn<Incident,String> shortDescriptionCol = new TableColumn<Incident,String>("Short Description");
		shortDescriptionCol.setCellValueFactory(new PropertyValueFactory("shortDescription"));
		shortDescriptionCol.setPrefWidth(500.0);
		incidentTable.getColumns().setAll(createdCol, numberCol, openerCol, callerCol, cityCol, assignmentGroupCol, shortDescriptionCol);
		
		// Set up the Incident View
		FXMLLoader incLoader = new FXMLLoader(getClass().getResource("IncidentView.fxml"));
		VBox incPane = incLoader.<VBox>load();
		Map<String, Object> incNamespace = incLoader.getNamespace();
		Scene secondaryScene = new Scene(incPane);
		incScene = secondaryScene;
		
		// Link Incident View Fields
		incNumber = (TextField)incNamespace.get("number");
		incCaller = (TextField)incNamespace.get("caller");
		incLocation = (TextField)incNamespace.get("location");
		incCategory = (TextField)incNamespace.get("category");
		incSubcat = (TextField)incNamespace.get("subcategory");
		incCloseCode = (TextField)incNamespace.get("closeCode");
		incCloseNotes = (TextArea)incNamespace.get("closeNotes");
		incShort = (TextField)incNamespace.get("shortDescription");
		incDesc = (TextArea)incNamespace.get("description");
		incCreated = (TextField)incNamespace.get("created");
		incState = (TextField)incNamespace.get("state");
		incCI = (TextField)incNamespace.get("configurationItem");
		incAssignGroup = (TextField)incNamespace.get("assignmentGroup");
		incAssignee = (TextField)incNamespace.get("assignee");
		incOpener = (TextField)incNamespace.get("opener");
		incWatchList = (TextArea)incNamespace.get("watchList");
		incCAWN = (TextArea)incNamespace.get("commentsAndWorkNotes");
		attCheck = (CheckBox)incNamespace.get("attCheck");
		
		// actually show the gui
		primaryStage.setTitle("Company Historical Ticket Viewer");
		primaryStage.setScene(listScene);
		primaryStage.show();
		
		// set up the incident GUI
		incStage = new Stage();
		incStage.setScene(incScene);
		
		// Set up the login Screen
		FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		VBox loginPane = loginLoader.<VBox>load();
		Map<String, Object> loginNamespace = loginLoader.getNamespace();
		Scene tertiaryScene = new Scene(loginPane);
		loginScene = tertiaryScene;
		
		// Link Login Fields
		userText = (TextField)loginNamespace.get("username");
		passwordText = (TextField)loginNamespace.get("password");
		
		// show the Login Screen
		loginStage = new Stage();
		loginStage.setTitle("Log In to Database:");
		loginStage.setScene(loginScene);
		loginStage.show();
	}
	
	/**
	 * This method establishes the connection to the database if it is
	 * configured per the installation instructions 
	 * 
	 * @return the connection variable for the successful connection
	 * @see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-connect-drivermanager.html
	 */
	
	@FXML
	private void connectToDB(Event e){
		conn = null;
		
		try{
			String user = userText.getText();
			userText.setText("");
			String password = passwordText.getText();
			passwordText.setText("");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?",user,password);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex){
			System.out.println("Exception: " + ex.toString());
		}
		
		if(conn == null){
			System.out.println("No Connection was able to be created. Can you verify your root password matches the installation instructions?");
			return;
		} else {
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("USE `incidents`;");
				stmt.close();
			} catch (Exception ex) {
				return;
			}
			System.out.println("Connection Secured!");
			loginStage.close();
			return;
		}
	}
	
	/**
	 * Method which occurs when someone uses the refresh button
	 * Runs the filters currently in the FilterQueue and gets the resultSet,
	 * which is then put into the incident TableView
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void refresh(Event e){
		if(filters == null){
			System.out.println("no filters");
			return;
		}
		try{
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(filters.sql());
			// System.out.println(result.getMetaData().getColumnCount());
			fillIncidentList(result);
		} catch (Exception except){
			except.printStackTrace();
		}
		return;
	}
	
	private void fillIncidentList(ResultSet results){
		incidentList.clear();
		try{
			while(results.next()){
				int i = 0;
				String[] values = new String[28];
				values[i++] = results.getString(results.findColumn("number"));
				values[i++] = results.getString(results.findColumn("state"));
				values[i++] = results.getString(results.findColumn("short_description"));
				values[i++] = results.getString(results.findColumn("description"));
				values[i++] = results.getString(results.findColumn("comments"));
				values[i++] = results.getString(results.findColumn("work_notes"));
				values[i++] = results.getString(results.findColumn("category"));
				values[i++] = results.getString(results.findColumn("subcategory"));
				values[i++] = results.getString(results.findColumn("city"));
				values[i++] = results.getString(results.findColumn("watch_list"));
				values[i++] = results.getString(results.findColumn("assignment_group"));
				values[i++] = results.getString(results.findColumn("close_code"));
				values[i++] = results.getString(results.findColumn("close_notes"));
				values[i++] = results.getString(results.findColumn("updated_by"));
				values[i++] = results.getString(results.findColumn("opener_email"));
				values[i++] = results.getString(results.findColumn("caller_email"));
				values[i++] = results.getString(results.findColumn("assigned_email"));
				values[i++] = results.getString(results.findColumn("resolver_email"));
				values[i++] = results.getString(results.findColumn("closer_email"));
				values[i++] = results.getString(results.findColumn("configuration_item"));
				values[i++] = results.getString(results.findColumn("created"));
				values[i++] = results.getString(results.findColumn("last_updated"));
				values[i++] = results.getString(results.findColumn("resolved_date"));
				values[i++] = results.getString(results.findColumn("closed_at"));
				values[i++] = results.getString(results.findColumn("update_count"));
				values[i++] = results.getString(results.findColumn("made_sla"));
				values[i++] = results.getString(results.findColumn("sys_id"));
				values[i++] = results.getString(results.findColumn("has_attachments"));
				//for(int j = 0; j < values.length; j++){
					//System.out.println(values[j]);
				//}
				Incident current = new Incident(values);
				incidentList.add(current);
				// System.out.println("Added: " + current.toString());
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which runs when someone uses the close option in the menu bar
	 * 
	 * Gracefully ends the database connection, then exits the program
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void exit(Event e){
		try{
			conn.close();
			System.out.println("Connection closed Successfully!");
		} catch (Exception exception){
			exception.printStackTrace();
		}
		System.exit(0);
	}
	
	/**
	 * Method which runs when someone uses the about option in the menu bar
	 * 
	 * Does nothing
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void about(Event e){
		return;
	}
	
	/**
	 * Method which runs when someone uses the addFilter option on the IncidentListView gui
	 * 
	 * creates a filter from the selected checkbox options and typed in text then
	 * adds it to the FilterQueue and the filters ObservableList
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void addFilter(Event e){
		if(filters == null){
			filters = new FilterQueue(new FilterNode((String)filterColumn.getValue(), (String)filterType.getValue(), (String)filterText.getText()));
		} else {
			filters.add(new FilterNode((String)filterColumn.getValue(), (String)filterType.getValue(), (String)filterText.getText()));
		}
		filtersList.add(new FilterNode((String)filterColumn.getValue(), (String)filterType.getValue(), (String)filterText.getText()));
		return;
	}
	
	/**
	 * Method which runs when someone uses the run Filters option on the IncidentListView gui
	 * 
	 * uses the refresh method to run the filters against the db and update the IncidentTable
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void runFilters(Event e){
		refresh(e);
		return;
	}
	
	/**
	 * Method which runs when someone uses the removeFilters option on the IncidentListView gui
	 * 
	 * gets the currently selected filter and removes it from both the filters
	 * list and from the FilterQueue
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void removeFilter(Event e){
		if(filters == null || filters.isEmpty()){
			return;
		}
		
		FilterNode current = (FilterNode)filtersView.getSelectionModel().getSelectedItem();
		if(current == null){
			return;
		}
		filtersList.remove(current);
		filters.remove(current);
		return;
	}
	
	/**
	 * Method which runs when someone uses the open selected incident option on the IncidentListView gui
	 * 
	 * gets the currently selected incident and uses its data to fill out the incidentView gui and shows it
	 * 
	 * @param e the triggering Event from the gui
	 */
	@FXML
	public void openSelected(Event e){
		Incident current = (Incident)incidentTable.getSelectionModel().getSelectedItem();
		incNumber.setText(current.getNumber());
		incCaller.setText(current.getCallerEmail());
		incLocation.setText(current.getCity());
		incCategory.setText(current.getCategory());
		incSubcat.setText(current.getSubcategory());
		incCloseCode.setText(current.getCloseCode());
		incCloseNotes.setText(current.getCloseNotes());
		incShort.setText(current.getShortDescription());
		incDesc.setText(current.getDescription());
		incCreated.setText(current.getCreatedAt());
		incState.setText(current.getState());
		incCI.setText(current.getCI());
		incAssignGroup.setText(current.getAssignmentGroupName());
		incAssignee.setText(current.getAssignee());
		incOpener.setText(current.getOpenerEmail());
		incWatchList.setText(current.getWatchList());
		incCAWN.setText(current.getCAWN());
		incSysID = current.getSysID();
		attCheck.setSelected(current.hasAttachments());
		
		incStage.setTitle("Number: " + current.getNumber());
		incStage.show();
		return;
	}
	
	/**
	 * Method for opening the file explorer looking at the attachments folder for the selected incident
	 * 
	 * @param e The triggering event
	 */
	@FXML
	public void openAttachments(Event e) {
		String url = "\\\\bpvp.local\\seattle\\Tacoma\\Users\\donovan.pate\\Documents\\My Recording Files" + incSysID;
		getHostServices().showDocument(url);
	}
}

