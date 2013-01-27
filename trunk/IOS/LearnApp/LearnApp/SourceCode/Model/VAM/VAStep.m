//
//  VAStep.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAStep.h"
#import "VAProcess.h"
@implementation VAStep

-(id)init {
    self = [super init];
    if (self) {
        //
    }
    return self;
}

-(void)dealloc {
    [_sStepName release];
    [super dealloc];
}
@end
