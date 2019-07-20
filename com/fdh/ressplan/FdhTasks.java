/*
 * Created on 22 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;

import java.util.ArrayList;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhTasks extends ArrayList{

	public FdhTasks()
	{
		
	}

	public boolean addPriorTask(FdhTask theTask,String previousTask)
	{
		int i;
		
		for (i=0;i< this.size();i++)
		{
			FdhTask task = (FdhTask) this.get(i);
			if (task.isSameTask(previousTask))
			{	
				theTask.addPriorTask(task);
				return true;
			}
		}
		return false;
	}
	
}
