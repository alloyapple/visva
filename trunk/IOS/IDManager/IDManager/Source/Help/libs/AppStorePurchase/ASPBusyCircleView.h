//
//  ASPBusyCircleView.h
//  Groupon
//
//  Created by AnhVT on 12/8/10.
//  Copyright 2010 AI&T. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ASPurchaseView;

@interface ASPBusyCircleView : UIView {
    ASPurchaseView *parent;
	UIActivityIndicatorView *busyView;
	UILabel *contentLabel;
    NSInteger retainCount;
    BOOL isRetain;
}
@property (nonatomic, retain) ASPurchaseView *parent;
@property (nonatomic, assign) BOOL isLancape;
@property (nonatomic, retain) UIActivityIndicatorView *busyView;
@property (nonatomic, retain) UILabel *contentLabel;
@property (nonatomic, assign) NSInteger retainCount;
@property (nonatomic, assign) BOOL isRetain;

- (id)init:(BOOL)setRetain;
- (void)startWithTitle:(NSString*)title;
- (void)stop;
- (BOOL)isAnimating;

@end
