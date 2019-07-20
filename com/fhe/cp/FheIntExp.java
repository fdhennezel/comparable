/*
 * Created on 21 mars 2005
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
public class FheIntExp {
	
	private int _type; // -1 lower equal - 0 equal - 1 greater equal
	private long _val; // valeur sur la partie droite de la contrainte
	private int _id;
	private boolean _pruned;
	
	private ArrayList _expAnd;
	private ArrayList _mult;
	private FheManager _m;
	
	public FheIntExp(FheManager m,long Val)
	{
		_type = -1;
		_val = Val;
		this._expAnd = new ArrayList();
		this._mult = new ArrayList();
		_m = m;
	}
	
	public void setId(int Id)
	{
		_id = Id;
	}
	
	public int getId()
	{
		return _id;
	}
	public boolean isPruned()
	{
		return _pruned;
	}
	public boolean add(long Mult, FheIntVar Var)
	{
		Long LMult = new Long(Mult);
		_mult.add(LMult);
		_expAnd.add(Var);
		Var.addExp(this);
		return true;
	}
	
	public boolean diff(long Mult, FheIntVar Var)
	{
		Long LMult = new Long(Mult);
		_mult.add(LMult);
		_expAnd.add(Var);
		return true;
	}
	
	public FheIntVar getVar(int i)
	{
		return (FheIntVar)_expAnd.get(i);
	}
	
	public long getMult(int i)
	{
		return ((Long)_mult.get(i)).longValue();
	}
	
	public long getVal()
	{
		return _val;
	}
	
	public long getSize()
	{
		return _expAnd.size();
	}
	
	public void printFactors()
	{
		int [] pointers;
		int i;
		boolean end;
		
		pointers = new int[_expAnd.size()];

	  	for (i=0; i< _expAnd.size();i++)
	  	{
	  		pointers[i] = 0;
	  	}

	  	if (_expAnd.size() == 0)
	  		end = true;
	  	else
	  		end = false;
	  	
	  	while(end == false)
	  	{
	  		long val = 0;
	  		for (i=0; i< _expAnd.size();i++)
		  	{
		  		FheIntVar var = (FheIntVar) _expAnd.get(i);
		  		Long mult = (Long) _mult.get(i);
		  		val += var.getValueAt(pointers[i]) * mult.longValue();
		  	}
	  		
	  		for (i=0; i< _expAnd.size();i++)
		  	{
		  		FheIntVar var = (FheIntVar) _expAnd.get(i);
		  		Long mult = (Long) _mult.get(i);
	  			System.out.print("("+mult.longValue()+")("+var.getValueAt(pointers[i])+")");
	  			if (i<_expAnd.size()-1) System.out.print("+");
		  	}
	  		System.out.println("="+val);
	  		
	  		pointers[_expAnd.size()-1]+=1;
	  		for (i=_expAnd.size()-1;i >0;i--)
	  		{
		  		FheIntVar var = (FheIntVar) _expAnd.get(i);
	  			if (pointers[i] >= var.size())
	  			{
	  				pointers[i]=0;
	  				pointers[i-1]+=1;
	  			}
	  			else
	  				i = 0;
	  		}
	  		
	  		FheIntVar var = (FheIntVar) _expAnd.get(0);
	  		if (pointers[0] >= var.size())
	  		{
	  			end = true;
	  		}	  		
	  	}
	}
	
	public boolean propagateLowerEqual()
	{
		int [] pointers;
		int [] positive;
		int i,j;
		boolean fail = false;

		_pruned = false;
	  		
		pointers = new int[_expAnd.size()];
		positive = new int[_expAnd.size()];

	  	for (i=0; i< _expAnd.size();i++)
	  	{
	  		FheIntVar var = (FheIntVar) _expAnd.get(i);
	  		Long mult = (Long) _mult.get(i);
	  		//System.out.println(mult.getName());
	  		if (mult.longValue() >=0)
	  		{
	  			pointers[i] = 0;
	  			positive[i] = 1;
	  		}
	  		else
	  		{
	  			pointers[i] = var.size() - 1;
	  			positive[i] = -1;
	  		}
	  	}

	  	j = 0;
	  	
	  	while(j < _expAnd.size())
	  	{
	  		FheIntVar var = (FheIntVar) _expAnd.get(j);
	  		if (positive[j]==1)
	  			pointers[j] = var.size() - 1;
	  		else
	  			pointers[j]=0;
	  		
	  		long val = 0;
	  		for (i=0; i< _expAnd.size();i++)
		  	{
	  			FheIntVar var2 = (FheIntVar) _expAnd.get(i);
		  		Long mult2 = (Long) _mult.get(i);
		  		val += var2.getValueAt(pointers[i]) * mult2.longValue();
		  	}
	  		if (val > _val)
	  		{
	  			fail = var.removeAt(pointers[j]);
  				_m.addPropagatedExp(var);
	  			if (fail == true)
	  				return fail;
	  			_pruned = true;
	  			if(positive[j]==1)
	  				pointers[j]-=1;
	  			j-=1;
	  		}
	  		else
	  		{
	  			if (positive[j]==1)
	  				pointers[j]=0;
	  			else
	  				pointers[j] = var.size() - 1;
	  		}
	  		j+=1;
	  	}
	  	return fail;
	}
	
	public void printAllValues()
	{
	  	for (Iterator i=_expAnd.iterator(); i.hasNext(); )
	  	{
	  		FheIntVar var = (FheIntVar) i.next();
	  	    var.printExp();
	  	}
	}
	
	public void printAllBounds()
	{
	  	for (Iterator i=_expAnd.iterator(); i.hasNext(); )
	  	{
	  		FheIntVar var = (FheIntVar) i.next();
	  	    var.printBounds();
	  	}
	}
	
	public void setVal(long val)
	{
		_val = val;
	}
	public void relaxExp()
	{
		_val++;
	}

}
