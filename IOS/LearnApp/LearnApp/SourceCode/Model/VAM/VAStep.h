//
//  VAStep.h
//  LearnApp
//
//  Created by Tam Nguyen on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@class VAProcess;
@interface VAStep : NSObject

@property (retain, nonatomic) NSString *Project_name;
@property (retain, nonatomic) NSString *Process_name;
@property (retain, nonatomic) NSNumber *Step_id;
@property (retain, nonatomic) NSNumber *Step_no;
@property (retain, nonatomic) NSString *Step_description;
@property (retain, nonatomic) NSNumber *Version_id;
@property (retain, nonatomic) NSNumber *Previous_vers_id;
@property (retain, nonatomic) NSString *Video_file_name;

//additional
@property (retain, nonatomic) NSString *Step_name;

//additional properties for OOP
@property (assign, nonatomic) VAProcess *parentProcess;
@end
