package domain;

public class User {
	private int id;
	private String tele;
	private String pwd;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public User(int id, String tele, String pwd) {
		super();
		this.id = id;
		this.tele = tele;
		this.pwd = pwd;
	}
	
}
