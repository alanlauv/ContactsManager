package se206.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * This class represents a list of all the contacts currrently in the contacts manager
 * application.
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class ContactList implements Iterable<Contact> {
	
	protected List<Contact> contactsList = new ArrayList<Contact>();
	
	public void add(Contact c) {
		contactsList.add(new Contact("Alan", "Lau", "021 0210 2121", "", "", "", "", "", ""));
	}
	
	public void remove() {
		contactsList.remove(new Contact("Alan", "Lau", "021 0210 2121", "", "", "", "", "", ""));
	}
	
	public Contact getContact(int index) {
		return new Contact("Alan", "Lau", "021 0210 2121", "", "", "", "", "", "");
	}

	public void sortList() {
		
	}
	
	@Override
	public Iterator<Contact> iterator() {        
        Iterator<Contact> contact = contactsList.iterator();
        return contact; 
    }
}
