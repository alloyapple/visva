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
NSString *degroupId = @"eGroupId";
NSString *detitle = @"eTitle";
NSString *deicon = @"eIcon";
NSString *detimeStamp = @"eTimeStamp";
//NSString *dedeleted = @"eDeleted";
NSString *defavorite = @"eFavorite";
NSString *deflag = @"eFlag";
//NSString *decountId = @"eCountId";
NSString *deurl = @"eUrl";
NSString *denote = @"eNote";
NSString *deimage = @"eImage";
NSString *deOrder = @"eOrder";

@implementation VAElementId

-(id)init{
    if ((self = [super init])) {
        //self.iDeleted = 0;
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
//    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE %@ (\
//                     %@ INTEGER PRIMARY KEY  NOT NULL , %@ INTEGER, %@ TEXT, %@ TEXT,\
//                     %@ DOUBLE, %@ INTEGER, %@ INTEGER, %@ INTEGER, \
//                     %@ INTEGER, %@ TEXT, %@ TEXT, %@ TEXT,\
//                     %@ INTEGER)",
//                     delementIdTable,
//                     deId, degroupId, detitle, deicon,
//                     detimeStamp, dedeleted, defavorite, deflag,
//                     decountId, deurl, denote, deimage,
//                     deOrder];
    
    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE %@ (\
                     %@ INTEGER PRIMARY KEY  NOT NULL , %@ INTEGER, %@ TEXT, %@ TEXT,\
                     %@ DOUBLE, %@ INTEGER, %@ INTEGER, \
                     %@ TEXT, %@ TEXT, %@ TEXT,\
                     %@ INTEGER)",
                     delementIdTable,
                     deId, degroupId, detitle, deicon,
                     detimeStamp, defavorite, deflag,
                     deurl, denote, deimage,
                     deOrder];
    return str;
}
+(NSString*)getDestroyQuery{
    return [NSString stringWithFormat: @"DELETE %@ WHERE 1;", delementIdTable];
}

+(NSMutableArray*)getListElement:(TDSqlManager *)manager group:(VAGroup *)group{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
//    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=0 and %@=%d ORDER BY %@ ", delementIdTable, dedeleted, degroupId, group.iGroupId, deOrder];
    
     NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=%d ORDER BY %@ ", delementIdTable, degroupId, group.iGroupId, deOrder];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAElementId *element = [[[VAElementId alloc] init] autorelease];
        element.iId = TDSqlInt(stmt, 0);
        element.group = group;
        element.sTitle = TDSqlText(stmt, 2);
        element.sEIcon = TDSqlText(stmt, 3);
        element.dTimeStamp = TDSqlDouble(stmt, 4);
        //element.iDeleted = TDSqlInt(stmt, 5);
        element.iFavorite = TDSqlInt(stmt, 5);
        element.iFlag = TDSqlInt(stmt, 6);
        //element.iCountId = TDSqlInt(stmt, 8);
        element.sUrl = TDSqlText(stmt, 7);
        element.sNote = TDSqlText(stmt, 8);
        element.sImage = TDSqlText(stmt, 9);
        element.iOrder = TDSqlInt(stmt, 10);
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
//    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ \
//                       (%@, %@, %@, %@,  \
//                       %@, %@, %@, %@,  \
//                       %@, %@, %@, %@) VALUES (?,?,?,? ,?,?,?,? ,?,?,?,?)",
//                       delementIdTable,
//                       detitle, deicon, detimeStamp, dedeleted,
//                       defavorite, deflag,decountId, deurl,
//                       denote, deimage, degroupId, deOrder];
    
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ \
                       (%@, %@, %@,  \
                       %@, %@, %@,  \
                       %@, %@, %@, %@) VALUES (?,?,? ,?,?,? ,?,?,?,?)",
                       delementIdTable,
                       detitle, deicon, detimeStamp,
                       defavorite, deflag, deurl,
                       denote, deimage, degroupId, deOrder];
    
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sTitle);
    TDSqlBindText(stmt, 2, _sEIcon);
    TDSqlBindDouble(stmt, 3, _dTimeStamp);
    //TDSqlBindInt(stmt, 4, _iDeleted);
    TDSqlBindInt(stmt, 4, _iFavorite);
    TDSqlBindInt(stmt, 5, _iFlag);
    //TDSqlBindInt(stmt, 7, _iCountId);
    TDSqlBindText(stmt, 6, _sUrl);
    TDSqlBindText(stmt, 7, _sNote);
    TDSqlBindText(stmt, 8, _sImage);
    TDSqlBindInt(stmt, 9, _group.iGroupId);
    TDSqlBindInt(stmt, 10, _iOrder);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iId = lastIndex;
        
        //insert list id
        for (VAPassword *pass in _aPasswords) {
            [pass saveToDB: manager];
        }
        
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
//    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET \
//                       %@ =?, %@=? ,%@=?, %@=?,\
//                       %@=?, %@ =?, %@=? ,%@=?,\
//                       %@=?, %@=?, %@=?, %@=?\
//                       WHERE %@=?",
//                       delementIdTable,
//                       detitle, deicon, detimeStamp, dedeleted,
//                       defavorite, deflag,decountId, deurl,
//                       denote, deimage, degroupId, deOrder, 
//                       deId];
    
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET \
                       %@ =?, %@=? ,%@=?,\
                       %@=?, %@ =?, %@=?,\
                       %@=?, %@=?, %@=?, %@=?\
                       WHERE %@=?",
                       delementIdTable,
                       detitle, deicon, detimeStamp,
                       defavorite, deflag, deurl,
                       denote, deimage, degroupId, deOrder,
                       deId];
    
    TDLOG(@"query element=%@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sTitle);
    TDSqlBindText(stmt, 2, _sEIcon);
    TDSqlBindDouble(stmt, 3, _dTimeStamp);
    //TDSqlBindInt(stmt, 4, _iDeleted);
    TDSqlBindInt(stmt, 4, _iFavorite);
    TDSqlBindInt(stmt, 5, _iFlag);
    //TDSqlBindInt(stmt, 7, _iCountId);
    TDSqlBindText(stmt, 6, _sUrl);
    TDSqlBindText(stmt, 7, _sNote);
    TDSqlBindText(stmt, 8, _sImage);
    TDSqlBindInt(stmt, 9, _group.iGroupId);
    TDSqlBindInt(stmt, 10, _iOrder);
    TDSqlBindInt(stmt, 11, _iId);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        for (VAPassword *pass in _aPasswords) {
            [pass saveToDB: manager];
        }
        return YES;
    }
}

-(BOOL)updateOrderToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET \
                       %@ =? WHERE %@=?",
                       delementIdTable,
                       deOrder, deId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _iOrder);
    TDSqlBindInt(stmt, 2, _iId);
    
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
//    NSString *query = [NSString stringWithFormat:@"DELETE From %@ WHERE %@=%d", delementIdTable, dedeleted, _iId];
    
    NSString *query = [NSString stringWithFormat:@"DELETE From %@ WHERE %@=%d", delementIdTable, deId, _iId];
    
    BOOL value = [manager executeQuery:query];
    BOOL rtnDeletePw = [VAPassword didDeleteFromDb:manager elementId:self];
    return value && rtnDeletePw;
}

-(BOOL)weakDeleteFromDb:(TDSqlManager*)manager{
    return [self deleteFromDb:manager];
    
    //self.iDeleted = 1;
    BOOL ret = [self updateToDb:manager];
    if (ret) {
        return YES;
    }else{
        //self.iDeleted = 0;
        return NO;
    }
}

@end
