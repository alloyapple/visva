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
@synthesize Process_name;
@synthesize Project_name;
@synthesize Description;
@synthesize Notes;
@synthesize Total_cycle_time;
@synthesize Value_adding_time;
@synthesize Nonvalue_adding_time;
@synthesize Defect;
@synthesize Tot_operators;
@synthesize Shifts;
@synthesize Availability;
@synthesize Tot_distance_traveled;
@synthesize Uptime;
@synthesize Change_over_time;
@synthesize Takt_time;
@synthesize Version_id;
@synthesize Previous_process;
@synthesize Next_process;
@synthesize VSM_name;

@synthesize listStep;
@synthesize parentProject;

- (void)dealloc {
    [self.Project_name release];
    [self.Process_name release];
    [self.Description release];
    [self.Notes release];
    [self.Total_cycle_time release];
    [self.Value_adding_time release];
    [self.Nonvalue_adding_time release];
    [self.Defect release];
    [self.Tot_operators release];
    [self.Shifts release];
    [self.Availability release];
    [self.Tot_distance_traveled release];
    [self.Uptime release];
    [self.Change_over_time release];
    [self.Takt_time release];
    [self.Version_id release];
    [self.Previous_process release];
    [self.Next_process release];
    [self.VSM_name release];
    
    [self.listStep release];
    [super dealloc];
}



@end
