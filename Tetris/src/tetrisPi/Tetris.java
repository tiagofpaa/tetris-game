package tetrisPi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Tetris extends JFrame {
	
	private static final long serialVersionUID = -4722429764792514382L;
	private ImageIcon main_board_img = new ImageIcon(Tetris.class.getResource("/main_board.png"));
	private ImageIcon game_over_img = new ImageIcon(Tetris.class.getResource("/game_over.png"));
	private static final long FRAME_TIME = 1000L / 50L;
	private static final int TYPE_COUNT = TileType.values().length;
	private ActionListener button_listener;
	
	//Game parts
	private JPanel main_panel = new JPanel();
	private BoardPanel board;
	private SidePanel side;
	private Sound sound;
	private Time time;
	private Menu menu;
	private  Random random;
	private Clock logicTimer;
	
	
	//Game DATA
	private User currently_playing;
	private boolean isPaused;
	private boolean isNewGame;
	private boolean isGameOver;
	private TileType currentType;
	private TileType nextType;
	private int currentCol;
	private int currentRow;
	private int currentRotation;
	private int dropCooldown;
	private float gameSpeed;
	private int level;
	private boolean speedUp;
	private int score;
	private int bonustime;
	private boolean bonus_trigger;
	
	//Game Over
	private JLabel end_score;
	private JButton back_to_main;
	private JButton restart;
	private JLabel game_over_label;
	
	
	public Tetris(Menu menu) throws IOException {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		this.menu = menu;
		
		init();
		newBoard();
		keyListener();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void actionListener(){
		button_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == restart){
					FileHandler.update_score(currently_playing.getUsername(), score);
					menu.quick_game();
					dispose();
				
				}else if(e.getSource() == back_to_main){
					menu.removeAll();
					dispose();
					menu.menuNew();
					menu.repaint();
				}
			}
		};
	}
	
	
	private void keyListener(){
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				case 82:
					gameOver();
					break;
				
				case 27:
					dispose();
					FileHandler.update_score(currently_playing.getUsername(), score);
					menu.menuNew();
					break;
				
				case 39:
					sound.nextSound();
					break;
								
				case 38:
					sound.stop();
					break;
					
				case 40:
					sound.play(true);
					break;
				
				case KeyEvent.VK_S:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
						speedUp = true;
					}
					break;
					
				case KeyEvent.VK_A:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
						currentCol--;
					}
					break;
					
				case KeyEvent.VK_D:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
						currentCol++;
					}
					break;
					
				case KeyEvent.VK_Q:
					if(!isPaused) {
						rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
					}
					break;
				
				case KeyEvent.VK_E:
					if(!isPaused) {
						rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
					}
					break;
					
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
						time.pause();
						
					}
					if(!isPaused) {
						time.resume();
					}
//					if(sound.isPlaying()){
//						sound.stop();
//					}else{
//						sound.play();
//					}
					break;

				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
					}
					break;
				
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
		
				case KeyEvent.VK_S:
					speedUp = false;
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					
					break;
				}
				
			}
			
		});
	}
	
	
	private void init() throws IOException{
		currently_playing = menu.getUserPlaying();
		
		setSize(new Dimension(680,672));
		actionListener();
		main_panel.setLayout(null);
		
		
		back_to_main = new JButton();
		back_to_main.setOpaque(false);
		back_to_main.setContentAreaFilled(false);
		back_to_main.setBorderPainted(false);
		back_to_main.setBounds(225, 435, 245, 38);
		
		restart = new JButton();
		restart.setOpaque(false);
		restart.setContentAreaFilled(false);
		restart.setBorderPainted(false);
		restart.setBounds(225, 380, 245, 38);
		
		back_to_main.addActionListener(button_listener);
		restart.addActionListener(button_listener);
		
		JLabel main_board = new JLabel(main_board_img);
		main_board.setBounds(0, 0, 680, 643);
		main_panel.setBounds(0, 0, 680, 672);
		main_panel.add(main_board);
		
		game_over_label = new JLabel(game_over_img);
		game_over_label.setBounds(0, 0, 680, 643);
		
		end_score = new JLabel("0");
		
		end_score.setForeground(Color.GREEN);
		end_score.setFont(new Font("Serif", Font.BOLD, 54));
		end_score.setBounds(350, 180, 200, 250);
		
		//sound = new Sound();
	}

	
	public void startGame() throws IOException {
		time = new Time();
		time.pause();
		
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;
		this.nextType = generateRandomPiece();
		
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		resetGame();
		
		while(true) {
			long start = System.nanoTime();
			
			logicTimer.update();
			
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
		
			if(dropCooldown > 0) {
				dropCooldown--;
			}
			
			renderGame();
			
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	
	
	int last = 0;
	public void updateGame() {
//		if(!sound.isPlaying() && this.isPaused){
//			sound.verify();
//		}

		if(bonustime > 0){
			if(time.getSeg() != last){
				bonus_trigger = true;
				bonustime--;
				last = time.getSeg();
			}
		}else{
			bonus_trigger = false;
		}
		
		
		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			currentRow++;
		} else {
			dropCooldown = 25;
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			checkClearedLines();
			if(!bonus_trigger){increaseSpeed();}else{logicTimer.reset();}
			diffLevel();
			spawnPiece();
		}
		
		if(bonus_trigger && !speedUp){
			logicTimer.setCyclesPerSecond(1f);
			logicTimer.reset();
		}
	}
	
	
	public void renderGame() throws IOException {
		board.repaint();
		side.updateGUI();
	}
	
	
	public void resetGame() {
		if(isGameOver){
			this.time = new Time();
		}else{
		time.start();
		}
		this.level = 0;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = generateRandomPiece();
		this.isNewGame = false;
		this.isGameOver = false;		
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
		
	
	private void spawnPiece() {
		this.currentType = nextType;
		randomPosition();
		this.currentRotation = 0;
		this.nextType = generateRandomPiece();
		randomRotation();
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			gameOver();
			time.pause();
			logicTimer.setPaused(true);
		}		
	}


	private void rotatePiece(int newRotation) {
		int newColumn = currentCol;
		int newRow = currentRow;
		
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);
		
		
		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		
		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	
	
	private void checkClearedLines(){
		int cleared = board.checkLines();
		if(cleared >= 2){
			bonustime();
		}
		if(cleared > 0) {
			score += cleared * 100;
		}
	}
	
	private void diffLevel(){
		String[] array = new String(Integer.toString(score)).split("");
		String nivel;
		if(score<100){
			nivel = "0";
		}else if(score<=1000){
			nivel = array[0];
		}else{
			nivel = array[0];
			nivel +=array[1];
		}
		level = Integer.parseInt(nivel);
	}
	
	
	
	
	//--------------------------------------------PROBABILIDADES----------------------------------------------------------------------
	
	
	/*
	 * Variavel continua
	 * Funcao Weibull
	 * 
	 */
	
	private double bonus_time_b =1.5f;  // 1/3.4f;
	private double bonus_time_d = 1/1.5f;  // 1/2f;
	
	private void bonustime(){
		double a = random.nextDouble();
		double b = bonus_time_b;
		double c = -Math.log(a);
		double d = bonus_time_d;
		double e = Math.pow(c, d);
		Double f = e * b * 10;
		System.out.println("[BONUS TIME OF : " + f.intValue() + "s. ]");
		if(this.bonustime + f <= 60){
		this.bonustime += f.intValue();
		logicTimer.setCyclesPerSecond(1f);
		logicTimer.reset();
		}else{
			this.bonustime = 60;
			logicTimer.setCyclesPerSecond(1f);
			logicTimer.reset();
		}
	}
	
	
	
	/*
	 * Variavel continua
	 * Funcao Exponencial
	 * 
	 */
	
	double increade_speed_mxx = 1f;
	
	private void increaseSpeed(){
		double a = random.nextDouble();
		double log = Math.log(a);
		double mxx = increade_speed_mxx;
		double x = (-mxx * log)/100;
		this.gameSpeed+=Math.round((double)x * 10000.0) / 10000.0;
		logicTimer.setCyclesPerSecond(gameSpeed);
		logicTimer.reset();
	}
	
	
	
	/*
	 * Variavel discreta
	 * Funcao Binomial
	 */
	
	private void randomRotation(){
		ArrayList<Variable> rotations = new ArrayList<Variable>();
		float a = random.nextFloat();
		int rotation = 0;
		double res = 0.0;
		double soma = 0.0;
		
		for(int k = 0; k <= 3; k++){
		for(int j= 0; j<= k; j++){
			int comb = combination(4, k);
			double pexp = Math.pow(1/4f, k);
			double va = Math.pow((1-(1/4f)), (4-k));
			res = pexp * va * comb;
		}
			soma += res;
			rotations.add(new Variable(soma,k));
		}
		
		rotations = sortList(rotations);
		
		if(a > 0 && a <= rotations.get(0).getProb()) {
			rotation = rotations.get(0).getRotation();
		}else 
			if(a > rotations.get(0).getProb() && a <= rotations.get(1).getProb()){
				rotation = rotations.get(1).getRotation();
		}else 
			if(a > rotations.get(1).getProb() && a <= rotations.get(2).getProb()){
				rotation = rotations.get(2).getRotation();
		}else 
			if(a > rotations.get(2).getProb() && a <= rotations.get(3).getProb()){
				rotation = rotations.get(3).getRotation();
		}else if(a > rotations.get(3).getProb()){
				rotation = rotations.get(3).getRotation();
		}
		
		rotatePiece(rotation);
	}
	
	
	private double generate_piece_x = 1/7f;
	private double generate_piece_media = 3f;//2.16f;
	
	/*
	 * Variavel discreta
	 *Distribuicao Uniforme && Poisson
	 */
	private TileType generateRandomPiece(){
		ArrayList<Variable> figuras = new ArrayList<Variable>();
		float a = random.nextFloat();
		int piece = 0;
		float summ = 0;
		double media = generate_piece_media;
		double dl=level;
		double soma = 0;
		double result = 0.0f;
			
			if(dl%3!=0 || level==0){
				for(int i = 0; i < TYPE_COUNT; i++){
					figuras.add(new Variable(i, summ+=generate_piece_x));
				}
			}else{
				for (int x = 0; x < TYPE_COUNT; x++) {
					for(int j= 0; j<= x; j++){
						double factorial = factorial(x);
						double e =  Math.exp(-media);
						double b = (float) Math.pow(media, x);
						result = (e*b)/factorial;
					}
						soma +=result;
						figuras.add(new Variable(x, (float) soma));
				}
			}
				
				figuras = sortList(figuras);
			
				if(a > 0 && a <= figuras.get(0).getProb()) {
					piece = figuras.get(0).getType();
				}else if(a > figuras.get(0).getProb() && a <= figuras.get(1).getProb()){
					piece = figuras.get(1).getType();
				}else if(a > figuras.get(1).getProb() && a <= figuras.get(2).getProb()){
					piece = figuras.get(2).getType();
				}else if(a > figuras.get(2).getProb() && a <= figuras.get(3).getProb()){
					piece = figuras.get(3).getType();
				}else if(a > figuras.get(3).getProb() && a <= figuras.get(4).getProb()){
					piece = figuras.get(4).getType();
				}else if(a > figuras.get(4).getProb() && a <= figuras.get(5).getProb()){
					piece = figuras.get(5).getType();
				}else if(a > figuras.get(5).getProb() && a <= figuras.get(6).getProb()){
					piece = figuras.get(6).getType();
				}	if(a > figuras.get(6).getProb()){
					piece = figuras.get(6).getType();
				}
				
			System.out.println("Random: " + a + " Peca: " + piece);
			
		return TileType.values()[piece];
	}
	
	private double random_position_x = 1/6f;
	
	
	/*
	 * Variavel discreta
	 * Funcao geometrica
	 */
	private void randomPosition(){
		ArrayList<Variable> positions = new ArrayList<Variable>();
		float a = random.nextFloat();
		int position = 0;
		int maxPos = 10;
		
		for(int k = 0; k < maxPos; k++){
			double v = (1 - (random_position_x));
			float b = (float) Math.pow(v, k);
			float res = (float) (b - (random_position_x));
			positions.add(new Variable(res ,k));
		}
		
		positions = sortList(positions);
		
		if(a > 0 && a <= positions.get(0).getProb()) {
			position = positions.get(0).getPosition();
		}else 
			if(a > positions.get(0).getProb() && a <= positions.get(1).getProb()){
			position = positions.get(1).getPosition();
		}else 
			if(a > positions.get(1).getProb() && a <= positions.get(2).getProb()){
			position = positions.get(2).getPosition();
		}else 
			if(a > positions.get(2).getProb() && a <= positions.get(3).getProb()){
			position = positions.get(3).getPosition();
		}else 
			if(a > positions.get(3).getProb() && a <= positions.get(4).getProb()){
			position = positions.get(4).getPosition();
		}else 
			if(a > positions.get(4).getProb() && a <= positions.get(5).getProb()){
			position = positions.get(5).getPosition();
		}else 
			if(a > positions.get(5).getProb() && a <= positions.get(6).getProb()){
			position = positions.get(6).getPosition();
		}else 
			if(a > positions.get(6).getProb() && a <= positions.get(7).getProb()){
			position = positions.get(7).getPosition();
		}else 
			if(a > positions.get(7).getProb() && a <= positions.get(8).getProb()){
			position = positions.get(8).getPosition();
		}else 
			if(a > positions.get(8).getProb() && a <= positions.get(9).getProb()){
			position = positions.get(9).getPosition();
		}
		
		this.currentCol = position;
		this.currentRow = currentType.getSpawnRow();
	}
	
	
	
	//--------------------------------------------PROBABILIDADES----------------------------------------------------------------------
	
	
	
	
	private ArrayList<Variable> sortList(ArrayList<Variable> var){
		ArrayList<Variable> res = var;
				
				res.sort(new Comparator<Variable>() {
					@Override
					public int compare(Variable arg0, Variable arg1) {
						int result = Double.compare(arg0.getProb(), arg1.getProb());
						if (result == 0)
						  result = Double.compare(arg0.getProb(), arg1.getProb());
						return result;
					}
				});
				
			return res;
	}
	
	private void newBoard(){
		this.side  = new SidePanel(this);	
		this.board = new BoardPanel(this);
		
		main_panel.add(side);
		main_panel.add(board);
		
		add(main_panel, BorderLayout.CENTER);
	}
	
	private void gameOver(){
		main_panel.removeAll();
		main_panel.add(end_score);
		main_panel.add(game_over_label);
		main_panel.add(back_to_main);
		main_panel.add(restart);
		end_score.setText(String.valueOf(score));
		FileHandler.update_score(currently_playing.getUsername(), score);
		logicTimer.setPaused(true);
		repaint();
	}
	
	public int randInt(int min, int max) {
	    int randomNum = this.random.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	
	public float randFloat(float min, float max) {
	    float randomNum = this.random.nextFloat() * (max - min) + min;
	    return randomNum;
	}
	
	
	public int combination(int n, int k){
	    return factorial(n) / (factorial(k)  * factorial(n - k));
	}

	
	public static int factorial(int n){
        int ret = 1;
        for (int i = 1; i <= n; ++i) ret *= i;
        return ret;
    }
	
	
	public boolean isPaused() {
		return isPaused;
	}
	
	
	public boolean isGameOver() {
		return isGameOver;
	}
	

	public boolean isNewGame() {
		return isNewGame;
	}
	

	public int getScore() {
		return score;
	}
	

	public int getLevel() {
		return level;
	}
	

	public TileType getPieceType() {
		return currentType;
	}
	
	
	public TileType getNextPieceType() {
		return nextType;
	}
	
	
	public int getPieceCol() {
		return currentCol;
	}
	
	
	public int getPieceRow() {
		return currentRow;
	}
	
	
	public int getPieceRotation() {
		return currentRotation;
	}

	public Time getTime() {
		return time;
	}

	public JPanel getBoardPanel() {
		return this.board;
	}

	public User getUserPlaying() {
		return this.currently_playing;
	}


	public int getBonusTime() {
		return this.bonustime;
	}
	
}