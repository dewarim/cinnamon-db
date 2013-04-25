// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007-2013 Texolution GmbH (http://texolution.eu)
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
// (or visit: http://www.gnu.org/licenses/lgpl.html)

package cinnamon;

import cinnamon.exceptions.CinnamonConfigurationException;
import cinnamon.exceptions.CinnamonException;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlConn {

	private Connection conn_;
	
	public SqlConn(String connectionString, String jdbcdriver) throws CinnamonException {
		try {
			Class.forName(jdbcdriver).newInstance();
			conn_ = 
				DriverManager.getConnection(connectionString);
		} catch (Exception e) {
			throw new CinnamonConfigurationException("Could not create SqlConn: " + e.getMessage(), e);
		}
	}
	
	public Connection getConnection() {
		return conn_;
	}
	
}
