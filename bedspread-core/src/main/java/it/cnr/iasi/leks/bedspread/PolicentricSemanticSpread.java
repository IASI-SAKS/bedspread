/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 * 
 *	 You should have received a copy of the GNU General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.SemanticSpreadFactory;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public abstract class PolicentricSemanticSpread implements ComputationStatusCallback{

	private volatile Map<String, AbstractSemanticSpread> semanticSpreadMap;
	private volatile Map<String, ComputationStatus> semanticSpreadStatusMap;
	
	protected KnowledgeBase kb;
	
	private SecureRandom random;
	
	public PolicentricSemanticSpread (Set<Node> originSet, KnowledgeBase kb) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		this.random = new SecureRandom();

		this.semanticSpreadMap = Collections.synchronizedMap(new HashMap<String, AbstractSemanticSpread>());
		this.semanticSpreadStatusMap = Collections.synchronizedMap(new HashMap<String, ComputationStatus>());

		this.kb = kb;
		
		for (Node origin : originSet) {
			String key = this.randomId();
			AbstractSemanticSpread semSpread = SemanticSpreadFactory.getInstance().getSemanticSpread(origin, this.kb, key, this);
			this.semanticSpreadMap.put(key, semSpread);
			this.semanticSpreadStatusMap.put(key, semSpread.getComputationStatus());
		}
		
	}
	
	public void startProcessingAndFlushData(Writer out) throws InteractionProtocolViolationException, AbstractBedspreadException, IOException{
		this.startProcessing();
		while (! this.isOver()) {
			this.doSomethingWhileProcessing();
		}
		Set<Node> s = this.mergeProcessingResults();
		this.flushData(out);
	}
	
	public void startProcessing() {
		for (String key : this.semanticSpreadStatusMap.keySet()) {
			if (! this.isInProgress(key)){
				AbstractSemanticSpread semSpread = this.semanticSpreadMap.get(key);
				Thread t = new Thread(semSpread);
				t.start();				
			}
		}
		
	}
	
	public boolean isOver(){
		for (String key : semanticSpreadStatusMap.keySet()) {
			if ((this.isInProgress(key)) || (!this.wasActivated(key))){
				return false;
			}
		}
		return true;
	}
	
	public List<AbstractSemanticSpread> getCompletedSemanticSpreadList() throws InteractionProtocolViolationException{
		if (! this.isOver()){
			InteractionProtocolViolationException ex = new InteractionProtocolViolationException(PropertyUtil.INTERACTION_PROTOCOL_ERROR_MESSAGE);
			throw ex;
		}
		List<AbstractSemanticSpread> list = new ArrayList<AbstractSemanticSpread>(this.semanticSpreadMap.values());
		return list;
	}
	
	private String randomId() {
	    return new BigInteger(130, random).toString(32);
	}
	
	private boolean isInProgress(String key){
		return ((this.semanticSpreadStatusMap.get(key) == ComputationStatus.Running) || (this.semanticSpreadStatusMap.get(key) == ComputationStatus.Paused));
	}

	private boolean wasActivated(String key){
		return (this.semanticSpreadStatusMap.get(key) != ComputationStatus.NotStarted);
	}

	public void notifyStatus(String id, ComputationStatus status) {
		this.semanticSpreadStatusMap.put(id, status);
	}
	
	protected abstract void doSomethingWhileProcessing() throws AbstractBedspreadException;
	protected abstract void flushData(Writer out)throws InteractionProtocolViolationException, IOException;
	public abstract Set<Node> mergeProcessingResults() throws InteractionProtocolViolationException;
	
}
