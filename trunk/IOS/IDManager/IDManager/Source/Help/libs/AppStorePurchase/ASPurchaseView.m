//
//  ASPurchaseView.h.m
//  AppStorePurchase
//
//  Created by AnhVT on 10/31/12.
//  Copyright (c) 2012 AnhVT. All rights reserved.
//
#import "ASPGlobal.h"
#import "ASPurchaseView.h"
#import "ASPNetworkConnect.h"
#import "TDbase64Wraper.h"
#import "JSON.h"
#import "TDAlert.h"
#import "TDCommonLibs.h"

@implementation ASPurchaseView
@synthesize language;
//@synthesize userID;
@synthesize purchaseServerURL;
//@synthesize busyView;
@synthesize busyBuyView;
@synthesize paramsDic;


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.paramsDic = nil;
        self.language = @"en";
        _isVerifyByServer = YES;
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/
-(id) initWithParent:(id)parent
   purchaseServerURL:(NSString*)serverURL
              params:(NSMutableDictionary*)pDic
            language:(NSString*)lang {
    CGRect mainRect = [[UIScreen mainScreen] bounds];
    mainRect.origin.x = 0.0;
    mainRect.origin.y = 0.0;
    if (mainRect.size.width > mainRect.size.height) {
        float t = mainRect.size.width;
        mainRect.size.width = mainRect.size.height;
        mainRect.size.height = t;
    }
    [self initWithFrame:mainRect];
    self.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.6];
    //self.alpha = 0.8f;
    
    m_parent = parent;
    self.purchaseServerURL = serverURL;
    self.language = lang;
    
    if (pDic) {
        self.paramsDic = [[NSMutableDictionary alloc] initWithDictionary:pDic];
    }
    [[SKPaymentQueue defaultQueue] addTransactionObserver:self];
    
    [[NSNotificationCenter defaultCenter] addObserver:parent
                                             selector:@selector(receivePurchaseError:)
                                                 name:ASP_NOTIFICATION_PURCHASE_ERROR
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:parent
                                             selector:@selector(receivePurchaseFinish:)
                                                 name:ASP_NOTIFICATION_PURCHASE_FINISH
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receiveNetworkError:)
                                                 name:ASP_NOTIFICATION_NETWORK_ERROR
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receiveNetworkFinish:)
                                                 name:ASP_NOTIFICATION_NETWORK_FINISH
                                               object:nil];
    /*
    self.busyView = [[ASPBusyCircleView alloc] init:NO];
    self.busyView.parent = self;
    [self addSubview:self.busyView];
    [self.busyView stop];
    */
    
    /*
    self.busyBuyView = [[[ASPBusyCircleView alloc] init:YES] autorelease];
    self.busyBuyView.parent = self;
    self.busyBuyView.center = self.center;
    [self addSubview:self.busyBuyView];
    [self.busyBuyView stop];
     */
    return self;

}

- (void)purchaseWithProductID:(NSString*)productID {
    if ([SKPaymentQueue canMakePayments] && productID) {
        NSString *purchasingString = @"purchasing...";
        if ([self.language isEqualToString:@"jp"]) {
            purchasingString = @"課金処理中...";
        } else if ([self.language isEqualToString:@"vn"]) {
            purchasingString = @"đang mua...";
        }
        //[self.busyBuyView startWithTitle:purchasingString];
        //[self webViewTouchEnabled:NO];
        self.loadingAlert = [TDAlert showLoadingMessageWithTitle:purchasingString delegate:self];
        
        NSSet *productIds = [NSSet setWithObject:productID];
        SKProductsRequest *skProductRequest = [[SKProductsRequest alloc] initWithProductIdentifiers:productIds];
        skProductRequest.delegate = self;
        [skProductRequest start];
    } else {
        NSString *errString = @"Device is not purchased or productID is error";
        if ([self.language isEqualToString:@"jp"]) {
            errString = @"装備はアイテムを購入ためにサポートしていません、又はプロダクトＩＤが不良です。";
        } else if ([self.language isEqualToString:@"vn"]) {
            errString = @"Thiết bị không hỗ trợ mua item hoặc productID bị lỗi";
        }
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:errString
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        alert.tag = TAG_PURCHASE_ERROR;
        [alert show];
        [alert release];
    }
}

#pragma mark -
#pragma mark Process SKPaymentTransactionObserver delegate

- (void)paymentQueue:(SKPaymentQueue *)queue removedTransactions:(NSArray *)transactions {
}
- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions {
    //[self.busyBuyView stop];
    [self.loadingAlert dismiss];
	for (SKPaymentTransaction *transaction in transactions) {
        ASLOC(@"Transactions number pending:%d",[transactions count]);
        //[self webViewTouchEnabled:NO];
        //[self.busyBuyView stop];
        NSString *purchasingString = @"purchasing...";
        if ([self.language isEqualToString:@"jp"]) {
            purchasingString = @"課金処理中...";
        } else if ([self.language isEqualToString:@"vn"]) {
            purchasingString = @"đang mua...";
        }
        
        if ([self superview] == nil) {
            [(UIView*)[m_parent view] addSubview:self];
        }
        
        //[self.busyBuyView startWithTitle:purchasingString];
        self.loadingAlert = [TDAlert showLoadingMessageWithTitle:purchasingString delegate:self];
        
		switch (transaction.transactionState) {
			case SKPaymentTransactionStatePurchasing:
				ASLOC(@"SKPaymentTransactionStatePurchasing");
                //[self.busyBuyView stop];
				break;
			case SKPaymentTransactionStatePurchased:
				ASLOC(@"SKPaymentTransactionStatePurchased");
				[self completedPurchaseTransaction:transaction];
				break;
			case SKPaymentTransactionStateFailed:
				ASLOC(@"SKPaymentTransactionStateFailed");
                //[self.busyBuyView stop];
                //[self webViewTouchEnabled:YES];
                [self.loadingAlert dismiss];
                [self removeFromSuperview];

                ASLOC(@"code:%d",[transaction.error code]);
                ASLOC(@"domain:%@",[transaction.error domain]);
                ASLOC(@"userInfo:%@",[[transaction.error userInfo] description]);
                ASLOC(@"localizedDescription:%@",[transaction.error localizedDescription]);
                ASLOC(@"localizedFailureReason:%@",[transaction.error localizedFailureReason]);
                
                if ([transaction.error code] != SKErrorPaymentCancelled) {
                    //Noi dung error: lỗi mua bán. có thể do netwwork hoặc setting account. Hãy  thử mua lại 1 lần nữa
                    
                    NSString *errString = @"Purchase error";
                    if ([self.language isEqualToString:@"jp"]) {
                        errString = [NSString stringWithFormat:@"メッセージコード%d\r\n購入が完了できませんでした。\r\nお手数ですがネットワーク設定又はアカウント設定を確認し、 しばらくたって から再度購入をしてください。",[transaction.error code]];
                    } else if ([self.language isEqualToString:@"vn"]) {
                        errString = @"Lỗi mua item. Hãy thử làm lại";
                    }

                    
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                                    message:errString
                                                                   delegate:self
                                                          cancelButtonTitle:@"OK"
                                                          otherButtonTitles:nil];
                    alert.tag = TAG_PURCHASE_ERROR;
                    [alert show];
                    [alert release];
                }
                 
				[[SKPaymentQueue defaultQueue] finishTransaction:transaction];
				break;
			case SKPaymentTransactionStateRestored:
				ASLOC(@"SKPaymentTransactionStateRestored");
                //[self.busyView stop];
                //[self webViewTouchEnabled:YES];
                [self removeFromSuperview];
				[[SKPaymentQueue defaultQueue] finishTransaction:transaction];
				break;
		}
	}
}
- (void)completedPurchaseTransaction:(SKPaymentTransaction *)transaction {
    ASLOC(@"completedPurchaseTransaction:Purchased");
    //AnhVT
    //Het suc can than khi sua ham nay
    //Khi ham nay khong bi thoat ra giua chung ma chua goi den lenh [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
    //Ket qua mua ban van nam trong queue cua appstore, va:
    //Ham nay se duoc goi lai tu dong moi khi cai nay duoc goi [[SKPaymentQueue defaultQueue] addTransactionObserver:self];
    //Nhung lan mua ban nao truoc do chua finish se duoc goi lai het
    //Do vay khong duoc gui len server nhung thong tin luu tru cuc bo, vi thong tin do se bi mat di khi ham duoc goi lai.
    if (_isVerifyByServer) {
        NSString *json = [TDbase64Wraper base64forData:transaction.transactionReceipt];
        NSString *httpBodyString = @"";
        NSArray *keyArray = [self.paramsDic allKeys];
        for (NSString *aKey in keyArray) {
            NSString *aParam = [NSString stringWithFormat:@"%@=%@&", aKey, [self.paramsDic objectForKey:aKey]];
            httpBodyString = [httpBodyString stringByAppendingString:aParam];
        }
        
        NSString *aParam = [NSString stringWithFormat:@"receipt=%@", json];
        httpBodyString = [httpBodyString stringByAppendingString:aParam];
        
        ASPNetworkConnect *netConnect = [[ASPNetworkConnect alloc] init];
        netConnect.parent = self;
        [netConnect sendPurchaseAppStore:httpBodyString transaction:transaction];
    }else
    {
        NSString *str = [[[NSString alloc] initWithData:transaction.transactionReceipt encoding:NSUTF8StringEncoding] autorelease];
        //[self.busyBuyView stop];
        //[self webViewTouchEnabled:YES];
        [self.loadingAlert dismiss];
        //NSDictionary *dic = [str JSONValue];
        ASLOC(@"response = %@", str);
        NSMutableDictionary *sendDic = [NSMutableDictionary dictionaryWithCapacity:1];
        [sendDic setObject:transaction.payment.productIdentifier forKey:@"product_id"];
        [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_PURCHASE_FINISH object:sendDic];
        [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
        [TDAlert showMessageWithTitle:TDLocStrOne(@"Purchase completed") message:nil delegate:nil];
        [self removeFromSuperview];
        
    }
	
    
}

#pragma mark -
#pragma mark Process SKProductsRequestDelegate delegate

//===================================================================================================
- (void)requestDidFinish:(SKRequest *)request {
	ASLOC(@"requestDidFinish");
	[request release];
}
//===================================================================================================
- (void)request:(SKRequest *)request didFailWithError:(NSError *)error {
	ASLOC(@"request:didFailWithError");
    //[self.busyBuyView stop];
    [self.loadingAlert dismiss];
    //[self webViewTouchEnabled:YES];
    [self removeFromSuperview];
}

//===================================================================================================
- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response {
	if (response == nil) {
		ASLOC(@"Product Response is nil");
		return;
	}
	BOOL isNotProductIdentifiers = YES;
    
	for (NSString *identifier in response.invalidProductIdentifiers) {
		ASLOC(@"invalid product identifier: %@", identifier);
        isNotProductIdentifiers = NO;
        //[self.busyBuyView stop];
        [self.loadingAlert dismiss];
        [self removeFromSuperview];
        
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:nil
                                                          message:@"Product ID is invalid"
                                                         delegate:self
                                                cancelButtonTitle:nil
                                                otherButtonTitles:@"Close", nil];
        alertView.tag = TAG_PURCHASE_ERROR;
		[alertView show];
        [alertView release];
        
	}
	for (SKProduct *product in response.products ) {
		ASLOC(@"valid product identifier:%@ title:%@", product.productIdentifier,product.localizedTitle);
        isNotProductIdentifiers = NO;
		SKPayment *payment = [SKPayment paymentWithProduct:product];
		[[SKPaymentQueue defaultQueue] addPayment:payment];
	}
    //Kiem tra neu truong hop mang loi, trong list invalidProductIdentifiers va products deu khong co gia tri
    //thi stop qui trinh mua item va thong bao loi mang
    if (isNotProductIdentifiers) {
        //[self.busyBuyView stop];
        [self.loadingAlert dismiss];
        [self removeFromSuperview];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:@"Network error"
                                                       delegate:self
                                              cancelButtonTitle:@"Close"
                                              otherButtonTitles:nil];
        alert.tag = TAG_NETWORK_ERROR;
        [alert show];
        [alert release];
        
    }
}

-(void) receiveNetworkError:(NSNotification*)notification {
    ASLOC(@"receiveNetworkError");
    ASPNetworkConnect *netConnect = (ASPNetworkConnect*)[notification object];
    //[self.busyBuyView stop];
    [self.loadingAlert dismiss];
    //[self webViewTouchEnabled:YES];
    [netConnect release];
    [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_PURCHASE_ERROR object:nil];
    [self removeFromSuperview];
}

-(void) receiveNetworkFinish:(NSNotification*)notification {
    ASLOC(@"receiveNetworkFinish");
    ASPNetworkConnect *netConnect = (ASPNetworkConnect*)[notification object];
    //[self.busyBuyView stop];
    [self.loadingAlert dismiss];
    //[self webViewTouchEnabled:YES];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_PURCHASE_FINISH object:netConnect.responseDic];
    [self removeFromSuperview];
    [netConnect release];
}


- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:m_parent
                                                    name:ASP_NOTIFICATION_PURCHASE_ERROR
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:m_parent
                                                    name:ASP_NOTIFICATION_PURCHASE_FINISH
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:ASP_NOTIFICATION_NETWORK_ERROR
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:ASP_NOTIFICATION_NETWORK_FINISH
                                                  object:nil];
    //[self.busyView release];
    [busyBuyView release];
    //[userID release];
    [_loadingAlert release];
    [purchaseServerURL release];
    [super dealloc];
}

@end
