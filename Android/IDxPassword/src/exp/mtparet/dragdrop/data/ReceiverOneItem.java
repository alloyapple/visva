/*
 *Copyright 2011 Visvateam
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

public class ReceiverOneItem {

	private int idImgFolder;
	private int idImgFolderIcon;
	private int idImgEdit;

	private String name;

	public ReceiverOneItem(ReceiverOneItem op) {
		this.idImgFolder = op.idImgFolder;
		this.idImgFolderIcon = op.idImgFolderIcon;
		this.idImgEdit = op.idImgEdit;
	}

	public ReceiverOneItem(int idImgFolder, int idImgFolderIcon, int idImgEdit,
			String name) {
		this.idImgFolder = idImgEdit;
		this.idImgFolderIcon = idImgFolderIcon;
		this.idImgEdit = idImgEdit;
	}

	public String getName() {
		return this.name;
	}

	public int getIdImgFolder() {
		return idImgFolder;
	}

	public void setIdImgFolder(int idImgFolder) {
		this.idImgFolder = idImgFolder;
	}

	public int getIdImgFolderIcon() {
		return idImgFolderIcon;
	}

	public void setIdImgFolderIcon(int idImgFolderIcon) {
		this.idImgFolderIcon = idImgFolderIcon;
	}

	public int getIdImgEdit() {
		return idImgEdit;
	}

	public void setIdImgEdit(int idImgEdit) {
		this.idImgEdit = idImgEdit;
	}

	public void setName(String name) {
		this.name = name;
	}

}
