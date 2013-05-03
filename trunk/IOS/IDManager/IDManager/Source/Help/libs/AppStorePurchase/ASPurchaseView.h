//
//  ASPurchaseView.h.h
//  AppStorePurchase
//
//  Created by AnhVT on 10/31/12.
//  Copyright (c) 2012 AnhVT. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <StoreKit/StoreKit.h>
#import "ASPBusyCircleView.h"
#import <UIKit/UIKit.h>


@interface ASPurchaseView : UIControl <UIAlertViewDelegate,SKProductsRequestDelegate,SKPaymentTransactionObserver> {
    NSString *purchaseServerURL;
    id m_parent;
    ASPBusyCircleView *busyBuyView;
    NSMutableDictionary *paramsDic;
    NSString *language;
}

@property (nonatomic, retain) NSString *purchaseServerURL;
@property (nonatomic, retain) ASPBusyCircleView *busyBuyView;
@property (nonatomic, retain) UIAlertView *loadingAlert;
@property (nonatomic, retain) NSMutableDictionary *paramsDic;
@property (nonatomic, retain) NSString *language;
@property (nonatomic, assign) BOOL isVerifyByServer; //check xem co verify bang server khong. Default: YES;

-(id) initWithParent:(id)parent
   purchaseServerURL:(NSString*)serverURL
              params:(NSMutableDictionary*)pDic
            language:(NSString*)lang;
- (void)purchaseWithProductID:(NSString*)productID;
-(void) receiveNetworkError:(NSNotification*)notification;
-(void) receiveNetworkFinish:(NSNotification*)notification;

@end
