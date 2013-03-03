//
//  SqliteDatabaseManager.h
//  Odybird
//
//  Created by tranduc on 6/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface TDSqlManager : NSObject{
    sqlite3 *database;
}

-(sqlite3*)getDatabase;
-(id)initWithPath:(NSString*)path;
-(id)initWithPath:(NSString*)path pass:(NSString*)pass;

-(BOOL)executeQuery:(NSString*)str;
-(void)closeDatabase;
- (NSArray *)getRowsForQuery:(NSString *)sql;
#pragma mark - helper
/* Check if data is exits row with a query
 */
-(BOOL)isExitRowWithQuery:(NSString*)query;

@end

@interface TDSqliteWraper : NSObject{
    
}

@end

#pragma mark - wraper funtion
static inline NSString *TDSqlText(sqlite3_stmt*stmt, int cC){
    const unsigned char *str = sqlite3_column_text(stmt, cC);
    if (str) {
        return [NSString stringWithUTF8String:(const char*)str];
    }else{
        return nil;
    }
    
}
static inline int TDSqlInt(sqlite3_stmt *stmt, int cCollumn){
    return sqlite3_column_int(stmt, cCollumn);
}
static inline int TDSqlDouble(sqlite3_stmt *stmt, int cCollumn){
    return sqlite3_column_double(stmt, cCollumn);
}
static inline int TDSqlBindText(sqlite3_stmt *stmt, int cCollumn, NSString *str){
    return sqlite3_bind_text(stmt, cCollumn, [str UTF8String], str.length, NULL);
}
static inline int TDSqlBindInt(sqlite3_stmt *stmt, int cCollumn, int value){
    return sqlite3_bind_int(stmt, cCollumn, value);
}
static inline int TDSqlBindDouble(sqlite3_stmt *stmt, int cCollumn, int value){
    return sqlite3_bind_double(stmt, cCollumn, value);
}

static inline BOOL TDSqlExecuteQuery(NSString *query, sqlite3 *db){
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, nil)== SQLITE_OK) {
        if(sqlite3_step(stmt)==SQLITE_ERROR){
            return NO;
        }
        sqlite3_finalize(stmt);
        return YES;
    }
    return NO;
}

