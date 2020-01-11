package tetrisPi;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Time {

	private Timer timer;
	private int count = 0;	//contagem do tempo
	private int seg = 0;
	private int min = 0;
	private double x;
	
	public Time() {
		start();
	}

	public void start() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				count++;
				seg = count % 60;
				min = count / 60;
				min %= 60;
			}
	},1000,1000);
}

	public void exponencial() {
		Random rand = new Random();
		double r = rand.nextDouble();
		double mx = 1.0/30.0; //30 segundos por nivel
		x = (-mx)*(Math.log(r));
		System.out.println(x);
	}
	
//	public void gameOver() {
//		seg = 0;
//		min = 0;
//		start();
//	}
	
	public void pause() {
		this.timer.cancel();
	}
	
	public void resume() {
		this.start();
	}

	public int getSeg() {
		return seg;
	}

	public int getMin() {
		return min;
	}

	public double getX() {
		return x;
	}
	
}