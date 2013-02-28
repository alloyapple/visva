//
//  VAStep.h
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@class VAProcess;
@class TDSqlManager;
@interface VAStep : NSObject
@property (assign, nonatomic) int iStepId;
@property (retain, nonatomic) NSString *sStepName;
@property (nonatomic, assign) BOOL bIsNoVideo;
@property (nonatomic, assign) BOOL bIsUseMillisecond;
@property (nonatomic, assign) int iShiftNumber;
@property (nonatomic, retain) NSString *sOperationName;

//additional properties for OOP
@property (assign, nonatomic) VAProcess *parentProcess;
@property (nonatomic, retain) NSMutableArray *aCircles;

+(NSString*)getCreateTableString;
+(NSMutableArray*)getListStep:(TDSqlManager*)manager process:(VAProcess*)process;
-(BOOL)insertToDb:(TDSqlManager*)manager;
-(BOOL)deleteFromDb:(TDSqlManager*)manager;
-(void)getListCircle:(TDSqlManager *)manager;
-(int)getNextCircleCount;
@end

@interface VACircle : NSObject
@property (nonatomic, assign) int iCircleId;
@property (nonatomic, assign) int iCircleCounter;
@property (nonatomic, assign) VAStep *parentStep;
@property (nonatomic, assign) BOOL bIsNoVideo;
@property (nonatomic, assign) BOOL bIsUseMillisecond;
@property (nonatomic, assign) int iShiftNumber;

@property (nonatomic, retain) NSString *sOperationName;
@property (nonatomic, retain) NSString *sAudioNote;
@property (nonatomic, retain) NSString *sVideo;
@property (nonatomic, assign) double dTimeCircle;

+(NSString*)getCreateTableString;
+(NSMutableArray*)getListCircle:(TDSqlManager*)manager step:(VAStep*)step;
-(BOOL)insertToDb:(TDSqlManager*)manager;
-(BOOL)deleteFromDb:(TDSqlManager*)manager;

@end