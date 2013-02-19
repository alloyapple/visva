package visvateam.outsource.idmanager.database;

public class FolderDatabase {
	// all variables
	private int folderId;
	private int userId;
	private String folderName;
	private int imgFolderId;
	private int imgFolderIconId;
	private boolean isNormalFolder;

	// constructor
	public FolderDatabase() {

	}

	// constructor
	public FolderDatabase(int folderId, int userId, String folderName,
			int imgFolderId, int imgFolderIconId, boolean isNormalFolder) {
		this.folderId = folderId;
		this.userId = userId;
		this.folderName = folderName;
		this.imgFolderId = imgFolderId;
		this.imgFolderIconId = imgFolderIconId;
		this.isNormalFolder = isNormalFolder;
	}

	// constructor
	public FolderDatabase(int userId, String folderName, int imgFolderId,
			int imgFolderIconId, int imgFolerEditId, boolean isNormalFolder) {
		this.userId = userId;
		this.folderName = folderName;
		this.imgFolderId = imgFolderId;
		this.imgFolderIconId = imgFolderIconId;
		this.isNormalFolder = isNormalFolder;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folerId) {
		this.folderId = folerId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getImgFolderId() {
		return imgFolderId;
	}

	public void setImgFolderId(int imgFolderId) {
		this.imgFolderId = imgFolderId;
	}

	public int getImgFolderIconId() {
		return imgFolderIconId;
	}

	public void setImgFolderIconId(int imgFolderIconId) {
		this.imgFolderIconId = imgFolderIconId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isNormalFolder() {
		return isNormalFolder;
	}

	public void setNormalFolder(boolean isNormalFolder) {
		this.isNormalFolder = isNormalFolder;
	}

}
