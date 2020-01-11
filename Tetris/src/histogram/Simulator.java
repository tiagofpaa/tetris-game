package histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import tetrisPi.Variable;


public class Simulator {
	
	private final static int sim_number = 10000;
	private static  Random random = new Random();
	
	
	/*
	 * Simulacao bonus Time
	 * Variavel continua
	 * Funcao Weibull
	 * 
	 */
	public static double[] simulate_bonustime(){
		double[] resultset = new double[sim_number];
		
		for(int i = 0; i < resultset.length; i++){
		double a =  random.nextDouble();
		double b = 1.5f;
		double c = -Math.log(a);
		double d = 1/1.5f;  //1/B
		double e = Math.pow(c, d);
		Double f = e * b;
		resultset[i] =  f;
		}
		return resultset;
	}
	
	
	
	/*
	 * Simulacao increase speed
	 * Variavel continua
	 * Funcao Exponencial
	 * 
	 */
	public static double[] simulate_increaseSpeed(){
		double[] resultset = new double[sim_number];
		for(int i = 0; i < resultset.length; i++){
			double a = random.nextDouble();
			double log = Math.log(a);
			float mxx =   1f;
			double x = -mxx * log;
			double aa = Math.round((double)x * 10000.0) / 10000.0;
			resultset[i] =  aa;
		}
		return resultset;
	}
	

	
	
	
	/*
	 * Simulacao rotation
	 * Variavel discreta
	 * Funcao Binomial
	 */
	public static int[] simulate_randomRotation(){
		int[] resultset = new int[sim_number];
		ArrayList<Variable> rotations = new ArrayList<Variable>();
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
		
		for(int i = 0; i < resultset.length; i++){
		float a = random.nextFloat();
		
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
		resultset[i] = rotation;
		}
		
		return resultset;
	}
	
	
	
	
	
	/*
	 * Simulacao generate random piece
	 * Variavel discreta
	 *Distribuicao Uniforme && Poisson
	 */
	public static int[] simulate_generateRandomPiece(){
		int[] resultset = new int[sim_number];
		ArrayList<Variable> figuras = new ArrayList<Variable>();
		int piece = 0;
		float media = 3f;
		double result = 0.0;
		double soma = 0;
		
				for (int x = 0; x < 7; x++) {
				for(int j= 0; j<= x; j++){
					double factorial = factorial(x);
					double e =  Math.exp(-media);
					double b = (float) Math.pow(media, x);
					result = (e*b)/factorial;
				}
					soma +=result;
					figuras.add(new Variable(x, (float) soma));
				}
				
				figuras = sortList(figuras);
				
			for(int i = 0; i < resultset.length; i++){
				float a = random.nextFloat();
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
				
				resultset[i] = piece;
			}
			
		return resultset;
	}
	
	
	/*
	 * Simulacao random position
	 * Variavel discreta
	 * Funcao geometrica
	 */
	public static int[] simulate_randomPosition(){
		int[] resultset = new int[sim_number];
		ArrayList<Variable> positions = new ArrayList<Variable>();
		int position = 0;
		int maxPos = 10;
		
		for(int k = 0; k < maxPos; k++){
			float v = (1 - (1/6f));
			float b = (float) Math.pow(v, (k));
			float res = (float) (1/6f)*b;
			positions.add(new Variable(res ,k));
		}
		
		positions = sortList(positions);
		
			
		for(int i = 0; i < resultset.length; i++){
		float a = random.nextFloat();
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
			resultset[i] = position;
		}
		
		return resultset;
	}
	
	
	private static ArrayList<Variable> sortList(ArrayList<Variable> var){
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
	

	
	public static float randFloat(float min, float max) {
	    float randomNum = random.nextFloat() * (max - min) + min;
	    return randomNum;
	}
	
	
	public static int combination(int n, int k){
	    return factorial(n) / (factorial(k)  * factorial(n - k));
	}

	
	public static int factorial(int n){
        int ret = 1;
        for (int i = 1; i <= n; ++i) ret *= i;
        return ret;
    }
}
