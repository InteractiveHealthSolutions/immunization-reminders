package org.ird.immunizationreminder.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.service.exception.UserServiceException;

public class UserSessionUtils {
	private static void clearSession( HttpServletRequest req ) {
		try {
			Context.logout( getUsername( req ).toString() );
		}
		catch ( Exception e ) {
		}
		
		try {
			req.getSession().removeAttribute( "username" );
		}
		catch ( Exception e ) {
		}
		
		req.getSession().invalidate();
	}

	public static LoggedInUser getActiveUser( HttpServletRequest req ) {
		LoggedInUser user = null;
		try {
			String username = (String) getUsername( req );
			Context.keepUserAlive( username );
			user = Context.getUser( username );
			req.setAttribute( "user" , user );
			req.getSession().setAttribute( "username" , username );
			req.getSession().setAttribute( "fullname" , user.getUser().getFullName() );
		}
		catch ( Exception e ) {
			clearSession( req );
			req.getSession().setAttribute( "logmessage" , UserServiceException.SESSION_EXPIRED );
		}
		return user;
	}

	public static boolean hasActiveUserPermission( String permission ,
			HttpServletRequest request ) throws UserServiceException {
		boolean perm = false;
		try {
			perm = getActiveUser( request ).hasPermission( permission );
		}
		catch ( NullPointerException e ) {// throw null pointer exception only
											// if user is null means not active
			throw new UserServiceException( UserServiceException.SESSION_EXPIRED );
		}
		return perm;
	}

	public static boolean isUserSessionActive( HttpServletRequest req ) {
		try {
			String username = (String) getUsername( req );
			Context.keepUserAlive( username );
			return true;
		}
		catch ( Exception e ) {
			clearSession( req );
			req.getSession().setAttribute( "logmessage" , UserServiceException.SESSION_EXPIRED );
		}
		return false;
	}

	private static Object getUsername( HttpServletRequest req ) {
		Object uname = req.getSession().getAttribute( "username" );
		if (uname == null) {
			for ( Cookie c : req.getCookies() ) {
				if (c.getName().compareTo( "username" ) == 0) {
					uname = c.getValue();
					break;
				}
			}
		}
		return uname;
	}

	public static Cookie createCookie( String name , String value , int age ) {
		Cookie cok = new Cookie( name , value );
		cok.setMaxAge( age );
		return cok;
	}

	public static void removeCookie( String name , HttpServletRequest req ,
			HttpServletResponse resp ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				c.setMaxAge( -1 );
				resp.addCookie( c );
				break;
			}
		}
	}

	public static Cookie getCookie( String name , HttpServletRequest req ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				return c;
			}
		}
		return null;
	}
}
