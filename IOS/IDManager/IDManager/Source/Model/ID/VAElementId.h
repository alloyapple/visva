//
//  VAElementId.h
//  IDManager
//
//  Created by tranduc on 2/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@class VAGroup;
@class TDSqlManager;
@interface VAElementId : NSObject

@property(nonatomic, assign) int iId;
@property(nonatomic, assign) VAGroup *group;
@property(nonatomic, retain) NSString *sTitle;
@property(nonatomic, retain) NSString *sEIcon;
@property(nonatomic, assign) double dTimeStamp;
//@property(nonatomic, assign) int iDeleted;
@property(nonatomic, assign) int iFavorite;
@property(nonatomic, assign) int iFlag;
//@property(nonatomic, assign) int iCountId;
@property(nonatomic, retain) NSString *sUrl;
@property(nonatomic, retain) NSString *sNote;
@property(nonatomic, retain) NSString *sImage;
@property(nonatomic, assign) int iOrder;

@property(nonatomic, retain) NSMutableArray *aPasswords;

/*
 * Get list Group from database
 * @return: user Array if exists, nil if error
 */
+(NSMutableArray*)getListElement:(TDSqlManager*)manager group:(VAGroup*)u;
+(NSString*)getCreateTableQuery;
+(NSString*)getDestroyQuery;

/*
 * Insert Group into database. Set iPrId to new inserted id.
 * @return: YES if complete, NO if error
 */
-(void)getListPassword:(TDSqlManager*)manager;

-(BOOL)insertToDb:(TDSqlManager*)manager;

/*
 * Update Group into database.
 * @return: YES if complete, NO if error
 */
-(BOOL)updateToDb:(TDSqlManager*)manager;
-(BOOL)updateOrderToDb:(TDSqlManager*)manager;
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
