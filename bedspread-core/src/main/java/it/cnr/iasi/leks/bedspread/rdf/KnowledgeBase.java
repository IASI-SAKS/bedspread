package it.cnr.iasi.leks.bedspread.rdf;

import java.util.List;

public interface KnowledgeBase {

	public int degree(AnyResource node);
	public int depth(AnyResource node);
	public int isMemberof(AnyResource node);
	public List<AnyResource> getNeighborhood (AnyResource node);
}
