/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.woshe;

import com.fhe.cp.*;

import java.io.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FhePerson {
//durations are in 15 minutes
	private long _minDuration,_maxDuration,_minDays,_maxDays;
	
	private long _minShiftDuration, _maxShiftDuration;
	
	private long _minLunchDuration, _maxLunchDuration;

	private long _mindayOneShiftDuration, _maxDayOneShiftDuration;
	private long _mindayTwoShiftsDuration, _maxDayTwoShiftsDuration;
	private long _maxDayTotalDuration,_minRestBetweenDays;
	
	private long _maxDayTwoShifts,_maxDayLate;
	
	private String _employeeId;

	private long [] _dayStartAvailability;
	private long [] _lunchStartAvailability;
	private long [] _lunchEndAvailability;
	private long [] _dayEndAvailability;

	private long [] _dayStartSolution;
	private long [] _lunchStartSolution;
	private long [] _lunchEndSolution;
	private long [] _dayEndSolution;
	
	private boolean _isScheduled;

	// the following variables correspond to the 15 mn step of the day, 1 to 96.
	private FheIntVar [] _dayStart;
	private FheIntVar [] _lunchStart;
	private FheIntVar [] _lunchEnd;
	private FheIntVar [] _dayEnd;
	
	private FheIntVar [] _workDuration;
	
    final int nbDays = 7;
	final int daySize = 96;
	
	private FheIntVar [] _dayWLunch;
	private FheIntVar [] _dayWOLunchMorning;
	private FheIntVar [] _dayWOLunchAfternoon;
	private FheIntVar [] _dayRest;
	
	private FheIntVar [] _pole;
	
	private FheSkills _skills;
	
	private long [] _dayStartWorkload;
	private long [] _lunchStartWorkload;
	private long [] _lunchEndWorkload;
	private long [] _dayEndWorkload;
	
	private boolean [] _searchStartFirst;
	private long [] _dayOrderSearch;
	private long [] _absoluteMaxDayDuration;
	
	private long _solutionCost;

	FhePerson(BufferedReader in, String employeeId)
	{
		int i,k;
		String fields[];
		
		_dayStartAvailability   = new long[nbDays];
		_lunchStartAvailability = new long[nbDays];
		_lunchEndAvailability   = new long[nbDays];
		_dayEndAvailability     = new long[nbDays];
		
		_dayStartSolution   = new long[nbDays];
		_lunchStartSolution = new long[nbDays];
		_lunchEndSolution   = new long[nbDays];
		_dayEndSolution     = new long[nbDays];
		
		_dayStartWorkload   = new long[nbDays];
		_lunchStartWorkload = new long[nbDays];
		_lunchEndWorkload   = new long[nbDays];
		_dayEndWorkload     = new long[nbDays];
	
		_searchStartFirst   = new boolean[nbDays];
		_dayOrderSearch		= new long[nbDays];
		_absoluteMaxDayDuration = new long[nbDays];

		for (i=0;i<nbDays;i++)
		{
			_dayStartSolution[i] = -1;
			_lunchStartSolution[i] = -1;
			_lunchEndSolution[i] = -1;
			_dayEndSolution[i] = -1;

			_dayStartWorkload[i] = -1;
			_lunchStartWorkload[i] = -1;
			_lunchEndWorkload[i] = -1;
			_dayEndWorkload[i] = -1;
			
			_dayOrderSearch[i] = 1;
		}
		
		_skills = new FheSkills();
		
		try
		{
			String str;
	        _employeeId = employeeId.split(";")[0];
			
	        str = in.readLine();
			_minDuration =(Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_minDays	 = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDays     = (Long.valueOf(str.split(";")[0])).longValue();
							
	        str = in.readLine();
			_minShiftDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxShiftDuration = (Long.valueOf(str.split(";")[0])).longValue();

	        str = in.readLine();
			_mindayOneShiftDuration  = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDayOneShiftDuration  = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _mindayTwoShiftsDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _maxDayTwoShiftsDuration = (Long.valueOf(str.split(";")[0])).longValue();
		    
	        str = in.readLine();
		    _minLunchDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _maxLunchDuration = (Long.valueOf(str.split(";")[0])).longValue();
		    
	        str = in.readLine();
			_maxDayTotalDuration     = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_minRestBetweenDays		 = (Long.valueOf(str.split(";")[0])).longValue();
			
	        str = in.readLine();
			_maxDayTwoShifts = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDayLate      = (Long.valueOf(str.split(";")[0])).longValue();
			
			for(i=0;i<nbDays;i++)
			{
		        str = in.readLine();
				_dayStartAvailability[i]   = (Long.valueOf(str.split(";")[0])).longValue();
				_lunchStartAvailability[i] = (Long.valueOf(str.split(";")[1])).longValue();
				_lunchEndAvailability[i]   = (Long.valueOf(str.split(";")[2])).longValue();
				_dayEndAvailability[i]     = (Long.valueOf(str.split(";")[3])).longValue();
				
				_absoluteMaxDayDuration[i] = _dayEndAvailability[i]+_lunchStartAvailability[i]
							-_lunchEndAvailability[i]-_dayStartAvailability[i];
				if(_absoluteMaxDayDuration[i]>_maxDayTwoShifts)
					_absoluteMaxDayDuration[i] = _maxDayTwoShifts;
			}
			
	        str = in.readLine();
        	fields = str.split(";");
        	
        	for(k=0;k<fields.length ;k++)
        		_skills.add(fields[k]);
			
	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
	}
	
	public boolean getSearchStartFirst(int day)
	{
		return _searchStartFirst[day];
	}
	public long getDayOrderSearch(int day)
	{
		return _dayOrderSearch[day];
	}
	
	public long getSolutionCost()
	{
		return _solutionCost;
	}
	
	public void setSolutionCost(long cost)
	{
		_solutionCost = cost;
	}
	
	public void printDurations()
	{
		int i;
		for (i=0;i<nbDays;i++)
		{
			System.out.println("Duration day "+i);
			_workDuration[i].printBounds();
		}
		
	}
	
	public void printSchedule(FileWriter SolutionToFile)
	{
			int i,j;
		int workduration=0;
		String [] toPrint;
		
		try
		{
			for (i=0;i<nbDays;i++)
			{
				toPrint = new String[daySize];
				j=0;
				while(j < daySize)
				{
					toPrint[j] = new String();
					j++;
				}		
				j=0;
				while(j < _dayStartSolution[i])
				{
					j++;
				}
				while(j < _lunchStartSolution[i])
				{
					toPrint[j] = toPrint[j].concat("#");
					j++;
					workduration++;
				}
				while(j < _lunchEndSolution[i])
				{
					j++;
				}
				while(j < _dayEndSolution[i])
				{
					toPrint[j] = toPrint[j].concat("#");
					j++;
					workduration++;
				}
				j=0;
				while(j < _dayStartAvailability[i])
				{
					toPrint[j] = toPrint[j].concat("-");
					j++;
				}
				while(j < _lunchStartAvailability[i])
				{
					j++;
				}
				while(j < _lunchEndAvailability[i])
				{
					toPrint[j] = toPrint[j].concat("-");
					j++;
				}
				while(j < _dayEndAvailability[i])
				{
					j++;
				}
				while(j < daySize)
				{
					toPrint[j] = toPrint[j].concat("-");
					j++;
				}
				SolutionToFile.write(";");
				j=0;
				while(j <daySize)
				{
					SolutionToFile.write(toPrint[j]+";");
					j++;
				}
				SolutionToFile.write(13);
			}
			SolutionToFile.write("Week Workduration="+workduration);
			SolutionToFile.write(13);
		} catch (IOException e) {
		System.err.println("Writing error: " + e);}
	}
	
	public void printDaySchedule(FileWriter SolutionToFile, int i)
	{
		int j;
		int workduration=0;
		String [] toPrint;
		
		toPrint = new String[daySize];
		try
		{
			j=0;
			while(j < daySize)
			{
				toPrint[j] = new String();
				j++;
			}		
			j=0;
			while(j < _dayStartSolution[i])
			{
				j++;
			}
			while(j < _lunchStartSolution[i])
			{
				toPrint[j] = toPrint[j].concat("#");
				j++;
				workduration++;
			}
			while(j < _lunchEndSolution[i])
			{
				j++;
			}
			while(j < _dayEndSolution[i])
			{
				toPrint[j] = toPrint[j].concat("#");
				j++;
				workduration++;
			}
			j=0;
			while(j < _dayStartAvailability[i])
			{
				toPrint[j] = toPrint[j].concat("-");
				j++;
			}
			while(j < _lunchStartAvailability[i])
			{
				j++;
			}
			while(j < _lunchEndAvailability[i])
			{
				toPrint[j] = toPrint[j].concat("-");
				j++;
			}
			while(j < _dayEndAvailability[i])
			{
				j++;
			}
			while(j < daySize)
			{
				toPrint[j] = toPrint[j].concat("-");
				j++;
			}
			SolutionToFile.write(_employeeId+";");
			j=0;
			while(j <daySize)
			{
				SolutionToFile.write(toPrint[j]+";");
				j++;
			}
//			SolutionToFile.write(13);
//			SolutionToFile.write("Week Workduration="+workduration);
			SolutionToFile.write(13);
		} catch (IOException e) {
		System.err.println("Writing error: " + e);}
	}
	
	public long getDayStart(int day)
	{
		return _dayStartSolution[day];
	}
	
	public long getLunchStart(int day)
	{
		return _lunchStartSolution[day];
	}
	
	public long getLunchEnd(int day)
	{
		return _lunchEndSolution[day];
	}
	
	public long getDayEnd(int day)
	{
		return _dayEndSolution[day];
	}
	
	public FheIntVar getWorkDuration(int day)
	{
		return _workDuration[day];
	}
	public void loadPersonData()
	{
		int i;
		
		try{
			FileReader file = new FileReader("Employees.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str;

	        str = in.readLine();
	        _employeeId = str.split(";")[0];
			
	        str = in.readLine();
			_minDuration =(Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_minDays	 = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDays     = (Long.valueOf(str.split(";")[0])).longValue();
							
	        str = in.readLine();
			_minShiftDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxShiftDuration = (Long.valueOf(str.split(";")[0])).longValue();

	        str = in.readLine();
			_mindayOneShiftDuration  = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDayOneShiftDuration  = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _mindayTwoShiftsDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _maxDayTwoShiftsDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    
	        str = in.readLine();
		    _minLunchDuration = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
		    _maxLunchDuration = (Long.valueOf(str.split(";")[0])).longValue();
		    
	        str = in.readLine();
			_maxDayTotalDuration     = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_minRestBetweenDays		 = (Long.valueOf(str.split(";")[0])).longValue();
			
	        str = in.readLine();
			_maxDayTwoShifts = (Long.valueOf(str.split(";")[0])).longValue();
	        str = in.readLine();
			_maxDayLate      = (Long.valueOf(str.split(";")[0])).longValue();
			
	        str = in.readLine();
			for(i=0;i<nbDays;i++)
			{
				_dayStartAvailability[i]   = (Long.valueOf(str.split(";")[0])).longValue();
				_lunchStartAvailability[i] = (Long.valueOf(str.split(";")[1])).longValue();
				_lunchEndAvailability[i]   = (Long.valueOf(str.split(";")[2])).longValue();
				_dayEndAvailability[i]     = (Long.valueOf(str.split(";")[3])).longValue();
			}

	        in.close();
	        file.close();

	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
	}
	
	public void saveSolution(FheManager m)
	{
		int i;
		
		for (i=0;i<nbDays;i++)
		{
			_dayStartSolution[i] = ((Long) _dayStart[i].get(0)).longValue();
			_lunchStartSolution[i] = ((Long) _lunchStart[i].get(0)).longValue();
			_lunchEndSolution[i] = ((Long) _lunchEnd[i].get(0)).longValue();
			_dayEndSolution[i] = ((Long) _dayEnd[i].get(0)).longValue();
		}
		for (i=0;i<nbDays;i++)
		{
/*			System.out.println("day start"+_dayStartSolution[i]);
			System.out.println("lunch start"+_lunchStartSolution[i]);
			System.out.println("lunch end"+_lunchEndSolution[i]);
			System.out.println("day end"+_dayEndSolution[i]);
			System.out.println("dayWLunch"+((Long)_dayWLunch[i].get(0)).longValue());
			System.out.println("dayWOLunchMorning"+((Long)_dayWOLunchMorning[i].get(0)).longValue());
			System.out.println("dayWOLunchAfternoon"+((Long)_dayWOLunchAfternoon[i].get(0)).longValue());
*/			if(_dayStart[i].size()> 1)
				System.out.println("BUG SIZE_dayStart");
			if(_lunchStart[i].size()> 1)
				System.out.println("BUG SIZE_lunchStart");
			if(_lunchEnd[i].size()> 1)
				System.out.println("BUG SIZE_lunchEnd");
			if(_dayEnd[i].size()> 1)
				System.out.println("BUG SIZE_dayEnd");
			if(_dayWLunch[i].size() >1)
				System.out.println("BUG SIZE_dayWLunch");
			if(_dayWOLunchMorning[i].size() >1)
				System.out.println("BUG SIZE_dayWOLunchMorning");
			if(_dayWOLunchMorning[i].size() >1)
				System.out.println("BUG SIZE_dayWOLunchMorning");
		}
		for (i=0;i<nbDays;i++)
		{
			if(((Long)_dayWLunch[i].get(0)).longValue() == 1)
			{
				_dayStartSolution[i] = ((Long) _dayStart[i].get(0)).longValue();
				_lunchStartSolution[i] = ((Long) _lunchStart[i].get(0)).longValue();
				_lunchEndSolution[i] = ((Long) _lunchEnd[i].get(0)).longValue();
				_dayEndSolution[i] = ((Long) _dayEnd[i].get(0)).longValue();
			}
			else
			{
				if((((Long)_dayWOLunchMorning[i].get(0)).longValue() == 1)
						||(((Long)_dayWOLunchAfternoon[i].get(0)).longValue() == 1))
				{
					_dayStartSolution[i] = ((Long) _dayStart[i].get(0)).longValue();
					_lunchStartSolution[i] = ((Long) _lunchStart[i].get(0)).longValue();
					_lunchEndSolution[i] = -1;
					_dayEndSolution[i] = -1;
				}
				else
				{
					_dayStartSolution[i] = -1;
					_lunchStartSolution[i] = -1;
					_lunchEndSolution[i] = -1;
					_dayEndSolution[i] = -1;
				}
			}	
		}		
	}
	
	public void removeVarAndExpFromManager(FheManager m)
	{
//		this.saveSolution(m);
		m.reset() ;
	}
		
	public void defineVarAndConstraints(FheManager m)
	{
		int i;
		
		_solutionCost = 100000000;
		
		_dayStart   = new FheIntVar[nbDays];
		_lunchStart = new FheIntVar[nbDays];
		_lunchEnd   = new FheIntVar[nbDays];
		_dayEnd     = new FheIntVar[nbDays];
		
		_workDuration = new FheIntVar[nbDays];
		
		_dayWLunch = new FheIntVar[nbDays];
		_dayWOLunchMorning = new FheIntVar[nbDays];
		_dayWOLunchAfternoon = new FheIntVar[nbDays];
		_dayRest   = new FheIntVar[nbDays];
		
		_pole = new FheIntVar[nbDays];
		
		for (i=0;i<nbDays;i++)
		{
			_dayStart[i]   = new FheIntVar(m,0,daySize-1,"Day Start "+i);
			_lunchStart[i] = new FheIntVar(m,0,daySize-1,"Lunch Start "+i);
			_lunchEnd[i]   = new FheIntVar(m,0,daySize-1,"Lunch End "+i);
			_dayEnd[i]     = new FheIntVar(m,0,daySize-1,"Day End "+i);
			
			_workDuration[i] = new FheIntVar(m,0,daySize-1,"Work Duration "+i);
			
			_dayWLunch[i]   = new FheIntVar(m,0,1,"Day with Lunch "+i);
			_dayWOLunchMorning[i]  = new FheIntVar(m,0,1,"Day without Lunch Morning"+i);
			_dayWOLunchAfternoon[i]  = new FheIntVar(m,0,1,"Day without Lunch Afternoon"+i);
			_dayRest[i]     = new FheIntVar(m,0,1,"Day with Rest "+i);
			_pole[i]     = new FheIntVar(m,0,1,"Pole "+i);
			
			_dayWLunch[i].setChoice(2,this);
			_dayWOLunchMorning[i].setChoice(3,this);
			_dayWOLunchAfternoon[i].setChoice(3,this);
			_dayRest[i].setChoice(1,this);
			_pole[i].setChoice(0,this);
			
			_dayStart[i].setChoice(10,this);
			_lunchStart[i].setChoice(11,this);
			_lunchEnd[i].setChoice(12,this);
			_dayEnd[i].setChoice(11,this);
			_workDuration[i].setChoice(14,this);
			
			_dayWLunch[i].setDay(i);
			_dayWOLunchMorning[i].setDay(i);
			_dayWOLunchAfternoon[i].setDay(i);
			_dayRest[i].setDay(i);
			
			_dayStart[i].setDay(i);
			_lunchStart[i].setDay(i);
			_lunchEnd[i].setDay(i);
			_dayEnd[i].setDay(i);

		}
		
		for(i=0;i<nbDays;i++)
		{
			FheIntExp lunch = new FheIntExp(m,0);
			lunch.add(1,_lunchStart[i]);
			lunch.add(-1,_lunchEnd[i]);
			m.addExpAnd(lunch);

			FheIntExp morning = new FheIntExp(m,0);
			morning.add(1,_dayStart[i]);
			morning.add(-1,_lunchStart[i]);
			m.addExpAnd(morning);
			
			FheIntExp afternoon = new FheIntExp(m,0);
			afternoon.add(1,_lunchEnd[i]);
			afternoon.add(-1,_dayEnd[i]);
			m.addExpAnd(afternoon);
			
			// one and only one day type per day
		   	FheIntExp oneDayTypePerDay = new FheIntExp(m,-1);
		   	oneDayTypePerDay.add(-1,_dayWLunch[i]);
		   	oneDayTypePerDay.add(-1,_dayWOLunchMorning[i]);
		   	oneDayTypePerDay.add(-1,_dayWOLunchAfternoon[i]);
		   	oneDayTypePerDay.add(-1,_dayRest[i]);
		   	m.addExpAnd(oneDayTypePerDay);
		   	
		   	FheIntExp onlyOneDayTypePerDay = new FheIntExp(m,1);
		   	onlyOneDayTypePerDay.add(1,_dayWLunch[i]);
		   	onlyOneDayTypePerDay.add(1,_dayWOLunchMorning[i]);
		   	onlyOneDayTypePerDay.add(1,_dayWOLunchAfternoon[i]);
		   	onlyOneDayTypePerDay.add(1,_dayRest[i]);
		   	m.addExpAnd(onlyOneDayTypePerDay);

		   	// Day with lunch : Min working duration
		   	FheIntExp workDurationDWLunchMiD = new FheIntExp(m,-2*_minShiftDuration);  	
		   	workDurationDWLunchMiD.add(-1,_workDuration[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunchMiD);

		   	// Day with lunch : Min working duration
		   	FheIntExp workDurationDWLunchMi = new FheIntExp(m,-2*_minShiftDuration);  	
		   	workDurationDWLunchMi.add(-1,_lunchStart[i]);
		   	workDurationDWLunchMi.add(1,_dayStart[i]);
		   	workDurationDWLunchMi.add(-1,_dayEnd[i]);
		   	workDurationDWLunchMi.add(1,_lunchEnd[i]);   	
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunchMi);
		   	 
		   	//Day with lunch : duration <= durationVariable
		   	FheIntExp workDurationDWLunchL = new FheIntExp(m,0);  	
		   	workDurationDWLunchL.add(1,_lunchStart[i]);
		   	workDurationDWLunchL.add(-1,_dayStart[i]);
		   	workDurationDWLunchL.add(1,_dayEnd[i]);
		   	workDurationDWLunchL.add(-1,_lunchEnd[i]);   	
		   	workDurationDWLunchL.add(-1,_workDuration[i]);   	
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunchL);
		   	
		   	//Day with lunch : duration >= durationVariable
		   	FheIntExp workDurationDWLunchU = new FheIntExp(m,0);  	
		   	workDurationDWLunchU.add(-1,_lunchStart[i]);
		   	workDurationDWLunchU.add(1,_dayStart[i]);
		   	workDurationDWLunchU.add(-1,_dayEnd[i]);
		   	workDurationDWLunchU.add(1,_lunchEnd[i]);   	
		   	workDurationDWLunchU.add(1,_workDuration[i]);   	
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunchU);
		   	
		   	// Day with lunch : Max working duration
		   	FheIntExp workDurationDWLunch = new FheIntExp(m,_maxDayTwoShiftsDuration);  	
		   	workDurationDWLunch.add(1,_lunchStart[i]);
		   	workDurationDWLunch.add(-1,_dayStart[i]);
		   	workDurationDWLunch.add(1,_dayEnd[i]);
		   	workDurationDWLunch.add(-1,_lunchEnd[i]);   	
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunch);
		   	 
		   	// Day with lunch : Max working duration
		   	FheIntExp workDurationDWLunchD = new FheIntExp(m,_maxDayTwoShiftsDuration);  	
		   	workDurationDWLunchD.add(1,_workDuration[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(workDurationDWLunchD);
		   	 
		   	// Day without lunch : duration >= durationVariable
		   	FheIntExp workDurationDWOLunchU = new FheIntExp(m,0);  	
		   	workDurationDWOLunchU.add(-1,_lunchStart[i]);
		   	workDurationDWOLunchU.add(1,_dayStart[i]);
		   	workDurationDWOLunchU.add(1,_workDuration[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunchU);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunchU);

		   	// Day without lunch : duration <= durationVariable
		   	FheIntExp workDurationDWOLunchL = new FheIntExp(m,0);  	
		   	workDurationDWOLunchL.add(1,_lunchStart[i]);
		   	workDurationDWOLunchL.add(-1,_dayStart[i]);
		   	workDurationDWOLunchL.add(-1,_workDuration[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunchL);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunchL);

		   	// Day without lunch : Max working duration
		   	FheIntExp workDurationDWOLunch = new FheIntExp(m,_maxDayOneShiftDuration);  	
		   	workDurationDWOLunch.add(1,_lunchStart[i]);
		   	workDurationDWOLunch.add(-1,_dayStart[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunch);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunch);

		   	// Day without lunch : Max working duration
		   	FheIntExp workDurationDWOLunchMD = new FheIntExp(m,_maxDayOneShiftDuration);  	
		   	workDurationDWOLunchMD.add(1,_workDuration[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunchMD);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunchMD);

		   	// Absolute max duration : Max working duration
		   	FheIntExp absoluteMaxDuration = new FheIntExp(m,_maxDayTwoShiftsDuration);  	
		   	absoluteMaxDuration.add(1,_workDuration[i]);
		   	m.addExpAnd(absoluteMaxDuration);
		   	 
		   	// Day without lunch : no work afternoon		   	
		   	FheIntExp workDurationDWOLunch2 = new FheIntExp(m,0);  	
		   	workDurationDWOLunch2.add(1,_dayEnd[i]);
		   	workDurationDWOLunch2.add(-1,_lunchEnd[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunch2);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunch2);

		   	FheIntExp workDurationDWOLunch3 = new FheIntExp(m,0);  	
		   	workDurationDWOLunch3.add(-1,_dayEnd[i]);
		   	workDurationDWOLunch3.add(1,_lunchEnd[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunch3);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunch3);

		   	// Day without lunch : Min working duration
		   	FheIntExp workDurationDWOLunchMiD = new FheIntExp(m,-_minShiftDuration);  	
		   	workDurationDWOLunchMiD.add(-1,_workDuration[i]);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(workDurationDWOLunchMiD);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(workDurationDWOLunchMiD);

		   	// Day rest : duration <=0		   	
		   	FheIntExp workDurationDRestL = new FheIntExp(m,0);  	
		   	workDurationDRestL.add(1,_workDuration[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRestL);

		   	// Day rest : duration >=0		   	
		   	FheIntExp workDurationDRestU = new FheIntExp(m,0);  	
		   	workDurationDRestU.add(-1,_workDuration[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRestU);

		   	// Day rest : no work morning		   	
		   	FheIntExp workDurationDRest1 = new FheIntExp(m,0);  	
		   	workDurationDRest1.add(1,_dayStart[i]);
		   	workDurationDRest1.add(-1,_lunchStart[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRest1);

		   	// Day rest : no work morning		   	
		   	FheIntExp workDurationDRest2 = new FheIntExp(m,0);  	
		   	workDurationDRest2.add(-1,_dayStart[i]);
		   	workDurationDRest2.add(1,_lunchStart[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRest2);

		   	// Day rest : no work afternoon		   	
		   	FheIntExp workDurationDRest3 = new FheIntExp(m,0);  	
		   	workDurationDRest3.add(-1,_dayEnd[i]);
		   	workDurationDRest3.add(1,_lunchEnd[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRest3);

		   	// Day rest : no work afternoon		   	
		   	FheIntExp workDurationDRest4 = new FheIntExp(m,0);  	
		   	workDurationDRest4.add(1,_dayEnd[i]);
		   	workDurationDRest4.add(-1,_lunchEnd[i]);
		   	_dayRest[i].eventValueIfThenAnd(workDurationDRest4);

		   	// Day with lunch and without lunch : min duration morning
		   	FheIntExp minShift1Duration = new FheIntExp(m,-_minShiftDuration);
		   	minShift1Duration.add(1,_dayStart[i]);
		   	minShift1Duration.add(-1,_lunchStart[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(minShift1Duration);
		   	_dayWOLunchMorning[i].eventValueIfThenAnd(minShift1Duration);
		   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(minShift1Duration);
		   	
		   	// Day with lunch : min duration afternoon
		   	FheIntExp minShift2Duration = new FheIntExp(m,-_minShiftDuration);
		   	minShift2Duration.add(1,_lunchEnd[i]);
		   	minShift2Duration.add(-1,_dayEnd[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(minShift2Duration);
		   	
		   	// Day with lunch : min lunch duration
		   	FheIntExp minLunchDuration = new FheIntExp(m,-_minLunchDuration);
		   	minLunchDuration.add(1,_lunchStart[i]);
		   	minLunchDuration.add(-1,_lunchEnd[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(minLunchDuration);
		   	
		   	// Day with lunch : max lunch duration
		   	FheIntExp maxLunchDuration = new FheIntExp(m,_maxLunchDuration);
		   	maxLunchDuration.add(-1,_lunchStart[i]);
		   	maxLunchDuration.add(1,_lunchEnd[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(maxLunchDuration);
		   	
		   	// Max total day duration
		   	FheIntExp maxTotalDayDuration = new FheIntExp(m,_maxDayTotalDuration);
		   	maxTotalDayDuration.add(-1,_dayStart[i]);
		   	maxTotalDayDuration.add(1,_dayEnd[i]);
		   	m.addExpAnd(maxTotalDayDuration);

		   	//Availabilities
		   	FheIntExp dayStartAvailability = new FheIntExp(m,-_dayStartAvailability[i]);
		   	dayStartAvailability.add(-1,_dayStart[i]);
		   	m.addExpAnd(dayStartAvailability);
  	
		   	FheIntExp lunchStartAvailabilityWL = new FheIntExp(m,_lunchStartAvailability[i]);
		   	lunchStartAvailabilityWL.add(1,_lunchStart[i]);
		   	_dayWLunch[i].eventValueIfThenAnd(lunchStartAvailabilityWL);
		   	
		   	if(_dayEndAvailability[i]>=0)
		   	{
			   	FheIntExp lunchEndAvailability = new FheIntExp(m,-_lunchEndAvailability[i]);
			   	lunchEndAvailability.add(-1,_lunchEnd[i]);
			   	_dayWLunch[i].eventValueIfThenAnd(lunchEndAvailability);
		
			   	FheIntExp lunchEndAvailabilityWOLA = new FheIntExp(m,-_lunchEndAvailability[i]);
			   	lunchEndAvailabilityWOLA.add(-1,_dayStart[i]);
			   	_dayWOLunchAfternoon[i].eventValueIfThenAnd(lunchEndAvailabilityWOLA);

			   	FheIntExp lunchStartAvailabilityWOLM = new FheIntExp(m,_lunchStartAvailability[i]);
			   	lunchStartAvailabilityWOLM.add(1,_lunchStart[i]);
			   	_dayWOLunchMorning[i].eventValueIfThenAnd(lunchStartAvailabilityWOLM);

			   	FheIntExp dayEndAvailability = new FheIntExp(m,_dayEndAvailability[i]);
			   	dayEndAvailability.add(1,_dayEnd[i]);
			   	m.addExpAnd(dayEndAvailability);
		   	}
		   	else
		   	{	
			   	FheIntExp dayEndAvailability = new FheIntExp(m,_lunchStartAvailability[i]);
			   	dayEndAvailability.add(1,_dayEnd[i]);
			   	m.addExpAnd(dayEndAvailability);
		   	}
	   	
		   	// Case1 : split availabilities and split workload
		   	if (_lunchEndAvailability[i] > -1 && _lunchEndWorkload[i] > -1)
		   	{
		   		long boundStart, boundEnd;
		   		
		   		boundStart = _dayStartWorkload[i]-_dayStartAvailability[i];
		   		if(boundStart < 0)
		   			boundStart = 0;

		   		boundEnd =_dayEndAvailability[i] - _dayEndWorkload[i];
		   		
		   		if (boundEnd < 0)
		   			boundEnd = 0;
		  
		   		_dayOrderSearch[i] = _dayEndWorkload[i] - _dayStartWorkload[i];		   		
		   		if(boundStart <= boundEnd)
				   	_searchStartFirst[i] = true;
		   		else
				   	_searchStartFirst[i] = false;
		   	}
		   	// Case2 : split availability and continuous workload
		   	if(_lunchEndAvailability[i] > -1 && _lunchEndWorkload[i] ==-1)
		   	{
		   		long boundStart, boundEnd;
		   		
		   		boundStart = _dayStartWorkload[i]-_dayStartAvailability[i];
		   		if(boundStart < 0)
		   			boundStart = 0;

		   		boundEnd =_dayEndAvailability[i] - _lunchStartWorkload[i];
		   		
		   		if (boundEnd < 0)
		   			boundEnd = 0;
		   		 		
		   		_dayOrderSearch[i] = _lunchStartWorkload[i] - _dayStartWorkload[i];
		   		if(boundStart <= boundEnd)
				   	_searchStartFirst[i] = true;
		   		else
				   	_searchStartFirst[i] = false;
		   	}
		   	// Case3 : continuous availability and continuous workload
		   	if(_lunchEndAvailability[i] == -1 && _lunchEndWorkload[i] ==-1)
		   	{	   		
		   		long boundStart, boundEnd;
		   		
		   		boundStart = _dayStartWorkload[i]-_dayStartAvailability[i];
		   		if(boundStart < 0)
		   			boundStart = 0;

		   		boundEnd =_lunchStartAvailability[i] - _lunchStartWorkload[i];
		   		
		   		if (boundEnd < 0)
		   			boundEnd = 0;
		   	
		   		_dayOrderSearch[i] = _lunchStartWorkload[i] - _dayStartWorkload[i];
		   		if(boundStart <= boundEnd)
				   	_searchStartFirst[i] = true;
		   		else
				   	_searchStartFirst[i] = false;
		   	}
		   	// Case4 : continuous availability and split workload
		   	if(_lunchEndAvailability[i] == -1 && _lunchEndWorkload[i] > -1)
		   	{
		   		long boundStart, boundEnd;
		   		
		   		boundStart = _dayStartWorkload[i]-_dayStartAvailability[i];
		   		if(boundStart < 0)
		   			boundStart = 0;

		   		boundEnd =_lunchStartAvailability[i] - _dayEndWorkload[i];
		   		if (boundEnd < 0)
		   			boundEnd = 0;
		   				   		
		   		_dayOrderSearch[i] = _dayEndWorkload[i] - _dayStartWorkload[i];		   		
		   		if(boundStart <= boundEnd)
				   	_searchStartFirst[i] = true;
		   		else
				   	_searchStartFirst[i] = false;
		   	}
		}

		// Max weekly duration
		FheIntExp maxWeeklyDuration = new FheIntExp(m,_maxDuration);
		FheIntExp maxWeeklyDurationV = new FheIntExp(m,_maxDuration);
		
		for(i=0;i<nbDays;i++)
		{
			maxWeeklyDuration.add(-1,_dayStart[i]);
			maxWeeklyDuration.add(1,_lunchStart[i]);
			maxWeeklyDuration.add(-1,_lunchEnd[i]);
			maxWeeklyDuration.add(1,_dayEnd[i]);
			maxWeeklyDurationV.add(1,_workDuration[i]);
		}
		m.addExpAnd(maxWeeklyDuration);
		m.addExpAnd(maxWeeklyDurationV);

		// Min weekly duration
		FheIntExp minWeeklyDuration = new FheIntExp(m,-_minDuration);
		FheIntExp minWeeklyDurationV = new FheIntExp(m,-_minDuration);
		for(i=0;i<nbDays;i++)
		{
			minWeeklyDuration.add(1,_dayStart[i]);
			minWeeklyDuration.add(-1,_lunchStart[i]);
			minWeeklyDuration.add(1,_lunchEnd[i]);
			minWeeklyDuration.add(-1,_dayEnd[i]);
			minWeeklyDurationV.add(-1,_workDuration[i]);
		}
		m.addExpAnd(minWeeklyDuration);
		m.addExpAnd(minWeeklyDurationV);

		// Max rest days
		FheIntExp maxRestDay = new FheIntExp(m,nbDays - _minDays);
		for(i=0;i<nbDays;i++)
			maxRestDay.add(1,_dayRest[i]);
		m.addExpAnd(maxRestDay);
		
		// Min rest days
		FheIntExp minRestDay = new FheIntExp(m,_maxDays - nbDays);
		for(i=0;i<nbDays;i++)
			minRestDay.add(-1,_dayRest[i]);
		m.addExpAnd(minRestDay);

		// Max day two shifts
		FheIntExp maxTwoShifts = new FheIntExp(m,_maxDayTwoShifts);
		for(i=0;i<nbDays;i++)
			maxTwoShifts.add(1,_dayWLunch[i]);
		m.addExpAnd(maxTwoShifts);
	}
	
	public boolean hasSkill(String thisSkill)
	{
		int i=0;
		while(i<_skills.size())
		{
			String skill = (String) _skills.get(i);
			if (thisSkill.compareTo(skill)==0)
				return true;
			i++;
		}
		return false;
	}
	
	public void addSkill(String skill)
	{
		_skills.add(skill);
	}

	public void setDayStartWorkload(int day, long Workload)
	{
		_dayStartWorkload[day] = Workload;
	}
	public void setLunchStartWorkload(int day, long Workload)
	{
		_lunchStartWorkload[day] = Workload;
	}
	public void setLunchEndWorkload(int day, long Workload)
	{
		_lunchEndWorkload[day] = Workload;
	}
	public void setDayEndWorkload(int day, long Workload)
	{
		_dayEndWorkload[day] = Workload;
	}
	
	public long getDayStartWorkload(int day)
	{
		return _dayStartWorkload[day];
	}
	public long getLunchStartWorkload(int day)
	{
		return _lunchStartWorkload[day];
	}
	
	public long getLunchEndWorkload(int day)
	{
		return _lunchEndWorkload[day];
	}
	public long getDayEndWorkload(int day)
	{
		return _dayEndWorkload[day];
	}
	
	public long getDayStartAvailability(int day)
	{
		return _dayStartAvailability[day];
	}
	public long getLunchStartAvailability(int day)
	{
		return _lunchStartAvailability[day];
	}
	public long getLunchEndAvailability(int day)
	{
		return _lunchEndAvailability[day];
	}
	public long getDayEndAvailability(int day)
	{
		return _dayEndAvailability[day];
	}
	
	public long getDayWLunch(int day)
	{
		return _dayWLunch[day].getFirst() ;
	}
	
	public long getDayWOLunchMorning(int day)
	{
		return _dayWOLunchMorning[day].getFirst() ;
	}
	
	public long getDayWOLunchAfternoon(int day)
	{
		return _dayWOLunchAfternoon[day].getFirst() ;
	}
	
	public long getDayRest(int day)
	{
		return _dayRest[day].getFirst() ;
	}
	
	public long getPole(int day)
	{
		return _pole[day].getFirst();
	}

	public FheIntVar getPoleVar(int day)
	{
		return _pole[day];
	}

	public FheIntVar getDayStartVar(int day)
	{
		return _dayStart[day];
	}

	public FheIntVar getDayEndVar(int day)
	{
		return _dayEnd[day];
	}
	public FheIntVar getLunchStartVar(int day)
	{
		return _lunchStart[day];
	}
	public FheIntVar getLunchEndVar(int day)
	{
		return _lunchEnd[day];
	}
	
	public boolean isDayChoosen(int day)
	{
		if(_dayRest[day].getFirst()==1)
			return false;
		
		if (_dayWLunch[day].size() == 1 && _dayWOLunchMorning[day].size() == 1
				&& _dayWOLunchAfternoon[day].size() == 1 && _dayRest[day].size() == 1)
			return true;
		else
			return false;
	}
}
