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
public class Group {

	private int id;
	private String name;
	private List<Contact> groupList = new ArrayList<Contact>();

	Group(String groupName) {
		name = groupName;
	}

	public void add(Contact contact) {
		groupList.add(contact);
	}
	
	public void remove(Contact contact) {
		groupList.remove(contact);
	}

	public String getName() {
		return name;
	}
	
	public int getCount() {
		return groupList.size();
	}
	
	public String getIdList() {
		String idList = "" + groupList.get(0).getID();
		for (int i=0; i<groupList.size(); i++) {
			idList = idList + "," + groupList.get(i).getID();
		}
		return idList;
	}
	
	public int getID() {
		return id;
	}

	public void setName(String newName) {
		name = newName;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setGroupList(String idList, List<Contact> contactList) {
		String splitList[] = idList.split(",");
		for (int i=0; i<splitList.length; i++) {
			for (Contact c : contactList) {
				if (c.getID() == Integer.parseInt(splitList[i])) {
					groupList.add(c);
				}
			}	
		}
	}
}
