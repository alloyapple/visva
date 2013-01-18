//
//  VAGlobal.h
//  LearnApp
//
//  Created by tranduc on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TDSqlManager.h"

@interface VAGlobal : NSObject
@property(nonatomic, retain)TDSqlManager *dbManager;

/*
 * @return: VAGlobal object
 */
+(VAGlobal*)share;

/*
 * Release global object
 */
+(void)releaseGlobal;
@end
