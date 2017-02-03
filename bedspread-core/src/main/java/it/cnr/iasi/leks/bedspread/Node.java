package it.cnr.iasi.leks.bedspread;

import java.util.HashSet;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;

public class Node {
	private AnyResource resource;
	private double activationScore;
	
	private Object MUTEX;
	
	public Node(AnyResource resource){
		this.resource = resource;
		this.activationScore = 0;
	}
	
	public AnyResource getResource(){
		return this.resource;
	}
	
	public double getScore(){
		double s;
		synchronized (MUTEX) {
			s = this.activationScore;
		}
		return s;
	}
	
	public void updateScore(double s){
		synchronized (MUTEX) {
			this.activationScore = s;
		}
	}
	
	@Override
	public boolean equals (Object obj){
		if (obj instanceof Node) {
			Node node = (Node) obj;
			String nodeID = node.getResource().getResourceID();
			String currentID = this.resource.getResourceID();
			return currentID.equalsIgnoreCase(nodeID);
		}	
		return false;		
	}
	
	@Override
	public int hashCode(){
		return this.resource.hashCode();
	}
}
