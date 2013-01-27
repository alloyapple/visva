//
//  VAGlobal.h
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VASetting.h"
#import "VAUser.h"
@interface VAGlobal : NSObject
@property(nonatomic, retain)VASetting *appSetting;
@property(nonatomic, retain)VAUser *user;
+(VAGlobal*)share;
+(void)releaseShare;
@end
