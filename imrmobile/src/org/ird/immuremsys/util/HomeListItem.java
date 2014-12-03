package org.ird.immuremsys.util;

import org.ird.immuremsys.constants.FormType;

public class HomeListItem {
	public static final int				MAX_ITEMS				= 1;

	public static final HomeListItem	UPDATE_VACCINATION		= new HomeListItem (0, "Update Vaccination", FormType.UPDATE_VACCINATION, "Update Vaccination", true);
	//public static final HomeListItem	MISSING_CXR_RESULT		= new HomeListItem (1, "Pending CXR Result Form", FormType.Form_MISSING_CXR_RESULT, "Patients with pending Xray results", true);
	
	public final int					INDEX;
	public final String					NAME;
	final boolean						show;
	final String						formType;
	final String						formDesc;

	public String getFormDesc ()
	{
		return formDesc;
	}

	public String getFormType ()
	{
		return formType;
	}

	public HomeListItem (int index, String displayName, String formType, String formDesc, boolean show)
	{
		this.INDEX = index;
		this.formType = formType;
		this.formDesc = formDesc;
		this.NAME = displayName;
		this.show = show;
	}

	public boolean isShown ()
	{
		return show;
	}

	/*
	 * public void setShown(boolean show) { this.show = show; }
	 */
}
