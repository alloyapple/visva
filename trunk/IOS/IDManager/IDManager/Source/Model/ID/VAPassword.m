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
@implementation VAPassword

-(id)init{
    if (self = [super init]) {
        _iPasswordId = -1;
    }
    return self;
}
#pragma mark - database
+(NSString*)getCreateTableQuery{
    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE Password (\"passwordId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"eId\" INTEGER, \"titleNameId\" TEXT, \"password\" TEXT)"];
    return str;
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
        //get list group
        [arr addObject:pass];
    }
    return arr;
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
@end
