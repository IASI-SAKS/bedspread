package it.cnr.iasi.leks.bedspread;

import it.cnr.iasi.leks.bedspread.impl.SimpleKnowledgeBase;
import it.cnr.iasi.leks.bedspread.impl.SimpleSemanticSpread;
import it.cnr.iasi.leks.bedspread.policies.SimpleTerminationPolicy;
import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;

public class SematicSpreadFactory {

	protected static SematicSpreadFactory FACTORY = null;
	
	protected SematicSpreadFactory(){		
	}
	
	public static synchronized SematicSpreadFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new SematicSpreadFactory();
		}
		return FACTORY;
	}

	public AbstractSemanticSpread getSemanticSpread(String uriIdentifier){
		AnyResource resource = RDFFactory.getInstance().createURI(uriIdentifier);
		Node origin = new Node(resource); 
	
		AbstractSemanticSpread s = this.getSimpleSemanticSpread(origin);
		return s;
	}
	
	private AbstractSemanticSpread getSimpleSemanticSpread(Node origin){
		KnowledgeBase kb = new SimpleKnowledgeBase();
		TerminationPolicy term = new SimpleTerminationPolicy();
	
		AbstractSemanticSpread semSpread = new SimpleSemanticSpread(origin, kb, term);
		return semSpread;
	}
}
