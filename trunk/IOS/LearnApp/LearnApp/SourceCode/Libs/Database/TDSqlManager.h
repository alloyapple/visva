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
-(void)closeDatabase;

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
    return [NSString stringWithUTF8String:(const char*)sqlite3_column_text(stmt, cC)];
}