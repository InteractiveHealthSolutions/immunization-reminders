package org.ird.immuremsys.constants;

public class ErrMsg
{
	public static final String	USERNAME_MISSING							= "You must enter the Username";
	public static final String	PASSWORD_MISSING							= "You must enter the Password";
	
	public static final String	ID_MISMATCH									= "Child id's must match!";
	public static final String	ID_MISSING									= "You must enter the Child ID";
	public static final String	MR_MISMATCH									= "MR numbers must match!";
	public static final String	MR_MISSING									= "You must enter the MR Number";
	public static final String	LOGIN_ERROR									= "Error during login, Please try again";
	public static final String	UNKNOWN_ERROR								= "Error making request no response returned, Please try again. If problem persists contact System Administrator";
	public static final String	INVALID_VACCINE								= "Selected vaccine is currently not in study. Contact Program Vendor to include it in study.";
	public static final String	VACCINE_CURRENTLY_GIVEN						= "Selected vaccine have been given currently. Check if you have selected correct vaccine name or ignore the message.";
	public static final String	VACCINE_GIVEN								= "Selected vaccine have been given. Check if you have selected correct vaccine name or ignore the message.";
	public static final String	VACCINE_MISSING								= "Please specify vaccine given today and assigned for next vaccination or choose system calculated";
	public static final String	REASON_NOT_VACCINATED						= "Please specify reason why vaccine was not given or late vaccinated.";
	public static final String	DATE_IN_FUTURE								= "Specified date is in future. Please specify date correctly.";
	public static final String	NEXT_VACCINATION_DATE_INVALID				= "Next vaccination date should be atleast 1 month (for measles2 15 months after birthdate or for child not vaccinated 1 day) after current vaccination date. Please specify date correctly.";
	public static final String	NEXT_VACCINATION_SUNDAY						= "Next vaccination date cannot fall on Sunday. Moved date one day after i.e. Monday.";
	public static final String	LAST_VACCINATION_SELECTED					= "Be very careful if choosing this option. Selecting Yes will mark child as Completed vaccinations and disable all further followups for the child.";
	public static final String	CURR_VACC_DATE								= "Current vaccination date should be after or maximum of 3 days before vaccination due date.";

	}
