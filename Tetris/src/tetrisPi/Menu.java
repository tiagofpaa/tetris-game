package tetrisPi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


public class Menu extends JFrame{
	
	private static final long serialVersionUID = 1136823974110127369L;
	private ImageIcon main_screen = new ImageIcon(Menu.class.getResource("/main_screen.png"));
	private ImageIcon control = new ImageIcon(Menu.class.getResource("/controls.png"));
	private ImageIcon select_us = new ImageIcon(Menu.class.getResource("/select_user.png"));
	private ImageIcon high_scor = new ImageIcon(Menu.class.getResource("/high_scores.png"));
	private ArrayList<User> user_list = new ArrayList<>();
	private User current_user;
	private String version = "v 1.82 Beta";
	
	private JPanel menu;

	
	//Main_screen
	private JLabel main_menu_label;
	private JLabel currentUser;
	private JLabel vers;
	private JButton quick_game;
	private JButton select_user;
	private JButton high_scores;
	private JButton controls;
	private JButton back_to_main;

	//Controls_screen
	private JLabel control_label;
	
	//Select_user
	private JLabel select_user_label;
	@SuppressWarnings("rawtypes")
	private DefaultListModel user_list_model;
	@SuppressWarnings("rawtypes")
	private JList list;
	private JTextField username_text_field;
	private JScrollPane list_of_users;
	
	//High_scores
	private JLabel high_scores_label;
	private JButton add_user_butt;
	private JButton remove_user_butt;
	private JButton select_user_butt;
	
	
	public Menu() throws IOException {
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		
		
		menu = new JPanel();
		menu.setLayout(null);
		menu.setPreferredSize(new Dimension(400, 450));
		
		FileHandler.configureAmb();
		user_list = FileHandler.readUsers();	
		
		String s = FileHandler.getLastPlayer();
		for(User u :user_list){
			if(s.equals(u.getUsername())){
				current_user = u;
			}
		}
		if(current_user == null){
			current_user = user_list.get(user_list.size()-1);
		}
		
		init();
		
				
		ActionListener button_listener = new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == quick_game){
					quick_game();
				}else if(e.getSource() == select_user){
					select_user();
				}else if(e.getSource() == high_scores){
					try {
						high_scores();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else if(e.getSource() == controls){
					controls();
				}else if(e.getSource() == back_to_main){
					back_to_main();
				}else if(e.getSource() == add_user_butt){
					addUser(username_text_field.getText());
				}else if(e.getSource() == select_user_butt){
					if(list.getSelectedValue() != null){
						for(User u : user_list){
							if(u.getUsername().equals(list.getSelectedValue().toString())){
								current_user = u;
								FileHandler.setLastPlayer(u.getUsername());
								back_to_main();
							}
						}
					}
				}else if(e.getSource() == remove_user_butt){
					if(list.getSelectedValue() != null && user_list.size()-1 >=1){
						if(FileHandler.playerExists((String) list.getSelectedValue())){
							FileHandler.removePlayer((String) list.getSelectedValue());
							user_list_model.removeAllElements();
							user_list = FileHandler.readUsers();
							for(int i = 0; i < user_list.size();i++){
								user_list_model.add(i, user_list.get(i).getUsername());
							}
						}
					}
				}
				
			}

		};
		
		quick_game.addActionListener(button_listener);
		select_user.addActionListener(button_listener);
		high_scores.addActionListener(button_listener);
		controls.addActionListener(button_listener);
		back_to_main.addActionListener(button_listener);
		add_user_butt.addActionListener(button_listener);
		select_user_butt.addActionListener(button_listener);
		remove_user_butt.addActionListener(button_listener);
		
		
		menu.add(vers);
		menu.add(currentUser);
		menu.add(quick_game);
		menu.add(select_user);
		menu.add(high_scores);
		menu.add(controls);
		menu.add(main_menu_label);
		
		add(menu);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	
	public void quick_game(){
		FrameRunnable t = new FrameRunnable(this);
		new Thread(t).start();
		this.dispose();
	}
	
	@SuppressWarnings("unchecked")
	private void addUser(String text) {
		if(!FileHandler.playerExists(text)){
			FileHandler.newUser(text);
			username_text_field.setText("");
			user_list_model.removeAllElements();
			user_list = FileHandler.readUsers();
			for(int i = 0; i < user_list.size();i++){
				user_list_model.add(i, user_list.get(i).getUsername());
			}
			
		}else{
			JOptionPane.showMessageDialog(this,
					"Player " + text + " already exists.", "Creation ERROR",
					JOptionPane.ERROR_MESSAGE);
			username_text_field.setText("");
					
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void select_user(){
		menu.removeAll();
		user_list_model.removeAllElements();
		user_list = FileHandler.readUsers();
		for(int i = 0; i < user_list.size();i++){
			user_list_model.add(i, user_list.get(i).getUsername());
		}
		list.setSelectedIndex(0);
		back_to_main.setBounds(265, 385, 100, 35);
		menu.add(username_text_field);
		menu.add(list_of_users);
		menu.add(back_to_main);
		menu.add(add_user_butt);
		menu.add(remove_user_butt);
		menu.add(select_user_butt);
		menu.add(select_user_label);
		menu.repaint();
		this.repaint();
	}
	
	private void high_scores() throws IOException{
		menu.removeAll();
		back_to_main.setBounds(265, 385, 100, 35);
		menu.add(FileHandler.loadScores());
		menu.add(high_scores_label);
		menu.add(back_to_main);
		this.repaint();
	}
	
	private void controls(){
		menu.removeAll();
		back_to_main.setBounds(265, 385, 100, 35);
		menu.add(control_label);
		menu.add(back_to_main);
		this.repaint();
	}
	
	private void back_to_main(){
		menu.removeAll();
		currentUser.setText(current_user.getUsername());
		int width = (int) currentUser.getPreferredSize().getWidth();
		currentUser.setBounds((int) ((menu.getBounds().getWidth()/2) - (width/2)), 100, 200, 100);
		menu.add(vers);
		menu.add(currentUser);
		menu.add(quick_game);
		menu.add(select_user);
		menu.add(high_scores);
		menu.add(controls);
		menu.add(main_menu_label);
		this.repaint();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init() throws IOException{
		menu.removeAll();
		main_menu_label = new JLabel(main_screen);
		main_menu_label.setBounds(0, 0, 400, 450);
		
		select_user_label = new JLabel(select_us);
		select_user_label.setBounds(0, 0, 400, 450);

		high_scores_label = new JLabel(high_scor);
		high_scores_label.setBounds(0, 0, 400, 450);
		
		control_label = new JLabel(control);
		control_label.setBounds(0, 0, 400, 450);
		
		currentUser = new JLabel(current_user.getUsername());
		int width = (int) currentUser.getPreferredSize().getWidth();

		currentUser.setFont(new Font("Dialog", Font.PLAIN, 30));
		currentUser.setForeground (Color.WHITE);
		currentUser.setBounds((int) (170 - (width/2)), 100, 200, 100);
		
		vers = new JLabel(version);
		vers.setForeground (Color.WHITE);
		vers.setBounds(320, 380, 100, 100);
		
		
		
		add_user_butt = new JButton();
		add_user_butt.setOpaque(false);
		add_user_butt.setContentAreaFilled(false);
		add_user_butt.setBorderPainted(false);
		add_user_butt.setBounds(280, 195, 100, 33);
		
		remove_user_butt  = new JButton();
		remove_user_butt.setOpaque(false);
		remove_user_butt.setContentAreaFilled(false);
		remove_user_butt.setBorderPainted(false);
		remove_user_butt.setBounds(230, 252, 80, 33);
		
		select_user_butt  = new JButton();
		select_user_butt.setOpaque(false);
		select_user_butt.setContentAreaFilled(false);
		select_user_butt.setBorderPainted(false);
		select_user_butt.setBounds(230, 292, 80, 33);
		
		
		quick_game = new JButton();
		quick_game.setOpaque(false);
		quick_game.setContentAreaFilled(false);
		quick_game.setBorderPainted(false);
		quick_game.setBounds(80, 195, 240, 40);
		
		select_user = new JButton();
		select_user.setOpaque(false);
		select_user.setContentAreaFilled(false);
		select_user.setBorderPainted(false);
		select_user.setBounds(80, 250, 240, 40);
		
		high_scores = new JButton();
		high_scores.setOpaque(false);
		high_scores.setContentAreaFilled(false);
		high_scores.setBorderPainted(false);
		high_scores.setBounds(80, 307, 240, 40);
		
		controls = new JButton();
		controls.setOpaque(false);
		controls.setContentAreaFilled(false);
		controls.setBorderPainted(false);
		controls.setBounds(80, 363, 240, 40);
		
		back_to_main = new JButton();
		back_to_main.setOpaque(false);
		back_to_main.setContentAreaFilled(false);
		back_to_main.setBorderPainted(false);
		
		
	
		username_text_field = new JTextField();
		username_text_field.setBounds(22, 195, 240, 32);
		
		
		Object[] arr = new Object[user_list.size()];
		int kek =0;
		for(User u : user_list){
			arr[kek] = u.getUsername();
			kek++;
		}
		
		user_list_model = new DefaultListModel();
		list = new JList(user_list_model); 
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setVisibleRowCount(-1);
		list_of_users = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		list_of_users.setPreferredSize(new Dimension(198, 174));
		list_of_users.setBounds(22, 255, 198, 174);
		menu.setOpaque(false);
		list.setVisible(true);
		this.setVisible(true);
		
	}
	
	public User getUserPlaying() {
		return this.current_user;
	}
	
	public void menuNew(){
		try {
			new Menu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
