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

//additional properties for OOP
@property (assign, nonatomic) VAProcess *parentProcess;

+(NSString*)getCreateTableString;
+(NSMutableArray*)getListStep:(TDSqlManager*)manager process:(VAProcess*)process;
-(BOOL)insertToDb:(TDSqlManager*)manager;
-(BOOL)deleteFromDb:(TDSqlManager*)manager;

@end
