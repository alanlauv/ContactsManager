package se206.project;

/**
 * This class represents a single contact for the contacts manager application
 * 
 * @author Alan Lau, alau645, 2714269
 *
 */
public class Contact {
	private String firstName;
	private String lastName;
	private String mobileph;
	private String homeph;
	private String workph;
	private String email;
	private String homeAdd;
	private String doa;
	private String photo;
	private String group;

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

	public void editContact(
			String firstName,
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

	public String getFullName() {
		//return firstName + " " + lastName;
		return "Alan Lau";
	}

	public String getFirstName() {
		return "Alan";
	}

	public String getLastName() {
		return "Lau";
	}

	public String getMobileph() {
		return "021 0210 2121";
	}

	public String getHomeph() {
		return "09 321 3213";
	}

	public String getWorkph() {
		return "09 123 1231";
	}

	public String getEmail() {
		return "alau645@aucklanduni.ac.nz";
	}

	public String getDoa() {
		return "1234/01/31";
	}

	public String getGroup() {
		return "group name";
	}
}
