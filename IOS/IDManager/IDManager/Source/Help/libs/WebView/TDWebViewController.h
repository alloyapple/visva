//
//  TDWebViewController.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UIKit/UIGestureRecognizerSubclass.h>
@interface TDRotateGesture : UIGestureRecognizer{
    CGPoint _centerPoint;
    CGPoint _beginPoint;
    float _minCheckLen;
    float _radius;
    BOOL _isValidTouch;
}

@property(nonatomic, assign)CGRect rect;
@property(nonatomic, assign)float minRadiusFactor;
@property(nonatomic, assign)float maxRadiusFactor;
@property(nonatomic, assign)CGPoint centerFactor;
@property(nonatomic, readonly)BOOL isClockWise;
@property(nonatomic, readonly)int angleStep;


@end

@protocol TDWebViewDelegate;
@interface TDWebViewController : UIViewController<UIWebViewDelegate, UITextFieldDelegate,
    UIGestureRecognizerDelegate>
@property (retain, nonatomic) IBOutlet UIWebView *wvContent;
@property (retain, nonatomic) IBOutlet UIView *vIconChoose;


@property (nonatomic, retain) NSString *sUrlStart;
@property (nonatomic, retain) NSString *sUrl;
@property (nonatomic, retain) NSArray *listPWID;
@property(nonatomic, retain) NSString *sNote;
@property (nonatomic, retain) UIImage *screenShot;

@property (nonatomic, assign) id<TDWebViewDelegate> webDelegate;
@property (nonatomic, assign) int iTag;
@property (nonatomic, assign) BOOL bIsTakeScreenShot;
@property (nonatomic, assign) BOOL bIsUseJogDial;

@end

@protocol TDWebViewDelegate <NSObject>
-(void)browserBack:(TDWebViewController*)controller;

@end