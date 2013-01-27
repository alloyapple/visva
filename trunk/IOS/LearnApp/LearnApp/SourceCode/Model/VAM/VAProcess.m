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

@end
