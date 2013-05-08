package org.upsam.civicrm.sync.dao;


import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * base datos utilizada en la sincronizacion
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */

public class HelperDataBase extends SQLiteOpenHelper
{
	
	public static final String DATABASE_NAME = "contacts_civi_sync";
    public static final int DATABASE_VERSION = 1;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",new Locale("es_ES"));
    
    /**TABLA DE CONTACTOS*/
    public static final String TABLA_CONTACTOS = "CIVICONTACTS";
    
    public static final String TABLA_CONTACTOS_COL_ID = "_ID";
    public static final String TABLA_CONTACTOS_COL_FECHA = "TIMESTAMP";
    public static final String TABLA_CONTACTOS_COL_CONTACTID = "CONTACTID";      
    public static final String TABLA_CONTACTOS_COL_IDANDROID = "IDANDROID";
    public static final String TABLA_CONTACTOS_COL_URIANDROID = "URIANDROID";  
    public static final String TABLA_CONTACTOS_COL_LOOKUPANDROID = "LOOKUPANDROID";  
    
    public static final String[] TABLA_CONTACTOS_COLS = {
    	TABLA_CONTACTOS_COL_ID,TABLA_CONTACTOS_COL_FECHA, TABLA_CONTACTOS_COL_CONTACTID,    	
    	TABLA_CONTACTOS_COL_IDANDROID,TABLA_CONTACTOS_COL_URIANDROID,TABLA_CONTACTOS_COL_LOOKUPANDROID};
    
    /**TABLA CONTROL SE EJECUTE SOLO UNA VEZ AL DIA*/
    public static final String TABLA_CONTROLEXECUTION = "CONTROLEXECUTION";
    
    public static final String TABLA_CONTROLEXECUTION_COL_ID = "_ID";
    public static final String TABLA_CONTROLEXECUTION_COL_FECHA = "FECHAEXECUTION";
    public static final String[] TABLA_CONTROLEXECUTION_COLS = {TABLA_CONTROLEXECUTION_COL_ID,TABLA_CONTROLEXECUTION_COL_FECHA};
   

	public HelperDataBase(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{
		 String CREATE_TABLA_CONTACTOS = "CREATE TABLE " + TABLA_CONTACTOS + "("
				 + TABLA_CONTACTOS_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + TABLA_CONTACTOS_COL_FECHA + " NUMERIC, "
                 + TABLA_CONTACTOS_COL_CONTACTID + " NUMERIC, "                                              
                 + TABLA_CONTACTOS_COL_IDANDROID + " TEXT, " 
                 + TABLA_CONTACTOS_COL_URIANDROID + " TEXT,"    
                 + TABLA_CONTACTOS_COL_LOOKUPANDROID + " TEXT"   
                 +")";

		  db.execSQL(CREATE_TABLA_CONTACTOS);
		  
		  String CREATE_TABLA_EXECUTION = "CREATE TABLE " + TABLA_CONTROLEXECUTION + "("
				  + TABLA_CONTROLEXECUTION_COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + TABLA_CONTROLEXECUTION_COL_FECHA + " TEXT)";
              
          db.execSQL(CREATE_TABLA_EXECUTION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
