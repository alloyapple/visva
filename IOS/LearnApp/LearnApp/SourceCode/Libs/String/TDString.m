//
//  TDString.m
//  LearnApp
//
//  Created by tranduc on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDString.h"
@implementation NSString (Empty)
-(BOOL)isEmpty{
    return [self isEqualToString:@""];
}
-(BOOL)isNotEmpty{
    return ![self isEqualToString:@""];
}
@end

@implementation TDString

@end
