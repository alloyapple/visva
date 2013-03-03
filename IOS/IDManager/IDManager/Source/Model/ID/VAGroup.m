//
//  VAGroup.m
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAGroup.h"
#import "TDSqlManager.h"
#import "TDLog.h"
#import "VAUser.h"
#import "VAElementId.h"

NSString *dgroupTable = @"GroupFolder";
NSString *dgroupId = @"gId";
NSString *dgroupName = @"gName";
NSString *dgroupType = @"gType";
NSString *dgroupIcon = @"gIcon";
NSString *dgrUserId = @"gUserId";
NSString *dgrDeleted = @"gDeleted";
NSString *diOrder = @"gOrder";
@implementation VAGroup
-(id)init{
    if ((self = [super init])) {
        self.iDeleted = 0;
        self.aElements = [NSMutableArray array];
    }
    return self;
}
-(void)dealloc{
    [_aElements release];
    [_sGroupIcon release];
    [_sGroupName release];
    [super dealloc];
}



#pragma mark - database
-(void)getListElement:(TDSqlManager *)manager{
    self.aElements = [VAElementId getListElement:manager group:self];
}
-(void)getListElementWithFullData:(TDSqlManager*)manager{
    [self getListElement:manager];
    for (VAElementId *element in _aElements) {
        [element getListPassword:manager];
    }
}

+(NSString*)getCreateTableQuery{
    NSString *str = [NSString stringWithFormat:@"CREATE  TABLE %@ (\
                     %@ INTEGER PRIMARY KEY  NOT NULL,\
                     %@ TEXT, %@ INTEGER, %@ TEXT, %@ INTEGER,\
                     %@ INTEGER, %@ INTEGER)",
                     dgroupTable,
                     dgroupId,
                     dgroupName, dgroupType, dgroupIcon, dgrUserId,
                     dgrDeleted, diOrder];
    return str;
}
+(NSString*)getDestroyQuery{
    return [NSString stringWithFormat: @"DELETE From %@ WHERE 1;", dgroupTable];
}

+(NSMutableArray*)getListGroup:(TDSqlManager *)manager user:(VAUser *)user{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=0 and %@=%d ORDER BY %@", dgroupTable, dgrDeleted, dgrUserId, user.iUserId, diOrder];
    sqlite3_stmt *stmt;
    TDLOG(@"GetGroup=%@", query);
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAGroup *group = [[[VAGroup alloc] init] autorelease];
        group.iGroupId = TDSqlInt(stmt, 0);
        group.sGroupName = TDSqlText(stmt, 1);
        group.iGroupType = TDSqlInt(stmt, 2);
        group.sGroupIcon = TDSqlText(stmt, 3);
        group.user = user;
        //get list group
        [arr addObject:group];
    }
    return arr;
}
-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ (\
                       %@, %@, %@, %@, \
                       %@, %@) VALUES (?,?,?,?,?,?)",
                       dgroupTable,
                       dgroupName, dgroupType, dgroupIcon, dgrUserId,
                       dgrDeleted, diOrder];
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sGroupName);
    TDSqlBindInt(stmt, 2, _iGroupType);
    TDSqlBindText(stmt, 3, _sGroupIcon);
    TDSqlBindInt(stmt, 4, _user.iUserId);
    TDSqlBindInt(stmt, 5, _iDeleted);
    TDSqlBindInt(stmt, 6, _iOrder);
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iGroupId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET \
                       %@ =?, %@=? ,%@=?, %@=?,\
                       %@=?, %@=?  WHERE %@=?",
                       dgroupTable,
                       dgroupName, dgroupType,dgroupIcon,dgrUserId,
                       dgrDeleted, diOrder, dgroupId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sGroupName);
    TDSqlBindInt(stmt, 2, _iGroupType);
    TDSqlBindText(stmt, 3, _sGroupIcon);
    TDSqlBindInt(stmt, 4, _user.iUserId);
    TDSqlBindInt(stmt, 5, _iDeleted);
    TDSqlBindInt(stmt, 6, _iOrder);
    TDSqlBindInt(stmt, 7, _iGroupId);
    
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
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
                       dgroupTable,
                       diOrder, dgroupId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _iOrder);
    TDSqlBindInt(stmt, 2, _iGroupId);
    
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE %@ WHERE %@=%d", dgroupName, dgrDeleted, _iGroupId];
    BOOL returnValue = [manager executeQuery:query];
    if (!returnValue) {
        return returnValue;
    }
    for (VAElementId *element in _aElements) {
        BOOL rtnDeleteEle = [element deleteFromDb:manager];
        returnValue = returnValue&rtnDeleteEle;
    }
    return returnValue;
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
-(void)updateElementOrder:(TDSqlManager*)manager{
    for (int i=0; i<_aElements.count; i++) {
        VAElementId *element = [_aElements objectAtIndex:i];
        element.iOrder = i;
        [element updateOrderToDb:manager];
    }
}

@end
