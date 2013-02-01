//=================================================================
//                 MyJSQLView FILE_PluginRepository.
//=================================================================
//
//    This class provides the general framework to create a File
// type repository that would be derived from a local or networked
// file system.
//
//                 << FILE_PluginRepository.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.2 10/11/2012
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
// Version 1.0 Initial MyJSQLView FILE_PluginRepository Class.
//         1.1 Added Return boolean Type for setRepository() As Defined With
//             PluginRepositoryInterface.
//         1.2 Inserted Place Holder for PluginRepositoryInterface Requirement
//             refresh();
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

/**
 *    The FILE_PluginRepository class provides the general framework to
 * create a File type repository that would be derived from a local or
 * networked file system.
 * 
 * @author Dana M. Proctor
 * @version 1.2 10/11/2012
 */

public class FILE_PluginRepository extends PluginRepository
{
   // Class Instances
   
   
   
   //==============================================================
   // FILE_PluginRepository Constructor
   //==============================================================

   public FILE_PluginRepository()
   {
      super();
   }
   
   //==============================================================
   // Class method to setup up the repository.
   // The argument should be a reference to a network location.
   // exp. http://dandymadeproductions.com/
   //==============================================================

   public boolean setRepository(String repository)
   {
      return true;
   }
   
   //==============================================================
   // Class method to refresh the repository by trying to download
   // the plugin list and reading again.
   //==============================================================
   
   public void refresh()
   {
      
   }
}
