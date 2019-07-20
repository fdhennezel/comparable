/*
 * Created on 25 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.cp;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FheExpOr {

	private FheIntExp _exp1,_exp2;
	private int _id;

	FheExpOr(FheIntExp Exp1, FheIntExp Exp2)
	{
		_exp1 = Exp1;
		_exp2 = Exp2;
	}
	
	public FheIntExp getExp1()
	{
		return _exp1;
	}
	
	public FheIntExp getExp2()
	{
		return _exp2;
	}
	
	public void setId(int Id)
	{
		_id = Id;
	}
	
	public int getId()
	{
		return _id;
	}
	

}
