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

	// TODO

	public static String PROTOCOL_HTTP = "http://";

	public static String PROTOCOL_HTTPS = "https:";

	public static String APP_DOMAIN = "http://www8190ui.sakura.ne.jp:8080/";
	// ===================== WEB SERVICE LINK ==================//

	/** SHOPPIE */
	public static final String URL_SHOPPIE_HOME = "http://ws.shoppie.com.vn/index.php/api/webservice/";
	/** HOME PAGE & LOOKBOOK campaignId=15 */
	public static final String URL_MERCHCAMPAIGNS = URL_SHOPPIE_HOME +"merchcategories_v2/format/json";
	/** NOTIFICATION */
	public static final String URL_NOTIFICATION = "/notifications";
	/** TIM NOI TICH DIEM */
	public static final String URL_ALL_STORE = "/allstores";
	/** SO PIE CHO MOI THUONG HIEU Type=1: checkin; Type=2: mua hang */
	public static final String URL_PROMOTIONS = "/promotions";
	/** LIST MERCHANTS & SO LIKE THUONG HIEU */
	public static final String URL_MERCHANTS = "/merchants";
	/** GIOI THIEU THUONG HIEU ? merchId=1*/
	public static final String URL_MERCH_IMAGES = "/merchimages";
	/** LIKE THUONG HIEU custId=118 merchId=1 */
	public static final String URL_LIKE_BRAND = "/likebrand";
	/** UNLIKE THUONG HIEU custId=118 merchId=1 */
	public static final String URL_UNLIKE_BRAND = "/unlikebrand";
	/** SO DU TAI KHOAN custId=18 */
	public static final String URL_CUST_BALANCE = "/customerbalance";
	/** GET FULL INFO OF USER custId=118 */
	public static final String URL_CUST_INFO = "/customerinfo";
	/** GET LIST FAVORITE BRAND custId=118 */
	public static final String URL_GET_LIKE_BRAND = "/getlikebrand";
	/** GET LIST FAVORITE PRODUCT custId=118 */
	public static final String URL_GET_LIKE_PRODUCT = "/getlikeproduct";
	/** LIKE SAN PHAM custId=118&productId=1 */
	public static final String URL_LIKE_PRODUCT = "/likeproduct";
	/** UNLIKE SAN PHAM custId=118&productId=1 */
	public static final String URL_UNLIKE_PRODUCT = "/unlikeproduct";
	/** DANH SACH QUA TANG */
	public static final String URL_GIFTS = "/allgifts";
	/** DANH SACH QUA DA DOI ?custId=118 */
	public static final String URL_GET_GIFT_REDEEM = "/getgiftredeem";
	/** DOI QUA ?merchId=2&storeId=9&custId=124&giftId=41&redeemQty=1*/
	public static final String URL_GIFT_REDEEM = "/giftredeem";
	/** HUY DOI QUA ?custId=270&txnId=850 */
	public static final String URL_CANCEL_GIFT_REDEEM="/cancelgiftredeem";
	/** DANH SACH GIAO DICH ?custId=118 */
	public static final String URL_GET_CUST_HISTORY_TXNS = "/getcusthistorytxns";
	

	public static final String URL_BRAND_PRODUCTS = "/merchproducts1";
	public static final String URL_BRAND_GIFT = "/merchgifts";
	public static final String URL_MERCHANTS_CATEGORY = "/merchcategories";
	public static final String URL_REGISTER = "/autoregister";
	/** /customerupdate?custId=118&custName=ThangNM&custEmail=nmtdhbk@yahoo.com&custPhone=123&gender=Male */
	public static final String URL_UPDATE_INFO = "/customerupdate";
	public static final String URL_LOOKBOOK = "/campaignimages";
	/** /customerfeedback?custId=118&message=Canifa,BlueExchange */
	public static final String URL_FEEDBACK = "/customerfeedback";
	public static final String URL_GIFT_SLIDER = "/sliders";
	
	// GCM a. Thang
	public static final String SENDER_ID1 = "AIzaSyD4keVrSodVstQl_91Md5c7INI4Xhmcl_4";
	public static final String SENDER_ID = "622843213634";
	// GCM Shoppie vn
//	public static final String SENDER_ID1 = "AIzaSyAbIken3kZUOqk_bV7CBXCtgJWNFytrt_w";
//	public static final String SENDER_ID = "280540613829";
	
	// FaceBook ID hackbook:   157111564357680
	//Shoppie: 491369950945744
	public static final String FB_APP_ID="491369950945744";
	public static final String[] FB_APP_PERMISSION={"publish_stream"};
	public static final String FB_TOKEN = "access_token";
	public static final String FB_EXPIRES = "expires_in";
	public static final String FB_KEY = "facebook-credentials";

	// Image header
	public static final String HEAD_IMAGE = "http://web.shoppie.com.vn:8080/";
	
	// Local storage
	public static final String STORE_CACHE="shoppie/cache/image";
	// ===================== PARAMETER =======================//

}
