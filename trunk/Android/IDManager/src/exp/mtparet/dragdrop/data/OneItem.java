/*
*Copyright 2011 Matthieu Paret
*
*This file is part of DragAndDrop.
*
*DragAndDrop is free software: you can redistribute it and/or modify
*it under the terms of the GNU Lesser General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*DragAndDrop is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License
*along with DragAndDrop.  If not, see <http://www.gnu.org/licenses/>.
*/

package exp.mtparet.dragdrop.data;

public class OneItem {
	
	private int id;
	private String name;
	private String url;
	
	public OneItem(OneItem op){
		this.id = op.id;
		this.name = op.name;
	}
	
	public OneItem(int id, String name,String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
