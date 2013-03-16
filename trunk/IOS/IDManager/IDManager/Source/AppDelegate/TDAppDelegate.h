//
//  TDAppDelegate.h
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDViewController.h"
#import "TDIdleWindow.h"


@class TDViewController;

@interface TDAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) TDViewController *viewController;
+(TDAppDelegate*)share;

#pragma mark - Dropbox:: begin
extern NSString *kDropboxChangeLinkedStatus;
#pragma mark Dropbox:: end

@end
