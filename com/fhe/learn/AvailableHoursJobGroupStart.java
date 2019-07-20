/*
 * Created on 21 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.learn;
import com.fhe.learn.ilogCplexVariable;


/**
 * @author Frédéric
 *
 * Variable with indices for Available Hours per Job Group at Period Start
 * TODO Link with ILOG modules - integrate within HR evolution HR planning software
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AvailableHoursJobGroupStart extends ilogCplexVariable{
	
	private String unit,group,period;
	
	AvailableHoursJobGroupStart(String aUnit, String aGroup, String aPeriod, String aName)
	{
		super(aName);
		this.unit = aUnit;
		this.group = aGroup;
		this.period= aPeriod;
	}

	public String Group(){return group;}
	public String Period(){return period;}
	public String Unit(){return unit;}
	
	public void PrintVariable(){
		System.out.println(unit+" "+group+" "+period);
	}
}
