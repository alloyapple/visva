//
//  VAProcess.h
//  LearnApp
//
//  Created by Tam Nguyen on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@class VAProject;
@interface VAProcess : NSObject

@property (retain, nonatomic) NSString *Project_name;
@property (retain, nonatomic) NSString *Process_name;
@property (retain, nonatomic) NSString *Description;
@property (retain, nonatomic) NSString *Notes;
@property (retain, nonatomic) NSDate *Total_cycle_time;
@property (retain, nonatomic) NSDate *Value_adding_time;
@property (retain, nonatomic) NSDate *Nonvalue_adding_time;
@property (retain, nonatomic) NSNumber *Defect;
@property (retain, nonatomic) NSNumber *Tot_operators;
@property (retain, nonatomic) NSNumber *Shifts;
@property (retain, nonatomic) NSNumber *Availability;
@property (retain, nonatomic) NSNumber *Tot_distance_traveled;
@property (retain, nonatomic) NSDate *Uptime;
@property (retain, nonatomic) NSDate *Change_over_time;
@property (retain, nonatomic) NSDate *Takt_time;
@property (retain, nonatomic) NSNumber *Version_id;
@property (retain, nonatomic) NSString *Previous_process;
@property (retain, nonatomic) NSString *Next_process;
@property (retain, nonatomic) NSString *VSM_name;


//additional properties
@property (retain, nonatomic) NSMutableArray *listStep;
@property (assign, nonatomic) VAProject *parentProject;

@end
