//
//  TDPreference.m
//  IDManager
//
//  Created by tranduc on 1/30/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDPreference.h"
#import "TDDatabase.h"

static TDPreference *instance = nil;
@interface TDPreference()
@property(nonatomic, retain)NSMutableDictionary *dic;
@property(nonatomic, retain)NSString *fileName;
@end
@implementation TDPreference
-(id)init{
    if ((self = [super init])) {
        instance = self;
        self.fileName = @"preference.plist";
        self.dic = [NSMutableDictionary dictionaryWithContentsOfFile:[TDDatabase pathInDocument:_fileName]];
        if (_dic == nil) {
            self.dic = [[[NSMutableDictionary alloc] init] autorelease];
        }
    }
    return self;
}
-(void)dealloc{
    instance = nil;
    [_dic release];
    [_fileName release];
    [super dealloc];
}
-(void)synchronize{
    [self.dic writeToFile:[TDDatabase pathInDocument:_fileName] atomically:YES];
}
+(TDPreference*)share{
    if (instance) {
        return instance;
    }else{
        instance = [[self alloc] init];
        return instance;
    }
}
+(void)remove{
    //[instance synchronize];
    [instance release];
}
+(void)set:(id)value forkey:(NSString *)key{
    //[[NSUserDefaults standardUserDefaults] setObject:value forKey:key];
    [[TDPreference share].dic setObject:value forKey:key];
}
+(id)getValue:(NSString *)key{
    return [[TDPreference share].dic valueForKey:key];
}
+(void)sync{
    //[[NSUserDefaults standardUserDefaults] synchronize];
    [[TDPreference share] synchronize];
}
@end
