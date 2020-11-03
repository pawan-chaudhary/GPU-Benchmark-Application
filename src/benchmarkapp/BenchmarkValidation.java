/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benchmarkapp;

import javax.swing.JTable;

/**
 *
 * @author Sakar
 * @about   Used to append CSV data from file into the existing data of table
 */
public class BenchmarkValidation{
    public static int findEmptyRow( JTable impTable )
    {
        int totalRows = impTable.getRowCount();
        int totalCols = impTable.getColumnCount();
        
        int insertableRowNum = totalRows;
        
        boolean insertNow = false;
        int checkRowNum = 0;
        
        while ( (insertNow == false) && (checkRowNum < totalRows) )
        {
            int checkColNum = 0;
            int emptyCellCount = 0;
            while( checkColNum < totalCols )
            {
                Object cellvalue = impTable.getValueAt(checkRowNum, checkColNum);
                if ( cellvalue == null )
                {
                    emptyCellCount++;
                }
                else if( cellvalue.toString().isBlank() )
                {
                    emptyCellCount++;
                }
                checkColNum++;
            }
            
            if( emptyCellCount == totalCols )
            {
                insertNow = true;
                insertableRowNum = checkRowNum;
            }
            
            checkRowNum++;            
        }
        
        if ( insertNow == false && checkRowNum == totalRows )
        {
            insertableRowNum = totalRows;
        }
        
        return insertableRowNum;
    }
}
