package org.upsam.civicrm.sync.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactValueDAO {

	
	private SQLiteDatabase db;
	 
    public ContactValueDAO(SQLiteDatabase database) {
            db = database;
    }
    
    /**
     * Devuelve -1 en caso de error
     * @param contactValue
     * @return
     */
    public long add(ContactValue contactValue) 
    {
        ContentValues values = new ContentValues();                       
        values.put(HelperDataBase.TABLA_CONTACTOS_COL_FECHA, contactValue.getFecha().getTime());
        values.put(HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID, contactValue.getContacId());      
        values.put(HelperDataBase.TABLA_CONTACTOS_COL_IDANDROID, contactValue.getIdAndroid());
        values.put(HelperDataBase.TABLA_CONTACTOS_COL_URIANDROID, contactValue.getUriAndroid());      
        values.put(HelperDataBase.TABLA_CONTACTOS_COL_LOOKUPANDROID, contactValue.getLookupAndroid());     
     
        long res = db.insert(HelperDataBase.TABLA_CONTACTOS, null, values);
        return res; 
    }
    
    /**
     * 
     * @param timestamp
     * @return
     */
    public List<ContactValue> getOutOfDate(Date timestamp)
    {
        List<ContactValue> records = new ArrayList<ContactValue>();
        
        String where = HelperDataBase.TABLA_CONTACTOS_COL_FECHA +" != ?";
        String [] params= new String[]{String.valueOf(timestamp.getTime())}; 
       
        
        Cursor cursor = db.query(HelperDataBase.TABLA_CONTACTOS,
        		HelperDataBase.TABLA_CONTACTOS_COLS,where, params, null, null,
        		HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID + " ASC");
 
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        	ContactValue rec = cursorToRegistroLlamada(cursor);
            records.add(rec);
            cursor.moveToNext();
        }
        return records; 
     }

    
    
    /**
     * 
     * @param key
     */
    public void remove(long key) 
    {
        db.delete(HelperDataBase.TABLA_CONTACTOS,
        		HelperDataBase.TABLA_CONTACTOS_COL_ID + "=" + key, 
                  null);
    }
    
    /**
     * 
     * @param contactValue
     * @param key
     */
    public void update(ContactValue contactValue,long key) 
    {
    	 String table = HelperDataBase.TABLA_CONTACTOS;
    	 
    	 ContentValues values = new ContentValues();
    	 values.put(HelperDataBase.TABLA_CONTACTOS_COL_FECHA, contactValue.getFecha().getTime());
         values.put(HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID, contactValue.getContacId());      
         values.put(HelperDataBase.TABLA_CONTACTOS_COL_IDANDROID, contactValue.getIdAndroid());
         values.put(HelperDataBase.TABLA_CONTACTOS_COL_URIANDROID, contactValue.getUriAndroid());  
         values.put(HelperDataBase.TABLA_CONTACTOS_COL_LOOKUPANDROID, contactValue.getLookupAndroid());     
         String whereClause =HelperDataBase.TABLA_CONTACTOS_COL_ID +" = ?";
         String []whereArgs = new String[]{String.valueOf(key)}; 
         
         db.update(table, values, whereClause, whereArgs);
    }
    
 
    /**
     * 
     * @param contact_id
     * @return
     */
    public ContactValue getContact(String contact_id)
    {        
    	 List<ContactValue> records = new ArrayList<ContactValue>();
    	 ContactValue contactValue=null;
         
         String where = HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID +" = ?";
         String [] params= new String[]{contact_id}; 
         
         Cursor cursor = db.query(HelperDataBase.TABLA_CONTACTOS,
         		HelperDataBase.TABLA_CONTACTOS_COLS,where, params, null, null,
         		HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID + " ASC");
  
         cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
         	 ContactValue rec = cursorToRegistroLlamada(cursor);
             records.add(rec);
             cursor.moveToNext();
         }
         if(records!=null && records.size()==1)
         {
        	 contactValue = records.get(0);
         }
         return contactValue; 
    }
       
            
    /**
     * 
     * @return
     */
    public List<ContactValue> getAllRecords() 
    {
        List<ContactValue> records = new ArrayList<ContactValue>();
        Cursor cursor = db.query(HelperDataBase.TABLA_CONTACTOS,
        		HelperDataBase.TABLA_CONTACTOS_COLS,null, null, null, null,
        		HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID + " ASC");
 
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        	ContactValue rec = cursorToRegistroLlamada(cursor);
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
     private ContactValue cursorToRegistroLlamada(Cursor c) 
     {
    	 ContactValue contactValue = new ContactValue();    	 
    	 contactValue.setId(c.getInt(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_ID)));    	     	     	 
    	 contactValue.setFecha(new Date(c.getLong(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_FECHA))));    	 
    	 contactValue.setContacId(c.getLong(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_CONTACTID)));    	
    	 contactValue.setIdAndroid(c.getString(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_IDANDROID)));
    	 contactValue.setUriAndroid(c.getString(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_URIANDROID)));   
    	 contactValue.setLookupAndroid(c.getString(c.getColumnIndex(HelperDataBase.TABLA_CONTACTOS_COL_LOOKUPANDROID)));  
         return contactValue;
     }
     
 
}
