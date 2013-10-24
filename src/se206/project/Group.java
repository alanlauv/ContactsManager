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
	private List<Contact> groupList = new ArrayList<Contact>();

	Group(String groupName) {
		name = groupName;
	}

	public void add(Contact contact) {
		if (!groupList.contains(contact)) {
			groupList.add(contact);
		}
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
		if (groupList.isEmpty()) {
			return null;
		} else {
			String idList = "" + groupList.get(0).getID();
			for (int i=0; i<groupList.size(); i++) {
				idList = idList + "," + groupList.get(i).getID();
			}
			return idList;
		}
	}

	public int getID() {
		return id;
	}
	
	public List<Contact> getGroupList() {
		return groupList;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public void setGroupList(List<Contact> contactList) {
		for (Contact contact : contactList) {
			if (contact.getGroup() != null && contact.getGroup().compareTo(name) == 0) {
				groupList.add(contact);
			}
		}
	}

	public void setGroupList(String idList, List<Contact> contactList) {
		if (idList != null) {
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

	@Override
	public int compareTo(Group g) {
		return this.name.compareToIgnoreCase(g.getName());
	}
}
