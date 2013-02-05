//
//  VAGlobal.m
//  LearnApp
//
//  Created by tranduc on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAGlobal.h"
#import "TDDatabase.h"
#import "VAProject.h"
#import "VAProcess.h"
#import "VAStep.h"

static VAGlobal* instance = nil;
@implementation VAGlobal
#pragma mark - share instance
+(VAGlobal*)share{
    if (instance == nil) {
        instance = [[VAGlobal alloc] init];
    }
    return instance;
}
+(void)releaseGlobal{
    [instance release];
}

#pragma mark - init/dealloc
-(void)dealloc{
    instance = nil;
    [_dbManager release];
    [super dealloc];
}
-(id)init{
    if ((self = [super init])) {
        [self openDatabase];
    }
    return self;
}

#pragma mark - database
-(void)openDatabase{
    NSString *dataName = @"learnApp.sqlite";
    NSString *path = [TDDatabase pathInDocument:dataName];
    self.dbManager = [[[TDSqlManager alloc] initWithPath:path] autorelease];
    
    //create table if not exits
    [_dbManager executeQuery:[VAProject getCreateTableString]];
    [_dbManager executeQuery:[VAProcess getCreateTableString]];
    [_dbManager executeQuery:[VAStep getCreateTableString]];
}
@end
