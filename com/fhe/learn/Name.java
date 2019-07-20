/*
 * Created on 17 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.learn;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Name implements Comparable {
	 private String  firstName, lastName;

	    public Name(String firstName, String lastName) {
	        if (firstName==null || lastName==null)
	            throw new NullPointerException();
	        this.firstName = firstName;
	        this.lastName = lastName;
	    }

	    public String firstName()    {return firstName;}
	    public String lastName()     {return lastName;}

	    public boolean equals(Object o) {
	        if (!(o instanceof Name))
	            return false;
	        Name n = (Name)o;
	        return n.firstName.equals(firstName) &&
	               n.lastName.equals(lastName);
	    }

	    public int hashCode() {
	        return 31*firstName.hashCode() + lastName.hashCode();
	    }

	    public String toString() {return firstName + " " + lastName;}

	    public int compareTo(Object o) {
	        Name n = (Name)o;
	        int lastCmp = lastName.compareTo(n.lastName);
	        return (lastCmp!=0 ? lastCmp :
	                firstName.compareTo(n.firstName));
	    }

}
