package se206.project;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class represents a single contact for the contacts manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
@SuppressWarnings("serial")
public class Contact implements Comparable<Contact>, Serializable {
	private String firstName;
	private String lastName;
	private String mobileph;
	private String homeph;
	private String workph;
	private String email;
	private String homeAdd;
	private String doa;
	private byte[] photo = null;
	private String group = null;
	private int id;

	/*
	Contact(String firstName,
			String lastName,
			String mobileph,
			String homeph,
			String workph,
			String email,
			String homeAdd,
			String doa) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileph = mobileph;
		this.homeph = homeph;
		this.workph = workph;
		this.email = email;
		this.homeAdd = homeAdd;
		this.doa = doa;
	}*/

	Contact(String firstName,
			String lastName,
			String mobileph,
			String homeph,
			String workph,
			String email,
			String homeAdd,
			String doa,
			String group) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileph = mobileph;
		this.homeph = homeph;
		this.workph = workph;
		this.email = email;
		this.homeAdd = homeAdd;
		this.doa = doa;
		this.group = group;
	}

	public void editContact(Contact editedContact) {
		this.firstName = editedContact.firstName;
		this.lastName = editedContact.lastName;
		this.mobileph = editedContact.mobileph;
		this.homeph = editedContact.homeph;
		this.workph = editedContact.workph;
		this.email = editedContact.email;
		this.homeAdd = editedContact.homeAdd;
		this.doa = editedContact.doa;
		this.group = editedContact.group;
		this.photo = editedContact.photo;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMobileph() {
		return mobileph;
	}

	public String getHomeph() {
		return homeph;
	}

	public String getWorkph() {
		return workph;
	}

	public String getEmail() {
		return email;
	}

	public String getHomeAdd() {
		return homeAdd;
	}

	public String getDoa() {
		return doa;
	}

	public String getGroup() {
		return group;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setPhoto(byte[] imageData) {
        photo = imageData;
    }

    public byte[] getPhoto(){
        return photo;
    }

	@Override
	public int compareTo(Contact c) {
		return Comparators.FIRSTNAME.compare(this, c);
	}

	public static class Comparators {

		public static Comparator<Contact> FIRSTNAME = new Comparator<Contact>() {
			@Override
			public int compare(Contact c1, Contact c2) {
				return c1.firstName.compareToIgnoreCase(c2.firstName);
			}
		};

		public static Comparator<Contact> LASTNAME = new Comparator<Contact>() {
			@Override
			public int compare(Contact c1, Contact c2) {
				return c1.lastName.compareToIgnoreCase(c2.lastName);
			}
		};

		public static Comparator<Contact> MOBILEPH = new Comparator<Contact>() {
			@Override
			public int compare(Contact c1, Contact c2) {
				return c1.mobileph.compareTo(c2.mobileph);
			}
		};
	}
}
