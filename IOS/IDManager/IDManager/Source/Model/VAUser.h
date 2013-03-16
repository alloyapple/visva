//
//  VAUser.h
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#define kMaxId 13
#define kMaxNumElementID 12
@class VAGroup;
@class TDSqlManager;
@interface VAUser : NSObject
@property(nonatomic, assign)int iUserId;
//@property(nonatomic, retain)NSString *sUserName;
@property(nonatomic, retain)NSString *sUserPassword;
@property(nonatomic, retain)NSMutableArray *aUserFolder;
@property(nonatomic, retain)VAGroup *favoriteGroup;
@property(nonatomic, retain)VAGroup *recentGroup;
//@property(nonatomic, assign)int iDeleted;

@property(nonatomic, assign)BOOL bIsLoadFullData;


//+(VAUser*)userWithFullData:(TDSqlManager*)manager;
/*
 * add new group
 */
-(BOOL)addGroup:(NSString *)name database:(TDSqlManager*)manager;


+(NSString *)getCreateTableQuery;
+(NSString*)getDestroyQuery;

-(void)loadFavoriteAndRecentIDs;
-(void)loadFullData:(TDSqlManager*)manager;


/*
 * Get list user from database
 * @return: user Array if exists, nil if error
 */
+(NSMutableArray*)getListUser:(TDSqlManager*)manager;

-(BOOL)insertToDb:(TDSqlManager*)manager;

/*
 * Insert VAUser into database. Set iPrId to new inserted id.
 * @return: YES if complete, NO if error
 */
-(void)getListGroup:(TDSqlManager*)manager;

/*
 * Update VAUser into database.
 * @return: YES if complete, NO if error
 */
-(BOOL)updateToDb:(TDSqlManager*)manager;
/*
 * Delete VAUser from database.
 * @return: YES if complete, NO if error
 */
-(BOOL)deleteFromDb:(TDSqlManager*)manager;

/*
 * weak delete VAUser from database
 */
-(BOOL)weakDeleteFromDb:(TDSqlManager*)manager;
-(BOOL)updateGroupOrder:(TDSqlManager*)manager;
-(void)exportToCSV:(NSString*)path;
-(void)readCSV:(NSString*)path dbManager:(TDSqlManager*)mana error:(NSError**)error;
@end
