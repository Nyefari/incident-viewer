# Historical Incident Viewer

This Repository contains the code written by Donovan Pate for the final project in CS 143 as taught by Dr. Adrian Veliz at Olympic College in Winter Quarter 2022.

The primary project output is a gui which allows the view of historical ticketing data for my place of work. The ticketing data is contained within a mysql database.
A database is used as there will be around 90000 incidents in the production version of this database at work.

## Contents

### Data Model

This repository contains a few data model objects, namely Incident, FilterNode and FilterQueue. 

An Incident represents the data for a single historical ticket. 
An incident can be created either from the output of a csv file from the LoadIncidents program OR from a String Array containing the String values of the incident in a set order.
Upon a rewrite, I would change there to only be the second constructor, and use that constructor from within the Load Incidents program as well. 
An Incident has methods for reading any of the StringProperties that are used in the GUI, but no setters are given. This is because the gui is intended for viewing data only, not for changing it.
An Incident also has an asSQL() method which produces a SQL insert statement. This is used by the LoadIncidents program when loading the incidents into the SQL database.

A FilterNode represents a single conditional clause within a sql query.
A FilterNode has 3 Fields which are strings that track the column, the type of filter, and the text to filter. It also points to the next FilterNode in the queue.
Methods are included for creating a FilterNode, for setting it's reference to the next FilterNode, checking equality with another FilterNode, and getting its' value either in SQL format or as a readable string.

FilterNodes are used within a FilterQueue to represent the entire sql query.
The FilterQueue is named a Queue as when it runs the first added is the first out in the sql query.
New FilterNodes are added to the end of the FilterQueue, but the removal of FilterNodes can be from anywhere in the FilterQueue, so it doesn't function as a perfect Queue.
The FilterQueue has references to both the top item of the Queue. 
The FilterQueue also has a sql() method which turns the entire queue into a String that is the sql query it represents.

### Controllers

Two main program files exist within this Repository. A LoadIncidents program and a IncidentViewer program.

LoadIncidents is a terminal program which asks the user for a csv path to load incidents data into the mysql database. 
This version needs the username and password for the database updated in the source code when compiled. Future versions will prompt the user. The intent is not to distribute this beyond myself, as there will be no need to load incidents again after they have been loaded the first time.

The IncidentViewer program acts as a controller relating the data model and the views. 
The primary view (IncidentListView) is loaded and a connection to the database is established.
Then, the user can add/remove/run filters as needed. These filters get added/removed to both an ObservableList<FilterNode> for the GUI and a FilterQueue for modelling the final sql query.
Upon running the filters, the lower table is populated with incidents based on the resultSet of the sql query the FilterQueue represents.
The user can then select a specific incident and open the second view, IncidentView.
This view shows the relevant data to the user for the incident. The only interaction here is to close the view.
The IncidentListView should always be closed by using the close option in the File menu. This ensures that the database connection will be closed successfully.

## Dependencies, Installation, and Use

This software assumes you have javafx installed. A mysql-connector.jar is included for the mysql connector/J. If you wish to compile one from source you can find the source code: https://github.com/mysql/mysql-connector-j .

This software also assumes a mysql database running on the computer you wish to run the software on. Devolopment of the software was done using the MySQL Community Edition found https://www.mysql.com/products/community/
I recommed using the installer on https://dev.mysql.com/downloads/ for windows. 

For the purposes of this guide, I will be using the MySQL installer for Windows - Community. You can also use this to install the connector/j, but all that is needed is the .jar file included in this repository.

In MySQL Installer - Community, select add on the right hand side, then navigate to and select MySQL Server 8.0.28 - X64 (Or whatever the newest version is). You do not need to select the checkbox for selecting which features are installed. If you wish to, however, the only thing you actually need is the MySQL Server. 
After selecting execute to install the MySQL Server, make sure to configure the product. There is no need to increase the memory usage beyond the "Development Computer" option. I would recommend keeping the default 3306 port, as that is what the source code expects. Use the strong password encryption, and make sure to note the password you set as the root password.

This program expects the password "ThisPasswordIsSecureISwear", but anything can be used. If you use something different, be sure to update the password variable in the connectToDB methods in both LoadIncidents and IncidentViewer.

The remaining default options will be fine.

After the database has been initialized, run the LoadIncidents program. Make sure when running the program you include the connector.jar in your classpath, as well as including the module--path for javafx (The Incident data model uses StringProperties). 
The command I used to run it during development after navigating to the source folder in my cmd prompt is: java --module-path="C:\Program Files\Java\javafx-sdk-17.0.1\lib" --add-modules="javafx.controls,javafx.media,javafx.fxml" -cp .;mysql-connector.jar LoadIncidents
NOTE: If using a linux terminal, then the classpath will have a : in place of the ;
If your mysql-connector .jar is named something different, or isn't in the same directory, then use that in place of the mysql-connector.jar in the command.

You should get the message that the connection is secured after a moment, and then messages showing the table has been created.

Then you will be prompted for the csv to load the incidents from. A csv with 5 dummy incidents representing the breadth of data contained in the real data set (blanks where there can be blanks, etc.) is included.

Reply with "Dummy Incidents.csv" to load the included incidents. The Quotes aren't needed, it will parse the entire line. After these incidents are loaded, you are done with the LoadIncidents program.

To run the IncidentViewer, simply run change the argument "LoadIncidents" to "IncidentViewer" in the cmd above.

Add filters as you wish and run the filters to navigate to an incident, then select and open the incident to open the incident view. 

## Issues encountered:

The JavaFX SceneBuilder doesn't seem to have the accessibilityText property in it.

## Citations

Citations are mostly included in the code itself, but shoutouts to the official mysql documentation https://dev.mysql.com/doc/refman/8.0/en/ and the official connector/j developer guide at https://dev.mysql.com/doc/connector-j/8.0/en/ . I did not always cite the specific pages for these in the source code, but my source code is also not directly copied from them. 



