//
//  TDString.h
//  LearnApp
//
//  Created by tranduc on 1/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface NSString (Empty)
-(BOOL)isEmpty;
-(BOOL)isNotEmpty;
@end

#pragma mark - regex
@interface NSString (Regex)
-(BOOL)isFloatNumber;
@end
@interface TDString : NSObject

@end

static inline NSString* TDiToS(int i){
    return [NSString stringWithFormat:@"%d", i];
}
static inline int TDStoi(NSString *s){
    return [s intValue];
}
static inline NSString *TDSnil(NSString *s){
    if (s != nil) {
        return s;
    }
    return @"";
}

static inline NSNumber *TDint(int i)
{
    return [NSNumber numberWithInt:i];
}
static inline NSNumber *TDbool(BOOL b)
{
    return [NSNumber numberWithBool:b];
}