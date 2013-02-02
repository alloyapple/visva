//
//  VAGlobal.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAGlobal.h"
#import "TDDatabase.h"
#import "TDCommonLibs.h"
#import "VAGroup.h"
#import "VAElementId.h"
#import "VAPassword.h"

@interface VAGlobal()
-(void)openDatabase;
-(void)createUser;
@end

static VAGlobal* instance = nil;
@implementation VAGlobal
-(id)init{
    if ((self = [super init])) {
        instance = self;
        self.appSetting = [[[VASetting alloc] init] autorelease];
        if (_appSetting.isFirstUse) {
            self.user = [[[VAUser alloc] init] autorelease];
        }else{
            [self createUser];
        }
        
    }
    return self;
}
-(void)createUser{
    [self openDatabase];
    NSMutableArray *listUser = [VAUser getListUser:_dbManager];
    if (listUser.count > 0) {
        self.user = [listUser objectAtIndex:0];
    }else{
        TDLOGERROR(@"NO user ");
        self.user = [[[VAUser alloc] init] autorelease];
    }
}
-(void)dealloc{
    instance = nil;
    [_appSetting release];
    [_user release];
    [_dbManager release];
    [super dealloc];
}
+(VAGlobal*)share{
    if (instance != nil) {
        return instance;
       
    }
    instance = [[VAGlobal alloc] init];
    return instance;
}
+(void)releaseShare{
    [instance release];
    instance = nil;
}

#pragma mark - database
-(void)createTable{
    [_dbManager executeQuery:[VAUser getCreateTableQuery]];
    [_dbManager executeQuery:[VAGroup getCreateTableQuery]];
    [_dbManager executeQuery:[VAElementId getCreateTableQuery]];
    [_dbManager executeQuery:[VAPassword getCreateTableQuery]];
}
#define kFileName @"data.dat"
-(void)initFirstDatabase{
    NSString *path = [TDDatabase pathInDocument:kFileName];
    /*
    BOOL val = [TDDatabase deleteFile:path];
    if (!val) {
        TDLOGERROR(@"delete file error %@", path);
        return;
    }
     */
    self.dbManager = [[[TDSqlManager alloc] initWithPath:path] autorelease];
    [self createTable];
}
-(void)openDatabase{
    NSString *path = [TDDatabase pathInDocument:kFileName];
    self.dbManager = [[[TDSqlManager alloc] initWithPath:path] autorelease];
}
-(void)loadDataAfterLogin{
    [_user loadFullData:_dbManager];
}

-(BOOL)insertCurrentUser{
    if (_dbManager == nil) {
        return NO;
    }
    return [self.user insertToDb:_dbManager];
}


@end
