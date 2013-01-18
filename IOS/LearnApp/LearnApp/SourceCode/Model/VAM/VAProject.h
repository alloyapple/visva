//
//  VAProject.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@class TDSqlManager;
@interface VAProject : NSObject
@property(nonatomic, assign)int iPrId;
@property(nonatomic, retain)NSString *sPrName;
@property(nonatomic, retain)NSString *sCompany;
@property(nonatomic, retain)NSString *sLocation;
@property(nonatomic, retain)NSString *sDescription;
@property(nonatomic, retain)NSString *sNote;
@property(nonatomic, retain)NSMutableArray *arrProcess; //List all process of project.

extern NSString *const dProjTable; // project table Name
extern NSString *const dProjtId; // Project Id field.

/*
 * Get string create Project table
 */
+(NSString*)getCreateProjTableString;
/*
 * Get list Projects from database
 * @return: Project Array if exists, nil if error
 */
+(NSMutableArray*)getListProject:(TDSqlManager*)manager;
/*
 * Insert VAProject into database. Set iPrId to new inserted id.
 * @return: YES if complete, NO if error
 */
-(BOOL)insertToDb:(TDSqlManager*)manager;

/*
 * Update VAProject into database.
 * @return: YES if complete, NO if error
 */
-(BOOL)updateToDb:(TDSqlManager*)manager;
/*
 * Delete VAProject from database.
 * @return: YES if complete, NO if error
 */
-(BOOL)deleteFromDb:(TDSqlManager*)manager;

/*
 * @return: Display name for project with company identify
 */
-(NSString *)getDisplayNameWithCompany;
@end
