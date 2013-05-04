package org.upsam.civicrm.sync.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExecutionValueDAO {

	
	private SQLiteDatabase db;
	 
    public ExecutionValueDAO(SQLiteDatabase database) {
            db = database;
    }
    
    /**
     * Devuelve -1 en caso de error
     * @param contactValue
     * @return
     */
    public long add(ExecutionValue executionValue) 
    {
        ContentValues values = new ContentValues();
        values.put(HelperDataBase.TABLA_CONTROLEXECUTION_COL_FECHA,HelperDataBase.sdf.format(executionValue.getFecha()));          
        long res = db.insert(HelperDataBase.TABLA_CONTROLEXECUTION, null, values);
        return res; 
    }
    
    /**
     * 
     * @param key
     */
    public void remove(long key) 
    {
        db.delete(HelperDataBase.TABLA_CONTROLEXECUTION,
        		HelperDataBase.TABLA_CONTROLEXECUTION_COL_ID + "=" + key, 
                  null);
    }
    
    /**
     * 
     * @param executionValue
     * @param key
     */
    public void update(ExecutionValue executionValue,long key) 
    {
    	 String table = HelperDataBase.TABLA_CONTROLEXECUTION;    	 
    	 ContentValues values = new ContentValues();
    	 values.put(HelperDataBase.TABLA_CONTROLEXECUTION_COL_FECHA,HelperDataBase.sdf.format(executionValue.getFecha()));      
         String whereClause =HelperDataBase.TABLA_CONTROLEXECUTION_COL_ID +" = ?";
         String []whereArgs = new String[]{String.valueOf(key)};          
         db.update(table, values, whereClause, whereArgs);
    }
    
    /**
     * 
     */
    public List<ExecutionValue> getAllRecords() 
    {
        List<ExecutionValue> records = new ArrayList<ExecutionValue>();
        Cursor cursor = db.query(HelperDataBase.TABLA_CONTROLEXECUTION,
        		HelperDataBase.TABLA_CONTROLEXECUTION_COLS,null, null, null, null,
        		null);
 
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        	ExecutionValue rec = cursorToRegistroLlamada(cursor);
            records.add(rec);
            cursor.moveToNext();
        }
        return records; 
     }
    
    /**
     * 
     * @param c
     * @return
     */
    private ExecutionValue cursorToRegistroLlamada(Cursor c) 
    {
    	 ExecutionValue executionValue = new ExecutionValue();   	 
    	 executionValue.setId(c.getInt(c.getColumnIndex(HelperDataBase.TABLA_CONTROLEXECUTION_COL_ID)));    	     	
    	try
    	{
    	 executionValue.setFecha(HelperDataBase.sdf.parse(c.getString(c.getColumnIndex(HelperDataBase.TABLA_CONTROLEXECUTION_COL_FECHA))));
	    }
		 catch(ParseException pe){}
        return executionValue;
    }
}
