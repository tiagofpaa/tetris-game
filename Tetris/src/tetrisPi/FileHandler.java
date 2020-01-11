package tetrisPi;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileHandler {
	
	private static JSONArray users;
	private static File appdata = new File(System.getenv("APPDATA") + "\\" + "Tetris");
	private static File path_file = new File(appdata.getAbsolutePath()+"/users.txt");
	private static ArrayList<User> user_list;
	private static JSONObject last_player;
	
	public static JPanel loadScores() throws IOException {
		JPanel scores = new JPanel();
		scores.setLayout(null);
		scores.setOpaque(true);
		scores.setBackground(new Color(0, 0, 0, 0));
		scores.setBounds(50, 198, 325, 180);
		Font font2 = new Font("Verdana", Font.BOLD, 15);

		JLabel firstplace = new JLabel("1");
		JLabel secondplace = new JLabel("2");
		JLabel thirdplace = new JLabel("3");
		JLabel fourthplace = new JLabel("4");
		JLabel fifthplace = new JLabel("5");
		JLabel sixplace = new JLabel("6");
		JLabel sevenplace = new JLabel("7");

		JLabel nameone = new JLabel("");
		JLabel nametwo = new JLabel("");
		JLabel namethree = new JLabel("");
		JLabel namefour = new JLabel("");
		JLabel namefive = new JLabel("");
		JLabel namesix = new JLabel("");
		JLabel nameseven = new JLabel("");

		JLabel scoreone = new JLabel("");
		JLabel scoretwo = new JLabel("");
		JLabel scorethree = new JLabel("");
		JLabel scorefour = new JLabel("");
		JLabel scorefive = new JLabel("");
		JLabel scoresix = new JLabel("");
		JLabel scoreseven = new JLabel("");


		firstplace.setFont(font2);
		firstplace.setForeground(Color.GREEN);
		
		fourthplace.setFont(font2);
		fourthplace.setForeground(Color.GREEN);
		
		fifthplace.setFont(font2);
		fifthplace.setForeground(Color.GREEN);
		
		sixplace.setFont(font2);
		sixplace.setForeground(Color.GREEN);
		
		sevenplace.setFont(font2);
		sevenplace.setForeground(Color.GREEN);

		secondplace.setFont(font2);
		secondplace.setForeground(Color.GREEN);

		thirdplace.setFont(font2);
		thirdplace.setForeground(Color.GREEN);

		nameone.setFont(font2);
		nameone.setForeground(Color.yellow);
		
		namefour.setFont(font2);
		namefour.setForeground(Color.yellow);
		
		namefive.setFont(font2);
		namefive.setForeground(Color.yellow);
		
		namesix.setFont(font2);
		namesix.setForeground(Color.yellow);
		
		nameseven.setFont(font2);
		nameseven.setForeground(Color.yellow);
		

		nametwo.setFont(font2);
		nametwo.setForeground(Color.yellow);

		namethree.setFont(font2);
		namethree.setForeground(Color.yellow);

		scoreone.setFont(font2);
		scoreone.setForeground(Color.red);

		scoretwo.setFont(font2);
		scoretwo.setForeground(Color.red);

		scorethree.setFont(font2);
		scorethree.setForeground(Color.red);
		
		scorefour.setFont(font2);
		scorefour.setForeground(Color.red);
		
		scorefive.setFont(font2);
		scorefive.setForeground(Color.red);
		
		scoresix.setFont(font2);
		scoresix.setForeground(Color.red);
		
		scoreseven.setFont(font2);
		scoreseven.setForeground(Color.red);
		

		firstplace.setBounds(0, 0, 25, 20);
		secondplace.setBounds(0, 25, 25, 20);
		thirdplace.setBounds(0, 50, 25, 20);

		nameone.setBounds(35, 0, 100, 20);
		nametwo.setBounds(35, 25, 100, 20);
		namethree.setBounds(35, 50, 100, 20);

		scoreone.setBounds(230, 0, 100, 20);
		scoretwo.setBounds(230, 25, 100, 20);
		scorethree.setBounds(230, 50, 100, 20);
		
		fourthplace.setBounds(0, 75, 25, 20);
		fifthplace.setBounds(0, 100, 25, 20);
		sixplace.setBounds(0, 125, 25, 20);
		sevenplace.setBounds(0, 150, 25, 20);

		namefour.setBounds(35, 75, 100, 20);
		namefive.setBounds(35, 100, 100, 20);
		namesix.setBounds(35, 125, 100, 20);
		nameseven.setBounds(35, 150, 100, 20);
		
		scorefour.setBounds(230, 75, 100, 20);
		scorefive.setBounds(230, 100, 100, 20);
		scoresix.setBounds(230, 125, 100, 20);
		scoreseven.setBounds(230, 150, 100, 20);

		scores.add(firstplace);
		scores.add(secondplace);
		scores.add(thirdplace);
		scores.add(fourthplace);
		scores.add(fifthplace);
		scores.add(sixplace);
		scores.add(sevenplace);

		scores.add(nameone);
		scores.add(nametwo);
		scores.add(namethree);
		scores.add(namefour);
		scores.add(namefive);
		scores.add(namesix);
		scores.add(nameseven);
		
		scores.add(scoreone);
		scores.add(scoretwo);
		scores.add(scorethree);
		scores.add(scorefour);
		scores.add(scorefive);
		scores.add(scoresix);
		scores.add(scoreseven);

		ArrayList<JLabel> places = new ArrayList<>();
		places.add(nameone);
		places.add(nametwo);
		places.add(namethree);
		places.add(namefour);
		places.add(namefive);
		places.add(namesix);
		places.add(nameseven);

		ArrayList<JLabel> points = new ArrayList<>();
		points.add(scoreone);
		points.add(scoretwo);
		points.add(scorethree);
		points.add(scorefour);
		points.add(scorefive);
		points.add(scoresix);
		points.add(scoreseven);

		readUsers();
		HashMap<String, Integer> scoremap = new HashMap<String, Integer>();
		ValueComparator bvc = new ValueComparator(scoremap);
		TreeMap<String, Integer> sortedscores = new TreeMap<String, Integer>(bvc);
		for (int i = 0; i < users.size(); i++) {
			JSONObject o = (JSONObject) users.get(i);
			String sc = String.valueOf(o.get("score"));
			int p = Integer.parseInt(sc);
			scoremap.put(o.get("nickName").toString(), p);
		}

		sortedscores.putAll(scoremap);

		int count = 0;
		for (String name : sortedscores.keySet()) {
			if (count < 7) {
				String key = name.toString();
				String value = scoremap.get(name).toString();
				places.get(count).setText(key);
				points.get(count).setText(value);
			}
			count++;
		}
		return scores;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<User> readUsers() {
		user_list = new ArrayList<>();
		users = new JSONArray();
		JSONParser parser = new JSONParser();
		try {
			Object objj = parser.parse(new FileReader(path_file));
			JSONObject jsonObject = (JSONObject) objj;
			users = (JSONArray) jsonObject.get("users");

			Iterator<Object> iterator = users.iterator();
			while (iterator.hasNext()) {
				JSONObject ooo = (JSONObject) iterator.next();
				String sc = String.valueOf(ooo.get("score"));
				user_list.add(new User(ooo.get("nickName").toString(), Integer.parseInt(sc)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_list;
	}
	
	public static void writeToFile(JSONObject obj) {
		try (FileWriter file = new FileWriter(path_file)) {
			file.write(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean playerExists(String text) {
		readUsers();
		for (Object o : users) {
			JSONObject obj = (JSONObject) o;
			if (obj.get("nickName").equals(text))
				return true;
		}
		return false;
	}

	
	@SuppressWarnings("unchecked")
	public static void update_score(String username, int score) {
	JSONArray us = new JSONArray();
	 if (playerExists(username)) {
		for(Object obj : users){
			JSONObject o = (JSONObject) obj;
			if(o.get("nickName").equals(username)){
				JSONObject ooo = (JSONObject) o;
				String sc = String.valueOf(ooo.get("score"));
				if(score > Integer.parseInt(sc)){
					o.put("score" , score);
					
				}
			us.add(o);
			}else{
				us.add(o);
			}
			
		}
		JSONObject write = new JSONObject();
		write.put("last", last_player);
		write.put("users", us);
		writeToFile(write);
	} 
		
	}

	@SuppressWarnings("unchecked")
	public static void newUser(String text) {
		JSONObject new_player = new JSONObject();
		new_player.put("nickName", text);
		new_player.put("score", "0");
		readUsers();
		users.add(new_player);
		JSONObject main = new JSONObject();
		main.put("users", users);
		main.put("last", last_player);
		writeToFile(main);
	}

	@SuppressWarnings("unchecked")
	public static void removePlayer(String selectedValue) {
		JSONArray deleted = new JSONArray();
		for (Object obj : users) {
			JSONObject jo = (JSONObject) obj;
			if (!jo.get("nickName").equals(selectedValue)) {
				deleted.add(jo);
			}
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("last", last_player);
		mainObj.put("users", deleted);
		writeToFile(mainObj);
	}
	
	@SuppressWarnings("unchecked")
	public static void setLastPlayer(String s){
			last_player = new JSONObject();
			last_player.put("nickName", s);
			JSONObject main = new JSONObject();
			main.put("users", users);
			main.put("last", last_player);
			writeToFile(main);
	}

	
	public static String getLastPlayer(){
		String ret = "";
		JSONParser parser = new JSONParser();
		try {
			Object objj = parser.parse(new FileReader(path_file));
			JSONObject jsonObject = (JSONObject) objj;
			JSONObject last = (JSONObject) jsonObject.get("last");
			if(last!=null){
				ret = (String) last.get("nickName");
			}else if(user_list.size()>0){
				ret = user_list.get(user_list.size()-1).getUsername();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	@SuppressWarnings({ "unchecked" })
	private static void initFile(){
		users = new JSONArray();
		JSONObject new_player = new JSONObject();
		new_player.put("nickName", "Player");
		new_player.put("score", "0");
		users.add(new_player);
		JSONObject main = new JSONObject();
		main.put("users", users);
		main.put("last", null);
		setLastPlayer("Player");
		writeToFile(main);
	}
	
	public static boolean configureAmb() {
		if(!appdata.isDirectory()){
			new File(appdata.getAbsolutePath()).mkdir();
			if(!appdata.isDirectory()){return false;}
		}
		if(appdata.listFiles().length==0){
			try {
				new File(appdata.getAbsoluteFile()+"/users.txt").createNewFile();
				initFile();
			} catch (IOException e) {
				return false;
			}
		}else{
			boolean aux = false;
			for(File f : appdata.listFiles()){
				if(f.getName().contains("users.txt")){
					aux = true;
				}
			}
			return aux;
		}
		return true;
	}
}
