package org.upsam.civicrm.sync.util;

import org.upsam.civicrm.sync.dao.ContactValueDAO;
import org.upsam.civicrm.sync.dao.ExecutionValueDAO;
import org.upsam.civicrm.sync.dao.HelperDataBase;

import android.content.Context;

public class HandlerData {

	private ContactValueDAO daoContact;
	private ExecutionValueDAO daoExecution;
	private HelperDataBase dbh;
	private static HandlerData instance;

	public static HandlerData getInstance(Context ctx) {
		if (instance == null) {
			synchronized (HandlerData.class) {
				if (instance == null) {
					instance = new HandlerData(ctx);
				}
			}
		}
		return instance;
	}

	private HandlerData(Context ctx) {
		dbh = new HelperDataBase(ctx);
		daoContact = new ContactValueDAO(dbh.getWritableDatabase());
		daoExecution = new ExecutionValueDAO(dbh.getWritableDatabase());
	}

	public void finish() {
		if(dbh!=null)
		   dbh.close();
		instance = null;
	}

	public ContactValueDAO getContactDAO() {
		return daoContact;
	}

	public ExecutionValueDAO getExecutionDAO() {
		return daoExecution;
	}

}
