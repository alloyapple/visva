//
//  ASPNetworkConnect.h
//  LoveCinema
//
//  Created by AnhVT on 10/02/03.
//  Copyright 2010 AI&T. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <StoreKit/StoreKit.h> 

#define TIME_TIMEOUT 30.0

#define ERROR_CODE_RESPONSE_EMPTY   1001
#define ERROR_CODE_DECODE_JSON      1002
#define ERROR_CODE_JSON_EMPTY       1003
#define ERROR_CODE_NOT_STATUS_KEY   1004
#define ERROR_CODE_TIME_OUT         1005
#define ERROR_CODE_WRONG_STATUS_KEY 1006

@class ASPurchaseView;

@interface ASPNetworkConnect : NSObject <UIAlertViewDelegate> {
}
@property (nonatomic, assign) ASPurchaseView *parent;
@property (nonatomic, retain) SKPaymentTransaction *transaction;
@property (nonatomic, retain) NSString *requestHtmlBody;
@property (nonatomic, retain) NSMutableDictionary *responseDic;

-(void)startConnection;
-(void)sendPurchaseAppStore:(NSString*)htmlBody transaction:(SKPaymentTransaction*)aTransaction;
-(NSInteger)completedPurchase;
-(void)showErrorMsgAlertView:(NSInteger)errorCode;
-(void)stopConnection;
@end
