package xyz.wisecraft.autoroles.data;

public class Infop {
	private String name;
	private int BlocksBroke;
	private int DiaBroke;
	private int BlocksPlace;
	private int Time;
	private int trees;
	private String oldtimer;
	
	public Infop(String name, int BlocksBroke, int BlocksPlace, int DiaBroke, int Time, int trees, String oldtimer) {
		
		this.setName(name);
		this.setBlocksBroke(BlocksBroke);
		this.setBlocksPlace(BlocksPlace);
		this.setDiaBroke(DiaBroke);
		this.setTime(Time);
		this.setTrees(trees);
		this.setOldtimer(oldtimer);
		
		
	}
	

	public String getName() {
		return name;}

	public void setName(String name) {
		this.name = name;}

	public int getBlocksBroke() {
		return BlocksBroke;}

	public void setBlocksBroke(int blocksBroke) {
		BlocksBroke = blocksBroke;}
	
	public int getDiaBroke() {
		return DiaBroke;}
	
	public void setDiaBroke(int diaBroke) {
		DiaBroke = diaBroke;}

	public int getBlocksPlace() {
		return BlocksPlace;}

	public void setBlocksPlace(int blocksPlace) {
		BlocksPlace = blocksPlace;}

	public int getTime() {
		return Time;}

	public void setTime(int time) {
		Time = time;}

	public int getTrees() {
		return trees;}

	public void setTrees(int trees) {
		this.trees = trees;}

	public String getOldtimer() {
		return oldtimer;}

	public void setOldtimer(String oldtimer) {
		this.oldtimer = oldtimer;}
}
