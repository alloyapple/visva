//
//  TDWebViewController.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol TDWebViewDelegate;
@interface TDWebViewController : UIViewController<UIWebViewDelegate, UITextFieldDelegate>
@property (nonatomic, retain) NSString *sUrlStart;
@property (nonatomic, retain) NSString *sUrl;
@property (retain, nonatomic) IBOutlet UIWebView *wvContent;
@property (nonatomic, assign) id<TDWebViewDelegate> webDelegate;
@property (nonatomic, assign) int iTag;
@end

@protocol TDWebViewDelegate <NSObject>
-(void)browserBack:(TDWebViewController*)controller;

@end