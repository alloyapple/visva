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
#import "TDSqlManager.h"
@interface VAGlobal : NSObject
@property(nonatomic, readonly)NSString *dbFileName;
@property(nonatomic, retain)VASetting *appSetting;
@property(nonatomic, retain)VAUser *user;
@property(nonatomic, retain)TDSqlManager *dbManager;

+(VAGlobal*)share;
+(void)releaseShare;
-(void)initFirstDatabase;
-(BOOL)insertCurrentUser;
-(void)loadDataAfterLogin;
-(void)destroyData;
-(void)reloadUserData;
@end
