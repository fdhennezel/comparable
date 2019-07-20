/*
 * Created on 21 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhProfile {

	private String _profileName;
	
	public FdhProfile(String name)
	{
		_profileName = name;		
	}

	public String getProfile()
	{
		return _profileName;
	}
	
	public boolean sameProfile(FdhProfile profile)
	{
		int e = _profileName.compareTo(profile.getProfile());
		
		if (e==0)
			return true;
		else
			return false;
	}
}
