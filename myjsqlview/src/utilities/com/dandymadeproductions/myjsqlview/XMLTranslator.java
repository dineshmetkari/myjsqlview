//=================================================================
//                   XMLTranslator Class
//=================================================================
//    This class handles the translation authority over
// reading and writing XML content, site connection parameters,
// from/to the myjsqlview.xml file.
//
//                  << XMLTranslator.java >>
//
//=================================================================
// Copyright (C) 2006-2011 Nil_lin, Dana Proctor
// Version 4.7 01/12/2011
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
// Version 1.0 11/27/2006 Initial XMLTranslator, Nil_lin.
//         1.1 12/01/2006 Initial Integration Into MyJSQLView,
//                        Open Software Header and Version
//                        Indicator Comments.
//         1.2 12/10/2006 Completely Rebuilt to Meet Requirements
//                        Specified by the Task. myjsqlview.xml
//                        Configuration File. Spec. Not Met.
//         1.2 12/10/2006 Class Method getSites() Completed.
//         1.3 12/14/2006 Class Method getSites() Added "Database"
//                        NodeValue Setting in SiteParameter.
//         1.4 12/15/2006 Robustnous in Handling XML Document Handling,
//                        Class Method displayErrors() & Class
//                        Instance errorInTranslation.
//         1.5 12/15/2006 File Handling .myjsqlview.xml and Sample
//                        myjsqlview.xml.
//         1.6 12/16/2006 Made .myjsqlview Directory in User Home
//                        and Loaded Sample myjsqlview.xml to That
//                        Location Under myjsqlview.xml Name.
//         1.7 12/16/2006 Implemented Class Method getLastSite().
//         1.8 12/16/2006 Implemented Class Method setLastSite();
//         1.9 12/16/2006 Added Class Method saveXML() Per Nil.
//         2.0 12/22/2006 Modified Class Method getSites() to Place
//                        the Sites Key as Concatenation of SiteName
//                        Database.
//         2.1 01/18/2007 Completed Class Method setSites().
//         2.2 01/19/2007 Removed '/n' From XML File for Removed
//                        Childnodes in Class Method saveXML().
//         2.3 01/19/2007 Reviewed Implementation and Cleaned/Commented.
//                        Fixed Class Method setSites() password to "".
//         2.4 01/19/2007 Class Method saveXML() Fixed newFileContent
//                        String to Start with "<".
//         2.5 01/21/2007 Class WriteDataFile Boolean Argument Addition,
//                        false.
//         2.6 01/22/2007 Class ReadDataFile Boolean Argument Addition,
//                        false.
//         2.7 04/19/2007 Saved Password.
//         2.8 09/06/2007 Cleaned Out Some Unused Instances.
//         2.9 09/08/2007 Code Cleanup.
//         3.0 09/09/2007 Removed New String Creation for currentKey in
//                        Class Method setSites(). Final Class Instances
//                        xmlFileName & sampleXMLFileName to Static.
//         3.1 10/18/2007 Removed Instantiation of Instance currentParameter
//                        in Class Method setSites().
//         3.2 12/12/2007 Header Update.
//         3.3 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         3.4 01/03/2009 Class Name Change SiteParameter to SiteParameters.
//         3.5 05/27/2009 Header Format Changes/Update.
//         3.6 10/25/2009 Constructor Instance myjsqlviewXMLDirectory Obtained
//                        From MyJSQLView_Utils Class.
//         3.7 10/31/2009 Added Class Instance fileError. Check in Constructor
//                        to Halt XML Processing on This Instance Error. Added
//                        Class Method getXMLTranslatorResult() to Indicate Status
//                        of Processing XML.
//         3.8 12/03/2009 Reviewed Code to Understand Why Vista, and Win98 Might
//                        be Giving XML Loading Errors. In the Case of Win98 Continues
//                        to Add Dupulicate Characters to End of myjsqlview.xml
//                        File. Still Think That Has to Due With OS Not Properly
//                        Flushing Stream When Writing. In Case of Vista to Not
//                        Enought Feedback to Do Anything. Normalized xmlDocument
//                        in setSites() and setLastSite(). Tested Again With Linux
//                        and XP Works.
//         3.9 02/06/2010 Constructor Check for SecurityException in Creation of the
//                        .myjsqlview Directory. Assumed fileError = true. Set 
//                        errorInTranslation and returned.
//         4.0 02/08/2010 Class Method SaveXML() newFileContent Changed to StringBuffer.
//                        Class Method textConversion() returnString Changed to
//                        StringBuffer. Class Method setSites() Added Instance 
//                        passwordBuffer.
//         4.1 02/15/2010 Class Method textConversion() Returned Empty String on Conditional
//                        Test. Class Method setSites() passwordBufferString Proper Conversion
//                        to passwordString and Check for Empty String.
//         4.2 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         4.3 04/07/2010 Corrections in Class Method textConversion().
//         4.4 05/16/2010 Parameterized Instance sites in Class Method getSites() and Argument
//                        to setSites() to Bring Code Into Compliance With Java 5.0 API.
//         4.5 05/18/2010 Minor Format Changes.
//         4.6 05/20/2010 Parameterized siteKeys in Class Method setSites().
//         4.7 01/12/2011 Class Method getSites() Changed currentSiteName Instance to a
//                        StringBuffer.
//
//-----------------------------------------------------------------
//                 nil_lin@users.sourceforge.net
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *    The XMLTranslator class handles the translation authority over
 * reading and writing XML content, site connection parameters,
 * from/to the myjsqlview.xml file.
 * 
 * @author Nil, Dana M. Proctor
 * @version 4.7 01/12/2011
 */

class XMLTranslator
{
   // Class Instances
   private String myjsqlviewXMLFileString;
   private static final String xmlFileName = "myjsqlview.xml";
   private static final String sampleXMLFileName = "myjsqlview.xml";

   private boolean xmlValidate, fileError, errorInTranslation;
   private Document xmlDocument;

   //==============================================================
   // XMLTranslator Constructor
   //==============================================================

   protected XMLTranslator()
   {
      // Class instances.
      String myjsqlviewXMLDirectory;
      String errorString;

      // Setting up basic instances.
      xmlValidate = false;
      fileError = false;
      errorInTranslation = false;

      // Setting up XML conection paramters file as needed.
      // If the users home directory .myjsqlivew.xml file is
      // not present then the sample myjsqlview.xml file is
      // used as sample to create. This code should take
      // care of installing the app as generic for users
      // on system. ex. linux /usr/lib or XP program files.

      myjsqlviewXMLDirectory = MyJSQLView_Utils.getMyJSQLViewDirectory();
      
      myjsqlviewXMLFileString = myjsqlviewXMLDirectory
                                + MyJSQLView_Utils.getFileSeparator() 
                                + xmlFileName;

      // System.out.println(myjsqlviewXMLFileString);

      // Make the director if does not exist.
      File myjsqlviewXMLDirectoryFile = new File(myjsqlviewXMLDirectory);
      if (!myjsqlviewXMLDirectoryFile.isDirectory())
      {
         try
         {
            fileError = !myjsqlviewXMLDirectoryFile.mkdir();
         }
         catch (SecurityException se)
         {
            errorString = "File Error: Failed to create .myjsqlview directory.\n" + se;
            displayErrors(errorString);
            errorInTranslation = true;
            return;
         }
      }

      File myjsqlviewXMLFile = new File(myjsqlviewXMLFileString);

      try
      {
         if (myjsqlviewXMLFile.createNewFile())
         {
            // System.out.println("File Does Not Exist, Creating.");
            byte[] xmlSampleFileData = ReadDataFile.mainReadDataString(sampleXMLFileName, false);

            if (xmlSampleFileData != null)
               WriteDataFile.mainWriteDataString(myjsqlviewXMLFileString, xmlSampleFileData, false);
            else
            {
               errorString = "Failed to Open Sample myjsqlview.xml File.\n";
               displayErrors(errorString);
               fileError = true;
            }
         }
      }
      catch (IOException ioe)
      {
         errorString = "XMLTranslator Constructor: Error in creating home directory " 
                       + ".myjsqlview.xml file.\n" + ioe;
         displayErrors(errorString);
         fileError = true;
      }

      // Try to create a document container that will hold the XML
      // configuration parameters from the file myjslqview.xml.
      
      if (!fileError)
      {
         try
         {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(xmlValidate);

            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDocument = builder.parse(myjsqlviewXMLFile);
            xmlDocument.normalize();
            //xmlDocument.getDocumentElement().normalize();
         }
         catch (SAXException sxe)
         {
            errorString = "XMLTranslator Constructor: Error in parsing File.\n" + sxe;
            displayErrors(errorString);
            errorInTranslation = true;
         }
         catch (ParserConfigurationException pce)
         {
            errorString = "XMLTranslator Constructor: Error in constructing DocumentBuilder.\n " + pce;
            displayErrors(errorString);
            errorInTranslation = true;
         }
         catch (IOException ioe)
         {
            errorString = "XMLTranslator Constructor: Error in Creating File.\n" + ioe;
            displayErrors(errorString);
            errorInTranslation = true;
         }
      }
   }

   //==============================================================
   // Class method to write the document object into the xml file.
   //==============================================================

   private void saveXML()
   {
      // Class method instances.
      String errorString;

      try
      {
         TransformerFactory factory = TransformerFactory.newInstance();
         Transformer trans = factory.newTransformer();
         trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         trans.setOutputProperty(OutputKeys.INDENT, "yes");

         DOMSource source = new DOMSource(xmlDocument);

         PrintWriter pw = new PrintWriter(new FileOutputStream(myjsqlviewXMLFileString));
         StreamResult result = new StreamResult(pw);
         trans.transform(source, result);
      }
      catch (TransformerException tfe)
      {
         errorString = "XMLTranslator saveXML(): Failed to transform object to XML file.\n" + tfe;
         displayErrors(errorString);
      }
      catch (IOException ioe)
      {
         errorString = "XMLTranslator saveXML(): Failed to create File to store XML result.\n" + ioe;
         displayErrors(errorString);
      }

      // Spent several hours trying to remove newlines in removal of child nodes
      // in the sites elements. The DOMConfiguration does not allow the access
      // to set the "element-content-whitespace" parameter for the DOM Document.
      // Unable to access the bytes, string of the document to remove. Also not
      // able to access at printwriter. Only other methods is to not
      // removeChilds,
      // but to replaceChild. This would require tracking the rmoved nodes.
      // Other
      // possible option create doc fragment.
      // Did not expore these other options, finally resorted to brute force
      // removing the newlines through re-reading the file and then filtering
      // and
      // re-saving. I did not like this approach, but everythings works and
      // would
      // like to release by end of month. 01/19/2007. May revisit later.

      byte[] xmlFileBytes = ReadDataFile.mainReadDataString(myjsqlviewXMLFileString, false);
      if (xmlFileBytes != null)
      {
         int inByte;
         StringBuffer newFileContent;
         
         // New file content, minus newlines.
         // > = 62 newline(LF,CarrigeReturn,/n) = 10
         newFileContent = new StringBuffer();
         newFileContent.append("<");

         for (int i = 0; i < xmlFileBytes.length; i++)
         {
            if (i != 0)
            {
               // Make sure only filter newlines that are not
               // at the end of an element.
               if (!Byte.toString(xmlFileBytes[i - 1]).equals("62")
                   && Byte.toString(xmlFileBytes[i]).equals("10"))
               {
                  // These are the newlines.
                  // System.out.println("newLine");
               }
               else
               {
                  inByte = Integer.parseInt(xmlFileBytes[i] + "");
                  newFileContent.append((char) inByte);
               }
            }
         }
         WriteDataFile.mainWriteDataString(myjsqlviewXMLFileString, (newFileContent.toString()).getBytes(), false);
         // System.out.println(newFileContent);
      }
   }

   //==============================================================
   // Class method to display an alert dialog indicating the
   // type of error that occurred during the attempt of
   // creating a XMLTranslator().
   //==============================================================

   protected void displayErrors(String errorString)
   {
      JOptionPane.showMessageDialog(null, errorString, "Alert", JOptionPane.ERROR_MESSAGE);
   }

   //==============================================================
   // Class method to get the Hashtable SiteParameter objects from
   // the XML file.
   //==============================================================
   
   protected Hashtable<String, SiteParameters> getSites()
   {
      // Class Method Instance.
      Hashtable<String, SiteParameters> sites;
      SiteParameters currentSiteParameter;
      NodeList siteElements;
      Node currentSite;
      NamedNodeMap currentSiteAttributes;
      StringBuffer currentSiteName;

      // Setting up some of the class instances.
      sites = new Hashtable <String, SiteParameters>();

      // Finding the site nodes and then
      // setting each sites' attributes.

      if (!errorInTranslation)
      {
         siteElements = xmlDocument.getElementsByTagName("Site");
         int i = 0;
         while (i < siteElements.getLength())
         {
            currentSiteParameter = new SiteParameters();

            currentSite = siteElements.item(i);
            currentSiteAttributes = currentSite.getAttributes();

            // Create the key that will be used in the hashtable.
            // Allows the creation of a single level folder for
            // sites in MyJSQLView_Access JMenu and ConnnectionManager JTree.

            currentSiteName = new StringBuffer();
            currentSiteName.append(currentSiteAttributes.getNamedItem("Name").getNodeValue());
            currentSiteName.append("#" + currentSiteAttributes.getNamedItem("Database").getNodeValue());
            // System.out.println(currentSiteName);

            // Filling the site parameter object.

            currentSiteParameter.setSiteName(currentSiteName.toString());
            currentSiteParameter.setDriver(currentSiteAttributes.getNamedItem("Driver").getNodeValue());
            currentSiteParameter.setProtocol(currentSiteAttributes.getNamedItem("Protocol").getNodeValue());
            currentSiteParameter.setSubProtocol(currentSiteAttributes.getNamedItem("SubProtocol").getNodeValue());
            currentSiteParameter.setHost(currentSiteAttributes.getNamedItem("Host").getNodeValue());
            currentSiteParameter.setPort(currentSiteAttributes.getNamedItem("Port").getNodeValue());
            currentSiteParameter.setDatabase(currentSiteAttributes.getNamedItem("Database").getNodeValue());
            currentSiteParameter.setUser(currentSiteAttributes.getNamedItem("User").getNodeValue());
            currentSiteParameter.setPassword(textConversion(currentSiteAttributes.getNamedItem("Password").getNodeValue().toCharArray(), true).toCharArray());
            currentSiteParameter.setSsh(currentSiteAttributes.getNamedItem("SSH").getNodeValue());

            // Placing the SiteParameter object in the sites hashtable.
            sites.put(currentSiteName.toString(), currentSiteParameter);
            i++;
         }
      }
      return sites;
   }

   //==============================================================
   // Class method to get the last SiteParameter used in the
   // MyJSQLView application.
   //==============================================================
   
   protected SiteParameters getLastSite()
   {
      // Class Method Instances
      Node root, currentChild;
      NodeList children, currentSettingsNodes;
      NamedNodeMap currentItemAttributes;

      SiteParameters lastSite = new SiteParameters();

      root = xmlDocument.getFirstChild();
      children = root.getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
         // Finding the Settings Node.
         currentChild = (Node) children.item(i);
         if (currentChild.getNodeName().equals("Settings"))
         {
            currentSettingsNodes = currentChild.getChildNodes();

            // Cycling through each child node of Settings Node
            // and checking to insure getting a Item.
            for (int index = 0; index < currentSettingsNodes.getLength(); index++)
            {
               if (currentSettingsNodes.item(index).getNodeName().equals("Item") &&
                   currentSettingsNodes.item(index).getNodeType() == Node.ELEMENT_NODE)
               {
                  Node value = currentSettingsNodes.item(index).getFirstChild();
                  if (value != null)
                  {
                     // Setting the last site parameters.
                     currentItemAttributes = currentSettingsNodes.item(index).getAttributes();
                     String name = currentItemAttributes.item(0).getNodeValue();
                     // System.out.println(name);

                     if (name.equals("Last Server Name"))
                        lastSite.setSiteName(value.getNodeValue());
                     if (name.equals("Last Driver"))
                        lastSite.setDriver(value.getNodeValue());
                     if (name.equals("Last Protocol"))
                        lastSite.setProtocol(value.getNodeValue());
                     if (name.equals("Last SubProtocol"))
                        lastSite.setSubProtocol(value.getNodeValue());
                     if (name.equals("Last Host"))
                        lastSite.setHost(value.getNodeValue());
                     if (name.equals("Last Port"))
                        lastSite.setPort(value.getNodeValue());
                     if (name.equals("Last Database"))
                        lastSite.setDatabase(value.getNodeValue());
                     if (name.equals("Last User"))
                        lastSite.setUser(value.getNodeValue());
                     if (name.equals("Last Password"))
                        lastSite.setPassword(" ".toCharArray());
                     if (name.equals("Last SSH"))
                        lastSite.setSsh(value.getNodeValue());
                  }
               }
            }
         }
      }
      return lastSite;
   }
   
   //==============================================================
   // Class method to get the status result of the XML Translation.
   //==============================================================

   protected boolean getXMLTranslatorResult()
   {
      if (fileError || errorInTranslation)
         return false;
      else
         return true;
   }

   //==============================================================
   // Class method to set the amended or new SiteParameter objects
   // into the XML file.
   //==============================================================
   
   protected void setSites(Hashtable<String, SiteParameters> sites)
   {
      // Class Method Instances
      Node root;
      Node currentChild;
      NodeList children;

      root = xmlDocument.getFirstChild();
      children = root.getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
         // Finding the Sitess Node.
         currentChild = (Node) children.item(i);
         if (currentChild.getNodeName().equals("Sites"))
         {
            NodeList sitesList = currentChild.getChildNodes();

            // Remove all the current site definitions
            // from the Sites Node.
            for (int j = 0; j < sitesList.getLength(); j++)
            {
               if (sitesList.item(j).getNodeName().equals("Site"))
                  currentChild.removeChild(sitesList.item(j));
            }
            currentChild.normalize();

            // Cycle through the new sites list and adding
            // to the Sites node.
            Enumeration<String> sitesKeys = sites.keys();

            while (sitesKeys.hasMoreElements())
            {
               String currentKey = sitesKeys.nextElement();
               SiteParameters currentParameter = sites.get(currentKey);

               Element currentSiteElement = xmlDocument.createElement("Site");

               String siteName = currentParameter.getSiteName();
               int poundIndex = siteName.indexOf('#');

               if (poundIndex != -1)
               {
                  siteName = siteName.substring(0, poundIndex);
                  // System.out.println(siteName);

                  currentSiteElement.setAttribute("Name", siteName);
                  currentSiteElement.setAttribute("Driver", currentParameter.getDriver());
                  currentSiteElement.setAttribute("Protocol", currentParameter.getProtocol());
                  currentSiteElement.setAttribute("SubProtocol", currentParameter.getSubProtocol());
                  currentSiteElement.setAttribute("Host", currentParameter.getHost());
                  currentSiteElement.setAttribute("Port", currentParameter.getPort());
                  currentSiteElement.setAttribute("Database", currentParameter.getDatabase());
                  currentSiteElement.setAttribute("User", currentParameter.getUser());

                  if (currentParameter.getPassword().length != 0)
                  {
                     String password;
                     StringBuffer passwordBuffer = new StringBuffer();
                     
                     password = "";
                     char[] passwordCharacters = currentParameter.getPassword();
                     
                     for (int j = 0; j < passwordCharacters.length; j++)
                        passwordBuffer.append(passwordCharacters[j]);
                     
                     if ((passwordBuffer.toString()).trim().equals(""))
                        password = "";
                     else
                        password = passwordBuffer.toString();
                     
                     currentSiteElement.setAttribute("Password", textConversion(password.toCharArray(), false));
                  }
                  else
                     currentSiteElement.setAttribute("Password", "");

                  currentSiteElement.setAttribute("SSH", currentParameter.getSsh());

                  currentChild.appendChild(currentSiteElement);
               }
            }
         }
      }
      xmlDocument.normalize();
      saveXML();
   }

   //==============================================================
   // Class method to set the last site loaded SiteParameter object
   // into the XML file.
   //==============================================================

   protected void setLastSite(SiteParameters site)
   {
      // Class Method Instances
      Node root, currentChild;
      NodeList children, currentSettingsNodes;
      NamedNodeMap currentItemAttributes;

      root = xmlDocument.getFirstChild();
      children = root.getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
         // Finding the Settings Node.
         currentChild = (Node) children.item(i);
         if (currentChild.getNodeName().equals("Settings"))
         {
            currentSettingsNodes = currentChild.getChildNodes();

            // Cycling through each child node of the Settings Node
            // and checking to insure getting a Item.
            for (int index = 0; index < currentSettingsNodes.getLength(); index++)
            {
               if (currentSettingsNodes.item(index).getNodeName().equals("Item") &&
                   currentSettingsNodes.item(index).getNodeType() == Node.ELEMENT_NODE)
               {
                  Node value = currentSettingsNodes.item(index).getFirstChild();
                  if (value != null)
                  {
                     // Setting the last site parameters.
                     currentItemAttributes = currentSettingsNodes.item(index).getAttributes();
                     String name = currentItemAttributes.item(0).getNodeValue();

                     if (name.equals("Last Server Name"))
                        value.setNodeValue(site.getSiteName());
                     if (name.equals("Last Driver"))
                        value.setNodeValue(site.getDriver());
                     if (name.equals("Last Protocol"))
                        value.setNodeValue(site.getProtocol());
                     if (name.equals("Last SubProtocol"))
                        value.setNodeValue(site.getSubProtocol());
                     if (name.equals("Last Host"))
                        value.setNodeValue(site.getHost());
                     if (name.equals("Last Port"))
                        value.setNodeValue(site.getPort());
                     if (name.equals("Last Database"))
                        value.setNodeValue(site.getDatabase());
                     if (name.equals("Last User"))
                        value.setNodeValue(site.getUser());
                     if (name.equals("Last Password"))
                        value.setNodeValue(" ");
                     if (name.equals("Last SSH"))
                        value.setNodeValue(site.getSsh());
                  }
               }
            }
         }
      }
      xmlDocument.normalize();
      saveXML();
   }
   
   //==============================================================
   // Changes text to a standard format.
   //==============================================================

   private String textConversion(char[] theseCharacters, boolean which)
   {
      // Class Method Instances.
      char[] myCharacters;
      
      myCharacters = MyJSQLView_Utils.getStandardCharacters();
      try
      {
         if (System.getProperty("os.name").length() > 2)
            myCharacters[4] = System.getProperty("os.name").charAt(0);
         if (System.getProperty("os.arch").length() > 2)
            for (int i=0; i<2; i++)
               myCharacters[i==0?2:7] = System.getProperty("os.arch").charAt(i);
         if (System.getProperty("os.version").length() > 3)
         {
            for (int i=0; i<2; i++)
               myCharacters[i==0?4:11] = System.getProperty("os.version").charAt(i);
            myCharacters[18] = System.getProperty("os.version").charAt(2);
         }
         else if (System.getProperty("os.version").length() > 2)
               myCharacters[17] = System.getProperty("os.version").charAt(1);
         if (System.getProperty("user.name").length() > 4)
            for (int i=3; i>=0; i--)
               myCharacters[myCharacters.length - (i+2)] = System.getProperty("user.name").charAt(i);
         if (System.getProperty("user.home").length() > 4)
            myCharacters[6] = System.getProperty("user.home").charAt(3);
      }
      catch (SecurityException se)
      {
         // Well tried.
      }
      
      char[] ch1 = new char[theseCharacters.length];
      int stop = myCharacters.length;
      int index, currentPosition, ch;
      StringBuffer returnString;

      // Begin
      index = 0;
      currentPosition = 0;

      if (which)
      {
         while (currentPosition < theseCharacters.length)
         {
            if (index >= stop)
               index = 0;
            ch1[currentPosition] = (char) (((theseCharacters[currentPosition] + myCharacters[index++]) - 32) % 94 + 32);
            currentPosition++;
         }
      }
      else
      {
         while (currentPosition < theseCharacters.length)
         {
            if (index >= stop)
               index = 0;
            ch = theseCharacters[currentPosition] - myCharacters[index++];
            while (ch < 32)
               ch += 94;
            ch1[currentPosition] = (char) ch;
            currentPosition++;
         }
      }

      returnString = new StringBuffer();
      
      for (int i = 0; i < ch1.length; i++)
         returnString.append(ch1[i]);
      
      if (returnString.length() == 0)
         return "";
      else
         return returnString.toString();
   }
}