/*
 * Created on 3 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.woshe;
import com.fhe.cp.*;
import java.util.*;
import java.io.*;


/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheBusinessWorkload {

	private ArrayList [] _activityWorkload;
	private FheSkills _allSkills;
	FheManager _m;
    final int nbDays = 7;
	
	FheBusinessWorkload(FheManager m)
	{
		_m = m;
		_m.addBusiness( this);
		try{
			char c;
			int i1,i2;

			FileReader file = new FileReader("Business.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str,fields[];
			_allSkills = new FheSkills();
			
			while ((str = in.readLine()) != null)
	        {
	            _allSkills.add(str.split(";")[0]);
	            str = in.readLine();
	            str = in.readLine();
	            str = in.readLine();
	            str = in.readLine();
	            str = in.readLine();
	            str = in.readLine();
	            str = in.readLine();
	        }

			in.close();
	        file.close();

	        _activityWorkload = new ArrayList[_allSkills.size()];
	        
	        file = new FileReader("Business.csv");
	        in = new BufferedReader(file);
	        int i,j,k;
	        i=0;
	        j=0;
	        int[]workload;
	        Integer workloaValue;
	        
    		String skill=null;
    		
	        while ((str = in.readLine()) != null)
	        {	            
	            if(j==0)
	            {
	            	j++;
	        		skill = (String)_allSkills.get(i);
	        		_activityWorkload[i] = new ArrayList(nbDays);
	            }
	            else
	            {
	        		_activityWorkload[i].add(new FheDayActivityWorkload(j-1,skill));
	        		FheDayActivityWorkload ww = (FheDayActivityWorkload) _activityWorkload[i].get(j-1);
	            	workload= ((FheDayActivityWorkload) _activityWorkload[i].get(j-1)).getActivityWorkload();

	            	fields = str.split(";");
	            	
	            	if(fields.length < 96)
	            		System.out.println("mauvaise charge");
	            		            		
	            	for(k=0;k<fields.length ;k++)
	            		workload[k]=(Integer.valueOf( fields[k])).intValue();
	        		j++;
        			k=0;
        			if(j==8)
	        		{
	        			j=0;
	        			i++;
	        		}
	            }  
	        }
	        in.close();
	        file.close();

	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
    }
	
	public void setRemainingWorkload(FheTeamWork team)
	{
		int i,j;

		for(i=0;i<_allSkills.size();i++)
		{
			for(j=0;j<nbDays;j++)
			{
				FheDayActivityWorkload dayActivity = (FheDayActivityWorkload) _activityWorkload[i].get(j);
				dayActivity.setRemainingActivityWorkload(j,team);
			}
		}
	}
	
	public long getCost(FhePerson person)
	{
		int i,j;
		
		long cost = 0;

		for(i=0;i<_allSkills.size();i++)
		{
			for(j=0;j<nbDays;j++)
			{
				FheDayActivityWorkload dayActivity = (FheDayActivityWorkload) _activityWorkload[i].get(j);
				cost+=dayActivity.getDayCost(j,person);
			}
		}
		return cost;
	}

	public void setRemainingWorkload(FhePerson person)
	{
		int i,j;

		for(i=0;i<_allSkills.size();i++)
		{
			String skill = (String)_allSkills.get(i);
			if (person.hasSkill( skill)== true)
			{
				for(j=0;j<nbDays;j++)
				{
					FheDayActivityWorkload dayActivity = (FheDayActivityWorkload) _activityWorkload[i].get(j);
					dayActivity.setRemainingActivityWorkload(j,person);
				}
			}
		}
	}
	
	public void printRemainingWorload(FileWriter SolutionToFile)
	{
		int i,j;

		try
		{
			for(i=0;i<_allSkills.size();i++)
			{
				SolutionToFile.write("Solution for "+_allSkills.get(i));
				SolutionToFile.write(13);
				for(j=0;j<nbDays;j++)
				{
					FheDayActivityWorkload dayActivity = (FheDayActivityWorkload) _activityWorkload[i].get(j);
					dayActivity.printRemainingActivityWorkload(i,SolutionToFile);
				}
			}
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
	}
	
	public void printWorkloadCoverage(FileWriter SolutionToFile, int day)
	{
		int i;

		try
		{
			for(i=0;i<_allSkills.size();i++)
			{
				SolutionToFile.write("Solution for "+_allSkills.get(i));
				SolutionToFile.write(13);
				FheDayActivityWorkload dayActivity = (FheDayActivityWorkload) _activityWorkload[i].get(day);
				dayActivity.printActivityWorkload(SolutionToFile);
				dayActivity.printActivityWork(SolutionToFile);
			}
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
	}
}