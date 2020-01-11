package tetrisPi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SidePanel extends JPanel{
	
	ArrayList<ImageIcon> pieces  = new ArrayList<>();
	private Image background = Toolkit.getDefaultToolkit().createImage(Tetris.class.getResource("/side_panel_bg.png"));
	private JLabel next_piece;
	private boolean ok = false;
	private static final long serialVersionUID = 2181495598854992747L;
	private Tetris tetris;
	private JLabel user_playing = new JLabel("");
	private JLabel highest_score = new JLabel("");
	private JLabel current_score = new JLabel("");
	private JLabel current_level = new JLabel("");
	private JLabel time = new JLabel("");
	private JLabel bonus_time = new JLabel("");
	
	
	public SidePanel(Tetris tetris) {
		user_playing.setForeground(Color.WHITE);
		user_playing.setFont(new Font("Serif", Font.BOLD, 14));
		
		highest_score.setForeground(Color.WHITE);
		highest_score.setFont(new Font("Serif", Font.BOLD, 14));
		
		current_score.setForeground(Color.WHITE);
		current_score.setFont(new Font("Serif", Font.BOLD, 14));
		
		current_level.setForeground(Color.WHITE);
		current_level.setFont(new Font("Serif", Font.BOLD, 14));
		
		time.setForeground(Color.WHITE);
		time.setFont(new Font("Serif", Font.BOLD, 14));
		
		bonus_time.setForeground(Color.WHITE);
		bonus_time.setFont(new Font("Serif", Font.BOLD, 14));
		
		loadfiguras();
		this.tetris = tetris;
		setLayout(null);
		this.setBounds(443, 85, 160, 504);
		this.user_playing.setBounds(67, 219, 90, 30);
		this.highest_score.setBounds(98, 239, 90, 30);
		this.current_score.setBounds(70, 284 , 90 , 30);
		this.time.setBounds(63, 350, 90, 30);
		this.bonus_time.setBounds(110, 330, 90, 30);
		this.current_level.setBounds(63, 306, 90, 30);
		
		add(user_playing);
		add(highest_score);
		add(current_score);
		add(current_level);
		add(time);
		add(bonus_time);
	}
	


	public void updateGUI() throws IOException {
		update_nextPiece();
		update_stats();
	}
	
	private void update_nextPiece() throws IOException{
		TileType type = tetris.getNextPieceType();
		String figura_name = type.name().split("")[type.name().split("").length-1];
		for(ImageIcon f:pieces){
			if(f.getDescription().equals(figura_name)){updateNext(f);}
			
		}
	}
	
	private void loadfiguras(){
		ImageIcon i = new ImageIcon(Tetris.class.getResource("/pieces/I.png"));
		ImageIcon o = new ImageIcon(Tetris.class.getResource("/pieces/O.png"));
		ImageIcon j = new ImageIcon(Tetris.class.getResource("/pieces/J.png"));
		ImageIcon l = new ImageIcon(Tetris.class.getResource("/pieces/L.png"));
		ImageIcon s = new ImageIcon(Tetris.class.getResource("/pieces/S.png"));
		ImageIcon z = new ImageIcon(Tetris.class.getResource("/pieces/Z.png"));
		ImageIcon t= new ImageIcon(Tetris.class.getResource("/pieces/T.png"));
		
		i.setDescription("I");
		o.setDescription("O");
		j.setDescription("J");
		l.setDescription("L");
		s.setDescription("S");
		z.setDescription("Z");
		t.setDescription("T");
		
		pieces.add(i);
		pieces.add(o);
		pieces.add(j);
		pieces.add(l);
		pieces.add(s);
		pieces.add(z);
		pieces.add(t);
	}
	private void updateNext(ImageIcon f) throws IOException{
		if(ok){this.remove(next_piece);}
		next_piece = new JLabel(f);
		this.add(next_piece);
		next_piece.setBounds(20,77, 121,92);
		ok = true;
		this.repaint();
	}
	
	
	public void paintComponent(Graphics page){
	    super.paintComponent(page);
	    int h = background.getHeight(null);
	    int w = background.getWidth(null);

	    if ( w > this.getWidth() )
	    {
	        background = background.getScaledInstance( getWidth(), -1, Image.SCALE_DEFAULT );
	        h = background.getHeight(null);
	    }

	    if ( h > this.getHeight() )
	    {
	        background = background.getScaledInstance( -1, getHeight(), Image.SCALE_DEFAULT );
	    }
	    int x = (getWidth() - background.getWidth(null)) / 2;
	    int y = (getHeight() - background.getHeight(null)) / 2;

	    page.drawImage( background, x, y, null );
	}
	
	private void update_stats(){
		this.current_score.setText(String.valueOf(tetris.getScore()));
		this.user_playing.setText(tetris.getUserPlaying().getUsername());
		this.highest_score.setText(String.valueOf(tetris.getUserPlaying().getPoints()));
		String time = String.valueOf(tetris.getTime().getMin());
		time+= ":";
		time+= String.valueOf(tetris.getTime().getSeg());
		this.time.setText(time);
		this.current_level.setText(String.valueOf(tetris.getLevel()));
		String bonus_time ="00:";
		bonus_time += String.valueOf(tetris.getBonusTime());
		this.bonus_time.setText(bonus_time);
	}
}
