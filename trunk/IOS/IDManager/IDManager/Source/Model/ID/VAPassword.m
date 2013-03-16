//
//  VAPassword.m
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAPassword.h"
#import "TDSqlManager.h"
#import "VAElementId.h"
#import "TDLog.h"
NSString *dpasswordTable = @"Password";
NSString *dpasswordId = @"passwordId";
NSString *dpassElementId = @"eId";
NSString *dpassTitleNameId = @"titleNameId";
NSString *dpasswordCollumn = @"password";

@interface VAPassword()
/*
 * Insert Group into database. Set iPrId to new inserted id.
 * @return: YES if complete, NO if error
 */
-(BOOL)insertToDb:(TDSqlManager*)manager;

/*
 * Update Group into database.
 * @return: YES if complete, NO if error
 */
-(BOOL)updateToDb:(TDSqlManager*)manager;

@end
@implementation VAPassword

-(id)init{
    if (self = [super init]) {
        _iPasswordId = -1;
        _isInDatabase = NO;
    }
    return self;
}
#pragma mark - database
+(NSString*)getCreateTableQuery{
    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE Password (\"passwordId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"eId\" INTEGER, \"titleNameId\" TEXT, \"password\" TEXT)"];
    return str;
}
+(NSString*)getDestroyQuery{
    return [NSString stringWithFormat: @"DELETE From %@ WHERE 1;", dpasswordTable];
}

+(NSMutableArray*)getListPass:(TDSqlManager *)manager element:(VAElementId *)element{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=%d", dpasswordTable, dpassElementId,element.iId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAPassword *pass = [[[VAPassword alloc] init] autorelease];
        pass.iPasswordId = TDSqlInt(stmt, 0);
        pass.sTitleNameId = TDSqlText(stmt, 2);
        pass.sPassword = TDSqlText(stmt, 3);
        pass.elementId = element;
        pass.isInDatabase = YES;
        //get list group
        [arr addObject:pass];
    }
    return arr;
}

-(BOOL)saveToDB:(TDSqlManager *)manager{
    if (_isInDatabase) {
        return [self updateToDb:manager];
    }else{
        return [self insertToDb:manager];
    }
}

-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ (%@, %@, %@) VALUES (?,?,?)", dpasswordTable, dpassTitleNameId, dpasswordCollumn, dpassElementId];
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sTitleNameId);
    TDSqlBindText(stmt, 2, _sPassword);
    TDSqlBindInt(stmt, 3, _elementId.iId);
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.isInDatabase = YES;
        self.iPasswordId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET %@ =?, %@=? ,%@=? WHERE %@=?", dpasswordTable, dpassTitleNameId, dpasswordCollumn, dpassElementId, dpasswordId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sTitleNameId);
    TDSqlBindText(stmt, 2, _sPassword);
    TDSqlBindInt(stmt, 3, _elementId.iId);
    TDSqlBindInt(stmt, 4, _iPasswordId);
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE %@ WHERE %@=%d", dpasswordTable, dpasswordId, _iPasswordId];
    return [manager executeQuery:query];
}

+(BOOL)didDeleteFromDb:(TDSqlManager*)manager elementId:(VAElementId*)ele{
    NSString *query = [NSString stringWithFormat:@"DELETE %@ WHERE %@=%d", dpasswordTable, dpassElementId, ele.iId];
    return [manager executeQuery:query];
}
-(void)copyFrom:(VAPassword *)pass{
    self.iPasswordId = pass.iPasswordId;
    self.elementId = pass.elementId;
    self.sTitleNameId = pass.sTitleNameId;
    self.sPassword = pass.sPassword;
    self.isInDatabase = pass.isInDatabase;
}
@end
