package se206.project;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of contacts in a particular group currently in
 * the contacts manager application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class GroupList extends ContactList {
	
	private String groupName;
	
	GroupList(String groupName) {
		this.groupName = groupName;
	}
	
	public void rename(String newName) {
		groupName = newName;
	}
	
	public String getGroupName() {
		return groupName;
	}

}
