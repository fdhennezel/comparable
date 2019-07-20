/*
 * Created on 3 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.woshe;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import com.fhe.cp.*;
/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheTeamWork extends ArrayList{

	private int _employeeNb;
	FheManager _m;
	final int nbDays = 7;
	
	public FheTeamWork(FheManager m)
	{
		_m = m;
		_m.addteam(this);
		_employeeNb++;
		
		try{
			
			FileReader file = new FileReader("Employees.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str;

			while ((str = in.readLine()) != null)
			{
				this.add(new FhePerson(in,str.split(";")[0]));
				
			}

			in.close();
	        file.close();
	        
	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
	    
	}

	public int getEmployeeNb()
	{
		return _employeeNb;
	}
	
	public void printSchedule(FileWriter SolutionToFile, FheBusinessWorkload business)
	{
		int i,day;
		
		for(day=0;day<nbDays;day++)
		{
			try
			{
				SolutionToFile.write("Shedule day= "+(day+1));
				SolutionToFile.write(13);	
				for(i=0;i<this.size() ;i++)
				{
					FhePerson person = (FhePerson) this.get( i);
					person.printDaySchedule(SolutionToFile,day);
				}
				SolutionToFile.write(13);	
				business.printWorkloadCoverage( SolutionToFile, day);
				SolutionToFile.write(13);	
			} catch (IOException e) {
				System.err.println("Writing error: " + e);}
		}
	}
	
}
