//=================================================================
//              TableFieldPlotter TableFieldChartsPanel
//=================================================================
//
//    This class provides the panel that holds all the charts used
// in the TableFieldProfiler code.
//
//               << TableFieldChartsPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 2.0 02/20/2010
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version
// 2 of the License, or (at your option) any later version. This
// program is distributed in the hope that it will be useful, 
// but WITHOUT ANY WARRANTY; without even the implied warranty
// of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
// the GNU General Public License for more details. You should
// have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation,
// Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// (http://opensource.org)
//
//=================================================================
// Revision History
// Changes to the code should be documented here and reflected
// in the present version number. Author information should
// also be included with the original copyright author.
//=================================================================
// Version 1.0 Initial TableFieldChartsPanel Class.
//         1.1 Changed chartsPanel's Border and Moved the Update to repaint()
//             It in the executeUpdate() Class Method to After the Control
//             Components Enabled Again.
//         1.2 Changes in getter/setter Methods That Are Called From The PiePanel
//             & PlotterPanel.
//         1.3 Removed the update() Call on Each of the Charts in Class Methods
//             showRecordCountChart(), showDistributionChart(), showPatternChart().
//         1.4 Class Method showDistributionChart() Set Charts minValue to
//             patternMinValueLabel.
//         1.5 Removed setColorIndex() in Class Method showRecordCountChart() &
//             showPatternChart().
//         1.6 Removed Class Method createChartsPanels(). Removed Class Instances
//             patternMaxValueLabel & patternMinValueLabel and Replaced With
//             patternMaxValue & patternMinValue. Changed Class Method showPatternChart()
//             Cleaned Out, Moved Processing of Number of Bars to Display to the
//             HorizontalPlotterPanel.
//         1.7 Reviewed SQL Statements Creations in Class Methods getRecordCountValues(),
//             getDistributionValues(), and getPatternValues() and Removed the Selection
//             of '*' Table Fields Where Appropriate, Basically All.
//         1.8 Placed '*' Back Into getRecordCountValues for the First Query Row Count
//             Because NULL Values Are Not Registered When Selected As Count Function
//             Takes the Field Name. Modified the Class Method excuteUpdate() to Bypass
//             Blob, Binary, and Bytea Fields for the Distribution and Pattern Charts.
//         1.9 Changed Package to Reflect Dandy Made Productions Code.
//         2.0 Changed Class Instances patternMax/MinValue to patternMax/MinField. Modified
//             the Collection of the Setting of Those Same Fields in Class Method
//             getDistributionValues().
//                  
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;

/**
 *    The TableFieldChartsPanel class provides the panel that holds all the
 * charts used in the TableFieldProfiler code.
 * 
 * @author Dana M. Proctor
 * @version 2.0 02/20/2010
 */

class TableFieldChartsPanel extends JPanel implements ActionListener
{
   private static final long serialVersionUID = 823652123292193862L;

   // Class Instances
   private String tableName, columnName, chartName;
   private JLabel statusIndicator;
   private JComboBox tableSelectionComboBox;
   private JComboBox columnSelectionComboBox;
   private JRadioButton recordCountRadioButton, distributionRadioButton, patternRadioButton;
   private JButton refreshButton;
   private HashMap columnNamesHashMap, columnClassHashMap, columnTypeHashMap;
   private boolean tableColumnsLoaded;
   private boolean disableActions;

   private CardLayout chartsCardLayout;
   private JPanel chartsPanel;
   private PlotterPanel recordCountChart;
   private PiePanel distributionChart;
   private HorizontalPlotterPanel patternChart;
   private String patternMaxField, patternMinField;

   //==============================================================
   // TableFieldChartsPanel Constructor
   //==============================================================

   TableFieldChartsPanel(Vector tableNames)
   {
      // Constructor Instances
      JPanel controlPanel;
      JPanel tableColumnControlSelectionPanel;
      JPanel chartsControlSelectionPanel;
      
      ImageIcon statusIdleIcon, statusWorkingIcon, refreshIcon;
      String iconsDirectory;

      // Initializing & setting up the panel.
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      
      statusIdleIcon = new ImageIcon(iconsDirectory + "statusIdleIcon.png");
      statusWorkingIcon = new ImageIcon(iconsDirectory + "statusWorkingIcon.gif");
      refreshIcon = new ImageIcon(iconsDirectory + "refreshIcon.png");
      
      tableName = "";
      columnName = "";
      chartName = "Record Count";
      patternMaxField = "";
      patternMinField = "";
      columnNamesHashMap = new HashMap();
      columnClassHashMap = new HashMap();
      columnTypeHashMap = new HashMap();
      
      tableColumnsLoaded = false;
      disableActions = false;
      
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      
      // ===============================================
      // Create the control panel.

      controlPanel = new JPanel(new GridLayout(2, 1, 0, 0));

      // Create the table/column selector control container and
      // add a status indicator, label and table/column selectors
      // after filling.
      
      tableColumnControlSelectionPanel = new JPanel(gridbag);
      tableColumnControlSelectionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
            .createLoweredBevelBorder(), BorderFactory.createEmptyBorder(0, 0, 0, 0)));

      statusIndicator = new JLabel("", JLabel.LEFT);
      statusIndicator.setIcon(statusIdleIcon);
      statusIndicator.setDisabledIcon(statusWorkingIcon);
      statusIndicator.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
      
      buildConstraints(constraints, 0, 0, 1, 1, 1, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(statusIndicator, constraints);
      tableColumnControlSelectionPanel.add(statusIndicator);

      JLabel tableFieldInfoLabel = new JLabel("Table Field Information");

      buildConstraints(constraints, 1, 0, 1, 1, 8, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(tableFieldInfoLabel, constraints);
      tableColumnControlSelectionPanel.add(tableFieldInfoLabel);
      
      tableSelectionComboBox = new JComboBox();
      tableSelectionComboBox.setBorder(BorderFactory.createRaisedBevelBorder());
      tableSelectionComboBox.setEnabled(false);

      columnSelectionComboBox = new JComboBox();
      columnSelectionComboBox.setBorder(BorderFactory.createRaisedBevelBorder());
      columnSelectionComboBox.setEnabled(false);

      if (!tableNames.isEmpty())
      {
         Iterator tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
            tableSelectionComboBox.addItem(tableNamesIterator.next());

         tableName = (String) tableSelectionComboBox.getItemAt(0);
         loadColumns(tableName);

         tableSelectionComboBox.setEnabled(true);
      }      
      
      buildConstraints(constraints, 2, 0, 1, 1, 51, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(tableSelectionComboBox, constraints);
      tableColumnControlSelectionPanel.add(tableSelectionComboBox);

      buildConstraints(constraints, 3, 0, 1, 1, 40, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(columnSelectionComboBox, constraints);
      tableColumnControlSelectionPanel.add(columnSelectionComboBox);

      controlPanel.add(tableColumnControlSelectionPanel);

      // Create the charts control container holder and add the chart
      // selection radio buttons components to control panel.
      
      chartsControlSelectionPanel = new JPanel(gridbag);
      chartsControlSelectionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
            .createRaisedBevelBorder(), BorderFactory.createEtchedBorder()));

      ButtonGroup chartsControlButtonGroup = new ButtonGroup();

      recordCountRadioButton = new JRadioButton("Record Count", true);
      recordCountRadioButton.setFocusPainted(false);
      recordCountRadioButton.addActionListener(this);
      chartsControlButtonGroup.add(recordCountRadioButton);

      buildConstraints(constraints, 0, 0, 1, 1, 32, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(recordCountRadioButton, constraints);
      chartsControlSelectionPanel.add(recordCountRadioButton);

      distributionRadioButton = new JRadioButton("Distribution Chart", false);
      distributionRadioButton.setFocusPainted(false);
      distributionRadioButton.addActionListener(this);
      chartsControlButtonGroup.add(distributionRadioButton);

      buildConstraints(constraints, 1, 0, 1, 1, 32, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(distributionRadioButton, constraints);
      chartsControlSelectionPanel.add(distributionRadioButton);

      patternRadioButton = new JRadioButton("Pattern Information", false);
      patternRadioButton.setFocusPainted(false);
      patternRadioButton.addActionListener(this);
      chartsControlButtonGroup.add(patternRadioButton);

      buildConstraints(constraints, 2, 0, 1, 1, 32, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(patternRadioButton, constraints);
      chartsControlSelectionPanel.add(patternRadioButton);
      
      refreshButton = new JButton(refreshIcon);
      refreshButton.setHorizontalAlignment(JButton.RIGHT);
      refreshButton.setMargin(new Insets(0, 0, 0, 0));
      refreshButton.addActionListener(this);
      
      buildConstraints(constraints, 3, 0, 1, 1, 2, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(refreshButton, constraints);
      chartsControlSelectionPanel.add(refreshButton);

      controlPanel.add(chartsControlSelectionPanel);

      add(controlPanel, BorderLayout.NORTH);

      // ===============================================
      // Create the charts panel.
      
      chartsCardLayout = new CardLayout();
      chartsPanel = new JPanel(chartsCardLayout);
      chartsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                               BorderFactory.createLoweredBevelBorder()));
      // Record Count Chart.
      recordCountChart = new PlotterPanel();
      chartsPanel.add("Record Count", recordCountChart);

      // Distribution Chart.
      distributionChart = new PiePanel();
      chartsPanel.add("Distribution Chart", distributionChart);

      // Pattern Chart.
      patternChart = new HorizontalPlotterPanel();
      chartsPanel.add("Pattern Information", patternChart);
      
      add(chartsPanel, BorderLayout.CENTER);
          
      tableSelectionComboBox.addActionListener(this);
      columnSelectionComboBox.addActionListener(this);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from the
   // panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Setting current Panel card to the selected table/column/chart.
      
      if (disableActions)
         return;
      
      if (panelSource instanceof JComboBox)
      {

         if (panelSource == tableSelectionComboBox)
         {
            tableName = (String) tableSelectionComboBox.getSelectedItem();
            
            if (!tableName.equals(""))
            {
               loadColumns(tableName);
               
               if (tableColumnsLoaded)
               {
                  columnName = (String) columnNamesHashMap.get(columnSelectionComboBox.getSelectedItem());
                  executeUpdate();
               }
               DBTablesPanel.setSelectedTableTabPanel(tableName);
            }
         }

         if (panelSource == columnSelectionComboBox)
         {
            columnName = (String) columnNamesHashMap.get(columnSelectionComboBox.getSelectedItem());
            executeUpdate();
         }
      }

      if (panelSource instanceof JRadioButton)
      {
         if (tableColumnsLoaded)
         {
            chartName = ((JRadioButton) panelSource).getText();
            
            chartsCardLayout.show(chartsPanel, chartName);     
            executeUpdate(); 
         }
      }
      
      if (panelSource instanceof JButton)
      {
         if (((JButton) panelSource) == refreshButton)
            executeUpdate();
      }
   }
   
   //==============================================================
   // Class method to process with the action(s) reqired by events
   // detected by the panel via the actionPerformed method.
   //==============================================================

   private void executeUpdate()
   {
      // System.out.println(tableName + " " + columnName + " " + chartName);
      
      if (!tableName.equals("") && !columnName.equals("") && !chartName.equals(""))
      {
         Thread updateThread = new Thread(new Runnable()
         {
            String selectedColumnName;
            String columnClass, columnType;
            boolean isBlobField;
            
            public void run()
            {
               Connection work_dbConnection = MyJSQLView_Access
                                              .getConnection("TableFieldChartsPanel executeUpdate()");
               
               if (work_dbConnection == null)
                  return;
               
               // Disable the controls while processing.
               
               disableActions = true;
               statusIndicator.setEnabled(false);
               tableSelectionComboBox.setEnabled(false);
               columnSelectionComboBox.setEnabled(false);
               recordCountRadioButton.setEnabled(false);
               distributionRadioButton.setEnabled(false);
               patternRadioButton.setEnabled(false);
               
               // Collect needed field data to determine if charts can
               // be produced.
               
               selectedColumnName = (String) columnSelectionComboBox.getSelectedItem();
               columnClass = (String)columnClassHashMap.get(selectedColumnName);
               columnType = (String)columnTypeHashMap.get(selectedColumnName);
               
               isBlobField = (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                       (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                       columnType.indexOf("BYTEA") != -1 ||
                       columnType.indexOf("BINARY") != -1 ||
                       columnType.indexOf("RAW") != -1;
             
               // Update appropriate chart.
  
               if (chartName.equals("Record Count"))
               {   
                  double recordCountValues[] = getRecordCountValues(work_dbConnection);

                  if (recordCountValues != null)
                     showRecordCountChart(recordCountValues);
               }
               // Distribution & Pattern Charts.
               else
               {
                  Vector[] dataVector;
                  
                  // Doctor data since do not want to pull
                  // in binary data.
                  if (isBlobField)
                  {
                     int i = 0;
                     dataVector = new Vector[2];
                     dataVector[0] = new Vector();
                     dataVector[1] = new Vector();
                     
                     dataVector[0].add(i, "Unsupported Field");
                     dataVector[1].add(i, new Double(0.0));
                     
                     patternMinField = "";
                     patternMaxField = "";
                  }
                  // Normal processing.
                  else
                  {
                     // Distribution Chart.
                     if (chartName.equals("Distribution Chart"))
                        dataVector = getDistributionValues(work_dbConnection);
                     // Pattern Chart.
                     else
                     {
                        getDistributionValues(work_dbConnection);
                        dataVector = getPatternValues(work_dbConnection); 
                     }
                  }

                  if (dataVector != null)
                  {
                     // Distribution Chart.
                     if (chartName.equals("Distribution Chart"))
                        showDistributionChart(dataVector);
                     // Pattern Chart.
                     else
                        showPatternChart(dataVector);
                  }      
               }
               
               // Close the connection and re-enable the controls. Let the
               // chartsPanel know to repaint since its contents has changed. 
               
               MyJSQLView_Access.closeConnection(work_dbConnection, "TableFieldChartsPanel executeUpdate()");
               
               recordCountRadioButton.setEnabled(true);
               distributionRadioButton.setEnabled(true);
               patternRadioButton.setEnabled(true);
               tableSelectionComboBox.setEnabled(true);
               columnSelectionComboBox.setEnabled(true);
               statusIndicator.setEnabled(true);
               disableActions = false;
               
               chartsPanel.repaint();
            }
         }, "TableFieldChartsPanel.updateThread");
         updateThread.start();
      }
   }
   
   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //==============================================================
   // Class method to load the current selected table column names.
   //==============================================================

   private void loadColumns(String tableName)
   {
      TableTabPanel selectedTableTabPanel = DBTablesPanel.getTableTabPanel(tableName);

      if (selectedTableTabPanel != null)
      {
         Vector fields = new Vector(selectedTableTabPanel.getAllTableHeadings());

         disableActions = true;
         columnSelectionComboBox.removeAllItems();
         columnSelectionComboBox.setEnabled(false);

         if (fields != null && !fields.isEmpty())
         {
            Iterator columnNamesIterator = fields.iterator();
            while (columnNamesIterator.hasNext())
               columnSelectionComboBox.addItem(columnNamesIterator.next());

            columnNamesHashMap = selectedTableTabPanel.getColumnNamesHashMap();
            columnClassHashMap = selectedTableTabPanel.getColumnClassHashMap();
            columnTypeHashMap = selectedTableTabPanel.getColumnTypeHashMap();
            columnName = (String) columnNamesHashMap.get(columnSelectionComboBox.getItemAt(0));
            
            if (columnName == null)
               columnName = "";
        
            columnSelectionComboBox.setEnabled(true);

            disableActions = false;
            tableColumnsLoaded = true;
         }
      }
   }

   //==============================================================
   // Class Method to create the obtained the needed data from the
   // database for the record count chart.
   //==============================================================

   private double[] getRecordCountValues(Connection dbConnection)
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      
      String identifierQuoteString, schemaTableName;
      double rowCount, distinctNotNull, nullCount, patternCount;
      
      // Setting up instances.
      
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      
      if (tableName.indexOf(".") != -1)
      {
         schemaTableName = identifierQuoteString +
                       tableName.substring(0, tableName.indexOf(".")) +
                       identifierQuoteString + "." + identifierQuoteString +
                       tableName.substring(tableName.indexOf(".") + 1) +
                       identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + tableName + identifierQuoteString;   
      // System.out.println(schemaTableName);
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         
         // Obtain the total record count for the table column.
         
         sqlStatementString = "SELECT COUNT(*) AS row_count FROM " + schemaTableName;
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         rowCount = db_resultSet.getDouble("row_count");
         
         // Obtain the unique record count for the table column.
         
         sqlStatementString = "SELECT COUNT(*) AS " + columnName + " FROM (SELECT DISTINCT "
                              + identifierQuoteString + columnName + identifierQuoteString
                              + " FROM " + schemaTableName + " WHERE "
                              + identifierQuoteString + columnName + identifierQuoteString + " IS NOT NULL";
         if (MyJSQLView_Access.getSubProtocol().indexOf("mysql") != -1 ||
             MyJSQLView_Access.getSubProtocol().indexOf("postgresql") != -1)
            sqlStatementString += ") as AS1";
         else
            sqlStatementString += ")";
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         distinctNotNull = db_resultSet.getDouble(columnName);
         
         // Obtain the null record count for the table column.
         
         sqlStatementString = "SELECT COUNT(*) AS equal_count FROM " + schemaTableName + " WHERE "
                              + identifierQuoteString + columnName + identifierQuoteString + " IS NULL";
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         nullCount = db_resultSet.getDouble("equal_count");
         
         // Obtain the pattern count for the table column.
         
         sqlStatementString = "SELECT COUNT(*) AS row_count FROM (SELECT " + identifierQuoteString + columnName
                              + identifierQuoteString + " FROM " + schemaTableName + " GROUP BY "
                              + identifierQuoteString + columnName + identifierQuoteString + " HAVING COUNT("
                              + identifierQuoteString + columnName + identifierQuoteString + ") > 1";
         if (MyJSQLView_Access.getSubProtocol().indexOf("mysql") != -1 ||
             MyJSQLView_Access.getSubProtocol().indexOf("postgresql") != -1)
            sqlStatementString += ") as AS1";
         else
            sqlStatementString += ")";
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         patternCount = db_resultSet.getDouble(1);
         
         db_resultSet.close();
         sqlStatement.close();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableFieldCharts getRecordCountValues()");
         return null;
      }
      return new double[] {rowCount, distinctNotNull,
                           rowCount != -1.0 ? rowCount - distinctNotNull : -1.0,
                           patternCount, nullCount};
   }
   
   //==============================================================
   // Class Method to create the obtained the needed data from the
   // database for the distribution chart.
   //==============================================================

   private Vector[] getDistributionValues(Connection dbConnection)
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      
      String identifierQuoteString, schemaTableName;
      Vector distributionVector[];
      double rowCountNotNull;
      
      // Setting up instances.
      
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      
      if (tableName.indexOf(".") != -1)
      {
         schemaTableName = identifierQuoteString +
                       tableName.substring(0, tableName.indexOf(".")) +
                       identifierQuoteString + "." + identifierQuoteString +
                       tableName.substring(tableName.indexOf(".") + 1) +
                       identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + tableName + identifierQuoteString;   
      // System.out.println(schemaTableName);
      
      distributionVector = new Vector[2];
      distributionVector[0] = new Vector();
      distributionVector[1] = new Vector();
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         
         // Obtain the total record count for the table column that
         // is not null.
         
         sqlStatementString = "SELECT COUNT(" + identifierQuoteString + columnName + identifierQuoteString
                              + ") AS row_count FROM (SELECT "+ identifierQuoteString + columnName
                              + identifierQuoteString + " FROM " + schemaTableName + " WHERE "
                              + identifierQuoteString + columnName + identifierQuoteString + " IS NOT NULL "
                              + "GROUP BY " + identifierQuoteString + columnName + identifierQuoteString
                              + " HAVING COUNT(" + identifierQuoteString + columnName + identifierQuoteString
                              + ") > 0";
         if (MyJSQLView_Access.getSubProtocol().indexOf("mysql") != -1 ||
             MyJSQLView_Access.getSubProtocol().indexOf("postgresql") != -1)
            sqlStatementString += ") as AS1";
         else
            sqlStatementString += ")";
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         rowCountNotNull = db_resultSet.getDouble("row_count");
         
         if (rowCountNotNull <= 0.0)
            return null;
         
         // Proceed to processing the distribution.
         
         sqlStatementString = "SELECT COUNT(" + identifierQuoteString + columnName + identifierQuoteString
                              + ") AS row_count," + identifierQuoteString + columnName
                              + identifierQuoteString + " AS like_wise FROM " + schemaTableName + " WHERE "
                              + identifierQuoteString + columnName + identifierQuoteString + " IS NOT NULL "
                              + "GROUP BY " + identifierQuoteString + columnName + identifierQuoteString
                              + " HAVING COUNT(" + identifierQuoteString + columnName + identifierQuoteString
                              + ") > 0 ORDER BY " + identifierQuoteString + columnName
                              + identifierQuoteString;
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         int i = 0;
         int currentPass = 0;
         double d1 = rowCountNotNull / 6D,  d2 = 0.0D, d3 = 0.0D;
         double currentMinPatternValue = 0;
         double currentMaxPatternValue = 0;
         
         while (db_resultSet.next())
         {
            rowCountNotNull--;
            
            // Single Pattern
            if (d1 < 1.0D)
            {
               double d4 = db_resultSet.getDouble("row_count");
               String s2 = db_resultSet.getString("like_wise");
               distributionVector[0].add(i, s2);
               distributionVector[1].add(i, new Double(d4));
               if (i == 0)
                  patternMinField = s2;
               if (rowCountNotNull == 0.0D)
                  patternMaxField = s2;
               i++;
            }
            // Multiple Patterns
            else
            {
               double d5 = db_resultSet.getDouble("row_count");
               String currentColumn = db_resultSet.getString("like_wise");
               
               // Handles the collection of the min/max field values
               // in the field pattern chart.
               if (currentPass == 0)
               {
            	   if (d5 != 1.0)
            	   {
            		   currentMinPatternValue = d5;
            		   patternMinField = currentColumn;
            		   currentMaxPatternValue = d5;
            		   patternMaxField = currentColumn;
            		   currentPass++;
            	   }
               }
               else
               {
            	   if (d5 < currentMinPatternValue && d5 != 1.0)
            	   {
            		   patternMinField = currentColumn;
            		   currentMinPatternValue = d5;
            	   }
            	   
            	   if (d5 > currentMaxPatternValue)
            	   {
            		   patternMaxField = currentColumn;
            		   currentMaxPatternValue = d5;
            	   }   
               }
               
               // Proceed with processing pattern distributions.
               d2 += d5;
               
               if (d3 < d1 && rowCountNotNull != 0.0D)
               {
                  d3++;
               }
               else
               {
                  distributionVector[0].add(i, currentColumn);
                  distributionVector[1].add(i, new Double(d2));
                  
                  d2 = 0.0D;
                  d3 = 0.0D;
                  i++;
               }
            }
         }
         db_resultSet.close();
         sqlStatement.close();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableFieldCharts getDistributionValues()");
         return null;
      }
      return distributionVector;
   }
   
   //==============================================================
   // Class Method to create the obtained the needed data from the
   // database for the pattern chart.
   //==============================================================

   private Vector[] getPatternValues(Connection dbConnection)
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      
      String identifierQuoteString, schemaTableName;
      Vector patternVector[];
      
      // Setting up instances.
      
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      
      if (tableName.indexOf(".") != -1)
      {
         schemaTableName = identifierQuoteString +
                       tableName.substring(0, tableName.indexOf(".")) +
                       identifierQuoteString + "." + identifierQuoteString +
                       tableName.substring(tableName.indexOf(".") + 1) +
                       identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + tableName + identifierQuoteString;   
      // System.out.println(schemaTableName);
      
      patternVector = new Vector[2];
      patternVector[0] = new Vector();
      patternVector[1] = new Vector();
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         
         // Obtain the total record count for the table column.
         
         sqlStatementString = "SELECT COUNT(" + identifierQuoteString + columnName + identifierQuoteString
                              + ") AS row_count, " + identifierQuoteString + columnName
                              + identifierQuoteString + " AS like_wise FROM " + schemaTableName
                              + " GROUP BY " + identifierQuoteString + columnName + identifierQuoteString
                              + " HAVING COUNT(" + identifierQuoteString + columnName
                              + identifierQuoteString + ") > 1 ORDER BY 1 DESC";
         // System.out.println(sqlStatementString);
         
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         if (db_resultSet == null)
            return null;
         
         int i = 0;
         while (db_resultSet.next())
         {
            double d = db_resultSet.getDouble("row_count");
            if (d < 1.0D)
               break;
            String s1 = db_resultSet.getString("like_wise");
            patternVector[0].add(i, s1);
            patternVector[1].add(i, new Double(d));
            i++;
         }
         
         if (db_resultSet != null)
            db_resultSet.close();
         sqlStatement.close();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableFieldCharts getPatternValues()");
         return null;
      }
      return patternVector;
   }

   //==============================================================
   // Class Method to have the recordCountChart draw the given set
   // of data.
   //==============================================================

   private void showRecordCountChart(double[] ad)
   {
      // Method Instances
      String as[];
      as = (new String[] {"Total", "Unique", "Repeat", "Pattern", "Null"});
      
      // Load the parameters into the chart panel.
      recordCountChart.setInit();
      recordCountChart.setTitle("Record Count");
      recordCountChart.setTableName(tableName);
      recordCountChart.setColumnName(columnName);
      recordCountChart.setZoomFactor(1.0D);
      
      recordCountChart.setbarLabels(as);
      recordCountChart.setBarValues(ad);
      
      // Draw the chart.
      recordCountChart.drawBarChart();
   }

   //==============================================================
   // Class Method to have the distributionChart draw the given set
   // of data.
   //==============================================================

   private void showDistributionChart(Vector[] avector)
   {
      // Load the parameters into the chart panel.
      distributionChart.setTitle("Distribution Chart");
      distributionChart.setTableName(tableName);
      distributionChart.setColumnName(columnName);
         
      distributionChart.setLabels(avector[0].toArray());
      distributionChart.setSliceValues(avector[1].toArray());
      distributionChart.setMinValue(patternMinField);
         
      // Redraw the chart.
      distributionChart.drawPieChart();
   }

   //==============================================================
   // Class Method to have the distributionChart draw the given set
   // of data.
   //==============================================================

   private void showPatternChart(Vector[] avector)
   {
      // Load the parameters into the chart panel.
      patternChart.setInit();
      patternChart.setTitle("Pattern Information");
      patternChart.setTableName(tableName);
      patternChart.setColumnName(columnName);
      patternChart.setZoomFactor(1.0D);
      
      patternChart.setData(avector);
      patternChart.setMinValue(patternMinField);
      patternChart.setMaxValue(patternMaxField);
      
      // Redraw the chart.
      patternChart.drawBarChart();
   }

   //==============================================================
   // Class Method to reset the panel's table selector comboBox
   // and table cards then reload tables.
   //==============================================================

   protected void reloadPanel(Connection dbConnection, Vector tableNames)
   {
      // Insure no actions are taken during reload.
      tableSelectionComboBox.setEnabled(false);

      tableColumnsLoaded = false;
      disableActions = true;

      // Clear the components of old data.
      tableSelectionComboBox.removeAllItems();
      columnSelectionComboBox.removeAllItems();

      // Try reloading tables.
      if (!tableNames.isEmpty())
      {
         Iterator tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
            tableSelectionComboBox.addItem(tableNamesIterator.next());

         tableName = (String) tableSelectionComboBox.getItemAt(0);
         loadColumns((String) tableSelectionComboBox.getItemAt(0));

         tableSelectionComboBox.setEnabled(true);
      }
      disableActions = false;
   }
}