package gtableIntegration.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Common/src/dynagent/common/utils/Auxiliar.java
public class Auxiliar {
	  public static boolean equals(Object valA , Object valB)
	   {   boolean result = false;
	  
	   	if(valA instanceof Integer[] && valB instanceof Integer[]){
		   valA= Arrays.asList((Integer[])valA);
		   Collections.sort((List)valA);
		   valB= Arrays.asList((Integer[])valB);
		   Collections.sort((List)valB);
	   	}
	   
	   if (valA == null && valB != null)
			return false;
		if (valA != null && valB == null)
			return false;
		if (valA == null && valB == null)
			return true;
	   
		if(valA.equals(valB))
			return true;		   		  
		   return result;
	   }
	   
	   
	   public static int  getMinPosition(LinkedList lista)
		{
		Long min  =Long.MAX_VALUE;
		int pos =-1;
			for (int i = 0; i <lista.size(); i++)
			{
			if (((Long)lista.get(i)) <min)
				{
				pos = i ;
				min = ((Long)lista.get(i));
				}
			}
		return pos;

	}
	   public static boolean hasDoubleValue(String value){
			try{
				new Double(value);
				return true;
			}
			catch(Exception ex){
				return false;
			}
		}
}
