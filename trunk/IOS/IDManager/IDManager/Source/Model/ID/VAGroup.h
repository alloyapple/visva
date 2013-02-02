//
//  VAGroup.h
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@class VAUser;
@class TDSqlManager;
@interface VAGroup : NSObject
@property(nonatomic, assign) int iGroupId;
@property(nonatomic, retain) NSString *sGroupName;
@property(nonatomic, assign) int iGroupType;
@property(nonatomic, retain) NSString *sGroupIcon;
@property(nonatomic, assign) VAUser *user;
@property(nonatomic, assign) int iDeleted;
@property(nonatomic, retain)NSMutableArray *aElements;

/*
 * Get list Group from database
 * @return: user Array if exists, nil if error
 */
+(NSMutableArray*)getListGroup:(TDSqlManager*)manager user:(VAUser*)u;
+(NSString*)getCreateTableQuery;

-(void)getListElement:(TDSqlManager*)manager;
-(void)getListElementWithFullData:(TDSqlManager*)manager;

/*
 * Insert Group into database. Set iPrId to new inserted id.
 * @return: YES if complete, NO if error
 */
-(BOOL)insertToDb:(TDSqlManager*)manager;

/*
 * Update Group into database.
 * @return: YES if complete, NO if error
 */
-(BOOL)updateToDb:(TDSqlManager*)manager;
/*
 * Delete Group from database.
 * @return: YES if complete, NO if error
 */
-(BOOL)deleteFromDb:(TDSqlManager*)manager;

/*
 * weak delete Group from database
 */
-(BOOL)weakDeleteFromDb:(TDSqlManager*)manager;
@end
