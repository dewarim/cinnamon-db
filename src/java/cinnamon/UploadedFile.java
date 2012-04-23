// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007 Dr.-Ing. Boris Horner
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

package cinnamon;

public class UploadedFile {

	private String fileBufferPath_;
	private String name_;
	private String fileName_;
	private String type_;
	private long length_;
	
	public UploadedFile(String fileBufferPath, String name, String fileName, String type, long len) {
		fileBufferPath_=fileBufferPath;
		name_=name;
		fileName_=fileName;
		type_=type;
		length_=len;
	}
	
	public String getFileBufferPath() {
		return fileBufferPath_;	// path of uploaded file in file-buffer
	}

	public String getName() {
		return name_;	// parameter name
	}

	public String getFileName() {
		return fileName_;	// original file name
	}

	public String getType() {
		return type_;
	}

	public long getLength() {
		return length_;
	}
}
