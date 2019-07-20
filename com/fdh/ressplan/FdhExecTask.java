/*
 * Created on 26 févr. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import com.fhe.cp.FheIntExp;
import com.fhe.cp.FheIntVar;
import com.fhe.cp.FheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhExecTask extends ArrayList{

	private FheManager _m;
	private String _taskName;
	private int _taskDuration, _taskStart, _taskEnd;
	private long _horizon;
	private FheIntVar _isScheduled,_scheduledOrder,_nonScheduledDuration;
	private FdhProfiles _profiles;
	private ArrayList _columnToPrint;
	private FdhExecTasks _previousExecTasks,_jointExecTasks;
	private FheIntExp _durationConstraint;
	private int _subOptimized;
	
	public FdhExecTask(FheManager m, String name, int duration, int start, int end, long horizon)
	{
		_m = m;
		_taskName = name;
		_horizon = horizon;
		_taskDuration = duration;
		_taskStart = start;
		_taskEnd = end;
		_profiles = new FdhProfiles();
		_subOptimized = 0;
		_previousExecTasks = new FdhExecTasks();
		_jointExecTasks = new FdhExecTasks();
	}
		
	public int getSubOptimized()
	{
		return _subOptimized;
	}
	
	public void setSubOptimized(int sub)
	{
		_subOptimized = sub;
	}
	
	public void addPriorExecTask(FdhExecTask task)
	{
		_previousExecTasks.add(task);
	}

	public void addJointExecTask(FdhExecTask task)
	{
		_jointExecTasks.add(task);
	}

	public boolean isSameTask(String name)
	{
		int e = name.compareTo(_taskName);
		if (e==0)
			return true;
		else
			return false;
	}

	public boolean propagateDurationConstraint()
	{
		return _durationConstraint.propagateLowerEqual();
	}
	
	public FheIntVar getNonScheduledDuration()
	{
		return _nonScheduledDuration;
	}
	public void addProfile(FdhProfile profile)
	{
		_profiles.add(profile);
	}

	public void reBuildRelax()
	{
		_nonScheduledDuration.reBuild();
	}
	
	public void initializeVar(FdhReadRessources ressources, int nbTasks)
	{
		int i,j,size,order;

		String varName, taskName;
		FheIntVar oneTask;
				
		size = 0;
		FdhProfile profile = (FdhProfile) _profiles.get(0);
		for(i=1;i<=ressources.getSize();i++)
		{
			if (ressources.isPossibleRessource(i-1,profile)>=0)
				size++;
		}
		_columnToPrint = new ArrayList(size);

		varName = null;
		
		for(i=1;i<=ressources.getSize();i++)
		{
			order = ressources.isPossibleRessource(i-1,profile);
			
			if (order >=0)
			{
				varName=order+ressources.getName(i)+_taskName;

				for(j=_taskStart;j<=_taskEnd;j++)
				{
					taskName=varName;
					oneTask = new FheIntVar(_m,0,1, taskName);
					oneTask.setExecTask(this);
					oneTask.setRessource(ressources.getRessource(i-1));
					oneTask.setDay(j);
					this.add(oneTask);			
				}
			}
		}	
		
		FheIntExp chain = new FheIntExp(_m,_taskDuration);
		
		for(i=0;i<this.size();i++)
		{
			oneTask = (FheIntVar)this.get(i);
			chain.add(1,oneTask);
		}
		
		_m.addExpAnd(chain);
		
		_durationConstraint = chain;
		_nonScheduledDuration = new FheIntVar(_m,0,_horizon,_taskName+"-nSchd");

		chain = new FheIntExp(_m,-_taskDuration);

		for(i=0;i<this.size();i++)
		{
			oneTask = (FheIntVar)this.get(i);
			chain.add(-1,oneTask);
		}
				
		chain.add(-1,_nonScheduledDuration);
		
		_m.addExpAnd(chain);

		Collections.sort(this,ORDER);
		
		_isScheduled = new FheIntVar(_m,0,1,_taskName+"-o-n");
		_scheduledOrder = new FheIntVar(_m,1,nbTasks,_taskName+"-order");

	}
	
	public int nonScheduledDuration()
	{
		int i,duration;
		FheIntVar oneTask;
		duration = 0;
		for(i=0;i<this.size();i++)
		{
			oneTask = (FheIntVar)this.get(i);
			if (oneTask.getFirst()==1)
				duration += 1;
		}
		
		return _taskDuration - duration;
		
	}
	
	public boolean isScheduled()
	{
		if (_isScheduled.getFirst() == 1)
			return true;
		else
			return false;
	}
	
	public void setScheduledOrder()
	{
		_scheduledOrder.setFirst();
		_m.addPropagatedExp(_scheduledOrder);
	}
	
	public FheIntVar getScheduledOrder()
	{
		return _scheduledOrder;
	}
	
	public void ScheduledTask()
	{
		_isScheduled.removeFirst();
		if(_subOptimized == 0)
			_subOptimized = 1;
	}
	
	public String getTaskName()
	{
		return _taskName;
	}
	
	public void testExecTask(FdhReadRessources ressources)
	{
		int i;

		for(i = 0; i< this.size();i++)
		{
			FheIntVar var = (FheIntVar) this.get(i);
			System.out.println("tache "+_taskName+" variable "+var.getName()+" jour "+var.getDay());
			var.print();
		}
	}
	
	public ArrayList printExecTask(FdhReadRessources ressources)
	{
		int j;
		int i=0;
		boolean isScheduled;
		Collections.sort(this,ORDER);
		FheIntVar theVar;
		String ressourceName,toPrint;
		int day = (int) _horizon+1;
					
		_columnToPrint.clear();
		ArrayList oneColumn=null;
		i=0;
		
//		toPrint = _taskName;
		isScheduled = false;
		
		while(i<this.size())
		{
			theVar = (FheIntVar)this.get(i);
			if(day > theVar.getDay())
			{
				for(j=day;j<=_horizon;j++)
					oneColumn.add("");
//					toPrint += ";";
//					SolutionToFile.write(";");

				if (isScheduled)
				{
					_columnToPrint.add(oneColumn);
//					SolutionToFile.write(toPrint);
//					SolutionToFile.write(13);
				}

				oneColumn = new ArrayList((int)_horizon+3);			

				day = theVar.getDay();

				ressourceName = ((FdhRessource) theVar.getRessource()).getName();
				
//				SolutionToFile.write(ressourceName+" ;");
				oneColumn.add(_taskName);
				oneColumn.add(((FdhProfile) _profiles.get(0)).getProfile());
				oneColumn.add(ressourceName);
				isScheduled = false;
				for(j=1;j<day;j++)
					oneColumn.add("");
//					toPrint = toPrint+";";
//					SolutionToFile.write(";");
			}
			else
			{
				day = theVar.getDay();
				if (theVar.getFirst()== 1)
				{
					isScheduled = true;
					oneColumn.add("1");
//					toPrint = toPrint + "1;";
//					SolutionToFile.write("1;");
				}
				else
					oneColumn.add("");
//					toPrint = toPrint + ";";
//					SolutionToFile.write(";");
				i++;
			}
		}
		
		for(j=day+1;j<=_horizon;j++)
			oneColumn.add("");
//			toPrint = toPrint + ";";
//			SolutionToFile.write(";");
		if (isScheduled)
		{
			_columnToPrint.add(oneColumn);
//			SolutionToFile.write(toPrint);
//			SolutionToFile.write(13);
		}		
		return _columnToPrint;
	}
	
	public int getTaskEnd()
	{
		return _taskEnd;
	}
	


    static final Comparator ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            if ((v1.getRessource().getName()).compareTo(v2.getRessource().getName()) > 0)
            	return 1;
            else if ((v1.getRessource().getName()).compareTo(v2.getRessource().getName()) < 0)
            	return -1;
            else if (v1.getDay() > v2.getDay())
            	return 1;
            else if (v1.getDay() < v2.getDay())
            	return -1;
            else
            	return 0;
        }
    };
	
}
