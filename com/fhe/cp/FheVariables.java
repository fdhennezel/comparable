/*
 * Created on 23 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.cp;
import java.util.*;
/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheVariables extends ArrayList{

	int _choiceNumber;
	int _currentExpAnd,_currentExpOr;
	
	FheVariables(int ChoiceNumber)
	{
		_choiceNumber = ChoiceNumber;
		_currentExpAnd = -1;
		_currentExpOr = -1;
	}
	
	FheVariables(int ChoiceNumber, int CurrentExpAnd, int CurrentExpOr, FheVariables Init)
	{
		_choiceNumber = ChoiceNumber;
		_currentExpAnd = CurrentExpAnd;
		_currentExpOr = CurrentExpOr;
		// duplicate list
	  	for (Iterator i=Init.iterator(); i.hasNext(); )
	  	{
	  		FheIntVar var = (FheIntVar) i.next();
	  		FheIntVar newVar = new FheIntVar(var);
	  	    this.add(newVar);
	  	}
	}
	
	public void reset()
	{
		this.clear();
		_choiceNumber = -1;
		_currentExpAnd= -1;
		_currentExpOr = -1;
	}
	
	public void addVariable(FheIntVar Var)
	{
		this.add(Var);
	}
	
	public boolean restore(FheVariables Backup)
	{
		int i;

		if (Backup.size()==0)
			return false;
		if (this.size()!=Backup.size())
			return false;
		
		i=0;
		while(i<this.size())
	  	{
	  		FheIntVar var = (FheIntVar) this.get(i);
	  		FheIntVar backVar = (FheIntVar) Backup.get(i);
	  	    if (var.restore(backVar)==false)
	  	    	return false;
	  	    i++;
	  	}
		
		return true;
	}
	
	public int getCurrentExpAnd()
	{
		return _currentExpAnd;
	}
	
	public int getCurrentExpOr()
	{
		return _currentExpAnd;
	}
	
	public int getChoiceNumber()
	{
		return _choiceNumber;
	}
	
	public void print()
	{
	  	for (Iterator i=this.iterator(); i.hasNext(); )
	  	{
	  		FheIntVar var = (FheIntVar) i.next();
	  		var.print();
	  	}		
	}
}
