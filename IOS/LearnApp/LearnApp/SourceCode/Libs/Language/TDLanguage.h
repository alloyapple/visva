//
//  TDLanguage.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 * wrapper for NSLocalizedString.
 */
#define TDLocalizedString(string, comment) NSLocalizedString(string, comment)
#define TDLocalizedStringOne(string) NSLocalizedString(string, string)

@interface TDLanguage : NSObject

@end
