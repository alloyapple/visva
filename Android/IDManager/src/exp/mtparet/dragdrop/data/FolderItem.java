package exp.mtparet.dragdrop.data;

public class FolderItem {

	private int folderImgid;
	private int folderId;
	private int folderIconId;
	private int imgFolderType;
	private String textFolderName;

	public FolderItem(FolderItem folder) {
		this.folderImgid = folder.folderImgid;
		this.folderIconId = folder.folderIconId;
		this.imgFolderType = folder.imgFolderType;
		this.folderId = folder.folderId;
		this.textFolderName = folder.textFolderName;
	}

	public FolderItem(int folderId, int folderImgid, int folderIconId, String textFolderName,
			int imgFolderType) {
		this.folderImgid = folderImgid;
		this.folderIconId = folderIconId;
		this.imgFolderType = imgFolderType;
		this.folderId = folderId;
		this.textFolderName = textFolderName;
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

	public String getTextFolderName() {
		return textFolderName;
	}

	public void setTextFolderName(String textFolderName) {
		this.textFolderName = textFolderName;
	}
}
