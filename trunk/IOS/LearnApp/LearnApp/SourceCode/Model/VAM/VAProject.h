//
//  VAProject.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VAProject : NSObject
@property(nonatomic, assign)int iPrId;
@property(nonatomic, retain)NSString *sPrName;
@property(nonatomic, retain)NSString *sCompany;
@property(nonatomic, retain)NSString *sLocation;
@property(nonatomic, retain)NSString *sDescription;
@property(nonatomic, retain)NSString *sNote;
@property(nonatomic, retain)NSArray *arrProcess;
@end
