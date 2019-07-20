/*
 * Created on 21 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fhe.learn;
import java.util.*;
/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AHJGS extends ArrayList{
	
	  private int size;
	  private int num_units, num_jg, num_period;
	  
	  AHJGS (int Units, int Jgs, int Periods)
	  {
	  	num_units  = Units;
	  	num_jg     = Jgs;
	  	num_period = Periods;
	  	size       = num_units*num_jg*num_period;
	  	
	  	int i_unit,i_jg,i_period;
	  	
	  	i_unit   = 0;
	  	
	  	while(i_unit < num_units)
	  	{
		  	i_jg     = 0;
	  		while(i_jg < num_jg)
	  		{
	  		  	i_period = 0;
	  			while(i_period < num_period)
	  			{
	  				String unit = "unit"+i_unit;
	  				String jg = "jg"+i_jg;
	  				String period="period"+i_period;
	  				AvailableHoursJobGroupStart ahjgs = new AvailableHoursJobGroupStart(unit,jg,period,"AHJGS");
	  				this.add(ahjgs);
	  				i_period++;
	  			}
	  			i_jg++;
	  		}
	  		i_unit++;
	  	}
	 }
	  
	  public void PrintElements()
	  {
	  	for (Iterator i=this.iterator(); i.hasNext(); ) {
	  	    AvailableHoursJobGroupStart elmt = (AvailableHoursJobGroupStart) i.next();
	  	    elmt.PrintVariable();
	  	}
	  }
	  
	  public void sort_for_all_unit_group()
	  {
	  	Collections.sort(this, FOR_ALL_UNIT_GROUP);
	  }
	  
	  static final Comparator FOR_ALL_UNIT_GROUP = new Comparator() {
        public int compare(Object o1, Object o2) {
        	AvailableHoursJobGroupStart s1 = (AvailableHoursJobGroupStart) o1;
        	AvailableHoursJobGroupStart s2 = (AvailableHoursJobGroupStart) o2;
        	int num = s1.Unit().compareTo(s2.Unit());
            return (num == 0 ? s1.Group().compareTo(s2.Group()) : num);
        }
    };

}
