//
//  TDSyn.h
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DropboxSDK/DropboxSDK.h>
#import "TDGDrive.h"


typedef enum {
    TDSyncOptionNone = 0,
    TDSyncOptionStopIfCloudEmpty = 1,
    TDSyncOptionCopyIfCloudEmpty = 1<<1,
    TDSyncOptionReplaceIfCloudExits = 1<<2,
    TDSyncOptionOnlyLoadListFile = 1<<3
}TDSyncOption;

@protocol TDSyncDelegate;
@interface TDSync : NSObject<UIAlertViewDelegate>{
    BOOL _isSyning;
    BOOL _isFileCloudExits;
    id<TDSyncDelegate> _syncDelegate;
    int _syncOption;
}
@property(nonatomic, assign)int tagSync;
@property(nonatomic, retain)NSString *pathFromRoot;
@property(nonatomic, retain)NSString *cloudFileName;
@property(nonatomic, retain)NSString *deviceRootFolder;
@property(nonatomic, retain)NSString *cloudRootFolder;
@property(nonatomic, assign)int syncOption;
@property(nonatomic, readonly)BOOL isSyning;
@property(nonatomic, assign)id<TDSyncDelegate> syncDelegate;
@property(nonatomic, retain)NSArray *listFiles;
//@property(nonatomic, retain)NSArray *fileExtention;

-(id)initWithFile:(NSString*)path option:(int)syncOp;
-(void)startSync;
-(void)loadCloudModifyDate;
-(void)cloudLoadedModifyDate:(NSDate*)date;
-(void)copyToCloud;
-(void)copyFromCloud;
-(void)loadFile:(id)fileData;
@end

typedef enum {
    TDSyncErrorNotLinked,
    TDSyncErrorFileNotExist,
    TDSyncErrorWrongFileClass,
    TDSyncErrorNetwork,
    TDSyncErrorUserCancel
}TDSyncErrorCode;
typedef enum {
    TDSyncMethodToCloud,
    TDSyncMethodFromCloud,
    TDSyncMethodNone
}TDSyncMethod;
@protocol TDSyncDelegate <NSObject>
-(void)syncFinish:(TDSync*)sync method:(TDSyncMethod)method;
-(void)syncError:(TDSync*)sync errorCode:(TDSyncErrorCode)code;
@optional
-(void)syncLoadedFile:(TDSync*)sync;
@end

#pragma mark - TDSynDropbox
@interface TDSynsDropbox:TDSync

@end

@interface TDSynsGDrive:TDSync

@end




