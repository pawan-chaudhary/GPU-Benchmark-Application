/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benchmarkapp;

import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sakar
 * @about   Basic functions of GPU Benchmark App. COntains GUI, basic logic, functionalities and their implementation
 */
public class BenchmarkApp extends javax.swing.JFrame {

//    LinkedList initialised for storing data of each column
    private LinkedList <String> listName;
    private LinkedList <String> listBrand;
    private LinkedList <String> listModel;
    private LinkedList <String> listType;
    private LinkedList <Integer> listScore;
    private LinkedList <Integer> listPrice;
    
    /**
     * Creates new form BenchmarkApp
     * Constructor, pre-loads necessary methods
     */
    public BenchmarkApp() {
        initComponents();
        preloadCSV();
        setExit();
    }
    
//    Sets necessary environment for Exit Confirmation Popup
    private void setExit()
    {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                int a = JOptionPane.showConfirmDialog(null, "Do you want to Close?", "Close Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if ( a == JOptionPane.YES_OPTION )
                {
                    e.getWindow().dispose();
                }
            }
        });
    }
    
//    reads the data from CSV file present in the filesystem and loads them into table view
    private void preloadCSV()
    {
        URL csvPath = BenchmarkApp.class.getResource("gpuDB.csv");
        
        File csvPointer = null;
        try
        {
            csvPointer = new File(csvPath.toURI());
        }
        catch (Exception exc)
        {
            System.out.println(exc);
        }
        
        try
        {
            FileReader csvContent = new FileReader(csvPointer);
            BufferedReader bufferContent = new BufferedReader(csvContent);
            DefaultTableModel tableGPUModel = (DefaultTableModel) tableGPUView.getModel();
            Object[] lines = bufferContent.lines().toArray();

            for(int i = 0; i < lines.length; i++)
            {
               String[] row = lines[i].toString().split(",");
               tableGPUModel.addRow(row);
            }     
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
//    this process generates linkedlist and populates it with data from each column of table according to the index it receives
    private LinkedList populateGPUColumn( int wantedIndex )
    {
        LinkedList wantedList;
        
        int totalRowCount = tableGPUView.getRowCount();
        int checkRowNum = 0;
        
        if ( wantedIndex == 4 || wantedIndex == 5 )
        {
            wantedList = new <Integer> LinkedList();
            
            while( checkRowNum < totalRowCount )
            {
                wantedList.add( Integer.parseInt( tableGPUView.getValueAt(checkRowNum, wantedIndex).toString() ) );
                checkRowNum++;
            }
        }
        else
        {
            wantedList = new <String> LinkedList();
            
            while( checkRowNum < totalRowCount )
            {
                wantedList.add( tableGPUView.getValueAt(checkRowNum, wantedIndex).toString() );
                checkRowNum++;
            }
        }
        
        return wantedList;
    }
    
//    populates linkedlist with detail of a particular row
//    takes row no of table as input parameter and populates the linkedlist with infromation from each column
    private LinkedList getStat( int wantedIndex )
    {
        LinkedList <String> gpuStat = new LinkedList();
        
        int totalColCount = tableGPUView.getColumnCount();
        int checkColNum = 0;
        
        while (checkColNum < totalColCount)
        {
            gpuStat.add( tableGPUView.getValueAt(wantedIndex, checkColNum).toString() );
            checkColNum++;
        }
        
        return gpuStat;
    }
    
//    takes the values of GPU Price and Mark Score of both compared columns and highlightes which is better
    private void verdict()
    {
        if ( !lblPrice_1.getText().isBlank() && !lblPrice_2.getText().isBlank() )
        {
            lblPrice_1.setForeground(new java.awt.Color(0, 0, 0));
            lblPrice_2.setForeground(new java.awt.Color(0, 0, 0));
            
            int priceGPU1 = Integer.parseInt(lblPrice_1.getText());
            int priceGPU2 = Integer.parseInt(lblPrice_2.getText());
            
            if ( priceGPU1 < priceGPU2 )
            {
                lblPrice_1.setForeground(new java.awt.Color(0, 153, 0));
            }
            else
            {
                lblPrice_2.setForeground(new java.awt.Color(0, 153, 0));
            }
        }
        
        if ( !lblScore_1.getText().isBlank() && !lblScore_2.getText().isBlank() )
        {
            lblScore_1.setForeground(new java.awt.Color(0, 0, 0));
            lblScore_2.setForeground(new java.awt.Color(0, 0, 0));
            
            int scoreGPU1 = Integer.parseInt(lblScore_1.getText());
            int scoreGPU2 = Integer.parseInt(lblScore_2.getText());
            
            if ( scoreGPU1 < scoreGPU2 )
            {
                lblScore_2.setForeground(new java.awt.Color(0, 153, 0));
            }
            else
            {
                lblScore_1.setForeground(new java.awt.Color(0, 153, 0));
            }
        }
        
    }
    
//    uses the populateGPUColumn to populate data into the global variable
//    these global variables are used later in sorting table according to columns
    private void linkedListTable()
    {
        listName = populateGPUColumn(0);
        listBrand = populateGPUColumn(1);
        listModel = populateGPUColumn(2);
        listType = populateGPUColumn(3);
        listScore = populateGPUColumn(4);
        listPrice = populateGPUColumn(5);
    }
    
//    @param sortIndex      index of the column to be used as sorting basis
//    @param orderIndex     index of the order of sort; 0 being ascending and 1 descending
//    uses the global variables to sort the table data based on the user-defined column and user-defined order
    private void sortTable( int sortIndex, int orderIndex )
    {
//        orderIndex 0 = ascending order; 1+ = descending order
        linkedListTable();
        
        if (sortIndex >= 4 )
        {
//            INTEGER
            LinkedList <Integer> intList = populateGPUColumn( sortIndex );
            LinkedList <Integer> sortedIntList = MergeSort.orderMergesort(intList, orderIndex);
            System.out.println( "sorted value is " + sortedIntList );
            LinkedList <Integer> orgList = new LinkedList();
            
            if ( sortIndex == 4 )
            {
                orgList = listScore;
            }
            else if ( sortIndex == 5 )
            {
                orgList = listPrice;
            }
            
            
            int sortedIntIndex = 0;
            for ( int eachSortedInt : sortedIntList )
            {
                for ( int orgIntIndex = 0; orgIntIndex < orgList.size(); orgIntIndex++ )
                {
                    int eachOrgPrice = orgList.get(orgIntIndex);
                    if ( eachOrgPrice == eachSortedInt )
                    {
                        tableGPUView.setValueAt( listName.get(orgIntIndex), sortedIntIndex, 0);
                        tableGPUView.setValueAt( listBrand.get(orgIntIndex), sortedIntIndex, 1);
                        tableGPUView.setValueAt( listModel.get(orgIntIndex), sortedIntIndex, 2);
                        tableGPUView.setValueAt( listType.get(orgIntIndex), sortedIntIndex, 3);
                        tableGPUView.setValueAt( listScore.get(orgIntIndex), sortedIntIndex, 4);
                        tableGPUView.setValueAt( listPrice.get(orgIntIndex), sortedIntIndex, 5);
                        orgList.set(orgIntIndex, 0);
                        break;
                    }
                }
                sortedIntIndex++;
            }
        }
        else
        {
//            STRING
            LinkedList <String> strList = populateGPUColumn( sortIndex );
            LinkedList <String> sortedStrList = SelectionSort.selectionSortOrder(strList, orderIndex);
            LinkedList <String> orgList = new LinkedList();
            
            switch (sortIndex)
            {
                case 0:
                    orgList = listName;
                    break;
                case 1:
                    orgList = listBrand;
                    break;
                case 2:
                    orgList = listModel;
                    break;
                case 3:
                    orgList = listType;
                    break;
                default:
                    break;
            }
            
            int sortedStrIndex = 0;
            for ( String eachSortedStr : sortedStrList )
            {
                for ( int orgStrIndex = 0; orgStrIndex < orgList.size(); orgStrIndex++ )
                {
                    String eachOrgStr = orgList.get(orgStrIndex);
                    if ( eachOrgStr.equals(eachSortedStr) )
                    {
                        tableGPUView.setValueAt( listName.get(orgStrIndex), sortedStrIndex, 0);
                        tableGPUView.setValueAt( listBrand.get(orgStrIndex), sortedStrIndex, 1);
                        tableGPUView.setValueAt( listModel.get(orgStrIndex), sortedStrIndex, 2);
                        tableGPUView.setValueAt( listType.get(orgStrIndex), sortedStrIndex, 3);
                        tableGPUView.setValueAt( listScore.get(orgStrIndex), sortedStrIndex, 4);
                        tableGPUView.setValueAt( listPrice.get(orgStrIndex), sortedStrIndex, 5);
                        orgList.set(orgStrIndex, "");
                        break;
                    }
                }
                sortedStrIndex++;
            }
            
        }
    }
    
//    opens file-browsing window to select CSV file
//    read the value present in the CSV file
//    loads the CSV values present in the file and displays in tabular format
    public void openfile()
    {
        JFileChooser fileChoose = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
        fileChoose.setFileFilter(filter);
        fileChoose.setDialogTitle("Choose your CSV File"); 
        int f = fileChoose.showOpenDialog(fileChoose); // return int value to hold that  value approve option was created
        if (f == JFileChooser.APPROVE_OPTION)
        {
            File choosedFile = fileChoose.getSelectedFile();
            String filePath = choosedFile.toString();
            File file = new File(filePath);
            try 
            {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                DefaultTableModel model = (DefaultTableModel) tableGPUView.getModel();
                Object[] lines = br.lines().toArray();

                for(int i = 0; i < lines.length; i++)
                {
                    String[] row = lines[i].toString().split(",");
                    model.addRow(row);
                }
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(rootPane, "Incompatible File Type.", "Open CSV File", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            }
        }
    }
    
//    open the usermanual.pdf file present in the source folder when user triggers the Help Command
    public void openUserManual()
    {
        try
        {
            Desktop desktop = Desktop.getDesktop();
            URL filePath = BenchmarkApp.class.getResource("usermanual.pdf");
            File file = new File(filePath.toURI());
            desktop.open(file);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(rootPane, "File not found.", "User Manual", JOptionPane.ERROR_MESSAGE);
        }
    }
    
//    method for query generation
//    accepts LinkedList as input which contains values of a column
//    creates 2 LinkedList, 1st with String value of each unique element present in the LinkedList
//    2nd with the count of that elements occurence within the LinkedList
    public LinkedList countOccurence( LinkedList<String> array )
    {
        LinkedList <String> array1 = new LinkedList();
        
        for(String each:array)
        {
            array1.add(each);
        }
        
        LinkedList result = new LinkedList();
        LinkedList <String> ele = new LinkedList();
        LinkedList <Integer> rep = new LinkedList();
        
        for ( int i = 0; i < array.size(); i++ )
        {
            if(i>=0 && !array1.get(i).isEmpty())
            {
                boolean unique = true;
                int count = 0;
                for(int j=0; j<array.size();j++)
                {
                    unique = true;
                    
                    if(array.get(i).equals(array1.get(j)))
                    {
                        unique = false;
                        array1.set(j, "");
                        count++;
                    }
                }
                ele.add(array.get(i));
                if(count!=0)
                {
                    rep.add(count);
                }
            }
        }
        
        result.add(ele);
        result.add(rep);
        
        return result;
    }
    
//    generates the message in String format, to be displayed in popup dialog box, when user triggers query function
    public String generateStockDetail( int index )
    {
        LinkedList stock = countOccurence( populateGPUColumn(index) );
        LinkedList <String> element = (LinkedList <String>) stock.get(0);
        LinkedList <Integer> reps = (LinkedList <Integer>) stock.get(1);
        String detail = "";
        for (String each:element)
        {
            detail = detail + "\n" + each + ": " + reps.pop();
        }
        
        return detail;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hideTopbar = new javax.swing.JPanel();
        btnGrpGPUType = new javax.swing.ButtonGroup();
        mainWindowPanel = new javax.swing.JPanel();
        sidebarMainPanel = new javax.swing.JPanel();
        btnSideBenchmark = new javax.swing.JButton();
        btnSideInsert = new javax.swing.JButton();
        btnSideView = new javax.swing.JButton();
        btnSideCompare = new javax.swing.JButton();
        btnSideHelp = new javax.swing.JButton();
        btnSideExit = new javax.swing.JButton();
        tabbedContentPane = new javax.swing.JTabbedPane();
        benchmarkTabPanel = new javax.swing.JPanel();
        lblBackdrop = new javax.swing.JLabel();
        insertTabPanel = new javax.swing.JPanel();
        lblGPUName = new javax.swing.JLabel();
        txtGPUName = new javax.swing.JTextField();
        lblGPUBrand = new javax.swing.JLabel();
        comboGPUBrand = new javax.swing.JComboBox<>();
        lblGPUModel = new javax.swing.JLabel();
        txtGPUModel = new javax.swing.JTextField();
        lblGPUType = new javax.swing.JLabel();
        radioDesktop = new javax.swing.JRadioButton();
        radioMobile = new javax.swing.JRadioButton();
        lblMarkScore = new javax.swing.JLabel();
        txtMarkScore = new javax.swing.JTextField();
        lblGPUPrice = new javax.swing.JLabel();
        txtGPUPrice = new javax.swing.JTextField();
        btnResetForm = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        insertStatusPanel = new javax.swing.JPanel();
        lblInsertStatusBar = new javax.swing.JLabel();
        viewTabPanel = new javax.swing.JPanel();
        lblSort = new javax.swing.JLabel();
        comboSortColName = new javax.swing.JComboBox<>();
        comboOrder = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        tableGPUView = new javax.swing.JTable();
        btnSaveData = new javax.swing.JButton();
        lblQuery = new javax.swing.JLabel();
        comboQueryColName = new javax.swing.JComboBox<>();
        statusPanel = new javax.swing.JPanel();
        lblStatusBar = new javax.swing.JLabel();
        compareTabPanel = new javax.swing.JPanel();
        comboCompareGPU_1 = new javax.swing.JComboBox<>();
        comboCompareGPU_2 = new javax.swing.JComboBox<>();
        compareDetailPanel = new javax.swing.JPanel();
        lblCatName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblCatBrand = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblCatModel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        lblCatType = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lblCatMarkScore = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lblCatPrice = new javax.swing.JLabel();
        lblName_1 = new javax.swing.JLabel();
        lblBrand_1 = new javax.swing.JLabel();
        lblModel_1 = new javax.swing.JLabel();
        lblType_1 = new javax.swing.JLabel();
        lblScore_1 = new javax.swing.JLabel();
        lblPrice_1 = new javax.swing.JLabel();
        lblName_2 = new javax.swing.JLabel();
        lblBrand_2 = new javax.swing.JLabel();
        lblModel_2 = new javax.swing.JLabel();
        lblType_2 = new javax.swing.JLabel();
        lblScore_2 = new javax.swing.JLabel();
        lblPrice_2 = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileOpenOption = new javax.swing.JMenuItem();
        fileExitOption = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpHelpOption = new javax.swing.JMenuItem();

        javax.swing.GroupLayout hideTopbarLayout = new javax.swing.GroupLayout(hideTopbar);
        hideTopbar.setLayout(hideTopbarLayout);
        hideTopbarLayout.setHorizontalGroup(
            hideTopbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        hideTopbarLayout.setVerticalGroup(
            hideTopbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GPU Benchmark");
        setResizable(false);

        mainWindowPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidebarMainPanel.setBackground(new java.awt.Color(8, 24, 49));

        btnSideBenchmark.setBackground(new java.awt.Color(0, 102, 102));
        btnSideBenchmark.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideBenchmark.setForeground(new java.awt.Color(255, 255, 255));
        btnSideBenchmark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/001-gpu.png"))); // NOI18N
        btnSideBenchmark.setText("BENCHMARK");
        btnSideBenchmark.setBorder(null);
        btnSideBenchmark.setBorderPainted(false);
        btnSideBenchmark.setContentAreaFilled(false);
        btnSideBenchmark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideBenchmarkActionPerformed(evt);
            }
        });

        btnSideInsert.setBackground(new java.awt.Color(0, 102, 102));
        btnSideInsert.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideInsert.setForeground(new java.awt.Color(255, 255, 255));
        btnSideInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/003-insertion.png"))); // NOI18N
        btnSideInsert.setText("INSERT");
        btnSideInsert.setBorder(null);
        btnSideInsert.setBorderPainted(false);
        btnSideInsert.setContentAreaFilled(false);
        btnSideInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideInsertActionPerformed(evt);
            }
        });

        btnSideView.setBackground(new java.awt.Color(0, 102, 102));
        btnSideView.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideView.setForeground(new java.awt.Color(255, 255, 255));
        btnSideView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/005-motion-graphics.png"))); // NOI18N
        btnSideView.setText("VIEW");
        btnSideView.setBorder(null);
        btnSideView.setBorderPainted(false);
        btnSideView.setContentAreaFilled(false);
        btnSideView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideViewActionPerformed(evt);
            }
        });

        btnSideCompare.setBackground(new java.awt.Color(0, 102, 102));
        btnSideCompare.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideCompare.setForeground(new java.awt.Color(255, 255, 255));
        btnSideCompare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/006-price-tags.png"))); // NOI18N
        btnSideCompare.setText("COMPARE");
        btnSideCompare.setBorder(null);
        btnSideCompare.setBorderPainted(false);
        btnSideCompare.setContentAreaFilled(false);
        btnSideCompare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideCompareActionPerformed(evt);
            }
        });

        btnSideHelp.setBackground(new java.awt.Color(0, 102, 102));
        btnSideHelp.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideHelp.setForeground(new java.awt.Color(255, 255, 255));
        btnSideHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/010-help.png"))); // NOI18N
        btnSideHelp.setText("HELP");
        btnSideHelp.setBorder(null);
        btnSideHelp.setBorderPainted(false);
        btnSideHelp.setContentAreaFilled(false);
        btnSideHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideHelpActionPerformed(evt);
            }
        });

        btnSideExit.setBackground(new java.awt.Color(0, 102, 102));
        btnSideExit.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        btnSideExit.setForeground(new java.awt.Color(255, 255, 255));
        btnSideExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons_32/012-power.png"))); // NOI18N
        btnSideExit.setText("EXIT");
        btnSideExit.setBorder(null);
        btnSideExit.setBorderPainted(false);
        btnSideExit.setContentAreaFilled(false);
        btnSideExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSideExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidebarMainPanelLayout = new javax.swing.GroupLayout(sidebarMainPanel);
        sidebarMainPanel.setLayout(sidebarMainPanelLayout);
        sidebarMainPanelLayout.setHorizontalGroup(
            sidebarMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidebarMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSideBenchmark, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSideInsert, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(btnSideView, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(btnSideCompare, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(btnSideHelp, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(btnSideExit, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        sidebarMainPanelLayout.setVerticalGroup(
            sidebarMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSideBenchmark, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSideInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSideView, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSideCompare, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSideHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSideExit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        mainWindowPanel.add(sidebarMainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 600));

        tabbedContentPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        benchmarkTabPanel.setLayout(null);

        lblBackdrop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/aniBackdrop_resized.gif"))); // NOI18N
        benchmarkTabPanel.add(lblBackdrop);
        lblBackdrop.setBounds(0, 0, 880, 600);

        tabbedContentPane.addTab("benchmark", benchmarkTabPanel);

        lblGPUName.setText("GPU Name");

        lblGPUBrand.setText("GPU Brand");

        comboGPUBrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nvidia", "AMD", "Intel", "Asus", "Gigabyte", "EVGA", "Sapphire" }));

        lblGPUModel.setText("GPU Model");

        lblGPUType.setText("GPU Type");

        btnGrpGPUType.add(radioDesktop);
        radioDesktop.setText("Desktop");
        radioDesktop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDesktopActionPerformed(evt);
            }
        });

        btnGrpGPUType.add(radioMobile);
        radioMobile.setText("Mobile");
        radioMobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioMobileActionPerformed(evt);
            }
        });

        lblMarkScore.setText("3D Mark Score");

        txtMarkScore.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMarkScoreKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMarkScoreKeyReleased(evt);
            }
        });

        lblGPUPrice.setText("GPU Price");

        txtGPUPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGPUPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGPUPriceKeyReleased(evt);
            }
        });

        btnResetForm.setText("Clear");
        btnResetForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFormActionPerformed(evt);
            }
        });

        btnInsert.setText("Insert");
        btnInsert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInsertMouseEntered(evt);
            }
        });
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        insertStatusPanel.setPreferredSize(new java.awt.Dimension(850, 22));

        lblInsertStatusBar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInsertStatusBar.setPreferredSize(new java.awt.Dimension(875, 20));

        javax.swing.GroupLayout insertStatusPanelLayout = new javax.swing.GroupLayout(insertStatusPanel);
        insertStatusPanel.setLayout(insertStatusPanelLayout);
        insertStatusPanelLayout.setHorizontalGroup(
            insertStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertStatusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInsertStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                .addContainerGap())
        );
        insertStatusPanelLayout.setVerticalGroup(
            insertStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertStatusPanelLayout.createSequentialGroup()
                .addComponent(lblInsertStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout insertTabPanelLayout = new javax.swing.GroupLayout(insertTabPanel);
        insertTabPanel.setLayout(insertTabPanelLayout);
        insertTabPanelLayout.setHorizontalGroup(
            insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertTabPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(insertTabPanelLayout.createSequentialGroup()
                        .addComponent(lblGPUModel)
                        .addGap(99, 99, 99)
                        .addComponent(txtGPUModel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(insertTabPanelLayout.createSequentialGroup()
                        .addComponent(lblGPUType)
                        .addGap(103, 103, 103)
                        .addComponent(radioDesktop)
                        .addGap(45, 45, 45)
                        .addComponent(radioMobile))
                    .addGroup(insertTabPanelLayout.createSequentialGroup()
                        .addComponent(lblMarkScore)
                        .addGap(81, 81, 81)
                        .addComponent(txtMarkScore, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(insertTabPanelLayout.createSequentialGroup()
                            .addComponent(lblGPUBrand)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboGPUBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(insertTabPanelLayout.createSequentialGroup()
                            .addComponent(lblGPUName)
                            .addGap(100, 100, 100)
                            .addComponent(txtGPUName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(insertTabPanelLayout.createSequentialGroup()
                        .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGPUPrice)
                            .addComponent(btnResetForm))
                        .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(insertTabPanelLayout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(btnInsert))
                            .addGroup(insertTabPanelLayout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(txtGPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))))
            .addGroup(insertTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(insertStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        insertTabPanelLayout.setVerticalGroup(
            insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertTabPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGPUName)
                    .addComponent(txtGPUName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGPUBrand)
                    .addComponent(comboGPUBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGPUModel)
                    .addComponent(txtGPUModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGPUType)
                    .addComponent(radioDesktop)
                    .addComponent(radioMobile))
                .addGap(30, 30, 30)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMarkScore)
                    .addComponent(txtMarkScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGPUPrice)
                    .addComponent(txtGPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83)
                .addGroup(insertTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert)
                    .addComponent(btnResetForm))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(insertStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        tabbedContentPane.addTab("insert", insertTabPanel);

        lblSort.setText("Sort Table");

        comboSortColName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GPU Name", "GPU Brand", "GPU Model", "GPU Type", "3D Mark Score", "GPU Price" }));
        comboSortColName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSortColNameActionPerformed(evt);
            }
        });

        comboOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascending", "Descending" }));
        comboOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOrderActionPerformed(evt);
            }
        });

        txtSearch.setToolTipText("Enter Price to search");
        txtSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btnSearch.setText("Search Price");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tableGPUView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GPU Name", "GPU Brand", "GPU Model", "GPU Type", "3D Mark Score", "GPU Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableGPUView.getTableHeader().setReorderingAllowed(false);
        tableScrollPane.setViewportView(tableGPUView);
        if (tableGPUView.getColumnModel().getColumnCount() > 0) {
            tableGPUView.getColumnModel().getColumn(0).setPreferredWidth(175);
            tableGPUView.getColumnModel().getColumn(1).setResizable(false);
            tableGPUView.getColumnModel().getColumn(1).setPreferredWidth(25);
            tableGPUView.getColumnModel().getColumn(2).setResizable(false);
            tableGPUView.getColumnModel().getColumn(3).setResizable(false);
            tableGPUView.getColumnModel().getColumn(3).setPreferredWidth(10);
            tableGPUView.getColumnModel().getColumn(4).setResizable(false);
            tableGPUView.getColumnModel().getColumn(4).setPreferredWidth(30);
            tableGPUView.getColumnModel().getColumn(5).setResizable(false);
            tableGPUView.getColumnModel().getColumn(5).setPreferredWidth(20);
        }

        btnSaveData.setText("Save Data");
        btnSaveData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDataActionPerformed(evt);
            }
        });

        lblQuery.setText("Query Stock");

        comboQueryColName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GPU Name", "GPU Brand", "GPU Model", "GPU Type" }));
        comboQueryColName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboQueryColNameActionPerformed(evt);
            }
        });

        lblStatusBar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout viewTabPanelLayout = new javax.swing.GroupLayout(viewTabPanel);
        viewTabPanel.setLayout(viewTabPanelLayout);
        viewTabPanelLayout.setHorizontalGroup(
            viewTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(viewTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableScrollPane)
                    .addGroup(viewTabPanelLayout.createSequentialGroup()
                        .addComponent(lblSort)
                        .addGap(18, 18, 18)
                        .addComponent(comboSortColName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewTabPanelLayout.createSequentialGroup()
                        .addComponent(lblQuery)
                        .addGap(50, 50, 50)
                        .addComponent(comboQueryColName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSaveData, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        viewTabPanelLayout.setVerticalGroup(
            viewTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewTabPanelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(viewTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboSortColName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(lblSort))
                .addGap(18, 18, 18)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(viewTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveData)
                    .addComponent(comboQueryColName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQuery))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        txtSearch.getAccessibleContext().setAccessibleName("");

        tabbedContentPane.addTab("view", viewTabPanel);

        comboCompareGPU_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCompareGPU_1ActionPerformed(evt);
            }
        });

        comboCompareGPU_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCompareGPU_2ActionPerformed(evt);
            }
        });

        compareDetailPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        lblCatName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatName.setText("GPU Name");

        lblCatBrand.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatBrand.setText("GPU Brand");

        lblCatModel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatModel.setText("GPU Model");

        lblCatType.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatType.setText("GPU Type");

        lblCatMarkScore.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatMarkScore.setText("3D Mark Score");

        lblCatPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCatPrice.setText("GPU Price");

        lblName_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblBrand_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblModel_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblType_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblScore_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPrice_1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblName_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblBrand_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblModel_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblType_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblScore_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPrice_2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout compareDetailPanelLayout = new javax.swing.GroupLayout(compareDetailPanel);
        compareDetailPanel.setLayout(compareDetailPanelLayout);
        compareDetailPanelLayout.setHorizontalGroup(
            compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compareDetailPanelLayout.createSequentialGroup()
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(compareDetailPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator3)
                            .addComponent(jSeparator4)
                            .addComponent(jSeparator5)))
                    .addGroup(compareDetailPanelLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBrand_1)
                            .addComponent(lblName_1)
                            .addComponent(lblModel_1)
                            .addComponent(lblType_1)
                            .addComponent(lblScore_1)
                            .addComponent(lblPrice_1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 330, Short.MAX_VALUE)
                        .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCatMarkScore)
                            .addComponent(lblCatType)
                            .addComponent(lblCatModel)
                            .addComponent(lblCatBrand)
                            .addComponent(lblCatName)
                            .addComponent(lblCatPrice))
                        .addGap(111, 111, 111)))
                .addGap(47, 47, 47)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBrand_2)
                    .addComponent(lblName_2)
                    .addComponent(lblModel_2)
                    .addComponent(lblType_2)
                    .addComponent(lblScore_2)
                    .addComponent(lblPrice_2))
                .addGap(183, 183, 183))
        );
        compareDetailPanelLayout.setVerticalGroup(
            compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compareDetailPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatName)
                    .addComponent(lblName_1)
                    .addComponent(lblName_2))
                .addGap(30, 30, 30)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatBrand)
                    .addComponent(lblBrand_1)
                    .addComponent(lblBrand_2))
                .addGap(30, 30, 30)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatModel)
                    .addComponent(lblModel_1)
                    .addComponent(lblModel_2))
                .addGap(30, 30, 30)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatType)
                    .addComponent(lblType_1)
                    .addComponent(lblType_2))
                .addGap(30, 30, 30)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatMarkScore)
                    .addComponent(lblScore_1)
                    .addComponent(lblScore_2))
                .addGap(30, 30, 30)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(compareDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCatPrice)
                    .addComponent(lblPrice_1)
                    .addComponent(lblPrice_2))
                .addContainerGap())
        );

        javax.swing.GroupLayout compareTabPanelLayout = new javax.swing.GroupLayout(compareTabPanel);
        compareTabPanel.setLayout(compareTabPanelLayout);
        compareTabPanelLayout.setHorizontalGroup(
            compareTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compareTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(compareTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(compareDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(compareTabPanelLayout.createSequentialGroup()
                        .addComponent(comboCompareGPU_1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboCompareGPU_2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        compareTabPanelLayout.setVerticalGroup(
            compareTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compareTabPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(compareTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboCompareGPU_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCompareGPU_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(compareDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        tabbedContentPane.addTab("compare", compareTabPanel);

        mainWindowPanel.add(tabbedContentPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, -30, 880, 630));

        fileMenu.setText("File");

        fileOpenOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        fileOpenOption.setText("Open");
        fileOpenOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpenOptionActionPerformed(evt);
            }
        });
        fileMenu.add(fileOpenOption);

        fileExitOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        fileExitOption.setText("Exit");
        fileExitOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileExitOptionActionPerformed(evt);
            }
        });
        fileMenu.add(fileExitOption);

        mainMenuBar.add(fileMenu);

        helpMenu.setText("Help");

        helpHelpOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        helpHelpOption.setText("Help");
        helpHelpOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpHelpOptionActionPerformed(evt);
            }
        });
        helpMenu.add(helpHelpOption);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainWindowPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainWindowPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(1096, 639));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

//    shows homepage when 1st button on sidebar is pressed
    private void btnSideBenchmarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideBenchmarkActionPerformed
        tabbedContentPane.setSelectedIndex(0);
    }//GEN-LAST:event_btnSideBenchmarkActionPerformed

//    shows the Insert view and form when insert button on sidebar is pressed
    private void btnSideInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideInsertActionPerformed
        tabbedContentPane.setSelectedIndex(1);
    }//GEN-LAST:event_btnSideInsertActionPerformed

//    displays the View Page with tables when View button on sidebar is pressed
    private void btnSideViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideViewActionPerformed
        tabbedContentPane.setSelectedIndex(2);
    }//GEN-LAST:event_btnSideViewActionPerformed

//    displays the Compare Page when Compare button on sidebar is pressed
    private void btnSideCompareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideCompareActionPerformed
        tabbedContentPane.setSelectedIndex(3);
        
        LinkedList <String> gpuNameLinkedList = populateGPUColumn(0);
        
        String[] gpuNameStringList = new String[  gpuNameLinkedList.size() ];
        
        int indexCount = 0;
        for ( String eachName : gpuNameLinkedList )
        {
            gpuNameStringList[indexCount] = eachName;
            indexCount++;
        }
        
        comboCompareGPU_1.setModel( new DefaultComboBoxModel(gpuNameStringList) );
        comboCompareGPU_2.setModel( new DefaultComboBoxModel(gpuNameStringList) );
    }//GEN-LAST:event_btnSideCompareActionPerformed

//    Insert button trigger
//    reads the values typed by user, validates them and inserts into the table
    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        try{
            String name = txtGPUName.getText();
            String brand = comboGPUBrand.getSelectedItem().toString();
            String model = txtGPUModel.getText();
            String type  = btnGrpGPUType.getSelection().getActionCommand();
            String markScore = txtMarkScore.getText();
            String price = txtGPUPrice.getText();

            LinkedList <String> existingGPU = populateGPUColumn(0);
            boolean unique = true;
            
//            checks the inserted GPU name value with the each data existing in Table
            for( int i = 0; i < existingGPU.size(); i++ )
            {
                if ( existingGPU.get(i).equals(name) )
                {
                    unique = false;
                    break;
                }
            }
            
//            if the typed data is not present inthe table, proceeds with insertion
            if ( unique == true )
            {
                LinkedList <String> formData = new LinkedList();
                formData.add(name);
                formData.add(brand);
                formData.add(model);
                formData.add(type);
                formData.add(markScore);
                formData.add(price);

                int insertableRow = BenchmarkValidation.findEmptyRow(tableGPUView);
                int colCounter = 0;

                if ( insertableRow >= tableGPUView.getRowCount() )
                {
                    DefaultTableModel gpuModel = (DefaultTableModel) tableGPUView.getModel();
                    gpuModel.addRow( new String[6] );
                }

                for ( String datum:formData )
                {
                    tableGPUView.setValueAt(datum, insertableRow, colCounter);
                    colCounter++;
                }
                
                txtGPUName.setText(null);
                txtGPUModel.setText(null);
                txtGPUPrice.setText(null);
                txtMarkScore.setText(null);
                
                lblInsertStatusBar.setText("GPU added to Inventory. Thank you.");
            }
            else
            {
                lblInsertStatusBar.setText("This GPU already exists. Please enter different one.");
            }
        }
        catch(NullPointerException nullExc)
        {
            lblInsertStatusBar.setText("All fields are mandatory. Fill and Try Again.");
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void radioDesktopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDesktopActionPerformed
        radioDesktop.setActionCommand("Desktop");
    }//GEN-LAST:event_radioDesktopActionPerformed

    private void radioMobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioMobileActionPerformed
        radioMobile.setActionCommand("Mobile");
    }//GEN-LAST:event_radioMobileActionPerformed

//    once the index of gpu is selected in column one, the specifications of that GPU is populated through getStat()
//    LinkedList with value received from gpuStat() is then used to populate the leftside column of COmpare view
    private void comboCompareGPU_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCompareGPU_1ActionPerformed
        int comboIndex = comboCompareGPU_1.getSelectedIndex();
        LinkedList <String> gpuStat_1 = getStat(comboIndex);
        
        lblName_1.setText( gpuStat_1.remove() );
        lblBrand_1.setText( gpuStat_1.remove() );
        lblModel_1.setText( gpuStat_1.remove() );
        lblType_1.setText( gpuStat_1.remove() );
        lblScore_1.setText( gpuStat_1.remove() );
        lblPrice_1.setText( gpuStat_1.remove() );
        
//        triggers the verdict() function after column is populated, which compares the value and highlightes the better option
        verdict();
    }//GEN-LAST:event_comboCompareGPU_1ActionPerformed

//    once the index of gpu is selected in column one, the specifications of that GPU is populated through getStat()
//    LinkedList with value received from gpuStat() is then used to populate the rightside column of COmpare view
    private void comboCompareGPU_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCompareGPU_2ActionPerformed
        int comboIndex = comboCompareGPU_2.getSelectedIndex();
        LinkedList <String> gpuStat_2 = getStat(comboIndex);
        
        lblName_2.setText( gpuStat_2.remove() );
        lblBrand_2.setText( gpuStat_2.remove() );
        lblModel_2.setText( gpuStat_2.remove() );
        lblType_2.setText( gpuStat_2.remove() );
        lblScore_2.setText( gpuStat_2.remove() );
        lblPrice_2.setText( gpuStat_2.remove() );
        
        verdict();
    }//GEN-LAST:event_comboCompareGPU_2ActionPerformed

//    takes the value from Order Combobox in index form
//    0 is ascending and 1 is descending
    private void comboOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrderActionPerformed
        int sortIndex = comboSortColName.getSelectedIndex();
//        index 0 = ascending order; 1+ = descending order
        int orderIndex = comboOrder.getSelectedIndex();
        
//        triggers the sort table function and notifies the user through statusbar
        sortTable( sortIndex, orderIndex );
        if ( sortIndex <= 3 )
        {
            lblStatusBar.setText("Table sorted alphabetically by " + comboSortColName.getSelectedItem() );
        }
        else
        {
            lblStatusBar.setText("Table sorted by " + comboSortColName.getSelectedItem() );
        }
    }//GEN-LAST:event_comboOrderActionPerformed

//    displays exit confirmation when exit option in sidebar is pressed
    private void btnSideExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideExitActionPerformed
        int a = JOptionPane.showConfirmDialog(null, "Do you want to Close?", "Close Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if ( a == JOptionPane.YES_OPTION )
        {
//            e.getWindow().dispose();
            System.exit(0);
        }
    }//GEN-LAST:event_btnSideExitActionPerformed

//    takes the index value of column name selected by the user
//    the index value of the column is used to define what to sort table based on
    private void comboSortColNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSortColNameActionPerformed
        int sortIndex = comboSortColName.getSelectedIndex();
//        index 0 = ascending order; 1+ = descending order
        int orderIndex = comboOrder.getSelectedIndex();
        
        sortTable( sortIndex, orderIndex );
        if ( sortIndex <= 3 )
        {
            lblStatusBar.setText("Table sorted alphabetically by " + comboSortColName.getSelectedItem() );
        }
        else
        {
            lblStatusBar.setText("Table sorted by " + comboSortColName.getSelectedItem() );
        }
    }//GEN-LAST:event_comboSortColNameActionPerformed

//    Search Button action trigger
//    when pressed integer data typed by user in txtSearch textfield is read
//    that data is used to search the table in price column
//    if found, the details of the GPU with the same price is displayed
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try 
        {
            int wanted = Integer.parseInt( txtSearch.getText() );
            sortTable(5, 0);
            linkedListTable();
            int index = BinarySearch.binarySearch(listPrice, 0, listPrice.size() - 1, wanted);
            if ( index != -1 )
            {
                String message = "GPU found.\n" + "\nName: " + listName.get(index) + "\nBrand: " + listBrand.get(index) + "\nModel: " + listModel.get(index) + "\nType: " + listType.get(index) + "\n3D Mark Score: " + listScore.get(index) + "\nPrice: " + listPrice.get(index);
                JOptionPane.showMessageDialog(rootPane, message, "Price Search Result", JOptionPane.INFORMATION_MESSAGE);
                txtSearch.setText(null);
                lblStatusBar.setText(null);
            }
            else
            {
                String message = "The GPU of specified price is not available. Please try another price.";
                JOptionPane.showMessageDialog(rootPane, message, "Price Search Result", JOptionPane.ERROR_MESSAGE);
                txtSearch.setText(null);
                lblStatusBar.setText(null);
            }
        } 
        catch (NumberFormatException numExc)
        {
            lblStatusBar.setText("Invalid value. Please enter valid Integer value in Price Search textbox.");
            txtSearch.setText(null);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

//    reads the index of the column name selected by the user
//    this value is used to generate stock count through countOccurence() method
//    data generated through countOccurence() is processed in displayable String
    private void comboQueryColNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboQueryColNameActionPerformed
        int queryIndex = comboQueryColName.getSelectedIndex();
        String message;
        String detail;
        switch (queryIndex)
        {
            case 0:
                message = "Total available GPUs = " + tableGPUView.getRowCount();
                JOptionPane.showMessageDialog(rootPane, message, "Stock Detail", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                detail = generateStockDetail(1);
                message = "Available GPU Brand Stock Details:\n" + detail;
                JOptionPane.showMessageDialog(rootPane, message, "Stock Detail", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                detail = generateStockDetail(2);
                message = "Available GPU Model Stock Details:\n" + detail;
                JOptionPane.showMessageDialog(rootPane, message, "Stock Detail", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 3:
                detail = generateStockDetail(3);
                message = "Available GPU Type Stock Details:\n" + detail;
                JOptionPane.showMessageDialog(rootPane, message, "Stock Detail", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_comboQueryColNameActionPerformed

//    triggers openfile() method when pressed through menu
    private void fileOpenOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenOptionActionPerformed
        openfile();
    }//GEN-LAST:event_fileOpenOptionActionPerformed

//    Triggers application exit when pressed
    private void fileExitOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileExitOptionActionPerformed
        int a = JOptionPane.showConfirmDialog(null, "Do you want to Close?", "Close Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if ( a == JOptionPane.YES_OPTION )
        {
            System.exit(0);
        }
    }//GEN-LAST:event_fileExitOptionActionPerformed

//    opens usermanual.pdf file when help is pressed in file menu
    private void helpHelpOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpHelpOptionActionPerformed
        openUserManual();
    }//GEN-LAST:event_helpHelpOptionActionPerformed

//    reset button, when pressed removes the data type by the user in insert form
    private void btnResetFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetFormActionPerformed
        txtGPUName.setText(null);
        txtGPUModel.setText(null);
        txtGPUPrice.setText(null);
        txtMarkScore.setText(null);
    }//GEN-LAST:event_btnResetFormActionPerformed

//    checks if the users has entered required fields, if not disables the button
    private void btnInsertMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsertMouseEntered
        if ( txtGPUName.getText().isBlank() || txtGPUModel.getText().isBlank() || txtGPUPrice.getText().isBlank() || txtMarkScore.getText().isBlank() )
        {
            btnInsert.setEnabled(false);
            lblInsertStatusBar.setText("All fields are mandatory. Fill and Try Again.");
        }
        else
        {
            btnInsert.setEnabled(true);
            lblInsertStatusBar.setText(null);
        }
    }//GEN-LAST:event_btnInsertMouseEntered

//    allows only integer, backspace to be pressed in the textfield
    private void txtMarkScoreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarkScoreKeyPressed
        if ( ( evt.getKeyChar() < '0' || evt.getKeyChar() > '9' ) && evt.getKeyCode() != 8 && evt.getKeyCode() != 37 && evt.getKeyCode() != 39 )
        {
            txtMarkScore.setEditable(false);
            lblInsertStatusBar.setText("Alphabets and Decimals are not allowed. We only allow and deal in absolutes like Sith.");
        }
    }//GEN-LAST:event_txtMarkScoreKeyPressed

    private void txtMarkScoreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarkScoreKeyReleased
        txtMarkScore.setEditable(true);
    }//GEN-LAST:event_txtMarkScoreKeyReleased

//    allows only integer, backspace to be pressed in the textfield
    private void txtGPUPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGPUPriceKeyPressed
        if ( ( evt.getKeyChar() < '0' || evt.getKeyChar() > '9' ) && evt.getKeyCode() != 8 && evt.getKeyCode() != 37 && evt.getKeyCode() != 39 )
        {
            txtGPUPrice.setEditable(false);
            lblInsertStatusBar.setText("Alphabets and Decimals are not allowed. We only allow and deal in absolutes like Sith.");
        }
    }//GEN-LAST:event_txtGPUPriceKeyPressed

    private void txtGPUPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGPUPriceKeyReleased
        txtGPUPrice.setEditable(true);
    }//GEN-LAST:event_txtGPUPriceKeyReleased

//    Export data from table in CSV format to filesystem
//    Once presed, the data present in table is outputed in CSV format
//    The CSVfile can be saved in user specified location in filesystem
    private void btnSaveDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDataActionPerformed
        String saveFilePath = null;
        JFileChooser saveFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
        saveFileChooser.setFileFilter(filter);
        saveFileChooser.setDialogTitle("Save as csv");
        int userSelection = saveFileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveFileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                saveFilePath = fileToSave.getAbsolutePath();
            }
        try
        {
            FileWriter fw = new FileWriter(saveFilePath);
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < tableGPUView.getRowCount(); i++)
            {
                for(int j = 0; j < tableGPUView.getColumnCount(); j++)
                {
                    String line = null;
                    if(j < 5){
                     line = (tableGPUView.getValueAt(i,j).toString()+",");
                    }
                    else if (j == 5)
                    {
                      line = (tableGPUView.getValueAt(i,j).toString());
                    }                        
                    bw.write(line);                       
                }                                    
                bw.newLine();        
            }
            bw.close();
            fw.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSaveDataActionPerformed

    private void btnSideHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSideHelpActionPerformed
        openUserManual();
    }//GEN-LAST:event_btnSideHelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BenchmarkApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BenchmarkApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BenchmarkApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BenchmarkApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BenchmarkApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel benchmarkTabPanel;
    private javax.swing.ButtonGroup btnGrpGPUType;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnResetForm;
    private javax.swing.JButton btnSaveData;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSideBenchmark;
    private javax.swing.JButton btnSideCompare;
    private javax.swing.JButton btnSideExit;
    private javax.swing.JButton btnSideHelp;
    private javax.swing.JButton btnSideInsert;
    private javax.swing.JButton btnSideView;
    private javax.swing.JComboBox<String> comboCompareGPU_1;
    private javax.swing.JComboBox<String> comboCompareGPU_2;
    private javax.swing.JComboBox<String> comboGPUBrand;
    private javax.swing.JComboBox<String> comboOrder;
    private javax.swing.JComboBox<String> comboQueryColName;
    private javax.swing.JComboBox<String> comboSortColName;
    private javax.swing.JPanel compareDetailPanel;
    private javax.swing.JPanel compareTabPanel;
    private javax.swing.JMenuItem fileExitOption;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem fileOpenOption;
    private javax.swing.JMenuItem helpHelpOption;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel hideTopbar;
    private javax.swing.JPanel insertStatusPanel;
    private javax.swing.JPanel insertTabPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblBackdrop;
    private javax.swing.JLabel lblBrand_1;
    private javax.swing.JLabel lblBrand_2;
    private javax.swing.JLabel lblCatBrand;
    private javax.swing.JLabel lblCatMarkScore;
    private javax.swing.JLabel lblCatModel;
    private javax.swing.JLabel lblCatName;
    private javax.swing.JLabel lblCatPrice;
    private javax.swing.JLabel lblCatType;
    private javax.swing.JLabel lblGPUBrand;
    private javax.swing.JLabel lblGPUModel;
    private javax.swing.JLabel lblGPUName;
    private javax.swing.JLabel lblGPUPrice;
    private javax.swing.JLabel lblGPUType;
    private javax.swing.JLabel lblInsertStatusBar;
    private javax.swing.JLabel lblMarkScore;
    private javax.swing.JLabel lblModel_1;
    private javax.swing.JLabel lblModel_2;
    private javax.swing.JLabel lblName_1;
    private javax.swing.JLabel lblName_2;
    private javax.swing.JLabel lblPrice_1;
    private javax.swing.JLabel lblPrice_2;
    private javax.swing.JLabel lblQuery;
    private javax.swing.JLabel lblScore_1;
    private javax.swing.JLabel lblScore_2;
    private javax.swing.JLabel lblSort;
    private javax.swing.JLabel lblStatusBar;
    private javax.swing.JLabel lblType_1;
    private javax.swing.JLabel lblType_2;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JPanel mainWindowPanel;
    private javax.swing.JRadioButton radioDesktop;
    private javax.swing.JRadioButton radioMobile;
    private javax.swing.JPanel sidebarMainPanel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabbedContentPane;
    private javax.swing.JTable tableGPUView;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField txtGPUModel;
    private javax.swing.JTextField txtGPUName;
    private javax.swing.JTextField txtGPUPrice;
    private javax.swing.JTextField txtMarkScore;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JPanel viewTabPanel;
    // End of variables declaration//GEN-END:variables
}
