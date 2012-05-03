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

    private String fileBufferPath;
    private String name;
    private String fileName;
    private String type;
    private long length;

    public UploadedFile(String fileBufferPath, String name, String fileName, String type, long len) {
        this.fileBufferPath = fileBufferPath;
        this.name = name;
        this.fileName = fileName;
        this.type = type;
        length = len;
    }

    public String getFileBufferPath() {
        return fileBufferPath;    // path of uploaded file in file-buffer
    }

    public String getName() {
        return name;    // parameter name
    }

    public String getFileName() {
        return fileName;    // original file name
    }

    public String getType() {
        return type;
    }

    public long getLength() {
        return length;
    }
}
