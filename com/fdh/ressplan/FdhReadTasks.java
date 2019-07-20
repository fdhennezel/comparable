/*
 * Created on 21 janv. 2006
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

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhReadTasks {
	private FheManager _m;
	private int [] _taskDuration;
	
	private FdhTasks _tasks;
	
	FdhReadTasks(FheManager m, FdhReadRessources ressources, FheIntVar nonScheduledDuration, long horizon)
	{
		long value;
		FdhTask task;
		FdhTasks tasks= new FdhTasks();
		_m = m;
		_m.addTheTasks( this);
		try{
			char c;
			int i,j,k;

			FileReader file = new FileReader("Tasks.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str,fields[];
			_tasks = new FdhTasks();
		
			while ((str = in.readLine()) != null)
			{
	            if (str.split(";").length < 5)
	            	System.out.println("Task.csv : format incorrect ");
	            int duration = (Integer.valueOf( str.split(";")[1])).intValue();
	            int min      = (Integer.valueOf( str.split(";")[4])).intValue();
	            int max      = (Integer.valueOf( str.split(";")[5])).intValue();
	            task = new FdhTask(m,str.split(";")[0],duration,min,max,horizon);
	            tasks.add(task);
	            FdhProfile profile = new FdhProfile(str.split(";")[2]);
	            task.addProfile(profile);
			}
        		            		
		
			in.close();
	        file.close();

	        file = new FileReader("Tasks.csv");
	        in = new BufferedReader(file);

	        i=0;
			while ((str = in.readLine()) != null)
			{
  	        	task = (FdhTask) tasks.get(i);

   	        	if (str.split(";")[3].length()>0)
   	        	{
   	        		if (!tasks.addPriorTask(task,str.split(";")[3]))
            	    	System.out.println(" enchaînement avec une tâche inconnue ");
   	        	}
				i++;
	        }   
			in.close();
	        file.close();
	       
	        FheIntExp order = new FheIntExp(_m,0);
	        FheIntExp totalNonScheduledMin = new FheIntExp(_m,0);
	    	totalNonScheduledMin.add(-1,nonScheduledDuration);

	    	for(i=0;i<_tasks.size();i++)
	        {
	        	task = (FdhTask) _tasks.get(i);
				task.addConstraints(ressources,nonScheduledDuration);
				if(task.getRessource().size()==0)
				{
					System.out.println("Aucune ressource ne peut effectuer la tache "+task.getTaskName());
				}
	        	order.diff(1,task.getScheduledOrder());
	        	totalNonScheduledMin.add(1,task.getNonScheduledTaskDuration());
			}
	        
	       
	        m.addExpEventDiff(order);
			m.addExpAnd(totalNonScheduledMin);
	        

	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
    }
	
	public FdhTask getTask(int i)
	{
		return (FdhTask) _tasks.get(i);
	}
	
	public int getSize()
	{
		return _tasks.size();
	}
	 
	public void printTasksSolution(FileWriter SolutionToFile, FdhReadRessources ressources)
	{
		int i;
		for(i=0;i<_tasks.size();i++)
		{
			FdhTask task = (FdhTask) _tasks.get(i);
			task.printTask(SolutionToFile, ressources);
		}		
	}
	public void displayTasksSolution( FdhReadRessources ressources)
	{
		int i;
		for(i=0;i<_tasks.size();i++)
		{
			FdhTask task = (FdhTask) _tasks.get(i);
			task.displayTask(ressources);
		}		
	}
	public void saveTasksSolution()
	{
		int i;
		for(i=0;i<_tasks.size();i++)
		{
			FdhTask task = (FdhTask) _tasks.get(i);
			task.saveTaskSolution();
		}		
	}

}
