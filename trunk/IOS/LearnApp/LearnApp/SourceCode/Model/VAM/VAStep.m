//
//  VAStep.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAStep.h"

@implementation VAStep

@synthesize Project_name;
@synthesize Process_name;
@synthesize Step_id;
@synthesize Step_no;
@synthesize Step_description;
@synthesize Version_id;
@synthesize Previous_vers_id;
@synthesize Video_file_name;
@synthesize Step_name;
@synthesize parentProcess;

-(id)init {
    self = [super init];
    if (self) {
        //
    }
    return self;
}

-(void)dealloc {
    [self.Project_name release];
    [self.Process_name release];
    [self.Step_id release];
    [self.Step_no release];
    [self.Step_description release];
    [self.Version_id release];
    [self.Previous_vers_id release];
    [self.Video_file_name release];
    [self.Step_name release];
    [super dealloc];
}
@end
