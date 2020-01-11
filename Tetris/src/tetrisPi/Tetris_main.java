package tetrisPi;

import java.io.IOException;

public class Tetris_main {

	public static void main(String[] args) {
		try {
			new Menu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
