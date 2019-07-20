/*
 * Created on 21 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.cp;
import java.util.*;
import com.fhe.woshe.FhePerson;
import com.fdh.ressplan.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheIntVar extends ArrayList {

	private long min,max;
	private int _choice;
	private String _name;
	private int _id;
	private ArrayList _solutions;
	private ArrayList _eventVarDiff;
	private ArrayList _eventExpIfThenAnd;
	private ArrayList _eventExpIfThenOr;
	private FheManager _m;
	private FhePerson _person;
	private FdhTask _task;
	private FdhRessource _ressource;
	private int _day;
	private FdhExecTask _execTask;
	private ArrayList _expressions;
	
	public FheIntVar(FheManager m,long MinVal, long MaxVal, String Name)
	{
		min = MinVal;
		max = MaxVal;
		_name = Name;
		_m = m;
		_choice = 100;
		
		this._eventVarDiff = new ArrayList();
		this._eventExpIfThenAnd = new ArrayList();
		this._eventExpIfThenOr = new ArrayList();
		this._solutions = new ArrayList();
		this._expressions = new ArrayList();
				
		_m.addVar(this);
		long i;

		for (i = min; i <= max;i++)
		{
			Long elmt = new Long(i);
			this.add(elmt);
		}
	}
	
	FheIntVar(FheIntVar Var)
	{
		_name = Var.getName();
		
		this.addAll(Var);
/*
	  	for (Iterator i=Var.iterator(); i.hasNext(); )
	  	{
	  	    Long elmt = (Long) i.next();
	  	    Long cpy = new Long(elmt.longValue());
	  	    this.add(cpy);
	  	}
	  	*/
//	  	System.out.println("create duplicate :");
//	  	this.printBounds();
	}

	public void reBuild()
	{
		long i;
		this.clear();
		
		for (i = min; i <= max;i++)
		{
			Long elmt = new Long(i);
			this.add(elmt);
		}		
	}
	
	public void addExp(FheIntExp exp)
	{
		_expressions.add(exp);
	}
	
	public int getExpressionSize()
	{
		return _expressions.size();
	}
	
	public FheIntExp getExpression(int i)
	{
		return (FheIntExp)_expressions.get(i);
	}
	
	public void addZero()
	{
		long elmt = ((Long) this.get(0)).longValue();
		if (elmt > 0)
		{
			this.add(0,new Long(0));
		}
	}
	
	public void setId(int Id)
	{
		_id = Id;
	}
	
	public void setChoice(int choice, FhePerson person)
	{
		_choice = choice;
		_person = person;
	}

	public void setChoice(int choice)
	{
		_choice = choice;
	}
	
	public void setTask(FdhTask task)
	{
		_task = task;
	}
	
	public void setExecTask(FdhExecTask task)
	{
		_execTask = task;
	}
	
	public void setRessource(FdhRessource ressource)
	{
		_ressource = ressource;
	}
	
	public int getChoice()
	{
		return _choice;
	}
	
	public FdhTask getTask()
	{
		return _task;
	}

	public FdhExecTask getExecTask()
	{
		return _execTask;
	}
	
	public FdhRessource getRessource()
	{
		return _ressource;
	}
	
	public void setDay(int day)
	{
		_day = day;
	}
	
	public int getDay()
	{
		return _day;
	}
	
	public FhePerson getPerson()
	{
		return _person;
	}
	
	public int getId()
	{
		return _id;
	}
	public void newSolution()
	{
		_solutions.add(this.get(0));
	}
	
	public long getSolution(int i)
	{
		return ((Long)_solutions.get(i)).longValue();
	}
	
	public boolean whenValue()
	{
		long val;
		int i;
		
		val = this.getValueAt(0);
//		System.out.println("whenValue "+val+" with variable "+_name);
		
		for(i = 0; i < _eventVarDiff.size() ;i++)
	  	{
			FheIntExp eventExp = (FheIntExp) _eventVarDiff.get(i);
			if (eventExp.getSize() == 1 && eventExp.getMult(0) == 1)
			{
				FheIntVar eventVar = (FheIntVar) eventExp.getVar(0);
//				System.out.println("Over "+eventVar.getName()+" remove Elmt "+(val+eventExp.getVal()));
				if(eventVar.removeElmt(val+eventExp.getVal())==true)
						return true;
			}
	  	}
		
		if(val == 1)
		{
			for(i = 0; i < _eventExpIfThenAnd.size();i++)
			{
				FheIntExp eventExp = (FheIntExp) _eventExpIfThenAnd.get(i);
				_m.addExpAnd(eventExp);
			}
			
			for(i = 0; i < _eventExpIfThenOr.size();i++)
			{
				FheExpOr eventExpOr = (FheExpOr) _eventExpIfThenOr.get(i);
				_m.addExpOr(eventExpOr);
			}
		}
		
		return false;
	}
	
	public void eventValueDiff(FheIntExp Exp)
	{
		_eventVarDiff.add(Exp);
	}
	
	public void eventValueIfThenAnd(FheIntExp Exp)
	{
		_eventExpIfThenAnd.add(Exp);
	}

	public void eventValueIfThenOr(FheIntExp Exp1,FheIntExp Exp2)
	{
		FheExpOr ExpOr = new FheExpOr(Exp1,Exp2);	
		_eventExpIfThenOr.add(ExpOr);
	}

	public int searchIndex(long val)
	{
		int index = 0;
		
		while(index < this.size())
		{
			long t1 = ((Long) this.get(index)).longValue();
			if (t1 == val) return index;
			index++;
		}
		return -1;
	}
	
	public boolean removeRessourceVal(long val)
	{
		boolean end = false;
		
		while(!end)
		{
			if (this.size() == 0)
				return true;
			
			long t1 = ((Long) this.get(0)).longValue();
			if (t1 < val)
				this.remove(0);
			else
				end = true;
		}
		
		return false;
	}
	
	public boolean removeRessourceAvailability(long startUnavailable, long endUnavailable)
	{
		boolean end = false;
		int ii;
		ii=0;
		if (startUnavailable > endUnavailable)
			return false;
		
		long t1 = ((Long) this.get(this.size()-1)).longValue();
		if(endUnavailable > t1)
			endUnavailable =t1;
		
		while(!end)
		{
			if (this.size() == 0)
				return true;
			
			t1 = ((Long) this.get(ii)).longValue();
			if (t1 < startUnavailable)
				ii++;
			else
				if (t1 <= endUnavailable)
					this.remove(ii);
				else
					end = true;
		}
		
		return false;
	}

	public long removeUsedRessource(long startUnavailable, long duration)
	{
		boolean end = false;
		int ii;
		ii=0;
		
		long t1,usedTime;
		t1=-1;
		usedTime = 0;
		while(ii < this.size())
		{
			if (this.size() == 0)
				return -1;
			
			t1 = ((Long) this.get(ii)).longValue();
			if (t1 < startUnavailable)
				ii++;
			else
				if (usedTime < duration)
				{
					this.remove(ii);
					usedTime++;
				}
				else
				{
					end = true;
					ii = this.size();
				}
		}
		
		if (!end)
			return -1;
		else
			return t1;
	}

	public boolean removeElmt(long val)
	{
		int index = this.searchIndex(val);
		if (index == -1)
			index = -1;
		
		if (index >=0 ) this.remove(index);
		
		if (index >= 0 && this.size()==1 )
			return this.whenValue();
		
		return this.isEmpty();
	}
	
	public long getMin()
	{
		return min;
	}
	
	public long getMax()
	{
		return max;
	}
		
	public String getName()
	{
		return _name;
	}
		
	public long getFirst()
	{
		Long elmt = (Long) this.get(0);
		return elmt.longValue();		
	}
	
	public long getLast()
	{
		Long elmt = (Long) this.get(this.size() -1);
		return elmt.longValue();		
	}
	
	public boolean setValue(long val)
	{
		int i;
		int index = this.searchIndex(val);
		if(index==-1)
			return true;
		
		this.setAt(index);

		return this.whenValue();

	}

	public int setLowestGreaterThan(long val)
	{
		int i,index;
		
		i=0;
		index = -1;
		
		while(i<this.size() )
		{
			long t1 = ((Long) this.get(i)).longValue();
			if (t1 >= val)
			{
				index = i;
				this.setAt(i);
				i = 10000 ;
			}
			else
				i++;
		}

		if(i<10000)
		{
			index = this.size() -1;
			this.setAt(this.size() - 1);
		}
		
		if (this.size() == 1)
			this.whenValue();

		return index;

	}

	public boolean setLowestActuallyGreaterThan(long val)
	{
		int i,index;
		
		i=0;
		index = -1;
		
		while(i<this.size() )
		{
			long t1 = ((Long) this.get(i)).longValue();
			if (t1 >= val)
			{
				index = i;
				this.setAt(i);
				i = 10000 ;
			}
			else
				i++;
		}

		if(i<10000)
			return true;
		
		return this.whenValue();
	}

	public int setGreatestLowerThan(long val)
	{
		int i,index;
		
		index = -1;
		i=this.size() -1;
		
		while(i>= 0)
		{
			long t1 = ((Long) this.get(i)).longValue();
			if (t1 <= val)
			{
				index = i;
				this.setAt(i);
				i = -10;
			}
			else
				i--;
		}

		if(i>-10)
		{
			index = 0;
			this.setAt(0);
		}
		
		if (this.size() == 1)
			this.whenValue();

		return index;

	}

	public boolean setAt(int index)
	{
		int i;
		
		i=this.size()-1;
		while(i>index)
		{
			this.remove(i);
			i--;
		}
		i--;
		while(i>=0)
		{
			this.remove(i);
			i--;
		}
		
		return false;
	}
	
	public boolean removeAfter(int index)
	{
		int i;
		
		i=this.size()-1;
		while(i>=index)
		{
			this.remove(i);
			i--;
		}
		
		return false;
	}

	public boolean removeBefore(int index)
	{
		int i;
		
		i=index;
		while(i>=0)
		{
			this.remove(i);
			i--;
		}
		
		return false;
	}

	public boolean setFirst()
	{
		int i;
		i=this.size()-1;
		while(i>0)
		{
			this.remove(i);
			i--;
		}

		return this.whenValue();
	}
	
	public boolean removeLast()
	{
		if (this.size()==1)
			return true;
				
		this.remove(this.size()-1);
		
		if (this.size() == 1)
			return this.whenValue();
		
		return false;
	}

	public boolean removeFirst()
	{
		if (this.size()==1)
			return true;
		
		this.remove(0);
		
		if (this.size() == 1)
			return this.whenValue();
		
		return false;
	}

	public boolean doRemoveFirst()
	{
		this.remove(0);
				
		return false;
	}

	public boolean setLast()
	{
		int i;
		i=this.size()-2;
		while(i>=0)
		{
			this.remove(i);
			i--;
		}


		return this.whenValue();
	}

	public long getValueAt(int i)
	{
		Long elmt = (Long) this.get(i);
		return elmt.longValue();
	}
	
	public boolean removeAt(int index)
	{
		int size = this.size() ;
		
		this.remove(index);
		
		if (size > 1 && this.size() == 1)
			this.whenValue();

		return this.isEmpty();
	}

	public boolean mustRemoveAt(int index)
	{
		int size = this.size() ;
		
		this.remove(index);
		
		if (size > 1 && this.size() == 1)
			this.whenValue();
		else
		{
			if(size == this.size())
				return true;
		}
		
		return this.isEmpty();
	}

	public boolean restore(FheIntVar Var)
	{
		this.clear();
		this.addAll(Var);
	  	/*
		for (Iterator i=Var.iterator(); i.hasNext(); )
	  	{
	  	    Long elmt = (Long) i.next();
	  	    this.add(elmt);
	  	}
	  	*/
		
//		System.out.println("fin de la restauration de la variable "+_name);
		return true;
	}

	public void print()
	{
		System.out.print(_name+" contains[");
	  	for (Iterator i=this.iterator(); i.hasNext(); )
	  	{
	  	    Long elmt = (Long) i.next();
	  	    System.out.print(elmt+" ");
	  	}
	  	System.out.println("]");
	}

	public void printExp()
	{
	  	for (Iterator i=this.iterator(); i.hasNext(); )
	  	{
	  	    Long elmt = (Long) i.next();
	  	    System.out.println(_name+" contains "+elmt);
	  	}
	}

	public void printBounds()
	{
		if(this.size()>0)
			System.out.println(_name+"=["+this.get(0)+"..."+this.get(this.size()-1)+"]");
	}
}
