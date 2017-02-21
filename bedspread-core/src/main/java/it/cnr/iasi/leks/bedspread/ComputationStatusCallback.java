package it.cnr.iasi.leks.bedspread;

public interface ComputationStatusCallback {

	public void notifyStatus(String id, ComputationStatus status);
}
