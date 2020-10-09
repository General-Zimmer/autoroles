package xyz.wisecraft.autoroles.data;

public class Timers {

	private int fly;
	private int tree;
	
	
	public Timers(int fly, int tree) {
		this.setFly(fly);
		this.setTree(tree);
	}

	
	public int getFly() {
		return fly;
	}

	public void setFly(int ftime) {
		this.fly = ftime;
	}


	public int getTree() {
		return tree;
	}


	public void setTree(int tree) {
		this.tree = tree;
	}
}

