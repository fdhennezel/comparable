/*
 * Created on 21 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.ressplan;
import java.io.FileWriter;
import java.io.IOException;

import com.fhe.cp.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhTheTaskPlan {
	   public static void main(String args[]) {
		final long horizon = 200;
		long optimum;

		int option = (Integer.valueOf(args[0])).intValue();
		try
		{
			int i;
		    FileWriter SolutionToFile = new FileWriter("Solution_Planning.csv");
		   	FheManager m = new FheManager();
		   	m.setSearchOption(option);

		   	FdhReadRessources ressources = new FdhReadRessources(m,horizon);
		   	
		   	FheIntVar nonScheduledDuration = new FheIntVar(m,0,10000," optimisons ");
		   	m.saveOptimizeVar(nonScheduledDuration);
	   		FheIntExp optimizeExp = new FheIntExp(m,10000);
	   		optimizeExp.add(1,nonScheduledDuration);
	   		m.addExpAnd(optimizeExp);
	   		m.addOptimization(optimizeExp);
		   	FdhReadTasks tasks = new FdhReadTasks(m,ressources,nonScheduledDuration,horizon);
		   		   	
		   	while(m.findSolution2()&& m.getFailNb()>0)
		   	{
				System.out.println("Restaure une solution partielle après "+m.getFailNb());
				m.setCurrentOptimizeVal();
				m.restorePartialSolution();
//		   		m.restaureOptimizeVar();
				m.resetNbFail();
				m.addPropagatedExp(nonScheduledDuration);
				optimizeExp.setVal(10000);
		   	}
		   	
			if(m.getNbSolutions()>0)
			{
		   		tasks.printTasksSolution(SolutionToFile, ressources);
			}
			else
				System.out.println("Pas de solution après "+m.getFailNb());
	   
		   	SolutionToFile.close();
		   	
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
	   }
}
