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

	/**
	 * Updates this contact's fields with the parameter contact's fields
	 * 
	 * @param editedContact
	 */
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

	/**
	 * Returns the full name of this contact
	 * 
	 * @return String full name
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Returns the first name of this contact
	 * 
	 * @return String first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the last name of this contact
	 * 
	 * @return String last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the mobile phone of this contact
	 * 
	 * @return String mobile phone
	 */
	public String getMobileph() {
		return mobileph;
	}

	/**
	 * Returns the home phone of this contact
	 * 
	 * @return String home phone
	 */
	public String getHomeph() {
		return homeph;
	}

	/**
	 * Returns the work phone of this contact
	 * 
	 * @return String work phone
	 */
	public String getWorkph() {
		return workph;
	}

	/**
	 * Returns the email of this contact
	 * 
	 * @return String email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the home address of this contact
	 * 
	 * @return String home address
	 */
	public String getHomeAdd() {
		return homeAdd;
	}

	/**
	 * Returns the DOB of this contact
	 * 
	 * @return String DOB
	 */
	public String getDoa() {
		return doa;
	}

	/**
	 * Returns the group of this contact
	 * 
	 * @return String group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Returns the ID of this contact
	 * 
	 * @return int ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the photo of this contact
	 * 
	 * @return byte[] photo
	 */
	public byte[] getPhoto(){
		return photo;
	}

	/**
	 * Sets the ID of this contact
	 * 
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Sets the group of this contact
	 * 
	 * @param groupName
	 */
	public void setGroup(String groupName) {
		group = groupName;
	}

	/**
	 * Sets the photo of this contact
	 * 
	 * @param imageData
	 */
	public void setPhoto(byte[] imageData) {
		photo = imageData;
	}

	@Override
	public int compareTo(Contact c) {
		return Comparators.FIRSTNAME.compare(this, c);
	}

	/**
	 * Represents the comparators used to sort contact by
	 * 
	 * @author Alan Lau, alau645, 2714269
	 *
	 */
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
