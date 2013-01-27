//
//  VAGlobal.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAGlobal.h"
static VAGlobal* instance = nil;
@implementation VAGlobal
-(id)init{
    if ((self = [super init])) {
        instance = self;
        self.appSetting = [[[VASetting alloc] init] autorelease];
        self.user = [[[VAUser alloc] init] autorelease];
    }
    return self;
}
-(void)dealloc{
    instance = nil;
    [_appSetting release];
    [_user release];
    [super dealloc];
}
+(VAGlobal*)share{
    if (instance == nil) {
        instance = [[VAGlobal alloc] init];
    }
    return instance;
}
+(void)releaseShare{
    [instance release];
    instance = nil;
}

@end
