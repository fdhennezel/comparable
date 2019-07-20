/*
 * Created on 26 févr. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.fhe.cp.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhReadExecTasks {

	private FheManager _m;
	private FdhExecTasks _tasks;
	private ArrayList _resultToPrint;
	private long _horizon;
	
	public FdhReadExecTasks(FheManager m,FdhReadRessources ressources,FheIntVar nonScheduledDuration,long horizon)
	{
		long value;
		_m = m;
		_horizon = horizon;
		_m.addTheExecTasks( this);
		FdhExecTask task,jointTask;
		FheIntVar var;
		String name1,name2;
		boolean end;
		int i,j,k;
		int day1,day2,diff;
		
		try{
			char c;

			FileReader file = new FileReader("ExecTasks.csv");
			BufferedReader in = new BufferedReader(file);
			String str,fields[];
			_tasks = new FdhExecTasks();
			name1 ="not a task name";
			jointTask = null;
	
			while ((str = in.readLine()) != null)
			{
				if (str.split(";").length < 5)
					System.out.println("ExecTasks.csv : format incorrect ");
				int duration = (Integer.valueOf( str.split(";")[1])).intValue();
				int start    = (Integer.valueOf( str.split(";")[3])).intValue();
				int finish      = (Integer.valueOf( str.split(";")[4])).intValue();
				if(str.split(";")[0].length()>0)
				{
					name1 = str.split(";")[0];
					task = new FdhExecTask(m,name1,duration,start,finish,horizon);
					jointTask = task;
				}
				else
				{
					task = new FdhExecTask(m,name1,duration,start,finish,horizon);
					jointTask.addJointExecTask(task);
					task.addJointExecTask(jointTask);
				}
				
				_tasks.add(task);
	            FdhProfile profile = new FdhProfile(str.split(";")[2]);
	            task.addProfile(profile);
			}

		}
        catch (IOException e) {
		System.err.println("Reading error: " + e);}
        ArrayList taskList = new ArrayList();
    	
        FheIntExp order = new FheIntExp(_m,0);
        FheIntExp totalNonScheduledMin = new FheIntExp(_m,0);
    	totalNonScheduledMin.add(-1,nonScheduledDuration);
       
        for(i=0;i<_tasks.size();i++)
        {
        	task = (FdhExecTask) _tasks.get(i);
			task.initializeVar(ressources, _tasks.size());
        	for(j=0;j<task.size();j++)
        		taskList.add(task.get(j));
        	order.diff(1,task.getScheduledOrder());
        	totalNonScheduledMin.add(1,task.getNonScheduledDuration());
        }
        m.addExpEventDiff(order);
		m.addExpAnd(totalNonScheduledMin);
        
        Collections.sort(taskList,ORDER);
        
// assurer qu'une ressource ne fasse qu'une seule tâche à la fois et enlever l'indisponibilité des ressources
        
        FheIntExp chain;

		i=0;
		var = (FheIntVar) taskList.get(i);
		name1 = var.getRessource().getName();
		day1 = var.getDay();
		chain = new FheIntExp(_m,1);

		while(i<taskList.size())
		{
			var = (FheIntVar) taskList.get(i);
			name2 = var.getRessource().getName();
			diff = name1.compareTo(name2);
			day2 = var.getDay();
			if(!var.getRessource().isAvailableRessource(day2))
				var.setFirst();
			
			if(diff==0 && day1 == day2)
			{
				chain.add(1,var);
				i++;
			}
			else
			{
				_m.addExpAnd(chain);
				name1 = var.getRessource().getName();
				day1 = var.getDay();
				chain = new FheIntExp(_m,1);
			}
		}		
		
/*        for(i=0;i<_tasks.size();i++)
        {
        	task = (FdhExecTask) _tasks.get(i);
        	task.testExecTask(ressources);
        }
*/        Collections.sort(_tasks,ORDERTASKS);
	}
	
	public long getHorizon()
	{
		return _horizon;
	}
	public FdhExecTask getTask(int i)
	{
		return (FdhExecTask) _tasks.get(i);
	}
	
	public int getTaskSize()
	{
		return _tasks.size();
	}
	
	public void storeExecTasksSolution(FdhReadRessources ressources)
	{
		int i;
		_resultToPrint = new ArrayList();
		ArrayList result;
		for(i=0;i<_tasks.size();i++)
		{
			FdhExecTask task = (FdhExecTask) _tasks.get(i);
			result = task.printExecTask(ressources);
			_resultToPrint.addAll(result);
		}
	}
	public void printExecTasksSolution(FileWriter SolutionToFile, FdhReadRessources ressources)
	{
		try
		{
			boolean printLine;
			String valToPrint;

/*			_resultToPrint = new ArrayList();
			ArrayList result;

			for(i=0;i<_tasks.size();i++)
			{
				FdhExecTask task = (FdhExecTask) _tasks.get(i);
				result = task.printExecTask(ressources);
				_resultToPrint.addAll(result);
			}
*/
			int i,day;
			SolutionToFile.write(";;;");
			for(day = 1;day<=_horizon/2;day++)	
			{
				SolutionToFile.write(day+";");
			}	
			SolutionToFile.write(13);
			for(i=0;i<_resultToPrint.size();i++)
			{
				printLine = false;
				for(day = 1;day<= _horizon/2+3;day++)
				{		
					valToPrint = (String) ((ArrayList)_resultToPrint.get(i)).get(day-1);
					if (valToPrint.compareTo("1")==0)
					{
						printLine = true;
						day = (int)_horizon;
					}
				}
				if (printLine)
				{
					for(day = 1;day<= _horizon/2+3;day++)		
							SolutionToFile.write(((ArrayList)_resultToPrint.get(i)).get(day-1)+";");
					SolutionToFile.write(13);
				}
			}
			SolutionToFile.write("+;+;+;");
			for(day = (int)_horizon/2 + 1;day<= _horizon + 3;day++)	
			{
				SolutionToFile.write(day+";");
			}	
			SolutionToFile.write(13);
			for(i=0;i<_resultToPrint.size();i++)
			{
				printLine = false;
				for(day = (int)_horizon/2 + 4;day<= _horizon+3;day++)
				{		
					valToPrint = (String) ((ArrayList)_resultToPrint.get(i)).get(day-1);
					if (valToPrint.compareTo("1")==0)
					{
						printLine = true;
						day = (int)_horizon;
					}
				}
				if (printLine)
				{
					for(day = 0;day<3;day++)
						SolutionToFile.write(((ArrayList)_resultToPrint.get(i)).get(day)+";");
					for(day = (int)_horizon/2 + 4;day<= _horizon+3;day++)
						SolutionToFile.write(((ArrayList)_resultToPrint.get(i)).get(day-1)+";");
					SolutionToFile.write(13);
				}
			}
		} catch (IOException e) {
			System.err.println("Writing error: " + e);
			}				    	
	}
	
	static final Comparator ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            int diff = (v1.getRessource().getName()).compareTo(v2.getRessource().getName());
            if (diff > 0)
            	return 1;
            else
            {
            	if (diff < 0)
            		return -1;
            	else
               		return (v1.getDay() > v2.getDay() ? 1 :
                        (v1.getDay() == v2.getDay() ? 0 : -1));
            }
        }
    };
    
    static final Comparator ORDERTASKS = new Comparator() {
        public int compare(Object o1, Object o2) {
            FdhExecTask t1 = (FdhExecTask) o1;
            FdhExecTask t2 = (FdhExecTask) o2;

       		return (t1.getTaskEnd() > t2.getTaskEnd() ? 1 :
                      (t1.getTaskEnd() == t2.getTaskEnd() ? 0 : -1));
        }
    };

}
