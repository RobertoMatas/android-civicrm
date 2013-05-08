package org.upsam.civicrm.sync.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upsam.civicrm.beans.ContactAndroid;
import org.upsam.civicrm.sync.ContactSyncService;
import org.upsam.civicrm.sync.dao.ContactValue;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactProvider {

	/**
	 * borrar contacto por lookup
	 * @param context
	 * @param lookupContact
	 */
	public static  void deleteContact(Context context,String lookupContact)
	{
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
		        null, null, null, null);
		while (cur.moveToNext()) {
		    try{
		        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);		        
		        if(lookupContact!=null && lookupContact.equalsIgnoreCase(lookupKey))
		        {
		         cr.delete(uri, null, null);
		        }
		    }
		    catch(Exception e)
		    {
		    	Log.e(ContactSyncService.class.getName(), "Exception encountered while deleting contact: " + e);
		    }
		}
	}
	
	/**
	 * obtener un contacto determinado
	 * @param groupID
	 * @param context
	 * @param displayNameIn
	 * @param phoneIn
	 * @param mailIn
	 * @return
	 */
	public  static ContactValue getContactValueFromAndroid(long groupID,Context context,String displayNameIn,String phoneIn,String mailIn) {
		    
		   
		   ContactAndroid  contactAndroid = null;
		   ContactValue resultado = null;
		   String contactId = null;

		    //uri llamada al servicio contactos
		   Uri groupURI = ContactsContract.Data.CONTENT_URI;

		    
		    //campos recuperamos
		    String[] projection = new String[] { 		    		
		    		ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,	
		    		
		    };
		   		   
		    //filtros de la query lanzamos
		    String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+ " = ?";
		    String[] selectionArgs = new String[]{String.valueOf(groupID)};
		    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";

		    //obtener los resultados
		    Cursor cursor= context.getContentResolver().query(groupURI, projection, selection, selectionArgs, sortOrder);
		    		
		    if(cursor !=null)
		    {
			    while (cursor.moveToNext()) 
			    {
	
			    	 
			      contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));				     
			      contactAndroid = new ContactAndroid();
			      contactAndroid.setContactId(contactId);
			    	 			    				     
			      //vamos a recuperar los datos-telefono,mail etc
			      Uri dataUri = ContactsContract.Data.CONTENT_URI;
			      Cursor dataCursor = context.getContentResolver().query(dataUri, null,
			    		  ContactsContract.Data.CONTACT_ID + "=" + contactId,null, null);
			      
			      String displayName="";
			      String nickName="";
			      String homePhone="";
			      String mobilePhone="";
			      String workPhone="";
			      String homeEmail="";
			      String workEmail="";
			      String companyName="";
			      String title = "";
			      while (dataCursor!=null && dataCursor.moveToNext()) 
				  {			    	 			    	  			    
			    	  //DISPLAYNAME
			    	  displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
			    	  contactAndroid.setName(displayName);
			    	  
			    	
			    	  //nickname
			    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
			    	  {
			    		  nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
			    		  contactAndroid.setNickName(nickName);
			    	  }
			    	  
			    	  //telefonos
			    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
			    	  {                                
			    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
			    		  {                                    
			    		     case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
			    		     {
			    		    	  homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
			    		    	  contactAndroid.setPhoneHome(homePhone);
			    		    	  break;
			    		     }
			    		     case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
			    		     {
			    		    	  mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
			    		    	  contactAndroid.setPhoneMobile(mobilePhone);
			    		    	  break;
			    		     }
			    		     case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
			    		     {			    		    	 			    		     
			    		    	  workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
			    		    	  contactAndroid.setPhoneWork(workPhone);
			    		    	  break;
			    		     }
			    		   } 			    		  
			    	   }
			    	  
			    	  //mails
			    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) 
			    	  {                                
			    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
			    		  {                                    
			    		    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
			    		    {
			    		    	homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
			    		    	contactAndroid.setEmailHome(homeEmail);
			    		    	break;
			    		    }
			    		    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
			    		    {
			    		    	workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
			    		    	contactAndroid.setEmailWork(workEmail);
			    		    	break;
			    		    }
			    		  }                           
			    	  }
			    	  
			    	  //COMPANIA
			    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
			    	  {                                
			    		  companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
			    		  title = dataCursor.getString(dataCursor.getColumnIndex("data4"));    
			    		  contactAndroid.setCompany(companyName);
			    		  contactAndroid.setTitleCompany(title);			    		  
			    	  } 
			    	  
			    	  contactAndroid.setLookupuri(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY )));		    	
			    	  
				  }	
				      if(contactAndroid!=null)
				      {
				    	  if(SyncUtil.ithemRegister(contactAndroid,  displayNameIn,phoneIn,mailIn))
				    	  {				    		  
				    		  resultado = new ContactValue();
				    		  resultado.setIdAndroid(contactAndroid.getContactId());				    		  
				    		  resultado.setLookupAndroid(contactAndroid.getLookupuri());				    		  				    						    		  
				    		  break;
				    	  }
				      }
			    }
		    }
		  
		    return resultado;
		  }		
	
	
		
		/**
		 * crear un grupo
		 * 
		 * @param nombreGrupo
		 * @return
		 */
	public static Uri createGroup(Context context,String nombreGrupo)
		{
			Uri result = null;
		    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Groups.CONTENT_URI)	         
		         .withValue(ContactsContract.Groups.TITLE,nombreGrupo)
		         .withValue(ContactsContract.Groups.GROUP_VISIBLE,Boolean.TRUE.booleanValue())	        
		    	 .build());

		    try {
		    	 ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		    	 if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI grupo:"+ res[0].uri);	
		            	result = res[0].uri;
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Grupo no insertado.");
		            }
		    } 
		    catch (Exception e) {
		        Log.e("Error", e.toString());
		    }
		    return result;
		}
		
		/**
		 * Obtener el id del grupo
		 * @param nombreGrupo
		 * @return
		 */
		public static long getGroup(Context context,String nombreGrupo)
		{
			 String groupId = getIdGrupo(context,nombreGrupo);
			 if(groupId!=null && !"".equalsIgnoreCase(groupId))
			 {
				 return Long.valueOf(groupId).longValue();
			 }
			 else
			 {
				 createGroup(context,nombreGrupo); 
				 return Long.valueOf(getIdGrupo(context,nombreGrupo)).longValue();
			 }
		}
		
		/**
		 * id del grupo
		 * @param nombreGrupo
		 * @return
		 */
		public static String getIdGrupo(Context context,String nombreGrupo)
		{
			 String selection = ContactsContract.Groups.DELETED + " = ? and " + ContactsContract.Groups.GROUP_VISIBLE + " = ?";        
			 String[] selectionArgs = { "0", "1" };
			 Cursor cursor =context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);        
			 cursor.moveToFirst();        
			 int len = cursor.getCount();        
			 String groupId = null;        
			 for (int i = 0; i < len; i++)        
			 {            
				 String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));            
				 String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));            
				 if (nombreGrupo.equalsIgnoreCase(title))
				 {                
					 groupId = id;                
					 break;            
				 }             
				 cursor.moveToNext();        
			  }        
			  cursor.close();   
			 
			  return groupId;
		}
		

		/**
		 * obtener laLookupkey de un contacto. Utilizado para el borrado de un determinado contacto
		 * @param uricontact
		 * @return
		 */
		public static Uri getLookupKeyContact(Context context,Uri uricontact)
		{
			String lookupKey = null;
			Uri uriLookUpKey =null;
			
			Uri uri = ContactsContract.Contacts.CONTENT_URI;
			
		    String[] projection = new String[] { 
		             ContactsContract.Contacts.LOOKUP_KEY		                        
		    };
		    	  	    	
		    //obtener los resultados
		    String selection = ContactsContract.Contacts._ID + " = '"+ (getContact(uricontact)) + "'";
		    Cursor cursor= context.getContentResolver().query(uri, projection, selection,null,null);
		    		 
		    if(cursor !=null)
		    {
			    while (cursor.moveToNext()) 
			    {

			    	lookupKey = cursor.getString(cursor
			          .getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
			     uriLookUpKey = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
			     
			    }
		    }	    	  
		    
		    return uriLookUpKey;
		}
		
		/**
		 * Obtener el id del contacto a partir de su uri
		 * @param uricontact
		 * @return
		 */
		public static long getContact(Uri uricontact)
		{
			
		    return Long.valueOf(uricontact.getPathSegments().get(1)).longValue();	    	    	 
		}
		
			
		/**
		 * 
		 * @param uricontact
		 * @param fieldChange
		 */
		@SuppressWarnings("all")
		private void updateContactEmailHome(Context context,Uri uricontact,String fieldChange)
		{		
			long rawContactId = getContact(uricontact);
			
			String where = ContactsContract.Data.RAW_CONTACT_ID +" = ? AND "+
					       ContactsContract.Data.MIMETYPE +" = ? AND "+
					       ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
			
			 String[] params = new String[] {String.valueOf(rawContactId),
					    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
			            String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_HOME)};

			
			 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					    .withSelection(where, params)
		                .withValue(ContactsContract.CommonDataKinds.Email.DATA, fieldChange)	                
		                .build());
			 try 
			 {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI update contact:"+ res[0].uri);	            		            
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not update.");
		            }
			 }
			 catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while updating contact: " + e);
		        }
		}
		

		
		/**
		 * 
		 * @param uricontact
		 * @param fieldChange
		 */
		public static void updateContactEmailWork(Context context,long rawContactId ,String fieldChange)
		{				
					
			String where = ContactsContract.Data.RAW_CONTACT_ID +" = ? AND "+
					       ContactsContract.Data.MIMETYPE +" = ? AND "+
					       ContactsContract.CommonDataKinds.Email.TYPE + " = ? ";
			
			 String[] params = new String[] {String.valueOf(rawContactId),
					    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
			            String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_WORK)};

			
			 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					    .withSelection(where, params)
		                .withValue(ContactsContract.CommonDataKinds.Email.DATA, fieldChange)	                
		                .build());
			 try 
			 {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI update contact:"+ res[0].uri);	            		            
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not update.");
		            }
			 }
			 catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while updating contact: " + e);
		        }
		}
		
		/**
		 * 
		 * @param uricontact
		 * @param fieldChange
		 */
		@SuppressWarnings("all")
		private void updateContactPhoneHome(Context context,Uri uricontact,String fieldChange)
		{
			
			long rawContactId = getContact(uricontact);
			
			String where = ContactsContract.Data.RAW_CONTACT_ID +" = ? AND "+
					       ContactsContract.Data.MIMETYPE +" = ? AND "+
					       ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
			
			 String[] params = new String[] {String.valueOf(rawContactId),
			            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
			            String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};

			
			 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					    .withSelection(where, params)
		                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fieldChange)	                
		                .build());
			 try 
			 {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI update contact:"+ res[0].uri);	            		            
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not update.");
		            }
			 }
			 catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while updating contact: " + e);
		        }
		}


		
		/**
		 * 
		 * @param uricontact
		 * @param fieldChange
		 */
		public static void updateContactPhoneWork(Context context,long rawContactId,String fieldChange)
		{						
			String where = ContactsContract.Data.RAW_CONTACT_ID +" = ? AND "+
					       ContactsContract.Data.MIMETYPE +" = ? AND "+
					       ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
			
			 String[] params = new String[] {String.valueOf(rawContactId),
			            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
			            String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)};

			
			 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					    .withSelection(where, params)
		                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fieldChange)	                
		                .build());
			 try 
			 {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI update contact:"+ res[0].uri);	            		            
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not update.");
		            }
			 }
			 catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while updating contact: " + e);
		        }
		}

		/**
		 * 
		 * @param uricontact
		 * @param fieldChange
		 */
		@SuppressWarnings("all")
		private void updateContactPhoneMobile(Context context,Uri uricontact,String fieldChange)
		{		
			long rawContactId = getContact(uricontact);
			
			String where = ContactsContract.Data.RAW_CONTACT_ID +" = ? AND "+
					       ContactsContract.Data.MIMETYPE +" = ? AND "+
					       ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
			
			 String[] params = new String[] {String.valueOf(rawContactId),
			            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
			            String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};

			
			 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					    .withSelection(where, params)
		                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fieldChange)	                
		                .build());
			 try 
			 {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {	            	
		            	Log.d(ContactSyncService.class.getName(), "URI update contact:"+ res[0].uri);	            		            
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not update.");
		            }
			 }
			 catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while updating contact: " + e);
		        }
		}

		
		/**
		 * Borrar todos los contactos de la agenda. 
		 */
		@SuppressWarnings("all")
		private void deleteAllContact(Context context)
		{
			ContentResolver cr = context.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
			        null, null, null, null);
			while (cur.moveToNext()) {
			    try{
			        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);		      		      
			        cr.delete(uri, null, null);		      
			    }
			    catch(Exception e)
			    {
			    	Log.e(ContactSyncService.class.getName(), "Exception encountered while deleting contact: " + e);
			    }
			}
		}
		
		/**
		 * borrar un determinado contacto
		 * @param uricontact
		 */
		public static void deleteContact(Context context,Uri lookupUri)
		{
			ContentResolver cr = context.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
			        null, null, null, null);
			while (cur.moveToNext()) {
			    try{
			        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);		        
			        if(lookupUri!=null && lookupUri.toString().equalsIgnoreCase(uri.toString()))
			        {
			         cr.delete(uri, null, null);
			        }
			    }
			    catch(Exception e)
			    {
			    	Log.e(ContactSyncService.class.getName(), "Exception encountered while deleting contact: " + e);
			    }
			}
		}
		

		
		
		/**
		 * Insertar un contacto
		 * @param contactAndroid
		 * @return
		 */
		public static Uri insertContact(Context context,ContactAndroid contactAndroid,long idGrupo) {
			 
			    Uri newContactUri = null;
			    		   
		        int phonetypeHome = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;	        	        
		        int phonetypeMovil = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;	        	      
		        int phonetypeWork = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;	        	       
		        int emailTypeHome =  ContactsContract.CommonDataKinds.Email.TYPE_HOME;	        	        
		        int emailTypeWork =  ContactsContract.CommonDataKinds.Email.TYPE_WORK;	        	      	       
		        
		        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		     	        
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
		                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "fernandoabruna@gmail.com")
		                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
		                .build());
		        
		        //display name
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,
		                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactAndroid.getName())
		                .build());
		        
		        //telefonos 
		        if(contactAndroid.getPhoneHome()!=null)
		        {
			        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                .withValue(ContactsContract.Data.MIMETYPE,
			                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactAndroid.getPhoneHome())
			                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phonetypeHome)
			                .build());
		        }
		        
		        if(contactAndroid.getPhoneMobile()!=null)
		        {
			        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                .withValue(ContactsContract.Data.MIMETYPE,
			                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactAndroid.getPhoneMobile())
			                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phonetypeMovil)
			                .build());
		        }
		        
		        if(contactAndroid.getPhoneWork()!=null)
		        {
			        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                .withValue(ContactsContract.Data.MIMETYPE,
			                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactAndroid.getPhoneWork())
			                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phonetypeWork)
			                .build());
		        }
		       //mails      
		        if(contactAndroid.getEmailHome()!=null)
		        {
			        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                .withValue(ContactsContract.Data.MIMETYPE,
			                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
			                .withValue(ContactsContract.CommonDataKinds.Email.DATA, contactAndroid.getEmailHome())
			                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailTypeHome)
			                .build());
		        }
		        
		        if(contactAndroid.getEmailWork()!=null)
		        {
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,
		                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.Email.DATA, contactAndroid.getEmailWork())
		                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailTypeWork)
		                .build());
		        }
		        
		      //compania  y titulo
		        if(contactAndroid.getCompany()!=null && contactAndroid.getTitleCompany()!=null)
		        {
			        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                .withValue(ContactsContract.Data.MIMETYPE,
			                		ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
			                .withValue(ContactsContract.CommonDataKinds.Organization.DATA, contactAndroid.getCompany())
			                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contactAndroid.getTitleCompany())
			                .build());
		        }
		        
		        
		        //anadimos el contacto a un grupo
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI) 
		        	.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)               
		        	.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)                
		        	.withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, idGrupo)
	                .build());
		        
		        try {	        	
		            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		            if (res!=null && res.length>0) 
		            {
		            	newContactUri = res[0].uri;	//a partir de aqui puedo coger el contacto para saber su id
		            	Log.d(ContactSyncService.class.getName(), "URI added contact:"+ newContactUri);
		            	
		            	
		            }
		            else
		            {
		            	Log.e(ContactSyncService.class.getName(), "Contact not added.");
		            }
		          
		        } 
		        catch (Exception e) 
		        {
		        	Log.e(ContactSyncService.class.getName(), "Exception encountered while inserting contact: " + e);
		        }
		        
		        return newContactUri;
		 }
		
	     /**
	      * Obtener un listado de contactos de toda la agenda
	      * TODO esta por terminar ya que de momento no es necesario su uso
	      */
	     @SuppressWarnings("all")
	     public static void getContacts(Context context) {
			    
			    //uri llamada al servicio contactos
			    Uri uri = ContactsContract.Contacts.CONTENT_URI;
			    
			    //campos recuperamos
			    String[] projection = new String[] { 
			    		 ContactsContract.Contacts._ID,
			             ContactsContract.Contacts.DISPLAY_NAME		                        
			    };
			    
			    //1 si tiene telefono
			    
			    //filtros de la query lanzamos
			   // String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"+ ("1") + "'";
			    String selection = null;
			    String[] selectionArgs = null;
			    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";

			    //obtener los resultados
			    Cursor cursor= context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
			    		 
			    if(cursor !=null)
			    {
				    while (cursor.moveToNext()) 
				    {
		
				      String contactId = cursor.getString(cursor
				          .getColumnIndex(ContactsContract.Data._ID));
				      
				      String displayname = cursor.getString(cursor
					          .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				    		      			      			      
				      
				      //vamos a recuperar los datos-telefono,mail etc
				      Uri dataUri = ContactsContract.Data.CONTENT_URI;
				      Cursor dataCursor = context.getContentResolver().query(dataUri, null,
				    		  ContactsContract.Data.CONTACT_ID + "=" + contactId,null, null);
				      
				      String displayName="";
				      String nickName="";
				      String homePhone="";
				      String mobilePhone="";
				      String workPhone="";
				      String homeEmail="";
				      String workEmail="";
				      String companyName="";
				      String title = "";
				      while (dataCursor!=null && dataCursor.moveToNext()) 
					  {
				    	  //DISPLAYNAME
				    	  displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
				    	  
				    	  //nickname
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
				    	  {
				    		  nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    	  }
				    	  
				    	  //telefonos
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :                                        
				    		    	  homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	  break;                                    
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :                                        
				    		    	  mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  break;                                    
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
				    		    	  workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  break;                                
				    		   }                            
				    	   }
				    	  
				    	  //mails
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) 
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
				    		    	homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	break;                                    
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
				    		    	workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	break;                               
				    		  }                           
				    	  }
				    	  
				    	  //COMPANIA
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		  title = dataCursor.getString(dataCursor.getColumnIndex("data4"));                            
				    	  } 
					  }
				      
				  
				    }
			    }

			  }

	     
		 /**
		  * Obtener todos los contactos de un determinado grupo
		  * @param groupID
		  */
	     @SuppressWarnings("all")
	     public static List<ContactAndroid> getContactsFromGroup(Context context,long groupID) {
			    
			 
			   List<ContactAndroid> listacontacts = new ArrayList<ContactAndroid>();
			   ContactAndroid  contactAndroid = null;
			   
			    //uri llamada al servicio contactos
			   Uri groupURI = ContactsContract.Data.CONTENT_URI;

			    
			    //campos recuperamos
			    String[] projection = new String[] { 		    		
			    		ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID		    		
			    };
			   		   
			    //filtros de la query lanzamos
			    String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+ " = ?";
			    String[] selectionArgs = new String[]{String.valueOf(groupID)};
			    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";

			    //obtener los resultados
			    Cursor cursor= context.getContentResolver().query(groupURI, projection, selection, selectionArgs, sortOrder);
			    		 
			    if(cursor !=null)
			    {
				    while (cursor.moveToNext()) 
				    {
		
				    	 
				      String contactId = cursor.getString(cursor
		                                    .getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));	
				      contactAndroid = new ContactAndroid();
				      contactAndroid.setContactId(contactId);
				    	 			    				     
				      //vamos a recuperar los datos-telefono,mail etc
				      Uri dataUri = ContactsContract.Data.CONTENT_URI;
				      Cursor dataCursor = context.getContentResolver().query(dataUri, null,
				    		  ContactsContract.Data.CONTACT_ID + "=" + contactId,null, null);
				      
				      String displayName="";
				      String nickName="";
				      String homePhone="";
				      String mobilePhone="";
				      String workPhone="";
				      String homeEmail="";
				      String workEmail="";
				      String companyName="";
				      String title = "";
				      while (dataCursor!=null && dataCursor.moveToNext()) 
					  {			    	 			    	  			    
				    	  //DISPLAYNAME
				    	  displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
				    	  contactAndroid.setName(displayName);
				    	  
				    	  //nickname
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
				    	  {
				    		  nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		  contactAndroid.setNickName(nickName);
				    	  }
				    	  
				    	  //telefonos
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
				    		     {
				    		    	  homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	  contactAndroid.setPhoneHome(homePhone);
				    		    	  break;
				    		     }
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
				    		     {
				    		    	  mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  contactAndroid.setPhoneMobile(mobilePhone);
				    		    	  break;
				    		     }
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
				    		     {			    		    	 			    		     
				    		    	  workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  contactAndroid.setPhoneWork(workPhone);
				    		    	  break;
				    		     }
				    		   } 			    		  
				    	   }
				    	  
				    	  //mails
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) 
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
				    		    {
				    		    	homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	contactAndroid.setEmailHome(homeEmail);
				    		    	break;
				    		    }
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
				    		    {
				    		    	workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	contactAndroid.setEmailWork(workEmail);
				    		    	break;
				    		    }
				    		  }                           
				    	  }
				    	  
				    	  //COMPANIA
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		  title = dataCursor.getString(dataCursor.getColumnIndex("data4"));    
				    		  contactAndroid.setCompany(companyName);
				    		  contactAndroid.setTitleCompany(title);			    		  
				    	  } 
					  }	
					      if(contactAndroid!=null)
					      {
					       listacontacts.add(contactAndroid);
					      }
				    }
			    }

			    return listacontacts;
			  }
		 
		 /**
		  * Obtener todos los contactos de un determinado grupo en un mapa cuya key sera el id del contacto
		  * que es unico
		  * @param groupID
		  */
		 public static Map<String,ContactAndroid> getMapContactsFromGroup(Context context,HandlerData handlerData,long groupID) {
			    
			 
			   Map<String,ContactAndroid> mapContacts = new HashMap<String, ContactAndroid>();
			   ContactAndroid  contactAndroid = null;
			   String contactId = null;
			   		
			    //uri llamada al servicio contactos
			   Uri groupURI = ContactsContract.Data.CONTENT_URI;

			    
			    //campos recuperamos
			    String[] projection = new String[] { 		    		
			    		ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID		    		
			    };
			   		   
			    //filtros de la query lanzamos
			    String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+ " = ?";
			    String[] selectionArgs = new String[]{String.valueOf(groupID)};
			    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";

			    //obtener los resultados
			    Cursor cursor= context.getContentResolver().query(groupURI, projection, selection, selectionArgs, sortOrder);
			    		 
			    if(cursor !=null)
			    {
				    while (cursor.moveToNext()) 
				    {
		
				    	 
				      contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));				     
				      contactAndroid = new ContactAndroid();
				      contactAndroid.setContactId(contactId);
				    	 			    				     
				      //vamos a recuperar los datos-telefono,mail etc
				      Uri dataUri = ContactsContract.Data.CONTENT_URI;
				      Cursor dataCursor = context.getContentResolver().query(dataUri, null,
				    		  ContactsContract.Data.CONTACT_ID + "=" + contactId,null, null);
				      
				      String displayName="";
				      String nickName="";
				      String homePhone="";
				      String mobilePhone="";
				      String workPhone="";
				      String homeEmail="";
				      String workEmail="";
				      String companyName="";
				      String title = "";
				      while (dataCursor!=null && dataCursor.moveToNext()) 
					  {			    	 			    	  			    
				    	  //DISPLAYNAME
				    	  displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
				    	  contactAndroid.setName(displayName);
				    	  
				    	  //nickname
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
				    	  {
				    		  nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		  contactAndroid.setNickName(nickName);
				    	  }
				    	  
				    	  //telefonos
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
				    		     {
				    		    	  homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	  contactAndroid.setPhoneHome(homePhone);
				    		    	  break;
				    		     }
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
				    		     {
				    		    	  mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  contactAndroid.setPhoneMobile(mobilePhone);
				    		    	  break;
				    		     }
				    		     case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
				    		     {			    		    	 			    		     
				    		    	  workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	  contactAndroid.setPhoneWork(workPhone);
				    		    	  break;
				    		     }
				    		   } 			    		  
				    	   }
				    	  
				    	  //mails
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) 
				    	  {                                
				    		  switch(dataCursor.getInt(dataCursor.getColumnIndex("data2")))
				    		  {                                    
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
				    		    {
				    		    	homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1")); 
				    		    	contactAndroid.setEmailHome(homeEmail);
				    		    	break;
				    		    }
				    		    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
				    		    {
				    		    	workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		    	contactAndroid.setEmailWork(workEmail);
				    		    	break;
				    		    }
				    		  }                           
				    	  }
				    	  
				    	  //COMPANIA
				    	  if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
				    	  {                                
				    		  companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				    		  title = dataCursor.getString(dataCursor.getColumnIndex("data4"));    
				    		  contactAndroid.setCompany(companyName);
				    		  contactAndroid.setTitleCompany(title);			    		  
				    	  } 
					  }	
					      if(contactAndroid!=null)
					      {
					    	  mapContacts.put(contactAndroid.getContactId().trim(), contactAndroid);				     
					      }
				    }
			    }

			  
			    return mapContacts;
			  }		
		 
}
