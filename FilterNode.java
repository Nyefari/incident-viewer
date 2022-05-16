/**
 * @author Donovan Pate "Nyefari"
 * 
 * This Class represents a single clause in an SQL select where query
 * Has 3 Fields which are strings that track the column, the type of filter, and the text to filter. 
 * It also points to the next FilterNode in the queue.
 * Methods are included for creating a FilterNode,
 * 	for setting it's reference to the next FilterNode,
 * 	checking equality with another FilterNode,
 * 	and getting its' value either in SQL format or as a readable string.
 */
public class FilterNode{
	private String text;
	private FilterNode and;
	private String type;
	private String column;
	
	/**
	 * Constructor taking in an argument for each field expected to be filled at creation
	 * 
	 * @param column the column that will be queried
	 * @param type the type of query this filterNode represents
	 * @param text the text that will be used as the value in the query
	 */
	public FilterNode(String column, String type, String text){
		this.column = column;
		this.text = text;
		this.type = type;
	}
	
	/**
	 * Method for creating a readable String representing this FilterNode
	 * 
	 * @return String representing this FilterNode
	 */
	public String toString(){
		return column + " " + type + " \"" + text + "\"";
	}
	
	/**
	 * Method for creating the sql that this filter represents
	 * 
	 * @return String representing this FilterNode as a clause in an SQL query
	 */
	public String asSQL(){
		String sql = "";
		String[] options = null;
		switch(type){
			case "is":
				return column + " = '" + text + "'";
			case "is not":
				return column + " != '" + text + "'";
			case "contains":
				return column + " LIKE '%" + text + "%'";
			case "does not contain":
				return column + " NOT LIKE '%" + text + "%'";
			case "is one of":
				options = text.split(",");
				sql = column + " IN ('" + options[0] + "'";
				for(int i = 1; i < options.length; i++){
					sql += ",'" + text + "'";
				}
				return sql + ")";
			case "is not one of":
				options = text.split(",");
				sql = column + " NOT IN ('" + options[0] + "'";
				for(int i = 1; i < options.length; i++){
					sql += ",'" + text + "'";
				}
				return sql + ")";
			default:
				System.out.println("Error with filter: " + this.toString());
				return "";
		}
	}
	
	/**
	 * Method for setting the next Node that this one points to
	 * 
	 * @param other the FilterNode to point to
	 */
	public void setAnd(FilterNode other){
		and = other;
	}
	
	/**
	 * Method for testing equality with another FilterNode
	 * 
	 * uses the toString hack since that includes all of the relevant info about the filter
	 * 
	 * @param other the FilterNode to point to
	 * @return true if they are functionally equivalent
	 */
	public boolean equals(Object obj){
		if(obj instanceof FilterNode){
			FilterNode other = (FilterNode)obj;
			return (this.toString().equals(other.toString()));
		}
		return false;
	}
	
	/**
	 * Method for getting the next Node that this one points to
	 * 
	 * @return the FilterNode this points to
	 */
	public FilterNode getAnd(){
		return and;
	}
}

