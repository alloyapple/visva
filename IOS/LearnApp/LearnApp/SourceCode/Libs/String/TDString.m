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

#pragma mark - regex
@implementation NSString(Regex)
-(BOOL)isFloatNumber{
    NSRegularExpression *regex = [[NSRegularExpression alloc]
                                  initWithPattern:
                                  @" *?[0-9]*\\.?[0-9]+ *" options:NSRegularExpressionCaseInsensitive error:nil];
    NSRange range = [regex rangeOfFirstMatchInString:self options:0 range:NSMakeRange(0,self.length)];
    if (range.location == 0 && range.length == self.length) {
        return YES;
    }
    return NO;
}
@end

@implementation TDString

@end
