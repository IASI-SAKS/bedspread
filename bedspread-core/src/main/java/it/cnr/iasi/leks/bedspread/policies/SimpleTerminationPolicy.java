package it.cnr.iasi.leks.bedspread.policies;

public class SimpleTerminationPolicy extends TerminationPolicy {

	private final int MAX_QUERIES_BEFORE_TRUE = 10;
	private int nQueries;
	
	public SimpleTerminationPolicy(){
		this.nQueries = MAX_QUERIES_BEFORE_TRUE;
	}
	
	public SimpleTerminationPolicy(int i){
		this.nQueries = i;
	}

	@Override
	public boolean wasMet() {
		if (this.nQueries > 0){
			this.nQueries --;
			return true;
		}	
		return false;
	}

}
