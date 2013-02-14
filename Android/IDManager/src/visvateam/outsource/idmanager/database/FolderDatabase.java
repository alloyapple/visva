package visvateam.outsource.idmanager.database;

public class FolderDatabase {
	// all variables
	private int folderId;
	private int userId;
	private String folderName;
	private int imgFolderId;
	private int imgFolderIconId;
	private int imgFolderEditId;

	// constructor
	public FolderDatabase() {

	}

	// constructor
	public FolderDatabase(int folderId,int userId, String folderName, int imgFolderId,
			int imgFolderIconId, int imgFolerEditId) {
		this.folderId = folderId;
		this.userId = userId;
		this.folderName = folderName;
		this.imgFolderId = imgFolderId;
		this.imgFolderIconId = imgFolderIconId;
		this.imgFolderEditId = imgFolerEditId;
	}

	// constructor
	public FolderDatabase(int userId,String folderName, int imgFolderId,
			int imgFolderIconId, int imgFolerEditId) {
		this.userId = userId;
		this.folderName = folderName;
		this.imgFolderId = imgFolderId;
		this.imgFolderIconId = imgFolderIconId;
		this.imgFolderEditId = imgFolerEditId;
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

	public int getImgFolderEditId() {
		return imgFolderEditId;
	}

	public void setImgFolderEditId(int imgFolderEditId) {
		this.imgFolderEditId = imgFolderEditId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
