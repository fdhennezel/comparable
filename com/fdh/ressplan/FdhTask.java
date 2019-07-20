/*
 * Created on 22 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import java.io.FileWriter;
import java.io.IOException;

import com.fhe.cp.FheIntExp;
import com.fhe.cp.FheIntVar;
import com.fhe.cp.FheManager;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhTask {
	private String _taskName;
	private int _taskDuration, _taskEndMax, _taskStartMin;
	private FdhTasks _previousTasks,_forbidenNextTasks;
	private FdhProfiles _profiles;
	
	private FheIntVar _taskStartDate,_taskEndDate,_taskRessource,_isScheduled,_scheduledOrder, _nonScheduledTaskDuration;
	private long _taskStartDateSolution,_taskEndDateSolution,_taskRessourceSolution,_delay;

	private long _horizon;
	
	private FheManager _m;
	private int _nbChoice;
	private FheIntExp _deadLineExp;
	private int _subOptimized;

	public void setDeadLineExp(FheIntExp deadLineExp)
	{
		_deadLineExp = deadLineExp;
	}
	public void relaxDeadLineExp()
	{
		_delay+=1;
		_deadLineExp.setVal(_taskEndMax+_delay);
	}
	public long getDelay()
	{
		return _delay;
	}
	public FdhTask(FheManager m, String name, int duration, int start, int end, long horizon)
	{
		_m = m;
		_taskName = name;
		_previousTasks = new FdhTasks();
		_forbidenNextTasks = new FdhTasks();
		_profiles = new FdhProfiles();
		_horizon = horizon;
		_taskDuration = duration;
		_taskEndMax = end;
		_taskStartMin = start;
		_subOptimized = 0;
	}

	public void printTask(FileWriter SolutionToFile, FdhReadRessources ressources)
	{
		try
		{
			SolutionToFile.write(_taskName+";"+ _taskStartDateSolution+";"+_taskEndDateSolution+";"+ressources.getName(_taskRessourceSolution)+";");
			SolutionToFile.write(13);
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
		
	}
	public void displayTask(FdhReadRessources ressources)
	{
			System.out.println(_taskName+" commence "+ _taskStartDateSolution+" fini "+_taskEndDateSolution+" avec "+ressources.getName(_taskRessourceSolution)+";");
	}
	
	public void saveTaskSolution()
	{
		if(_isScheduled.getFirst()==0)
			System.out.println("erreur"+_taskName);
		_taskStartDateSolution = _taskStartDate.getFirst();
		_taskEndDateSolution = _taskEndDate.getFirst();
		_taskRessourceSolution = _taskRessource.getFirst();
	}
	
	public void addPriorTask(FdhTask task)
	{
		_previousTasks.add(task);
	}

	public void addProfile(FdhProfile profile)
	{
		_profiles.add(profile);
	}

	public void addForbidenNextTask(FdhTask task)
	{
		_forbidenNextTasks.add(task);
	}
	
	public boolean isSameTask(String name)
	{
		int e = name.compareTo(_taskName);
		if (e==0)
			return true;
		else
			return false;
	}
	public String getTaskName()
	{
		return _taskName;
	}
	
	public void ScheduledTask()
	{
		_isScheduled.removeFirst();
		if(_subOptimized == 0)
			_subOptimized = 1;
	}
	
	public void setSubOptimized(int sub)
	{
		_subOptimized = sub;
	}

	public int getSubOptimized()
	{
		return _subOptimized;
	}
	
	public void addConstraints(FdhReadRessources ressources, FheIntVar nonScheduledDuration)
	{
		int i;
		FheIntExp chain;
		
		_taskStartDate = new FheIntVar(_m,1,_horizon,"début "+_taskName);
		_taskStartDate.setChoice(100);
		
		_taskStartDate.setTask(this);
		_taskEndDate = new FheIntVar(_m,1,_horizon,"fin "+_taskName);
		_taskEndDate.setChoice(1);
		_taskEndDate.setTask(this);
		_nonScheduledTaskDuration = new FheIntVar(_m,1,_horizon,"non scheduled task duration "+_taskName);
		_nonScheduledTaskDuration.setChoice(100);
		_nonScheduledTaskDuration.setTask(this);
		_taskRessource = new FheIntVar(_m,1,ressources.getSize(),"personne "+_taskName);
		_taskRessource.setChoice(100);
		_taskRessource.setTask(this);
		_isScheduled = new FheIntVar(_m,0,1," est planifiée "+_taskName);
		_isScheduled.setChoice(100);
		_isScheduled.setTask(this);
		
//commence après les tâches précédentes : contrainte inviolable
		for(i=0;i<_previousTasks.size();i++)
		{
			chain = new FheIntExp(_m,0);
			chain.add(-1,_taskStartDate);
			FdhTask previousTask = (FdhTask) _previousTasks.get(i);
			chain.add(1,previousTask.getEndDate());
			_m.addExpAnd(chain);
		}
		
// durée maximum de la tâche : contrainte inviolable
		chain = new FheIntExp(_m,-_taskDuration);
		chain.add(-1, _nonScheduledTaskDuration);
		chain.add(1,_taskStartDate);
		chain.add(-1,_taskEndDate);
		_m.addExpAnd(chain);

// durée minimum de la tâche : contrainte avec relaxation possible
		chain = new FheIntExp(_m,_taskDuration);
		chain.add(-1,_taskStartDate);
		chain.add(1,_taskEndDate);
		_m.addExpAnd(chain);
		
// fini avant le fin max : contrainte inviolable
		chain = new FheIntExp(_m,_taskEndMax);
		chain.add(1,_taskEndDate);
		_m.addExpAnd(chain);
		_deadLineExp = chain;
		
// commence après le début min : contrainte inviolable
		
		chain = new FheIntExp(_m,-_taskStartMin);
		chain.add(-1,_taskStartDate);
		_m.addExpAnd(chain);
		_deadLineExp = chain;

// seules les ressources désignées peuvent effectuer la tâche : contrainte inviolable 
		_nbChoice = 0;
		for(i=0;i<ressources.getSize();i++)
		{
			FdhProfile profile = (FdhProfile) _profiles.get(0);
			if (ressources.isPossibleRessource(i,profile)==-1)
				_taskRessource.removeElmt(i+1);
		}		
	}
	
	public void oneChoice()
	{
		_nbChoice++;
	}
	public void resetChoice()
	{
		_nbChoice=0;
	}
	public int getNbChoice()
	{
		return _nbChoice;
	}
	public FheIntVar getEndDate()
	{
		return _taskEndDate;
	}
	public FheIntVar getStartDate()
	{
		return _taskStartDate;
	}
	public FheIntVar getScheduledOrder()
	{
		return _scheduledOrder;
	}
	public FheIntVar getNonScheduledTaskDuration()
	{
		return _nonScheduledTaskDuration;
	}
	public FheIntVar getRessource()
	{
		return _taskRessource;
	}

	public int getTaskDuration()
	{
		return _taskDuration;
	}
	
	public boolean isScheduled()
	{
		if (_isScheduled.getFirst()==0)
			return false;
		else 
			return true;
	}
	
	public boolean setScheduledDate()
	{
		return (_isScheduled.setLast());
	}

	public boolean setMinStart(FdhReadRessources ressources)
	{
		
		long min = _horizon;
		int i,ii;
		
		i=0;
		while(i<_taskRessource.size())
		{
			long index = ((Long) _taskRessource.get(i)).longValue();
			ii = (int) index;
			FdhRessource ressource = ressources.getRessource(ii-1);
			if(min > ressource.getStartAvailability())
				min = ressource.getStartAvailability();
			i++;
		}
		_m.addPropagatedExp(_taskStartDate);
		return _taskStartDate.removeRessourceVal(min);
	}
}
