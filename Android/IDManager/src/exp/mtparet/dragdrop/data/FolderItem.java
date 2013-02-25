package exp.mtparet.dragdrop.data;

public class FolderItem {

	private int folderImgid;
	private int folderId;
	private int folderIconId;
	private int imgFolderType;

	public FolderItem(FolderItem folder) {
		this.folderImgid = folder.folderImgid;
		this.folderIconId = folder.folderIconId;
		this.imgFolderType = folder.imgFolderType;
		this.folderId = folder.folderId;
	}

	public FolderItem(int folderId,int folderImgid, int folderIconId, int imgFolderType) {
		this.folderImgid = folderImgid;
		this.folderIconId = folderIconId;
		this.imgFolderType = imgFolderType;
		this.folderId = folderId;
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

	public int getFolderImgid() {
		return folderImgid;
	}

	public void setFolderImgid(int folderImgid) {
		this.folderImgid = folderImgid;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
}
