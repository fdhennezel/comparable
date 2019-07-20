/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.woshe;

import java.io.*;

import com.fhe.cp.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Demowoshe {
	   public static void main(String args[]) {
	   	
	   	boolean fail=false;
	   	
	   	FheManager m = new FheManager();
	   
    	FhePerson emp;
	   	
	   	FheBusinessWorkload business = new FheBusinessWorkload(m);
	   	
	   	FheTeamWork team = new FheTeamWork(m);

	   	try
			{
				int i;
			    FileWriter SolutionToFile = new FileWriter("WorkloadCoverage.csv");

			   	business.setRemainingWorkload(team);

			   	for(i=0;i<team.size();i++)
			   	{	   	
			 		emp = (FhePerson)team.get(i);
			 		
				   	business.setRemainingWorkload(emp);
				   	
				   	emp.defineVarAndConstraints(m);

				   	System.out.println("Propagate...");
				   	System.out.println("End propagate with fail="+fail+" and "+ m.getFailNb() +" fails");
				   	if(fail == false)
				   	{
				   		System.out.println("Solution with "+m.getFailNb() +" fails");
				   	}	   	
				   	emp.removeVarAndExpFromManager( m);
			   		emp.printSchedule(SolutionToFile) ;
				   	business.setRemainingWorkload(team);
				   	business.printRemainingWorload(SolutionToFile) ;
			   	}	   	
			   	team.printSchedule(SolutionToFile,business);
		   
		   SolutionToFile.close();
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}

	   	
	   }
}
