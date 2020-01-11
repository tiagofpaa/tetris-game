package tetrisPi;

public class User {
	
	private String username;
	private int points;
	
	public User(String username, int score) {
		this.username = username;
		this.points = score;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
