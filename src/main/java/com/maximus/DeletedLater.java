package com.maximus;

public class DeletedLater {
	
	String test;
	boolean isTest;
	boolean isPushFromLocal;
	boolean isPushFromLocal3;
	boolean isUpdateFromRemote;
	boolean isTestFatch;
	boolean isTestPull;
	boolean isTestPushWithSSH;
	boolean isTestPullWithSSH;
	
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public boolean isTest() {
		return isTest;
	}
	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}
	public boolean isPushFromLocal() {
		return isPushFromLocal;
	}
	public void setPushFromLocal(boolean isPushFromLocal) {
		this.isPushFromLocal = isPushFromLocal;
	}
	public boolean isPushFromLocal3() {
		return isPushFromLocal3;
	}
	public void setPushFromLocal3(boolean isPushFromLocal3) {
		this.isPushFromLocal3 = isPushFromLocal3;
	}
	public boolean isUpdateFromRemote() {
		return isUpdateFromRemote;
	}
	public void setUpdateFromRemote(boolean isUpdateFromRemote) {
		this.isUpdateFromRemote = isUpdateFromRemote;
	}
	@Override
	public String toString() {
		return "DeletedLater [test=" + test + ", isTest=" + isTest + ", isPushFromLocal=" + isPushFromLocal
				+ ", isPushFromLocal3=" + isPushFromLocal3 + ", isUpdateFromRemote=" + isUpdateFromRemote + "]";
	}
}
