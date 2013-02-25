package exp.mtparet.dragdrop.data;

public class FolderItem {

	private int id;
	private int folderIconId;
	private int imgFolderType;

	public FolderItem(FolderItem folder) {
		this.id = folder.id;
		this.folderIconId = folder.folderIconId;
		this.imgFolderType = folder.imgFolderType;
	}

	public FolderItem(int id, int folderIconId, int imgFolderType) {
		this.id = id;
		this.folderIconId = folderIconId;
		this.imgFolderType = imgFolderType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFolderIconId() {
		return folderIconId;
	}

	public void setFolderIconId(int folderIconId) {
		this.folderIconId = folderIconId;
	}

	public int getImgFolderType() {
		return imgFolderType;
	}

	public void setImgFolderType(int imgFolderType) {
		this.imgFolderType = imgFolderType;
	}
}
