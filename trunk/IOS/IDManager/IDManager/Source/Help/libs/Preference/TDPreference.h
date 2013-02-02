//
//  TDPreference.h
//  IDManager
//
//  Created by tranduc on 1/30/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TDPreference : NSObject{
    
}
+(void)set:(id)value forkey:(NSString *)key;
+(id)getValue:(NSString*)key;
+(void)sync;
+(void)clearPreference;
@end
