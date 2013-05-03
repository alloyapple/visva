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
#import "TDAppDelegate.h"
#import "TDSyn.h"

@interface VAGlobal()
-(void)openDatabase;
-(void)createUser;
@end

static VAGlobal* instance = nil;
#define kFileName @"idpxData.sqlite"
#define kPassSqlite @"idxpass_@1234#!"


@implementation VAGlobal

#pragma mark - init - dealoc
-(id)init{
    if ((self = [super init])) {
        instance = self;
        _dbFileName = [kFileName retain];
        self.appSetting = [[[VASetting alloc] init] autorelease];
        
        if (_appSetting.isFirstUse) {
            self.user = [[[VAUser alloc] init] autorelease];
        }else{
            [self openDatabase];
            [self createUser];
        }
    }
    return self;
}
-(void)dealloc{
    instance = nil;
    [_appSetting release];
    [_dbFileName release];
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

-(void)createUser{
    
    NSMutableArray *listUser = [VAUser getListUser:_dbManager];
    if (listUser.count > 0) {
        self.user = [listUser objectAtIndex:0];
    }else{
        TDLOGERROR(@"NO user ");
        self.user = [[[VAUser alloc] init] autorelease];
    }
}
-(void)reloadUserData{
    [self openDatabase];
    [self createUser];
    [_user loadFullData:_dbManager];
}


#pragma mark - database
-(void)createTable{
    [_dbManager executeQuery:[VAUser getCreateTableQuery]];
    [_dbManager executeQuery:[VAGroup getCreateTableQuery]];
    [_dbManager executeQuery:[VAElementId getCreateTableQuery]];
    [_dbManager executeQuery:[VAPassword getCreateTableQuery]];
    
}

-(void)initFirstDatabase{
    [TDDatabase copyFromBundleToDocument:@"Thumb"];
    [self openDatabase];
    [self destroyData];
    [self createTable];
}
-(void)openDatabase{
    NSString *path = [TDDatabase pathInDocument:_dbFileName];
    TDLOG(@"Open %@", path);
    if (_dbManager) {
        [self.dbManager closeDatabase];
    }
    self.dbManager = [[[TDSqlManager alloc] initWithPath:path pass:kPassSqlite] autorelease];
}
-(void)destroyData{
    [_dbManager executeQuery:[VAUser getDestroyQuery]];
    [_dbManager executeQuery:[VAGroup getDestroyQuery]];
    [_dbManager executeQuery:[VAElementId getDestroyQuery]];
    [_dbManager executeQuery:[VAPassword getDestroyQuery]];
    [[TDGDrive shareInstance] unlinkAll];
    [[DBSession sharedSession] unlinkAll];
    
    [_appSetting makeDefault];
    [_appSetting saveSetting];
}
-(void)loadDataAfterLogin{
    [_user loadFullData:_dbManager];
}

-(BOOL)insertCurrentUser{
    if (_dbManager == nil) {
        return NO;
    }
    BOOL v = [self.user insertToDb:_dbManager];
    return v;
}


@end
