//
//  VAUser.h
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VAUser : NSObject
@property(nonatomic, assign)int iUserId;
@property(nonatomic, retain)NSString *sUserName;
@property(nonatomic, retain)NSString *sUserPassword;
@property(nonatomic, retain)NSMutableArray *aUserFolder;
@end
