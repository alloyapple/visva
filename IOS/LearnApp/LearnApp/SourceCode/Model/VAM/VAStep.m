//
//  VAStep.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAStep.h"
#import "VAProcess.h"
#import "TDSqlManager.h"
#import "TDCommonLibs.h"


NSString *dStepTable = @"step_table";
NSString *dProcessId = @"proc_id";
NSString *dStepId = @"step_id";
NSString *dStepName = @"step_name";

@implementation VAStep

-(id)init {
    self = [super init];
    if (self) {
        //
    }
    return self;
}

-(void)dealloc {
    [_sStepName release];
    [super dealloc];
}

#pragma mark - static method
+(NSString*)getCreateTableString{
    NSString *query = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (\
                       %@ INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\
                       %@ INTEGER, %@ TEXT)", dStepTable,dStepId,
                       dProcessId, dStepName];
    TDLOG(@"query VAProject create = %@", query);
    return query;
}
+(NSMutableArray*)getListStep:(TDSqlManager*)manager process:(VAProcess*)process{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ where %@ = %d", dStepTable, dProcessId, process.iProcId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAStep *step = [[[VAStep alloc] init] autorelease];
        step.iStepId = TDSqlInt(stmt, 0);
        step.parentProcess = process;
        step.sStepName = TDSqlText(stmt, 2);
        
        [arr addObject:step];
    }
    return arr;
}

#pragma mark - insert/update/delete
-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ \
                       (%@, %@) VALUES (?,?)",
                       dStepTable,
                       dProcessId, dStepName];
    TDLOG(@"Query insert VAStep = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _parentProcess.iProcId);
    TDSqlBindText(stmt, 2, _sStepName);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iStepId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ \
                       SET %@ =?, %@=? WHERE %@=?",
                       dStepTable,
                       dProcessId, dStepName, dStepId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindInt(stmt, 1, _parentProcess.iProcId);
    TDSqlBindText(stmt, 2, _sStepName);
    TDSqlBindInt(stmt, 3, _iStepId);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        return YES;
    }
}
-(BOOL)deleteFromDb:(TDSqlManager*)manager{
    NSString *query = [NSString stringWithFormat:@"DELETE from %@ WHERE %@=%d", dStepTable, dStepId, _iStepId];
    return [manager executeQuery:query];
}
@end
