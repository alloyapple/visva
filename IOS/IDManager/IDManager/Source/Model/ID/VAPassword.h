//
//  VAPassword.h
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@class VAElementId;
@class TDSqlManager;
@class VAElementId;

@interface VAPassword : NSObject

@property(nonatomic, assign)int iPasswordId;
@property(nonatomic, assign)VAElementId *elementId;
@property(nonatomic, retain)NSString *sTitleNameId;
@property(nonatomic, retain)NSString *sPassword;
/*
 * Get list Group from database
 * @return: user Array if exists, nil if error
 */
+(NSMutableArray*)getListPass:(TDSqlManager*)manager element:(VAElementId*)element;
+(NSString*)getCreateTableQuery;
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
