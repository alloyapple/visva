package vn.com.shoppie.webconfig;

/**
 * WebServiceConfig class contains web service settings
 * 
 * @author Kane
 */
public final class WebServiceConfig {
	// Network time out: 60s
	public static int NETWORK_TIME_OUT = 60000;

	// result code for activity result
	public static int RESULT_OK = 1;

	public static String STRING_INVALID_TOKEN = "AccessToken invalid";

	public static String STRING_EXPIRE_TOKEN = "AccessToken expired";

	// ===================== DOMAIN ==========================//

	public static String PROTOCOL_HTTP = "http://";

	public static String PROTOCOL_HTTPS = "https:";

	public static String APP_DOMAIN = "http://www8190ui.sakura.ne.jp:8080/";
	// ===================== WEB SERVICE LINK ==================//

	/** SHOPPIE */
	public static final String URL_SHOPPIE_HOME = "http://ws.shoppie.com.vn/index.php/api/webservice/";
	/** HOME PAGE & LOOKBOOK campaignId=15 */
	public static final String URL_MERCHCAMPAIGNS = URL_SHOPPIE_HOME
			+ "merchcategories_v2/format/json";
	/** Merchant Campain */
	public static final String URL_MERCHANT_CAMPAIGN = URL_SHOPPIE_HOME
			+ "merchcampaigns_v2/format/json";
	/** Update campaign View */
	public static final String URL_UPDATE_CAMPAIGN_VIEW = URL_SHOPPIE_HOME
			+ "updatecampaignviewed_v2/format/json";
	public static final String URL_REGISTER =URL_SHOPPIE_HOME +"/autoregister/format/json";
	public static final String URL_UPDATE_INFO = "/customerupdate";
	public static final String URL_FEEDBACK = "/customerfeedback";

	// GCM a. Thang
	public static final String SENDER_ID1 = "AIzaSyD4keVrSodVstQl_91Md5c7INI4Xhmcl_4";
	public static final String SENDER_ID = "622843213634";
	// GCM Shoppie vn
	// public static final String SENDER_ID1 =
	// "AIzaSyAbIken3kZUOqk_bV7CBXCtgJWNFytrt_w";
	// public static final String SENDER_ID = "280540613829";

	// FaceBook ID hackbook: 157111564357680
	// Shoppie: 491369950945744
	public static final String FB_APP_ID = "491369950945744";
	public static final String[] FB_APP_PERMISSION = { "publish_stream" };
	public static final String FB_TOKEN = "access_token";
	public static final String FB_EXPIRES = "expires_in";
	public static final String FB_KEY = "facebook-credentials";

	// Image header
	public static final String HEAD_IMAGE = "http://web.shoppie.com.vn:8080/";

	// ===================== PARAMETER =======================//

}
