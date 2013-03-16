//
//  VASetting.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASetting.h"
#import "TDPreference.h"
#import "TDAppDelegate.h"
#import "TDCommonLibs.h"
#import "TDString.h"

@implementation VASetting
-(id)init{
    if ((self = [super init])) {
        [self getSetting];
    }
    return self;
}
#define kisFirstUse @"isFirstUse"
#define kSecureOn @"isSecureOn"
#define kSecureDura @"secureDura"
#define kNumDestroyData @"numDestroy"
#define kCSVExport @"csvExport"
#define kUseDropBoxSync @"isUseDropboxSync"
#define kUseGoogleDriveSync @"isUseGDriveSync"
#define kGDriveLastSync @"GDriveLastSync"
#define kDropboxLastSync @"DropboxLastSync"
#define kIsUnlockHideIad @"isUnlockhideIad"
#define kIsUnlockLimitID @"isUnlockLimitId"

#define kMaxLimitID 12
-(void)getSetting{
    NSNumber *num;
    num = [TDPreference getValue:kisFirstUse];
    if (num) {
        self.isFirstUse = [num boolValue];
        self.isSecurityOn = [[TDPreference getValue:kSecureOn] boolValue];
        self.fSecurityDuration = [[TDPreference getValue:kSecureDura] floatValue];
        self.numBeforeDestroyData = [[TDPreference getValue:kNumDestroyData] intValue];
        self.isUnlockCSVExport = [[TDPreference getValue:kCSVExport] boolValue];
        self.isUseDropboxSync = [[TDPreference getValue:kUseDropBoxSync] boolValue];
        self.isUseGoogleDriveSync = [[TDPreference getValue:kUseGoogleDriveSync] boolValue];
        self.googleDriveLastSync = [TDPreference getValue:kGDriveLastSync];
        self.dropboxLastSync = [TDPreference getValue:kDropboxLastSync];
        self.isUnlockHideIad = [[TDPreference getValue:kIsUnlockHideIad] boolValue];
        self.isUnlockLimitId = [[TDPreference getValue:kIsUnlockLimitID] boolValue];
    }else{
        [self makeDefault];
    }
    _maxLimitId = kMaxLimitID;

}
-(void)makeDefault{
    self.isFirstUse = YES;
    self.isSecurityOn = YES;
    self.fSecurityDuration = 1;
    self.isUnlockCSVExport = YES;
    self.numBeforeDestroyData = -1;
    self.isUseDropboxSync = NO;
    self.isUseGoogleDriveSync = NO;
    self.googleDriveLastSync = nil;
    self.dropboxLastSync = nil;
    self.isUnlockHideIad = NO;
    self.isUnlockLimitId = NO;
    
}
-(void)saveSetting{
    [TDPreference set:[NSNumber numberWithBool:_isFirstUse] forkey:kisFirstUse];
    [TDPreference set:[NSNumber numberWithBool:_isSecurityOn] forkey:kSecureOn];
    [TDPreference set:[NSNumber numberWithFloat:_fSecurityDuration] forkey:kSecureDura];
    [TDPreference set:[NSNumber numberWithInt:_numBeforeDestroyData] forkey:kNumDestroyData];
    [TDPreference set:[NSNumber numberWithBool:_isUnlockCSVExport] forkey:kCSVExport];
    [TDPreference set:[NSNumber numberWithBool:_isUseDropboxSync] forkey:kUseDropBoxSync];
    [TDPreference set:[NSNumber numberWithBool:_isUseGoogleDriveSync] forkey:kUseGoogleDriveSync];
    
    [TDPreference set:TDNbool(_isUnlockHideIad) forkey:kIsUnlockHideIad];
    [TDPreference set:TDNbool(_isUnlockLimitId) forkey:kIsUnlockLimitID];
    
    if (_googleDriveLastSync) {
        [TDPreference set:_googleDriveLastSync forkey:kGDriveLastSync];
    }
    if (_dropboxLastSync) {
        [TDPreference set:_dropboxLastSync forkey:kDropboxLastSync];
    }
    [TDPreference sync];
}
-(void)updateSecurityTime{
    if(_isSecurityOn){
        [(TDApplicationIdle*)[UIApplication sharedApplication]
         setFIdleTime:_fSecurityDuration*60];
    }else{
        [(TDApplicationIdle*)[UIApplication sharedApplication] removeIdleCheck];
    }
}
-(BOOL)isDestroyDataEnable{
    return _numBeforeDestroyData != -1;
}
-(NSString*)stringDisplayNumBeforeDestroy{
    if (_numBeforeDestroyData == -1) {
        return TDLocStrOne(@"Off");
    }else{
        return [NSString stringWithFormat:@"%d", _numBeforeDestroyData];
    }
}
-(NSString *)stringDisplaySecurityTime{
    if (_isSecurityOn) {
        return [NSString stringWithFormat:@"%2.0f%@",
                _fSecurityDuration,
        TDLocStrOne(@"min")];
    }else{
        return TDLocStrOne(@"Off");
    }
}
@end
