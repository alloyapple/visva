//
//  VAUser.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAUser.h"
#import "TDSqlManager.h"
#import "TDLog.h"
#import "VAGroup.h"
#import "VAElementId.h"
#import "CHCSVParser.h"
#import "TDDatabase.h"
#import "TDString.h"
#import "VAPassword.h"
#import "VAGlobal.h"

NSString *duserTable = @"User";
NSString *duserId = @"userId";
NSString *duserName = @"userName";
NSString *duserPassword = @"password";
NSString *duserDeleted = @"deleted";
@implementation VAUser
-(id)init{
    if ((self = [super init])) {
        self.aUserFolder = [NSMutableArray array];
        self.iDeleted = 0;
        self.bIsLoadFullData = NO;
    }
    return self;
}
-(void)dealloc{
    [_aUserFolder release];
    [_sUserName release];
    [_sUserPassword release];
    [super dealloc];
}

#pragma mark - database
#pragma mark add
-(BOOL)addGroup:(NSString *)name database:(TDSqlManager*)manager{
    VAGroup *group = [[[VAGroup alloc] init] autorelease];
    group.user = self;
    group.sGroupName = name;
    int lastOrder = [[_aUserFolder lastObject] iOrder];
    group.iOrder = lastOrder +1;
    BOOL val = [group insertToDb:manager];
    if (val) {
        [self.aUserFolder addObject:group];
        return YES;
    }else{
        return NO;
    }
}
#pragma mark get
-(void)getListGroup:(TDSqlManager*)manager{
    self.aUserFolder = [VAGroup getListGroup:manager user:self];
    if (self.aUserFolder == nil) {
        self.aUserFolder = [NSMutableArray array];
    }
}
-(void)getListGroupWithFullData:(TDSqlManager*)manager{
    [self getListGroup:manager];
    for (VAGroup *group in _aUserFolder) {
        [group getListElementWithFullData:manager];
    }
}
-(void)loadFavoriteAndRecentIDs{
    self.favoriteGroup = [[[VAGroup alloc] init] autorelease];
    self.recentGroup = [[[VAGroup alloc] init] autorelease];
    double time = [[NSDate date] timeIntervalSince1970] - 84600;
    for (VAGroup *group in _aUserFolder) {
        for (VAElementId *element in group.aElements) {
            if (element.dTimeStamp >= time) {
                [self.recentGroup.aElements addObject:element];
            }
            if (element.iFavorite == 1) {
                [self.favoriteGroup.aElements addObject:element];
            }
        }
    }
}
-(void)loadFullData:(TDSqlManager *)manager{
    [self getListGroupWithFullData:manager];
    [self loadFavoriteAndRecentIDs];
    self.bIsLoadFullData = YES;
}

+(VAUser*)userWithFullData:(TDSqlManager *)manager{
    NSMutableArray *arr = [VAUser getListUser:manager];
    if (arr.count==0) {
        return nil;
    }
    VAUser *user = [arr objectAtIndex:0];
    [user loadFullData:manager];
    return user;
}


+(NSString *)getCreateTableQuery{
    NSString *str = [NSString stringWithFormat: @"CREATE  TABLE User (\"userId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"userName\" TEXT, \"password\" TEXT, \"deleted\" INTEGER)" ];
    return str;
}
+(NSString*)getDestroyQuery{
    return [NSString stringWithFormat: @"DELETE %@ WHERE 1;", duserTable];
}
+(NSMutableArray*)getListUser:(TDSqlManager *)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ Where %@=0", duserTable, duserDeleted];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAUser *user = [[[VAUser alloc] init] autorelease];
        user.iUserId = TDSqlInt(stmt, 0);
        user.sUserName = TDSqlText(stmt, 1);
        user.sUserPassword = TDSqlText(stmt, 2);
        //get list group
        [arr addObject:user];
    }
    return arr;
}



-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ (%@, %@, %@) VALUES (?,?,?)", duserTable, duserName, duserPassword, duserDeleted];
    TDLOG(@"Query insert VAProject = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindText(stmt, 1, _sUserName);
    TDSqlBindText(stmt, 2, _sUserPassword);
    TDSqlBindInt(stmt, 3, _iDeleted);
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iUserId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET %@ =?, %@=? ,%@=? WHERE %@=?", duserTable, duserName, duserPassword,duserDeleted, duserId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindText(stmt, 1, _sUserName);
    TDSqlBindText(stmt, 2, _sUserPassword);
    TDSqlBindInt(stmt, 3, _iDeleted);
    TDSqlBindInt(stmt, 4, _iUserId);
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE From %@ WHERE %@=%d", duserName, duserId, _iUserId];
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

-(BOOL)updateGroupOrder:(TDSqlManager *)manager{
    BOOL result = YES;
    for (int i=0; i<_aUserFolder.count; i++) {
        VAGroup *group = [_aUserFolder objectAtIndex:i];
        group.iOrder = i;
        BOOL val = [group updateOrderToDb:manager];
        result = result && val;
    }
    return result;
}

#pragma mark - import/export
#define kNumNormalField 7
-(void)exportToCSV:(NSString*)path{
    CHCSVWriter *writer = [[[CHCSVWriter alloc] initForWritingToCSVFile:path] autorelease];
    NSMutableArray *fields = [NSMutableArray arrayWithObjects:
                              @"group", @"title",
                              @"icon", @"fav",
                              @"url", @"note", @"image", nil];
    for (int i=1; i<=kMaxId; i++) {
        [fields addObject:[@"id" stringByAppendingFormat:@"%d",i]];
        [fields addObject:[@"pa" stringByAppendingFormat:@"%d",i]];
    }
    [writer writeLineOfFields:fields];
    for (VAGroup *g in _aUserFolder) {
        for (VAElementId *e in g.aElements) {
            NSMutableArray *rows = [NSMutableArray arrayWithObjects:
                                    TDSnil(g.sGroupName), TDSnil(e.sTitle),
                                    TDSnil(e.sEIcon),TDiToS(e.iFavorite),
                                    TDSnil(e.sUrl) , TDSnil(e.sNote), TDSnil(e.sImage),
                                    nil];
            for (VAPassword *pass in e.aPasswords) {
                [rows addObject:pass.sTitleNameId];
                [rows addObject:pass.sPassword];
            }
            for (int i= e.aPasswords.count; i<kMaxId; i++) {
                [fields addObject:@""];
                [fields addObject:@""];
            }
            [writer writeLineOfFields:rows];
        }
    }
    TDLOG(@"write to file %@", path);
}
-(VAGroup*)findGroupByName:(NSString*)name{
    for (VAGroup *g in _aUserFolder) {
        if ([g.sGroupName isEqualToString:name]) {
            return g;
        }
    }
    return nil;
}
-(void)readCSV:(NSString*)path dbManager:(TDSqlManager*)mana error:(NSError**)error
{
    NSArray *list = [NSArray arrayWithContentsOfCSVFile:path];
    if (list.count == 0) {
        return;
    }
    TDLOG(@"%@", [list objectAtIndex:0]);
    for (int l=1; l<list.count; l++) {
        NSArray *line = [list objectAtIndex:l];
        if (line.count < kNumNormalField) {
            TDLOGERROR(@"error file %d, %@", line.count, line);
                //NSError *e = [NSError errorWithDomain:@"error.csv" code:1 userInfo:nil];
            //&error = e;
            continue;
        }
        NSString *gName = [line objectAtIndex:0];
        VAGroup *g = [self findGroupByName:gName];
        if (!g) {
            g = [[[VAGroup alloc] init] autorelease];
            g.user = self;
            g.sGroupName = gName;
            [g insertToDb:mana];
            [self.aUserFolder addObject:g];
        }
        
        VAElementId *e = [[[VAElementId alloc] init] autorelease];
        e.sTitle = [line objectAtIndex:1];
        e.sEIcon = [line objectAtIndex:2];
        e.iFavorite = [[line objectAtIndex:3] intValue];
        e.sUrl = [line objectAtIndex:4];
        e.sNote = [line objectAtIndex:5];
        e.sImage = [line objectAtIndex:6];
        e.group = g;
        [e insertToDb:mana];
        [g.aElements addObject:e];
        
        for (int i=kNumNormalField; i+1<line.count; i=i+2) {
            NSString *sId = [line objectAtIndex:i];
            NSString *spw = [line objectAtIndex:i+1];
            if ([sId isEmpty] || [spw isEmpty]) {
                continue;
            }
            VAPassword *pass = [[[VAPassword alloc] init] autorelease];
            pass.elementId = e;
            pass.sTitleNameId = sId;
            pass.sPassword = spw;
            [pass insertToDb:mana];
            [e.aPasswords addObject:pass];
        }
        
    }
    //*error = nil;
}
@end
