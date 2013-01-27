//
//  VAProject.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAProject.h"
#import "TDLog.h"
#import "TDSqlManager.h"

@implementation VAProject

#pragma mark - fields Name

  NSString *const dProjTable = @"T_Project";
  NSString *const dProjId = @"project_id";
  NSString *const dProjName = @"project_name";
  NSString *const dProjLocation = @"location";
  NSString *const dProjCompany = @"company";
  NSString *const dProjDescription = @"description";
  NSString *const dProjNote = @"Note";

#pragma mark - static method
+(NSString*)getCreateProjTableString{
    NSString *query = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (\
                       %@ INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\
                       %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT)", dProjTable,dProjId,
                       dProjName, dProjLocation, dProjCompany, dProjDescription, dProjNote];
    TDLOG(@"query VAProject create = %@", query);
    return query;
}
+(NSMutableArray*)getListProject:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@", dProjTable];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAProject *proj = [[[VAProject alloc] init] autorelease];
        proj.iPrId = TDSqlInt(stmt, 0);
        proj.sPrName = TDSqlText(stmt, 1);
        proj.sLocation = TDSqlText(stmt, 2);
        proj.sCompany = TDSqlText(stmt, 3);
        proj.sDescription = TDSqlText(stmt, 4);
        proj.sNote = TDSqlText(stmt, 5);
        [arr addObject:proj];
    }
    return arr;
}

#pragma mark - insert/update/delete
-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ (%@, %@, %@, %@, %@) VALUES (?,?,?,?,?)", dProjTable,
                       dProjName, dProjLocation, dProjCompany, dProjDescription, dProjNote];
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sPrName);
    TDSqlBindText(stmt, 2, _sLocation);
    TDSqlBindText(stmt, 3, _sCompany);
    TDSqlBindText(stmt, 4, _sDescription);
    TDSqlBindText(stmt, 5, _sNote);

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iPrId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET %@ =?, %@=?, %@=?, %@=?, %@=? WHERE %@=?", dProjTable,
                       dProjName, dProjLocation, dProjCompany, dProjDescription, dProjNote, dProjName];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sPrName);
    TDSqlBindText(stmt, 2, _sLocation);
    TDSqlBindText(stmt, 3, _sCompany);
    TDSqlBindText(stmt, 4, _sDescription);
    TDSqlBindText(stmt, 5, _sNote);
    TDSqlBindInt(stmt, 6, _iPrId);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iPrId = lastIndex;
        return YES;
    }
}
-(BOOL)deleteFromDb:(TDSqlManager*)manager{
    NSString *query = [NSString stringWithFormat:@"DELETE %@ WHERE %@=%d", dProjTable, dProjId, _iPrId];
    return [manager executeQuery:query];
}

#pragma mark - get method
-(NSString *)getDisplayNameWithCompany{
    return [NSString stringWithFormat:@"%@ %@", _sCompany, _sPrName];
}
-(id)init{
    self = [super init];
    if (self) {
        self.aProcesses = [NSMutableArray array];
    }
    return self;
}
#pragma mark - method
-(void)addProcess:(VAProcess *)proc{
    [self.aProcesses addObject:proc];
}

#pragma mark - dealloc
-(void)dealloc{
    [_sPrName release];
    [_sCompany release];
    [_sDescription release];
    [_sLocation release];
    [_sNote release];
    [_aProcesses release];
    [super dealloc];
}

@end






