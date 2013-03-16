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

@interface VAPassword : NSObject{
    //BOOL _isInDatabase;
}

@property(nonatomic, assign)int iPasswordId;
@property(nonatomic, assign)VAElementId *elementId;
@property(nonatomic, retain)NSString *sTitleNameId;
@property(nonatomic, retain)NSString *sPassword;
@property(nonatomic, assign)BOOL isInDatabase;

-(void)copyFrom:(VAPassword*)pass;
/*
 * Get list Group from database
 * @return: user Array if exists, nil if error
 */
+(NSMutableArray*)getListPass:(TDSqlManager*)manager element:(VAElementId*)element;
+(NSString*)getCreateTableQuery;
+(NSString*)getDestroyQuery;

-(BOOL)saveToDB:(TDSqlManager*)manager;
/*
 * Delete Group from database.
 * @return: YES if complete, NO if error
 */
-(BOOL)deleteFromDb:(TDSqlManager*)manager;
+(BOOL)didDeleteFromDb:(TDSqlManager*)manager elementId:(VAElementId*)ele;

@end
