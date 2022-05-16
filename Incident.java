import java.util.*;
import javafx.beans.property.*;

/**
 * @author Donovan Pate "Nyefari"
 * 
 * This class of Objects represents a single incident as stored from a
 * Ticketing system formerly used at my place of work.
 * 
 */

public class Incident {
	/**
	 * Fields for keeping the Data in a FX accessible way
	 */
	private StringProperty number, state, shortDescription, description, comments, workNotes, category, subcategory, city,
			watchList, assignmentGroupName, closeCode, closeNotes, updater, openerEmail, callerEmail, assignedEmail,
			resolverEmail, closerEmail, configurationItem, createdAt, updatedAt, resolvedAt, closedAt, updateCount, sla, sysID;
	private boolean hasAttach;
	
	/**
	 * Constructor for creating an incident from a csv row. Takes the columns String too
	 * 
	 * @param incidentRow the String representing the incidents values
	 * @param columns the String with the columns in the order they are in this csv
	 */
	public Incident(String incidentRow, String columns){
		String[] values = incidentRow.split("\",\"");
		List<String> columnList = Arrays.asList(columns.split("\",\""));
//		if(columnList.size() != values.length) {
//			System.out.println(columnList.toString());
//			System.out.println(Arrays.toString(values));
//		}
		number = new SimpleStringProperty(this,"number",values[columnList.indexOf("number")]);
		state = new SimpleStringProperty(this,"state",values[columnList.indexOf("state")]);
		shortDescription = new SimpleStringProperty(this,"shortDescription",values[columnList.indexOf("short_description")]);
		description = new SimpleStringProperty(this,"description",values[columnList.indexOf("description")]);
		comments = new SimpleStringProperty(this,"comments",values[columnList.indexOf("comments")]);
		workNotes = new SimpleStringProperty(this,"workNotes",values[columnList.indexOf("work_notes")]);
		category = new SimpleStringProperty(this,"category",values[columnList.indexOf("u_ref_category.u_name")]);
		if(category.get().equals("")){
			category = new SimpleStringProperty(this,"category",values[columnList.indexOf("category")]);
		}
		subcategory = new SimpleStringProperty(this,"subcategory",values[columnList.indexOf("u_ref_subcategory.u_name")]);
		if(subcategory.get().equals("")){
			subcategory = new SimpleStringProperty(this,"subcategory",values[columnList.indexOf("subcategory")]);
		}
		int locationIndex = columnList.indexOf("location");
		if(locationIndex != -1){
			city = new SimpleStringProperty(this,"city",values[locationIndex]);
		} else {
			city = new SimpleStringProperty(this,"city",values[columnList.indexOf("u_city")]);
		}
		if(city.get().equals("")){
			city = new SimpleStringProperty(this,"city",values[columnList.indexOf("u_choice_2")]);
		}
		watchList = new SimpleStringProperty(this,"watchList",values[columnList.indexOf("watch_list")]);
		assignmentGroupName = new SimpleStringProperty(this,"assignmentGroupName",values[columnList.indexOf("assignment_group.name")]);
		closeCode = new SimpleStringProperty(this,"closeCode",values[columnList.indexOf("close_code")]);
		closeNotes = new SimpleStringProperty(this,"closeNotes",values[columnList.indexOf("close_notes")]);
		updater = new SimpleStringProperty(this,"updater",values[columnList.indexOf("sys_updated_by")]);
		openerEmail = new SimpleStringProperty(this,"openerEmail",values[columnList.indexOf("opened_by.email")]);
		callerEmail = new SimpleStringProperty(this,"callerEmail",values[columnList.indexOf("caller_id.email")]);
		assignedEmail = new SimpleStringProperty(this,"assignedEmail",values[columnList.indexOf("assigned_to.email")]);
		String resolver = "";
		if(values.length > 37) {
			resolver = values[columnList.indexOf("resolved_by.email")];
		}
		resolverEmail = new SimpleStringProperty(this,"resolverEmail",resolver);
		closerEmail = new SimpleStringProperty(this,"closerEmail",values[columnList.indexOf("closed_by.email")]);
		configurationItem = new SimpleStringProperty(this,"configurationItem",values[columnList.indexOf("cmdb_ci.name")]);
		createdAt = new SimpleStringProperty(this,"createdAt",values[columnList.indexOf("sys_created_on")]);
		updatedAt = new SimpleStringProperty(this,"updatedAt",values[columnList.indexOf("sys_updated_on")]);
		resolvedAt = new SimpleStringProperty(this,"resolvedAt",values[columnList.indexOf("resolved_at")]);
		closedAt = new SimpleStringProperty(this,"closedAt",values[columnList.indexOf("closed_at")]);
		updateCount = new SimpleStringProperty(this,"updateCount",values[columnList.indexOf("sys_mod_count")]);
		sla = new SimpleStringProperty(this,"sla",values[columnList.indexOf("made_sla")]);
	}
	
	/**
	 * Constructor that takes in a String[] in an assumed order
	 * 
	 * @param incidentValuesInOrder the String[] containing the data for this incident
	 */
	public Incident(String[] incidentValuesInOrder){
		int i = 0;
		number = new SimpleStringProperty(this,"number",incidentValuesInOrder[i++]);
		state = new SimpleStringProperty(this,"state",incidentValuesInOrder[i++]);
		shortDescription = new SimpleStringProperty(this,"shortDescription",incidentValuesInOrder[i++]);
		description = new SimpleStringProperty(this,"description",incidentValuesInOrder[i++]);
		comments = new SimpleStringProperty(this,"comments",incidentValuesInOrder[i++]);
		workNotes = new SimpleStringProperty(this,"workNotes",incidentValuesInOrder[i++]);
		category = new SimpleStringProperty(this,"category",incidentValuesInOrder[i++]);
		subcategory = new SimpleStringProperty(this,"subcategory",incidentValuesInOrder[i++]);
		city = new SimpleStringProperty(this,"city",incidentValuesInOrder[i++]);
		watchList = new SimpleStringProperty(this,"watchList",incidentValuesInOrder[i++]);
		assignmentGroupName = new SimpleStringProperty(this,"assignmentGroupName",incidentValuesInOrder[i++]);
		closeCode = new SimpleStringProperty(this,"closeCode",incidentValuesInOrder[i++]);
		closeNotes = new SimpleStringProperty(this,"closeNotes",incidentValuesInOrder[i++]);
		updater = new SimpleStringProperty(this,"updater",incidentValuesInOrder[i++]);
		openerEmail = new SimpleStringProperty(this,"openerEmail",incidentValuesInOrder[i++]);
		callerEmail = new SimpleStringProperty(this,"callerEmail",incidentValuesInOrder[i++]);
		assignedEmail = new SimpleStringProperty(this,"assignedEmail",incidentValuesInOrder[i++]);
		resolverEmail = new SimpleStringProperty(this,"resolverEmail",incidentValuesInOrder[i++]);
		closerEmail = new SimpleStringProperty(this,"closerEmail",incidentValuesInOrder[i++]);
		configurationItem = new SimpleStringProperty(this,"configurationItem",incidentValuesInOrder[i++]);
		createdAt = new SimpleStringProperty(this,"createdAt",incidentValuesInOrder[i++]);
		updatedAt = new SimpleStringProperty(this,"updatedAt",incidentValuesInOrder[i++]);
		resolvedAt = new SimpleStringProperty(this,"resolvedAt",incidentValuesInOrder[i++]);
		closedAt = new SimpleStringProperty(this,"closedAt",incidentValuesInOrder[i++]);
		updateCount = new SimpleStringProperty(this,"updateCount",incidentValuesInOrder[i++]);
		sla = new SimpleStringProperty(this,"sla",incidentValuesInOrder[i++]);
		sysID = new SimpleStringProperty(this,"sla",incidentValuesInOrder[i++]);
		hasAttach = incidentValuesInOrder[i++] == "1" ? true : false;
	}
		
	/**
	 * This method converts some of the data contained into a String. 
	 * Mostly for testing purposes
	 * 
	 * @return String representing this incident
	 */
	public String toString(){
		return "number: " + number.get() +
				"\ncreated at: " + createdAt.get() +
				"\nopened by: " + openerEmail.get() +
				"\nlocation: " + city.get() +
				"\nshort description: " + shortDescription.get() +
				"\nassigned to: " + assignedEmail.get() +
				"\nassignment group: " + assignmentGroupName.get();
	}
	
	/**
	 * This method generates the SQL Insert statement for inserting this incident into a database
	 * 
	 * @return String that is the Insert statement
	 */
	public String getSQL(){
		int slaInt = sla.get() == null || sla.get().equals("TRUE") ? 1 : 0;
		String sql = "UPDATE `incident_table` SET `created`=\"" + createdAt.get() + 
				"\",`state`=\"" + state.get() +
				"\",`short_description`=\"" + shortDescription.get() +
				"\",`caller_email`=\"" + callerEmail.get() +
				"\",`comments`=\"" + comments.get() +
				"\",`category`=\"" + category.get() +
				"\",`city`=\"" + city.get() +
				"\",`close_code`=\"" + closeCode.get() +
				"\",`close_notes`=\"" + closeNotes.get() +
				"\",`closed_at`=\"" + closedAt.get() +
				"\",`comments_and_work_notes`=\"" + comments.get() + workNotes.get() +
				"\",`description`=\"" + description.get() +
				"\",`made_sla`=\"" + slaInt +
				"\",`resolved_date`=\"" + resolvedAt.get() +
				"\",`subcategory`=\"" + subcategory.get() +
				"\",`last_updated`=\"" + updatedAt.get() +
				"\",`update_count`=\"" + updateCount.get() +
				"\",`updated_by`=\"" + updater.get() +
				"\",`work_notes`=\"" + workNotes.get() +
				"\",`watch_list`=\"" + watchList.get() +
				"\",`assigned_email`=\"" + assignedEmail.get() +
				"\",`assignment_group`=\"" + assignmentGroupName.get() +
				"\",`closer_email`=\"" + closerEmail.get() +
				"\",`configuration_item`=\"" + configurationItem.get() +
				"\",`opener_email`=\"" + openerEmail.get() +
				"\",`resolver_email`=\"" + resolverEmail.get() +
				"\",`keywords`=\"" + comments.get() + workNotes.get() + shortDescription.get() + description.get() +
				"\" WHERE `number`=\"" + number.get() + "\"";
		// System.out.println(sql);
		return sql;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getNumber(){return number.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty numberProperty(){
		if(number == null){number = new SimpleStringProperty(this, "number");}
		return number;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCreatedAt(){return createdAt.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty createdAtProperty(){
		if(createdAt == null){createdAt = new SimpleStringProperty(this, "createdAt");}
		return createdAt;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getOpenerEmail(){return openerEmail.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty openerEmailProperty(){
		if(openerEmail == null){openerEmail = new SimpleStringProperty(this, "openerEmail");}
		return openerEmail;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCallerEmail(){return callerEmail.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty affectedUserProperty(){
		if(callerEmail == null){callerEmail = new SimpleStringProperty(this, "callerEmail");}
		return callerEmail;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCity(){return city.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty cityProperty(){
		if(city == null){city = new SimpleStringProperty(this, "city");}
		return city;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getAssignmentGroupName(){return assignmentGroupName.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty assignmentGroupNameProperty(){
		if(assignmentGroupName == null){assignmentGroupName = new SimpleStringProperty(this, "assignmentGroupName");}
		return assignmentGroupName;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getShortDescription(){return shortDescription.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty shortDescriptionProperty(){
		if(shortDescription == null){shortDescription = new SimpleStringProperty(this, "shortDescription");}
		return shortDescription;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCategory(){return category.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty categoryProperty(){
		if(category == null){category = new SimpleStringProperty(this, "category");}
		return category;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getSubcategory(){return subcategory.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty subcategoryProperty(){
		if(subcategory == null){subcategory = new SimpleStringProperty(this, "subcategory");}
		return subcategory;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCloseCode(){return closeCode.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty closeCodeProperty(){
		if(closeCode == null){closeCode = new SimpleStringProperty(this, "closeCode");}
		return closeCode;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCloseNotes(){return closeNotes.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty closeNotesProperty(){
		if(closeNotes == null){closeNotes = new SimpleStringProperty(this, "closeNotes");}
		return closeNotes;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getDescription(){return description.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty descriptionProperty(){
		if(description == null){description = new SimpleStringProperty(this, "description");}
		return description;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getState(){return state.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty stateProperty(){
		if(state == null){state = new SimpleStringProperty(this, "state");}
		return state;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getCI(){return configurationItem.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty configurationItemProperty(){
		if(configurationItem == null){configurationItem = new SimpleStringProperty(this, "configurationItem");}
		return configurationItem;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getAssignee(){return assignedEmail.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty assignedEmailProperty(){
		if(assignedEmail == null){assignedEmail = new SimpleStringProperty(this, "assignedEmail");}
		return assignedEmail;
	}
	
	/**
	 * Public method for the property to play nice with the table
	 * 
	 * @return String with the properties' value
	 */
	public String getWatchList(){return watchList.get();}
	
	/**
	 * Public method for the property to play nice with the table
	 * using the example in the javafx doc for TableViews
	 * @return the StringProperty representing this property
	 */
	public StringProperty watchListProperty(){
		if(watchList == null){watchList = new SimpleStringProperty(this, "watchList");}
		return watchList;
	}
	
	/**
	 * Public method for the getting data for the IncidentView view
	 * 
	 * @return String with the values of the comment and workNotes properties concatenated
	 */
	public String getCAWN(){return comments.get() + workNotes.get();}
	
	public String getSysID() {return sysID.get();}
	
	public boolean hasAttachments() {return hasAttach;}
}

