/*
 * Created on 6 mars 2006
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
public class FdhTheExecTaskPlan {
	   public static void main(String args[]) {
		final long horizon = 50;
		long optimum;
		int option = (Integer.valueOf(args[0])).intValue();

		try
		{
			int i;
		    FileWriter SolutionToFile = new FileWriter("Solution_Planning.csv");
		   	FheManager m = new FheManager();

		   	FdhReadRessources ressources = new FdhReadRessources(m,horizon);
		   	
		   	m.setSearchOption(option);
		  		   		
		   	FheIntVar nonScheduledDuration = new FheIntVar(m,0,10000," optimisons ");
		   	m.saveOptimizeVar(nonScheduledDuration);
	   		FheIntExp optimizeExp = new FheIntExp(m,10000);
	   		optimizeExp.add(1,nonScheduledDuration);
	   		m.addExpAnd(optimizeExp);
	   		m.addOptimization(optimizeExp);
		   	FdhReadExecTasks tasks = new FdhReadExecTasks(m,ressources,nonScheduledDuration,horizon);
	   		
		   	while(m.findSolution()&& m.getFailNb()>0)
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
		   		tasks.printExecTasksSolution(SolutionToFile, ressources);
			}
			else
				System.out.println("Pas de solution après "+m.getFailNb());
	   
		   	SolutionToFile.close();
		   	
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}

		
/*		
		try
		{
			int i;
		    FileWriter SolutionToFile = new FileWriter("Solution_Planning.csv");
		   	FheManager m = new FheManager();

		   	FdhReadRessources ressources = new FdhReadRessources(m,horizon);
		   	
		   	FheIntVar fin = new FheIntVar(m,1,horizon," optimisons ");
		   	m.addOptimizeVar(fin);
		   	m.setSearchOption(option);

		   	FdhReadTasks tasks = new FdhReadTasks(m,ressources,horizon, fin);
		   		   	
		   	if(option == 1)
		   	{
		   		FheIntExp optimize = new FheIntExp(horizon);
		   		optimize.add(1,fin);
		   		m.addExpAnd(optimize);
		   		m.addOptimization(optimize);

		   		m.StartTaskASAP(false);
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
	
*/
	   }
}
