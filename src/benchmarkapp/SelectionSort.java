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
 * @about   Selection Sort algorithm, used to Sort String data received
 */
public class SelectionSort {
    
    private static String order;
    
    public static LinkedList <String> selectionSortOrder( LinkedList <String> strArr, int orderIndex )
    {
        if ( orderIndex == 0 )
        {
            order = "asc";
        }
        else
        {
            order = "dsc";
        }
        
        LinkedList <String> result = selectionSort(strArr);
        
        return result;
    }
    
    public static LinkedList <String> selectionSort( LinkedList <String> strArr )
    {
        int pointerA = 0;
        while ( pointerA < strArr.size() - 1 )
        {
            int index = pointerA;
            int pointerB = pointerA + 1;
            
            if ( order.equals("asc") )
            {
                while ( pointerB < strArr.size() )
                {
                    if ( strArr.get(pointerB).compareToIgnoreCase(strArr.get(index) ) < 0 )
                    {
                        index = pointerB;
                    }
                    pointerB++;
                }
            }
            else
            {
                while ( pointerB < strArr.size() )
                {
                    if ( strArr.get(pointerB).compareToIgnoreCase(strArr.get(index) ) > 0 )
                    {
                        index = pointerB;
                    }
                    pointerB++;
                }
            }
            
            String tmp = strArr.get( index );
            strArr.set(index, strArr.get( pointerA ) );
            strArr.set(pointerA, tmp);
            
            pointerA++;
        }
        
        return strArr;
        
    }
}
