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

	private String name;

	Group(String groupName) {
		name = groupName;
	}

	public void rename(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

}
