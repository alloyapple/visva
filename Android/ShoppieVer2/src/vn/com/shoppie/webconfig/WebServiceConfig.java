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
	/** register account to SP server */
	public static final String URL_REGISTER = URL_SHOPPIE_HOME
			+ "/v2autoregister/format/json";
	/** Merchant Products */
	public static final String URL_MERCHANT_PRODUCT = URL_SHOPPIE_HOME
			+ "merchproducts_v2/format/json";
	/** update pie */
	public static final String URL_UPDATE_PIE = URL_SHOPPIE_HOME
			+ "/updateluckypie_v2/format/";
	/** merchant stores */
	public static final String URL_MERCHANT_STORES = URL_SHOPPIE_HOME
			+ "merchstores_v2/format/json";
	/** update friends */
	public static final String URL_UPDATE_FRIENDS = URL_SHOPPIE_HOME
			+ "friends_v2/format/json";
	/** update history transaction */
	public static final String URL_HISTORY_TRANSACTION = URL_SHOPPIE_HOME
			+ "txnhistory_v2/format/json";
	public static final String URL_UPDATE_INFO = "/customerupdate";
	public static final String URL_FEEDBACK = "/customerfeedback";
	public static final String URL_GET_GIFT_LIST = "http://210.211.117.90/shoppie.webservice/ci/index.php/api/webservice/allgifts/format/json";
	public static final String URL_GET_GIFT_TRANSACTION_AVAILABLE = "http://210.211.117.90/shoppie.webservice/ci/index.php/api/webservice/giftredeem_v2/format/json";

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
