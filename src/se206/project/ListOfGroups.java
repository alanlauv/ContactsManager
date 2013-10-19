package se206.project;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a list of all the groups currently in the contacts
 * manager application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class ListOfGroups {
	
	private List<GroupList> listOfGroups = new ArrayList<GroupList>();
	
	public void add(GroupList g) {
		listOfGroups.add(g);
	}
	
	public void remove(GroupList g) {
		listOfGroups.remove(g);
	}
	
	public GroupList getGroupList(int index) {
		return listOfGroups.get(index);
	}
	
}
