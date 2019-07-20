/*
 * Created on 22 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import com.fhe.cp.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhRessource {
	private String _ressourceName;
	private FdhProfiles _ressourceProfiles;
	private FheManager _m;
	private FheIntVar _startAvailability;
	private long _horizon;
	
	
	public FdhRessource(FheManager m,String name, long horizon)
	{
		_m=m;
		_ressourceName = name;
		_ressourceProfiles = new FdhProfiles();
		_horizon = horizon;
		
		_startAvailability = new FheIntVar(_m,1,horizon,_ressourceName);
		_startAvailability.setChoice(100);
		_startAvailability.setRessource(this);
	}
	
	public boolean removeUnavailabilyty(long startUnavailable, long endUnavailable)
	{
		return _startAvailability.removeRessourceAvailability(startUnavailable, endUnavailable);
	}
	
	public long usedRessource(long startUnavailable, long duration)
	{
		return _startAvailability.removeUsedRessource(startUnavailable, duration);
	}
	
	public void addProfile(String profile)
	{
		FdhProfile Profile = new FdhProfile(profile);
		_ressourceProfiles.add(Profile);
	}
	
	public int IsProfile(FdhProfile profile)
	{
		int i;
		
		for (i=0;i<_ressourceProfiles.size();i++)
		{
			FdhProfile theProfile = (FdhProfile) _ressourceProfiles.get(i);
			if (theProfile.sameProfile(profile))
				return i;
		}
		return -1;
	}
	
	public long getStartAvailability()
	{
		return _startAvailability.getFirst();
	}
	
	public boolean isAvailableRessource(long day)
	{
		if(_startAvailability.searchIndex(day) >= 0)
			return true;
		else
			return false;
	}
	
	public boolean removeRessourceAvailability(long val)
	{
		_m.addPropagatedExp(_startAvailability);
		return _startAvailability.removeRessourceVal(val);
	}
	
	public String getName()
	{
		return _ressourceName;
	}
}
