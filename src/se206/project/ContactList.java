package se206.project;

import java.util.ArrayList;
import java.util.Collections;
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
		contactsList.add(c);
	}

	public void remove(Contact c) {
		contactsList.remove(c);
	}

	public Contact getContact(int index) {
		return contactsList.get(index);
	}

	public void sortByFirstName() {
		Collections.sort(contactsList, Contact.Comparators.FIRSTNAME);
	}

	public void sortByLastName() {
		Collections.sort(contactsList, Contact.Comparators.LASTNAME);
	}

	public void sortByMobilePh() {
		Collections.sort(contactsList, Contact.Comparators.MOBILEPH);
	}

	// Fix this up
	@Override
	public Iterator<Contact> iterator() {        
		Iterator<Contact> contact = contactsList.iterator();
		return contact; 
	}
}
