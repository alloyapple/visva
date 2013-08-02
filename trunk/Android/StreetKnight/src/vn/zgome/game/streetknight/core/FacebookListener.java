package vn.zgome.game.streetknight.core;

public interface FacebookListener {
	public final static String LINK = "http://appstore.vn/android/details?id=vn.zgome.streetknight.android";
	public final static String[] NAME_STAGE = { "Hà Nội", "Hạ Long", "Sài Gòn",
			"Nhật Bản", "Roma", "Ai Cập", "Hải đảo" };
	public final static String[] PICTURE_URL = {
			"http://farm6.staticflickr.com/5342/9311963059_c9b4141fcd_o.png",
			"http://farm6.staticflickr.com/5473/9314750786_bb748791ed_o.png",
			"http://farm8.staticflickr.com/7385/9314751364_3dbec62929_o.png",
			"http://farm8.staticflickr.com/7345/9314750364_e4bfe268f3_o.png",
			"http://farm4.staticflickr.com/3681/9314750428_2ea01654de_o.png",
			"http://farm4.staticflickr.com/3762/9311963639_0bd98734fe_o.png",
			"http://farm4.staticflickr.com/3829/9311963397_cecc775c02_o.png" };

	public void onFeedRequest(final String link, final String name,
			final String captain, final String descript, final String pictureUrl);
}
