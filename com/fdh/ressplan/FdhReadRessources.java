/*
 * Created on 22 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.fhe.cp.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhReadRessources {
	private FheManager _m;
	
	private FdhRessources _ressources;
	private long _horizon;
	
		
	public FdhReadRessources(FheManager m, long horizon)
	{
		long value;
		FdhRessource _ressource;
		_m = m;
		_horizon = horizon;
		_m.addTheRessources(this);

		try{
			char c;
			int j,k;

			FileReader file = new FileReader("Ressources.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str,fields[],previous;
			_ressources = new FdhRessources();
				
			in.close();
	        file.close();

	        file = new FileReader("Ressources.csv");
	        in = new BufferedReader(file);

			previous = "";
			_ressource = null;
			while ((str = in.readLine()) != null)
			{
				int test = previous.compareTo(str.split(";")[0]);
				if (previous.compareTo(str.split(";")[0]) == 0)
				{
// Describles the unavailability of previous ressource
					_ressource.removeUnavailabilyty((Long.valueOf(str.split(";")[1])).longValue(),(Long.valueOf(str.split(";")[2])).longValue());
				}
				else
				{
					_ressource = new FdhRessource(_m,str.split(";")[0], horizon);
					previous = _ressource.getName();
					if(previous.compareTo("") == 1)
					{
						System.out.println("Une ressource doit avoir un nom");
						previous = str.split(";")[-1];
					}
					for(j=1;j<str.split(";").length;j++)
					{
						_ressource.addProfile(str.split(";")[j]);
					}
					_ressources.add(_ressource);
				}
	        }   
			in.close();
	        file.close();


	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
    }
	
	public long getSize()
	{
		return _ressources.size();
	}
	
	public FdhRessource getRessource(int varValue)
	{
		return (FdhRessource) _ressources.get(varValue);
	}
	
	public int isPossibleRessource(int i,FdhProfile profile)
	{
		FdhRessource theRessource = (FdhRessource) _ressources.get(i);
		return theRessource.IsProfile(profile);
	}
	 
	public String getName(long index)
	{
		FdhRessource ressource = (FdhRessource) _ressources.get((int)index-1);
		return ressource.getName();
	}

	public boolean updateASAPRessource(FdhTask task)
	{
		int ii;
		long i,j;
		
		FheIntVar possibleRessource = task.getRessource();
		
		long index = ((Long) possibleRessource.get(0)).longValue();
		ii = (int) index;
		FdhRessource ressource = (FdhRessource) _ressources.get(ii-1);
		if(ressource.removeRessourceAvailability(task.getStartDate().getFirst()))
			return true;
		j = ressource.getStartAvailability();
		i = ressource.usedRessource(j, task.getTaskDuration());
		if (i == -1)
			return true;
		_m.addPropagatedExp(task.getStartDate());
		if(task.getStartDate().setLowestActuallyGreaterThan(j))
			return true;
		_m.addPropagatedExp(task.getEndDate());
		if(task.getEndDate().setLowestActuallyGreaterThan(i))
			return true;

		return false;
	}

	public long ressourceChoiceMinStart(FdhTask task)
	{
		int ii,i;
		long min = _horizon;
		long indexmin = -1;
		boolean end=false;
		
		i=0;
		FheIntVar possibleRessource = task.getRessource();
		
		while(i<possibleRessource.size())
		{
			long index = ((Long) possibleRessource.get(i)).longValue();
			ii = (int) index;
			FdhRessource ressource = (FdhRessource) _ressources.get(ii-1);
			if(min > ressource.getStartAvailability())
			{
				min = ressource.getStartAvailability();
				indexmin = index; 
			}
			i++;
		}
		
		return indexmin;
	}

}
