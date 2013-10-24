package se206.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of contacts in a particular group currently in
 * the contacts manager application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
@SuppressWarnings("serial")
public class Group implements Comparable<Group>, Serializable {

	private int id;
	private String name;

	// List of contacts belonging to this group
	private List<Contact> groupList = new ArrayList<Contact>();

	Group(String groupName) {
		name = groupName;
	}

	/**
	 * Adds this contact into this group
	 * 
	 * @param contact
	 */
	public void add(Contact contact) {
		if (!groupList.contains(contact)) {
			groupList.add(contact);
		}
	}

	/**
	 * Removes this contact from this group
	 * 
	 * @param contact
	 */
	public void remove(Contact contact) {
		groupList.remove(contact);
	}

	/**
	 * Returns the name of this group
	 * 
	 * @return String group name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the number of contacts in this group
	 * 
	 * @return int size
	 */
	public int getCount() {
		return groupList.size();
	}

	/**
	 * Returns the ID of this group
	 * 
	 * @return int ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns a list of contacts belonging to this group
	 * 
	 * @return List<Contact> group list
	 */
	public List<Contact> getGroupList() {
		return groupList;
	}

	/**
	 * Sets this group name to the new name. Each contact belonging to this group
	 * will also have its group field updated
	 * 
	 * @param newName
	 */
	public void setName(String newName) {
		name = newName;
		for (Contact contact : groupList) {
			contact.setGroup(newName);
		}
	}

	/**
	 * Sets the ID of this group
	 * 
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Sets the list of contacts belonging to this group
	 * 
	 * @param contactList
	 */
	public void setGroupList(List<Contact> contactList) {
		for (Contact contact : contactList) {
			if (contact.getGroup() != null && contact.getGroup().compareTo(name) == 0) {
				groupList.add(contact);
			}
		}
	}

	@Override
	public int compareTo(Group g) {
		return this.name.compareToIgnoreCase(g.getName());
	}
}
