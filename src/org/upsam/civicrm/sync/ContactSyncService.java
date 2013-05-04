package org.upsam.civicrm.sync;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.upsam.civicrm.MainActivity;
import org.upsam.civicrm.R;
import org.upsam.civicrm.beans.ContactAndroid;
import org.upsam.civicrm.beans.ContactSync;
import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.sync.dao.ContactValue;
import org.upsam.civicrm.sync.util.ContactProvider;
import org.upsam.civicrm.sync.util.HandlerData;
import org.upsam.civicrm.sync.util.SyncUtil;
import org.upsam.civicrm.util.Utilities;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;



/**
 * Sincronizacion de contactos con civicrm
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */
public class ContactSyncService extends IntentService 
{
    /**
     * nombre del grupo se crea en el movil	
     */
	private static final String nameGroup= "OpenMobileCRM";
	
	/**
	 * control de errores al obtener la lista de contactos del civi
	 */
	private String error_call_list;	
	private List<ContactSync> listContactos;
	
	/**
	 * datos de conexion
	 */
	private DataCivi datacivi;
	
	/**
	 * total de usuarios a sincronizar
	 */
	private String syncTotalUsers;
	
	/**
	 * Cada cuantas horas se ejecuta la sincronizacion
	 */
	private int executionHours;
	
	/**
	 * Grupo android de la agenda de contactos
	 */
	private long idGrupo;
	
	/**
	 * manejador de la base de datos
	 */
	private HandlerData handlerData;
	
	/**
	 * Mapa contactos de la agenda
	 */
	private Map<String,ContactAndroid> mapContacsAndroid;
	
	/**
	 * Hora de grabacion del proceso
	 */
	private Date timeStamp;
	
	
	/**
	 * cronometro de control de tiempo de ejecucion
	 */
	long inicio;
	long fin;
	long totalMsg;
	
   /**
    * 	preferencias
    */
	private SharedPreferences prefs;
	
	/**
	 * gestor de notificaciones
	 */
	private NotificationManager notificationManager;

	/**
	 * constructor del servicio
	 */
	public ContactSyncService() 
	{
	    super("ContactSyncService");
	}

	/**
	 * Tratamiento de la sincronizacion
	 */
	@Override
	protected void onHandleIntent(Intent intent) 
	{
	  
	  ContactValue contactBD =null;	 	
	  ContactAndroid contactAndroid= null;
	  
	  try
	  {
		  
		  if (Utilities.isInformationLoad(this))//comprobar no se ejecuta sin datos de autenticacion
		  {
		  
			  init();
			  
			  if(Utilities.isOnline(this))
			  {		  				  				  
				  
				//comprobar cuando ultima ejecucion. si ejecucion hace mas de las horas indicadas por preferencias ejecutamos
				if(SyncUtil.isNeverExecuted(handlerData) || !SyncUtil.isExecutedAlready(handlerData,executionHours))
				{
				   						
					//comenzamos a contar sincronizacion en este instante asi que inputamos tiempo de ejecucion
			    	SyncUtil.insertTimeStamp(handlerData,timeStamp);
					
				   //obtener contactos del civi
					new ContactListTask().execute(SyncUtil.getQueryContactos(datacivi));
			    	
			    	while(error_call_list==null)
			    	{
			    		try
			    		{
			    		 Thread.sleep(100);
			    		}
			    		catch(InterruptedException ie){}
			    	}
			    	
			    	if(!error_call_list.equalsIgnoreCase("KO") && listContactos!=null && listContactos.size()>0)
			    	{			    		
			    		
			    		//por preferencias se controla el numero total de usuarios a sincronizar
			    		List<ContactSync> listaAsincronizar = SyncUtil.getListaSincronizacion(listContactos,syncTotalUsers);
			    		
			    		initContacts();
			    		
			    		 //INI-BUCLE recorro todos los contactos del civi obtnidos
			    		for (ContactSync contactCIVI : listaAsincronizar) 
			    		{
			    			contactBD= handlerData.getContactDAO().getContact(contactCIVI.getContact_id());
			    			
			    			if(contactBD!=null)//si usuario ya existe en BBDD
			    			{
			    				//obtener este contacto de la agenda de android a partir de info BD
			    				contactAndroid = mapContacsAndroid.get(contactBD.getIdAndroid().trim());
			    				
			    				if(contactAndroid!=null)//comprobamos verdaderamente existe en la agenda de android
			    				{		    							    							    							
				    				if(SyncUtil.isSameRegister(contactCIVI,contactAndroid))//usuario coincide exactamente en nombre,phone y mail
				    				{				    									    									    					
				    					SyncUtil.updateBDTimeStamp(handlerData,contactBD,timeStamp);
				    				}
				    				else if(SyncUtil.isChangeRegister(contactCIVI,contactAndroid))//usuario coincide en nombre pero phone o mail son distintos
				    				{
				    					updateContactInAndroid(contactCIVI,contactBD,contactAndroid);//modifico usuario de la agenda 
				    					SyncUtil.updateBDTimeStamp(handlerData,contactBD,timeStamp);
				    				}
				    				else //no coincide en ningun campo nombre,phone o mail (para la uriAndroid el usuario no tiene nada que ver)	
				    				{		    				     		    					
				    					
				    					 //borro bbdd ese contacto y de la agenda de  Android
				    					 deleteContact(contactBD);
										 
										 //lo inserto de nuevo en Android y en bbdd
				    					 insertComplete(contactCIVI);	
				    				}
			    				}
			    				else
			    				{
			    					//borro bbdd ese contacto en caso no exista en la agenda.Este caso nunca deberia darse
			    					 SyncUtil.deleteBD(handlerData,contactBD);
			    				}
			    			}
			    			else //no existe usuario es un alta
			    			{
			    				//insertar en android y en bbdd BBDD solo guarda id,timestamp,contact_id,UriAndroid,idAndroid
			    				
			    				//EL CONTACTO NO TENGA NI TELEFONO Y MAIL: en este caso se creara solo con su nombre
			    				//EL CONTACTO TENGA varios TELEFONOs o MAILs: solo se inserta el primario.Para mas detalle que vaya a la applicacion	
			    				
			    				insertComplete(contactCIVI);		    								    						
			    			}
						}
			    		
			    		//TRATAMIENTO DE REGISTROS BORRADOS EN CIVI
			    		List<ContactValue> listaBorrados = handlerData.getContactDAO().getOutOfDate(timeStamp);
			    		if(listaBorrados!=null && listaBorrados.size()>0)
			    		{
			    			for (ContactValue contactToDelete : listaBorrados) 
			    			{
			    				deleteContact(contactToDelete);
							}		    					    			
			    		}
			    				    				    			    
			    		createNotification(SyncUtil.NOTIFICACION_SYNC_WAS_OK);
			    	}
			    	else
			    	{		    		
			    		createNotification(SyncUtil.NOTIFICACION_SYNC_FAIL_REST);
			    	}		    			    
				}		
			  }
			  else
			  {
				  createNotification(SyncUtil.NOTIFICACION_SYNC_NOTINTERNET);			  
			  }
		  }//fin-informacion autenticacion esta cargada
	  }	
	  catch(Exception e)
	  {
		  Log.e(ContactSyncService.class.toString(), "No se ejecuto sincronizacion");		    		 
		  createNotification(SyncUtil.NOTIFICACION_SYNC_WAS_KO);
	  }	
	  finally
	  {
		  finish();
	  }
	  	
	}

	/**
	 * Inserta en BD y en la agenda de android
	 * 
	 * @param contactSync
	 */
	private void insertComplete(ContactSync contactSync)
	{
		Uri uriContact = ContactProvider.insertContact(this,SyncUtil.createContactAndroid(contactSync,getString(R.string.sync_title_company)),idGrupo);
		
		if(uriContact!=null)
		{		    								
			SyncUtil.insertBD(this,idGrupo,handlerData, uriContact, contactSync, timeStamp);
		}
	}
	
	
	/**
	 * Inicializacion
	 */
	private void init()
	{	   		
				
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncTotalUsers = SyncUtil.getTotalUsers(this,prefs);
		executionHours =  SyncUtil.getHoursExecution(this,prefs);	
		datacivi = Utilities.getDataCivi(this);	
		error_call_list= null;		
		handlerData = HandlerData.getInstance(this);
		timeStamp = new Date();				
		inicio = System.currentTimeMillis();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
	}
	
	private void initContacts()
	{
		idGrupo =ContactProvider.getGroup(this,nameGroup);
		mapContacsAndroid =ContactProvider.getMapContactsFromGroup(this,handlerData,idGrupo) ;
	}
	
	/**
	 * 
	 */
	private void finish()
	{
		if(handlerData!=null)
		  handlerData.finish();		
	}
	
	/**
	 * 
	 */
	private void endTime()
	{
		fin = System.currentTimeMillis();
		totalMsg = fin -inicio;
	}
	
	/**
	 * Obtener el listado de contactos
	 * 
	 *
	 */
	private class ContactListTask extends AsyncTask<String,Void,String>
	{			
		    public ContactListTask(){}
			@Override
			protected String doInBackground(String... uriBuilder) 
			{				
				   StringBuilder builder = new StringBuilder();
			       try
			       {	    			    	     			   			    	   			    	  			    	   
			    	      HttpClient client = new DefaultHttpClient();    	       	    			    	   	    		    					    		
			    	      HttpGet httpGet = new HttpGet(uriBuilder[0]);			    	      			    	      			    	        	 
			    	      HttpResponse response = client.execute(httpGet);
			    	      StatusLine statusLine = response.getStatusLine();
			    	      int statusCode = statusLine.getStatusCode();
			    	      if (statusCode == 200) 
			    	      {
			    	        HttpEntity entity = response.getEntity();
			    	        InputStream content = entity.getContent();
			    	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			    	        String line= null;
			    	        while ((line = reader.readLine()) != null) {
			    	          builder.append(line);
			    	        }
			    	      } else {
			    	        Log.e(ContactListTask.class.toString(), "Failed to call rest");
			    	        error_call_list = "KO";
			    	      }    	       	   
			       }
			       catch(Exception e)
			       {
			    	   Log.i(ContactListTask.class.getName(),"ERROR OBTENIENDO contactos:"+e);
			    	   error_call_list = "KO";
			       }			       			       			     
				   return builder.toString();
			}
			
			@Override
			protected void onPostExecute(String result) 
			{	
				 ContactSync contactSync = null;
				  try 
				  {							
					  JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
					  
					  			
					  if("0".equalsIgnoreCase(object.getString("is_error")))
					  {
						  if(object.getJSONArray("values")!=null)
						  {
							  JSONArray listContacts = object.getJSONArray("values");

                             
                               for(int i=0;i< listContacts.length();i++)
                               {
                            	   JSONObject contacto =listContacts.getJSONObject(i);
                            	   
                            	   if(listContactos==null)
                            		   listContactos = new ArrayList<ContactSync>();
                            	   
                            	   contactSync = new ContactSync();
                            	   contactSync.setContact_id(contacto.getString("contact_id"));
                            	   contactSync.setDisplay_name(contacto.getString("display_name"));
                            	   contactSync.setContact_type(contacto.getString("contact_type"));
                            	   if(contacto.getString("email")!=null && !"".equalsIgnoreCase(contacto.getString("email")))
                            	   {
                            	     contactSync.setPrimary_mail(contacto.getString("email"));
                            	   }
                            	   if(contacto.getString("phone")!=null && !"".equalsIgnoreCase(contacto.getString("phone")))
                            	   {
                            	     contactSync.setPrimary_phone(contacto.getString("phone"));
                            	   }
                            	
                            	   listContactos.add(contactSync);
                               }
                               
                               error_call_list = "OKIS";

						  }
						  else
						  {
							  error_call_list = "KO";
						  }
					  }
					  else
					  {
						  error_call_list = "KO";
					  }						  										  					  
		    	  } 
				  catch (Exception e) 
				  {
					  Log.i(ContactListTask.class.getName(),"ERROR OBTENIENDO contactos:"+e);
					  error_call_list = "KO";
				  }					 		    	               
			}
		}

	@SuppressLint("NewApi")
	private void createNotification(int tipo)
	 {
		 String infoText = null;
		 String textoBreve = null;
		 
		 endTime();
		 int minutes = (int) ((totalMsg / (1000*60)) % 60);		
		 
		 notificationManager.cancelAll();
		 
		 if(tipo==SyncUtil.NOTIFICACION_SYNC_WAS_OK)
		 {
			 infoText = getString(R.string.sync_ok_notification) +" "+ String.valueOf(minutes) +" "+ getString(R.string.sync_ok_min_notification);
			 textoBreve =getString(R.string.sync_text_notification_ok);			
		 }
		 else if(tipo==SyncUtil.NOTIFICACION_SYNC_WAS_KO)
		 {
			 infoText = getString(R.string.sync_error_notification);
			 textoBreve =getString(R.string.sync_text_notification_ko);		
		 }
		 else if(tipo==SyncUtil.NOTIFICACION_SYNC_NOTINTERNET)
		 {
			 infoText = getString(R.string.sync_internet_notification);		
			 textoBreve =getString(R.string.sync_text_notification_ko);		
		 }
		 else if(tipo==SyncUtil.NOTIFICACION_SYNC_FAIL_REST)
		 {
			 infoText =getString(R.string.sync_ko_rest_notification);	
			 textoBreve =getString(R.string.sync_text_notification_ko);		
		 }
		 
		 Intent intent = new Intent(this, MainActivity.class);
		 PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		 		
		 
		 Notification noti = new Notification.Builder(this)
	        .setContentTitle(getString(R.string.sync_title_notification))
	        .setContentText(textoBreve)	 
	        .setContentIntent(pIntent)
	        .setStyle(new Notification.BigTextStyle().bigText(infoText)) 
	        .setAutoCancel(Boolean.TRUE)
	        .setVibrate(new long[] {0, 500, 250, 500 })
	        .setSmallIcon(R.drawable.ic_launcher).build();		
		 		

		 notificationManager.notify(0, noti); 
	 }
	
	/**
	 * 
	 * @param contactSync
	 * @param contactBD
	 * @param contactAndroid
	 */
	private void updateContactInAndroid(ContactSync contactCIVI,ContactValue contactBD,ContactAndroid contactAndroid)
	{
		//si estas en este metodo es porque el mail o el phone o ambos son distintos
						
		if(SyncUtil.isDiferent( contactCIVI.getPrimary_phone(),contactAndroid.getPhoneWork()))
		{			
			//ContactProvider.updateContactPhoneWork(this,Long.valueOf(contactAndroid.getContactId()), contactCIVI.getPrimary_phone());
			//debido al problema de los ids de las consultas el idcontact de contactAndroid es de las consultas pero 
			//para modificar el contacto necesito el id primero que me devolvio al insertar qeu lo tengo en la uri me devolvio el insert
			 Uri.Builder uriBuilder = Uri.parse(contactBD.getUriAndroid()).buildUpon();   
			 ContactProvider.updateContactPhoneWork(this,ContactProvider.getContact(uriBuilder.build()), contactCIVI.getPrimary_phone());
		}
		
		if(SyncUtil.isDiferent( contactCIVI.getPrimary_mail(),contactAndroid.getEmailWork()))
		{		
			//ContactProvider.updateContactEmailWork(this,Long.valueOf(contactAndroid.getContactId()), contactCIVI.getPrimary_mail());
			//lo mismo qeu antes
			Uri.Builder uriBuilder = Uri.parse(contactBD.getUriAndroid()).buildUpon();  
			ContactProvider.updateContactEmailWork(this,ContactProvider.getContact(uriBuilder.build()), contactCIVI.getPrimary_mail());
		}
	}
	
	
	/**
	 * borra un contacto de la BBDD y de la agenda de android
	 * @param contactToDelete
	 */
	private void deleteContact(ContactValue contactToDelete)
	{
		 //borro bbdd ese contacto
		 SyncUtil.deleteBD(handlerData,contactToDelete);
		 
		 //lo borro de la agenda de  Android		    					
		// Uri.Builder uriBuilder = Uri.parse(contactToDelete.getUriAndroid()).buildUpon();    
		// deleteContact(getLookupKeyContact(uriBuilder.build()));
		 ContactProvider.deleteContact(this, contactToDelete.getLookupAndroid());
	}
}
