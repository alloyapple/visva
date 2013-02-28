//
//  VAProcess.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAProcess.h"
#import "VAProject.h"
#import "VAStep.h"
#import "TDSqlManager.h"
#import "TDCommonLibs.h"

NSString * dProcTable = @"tprocess";

NSString * dProjectId = @"proj_id";
NSString * dProcId = @"proc_id";
NSString * dVersionId=@"version_id";
NSString * dProcName = @"proc_name";
NSString * dsStartPoint=@"start_point";
NSString * dsEndPoint=@"end_point";
NSString * dProcDescription=@"proc_desc";
NSString * dProcDefectNote=@"proc_defect_note";

NSString * dUptime=@"uptime";
NSString * dValueAddingTime=@"value_add_time";
NSString * dNonValAddingTime=@"non_add_time";
NSString * dDefect=@"defect";
NSString * dNeedVerify=@"need_verify";

@implementation VAProcess
-(id)init{
    if ((self = [super init])) {
        self.aSteps = [NSMutableArray array];
    }
    return self;
}
- (void)dealloc {
    [_sProcName release];
    [_sProcDefectNote release];
    [_sProcDescription release];
    [_aSteps release];
    [_sStartPoint  release];
    [_sEndPoint release];
    [super dealloc];
}

-(void)addStep:(VAStep *)step{
    [self.aSteps addObject:step];
}

+(NSString*)getCreateTableString{
    NSString *query = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (\
                       %@ INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\
                       %@ INTEGER, %@ INTEGER, %@ TEXT, %@ TEXT, %@ TEXT, \
                       %@ TEXT,  %@ DOUBLE,  %@ DOUBLE,  %@ DOUBLE,  %@ DOUBLE, %@ INTEGER,\
                       %@ TEXT)",
                       dProcTable,
                       dProcId,
                       dProjectId, dVersionId, dsStartPoint, dsEndPoint, dProcDescription,
                       dProcDefectNote,dUptime, dValueAddingTime, dNonValAddingTime, dDefect, dNeedVerify,
                       dProcName ];
    TDLOG(@"query VAProject create = %@", query);
    return query;
}
+(NSMutableArray*)getListProcess:(TDSqlManager*)manager project:(VAProject*)proj{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ WHERE %@ = %d", dProcTable, dProjectId, proj.iPrId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAProcess *process = [[[VAProcess alloc] init] autorelease];
        process.iProcId = TDSqlInt(stmt, 0);
        process.parentProject = proj;
        process.iVersionId = TDSqlInt(stmt, 2);
        process.sStartPoint = TDSqlText(stmt, 3);
        process.sEndPoint = TDSqlText(stmt, 4);
        process.sProcDescription = TDSqlText(stmt, 5);
        process.sProcDefectNote = TDSqlText(stmt, 6);
        process.tUptime = TDSqlDouble(stmt, 7);
        process.tValueAddingTime = TDSqlDouble(stmt, 8);
        process.tNonValAddingTime = TDSqlDouble(stmt, 9);
        process.fDefect = TDSqlDouble(stmt, 10);
        process.bNeedVerify = TDSqlInt(stmt, 11);
        process.sProcName = TDSqlText(stmt, 12);
        
        [arr addObject:process];
    }
    return arr;
}

#pragma mark - insert/update/delete
-(BOOL)insertToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"INSERT INTO %@ \
                       (%@, %@, %@, \
                       %@, %@, %@, %@,\
                       %@, %@, %@, %@, %@ ) VALUES (?,?,?, ?,?,?,?, ?,?,?,?,?)",
                       dProcTable,
                       dProjectId, dVersionId, dsStartPoint,
                       dsEndPoint, dProcDescription,dProcDefectNote,dUptime,
                       dValueAddingTime, dNonValAddingTime, dDefect, dNeedVerify, dProcName ];
    TDLOG(@"Query insert VAProcess = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _parentProject.iPrId);
    TDSqlBindInt(stmt, 2, _iVersionId);
    
    TDSqlBindText(stmt, 3, _sStartPoint);
    TDSqlBindText(stmt, 4, _sEndPoint);
    TDSqlBindText(stmt, 5, _sProcDescription);
    TDSqlBindText(stmt, 6, _sProcDefectNote);
    
    TDSqlBindDouble(stmt, 7, _tUptime);
    TDSqlBindDouble(stmt, 8, _tValueAddingTime);
    TDSqlBindDouble(stmt, 9, _tNonValAddingTime);
    TDSqlBindDouble(stmt, 10, _fDefect);
    TDSqlBindInt(stmt, 11, _bNeedVerify);
    
    TDSqlBindText(stmt, 12, _sProcName);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iProcId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ SET \
                       %@ =?, %@=?, %@=?,\
                       %@ =?, %@=?, %@=?, %@=?,\
                       %@ =?, %@=?, %@=?, %@=?,\
                       %@=? \
                       WHERE %@=?"
                       ,dProcTable,
                       dProjectId, dVersionId, dsStartPoint,
                       dsEndPoint, dProcDescription,dProcDefectNote,dUptime,
                       dValueAddingTime, dNonValAddingTime, dDefect, dNeedVerify
                       ,dProcName,
                       dProcId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindInt(stmt, 1, _parentProject.iPrId);
    TDSqlBindInt(stmt, 2, _iVersionId);
    
    TDSqlBindText(stmt, 3, _sStartPoint);
    TDSqlBindText(stmt, 4, _sEndPoint);
    TDSqlBindText(stmt, 5, _sProcDescription);
    TDSqlBindText(stmt, 6, _sProcDefectNote);
    
    TDSqlBindDouble(stmt, 7, _tUptime);
    TDSqlBindDouble(stmt, 8, _tValueAddingTime);
    TDSqlBindDouble(stmt, 9, _tNonValAddingTime);
    TDSqlBindDouble(stmt, 10, _fDefect);
    TDSqlBindInt(stmt, 11, _bNeedVerify);
    
    TDSqlBindText(stmt, 12, _sProcName);
    TDSqlBindInt(stmt, 13, _iProcId);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        return YES;
    }
}

-(BOOL)weakDeleteFromDb:(TDSqlManager*)manager{
    NSString *query = [NSString stringWithFormat:@"DELETE from %@ WHERE %@=%d", dProcTable, dProcId, _iProcId];
    TDLOG(@"query delete = %@", query);
    return [manager executeQuery:query];
}

-(BOOL)deleteFromDb:(TDSqlManager*)manager{
    if ([self weakDeleteFromDb:manager]) {
        for (VAStep *step in _aSteps) {
            [step deleteFromDb:manager];
        }
        return YES;
    }
    return NO;
}

-(void)getListStep:(TDSqlManager *)manager{
    self.aSteps = [VAStep getListStep:manager process:self];
    for (VAStep *step in _aSteps) {
        [step getListCircle:manager];
    }
}

@end
