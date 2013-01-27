//
//  VAUser.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAUser.h"

@implementation VAUser
-(id)init{
    if ((self = [super init])) {
        self.aUserFolder = [NSMutableArray array];
    }
    return self;
}
-(void)dealloc{
    [_aUserFolder release];
    [_sUserName release];
    [_sUserPassword release];
    [super dealloc];
}
@end
