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
