package vn.com.shoppie.util.parse;

import org.xml.sax.Attributes;

import vn.com.shoppie.database.sobject.Campaign;
import vn.com.shoppie.database.sobject.CampaignImage;
import vn.com.shoppie.database.sobject.CustBalance;
import vn.com.shoppie.database.sobject.Gift;
import vn.com.shoppie.database.sobject.GiftRedeem;
import vn.com.shoppie.database.sobject.HistoryTxn;
import vn.com.shoppie.database.sobject.MerchImage;
import vn.com.shoppie.database.sobject.Merchant;
import vn.com.shoppie.database.sobject.MerchantCategory;
import vn.com.shoppie.database.sobject.Notification;
import vn.com.shoppie.database.sobject.Product;
import vn.com.shoppie.database.sobject.Promotion;
import vn.com.shoppie.database.sobject.ShoppieObject;
import vn.com.shoppie.database.sobject.Slider;
import vn.com.shoppie.database.sobject.Store;
import vn.com.shoppie.database.sobject.User;

public class WiModelProtocol implements OnParseListener {
	private ShoppieObject _currentObject = null;

	WiModelManager mng;

	public WiModelProtocol(WiModelManager mng) {
		this.mng = mng;
		init();
	}

	private void init() {
	}

	public void parseAttributes(String elementName, Attributes atts) {

	}

	public void endElement(String element) {
		if (element.equals(HistoryTxn.CLASS_UNIQUE) || element.equals(MerchImage.CLASS_UNIQUE) || element.equals(Promotion.CLASS_UNIQUE) || element.equals(Notification.CLASS_UNIQUE) || element.equals(CustBalance.CLASS_UNIQUE) || element.equals(Campaign.CLASS_UNIQUE)
				|| element.equals(CampaignImage.CLASS_UNIQUE) || element.equals(User.CLASS_UNIQUE) || element.equals(Store.CLASS_UNIQUE) || element.equals(Product.CLASS_UNIQUE) || element.equals(Gift.CLASS_UNIQUE) || element.equals(Merchant.CLASS_UNIQUE) || element.equals(Slider.CLASS_UNIQUE)
				|| element.equals(MerchantCategory.CLASS_UNIQUE) || element.equals(GiftRedeem.CLASS_UNIQUE)) {
			mng.addShoppieObject(_currentObject);
			// Log.e("object: "+_crtObject, "element: "+element);
		}
		_crtElement = "";
	}

	String _crtObject;
	String _crtElement;

	@Override
	public void charactor(String content) {
		if (content == null || _crtObject == null || _crtElement == null) {
			System.out.println("NULL: " + _crtObject + "-" + _crtElement);
			return;
		}

		try {
			// HISTORYTNX
			if (_crtObject.equals(HistoryTxn.CLASS_UNIQUE)) {
				if (_crtElement.equals("txnId")) {
					((HistoryTxn) _currentObject).txnId = content;
				} else if (_crtElement.equals("merchId")) {
					((HistoryTxn) _currentObject).merchId = content;
				} else if (_crtElement.equals("txnType")) {
					((HistoryTxn) _currentObject).txnType = content;
				} else if (_crtElement.equals("txnAmt")) {
					((HistoryTxn) _currentObject).txnAmt = content;
				} else if (_crtElement.equals("pieQty")) {
					((HistoryTxn) _currentObject).pieQty = content;
				} else if (_crtElement.equals("billCode")) {
					((HistoryTxn) _currentObject).billCode = content;
				} else if (_crtElement.equals("txnDate")) {
					((HistoryTxn) _currentObject).txnDate = content;
				} else if (_crtElement.equals("merchName")) {
					((HistoryTxn) _currentObject).merchName = content;
				} else if (_crtElement.equals("storeName")) {
					((HistoryTxn) _currentObject).storeName = content;
				}
			}

			// MERCHIMAGE
			if (_crtObject.equals(MerchImage.CLASS_UNIQUE)) {
				if (_crtElement.equals("merchId")) {
					((MerchImage) _currentObject).merchId = content;
				} else if (_crtElement.equals("imageId")) {
					((MerchImage) _currentObject).imageId = content;
				} else if (_crtElement.equals("imageDesc")) {
					((MerchImage) _currentObject).imageDesc = content;
				} else if (_crtElement.equals("image")) {
					((MerchImage) _currentObject).image = content;
				}
			}

			// PROMOTION
			if (_crtObject.equals(Promotion.CLASS_UNIQUE)) {
				if (_crtElement.equals("merchId")) {
					((Promotion) _currentObject).merchId = content;
				} else if (_crtElement.equals("txnType")) {
					((Promotion) _currentObject).txnType = content;
				} else if (_crtElement.equals("pieQty")) {
					((Promotion) _currentObject).pieQty = content;
				} else if (_crtElement.equals("campaignName")) {
					((Promotion) _currentObject).campaignName = content;
				}
			}

			// NOTIFICATION
			if (_crtObject.equals(Notification.CLASS_UNIQUE)) {
				if (_crtElement.equals("notificationId")) {
					((Notification) _currentObject).notificationId = content;
				} else if (_crtElement.equals("merchId")) {
					((Notification) _currentObject).merchId = content;
				} else if (_crtElement.equals("storeId")) {
					((Notification) _currentObject).storeId = content;
				} else if (_crtElement.equals("content")) {
					((Notification) _currentObject).content = content;
				} else if (_crtElement.equals("status")) {
					((Notification) _currentObject).status = content;
				} else if (_crtElement.equals("regdate")) {
					((Notification) _currentObject).regdate = content;
				}
			}

			// CAMPAIGN
			if (_crtObject.equals(Campaign.CLASS_UNIQUE)) {
				if (_crtElement.equals("campaignId")) {
					((Campaign) _currentObject).id = content;
				} else if (_crtElement.equals("merchId")) {
					((Campaign) _currentObject).merchId = content;
				} else if (_crtElement.equals("campaignName")) {
					((Campaign) _currentObject).name = content;
				} else if (_crtElement.equals("campaignDesc")) {
					((Campaign) _currentObject).desc = content;
				} else if (_crtElement.equals("campaignImage")) {
					((Campaign) _currentObject).linkImage = content;
				} else if (_crtElement.equals("campaignType")) {
					((Campaign) _currentObject).campaignType = content;
				} else if (_crtElement.equals("newStatus")) {
					((Campaign) _currentObject).newStatus = content;
				}
			}

			// CUSTOMBALANCE
			if (_crtObject.equals(CustBalance.CLASS_UNIQUE)) {
				if (_crtElement.equals("creditBal")) {
					((CustBalance) _currentObject).creditBal = content;
				} else if (_crtElement.equals("debitBal")) {
					((CustBalance) _currentObject).debitBal = content;
				} else if (_crtElement.equals("currentBal")) {
					((CustBalance) _currentObject).currentBal = content;
				}
			}

			// CAMPAIGNIMAGE
			if (_crtObject.equals(CampaignImage.CLASS_UNIQUE)) {
				if (_crtElement.equals("campaignId")) {
					((CampaignImage) _currentObject).campaignId = content;
				} else if (_crtElement.equals("imageName")) {
					((CampaignImage) _currentObject).imageName = content;
				} else if (_crtElement.equals("imagePath")) {
					((CampaignImage) _currentObject).imagePath = content;
				} else if (_crtElement.equals("imageDesc")) {
					((CampaignImage) _currentObject).imageDesc = content;
				} else if (_crtElement.equals("price")) {
					((CampaignImage) _currentObject).price = content;
				}
			}

			// USER REGISTER
			else if (_crtObject.equals(User.CLASS_UNIQUE)) {
				if (_crtElement.equals("dataValue")) {
					((User) _currentObject).custId = content;
				}
			}

			// STORE
			else if (_crtObject.equals(Store.CLASS_UNIQUE)) {
				if (_crtElement.equals("merchId")) {
					((Store) _currentObject).merchId = content;
				} else if (_crtElement.equals("storeId")) {
					((Store) _currentObject).storeId = content;
				} else if (_crtElement.equals("storeCode")) {
					((Store) _currentObject).storeCode = content;
				} else if (_crtElement.equals("storeName")) {
					((Store) _currentObject).storeName = content;
				} else if (_crtElement.equals("storeAddress")) {
					((Store) _currentObject).storeAddress = content;
				} else if (_crtElement.equals("latitude")) {
					((Store) _currentObject).latitude = content;
				} else if (_crtElement.equals("longtitude")) {
					((Store) _currentObject).longtitude = content;
				} else if (_crtElement.equals("thumbnail")) {
					((Store) _currentObject).thumbnail = content;
				} else if (_crtElement.equals("merchCatId")) {
					((Store) _currentObject).merchCatId = content;
				}
			}

			// PRODUCT
			else if (_crtObject.equals(Product.CLASS_UNIQUE)) {
				if (_crtElement.equals("productId")) {
					((Product) _currentObject).productId = content;
				} else if (_crtElement.equals("merchId")) {
					((Product) _currentObject).merchId = content;
				} else if (_crtElement.equals("productName")) {
					((Product) _currentObject).productName = content;
				} else if (_crtElement.equals("longDesc")) {
					((Product) _currentObject).longDesc = content;
				} else if (_crtElement.equals("likedNumber")) {
					((Product) _currentObject).likedNumber = content;
				} else if (_crtElement.equals("price")) {
					((Product) _currentObject).price = content;
				} else if (_crtElement.equals("oldPrice")) {
					((Product) _currentObject).oldPrice = content;
				} else if (_crtElement.equals("productImage")) {
					((Product) _currentObject).productImage = content;
				} else if (_crtElement.equals("pieQty")) {
					((Product) _currentObject).pieQty = content;
				}
			}

			// GIFT
			else if (_crtObject.equals(Gift.CLASS_UNIQUE)) {
				if (_crtElement.equals("giftId")) {
					((Gift) _currentObject).giftId = content;
				} else if (_crtElement.equals("merchId")) {
					((Gift) _currentObject).merchId = content;
				} else if (_crtElement.equals("giftName")) {
					((Gift) _currentObject).giftName = content;
				} else if (_crtElement.equals("description")) {
					((Gift) _currentObject).description = content;
				} else if (_crtElement.equals("pieQty")) {
					((Gift) _currentObject).pieQty = content;
				} else if (_crtElement.equals("giftImage")) {
					((Gift) _currentObject).giftImage = content;
				} else if (_crtElement.equals("redeemQty")) {
					((Gift) _currentObject).redeemQty = content;
				}
			}

			// GIFT REDEEM
			else if (_crtObject.equals(GiftRedeem.CLASS_UNIQUE)) {
				if (_crtElement.equals("txnId")) {
					((GiftRedeem) _currentObject).txnId = content;
				} else if (_crtElement.equals("merchId")) {
					((GiftRedeem) _currentObject).merchId = content;
				} else if (_crtElement.equals("redeemQty")) {
					((GiftRedeem) _currentObject).redeemQty = content;
				} else if (_crtElement.equals("pieQty")) {
					((GiftRedeem) _currentObject).pieQty = content;
				} else if (_crtElement.equals("giftId")) {
					((GiftRedeem) _currentObject).giftId = content;
				} else if (_crtElement.equals("giftName")) {
					((GiftRedeem) _currentObject).giftName = content;
				} else if (_crtElement.equals("giftImage")) {
					((GiftRedeem) _currentObject).giftImage = content;
				} else if (_crtElement.equals("storeName")) {
					((GiftRedeem) _currentObject).storeName = content;
				} else if (_crtElement.equals("storeId")) {
					((GiftRedeem) _currentObject).storeId = content;
				} else if (_crtElement.equals("status")) {
					((GiftRedeem) _currentObject).status = content;
				}
			}

			// SLIDER
			else if (_crtObject.equals(Slider.CLASS_UNIQUE)) {
				if (_crtElement.equals("slideId")) {
					((Slider) _currentObject).slideId = content;
				} else if (_crtElement.equals("slideDesc")) {
					((Slider) _currentObject).slideDesc = content;
				} else if (_crtElement.equals("slideLink")) {
					((Slider) _currentObject).slideLink = content;
				} else if (_crtElement.equals("slideImage")) {
					((Slider) _currentObject).slideImage = content;
				}
			}

			// MERCHANTS
			else if (_crtObject.equals(Merchant.CLASS_UNIQUE)) {
				if (_crtElement.equals("merchId")) {
					((Merchant) _currentObject).merchId = content;
				} else if (_crtElement.equals("merchName")) {
					((Merchant) _currentObject).merchName = content;
				} else if (_crtElement.equals("merchCatId")) {
					((Merchant) _currentObject).merchCatId = content;
				} else if (_crtElement.equals("merchImage")) {
					((Merchant) _currentObject).merchImage = content;
				} else if (_crtElement.equals("merchBanner")) {
					((Merchant) _currentObject).merchBanner = content;
				} else if (_crtElement.equals("merchDescription")) {
					((Merchant) _currentObject).merchDescription = content;
				}
			}

			// MERCHANTCATEGORY
			else if (_crtObject.equals(MerchantCategory.CLASS_UNIQUE)) {
				if (_crtElement.equals("merchCatId")) {
					((MerchantCategory) _currentObject).merchCatId = content;
				} else if (_crtElement.equals("merchCatName")) {
					((MerchantCategory) _currentObject).merchCatName = content;
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void startElement(String element) {
		_crtElement = element;
		// HISTORYTNX
		if (element.equals(HistoryTxn.CLASS_UNIQUE)) {
			String[] values = new String[HistoryTxn.NUM_FIELDS];
			_currentObject = new HistoryTxn(-1, values);
			_crtObject = element;
			return;
		}
		// MERCHIMAGE
		if (element.equals(MerchImage.CLASS_UNIQUE)) {
			String[] values = new String[MerchImage.NUM_FIELDS];
			_currentObject = new MerchImage(-1, values);
			_crtObject = element;
			return;
		}
		// PROMOTION
		if (element.equals(Promotion.CLASS_UNIQUE)) {
			String[] values = new String[Promotion.NUM_FIELDS];
			_currentObject = new Promotion(-1, values);
			_crtObject = element;
			return;
		}

		// NOTIFICATION
		if (element.equals(Notification.CLASS_UNIQUE)) {
			String[] values = new String[Notification.NUM_FIELDS];
			_currentObject = new Notification(-1, values);
			_crtObject = element;
			return;
		}
		// CAMPAIGN
		if (element.equals(Campaign.CLASS_UNIQUE)) {
			String[] values = new String[Campaign.NUM_FIELDS];
			_currentObject = new Campaign(-1, values);
			_crtObject = element;
			return;
		}
		// CAMPAIGNIMAGE
		if (element.equals(CampaignImage.CLASS_UNIQUE)) {
			String[] values = new String[CampaignImage.NUM_FIELDS];
			_currentObject = new CampaignImage(-1, values);
			_crtObject = element;
			return;
		}
		// CUSTOMBALANCE
		if (element.equals(CustBalance.CLASS_UNIQUE)) {
			String[] values = new String[CustBalance.NUM_FIELDS];
			_currentObject = new CustBalance(-1, values);
			_crtObject = element;
			return;
		}
		// USER REGISTER
		if (element.equals(User.CLASS_UNIQUE)) {
			String[] values = new String[User.NUM_FIELDS];
			_currentObject = new User(-1, values);
			_crtObject = element;
			return;
		}

		// STORE
		if (element.equals(Store.CLASS_UNIQUE)) {
			String[] values = new String[Store.NUM_FIELDS];
			_currentObject = new Store(-1, values);
			_crtObject = element;
			return;
		}

		// PRODUCT
		if (element.equals(Product.CLASS_UNIQUE)) {
			String[] values = new String[Product.NUM_FIELDS];
			_currentObject = new Product(-1, values);
			_crtObject = element;
			return;
		}

		// GIFT
		if (element.equals(Gift.CLASS_UNIQUE)) {
			String[] values = new String[Gift.NUM_FIELDS];
			_currentObject = new Gift(-1, values);
			_crtObject = element;
			return;
		}

		// GIFTREDEEM
		if (element.equals(GiftRedeem.CLASS_UNIQUE)) {
			String[] values = new String[GiftRedeem.NUM_FIELDS];
			_currentObject = new GiftRedeem(-1, values);
			_crtObject = element;
			return;
		}

		// SLIDER
		if (element.equals(Slider.CLASS_UNIQUE)) {
			String[] values = new String[Slider.NUM_FIELDS];
			_currentObject = new Slider(-1, values);
			_crtObject = element;
			return;
		}

		// MERCHANT
		if (element.equals(Merchant.CLASS_UNIQUE)) {
			String[] values = new String[Merchant.NUM_FIELDS];
			_currentObject = new Merchant(-1, values);
			_crtObject = element;
			return;
		}

		// MERCHANTCATEGORY
		if (element.equals(MerchantCategory.CLASS_UNIQUE)) {
			String[] values = new String[MerchantCategory.NUM_FIELDS];
			_currentObject = new MerchantCategory(-1, values);
			_crtObject = element;
			return;
		}
	}

	public void startDocument() {
	}

	public void finishDocument() {
	}

}
