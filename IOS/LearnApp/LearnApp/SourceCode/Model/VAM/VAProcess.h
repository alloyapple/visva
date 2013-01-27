//
//  VAProcess.h
//  LearnApp
//
//  Created by Tam Nguyen on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@class VAProject;
@class VAStep;
@interface VAProcess : NSObject
@property(nonatomic, assign)VAProject *parentProject;
@property(nonatomic, assign)int iProcId;
@property(nonatomic, retain)NSString *sProcName;
@property(nonatomic, assign)int iVersionId;
@property(nonatomic, retain)NSString *sStartPoint;
@property(nonatomic, retain)NSString *sEndPoint;

@property(nonatomic, retain)NSString *sProcDescription;
@property(nonatomic, retain)NSString *sProcDefectNote;
@property(nonatomic, assign)NSTimeInterval tUptime;
@property(nonatomic, assign)NSTimeInterval tValueAddingTime;
@property(nonatomic, assign)NSTimeInterval tNonValAddingTime;
@property(nonatomic, assign)float fDefect;
@property(nonatomic, assign)BOOL bNeedVerify;

@property(nonatomic, retain)NSMutableArray *aSteps;
-(void)addStep:(VAStep*)step;
@end
