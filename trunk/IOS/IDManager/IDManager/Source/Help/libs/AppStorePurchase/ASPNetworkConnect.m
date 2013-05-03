//
//  ASPNetworkConnect.m
//
//  Created by AnhVT on 10/02/03.
//  Copyright 2010 AI&T. All rights reserved.
//
#import "ASPGlobal.h"
#import "ASPNetworkConnect.h"
#import "JSON.h"
#import "ASPBusyCircleView.h"
#import "ASPurchaseView.h"


@interface ASPNetworkConnect()
@property (nonatomic, retain) NSMutableData *responseData;
@property (nonatomic, retain) NSString *msgFromServer;
@property (nonatomic, retain) NSURLConnection *con;
@property (nonatomic, retain) NSTimer *timeoutTimer;

@end

@implementation ASPNetworkConnect
- (id) init {
    //ASLOC(@"ASPNetworkConnect:init");
    if (self = [super init]) {
        _responseData = nil;
        
        _transaction = nil;
        _requestHtmlBody = nil;
        self.msgFromServer = @"";
    }    
    return self;
}

- (void)dealloc {
    ASLOC(@"ASPNetworkConnect:dealloc");
    [_responseData release];
    [_msgFromServer release];
    [_con release];
    [_timeoutTimer release];
    [_transaction release];
    [_requestHtmlBody release];
    [_responseDic release];
    
	[super dealloc];
}
- (void)startConnection {
    ASLOC(@"startConnection:%@",self.requestHtmlBody);
    self.responseData = [[[NSMutableData alloc] init] autorelease];
    self.responseDic = nil;

    NSData *bodyData = [self.requestHtmlBody dataUsingEncoding:NSUTF8StringEncoding];
    NSString *urlString = _parent.purchaseServerURL;
    ASLOC(@"startConnection:%@",urlString);
    NSURL *requestURL = [NSURL URLWithString:urlString];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:requestURL];
    [request setHTTPMethod: @"POST"];
    [request setTimeoutInterval:TIME_TIMEOUT];
    [request setCachePolicy:NSURLRequestReloadIgnoringCacheData];
    [request setHTTPBody:bodyData];
    if (self.con) {
        [self.con cancel];
        self.con = nil;
    }
    self.con = [NSURLConnection connectionWithRequest:request delegate:self];
    
    self.timeoutTimer = [NSTimer scheduledTimerWithTimeInterval:TIME_TIMEOUT
                                                    target:self 
                                                  selector:@selector(stopConnection) 
                                                  userInfo:nil 
                                                   repeats:NO];
}
- (void)stopConnection {
    ASLOC(@"stopConnection");
    [_con cancel];
    [self showErrorMsgAlertView:ERROR_CODE_TIME_OUT];
}
-(void)cancelTimer
{
    if ([self.timeoutTimer isValid]) {
        [_timeoutTimer invalidate];
    }
    self.timeoutTimer = nil;
}
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    ASLOC(@"Error ASPNetworkConnect code:%d",[error code]);
    [self cancelTimer];
    [self showErrorMsgAlertView:[error code]];
    
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    //ASLOC(@"connection:didReceiveResponse:%@",[[((NSHTTPURLResponse *)response) allHeaderFields] description]);
    int statusCode = [((NSHTTPURLResponse *)response) statusCode];
    ASLOC(@"Connection status code: %d", statusCode);
    if (statusCode != 200) {
        [self cancelTimer];
        [_con cancel];
        [self showErrorMsgAlertView:statusCode];
    }
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)partialData {
	[_responseData appendData:partialData];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    ASLOC(@"connectionDidFinishLoading");
    [self cancelTimer];
    NSInteger buyResult = [self completedPurchase];
    if (buyResult) {
        [self showErrorMsgAlertView:buyResult];
    } else {
        if (self.msgFromServer && [self.msgFromServer length]) {
            [self.parent.busyBuyView stop];
            UIAlertView *alert =[[UIAlertView alloc]initWithTitle:nil
                                                          message:self.msgFromServer
                                                         delegate:self
                                                cancelButtonTitle:@"OK"
                                                otherButtonTitles:nil];
            alert.tag = TAG_COMPLETED_PURCHASE;
            [alert show];
            [alert release];
        } else {
            [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_NETWORK_FINISH object:self];
        }
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	ASLOC(@"alertView:clickedButtonAtIndex:%d",buttonIndex);
    if (alertView.tag == TAG_CONFIRM_RESEND_PURCHASE) {
        if (buttonIndex == 0) {
            [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_NETWORK_ERROR object:self];
        } else if (buttonIndex == 1) {
            [self startConnection];
        }
    } else if (alertView.tag == TAG_COMPLETED_PURCHASE) {
        [[NSNotificationCenter defaultCenter] postNotificationName:ASP_NOTIFICATION_NETWORK_FINISH object:self];
    }
}

- (void)showErrorMsgAlertView:(NSInteger)errorCode {
    //Noi dung error: Lỗi mua bán (kèm mã lỗi). do server xử lý lỗi. Hãy bấm reset hoặc khởi động lại app để gửi lại lên server.
    NSString *errString = [NSString stringWithFormat:@"Error code: %d\r\n Select Retry or restart app for resend to server",errorCode];
    NSString *cancelString = @"Cancel";
    NSString *retryString = @"Retry";
    if ([self.parent.language isEqualToString:@"jp"]) {
        errString = [NSString stringWithFormat:@"メッセージコード%d\r\n購入が完了できませんでした。\r\nお手数ですがリトライボタンを押し、再度起動してください。",errorCode];
        cancelString = @"キャンセル";
        retryString = @"リトライ";
    } else if ([self.parent.language isEqualToString:@"vn"]) {
        errString = [NSString stringWithFormat:@"Lỗi mua item %d. Hãy bấm reset hoặc khởi động lại app để gửi lại lên server.",errorCode];
        cancelString = @"Thoát";
        retryString = @"Thử lại";
    }
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                    message:errString
                                                   delegate:self
                                          cancelButtonTitle:cancelString
                                          otherButtonTitles:retryString, nil];
    alert.tag = TAG_CONFIRM_RESEND_PURCHASE;
    [alert show];
    [alert release];
}

- (void)sendPurchaseAppStore:(NSString*)htmlBody transaction:(SKPaymentTransaction*)aTransaction {
    self.requestHtmlBody = htmlBody;
    self.transaction = aTransaction;
    [self startConnection];
}

- (NSInteger)completedPurchase {
    ASLOC(@"completedPurchase");
    
    if (self.responseData == nil) {
        ASLOC(@"response data from server is nil");
        return ERROR_CODE_RESPONSE_EMPTY;
    }
    ASLOC(@"resultData:%s",[self.responseData bytes]);
    
    //Du lieu test truong hop json bi loi
    //NSString *jsonError = @"sadfadfsdf{dafsdfasdf>>>";
    //resultData = [jsonError dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *error = nil;
    NSString *strResult = [[[NSString alloc] initWithData:self.responseData encoding:NSUTF8StringEncoding] autorelease];
    NSDictionary *resDic = [strResult JSONValue];
    
    if (error) { //not use
        ASLOC(@"Error: decode Json data");
        return ERROR_CODE_DECODE_JSON;
    }
    if (resDic == nil) {
        ASLOC(@"response data from server is nil or not is json");
        return ERROR_CODE_JSON_EMPTY;
    }
    
    self.responseDic = [[[NSMutableDictionary alloc] initWithDictionary:resDic] autorelease];
    
    //ASLOC(@"resDic:%@",[resDic description]);
    if ([resDic objectForKey:@"status"] == nil) {
        ASLOC(@"response data from server is error: not status key");
        return ERROR_CODE_NOT_STATUS_KEY;
    }
    NSInteger status = [[resDic objectForKey:@"status"] intValue];
    NSString *msgString = [resDic objectForKey:@"msg"];
    ASLOC(@"Result buy item status:%d msg:%@",status,msgString);
    ASLOC(@"transaction = %@", self.transaction);
    if (status == 1) {
        self.msgFromServer = msgString;
        [[SKPaymentQueue defaultQueue] finishTransaction:self.transaction];//comment for test
        return 0;
    } else if (status == 0) {
        ASLOC(@"Error: response purchase is not ok");
        self.msgFromServer = msgString;
        [[SKPaymentQueue defaultQueue] finishTransaction:self.transaction];//comment for test
        return 0;
    } else {
        ASLOC(@"response data from server is error");
        return ERROR_CODE_WRONG_STATUS_KEY;
    }
    self.msgFromServer = @"";
    return 0;
}



@end
