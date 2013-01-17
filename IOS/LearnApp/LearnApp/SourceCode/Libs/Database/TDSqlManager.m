//
//  SqliteDatabaseManager.m
//  Odybird
//
//  Created by tranduc on 6/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TDSqlManager.h"
#import "TDDatabase.h"
#import "TDLog.h"

@implementation TDSqlManager
-(sqlite3*)getDatabase{
    return database;
}
/*
 * Init sqlite data base with path
 */
-(id)initWithPath:(NSString*)path{
    self = [super init];
    if (self) {
        if(sqlite3_open([path UTF8String], &database)!= SQLITE_OK){
            sqlite3_close(database);
            database = NULL;
            NSAssert(0, @"Fail to open data base");
        }
    }
    return self;
}

-(void)closeDatabase{
    if (database != NULL) {
        sqlite3_close(database);
    }
    database = NULL;
}
-(void)dealloc{
    [self closeDatabase];
    [super dealloc];
}

-(BOOL)isExitRowWithQuery:(NSString*)query{
    sqlite3_stmt *stmt;
    BOOL isExit = NO;
    if (sqlite3_prepare_v2(database, [query UTF8String], -1, &stmt, nil)== SQLITE_OK) {
        if (sqlite3_step(stmt)==SQLITE_ROW) {
            isExit = YES;
        }
        sqlite3_finalize(stmt);
    }
    return isExit;
}


@end
