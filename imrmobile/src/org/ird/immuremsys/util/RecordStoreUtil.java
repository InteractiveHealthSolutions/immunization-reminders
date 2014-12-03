package org.ird.immuremsys.util;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;

public class RecordStoreUtil {
	
	private RecordStoreUtil() {
		openRecStore();
	}
	
	public static RecordStoreUtil getInstance(){
		if(rutil == null){
			rutil = new RecordStoreUtil();
		}
		return rutil;
	}
	private static RecordStoreUtil rutil;
	
	private RecordStore rs = null;
	static final String REC_STORE = "IMRRMS";

	public void openRecStore() {
		try {
			rs = RecordStore.openRecordStore(REC_STORE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearRecordStore(){
		closeRecStore();
		deleteRecStore();
		openRecStore();
	}
	public void closeRecStore() {
		try {
			rs.closeRecordStore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteRecStore() {
		if (RecordStore.listRecordStores() != null) {
			try {
				RecordStore.deleteRecordStore(REC_STORE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean writeRecord(String str) {
		byte[] rec = str.getBytes();
		try {
			rs.addRecord(rec, 0, rec.length);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean writeRecord(String key, String value) {
		byte[] rec = (key+":"+value).getBytes();
		try {
			rs.addRecord(rec, 0, rec.length);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public String readRecords() {
		String str = null;
		try {
			byte[] recData = new byte[5];
			int len;

			for (int i = 1; i <= rs.getNumRecords(); i++) {
				if (rs.getRecordSize(i) > recData.length) {
					recData = new byte[rs.getRecordSize(i)];
				}
				len = rs.getRecord(i, recData, 0);
				str = new String(recData, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	public String searchRecordStore(String searchText)
	{
		String str = "";
	     try
	     {
	       // Record store is not empty
	       if (rs.getNumRecords() > 0)
	       {
	         // Setup the search filter with the user requested text
	         SearchFilter search = new SearchFilter(searchText);

	         RecordEnumeration re = rs.enumerateRecords(search, null, false);

	         // A match was found using the filter
	         if (re.numRecords() > 0)
	         {
	           // Append all records found onto the form
	           while (re.hasNextElement())
	             str += new String(re.nextRecord());
	         }

	         re.destroy();   // Release enumerator
	       }
	     }
	     catch (Exception e)
	     {
	    	 e.printStackTrace();
	     }
		return str;
	}

	public String searchRecordStoreByKey(String key) {
		String str = "";
		try {
			// Record store is not empty
			if (rs.getNumRecords() > 0) {
				// Setup the search filter with the user requested text
				SearchFilter search = new SearchFilter(key);

				RecordEnumeration re = rs.enumerateRecords(search, null, false);

				// A match was found using the filter
				if (re.numRecords() > 0) {
					// Append all records found onto the form
					while (re.hasNextElement())
						str += new String(re.nextRecord());
				}

				str = str.substring(str.toLowerCase().indexOf(":")+1);
				re.destroy(); // Release enumerator
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	//********************************************************
	// Called for each record when creating the enumerator.
	// Checks to see if the record contains text that
	// matches the text string entered by the user.
	//********************************************************
	class SearchFilter implements RecordFilter
	{
	     private String searchText = null;

	     public SearchFilter(String searchText)
	     {
	       // Text to find
	       this.searchText = searchText.toLowerCase();
	     }

	     public boolean matches(byte[] candidate)
	     {
	       String str = new String(candidate).toLowerCase();

	       // Look for text
	       if (searchText != null && str.indexOf(searchText) != -1)
	         return true;
	       else
	         return false;
	     }
	}
}