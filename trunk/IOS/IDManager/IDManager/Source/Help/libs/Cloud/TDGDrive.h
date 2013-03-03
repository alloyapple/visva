//
//  TDGDrive.h
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GTLDrive.h"
#import "GTMOAuth2ViewControllerTouch.h"

@interface TDGDrive : NSObject
+(GTLServiceDrive*)shareService;
+(TDGDrive*)shareInstance;
- (BOOL)isAuthorized;
- (void)authorizedFrom:(UIViewController*)vc;
- (void)unlinkAll;
extern NSString *kGDriverChangeAuthenStatus;
@end

