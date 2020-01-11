package tetrisPi;

import java.io.IOException;

public class FrameRunnable implements Runnable{
	
	private Menu menu;
	private Tetris tetris;
	
	public FrameRunnable(Menu m) {
		this.menu = m;
	}
	
    public void run(){
       try {
		this.tetris = new Tetris(menu);
		this.tetris.setTitle("Tetris - " + menu.getUserPlaying().getUsername());
		tetris.startGame();
	} catch (IOException e) {
		e.printStackTrace();
	}
       
    }
    
    public Tetris getTetris(){
    	return this.tetris;
    }
}
