package org.upsam.civicrm.sync.util;


import java.util.Date;
import java.util.List;

import org.upsam.civicrm.beans.ContactAndroid;
import org.upsam.civicrm.beans.ContactSync;
import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.sync.dao.ContactValue;
import org.upsam.civicrm.sync.dao.ExecutionValue;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class SyncUtil {
	
	public static final String SYNCRONIZED_ALL = "ALL";
	
	public static final int NOTIFICACION_SYNC_WAS_OK = 0;
	public static final int NOTIFICACION_SYNC_WAS_KO = 1;
	public static final int NOTIFICACION_SYNC_NOTINTERNET = 2;
	public static final int NOTIFICACION_SYNC_FAIL_REST = 3;
		
	public static void updateBDTimeStamp(HandlerData handlerData,ContactValue contactValue,Date timeStamp)
	{
		contactValue.setFecha(timeStamp);
		handlerData.getContactDAO().update(contactValue, contactValue.getId());
	}
	
	public static  void deleteBD(HandlerData handlerData,ContactValue contactValue)
	{
		handlerData.getContactDAO().remove(contactValue.getId());
	}
	
	public static ContactAndroid createContactAndroid(ContactSync contactSync,String textoCompany)
	{
		ContactAndroid contactAndroid = new ContactAndroid();
		
		if(contactSync!=null && contactSync.getDisplay_name()!=null)
		{
			contactAndroid.setName(contactSync.getDisplay_name().trim());
		}
		
		if(contactSync!=null && contactSync.getPrimary_mail()!=null)
		{
			contactAndroid.setEmailWork(contactSync.getPrimary_mail());
		}
		
		if(contactSync!=null && contactSync.getPrimary_phone()!=null)
		{
			contactAndroid.setPhoneWork(contactSync.getPrimary_phone());
		}
	    
		if(contactSync!=null && !TextUtils.isEmpty(contactSync.getContact_type()) && "Organization".equalsIgnoreCase(contactSync.getContact_type()))
		{
			contactAndroid.setCompany(textoCompany);
			contactAndroid.setTitleCompany(contactSync.getDisplay_name());
		}
			
	    return contactAndroid;
	
	}
	
	public static boolean isExecutedAlready(HandlerData handlerData, int hoursExecution)
	{
		List<ExecutionValue> listExecution = handlerData.getExecutionDAO().getAllRecords();
		
		Boolean resultado = Boolean.TRUE;
		if(listExecution!=null && listExecution.size()==1)
		{
			ExecutionValue executionValue = listExecution.get(0);			
			long difference = (new Date()).getTime() - executionValue.getFecha().getTime();
			long hours = (difference/1000)/3600;
			if(hours>=hoursExecution)
			{
				 resultado = Boolean.FALSE;
			} 
		}
		return resultado;
	}
	
	public static boolean isNeverExecuted(HandlerData handlerData)
	{
		List<ExecutionValue> listExecution = handlerData.getExecutionDAO().getAllRecords();
		Boolean resultado = Boolean.FALSE;
		if(listExecution==null || (listExecution!=null && listExecution.size()==0)) 
		{
			resultado = Boolean.TRUE;
		}
		return resultado;
	}
	
	
	public static String getString(String[] datos)
	{
		String resultado= null;
		if(datos!=null && datos.length>0)
		{
			for (String string : datos) 
			{
				if(resultado!=null)
				{
					resultado = resultado +","+string;
				}
				else
				{
					resultado =string;
				}
			}
		}
		return resultado;
	}
	
	public static String[] getString(String datos)
	{
		String [] resultado= null;
		if(datos!=null && !"".equalsIgnoreCase(datos))
		{
			resultado = datos.split(",");
		}
		return resultado;
	}
	
	public static String getQueryContactos(DataCivi datacivi)
	{
		    Uri.Builder uriBuilder = Uri.parse(datacivi.getBase_url()).buildUpon();           
	    	uriBuilder.appendQueryParameter("entity","Contact");
	    	uriBuilder.appendQueryParameter("action", "get");
	    	uriBuilder.appendQueryParameter("key", datacivi.getSite_key());
	    	uriBuilder.appendQueryParameter("api_key", datacivi.getApi_key());
	    	uriBuilder.appendQueryParameter("rowCount", "100000");    
	    	uriBuilder.appendQueryParameter("return[display_name]", "1");  
	    	uriBuilder.appendQueryParameter("return[contact_id]", "1");
	    	uriBuilder.appendQueryParameter("return[contact_type]", "1");
	    	uriBuilder.appendQueryParameter("return[email]", "1");
	    	uriBuilder.appendQueryParameter("return[phone]", "1");
	    	return uriBuilder.build().toString();
	}
	
	
	public static boolean isChangeRegister(ContactSync contactSync,ContactAndroid contactAndroid)
	{
		boolean resultado = Boolean.FALSE;										
			
		//if(contactSync.getDisplay_name().trim().equalsIgnoreCase(contactAndroid.getName().trim()))
		if(isSameDisplayName(contactSync.getDisplay_name(),contactAndroid.getName()))
		{
			if(isDiferent( contactSync.getPrimary_phone(),contactAndroid.getPhoneWork()))
			{
				resultado = Boolean.TRUE;
			}
			else if(isDiferent( contactSync.getPrimary_mail(),contactAndroid.getEmailWork()))
			{
				resultado = Boolean.TRUE;
			}
		}
										
		return resultado;
	}
	
	
	public static boolean isSameDisplayName(String c1,String c2)
	{
		//por alguna razon la agenda de android me mete puntos detras del Mr, Sr ect y tb me mete comas y al comparar
		//me da que son distintos cuando son iguales. de momento controlamos aqui la comparacion		
		return c1.trim().replace(".", "").replace(",", "").equalsIgnoreCase(c2.trim().replace(".", "").replace(",", ""));
	}
	
	public static boolean isSameRegister(ContactSync contactSync,ContactAndroid contactAndroid)
	{
		boolean resultado = Boolean.TRUE;										
			
		//if(!contactSync.getDisplay_name().trim().equalsIgnoreCase(contactAndroid.getName().trim()))
		if(!isSameDisplayName(contactSync.getDisplay_name(),contactAndroid.getName()))
		{
			resultado = Boolean.FALSE;
		}
		else if(isDiferent( contactSync.getPrimary_phone(),contactAndroid.getPhoneWork()))
		{
			resultado = Boolean.FALSE;
		}
		else if(isDiferent( contactSync.getPrimary_mail(),contactAndroid.getEmailWork()))
		{
			resultado = Boolean.FALSE;
		}
								
		return resultado;
	}
	
	public static boolean  isDiferent(String first,String second)
	{
		boolean resultado = Boolean.TRUE;
		
		if(first==null && second==null)
		{
			resultado = Boolean.FALSE;
		}				
		else if((first==null && "".equalsIgnoreCase(second)) || (second==null && "".equalsIgnoreCase(first)) )
		{
			resultado = Boolean.FALSE;
		}				
		else if(first!=null && first.equalsIgnoreCase(second))
		{
			resultado = Boolean.FALSE;
		}
		
		return resultado;
	}
	
	public static boolean isNumber(String valor)
	{
		boolean resultado = Boolean.TRUE;
		try
		{
		  Integer.parseInt(valor);
		}
		catch(NumberFormatException ne)
		{
			resultado = Boolean.FALSE;
		}
		return resultado;
		
	}
	
	public static List<ContactSync> getListaSincronizacion(List<ContactSync> listContactos,String syncTotalUsers)
	{
		if(SYNCRONIZED_ALL.equalsIgnoreCase(syncTotalUsers))
		{
			return listContactos;
		}
		else if(!isNumber(syncTotalUsers))
		{
			return listContactos;
		}
		else if(listContactos!=null && listContactos.size()<= Integer.parseInt(syncTotalUsers))
		{
			return listContactos;
		}
		else
		{
			return listContactos.subList(0, Integer.parseInt(syncTotalUsers)-1);
		}
		
	}
	
	
	public static String getTotalUsers(Context applicationContext,SharedPreferences prefsManager)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		String value = prefs.getString("PREF_USERS_SYNC", null);	
		//si el usuari no las carga no esta funcionando el default value asi qeu lo asigno aqui en ese caso
		if(value==null)
		{
			Editor editor = prefs.edit();
			editor.putString("PREF_USERS_SYNC",SYNCRONIZED_ALL);		
			editor.commit();
			value = prefs.getString("PREF_USERS_SYNC", null);	
		}
		
		return value;
	}
	
	public static int getHoursExecution(Context applicationContext,SharedPreferences prefsManager)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		String value = prefs.getString("PREF_HOURS_SYNC", null);
		//si el usuari no las carga no esta funcionando el default value asi qeu lo asigno aqui en ese caso
		if(value==null)
		{
			Editor editor = prefs.edit();
			editor.putString("PREF_HOURS_SYNC","1");
			editor.commit();
			value = prefs.getString("PREF_HOURS_SYNC", null);	
		}
		
		return Integer.parseInt(value);
	}
	
	public static boolean isActivateSync(Context applicationContext)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		boolean value = prefs.getBoolean("PREF_ACTIVATE_SYNC", Boolean.FALSE);				
		return value;
	}
	
	public static void insertTimeStamp(HandlerData handlerData,Date timeStamp)
	{
		List<ExecutionValue> listExecution = handlerData.getExecutionDAO().getAllRecords();
		
		if(listExecution==null || (listExecution!=null && listExecution.size()==0)) 
		{
			//si no existe lo damos de alta
			ExecutionValue executionValue = new ExecutionValue();
			executionValue.setFecha(timeStamp);
			handlerData.getExecutionDAO().add(executionValue);
		}
		else
		{
			//si registro existe modificamos sobre el (solo puede existir un registro en esta bbdd)
			ExecutionValue executionValue = listExecution.get(0);
			executionValue.setFecha(timeStamp);
			handlerData.getExecutionDAO().update(executionValue, executionValue.getId());
		}				
	}
	
	 public static boolean ithemRegister(ContactAndroid contactAndroid,String name,String phone,String mail)
	 {
		 Boolean resultado = Boolean.FALSE;

	   	  if(isSameDisplayName(contactAndroid.getName(), name))
	   	  {
	   		  if(!isDiferent(phone,contactAndroid.getPhoneWork()) && !isDiferent(mail,contactAndroid.getEmailWork()))
	   		  {
	   			 resultado = Boolean.TRUE;
	   		  }
	   		
	   	  }
		 return resultado;
	 }
	
	 public static void insertBD(Context context,long idGrupo,HandlerData handlerData, Uri uriContact,ContactSync contactSync,Date timeStamp)
	 {
			ContactValue contactValue = new ContactValue();
			
			//problemilla por alguna razon cuando mete el usuario en el grupo ya no corresponde el contact id con el que nos
			//devuelve la inserccion asi que mientras no localizo la forma de resolver esto necesito consultar el usuario recien insertado
			//para pillar su id del grupo que me devuelve esto sino no lo encuentro nunca
			ContactValue contactValueTemp = ContactProvider.getContactValueFromAndroid(idGrupo, context, contactSync.getDisplay_name(), contactSync.getPrimary_phone(), contactSync.getPrimary_mail());
			
			contactValue.setContacId(Long.valueOf(contactSync.getContact_id()));
			contactValue.setFecha(timeStamp);
						
			contactValue.setUriAndroid(uriContact.toString());
			
			//valores vamos a rectificar
			contactValue.setIdAndroid(contactValueTemp.getIdAndroid());					
			contactValue.setLookupAndroid(contactValueTemp.getLookupAndroid());
			
			handlerData.getContactDAO().add(contactValue);
	 }
}
