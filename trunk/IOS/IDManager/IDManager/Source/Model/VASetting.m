//
//  VASetting.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASetting.h"
#import "TDPreference.h"

@implementation VASetting
-(id)init{
    if ((self = [super init])) {
        [self getSetting];
    }
    return self;
}
#define kisFirstUse @"isFirstUse"

-(void)getSetting{
    NSNumber *num;
    num = [TDPreference getValue:kisFirstUse];
    if (num) {
        self.isFirstUse = [num boolValue];
    }else{
        self.isFirstUse = YES;
    }
}
-(void)saveSetting{
    [TDPreference set:[NSNumber numberWithBool:_isFirstUse] forkey:kisFirstUse];
    
    [TDPreference sync];
}
@end
