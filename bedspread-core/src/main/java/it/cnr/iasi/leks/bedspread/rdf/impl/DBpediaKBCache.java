package it.cnr.iasi.leks.bedspread.rdf.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 
 * @author ftaglino
 *
 */
public class DBpediaKBCache {
	
	int num_total_triple = 0;
	
	Map<String, Integer> num_triples_by_predicate = new HashMap<String, Integer>();
	Map<String, Integer> num_triples_by_subject = new HashMap<String, Integer>();
	Map<String, Integer> num_triples_by_object = new HashMap<String, Integer>();
	Map<String, Integer> num_triples_by_node = new HashMap<String, Integer>();
	
	Map<Vector<String>, Integer> num_triples_by_predicate_and_subject = new HashMap<Vector<String>, Integer>();
	Map<Vector<String>, Integer> num_triples_by_predicate_and_object = new HashMap<Vector<String>, Integer>();
	Map<Vector<String>, Integer> num_triples_by_subject_and_object = new HashMap<Vector<String>, Integer>();
	Map<Vector<String>, Integer> num_triples_by_predicate_and_node = new HashMap<Vector<String>, Integer>();
	
	
	public DBpediaKBCache() {
	
	}

}
