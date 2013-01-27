//
//  TDAppDelegate.h
//  LearnApp
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VARootViewController.h"
@interface TDAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, retain) VARootViewController *rootViewController;
/*
 * @return: Share instance of TDAppDelegate
 */
+(TDAppDelegate*)share;
@end
