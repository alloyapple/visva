
package exp.mtparet.dragdrop.data;

/**
 * @author visva-team
 *
 */
public class OneItem {

	private int iconId;
	private String name;
	private String url;
	private int passwordId;

	public OneItem(OneItem op) {
		this.iconId = op.iconId;
		this.name = op.name;
		this.url = op.url;
		this.passwordId = op.passwordId;
	}

	public OneItem(int passwordId, int iconId, String name, String url) {
		this.passwordId = passwordId;
		this.iconId = iconId;
		this.name = name;
		this.url = url;
	}

	public int getIconId() {
		return this.iconId;
	}

	public String getName() {
		return this.name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPasswordId() {
		return passwordId;
	}

	public void setPasswordId(int passwordId) {
		this.passwordId = passwordId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public void setName(String name) {
		this.name = name;
	}

}
