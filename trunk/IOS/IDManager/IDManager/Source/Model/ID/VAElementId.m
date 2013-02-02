//
//  VAElementId.m
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAElementId.h"
#import "TDSqlManager.h"
#import "TDLog.h"
#import "VAGroup.h"
#import "VAPassword.h"

NSString *delementIdTable = @"ElementID";
NSString *deId = @"eId";
NSString *degroupId = @"groupId";
NSString *detitle = @"title";
NSString *deicon = @"icon";
NSString *detimeStamp = @"timeStamp";
NSString *dedeleted = @"deleted";
NSString *defavorite = @"favorite";
NSString *deflag = @"flag";
NSString *decountId = @"countId";
NSString *deurl = @"url";
NSString *denote = @"note";
NSString *deimage = @"image";

@implementation VAElementId

-(id)init{
    if ((self = [super init])) {
        self.iDeleted = 0;
        self.aPasswords = [NSMutableArray array];
    }
    return self;
}
-(void)dealloc{
    [_sTitle release];
    [_sEIcon release];
    [_sImage release];
    [_sUrl release];
    [_sNote release];
    [_aPasswords release];
    [super dealloc];
}
#pragma mark - database
+(NSString*)getCreateTableQuery{
    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE ElementID (\"eId\" INTEGER PRIMARY KEY  NOT NULL , \"groupId\" INTEGER, \"title\" TEXT, \"icon\" TEXT, \"timeStamp\" DOUBLE, \"deleted\" INTEGER, \"favorite\" INTEGER, \"flag\" INTEGER, \"countId\" INTEGER, \"url\" TEXT, \"note\" TEXT, \"image\" TEXT)"];
    return str;
}
+(NSMutableArray*)getListElement:(TDSqlManager *)manager group:(VAGroup *)group{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=0 and %@=%d ", delementIdTable, dedeleted, degroupId, group.iGroupId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAElementId *element = [[[VAElementId alloc] init] autorelease];
        element.iId = TDSqlInt(stmt, 0);
        element.sTitle = TDSqlText(stmt, 2);
        element.sEIcon = TDSqlText(stmt, 3);
        element.dTimeStamp = TDSqlDouble(stmt, 4);
        element.iDeleted = TDSqlInt(stmt, 5);
        element.iFavorite = TDSqlInt(stmt, 6);
        element.iFlag = TDSqlInt(stmt, 7);
        element.iCountId = TDSqlInt(stmt, 8);
        element.sUrl = TDSqlText(stmt, 9);
        element.sNote = TDSqlText(stmt, 10);
        element.sImage = TDSqlText(stmt, 11);
        [arr addObject:element];
    }
    return arr;
}

-(void)getListPassword:(TDSqlManager *)manager{
    self.aPasswords = [VAPassword getListPass:manager element:self];
}

-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ \
                       (%@, %@, %@, %@,  %@, %@, %@, %@,  %@, %@, %@) VALUES (?,?,?,? ,?,?,?,? ,?,?,?)",
                       delementIdTable, detitle, deicon, detimeStamp, dedeleted, defavorite, deflag,decountId, deurl, denote, deimage, degroupId];
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sTitle);
    TDSqlBindText(stmt, 2, _sEIcon);
    TDSqlBindDouble(stmt, 3, _dTimeStamp);
    TDSqlBindInt(stmt, 4, _iDeleted);
    TDSqlBindInt(stmt, 5, _iFavorite);
    TDSqlBindInt(stmt, 6, _iFlag);
    TDSqlBindInt(stmt, 7, _iCountId);
    TDSqlBindText(stmt, 8, _sUrl);
    TDSqlBindText(stmt, 9, _sNote);
    TDSqlBindText(stmt, 10, _sImage);
    TDSqlBindInt(stmt, 11, _group.iGroupId);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET %@ =?, %@=? ,%@=?, %@=?, %@=?, %@ =?, %@=? ,%@=?, %@=?, %@=?  WHERE %@=?",
                       delementIdTable, detitle, deicon, detimeStamp, dedeleted, defavorite, deflag,decountId, deurl, denote, deimage, deId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sTitle);
    TDSqlBindText(stmt, 2, _sEIcon);
    TDSqlBindDouble(stmt, 3, _dTimeStamp);
    TDSqlBindInt(stmt, 4, _iDeleted);
    TDSqlBindInt(stmt, 5, _iFavorite);
    TDSqlBindInt(stmt, 6, _iFlag);
    TDSqlBindInt(stmt, 7, _iCountId);
    TDSqlBindText(stmt, 8, _sUrl);
    TDSqlBindText(stmt, 9, _sNote);
    TDSqlBindText(stmt, 10, _sImage);
    TDSqlBindInt(stmt, 11, _iId);
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE %@ WHERE %@=%d", delementIdTable, dedeleted, _iId];
    return [manager executeQuery:query];
}

-(BOOL)weakDeleteFromDb:(TDSqlManager*)manager{
    self.iDeleted = 1;
    BOOL ret = [self updateToDb:manager];
    if (ret) {
        return YES;
    }else{
        self.iDeleted = 0;
        return NO;
    }
}
@end
