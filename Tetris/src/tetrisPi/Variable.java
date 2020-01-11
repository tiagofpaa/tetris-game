package tetrisPi;

public class Variable {
	
	private int type = 0;
	private double prob =0;
	private int rotation =0;
	
	public Variable(int type, Float prob) {
		this.type = type;
		this.prob = prob;
	}
	
	public Variable(double prob, int rotation){
		this.rotation = rotation;
		this.prob = prob;
	}
	
	public int getType(){
		return this.type;
	}
	
	public double getProb(){
		return this.prob;
	}
	
	public int getRotation(){
		return this.rotation;
	}
	public int getPosition(){
		return this.rotation;
	}

}
