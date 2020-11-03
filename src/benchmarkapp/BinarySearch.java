/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benchmarkapp;

import java.util.LinkedList;

/**
 *
 * @author Sakar
 * @about   Binary Search Algorithm, used to search GPU Price received as Integer datatype
 */
public class BinarySearch {
    
    public static int binarySearch( LinkedList <Integer> array, int low, int high, int wanted )
    {
        System.out.println( "low=" + low + "high=" + high );
        if ( high >= low )
        {
            int mid = low + ((high-low) / 2);
            if ( wanted == array.get(mid) )
            {
                return mid;
            }
            
            if ( wanted < array.get(mid) )
            {
                return binarySearch(array, low, mid-1, wanted);
            }
            else
            {
                return binarySearch(array, mid+1, high, wanted);
            }
        }
        
        return -1;
    }
}
