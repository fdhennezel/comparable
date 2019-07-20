/*
 * Created on 3 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.woshe;
import java.io.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheDayActivityWorkload {
	
	private int [] _activityWorkload;
	private int [] _activityRemainingWorkload;
	
	private long _dayStartRemaining, _lunchStartRemaining, _lunchEndRemaining, _dayEndRemaining;
	
	private int _day;
	
	final int daySize = 96;
	
	private String _skill;
	
	public FheDayActivityWorkload(int day,String skill)
	{
		int i;

		_skill = skill;
		_day = day;
		_activityWorkload = new int[daySize];
		_activityRemainingWorkload = new int[daySize];
		
	}

	public long getDayCost(int day, FhePerson person)
	{
		
		int ii;
		long i;
		long dayStart,lunchStart,lunchEnd,dayEnd;
		long cost=0;
		
		long workload [] = new long[daySize];
		
		for(ii=0;ii<daySize;ii++)
		{
			workload[ii]=_activityRemainingWorkload[ii];
		}

		dayStart = person.getDayStartVar(day).getFirst();
		lunchStart = person.getLunchStartVar(day).getFirst();
		lunchEnd= person.getLunchEndVar(day).getFirst();
		dayEnd = person.getDayEndVar(day).getFirst();
		
		for(i=dayStart;i<lunchStart;i++)
		{
			ii = (int) i;
			workload[ii] -=1;
		}
		
		for(i=lunchEnd;i<dayEnd;i++)
		{
			ii = (int) i;
			workload[ii] -=1;
		}

		for(ii=0;ii<daySize;ii++)
		{
			if(workload[ii]>0)
				cost +=workload[ii];
			else
				cost -=workload[ii];
		}

		return cost;

	}
	
	public void setRemainingActivityWorkload(int day, FheTeamWork teamWork)
	{
		
		// valid method with one skill per person
		int i,ii;
		long j;
		FhePerson person;
		
		for (i=0;i<daySize;i++)
		{
			_activityRemainingWorkload[i] = _activityWorkload[i];
		}
		
		for(i=0;i<teamWork.size() ;i++)
		{
			person = (FhePerson) teamWork.get(i);
			if(person.hasSkill(_skill)==true)
			{
				for(j=person.getDayStart(day);j<person.getLunchStart(day);j++)
				{
					ii = (int) j;
					_activityRemainingWorkload[ii] -=1;
				}
				for(j=person.getLunchEnd(day);j<person.getDayEnd(day);j++)
				{
					ii = (int) j;
					_activityRemainingWorkload[ii] -=1;
				}
			}
		}
		
		_dayStartRemaining = -1;
		_lunchStartRemaining = -1;
		_lunchEndRemaining = -1;
		_dayEndRemaining = -1;
		
		for (i=0;i<daySize;i++)
		{
			if(_dayStartRemaining == -1)
			{
				if(_activityRemainingWorkload[i] > 0)
					_dayStartRemaining = i;
			}
			else
			{
				if(_lunchStartRemaining ==-1)
				{
					if(_activityRemainingWorkload[i]<=0)
						_lunchStartRemaining = i;
				}
				else
				{
					if(_lunchEndRemaining ==-1)
					{
						if(_activityRemainingWorkload[i]>0)
							_lunchEndRemaining = i;
					}
					else
					{
						if(_activityRemainingWorkload[i]>0)
						{
							_dayEndRemaining = i;
						}						
					}
				}
			}
		}
		
	}

	public void printRemainingActivityWorkload(int day, FileWriter SolutionToFile)
	{
		int i,ii;
		long j;
		
		try
		{

    	    //Write the file.
			SolutionToFile.write(";");
			for (i=0;i<daySize;i++)
			{
 				SolutionToFile.write(_activityRemainingWorkload[i]+";");
			}
            SolutionToFile.write(13);
			
		} catch (IOException e) {
		    System.err.println("Writing error: " + e);
		}
	}

	public void printActivityWorkload(FileWriter SolutionToFile)
	{
		int i,ii;
		long j;
		
		try
		{

    	    //Write the file.
			SolutionToFile.write(";");
			for (i=0;i<daySize;i++)
			{
 				SolutionToFile.write(_activityWorkload[i]+";");
			}
            SolutionToFile.write(13);
			
		} catch (IOException e) {
		    System.err.println("Writing error: " + e);
		}
	}

	public void printActivityWork(FileWriter SolutionToFile)
	{
		int i,ii;
		long j;
		
		try
		{

    	    //Write the file.
			SolutionToFile.write(";");
			for (i=0;i<daySize;i++)
			{
 				SolutionToFile.write((_activityWorkload[i]-_activityRemainingWorkload[i])+";");
			}
            SolutionToFile.write(13);
			
		} catch (IOException e) {
		    System.err.println("Writing error: " + e);
		}
	}

	public void setRemainingActivityWorkload(int day, FhePerson person)
	{
		
		// valid method with one skill per person
		int i,i1,i2;
		
		if(person.getDayStartAvailability(day)>-1)
		{
			if(person.getLunchEndAvailability( day) == -1)
			{
				//continuous availability
				person.setLunchEndWorkload( day,-1);
				person.setDayEndWorkload( day,-1);

				i1=(int)person.getDayStartAvailability(day);
				i2=(int)person.getLunchStartAvailability(day);
				for (i=i1;i<=i2;i++)
				{
					if(person.getDayStartWorkload(day) == -1)
					{
						if(_activityRemainingWorkload[i] > 0)
							person.setDayStartWorkload(day,i);
					}
					else
					{
						if(person.getLunchStartWorkload(day) ==-1)
						{
							if(_activityRemainingWorkload[i]<=0 || i == (int)person.getLunchStartAvailability(day))
								person.setLunchStartWorkload(day,i);
						}
						else
						{
							if(person.getLunchEndWorkload(day)==-1)
							{
								if(_activityRemainingWorkload[i]>0)
									person.setLunchEndWorkload(day,i);
							}
							else
							{
								if(person.getDayEndWorkload( day)==-1)
								{
									if(_activityRemainingWorkload[i]<=0 || i == (int)person.getDayEndAvailability(day))
										person.setDayEndWorkload(day,i);
								}
								else
								{
									if(_activityRemainingWorkload[i]>0)
										person.setDayEndWorkload(day,-1);
								}
							}
						}
					}
				}
			}
			else
			{
				//split availability
				i1=(int)person.getDayStartAvailability(day);
				i2=(int)person.getDayEndAvailability(day);
				for (i=i1;i<=i2;i++)
				{
					if(person.getDayStartWorkload(day) == -1)
					{
						if(_activityRemainingWorkload[i] > 0)
							person.setDayStartWorkload(day,i);
					}
					else
					{
						if(person.getLunchStartWorkload(day) ==-1)
						{
							if(_activityRemainingWorkload[i]<=0 || i == (int)person.getLunchStartAvailability(day))
								person.setLunchStartWorkload(day,i);
						}
						else
						{
							if(person.getLunchEndWorkload(day)==-1)
							{
								if(_activityRemainingWorkload[i]>0 && i >= (int)person.getLunchEndAvailability(day))
									person.setLunchEndWorkload(day,i);
							}
							else
							{
								if(person.getDayEndWorkload( day)==-1)
								{
									if(_activityRemainingWorkload[i]<=0 || i == (int)person.getDayEndAvailability(day))
										person.setDayEndWorkload(day,i);
								}
								else
								{
									if(_activityRemainingWorkload[i]>0 && i < (int)person.getDayEndAvailability(day))
										person.setDayEndWorkload(day,-1);
								}
							}
						}
					}
				}
			}
		}
		
//		System.out.println(day+" day start workload "+person.getDayStartWorkload(day));
//		System.out.println(day+" lunch start workload "+person.getLunchStartWorkload(day));
//		System.out.println(day+" lunch end workload "+person.getLunchEndWorkload(day));
//		System.out.println(day+" day end workload "+person.getDayEndWorkload(day));

	}
	
	public int [] getActivityWorkload()
	{
		return _activityWorkload;
	}
		
}
