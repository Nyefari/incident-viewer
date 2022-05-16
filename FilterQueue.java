/**
 * @author Donovan Pate "Nyefari"
 * 
 * This class contains the code for a FilterQueue
 * 
 * Eventually I might allow OR statements in which case this would become
 * more of a FilterTree
 * 
 * It is called a Queue since things are only added to the back, and are functionally acted upon from the front
 */
public class FilterQueue {
	private FilterNode root;
	
	/**
	 * Constructor for creating a null FilterQueue
	 */
	public FilterQueue(){
		return;
	}
	
	/**
	 * Constructor for creating a non-empty FilterQueue with a single FilterNode
	 */
	public FilterQueue(FilterNode filter){
		root = filter;
	}
	
	/**
	 * Method for checking if this is an empty FilterQueue
	 *
	 * @return true if empty
	 */
	public boolean isEmpty(){
		return root == null;
	}
	
	/**
	 * Method for adding a new FilterNode to the queue
	 */
	public void add(FilterNode filter){
		if(this.isEmpty()){
			root = filter;
			return;
		}
		FilterNode current = root;
		while(current.getAnd() != null){
			current = current.getAnd();
		}
		current.setAnd(filter);
		return;
	}
	
	/**
	 * Method for removing a FilterNode from the Queue
	 * 
	 * @param the FilterNode to be removed
	 */
	public void remove(FilterNode filter){
		if(this.isEmpty()){
			throw new IllegalArgumentException("These are not the filters you are looking for");
		}
		
		if(root.equals(filter)){
			root = root.getAnd();
			return;
		}
		
		FilterNode current = root;
		FilterNode previous = null;
		while(!current.equals(filter) && (current.getAnd() != null)){
			previous = current;
			current = current.getAnd();
		}
		if(current.equals(filter)){
			previous.setAnd(current.getAnd());
		}
	}
	
	/**
	 * Method for getting the sql query statement that this FilterQueue represents
	 * 
	 * @return String that is the sql query
	 */
	public String sql(){
		if(this.isEmpty()){
			return "SELECT * FROM `incident_table`";
		}
		
		String sql = "SELECT * FROM `incident_table` WHERE " + root.asSQL();
		FilterNode current = root;
		while(current.getAnd() != null){
			current = current.getAnd();
			sql += " and (" + current.asSQL() + ")";
		}
		return sql + ";";
	}
}

