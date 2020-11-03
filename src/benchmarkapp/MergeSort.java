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
 * @about MergeSort in LinkedList
 */
public class MergeSort {
    public static void main(String[] args)
    {
        int[] a = {5,4,3,2,1,5,4,3,3,4,6,3,3,7,0,9,8,3,8,5};
        LinkedList <Integer> array = new LinkedList( );
        for (int i = 0; i < a.length; i++)
        {
            array.add(a[i]);
        }
        
//        String[] a = {"b", "ad", "ga", "rd", "as", "asw"};
//        
//        LinkedList array = new LinkedList();
//        for ( String datum : a )
//        {
//            array.add(datum);
//        }
        
        System.out.println( "Initial LinkedList MOFO: " + array );
        
        array = orderMergesort(array, 1);
        
        System.out.println( "Final LinkedList MOFO: " + array );
        
    }
    
    private static String order;
    
    public static LinkedList <Integer> orderMergesort( LinkedList <Integer> array, int orderIndex )
    {
        if ( orderIndex == 0 )
        {
            order = "asc";
        }
        else
        {
            order = "dsc";
        }
        
        LinkedList <Integer> result = mergesort(array);
        
        return result;
    }
    
    public static LinkedList <Integer> mergesort( LinkedList <Integer> array )
    {
        if ( array.size() <= 1 )
        {
            return array;
        }
        
        int midpoint = array.size() / 2;
        
        LinkedList <Integer> left = new LinkedList();
        LinkedList <Integer> right = new LinkedList();
        
        for ( int i = 0; i < midpoint; i++ )
        {
            left.add(array.get(i));
        }
        
        while ( midpoint < array.size() )
        {
            right.add(array.get(midpoint));
            midpoint++;
        }
        
        left = mergesort(left);
        right = mergesort(right);
        
        LinkedList <Integer> result = new LinkedList();
        result = join( left, right );
        
        return result;
    }
    
    public static LinkedList join( LinkedList <Integer> left, LinkedList <Integer> right )
    {
        LinkedList <Integer> result = new LinkedList();
        
        int rp;
        int lp;
        
        rp = lp = 0;
        
        if ( order.equals("asc") )
        {
            while ( lp < left.size() && rp < right.size() )
            {
                if ( left.get(lp) < right.get(rp) )
                {
                    result.add( left.get(lp) );
                    lp++;
                }
                else
                {
                    result.add( right.get(rp) );
                    rp++;
                }
            }
        }
        else
        {
            while ( lp < left.size() && rp < right.size() )
            {
                if ( left.get(lp) > right.get(rp) )
                {
                    result.add( left.get(lp) );
                    lp++;
                }
                else
                {
                    result.add( right.get(rp) );
                    rp++;
                }
            }
        }
            
        
        while ( lp < left.size() )
        {
            result.add( left.get(lp) );
            lp++;
        }
        
        while ( rp < right.size() )
        {
            result.add( right.get(rp) );
            rp++;
        }
        
        return result;
    }
    
}
