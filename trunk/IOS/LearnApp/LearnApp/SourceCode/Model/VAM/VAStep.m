//
//  VAStep.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAStep.h"
#import "VAProcess.h"
#import "TDSqlManager.h"
#import "TDCommonLibs.h"

@implementation VAStep
NSString *dStepTable = @"step_table";
NSString *dProcessId = @"proc_id";
NSString *dStepId = @"step_id";
NSString *dStepName = @"step_name";

NSString *dbIsNoVideo = @"is_no_video";
NSString *dbIsUseMillisecond = @"use_milis";
NSString *diShiftNumber = @"shift_num";
NSString *dsOperationName = @"op_name";

-(id)init {
    self = [super init];
    if (self) {
        //
        _bIsNoVideo = YES;
        _bIsUseMillisecond = YES;
        self.aCircles = [NSMutableArray array];
    }
    return self;
}

-(void)dealloc {
    [_sStepName release];
    [super dealloc];
}

#pragma mark - static method
+(NSString*)getCreateTableString{
    NSString *query = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (\
                       %@ INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\
                       %@ INTEGER, %@ TEXT, %@ INTEGER, %@ INTEGER, %@ INTEGER,\
                       %@ TEXT)", dStepTable,dStepId,
                       dProcessId, dStepName, dbIsNoVideo, dbIsUseMillisecond, diShiftNumber, dsOperationName];
    TDLOG(@"query VAProject create = %@", query);
    return query;
}
+(NSMutableArray*)getListStep:(TDSqlManager*)manager process:(VAProcess*)process{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ where %@ = %d", dStepTable, dProcessId, process.iProcId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VAStep *step = [[[VAStep alloc] init] autorelease];
        step.iStepId = TDSqlInt(stmt, 0);
        step.parentProcess = process;
        step.sStepName = TDSqlText(stmt, 2);
        step.bIsNoVideo = TDSqlInt(stmt, 3);
        step.bIsUseMillisecond = TDSqlInt(stmt, 4);
        step.iShiftNumber = TDSqlInt(stmt, 5);
        step.sOperationName = TDSqlText(stmt, 6);
        
        [arr addObject:step];
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
                       (%@, %@, %@, %@, %@, %@) VALUES (?,?,?,?,?,?)",
                       dStepTable,
                       dProcessId, dStepName,dbIsNoVideo, dbIsUseMillisecond, diShiftNumber, dsOperationName];
    TDLOG(@"Query insert VAStep = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _parentProcess.iProcId);
    TDSqlBindText(stmt, 2, _sStepName);
    TDSqlBindInt(stmt, 3, _bIsNoVideo);
    TDSqlBindInt(stmt, 4, _bIsUseMillisecond);
    TDSqlBindInt(stmt, 5, _iShiftNumber);
    TDSqlBindText(stmt, 6, _sOperationName);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iStepId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ \
                       SET %@ =?, %@=?, %@=?, %@=?, \
                       %@=?, %@=? WHERE %@=?",
                       dStepTable,
                       dProcessId, dStepName, dbIsNoVideo, dbIsUseMillisecond,
                       diShiftNumber, dsOperationName, dStepId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindInt(stmt, 1, _parentProcess.iProcId);
    TDSqlBindText(stmt, 2, _sStepName);
    TDSqlBindInt(stmt, 3, _bIsNoVideo);
    TDSqlBindInt(stmt, 4, _bIsUseMillisecond);
    TDSqlBindInt(stmt, 5, _iShiftNumber);
    TDSqlBindText(stmt, 6, _sOperationName);
    TDSqlBindInt(stmt, 7, _iStepId);
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE from %@ WHERE %@=%d", dStepTable, dStepId, _iStepId];
    return [manager executeQuery:query];
}
-(void)getListCircle:(TDSqlManager *)manager{
    self.aCircles = [VACircle getListCircle:manager step:self];
}
-(int)getNextCircleCount{
    if (_aCircles == nil) {
        return 1;
    }else{
        return [[_aCircles lastObject] iCircleCounter];
    }
}
@end


@implementation VACircle
NSString *dCircleTable = @"circle_table";
NSString *diCircleId = @"cir_id";
NSString *diCircleCounter = @"cir_counter";
NSString *diparentStep = @"par_step_id";
NSString *dbIsNoVideoCir = @"is_novideo";
NSString *dbIsUseMilliCir = @"is_use_mili";
NSString *diShiftNumCir = @"i_shift_num";
NSString *dsOpNameCir = @"op_name";
NSString *dsAudioNote = @"audio_note";
NSString *dsVideo = @"video";
NSString *ddTimeCircle = @"timeCircle";
-(id)init {
    self = [super init];
    if (self) {
        //
        _bIsNoVideo = YES;
        _bIsUseMillisecond = YES;
        
    }
    return self;
}

-(void)dealloc {
    [_sOperationName release];
    [_sAudioNote release];
    [_sVideo release];
    [super dealloc];
}

#pragma mark - static method
+(NSString*)getCreateTableString{
    NSString *query = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (\
                       %@ INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,\
                       %@ INTEGER, %@ INTEGER, %@ INTEGER, %@ INTEGER,\
                       %@ INTEGER, %@ TEXT,  %@ TEXT,  %@ TEXT,\
                       %@ DOUBLE)",
                       dCircleTable, diCircleId,
                       diparentStep, diCircleCounter, dbIsNoVideoCir, dbIsUseMilliCir,
                       diShiftNumCir, dsOpNameCir, dsAudioNote, dsVideo, ddTimeCircle];
    TDLOG(@"query VAProject create = %@", query);
    return query;
}
+(NSMutableArray*)getListCircle:(TDSqlManager *)manager step:(VAStep *)step{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return nil;
    }
    NSString *query = [NSString stringWithFormat:@"SELECT * from %@ where %@ = %d", dCircleTable, diparentStep, step.iStepId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return nil;
    }
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:1];
    while (sqlite3_step(stmt)==SQLITE_ROW) {
        VACircle *circle = [[[VACircle alloc] init] autorelease];
        circle.iCircleId = TDSqlInt(stmt, 0);
        circle.parentStep = step;
        circle.iCircleCounter = TDSqlInt(stmt, 2);
        circle.bIsNoVideo = TDSqlInt(stmt, 3);
        circle.bIsUseMillisecond = TDSqlInt(stmt, 4);
        circle.iShiftNumber = TDSqlInt(stmt, 5);
        circle.sOperationName = TDSqlText(stmt, 6);
        circle.sAudioNote = TDSqlText(stmt, 7);
        circle.sVideo = TDSqlText(stmt, 8);
        circle.dTimeCircle = TDSqlDouble(stmt, 9);
        
        [arr addObject:step];
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
                       (%@, %@, %@, %@, %@, %@, %@, %@, %@) VALUES (?,?,?,?,?,?,?,?, ?)",
                       dCircleTable,
                       dStepId, diCircleCounter,dbIsNoVideoCir, dbIsUseMilliCir,
                       diShiftNumCir, dsOpNameCir, dsAudioNote, dsVideo,
                       ddTimeCircle];
    TDLOG(@"Query insert VAStep = %@", query);
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    TDSqlBindInt(stmt, 1, _parentStep.iStepId);
    TDSqlBindInt(stmt, 2, _iCircleCounter);
    TDSqlBindInt(stmt, 3, _bIsNoVideo);
    TDSqlBindInt(stmt, 4, _bIsUseMillisecond);
    TDSqlBindInt(stmt, 5, _iShiftNumber);
    TDSqlBindText(stmt, 6, _sOperationName);
    TDSqlBindText(stmt, 7, _sAudioNote);
    TDSqlBindText(stmt, 8, _sVideo);
    TDSqlBindDouble(stmt, 9, _dTimeCircle);
    
    if (sqlite3_step(stmt) != SQLITE_DONE) {
        sqlite3_finalize(stmt);
        NSAssert(0, @"Error updating table: %@", query);
        return NO;
    }else{
        sqlite3_finalize(stmt);
        int lastIndex = sqlite3_last_insert_rowid(db);
        self.iCircleId = lastIndex;
        return YES;
    }
}

-(BOOL)updateToDb:(TDSqlManager*)manager{
    sqlite3 *db = [manager getDatabase];
    if (db == NULL) {
        return NO;
    }
    NSString *query = [NSString stringWithFormat:@"UPDATE %@ \
                       SET %@ =?, %@=?, %@=?, %@=?, \
                       %@=?, %@=?, %@=?, %@=?\
                       %@=?\
                       WHERE %@=?",
                       dCircleTable,
                       dStepId, diCircleCounter,dbIsNoVideoCir, dbIsUseMilliCir,
                       diShiftNumCir, dsOpNameCir, dsAudioNote, dsVideo,
                       ddTimeCircle,
                       diCircleId];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(db, [query UTF8String], -1, &stmt, NULL) != SQLITE_OK) {
        return NO;
    }
    
    TDSqlBindInt(stmt, 1, _parentStep.iStepId);
    TDSqlBindInt(stmt, 2, _iCircleCounter);
    TDSqlBindInt(stmt, 3, _bIsNoVideo);
    TDSqlBindInt(stmt, 4, _bIsUseMillisecond);
    TDSqlBindInt(stmt, 5, _iShiftNumber);
    TDSqlBindText(stmt, 6, _sOperationName);
    TDSqlBindText(stmt, 7, _sAudioNote);
    TDSqlBindText(stmt, 8, _sVideo);
    TDSqlBindDouble(stmt, 9, _dTimeCircle);
    TDSqlBindInt(stmt, 10, _iCircleId);
    
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
    NSString *query = [NSString stringWithFormat:@"DELETE from %@ WHERE %@=%d", dCircleTable, diCircleId, _iCircleId];
    return [manager executeQuery:query];
}
@end