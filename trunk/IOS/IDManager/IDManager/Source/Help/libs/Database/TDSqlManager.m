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
-(BOOL)executeQuery:(NSString *)str{
    if (database == NULL) {
        return NO;
    }
    return TDSqlExecuteQuery(str, database);
}
- (NSArray *)getRowsForQuery:(NSString *)sql {
	if (database != NULL) {
        TDLOGERROR(@"Database is not opened");
        return nil;
    }
    
	NSMutableArray *resultsArray = [[[NSMutableArray alloc] initWithCapacity:1] autorelease];
	
	sqlite3_stmt *statement;
	const char *query = [sql UTF8String];
	int returnCode = sqlite3_prepare_v2(database, query, -1, &statement, NULL);
	
	if (returnCode == SQLITE_ERROR) {
		return nil;
	}
	
	while (sqlite3_step(statement) == SQLITE_ROW) {
		int columns = sqlite3_column_count(statement);
		NSMutableDictionary *result = [[[NSMutableDictionary alloc] initWithCapacity:columns] autorelease];
        
		for (int i = 0; i<columns; i++) {
			const char *name = sqlite3_column_name(statement, i);
            
			NSString *columnName = [NSString stringWithCString:name encoding:NSUTF8StringEncoding];
			
			int type = sqlite3_column_type(statement, i);
			
			switch (type) {
				case SQLITE_INTEGER:
				{
					int value = sqlite3_column_int(statement, i);
					[result setObject:[NSNumber numberWithInt:value] forKey:columnName];
					break;
				}
				case SQLITE_FLOAT:
				{
					double value = sqlite3_column_double(statement, i);
					[result setObject:[NSNumber numberWithDouble:value] forKey:columnName];
					break;
				}
                
				case SQLITE_TEXT:
				{
					const char *value = (const char*)sqlite3_column_text(statement, i);
					[result setObject:[NSString stringWithCString:value encoding:NSUTF8StringEncoding] forKey:columnName];
					break;
				}
                    
				case SQLITE_BLOB:
					break;
				case SQLITE_NULL:
					[result setObject:[NSNull null] forKey:columnName];
					break;
                    
				default:
				{
					const char *value = (const char *)sqlite3_column_text(statement, i);
					[result setObject:[NSString stringWithCString:value encoding:NSUTF8StringEncoding] forKey:columnName];
					break;
				}
                    
			} //end switch
			
			
		} //end for
		
		[resultsArray addObject:result];
	} //end while
	sqlite3_finalize(statement);
	return resultsArray;
	
}


@end
