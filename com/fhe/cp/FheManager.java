 /*
 * Created on 22 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.cp;
import java.util.*;

import com.fhe.woshe.*;
import com.fdh.sudoku.*;
import com.fdh.ressplan.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheManager {
	private ArrayList _expAnd;			   // List of constraints that must always be satisfied
	private ArrayList _expOr;			   // List of Or Expression
	private ArrayList _expEventDiff;

	private ArrayList _varAtChoicePoints;  // List of FheVariables
	private FheVariables _vars;            // List of the FheIntVar considered in this manager
	private FheVariables _solutionVars;
	private int _idVar,_idExpAnd,_idExpOr;
	private long _failNb,_nbSauv,_nbRest,_nbChoicePoint;
	private int _nbSolutions;
	private int _searchOption;
	
	private FheIntVar _optimize, _solutionOptimize;
	private long _currentOptimizeVal,_saveCurrentOptimizeVal;
	private FheIntExp _optimizationExp;
	private FheTeamWork _team;
	private FheBusinessWorkload _business;
	private FdhReadSudoku _sudoku;
	private FdhReadTasks _theTasks;
	private FdhReadRessources _theRessources;
	private FdhReadExecTasks _theExecTasks;
	private ArrayList _expList;
	private boolean _newSubOptimize;
	private final int _maxBack = 10;
	private boolean _debug;

	public FheManager()
	{
		this._expAnd = new ArrayList();
		this._expOr = new ArrayList();
		this._expEventDiff = new ArrayList();
//		this._expEventIfThenOr = new ArrayList();
//		this._expEventIfThenAnd = new ArrayList();
		this._varAtChoicePoints = new ArrayList();
		this._vars = new FheVariables(-1);
		this._solutionVars = new FheVariables(-1);
		_idVar = 0;
		_idExpAnd = 0;
		_idExpOr = 0;
		_failNb = 0;
		_nbSauv = 0;
		_nbChoicePoint = 0;
		_nbRest = 0;
		_nbSolutions = 0;
		_expList = new ArrayList();
		_currentOptimizeVal = 0;
		_saveCurrentOptimizeVal = 0;
		_newSubOptimize = true;
	}
	
	public void addPropagatedExp(FheIntVar var)
	{	
		for(int i=0;i<var.getExpressionSize();i++)
			if(!_expList.contains(var.getExpression(i)))
				_expList.add(var.getExpression(i));
	}
	
	public void setCurrentOptimizeVal()
	{
		_currentOptimizeVal = _saveCurrentOptimizeVal;
		_newSubOptimize = true;
	}
	
	public void initPropagateExp(FheIntExp exp)
	{
		_expList.add(exp);
	}
	
	public void initPropagate()
	{
		_expList.addAll(_expAnd);
	}
	
	public FheIntVar oneVar()
	{
		return (FheIntVar) _vars.get(0);
	}
		
	public void checkVar()
	{
		int i;
		
		for(i = 0; i < _vars.size();i++)
		{
			FheIntVar var = (FheIntVar)_vars.get(i);
			if (var.size() == 0)
				System.out.println("fail non détecté sur la variable "+ var.getName());
		}
	}
	public void addOptimization(FheIntExp optimizationExp)
	{
		_optimizationExp = optimizationExp;
	}
	public void saveOptimizeVar(FheIntVar optimize)
	{
		_optimize = optimize;
		_solutionOptimize = new FheIntVar(_optimize);
	}
	
	public void restaureOptimizeVar()
	{
		_optimize = _solutionOptimize;
	}
	public void addBusiness(FheBusinessWorkload business)
	{
		_business = business;
	}
	
	public void addSudoku(FdhReadSudoku sudoku )
	{
		_sudoku = sudoku;
	}

	public void addTheTasks(FdhReadTasks theTasks )
	{
		_theTasks = theTasks;
	}

	public void addTheExecTasks(FdhReadExecTasks theTasks )
	{
		_theExecTasks = theTasks;
	}

	public void addTheRessources(FdhReadRessources theRessources )
	{
		_theRessources = theRessources;
	}

	public void addteam(FheTeamWork team)
	{
		_team = team;
	}
	
	public void reset()
	{
		_vars.reset();
		
		int i;
		for(i = 0; i < _varAtChoicePoints.size() ;i++)
		{
			FheVariables vars = (FheVariables) _varAtChoicePoints.get(i);
			vars.reset();
		}
		_varAtChoicePoints.clear();

		_expAnd.clear();
		_expOr.clear();
		_expEventDiff.clear();
		
		_idVar = 0;
		_idExpAnd = 0;
		_idExpOr = 0;
		_failNb = 0;
		_nbSolutions = 0;
		_expList.clear();
		this.initPropagate();
	}
	
	public void resetNbFail()
	{
		_failNb = 0;
	}
	
	public long getFailNb()
	{
		return _failNb;
	}
	public int getNbSolutions()
	{
		return _nbSolutions;
	}
	public long getNbChoicePoint()
	{
		return _nbChoicePoint;
	}
	public void addExpAnd(FheIntExp Exp)
	{
		_expAnd.add(Exp);	
		_idExpAnd+=1;
		Exp.setId(_idExpAnd);
	}

	public void addDefiniteExpAnd(FheIntExp Exp)
	{
		_expAnd.add(Exp);
		_expList.add(Exp);
		Exp.setId(0);
	}

	public void addExpEventDiff(FheIntExp Exp)
	{
		_expEventDiff.add(Exp);	
	}

	public void addExpOr(FheIntExp Exp1, FheIntExp Exp2)
	{
		FheExpOr ExpOr = new FheExpOr(Exp1,Exp2);
		_idExpOr += 1;
		_expOr.add(ExpOr);
		ExpOr.setId(_idExpOr);
	}
	
	public void addExpOr(FheExpOr ExpOr)
	{
		_idExpOr += 1;
		_expOr.add(ExpOr);	
	}
	
	public void addVar(FheIntVar Var)
	{
		Var.setId(_idVar);
		_idVar+=1;
		_vars.add(Var);
		_solutionVars.add(Var);
	}
	
	public void addVarAtChoicePoint(int number)
	{
		Collections.sort(this._vars,ID_ORDER);
		FheVariables vars = new FheVariables(number,_idExpAnd, _idExpOr,_vars);
		_varAtChoicePoints.add(vars);
		if(number == 12)
			_nbSauv++;
		if (_varAtChoicePoints.size()>_maxBack)
			_varAtChoicePoints.remove(0);
	}
	
	public void savePartialSolution()
	{
		_solutionVars = new FheVariables(-1,-1, -1,_vars);
		_saveCurrentOptimizeVal = _optimize.getFirst();
		
		_newSubOptimize = false;
	}
	public void printVar()
	{
		_vars.print();
	}
	public void restoreVarAtLastChoicePoint()
	{
		int i;
		int currentExp;
		
		Collections.sort(this._vars,ID_ORDER);
		FheVariables var = (FheVariables) _varAtChoicePoints.remove(_varAtChoicePoints.size()-1);
		if(var.getChoiceNumber() ==12)
			System.out.println("BUG BUG");
		_vars.restore(var);
		
//	    _vars.print() ;
		
		i=_expAnd.size()-1;
		if(i>=0)
		{
			while(((FheIntExp)_expAnd.get(i)).getId()>var.getCurrentExpAnd())
			{
				_expAnd.remove(i);
				i--;
			}
		}
		_idExpAnd = var.getCurrentExpAnd()+1;

		i=_expOr.size()-1;
		if(i>=0)
		{
			while(((FheExpOr)_expOr.get(i)).getId()>var.getCurrentExpOr())
			{
				_expOr.remove(i);
				i--;
			}
		}
		_idExpOr = var.getCurrentExpOr()+1;
		
//		_expList.clear();
	}

	public boolean restoreVarAtLabelChoicePoint(int number)
	{
		int i,ii;
		int currentExp;
		
		Collections.sort(this._vars,ID_ORDER);
		
		i=_varAtChoicePoints.size()-1;
		ii=i;

		FheVariables var = null;
		
		while(i>=0)
		{
			var = (FheVariables) _varAtChoicePoints.remove(i);
			if (var.getChoiceNumber() == number)
			{
//			 	System.out.println("restoration du "+i+"ième");
				i=-10;
			}
			else
				i--;
		}
		_nbRest++;
	 	if (i==-1)
	 	{
	 		System.out.println("maxBack trop petit......");
	 		return true;
	 	}

	 	_vars.restore(var);
	 					
		i=_expAnd.size()-1;
		if(i>=0)
		{
			while(((FheIntExp)_expAnd.get(i)).getId()>var.getCurrentExpAnd())
			{
				_expAnd.remove(i);
				i--;
			}
		}
		
		_idExpAnd = var.getCurrentExpAnd()+1;

		i=_expOr.size()-1;
		if(i>=0)
		{
			while(((FheExpOr)_expOr.get(i)).getId()>var.getCurrentExpOr())
			{
				_expOr.remove(i);
				i--;
			}
		}
		_idExpOr = var.getCurrentExpOr()+1;
		
//		_expList.clear();
		
		return false;
	}

	public boolean restorePartialSolution()
	{
	 	_vars.restore(_solutionVars);
	 	
	 	int i;
	 	FdhExecTask task;
	 	for(i=0;i<_theExecTasks.getTaskSize();i++)
	 	{
			task = _theExecTasks.getTask(i);
			if (task.getSubOptimized()<2)
				task.reBuildRelax();
	 	}
	 	
	 	_optimize.reBuild();
	 	
	 	return false;
	}

	public boolean restoreInitialState()
	{
		int i;
		int currentExp;		

		_nbRest=0;
		_failNb=0;

		_expList.clear();
		for(i=0;i<_expAnd.size();i++)
			_expList.add(_expAnd.get(i));

		if (_varAtChoicePoints.size()==0)
			return false;
		Collections.sort(this._vars,ID_ORDER);

		FheVariables var = null;
		
		var = (FheVariables) _varAtChoicePoints.remove(0);

		for(i = 0; i < _varAtChoicePoints.size() ;i++)
		{
			FheVariables vars = (FheVariables) _varAtChoicePoints.get(i);
			vars.reset();
		}
		
		_varAtChoicePoints.clear();

	 	_vars.restore(var);
	 					
		i=_expAnd.size()-1;
		if(i>=0)
		{
			while(((FheIntExp)_expAnd.get(i)).getId()>var.getCurrentExpAnd())
			{
				_expAnd.remove(i);
				i--;
			}
		}
		
		_idExpAnd = var.getCurrentExpAnd()+1;

		i=_expOr.size()-1;
		if(i>=0)
		{
			while(((FheExpOr)_expOr.get(i)).getId()>var.getCurrentExpOr())
			{
				_expOr.remove(i);
				i--;
			}
		}
		_idExpOr = var.getCurrentExpOr()+1;
				
		return false;
	}

	public boolean propagateSimple()
	{
		boolean pruned;
		pruned=true;
	
	  	while(_expList.size()>0)
	  	{
//	  		if (_debug)
//	  			pruned = true;
	  	    FheIntExp exp = (FheIntExp) _expList.get(0);
	  	    if(exp.propagateLowerEqual()==true)
	  	    {
	  	    	_expList.clear();
	  	    	return true;
	  	    }
	  	    _expList.remove(0);
	  	}
		
/*		while(pruned == true)
		{
			pruned = false;
		  	for (int i=0;i<_expAnd.size(); i++)
		  	{
		  	    FheIntExp exp = (FheIntExp) _expAnd.get(i);
		  	    if(exp.propagateLowerEqual()==true)
		  	    	return true;
		  	    if(exp.isPruned()==true)
		  	    {
		  	    	pruned = true;
//		  	    System.out.println("pruned="+pruned);
//		  	    exp.printAllBounds();
		  	    }
		  	}
		}			
*/		
		return false;
	}

	public boolean SearchFirst(FheIntVar var)
	{
		boolean fail;
		
		fail = this.propagate();
		if (fail == true)
			return true;
		if(var.size() ==1)
			return false;
		this.addVarAtChoicePoint(0);
		var.setFirst();
		while(this.SearchFirst(var)==true)
		{
			this.restoreVarAtLastChoicePoint();
			if(var.removeFirst()==true)
				return true;
			fail=this.propagate();
			if(fail==true)
				return true;
			this.addVarAtChoicePoint(0);		
		}
		return false;
	}
	
	public boolean SearchLast(FheIntVar var)
	{
		boolean fail;
		
		fail = this.propagate();
		if (fail == true)
			return true;
		if(var.size() ==1)
			return false;
		this.addVarAtChoicePoint(0);
		var.setLast();
		while(this.SearchLast(var)==true)
		{
			this.restoreVarAtLastChoicePoint();
			if(var.removeLast()==true)
				return true;
			fail=this.propagate();
			if(fail==true)
				return true;
			this.addVarAtChoicePoint(0);		
		}
		return false;
	}
	

	public int VarChoice()
	{
		boolean end=false;
		int i;
//		_vars.print() ;
		
		if (_vars.size() == 0)
			return -1;
	
		Collections.sort(this._vars,CHOICE);

		i = 0;
		while(i <= _vars.size()-1 && end == false)
		{
			FheIntVar var = (FheIntVar) _vars.get(i);
			if(var.getChoice()<=3)
			{
				if (var.size() > 1)
					end = true;
				else
					i++;
			}
			else
				i++;
		}
		
		if (i <= _vars.size()-1)
			return i;
		
//		FheIntVar var = (FheIntVar) _vars.get(0);
		
//		if (var.size() == 1)
//			return true;

		return -1;
	}
	
	public int VarChoicePole()
	{
		boolean end=false;
		int i;
//		_vars.print() ;
		
		if (_vars.size() == 0)
			return -1;
	
		Collections.sort(this._vars,CHOICE);

		i = 0;
		while(i <= _vars.size()-1 && end == false)
		{
			FheIntVar var = (FheIntVar) _vars.get(i);
			if(var.getChoice()==0)
			{
				if (var.size() > 1)
					end = true;
				else
					i++;
			}
			else
				i++;
		}
		
		if (i <= _vars.size()-1)
			return i;
		
//		FheIntVar var = (FheIntVar) _vars.get(0);
		
//		if (var.size() == 1)
//			return true;

		return -1;
	}
	
	public int VarChoiceDayStartEnd()
	{
//		_vars.print() ;
		int i;
		
		i = 0;
		
		while(i<_vars.size())
		{
			FheIntVar var = (FheIntVar) _vars.get( i);
			if(var.getChoice() >= 10)
			{
				if(var.size() > 1)
				{
					return i;
				}
			}
			i++;
		}
		
		return -1;
	}

	public boolean propagate()
	{
		for(int i=0;i<_vars.size();i++)
		{
			FheIntVar var = (FheIntVar) _vars.get(i);
			if (var.size()==0)
				return true;
		}
		if (this.propagateEvent()== true)
			return true;
		else
		{
			if (this.propagateSimple() == true)
				return true;
			else
			{
				if(this.propagateOr() == true)
					return true;
			}
		}
		
		return false;
	}
	
	public boolean propagateEvent()
	{
		boolean fail=false, end = false;
		int i,j,k,l;
		long value,nbVar;
		
		while(end == false)
		{
		end = true;
		for(i = 0; i< _expEventDiff.size();i++)
		{
			FheIntExp exp = (FheIntExp)_expEventDiff.get(i);
			nbVar = exp.getSize();

			for(j=0;j<nbVar;j++)
			{
				FheIntVar varj = (FheIntVar) ((FheIntExp)_expEventDiff.get(i)).getVar(j);
				if (varj.size() == 1)
				{
					value = varj.getFirst();
					for(k=0;k<nbVar;k++)
					{
						if (k!=j)
						{
    						FheIntVar vark = (FheIntVar) ((FheIntExp)_expEventDiff.get(i)).getVar(k);
    						if (vark.size()==1)
							{
								if (vark.getFirst() == value)
									return true;
							}
    						else
    						{
    							l=0;
    							while(l < vark.size() && vark.getValueAt(l)< value)
    								l++;
    							if(l < vark.size() && vark.getValueAt(l)==value)
    							{
    								vark.removeAt(l);
    								if (vark.size()==1)
    									end = false;
    							}
    						}
						}
					}
				}
//				System.out.println(var.getName());
			}

		}
		}
		
		return false;
	}
	
	public boolean propagateOr()
	{
		boolean fail=false;
		int i;
		
		for(i = 0; i< _expOr.size();i++)
		{
			FheExpOr ExpOr = (FheExpOr) _expOr.get(i);
			fail = tryExp(ExpOr.getExp1());
			if (fail==true)
			{
				fail = tryExp(ExpOr.getExp2());
				if (fail == true)
					return true;
				else
					this.addExpAnd(ExpOr.getExp2());
			}
			else
			{
				fail = tryExp(ExpOr.getExp2());
				if (fail == true)
				{
					fail = tryExp(ExpOr.getExp1());
					if (fail == true)
					{
						return true;
					}
					else
						this.addExpAnd(ExpOr.getExp1());
				}					
			}
		}
		
		return this.propagateSimple();
	}
	
	private boolean tryExp(FheIntExp Exp)
	{
		boolean fail;
		
		_expAnd.add(Exp);
			
		this.addVarAtChoicePoint(0);
		
		fail = propagate();
		
		this.restoreVarAtLastChoicePoint();

		_expAnd.remove(Exp);

		return fail;
	}
	
	public boolean newSolution()
	{
		int i,j;
		boolean equal;
		
		for(j = 0; j <_nbSolutions; j++)
		{
			equal = true;
			for(i = 0;i < _vars.size() ;i++)
			{
				FheIntVar var = (FheIntVar)_vars.get(i);
				long val1 = var.getSolution(j);
				long val2 = var.getFirst();
				if(var.getSolution(j)!=var.getFirst())
				{
					equal = false;
					i = _vars.size();
				}
			}
			if (equal == true)
				return false;
		}
		
		return true;
	}
	
	public boolean propagateAnd()
	{
		int i,down,up;
		boolean fail,end;
		
		fail = this.propagateSimple();
		
		if (fail == true)
			return true;

	  	for (int ie=0;ie<_expAnd.size(); ie++ )
	  	{
	  		FheIntExp exp = (FheIntExp) _expAnd.get(ie);
	  		for(i=0;i<exp.getSize();i++)
	  		{
	  			FheIntVar var = exp.getVar(i);

	  			long mult = exp.getMult(i);

	  			if(mult>0)
	  			{
		  			up = var.size()-1;
		  			down = 1;
		  			do
		  			{
		  				end = false;
		  				this.addVarAtChoicePoint(0);
		  				var.setAt(up);
		  				fail=this.propagateSimple();
			  			this.restoreVarAtLastChoicePoint();
			  			if(fail==true)
			  			{
			  				if (var.size() == 1)
			  					return true;
			  				var.removeAfter(up);
			  				up = down+(up-down)/2;
			  				if (up == down)
			  					end = true;
			  				else
			  				{
				  				if (up - 1 <=down)
				  					up = down;
			  				}
			  			}
			  			else
			  			{
			  				if (up == var.size()-1)
			  					end=true;
			  				else
			  				{
			  					down = up;
								up = var.size()-1;
			  				}
			  			}
		  			}
		  			while(end==false);
	  			}
	  			else
	  			{
		  			do
		  			{
			  			up = var.size()-2;
			  			down = 0;
		  				end = false;
		  				this.addVarAtChoicePoint(0);
		  				var.setAt(down);
	  					fail=this.propagateSimple();
			  			this.restoreVarAtLastChoicePoint();
			  			if(fail==true)
			  			{
			  				if (var.size() == 1)
			  					return true;
			  				var.removeBefore(down);
			  				down = up-(up-down)/2;
			  				if (up == down)
			  					end = true;
			  				else
			  				{
				  				if (up - 1 <=down)
				  					up = down;
			  				}
			  			}
			  			else
			  			{
			  				if (down == 0)
			  					end=true;
			  				else
			  				{
			  					up = down;
								down = 0;
			  				}
			  			}
		  			}
		  			while(end==false);
	  			}
	  		}
		}
	  	return fail;
	}
	public boolean SearchMinSizeMinVal()
	{
		boolean fail,end;
		int index;
		long val;
		FheIntVar var;
		
		fail = this.propagate();
		_vars.print() ;
		
		if(fail == true)
			return fail;

		_nbChoicePoint +=1;
		
//		System.out.println("Choix ok");		
				
		index = this.VarChoiceMinSize() ;
		
		if (index == -1)
			return false;
		
		var = (FheIntVar) _vars.get(index);
				
		this.addVarAtChoicePoint(12);
		
		var.setFirst();
				
//		System.out.println("Point de choix sur la variable "+var.getName()+" valeur "+var.getFirst());
		
		while(this.SearchMinSizeMinVal() == true)
		{
			_failNb+=1;
//			System.out.println("Fail sur point de choix, variable "+var.getName()+" valeur "+var.getFirst());
			end = this.restoreVarAtLabelChoicePoint(12);
//			person.printDurations() ;
//			_vars.print() ;
//			if (end == true)
//				return false;
			
//			var.printBounds();
			if(var.removeFirst() == true)
				return true;				
			
			fail = this.propagate() ;
			
			if(fail == true)
				return true;

			this.addVarAtChoicePoint(12);
		}

		return false;
	}
	

	public boolean PropagateStartTimes(FheIntVar TaskStartVar)
	{
		int i;
		FdhTask task = TaskStartVar.getTask();
		if(_theRessources.updateASAPRessource(task))
			return true;
		task.setScheduledDate();
		
		for(i=0;i<_theTasks.getSize();i++)
		{
			task = _theTasks.getTask(i);
			if(!task.isScheduled())
			{
				if (task.setMinStart(_theRessources))
						return true;
			}
		}		
		return false;
	}
	
	public boolean findSolution()
	{
		boolean fail;
   		this.initPropagate();
   		
		fail = this.propagate();
		if (fail)
			return fail;
		_currentOptimizeVal = _optimize.getFirst();
		
		return this.OptimizeTasksInterval();
	}
	
	public boolean findSolution2()
	{
		boolean fail;
   		this.initPropagate();
   		
		fail = this.propagate();
		if (fail)
			return fail;
		_currentOptimizeVal = _optimize.getFirst();
		
		return this.OptimizeTasksInterval2();
	}
	
	public long VarChoiceRessourceMinStart(FheIntVar var)
	{
		int i;
		FdhTask task = var.getTask();
			
		return _theRessources.ressourceChoiceMinStart(task);
	}

	public void setSearchOption(int option)
	{
		_searchOption = option;
	}
	public int getSearchOption()
	{
		return _searchOption;
	}
	
	public boolean StartTaskASAP(boolean failed)
	{
		boolean fail,end;
		int index;
		long theChoicePointNumber;
		long ressourceVarVal;
		FheIntVar taskVar;
		
		if (failed)
			return true;
		
//		_vars.print() ;

		fail = this.propagate();
		
		if(fail == true)
			return fail;
		
//		System.out.println("Choix ok");		
				
		end = false;
		while(!end)
		{
			end = true;
			index = this.VarChoiceStartTaskMinStart() ;
		
			if (index == -1)
			{
				System.out.println("Solution trouvée en "+this.getFailNb()+" avec "+this.getNbChoicePoint()+" points de choix et un optimum de "+_optimize.getFirst());
				_theTasks.saveTasksSolution();
				_nbSolutions +=1;
				if(_searchOption == 0)
					return false;
				this.addPropagatedExp(_optimize);
				_optimizationExp.setVal(_optimize.getFirst()-1);
//attetion, il faut que cette contrainte soit prise en compte après le backtrack
				this.resetNbFail();
//			_vars.print() ;
//	   		System.out.println(" ");
//	   		System.out.println(" ");
				return true;
			}
			FheIntVar TaskStartVar = (FheIntVar) _vars.get(index);
			FdhTask task = TaskStartVar.getTask();
//			System.out.println("Choix "+task.getTaskName());		
			TaskStartVar=task.getStartDate();
//			System.out.println("start "+TaskStartVar.getFirst());		
			ressourceVarVal = this.VarChoiceRessourceMinStart(TaskStartVar);
			if (ressourceVarVal < 0)
				return true;
			FheIntVar ressourceVar = task.getRessource();
		
			this.addVarAtChoicePoint(12);

			_nbChoicePoint +=1;

			theChoicePointNumber = _nbChoicePoint;
			task.oneChoice();
			ressourceVar.setValue(ressourceVarVal);
			this.addPropagatedExp(ressourceVar);
		
			failed = this.PropagateStartTimes(TaskStartVar);
			
			if(this.StartTaskASAP(failed))
			{
				end = this.restoreVarAtLabelChoicePoint(12);
				failed = this.propagate();
				if (failed)
					return true;
				if (_failNb>1000)
					return true;
				_failNb+=1;
//				System.out.println(_failNb+"ième fail sur la tache "+task.getTaskName());
			
//			_vars.print() ;
//	   		System.out.println(" ");
//	   		System.out.println(" ");
			// enlever la valeur ressourceVarVal à la variable ressourceVar

				this.addPropagatedExp(ressourceVar);
				if(ressourceVar.removeElmt(ressourceVarVal) == true)
					return true;	
				end = false;
			}
		}
		return false;
	}

	public boolean ExecuteTasksInterval()
	{
		boolean fail,end;
		int index;
		int i,j,jvar,diff,theDay;
		FdhExecTask task;
		FheIntVar execVar,var;
		String execName,name;
				
		end = false;
		while(!end)
		{
			execVar = null;
			task = null;
			fail = this.propagate();
			
			if(fail == true)
				return fail;

			end = true;
			
			jvar = -1;
			theDay = -1;
			i = 0;
			while(i<_theExecTasks.getTaskSize())
			{
				task = _theExecTasks.getTask(i);
				
				if (!task.isScheduled())
				{
					if(task.nonScheduledDuration() == 0)
						task.ScheduledTask();
					else
					{
						Collections.sort(task,MAXNONBOUNDEDORDER);
						j = 0;
						name = "is not a task name";
						while(j<task.size())
						{
							execVar = (FheIntVar)task.get(j);
							if(execVar.getRessource().getName()==name)
								j++;
							else
							{
								name = execVar.getRessource().getName();
								if((execVar.size()>1)&&(execVar.getDay()>theDay))
								{
									theDay = execVar.getDay(); 
									execName = name;
									jvar = j;
									i=_theExecTasks.getTaskSize();
								}
							}
						}
					}
				}
				i++;
			}
					
			if (jvar < 0)
			{
				i = 0;
				while(i<_theExecTasks.getTaskSize())
				{
					task = _theExecTasks.getTask(i);
					if(task.nonScheduledDuration() >0)
						System.out.println("La tache "+task.getTaskName()+" n'est pas planifiée de "+task.nonScheduledDuration());
					i++;
				}
				_nbSolutions =1;
				
				System.out.println("Solution trouvée en "+this.getFailNb()+" avec "+this.getNbChoicePoint()+" fails avec "+this.getNbChoicePoint()+" points de choix");
				return false;
			}
			else
				end = false;

			_nbChoicePoint +=1;
		
			execVar = (FheIntVar)task.get(jvar);
			name = execVar.getRessource().getName();
//			if (name.compareTo("CTA")==0)
//				System.out.println("variable "+execVar.getName()+" sur le jour "+execVar.getDay());
			execVar.setLast();
			this.addPropagatedExp(execVar);
			
			j=jvar+1;
			while(j<task.size())
			{
				fail = task.propagateDurationConstraint();
				if (fail)
					return true;
				var = (FheIntVar) task.get(j);
				if ((var.size()==1)||(var.getRessource().getName()!=name))
					j = task.size();
				else
				{
//					if (name.compareTo("CTA")==0)
//						System.out.println("variable "+var.getName()+" sur le jour "+var.getDay());
					var.setLast();
					this.addPropagatedExp(var);
					j++;
				}
			}
			
		}
		return false;
	}

	public int chooseTask()
	{
		/*
		 * Sélectionne une tâche 
		 *  - qui n'est pas encore planifiée
		 *  et dont
		 *  - la variable d'ordre de planification est la plus petite (toutes ces variables ont le même domaine au début)
		 *  - la date de fin possible est la plus petite
		 */
		boolean fail,end;
		int index;
		int i,j,ivar,diff,theDay,duration;
		long theOrder,theEnd;
		FdhExecTask task;
		FheIntVar execVar,var;
		String execName,name;
		
		i = 0;
		theOrder = _theExecTasks.getTaskSize();
		theEnd = _theExecTasks.getHorizon();
		ivar = -1;
		while(i<_theExecTasks.getTaskSize())
		{
			task = _theExecTasks.getTask(i);
			if (!task.isScheduled())
			{
				if(task.getScheduledOrder().getFirst() <= theOrder)
				{
					if(task.getScheduledOrder().getFirst() < theOrder || task.getTaskEnd() < theEnd)
					{
						theOrder = task.getScheduledOrder().getFirst();
						theEnd = task.getTaskEnd();
						ivar = i;
					}
				}
			}
			i++;
		}
		
		return ivar;
	}
	
	public int chooseTaskSubOptimize()
	{
		boolean fail,end;
		int index;
		int i,j,ivar,diff,theDay,duration;
		long theOrder,theEnd;
		FdhExecTask task;
		FheIntVar execVar,var;
		String execName,name;
		
		i = 0;
		theOrder = _theExecTasks.getTaskSize();
		theEnd = _theExecTasks.getHorizon();
		ivar = -1;
		while(i<_theExecTasks.getTaskSize())
		{
			task = _theExecTasks.getTask(i);
			if (!task.isScheduled() && task.getSubOptimized()==2)
			{
				if(task.getScheduledOrder().getFirst() <= theOrder)
				{
					if(task.getScheduledOrder().getFirst() < theOrder || task.getTaskEnd() < theEnd)
					{
						theOrder = task.getScheduledOrder().getFirst();
						theEnd = task.getTaskEnd();
						ivar = i;
					}
				}
			}
			i++;
		}
		
		return ivar;
	}

	public void defineSubOptimization()
	{
		int i;
		long theOrder;
		FdhExecTask task;
		FheIntExp exp;
/*
 * Définir un sous ensemble de tâches pour tenter de minimiser la relaxation
 * A chaque tâche est associée une variable d'ordre de planification. La valeur de cette variable
 * est différente pour chaque tâche.
 */
/*
 * Rechercher parmi toutes les tâches planifiées le plus grand ordre
 */
		i = 0;
		theOrder = -1;
		while(i<_theExecTasks.getTaskSize())
		{
			task = _theExecTasks.getTask(i);
			if(task.getSubOptimized()==1)
			{
				if(task.getScheduledOrder().getFirst() >= theOrder)
					theOrder = task.getScheduledOrder().getFirst();
			}
			i++;
		}
/*
 * ajouter une contrainte pour chaque tâche une contrainte imposant d'être planifiée dans le sous problème
 * (l'ordre de planfication de la tâche doit rester inférieur au plus grand ordre parmi toutes les taches planifiées)
 */
		i = 0;
		while(i<_theExecTasks.getTaskSize())
		{
			task = _theExecTasks.getTask(i);
			if(task.getSubOptimized()==1)
			{
				exp = new FheIntExp(this,theOrder);
				exp.add(1,task.getScheduledOrder());
				this.addDefiniteExpAnd(exp);
				task.setSubOptimized(2);
			}
			i++;
		}		
	}

	public void defineSubOptimization2()
	{
		int i;
		long theOrder;
		FdhTask task;
		FheIntExp exp;
/*
 * Définir un sous ensemble de tâches pour tenter de minimiser la relaxation
 * A chaque tâche est associée une variable d'ordre de planification. La valeur de cette variable
 * est différente pour chaque tâche.
 */
/*
 * Rechercher parmi toutes les tâches planifiées le plus grand ordre
 */
		i = 0;
		theOrder = -1;
		while(i<_theTasks.getSize())
		{
			task = _theTasks.getTask(i);
			if(task.getSubOptimized()==1)
			{
				if(task.getScheduledOrder().getFirst() >= theOrder)
					theOrder = task.getScheduledOrder().getFirst();
			}
			i++;
		}
/*
 * ajouter une contrainte pour chaque tâche une contrainte imposant d'être planifiée dans le sous problème
 * (l'ordre de planfication de la tâche doit rester inférieur au plus grand ordre parmi toutes les taches planifiées)
 */
		i = 0;
		while(i<_theExecTasks.getTaskSize())
		{
			task = _theTasks.getTask(i);
			if(task.getSubOptimized()==1)
			{
				exp = new FheIntExp(this,theOrder);
				exp.add(1,task.getScheduledOrder());
				this.addDefiniteExpAnd(exp);
				task.setSubOptimized(2);
			}
			i++;
		}		
	}

	public int chooseTaskExecVar(FdhExecTask task)
	{
/*
 *   Pour une tâche donnée, retourne la variable [0,1] correspondant à la ressource avec :
 * 	 - la disponibilité le plus tôt
 *   - en cas de disponibilité égale, la première ressource par ordre de priorité de compétence  
 */		
		int j, jvar, theDay;
		FheIntVar execVar;
		String execName="",name;
		Collections.sort(task,MAXNONBOUNDEDORDER);
		j = 0;
		jvar = -1;
		theDay = (int)_theExecTasks.getHorizon();
		name = "is not a task name";
		while(j<task.size())
		{
			execVar = (FheIntVar)task.get(j);
//			System.out.println("exectask : jour "+execVar.getDay()+" - Nom "+execVar.getRessource().getName()+" - size "+execVar.size());
			if(execVar.getRessource().getName()==name)
				j++;
			else
			{
				name = execVar.getRessource().getName();
				if((execVar.size()>1)&&(execVar.getDay()< theDay))
				{
					theDay = execVar.getDay(); 
					execName = name;
					jvar = j;
				}
			}
		}
		
//		System.out.println("Choix fait : jour "+theDay+" - Nom "+ execName +" - jvar "+jvar);
		return jvar;
	}

	public boolean OptimizeTasksInterval()
	{
		boolean fail,end;
		int index,i,j;
		FdhExecTask task;
		FheIntVar execVar,var;
		String name;
				
		end = false;
		while(!end)
		{
			execVar = null;
			task = null;
			fail = this.propagate();
			
			if(fail == true)
				return fail;

			end = true;
			
			if(_newSubOptimize && _optimize.getFirst() > _currentOptimizeVal )
			{				
				System.out.println("Solution partielle dégradée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
				this.savePartialSolution();
				this.defineSubOptimization();
				this.addPropagatedExp(_optimize);
				_optimizationExp.setVal(_optimize.getFirst()-1);
				return true;
			}
			
			if(_newSubOptimize)
				index = this.chooseTask();
			else
			{
				index = this.chooseTaskSubOptimize();
				if (index == -1)
				{
					if(_optimize.getFirst() > _currentOptimizeVal )
					{
						System.out.println("Solution partielle améliorée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
						this.savePartialSolution();
						this.defineSubOptimization();
						this.addPropagatedExp(_optimize);
						_optimizationExp.setVal(_optimize.getFirst()-1);
						return true;
					}
					else
					{
						System.out.println("Solution partielle retrouvée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
						this.savePartialSolution();
						_newSubOptimize = true;
						this.addPropagatedExp(_optimize);
						_optimizationExp.setVal(_theExecTasks.getHorizon());
						index = this.chooseTask();
						this.resetNbFail();
					}
				}
			}

			if(index < 0)
			{				
				System.out.println("Solution trouvée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
				for(i=0;i<_theExecTasks.getTaskSize();i++)
				{
					task = _theExecTasks.getTask(i);
					if(task.getNonScheduledDuration().getFirst()>0)
						System.out.println(" tache "+task.getTaskName()+ " a une durée non planifiée de "+ task.getNonScheduledDuration().getFirst() );
				}
				_nbSolutions =1;
				_theExecTasks.storeExecTasksSolution(_theRessources);
				return false;
			}

			
			task = _theExecTasks.getTask(index);

			if(!_newSubOptimize && task.getSubOptimized() < 2)
			{
				index = -1;
			}

			index = this.chooseTaskExecVar(task);
			if (index >=0 )
			{
				this.addVarAtChoicePoint(12);

				_nbChoicePoint +=1;
//				while(index >=0)
//				{
					execVar = (FheIntVar)task.get(index);
//				System.out.println("point de choix sur variable "+execVar.getName()+" sur le jour "+execVar.getDay());
					name = execVar.getRessource().getName();
					execVar.setLast();
					this.addPropagatedExp(execVar);
				
					j=index+1;
					while(j<task.size())
					{
						fail = task.propagateDurationConstraint();
						if (fail)
							return true;
						var = (FheIntVar) task.get(j);
						if (var.size()==1)
							j = task.size();
						else
						{
							if (var.getRessource().getName()!=name)
								var.setFirst();
							else
								var.setLast();
							this.addPropagatedExp(var);
							j++;
						}
					}
//					index = this.chooseTaskExecVar(task);
//				}
				
				task.ScheduledTask();

				this.addPropagatedExp(task.getScheduledOrder());
				task.getScheduledOrder().setFirst();

//				System.out.println(" tache "+task.getTaskName()+ " a une durée non planifiée de "+ task.nonScheduledDuration() );
//				task.getScheduledOrder().printBounds();

				if(this.OptimizeTasksInterval())
				{
					end = this.restoreVarAtLabelChoicePoint(12);
					if (end)
					{
						if (_failNb < 1000)
							System.out.println(" BUG BUG BUG BUG BUG BUG "); 
						return true;
					}
					if (_failNb>1000)
					{
						return true;
					}
					_failNb+=1;
//					_debug = true;
					fail = this.propagate();
					if (fail)
						return true;
//					_debug = false;

//					System.out.println("backtrack sur tache "+task.getTaskName()+" scheduled order "+task.getScheduledOrder().getFirst());
					this.addPropagatedExp(task.getScheduledOrder());
					if(task.getScheduledOrder().removeFirst() == true)
						return true;	
					end = false;
				}
			}
			else
			{
				end = false;
				task.ScheduledTask();
				task.getScheduledOrder().setFirst();
			}
		}
		return false;
	}
	
	public boolean OptimizeTasksInterval2()
	{
		boolean fail,end;
		int index,i,j;
		FdhExecTask task;
		FheIntVar execVar,var;
		String name;
				
		end = false;
		while(!end)
		{
			execVar = null;
			task = null;
			fail = this.propagate();
			
			if(fail == true)
				return fail;

			end = true;
			
			if(_newSubOptimize && _optimize.getFirst() > _currentOptimizeVal )
			{				
				System.out.println("Solution partielle dégradée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
				this.savePartialSolution();
				this.defineSubOptimization();
				this.addPropagatedExp(_optimize);
				_optimizationExp.setVal(_optimize.getFirst()-1);
				return true;
			}
			
			if(_newSubOptimize)
				index = this.chooseTask();
			else
			{
				index = this.chooseTaskSubOptimize();
				if (index == -1)
				{
					if(_optimize.getFirst() > _currentOptimizeVal )
					{
						System.out.println("Solution partielle améliorée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
						this.savePartialSolution();
						this.defineSubOptimization();
						this.addPropagatedExp(_optimize);
						_optimizationExp.setVal(_optimize.getFirst()-1);
						return true;
					}
					else
					{
						System.out.println("Solution partielle retrouvée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
						this.savePartialSolution();
						_newSubOptimize = true;
						this.addPropagatedExp(_optimize);
						_optimizationExp.setVal(_theExecTasks.getHorizon());
						index = this.chooseTask();
						this.resetNbFail();
					}
				}
			}

			if(index < 0)
			{				
				System.out.println("Solution trouvée en "+this.getFailNb()+" fails avec une durée non planifiée de "+_optimize.getFirst());
				for(i=0;i<_theExecTasks.getTaskSize();i++)
				{
					task = _theExecTasks.getTask(i);
					if(task.getNonScheduledDuration().getFirst()>0)
						System.out.println(" tache "+task.getTaskName()+ " a une durée non planifiée de "+ task.getNonScheduledDuration().getFirst() );
				}
				_nbSolutions =1;
				_theExecTasks.storeExecTasksSolution(_theRessources);
				return false;
			}

			
			task = _theExecTasks.getTask(index);

			if(!_newSubOptimize && task.getSubOptimized() < 2)
			{
				index = -1;
			}

			index = this.chooseTaskExecVar(task);
			if (index >=0 )
			{
				this.addVarAtChoicePoint(12);

				_nbChoicePoint +=1;
				while(index >=0)
				{
					execVar = (FheIntVar)task.get(index);
//				System.out.println("point de choix sur variable "+execVar.getName()+" sur le jour "+execVar.getDay());
					name = execVar.getRessource().getName();
					execVar.setLast();
					this.addPropagatedExp(execVar);
				
					j=index+1;
					while(j<task.size())
					{
						fail = task.propagateDurationConstraint();
						if (fail)
							return true;
						var = (FheIntVar) task.get(j);
						if ((var.size()==1)||(var.getRessource().getName()!=name))
							j = task.size();
						else
						{
							var.setLast();
							this.addPropagatedExp(var);
							j++;
						}
					}
					index = this.chooseTaskExecVar(task);
				}
				
				task.ScheduledTask();

				this.addPropagatedExp(task.getScheduledOrder());
				task.getScheduledOrder().setFirst();

//				System.out.println(" tache "+task.getTaskName()+ " a une durée non planifiée de "+ task.nonScheduledDuration() );
//				task.getScheduledOrder().printBounds();

				if(this.OptimizeTasksInterval())
				{
					end = this.restoreVarAtLabelChoicePoint(12);
					if (end)
					{
						if (_failNb < 100)
							System.out.println(" BUG BUG BUG BUG BUG BUG "); 
						return true;
					}
					if (_failNb>100)
					{
						return true;
					}
					_failNb+=1;
//					_debug = true;
					fail = this.propagate();
					if (fail)
						return true;
//					_debug = false;

//					System.out.println("backtrack sur tache "+task.getTaskName()+" scheduled order "+task.getScheduledOrder().getFirst());
					this.addPropagatedExp(task.getScheduledOrder());
					if(task.getScheduledOrder().removeFirst() == true)
						return true;	
					end = false;
				}
			}
			else
			{
				end = false;
				task.ScheduledTask();
				task.getScheduledOrder().setFirst();
			}
		}
		return false;
	}

	public boolean checkPostedConstraints()
	{
		boolean fail;
				
		this.addVarAtChoicePoint(12);

		fail = this.propagate();
	
		this.restoreVarAtLabelChoicePoint(12);

		return fail;
	}

	public int VarChoiceMinSize()
	{
		boolean end=false;
		int i;
		
		if (_vars.size() == 0)
			return -1;
	
		Collections.sort(this._vars,CHOICE);

		i = 0;
		while(i <= _vars.size()-1 && end == false)
		{
			FheIntVar var = (FheIntVar) _vars.get(i);
			if (var.size() > 1)
				end = true;
			else
				i++;
		}
		
		if (i <= _vars.size()-1)
			return i;
		
		return -1;
	}
	public int VarChoiceStartTaskMinStart()
	{
		boolean end=false;
		int i;
		
		if (_vars.size() == 0)
			return -1;
	
		Collections.sort(this._vars,CHOICE_ASAP);

		i = 0;
		while(i <= _vars.size()-1 && end == false)
		{
			FheIntVar var = (FheIntVar) _vars.get(i);
			if(var.getChoice()>1)
				i = _vars.size();
			else
			{
				FdhTask task = var.getTask();
				if (!task.isScheduled())
					end = true;
				else
					i++;
			}
		}
		
		if (i <= _vars.size()-1)
			return i;
		
		return -1;
	}

	
    static final Comparator ID_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            return (v1.getId() > v2.getId() ? 1 :
                (v1.getId() == v2.getId() ? 0 : -1));
        }
    };
    
    static final Comparator EXP_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntExp e1 = (FheIntExp) o1;
            FheIntExp e2 = (FheIntExp) o2;
            return (e1.getId() > e2.getId() ? 1 :
                (e1.getId() == e2.getId() ? 0 : -1));
        }
    };

    static final Comparator SIZE_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            if(v1.size() < v2.size())
            	return 1;
            else
            	if (v1.size() == v2.size())
            		return 0;
            	else
            		return 1;
        }
    };
    static final Comparator SIZE_DE_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            if(v1.size() > v2.size())
            	return 1;
            else
            {
            	if (v1.size() == v2.size())
            		return (v1.getFirst() > v2.getFirst() ? 1 :
                      (v1.getFirst() == v2.getFirst() ? 0 : -1));
            	else
            		return -1;
            }
//            return (v1.size() > v2.size() ? 1 :
//                (v1.size() == v2.size() ? 0 : -1));
        }
    };
    static final Comparator CHOICE = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
   		return (v1.size() > v2.size() ? 1 :
            (v1.size() == v2.size() ? 0 : -1));
        }
    };
    
    static final Comparator CHOICE_ASAP = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
            if (v1.getChoice() > v2.getChoice())
            	return 1;
            else if ((v1.getChoice() < v2.getChoice()))
            	return -1;
            else
            	return (v1.getLast() > v2.getLast() ? 1 : 
            		(v1.getLast() == v2.getLast() ? 0 : -1));
        }
    };

    static final Comparator MAXNONBOUNDEDORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            FheIntVar v1 = (FheIntVar) o1;
            FheIntVar v2 = (FheIntVar) o2;
       		if(v1.size() < v2.size())
    			return 1;
    		else
    		{
    			if(v1.size() > v2.size())
    				return -1;
    			else
    			{
    				int diff = (v1.getName()).compareTo(v2.getName());
    				if (diff>0)
    					return 1;
    	            else
    	            {
    	            	if (diff <0)
    	            		return -1;
    	            	else
    	            	{
    	            		if (v1.getDay() > v2.getDay())
                    			return 1;
                    		else
                    		{
                    			if (v1.getDay() < v2.getDay())
                    				return -1;
                    			else
                    				return 0;
                    		}
            			}
            		}
            	}
            }
        }
	};

}

