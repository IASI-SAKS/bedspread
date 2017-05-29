/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 http://leks.iasi.cnr.it/tools/bedspread
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

import java.lang.Runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

/**
 * 
 * @author gulyx
 *
 */
public class SemanticSpreadJob implements Runnable{
	private Node origin;
	private AnyResource targetResource;
	
	private Node targetNode;
	
	private ComputationStatus status;

	protected SetOfNodesFactory setOfNodesFactory;
	
	private final Object callbackMutex = new Object();
	private ComputationCallback callback;
	private String jobID;
	
	protected KnowledgeBase kb;

	protected final Logger logger = LoggerFactory.getLogger(SemanticSpreadJob.class);
		
	public SemanticSpreadJob(Node origin, AnyResource targetResource, ComputationCallback callback, KnowledgeBase kb){
		this(origin, targetResource, targetResource.getResourceID(), callback, kb);
	}

	public SemanticSpreadJob(Node origin, AnyResource targetResource, String jobID, ComputationCallback callback, KnowledgeBase kb){
		this.targetResource = targetResource;
		this.origin = origin;
		
		this.kb = kb;
		this.status = ComputationStatus.NotStarted;
		
		this.jobID = jobID;
		this.callback = callback;
		
		this.setOfNodesFactory = SetOfNodesFactory.getInstance();		
	}

	public AnyResource getTargetReource(){
		return this.targetResource;
	}
	
	public Node getOrigin(){
		return this.origin;
	}
	
	public Node getProcessedNode() throws InteractionProtocolViolationException{
		synchronized (this.status){
			if (this.status != ComputationStatus.Completed){
				String msg = "Computation Status expected to be Completed but it resulted: " + this.status.name();
				throw new InteractionProtocolViolationException(msg);
			}
		}	
		return this.targetNode;
	}

	public ComputationStatus getComputationStatus() {
		ComputationStatus s;
		synchronized (this.status) {
			s = this.status;
		}
		return s;
	}
	
	public void run (){
		synchronized (this.status) {
			this.status = ComputationStatus.Running;
			this.logger.info("--- NEW EXECUTION ---");
		}
		
		this.targetNode = new Node(this.targetResource);
		Double newScore = this.callback.computeJobScore(this.origin, this.targetNode);
		this.targetNode.updateScore(newScore);

		synchronized (this.status) {
			this.status = ComputationStatus.Completed;
			this.logger.info("--- EXECUTION COMPLETED ---");
		}
		this.notifyCallback();
	}


	public ComputationStatus getStatus() {
		synchronized (this.status){
			return this.status;
		}	
	}
	
	private void notifyCallback(){
		synchronized (this.callbackMutex) {
			if ((this.jobID != null) && (this.callback != null)){
					try {
						this.callback.notifyJobStatus(this.jobID, this.getStatus());
					} catch (AbstractBedspreadException e) {
						this.logger.error("It was not possible to notify back the callback: JobID {}",this.jobID);
					}
			}
			else{
				this.logger.error("An error occourred while trying to nofity the callback: JobID {}, Callback {}",this.jobID, this.callback);
			}
		}	
	}
	
}
