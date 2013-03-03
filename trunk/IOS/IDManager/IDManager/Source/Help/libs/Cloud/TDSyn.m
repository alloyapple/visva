//
//  TDSyn.m
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDSyn.h"
#import "TDCommonLibs.h"
#import "TDDatabase.h"

#import "VAGlobal.h"
#import "TDAlert.h"
#pragma mark - TDSyn
@implementation TDSync
@synthesize syncOption = _syncOption;

-(void)dealloc{
    [_listFiles release];
    [super dealloc];
}
-(id)initWithFile:(NSString*)path option:(int)syncOp{
    if (self = [super init]) {
        self.pathFromRoot = path;
        self.syncOption = syncOp;
        self.deviceRootFolder = [TDDatabase documentPath];
        self.cloudRootFolder = @"/";
        self.cloudFileName = self.pathFromRoot;
        _isFileCloudExits = NO;
        _isSyning = NO;
    }
    return self;
}
-(NSDate *)deviceModifyDate{
    NSString *fullPath = [_deviceRootFolder stringByAppendingPathComponent:_pathFromRoot];
    NSURL *urlFile = [[[NSURL alloc] initFileURLWithPath:fullPath] autorelease];
    NSDate *modifyDateDevice;
    NSError *error;
    [urlFile getResourceValue:&modifyDateDevice forKey:NSURLContentModificationDateKey error:&error];
    TDLOG(@"DbDevice modify date = %@", modifyDateDevice);
    return modifyDateDevice;
}
-(NSDate*)cloudModifyDate{
    return nil;
}
-(void)loadCloudModifyDate{
    
}
-(void)startSync{
    if (_isSyning) {
        return;
    }
    _isSyning = YES;
    [self loadCloudModifyDate];
}
#define kTagAlertCloudToDevice 142
#define kTagAlertDeviceToCloud 143
#define kTagAlertReplaceExitsFile 144
-(void)cloudLoadedModifyDate:(NSDate*)date{
    TDLOG(@"Cloud modify date: %@", date);
    if (!_isFileCloudExits || date == nil) { //file not exist in cloud
        if (_syncOption & TDSyncOptionStopIfCloudEmpty) {
            [_syncDelegate syncError:self errorCode:TDSyncErrorFileNotExist];
        }
        if (_syncOption & TDSyncOptionCopyIfCloudEmpty) {
            [self copyToCloud];
        }
        return;
    }
    if (_syncOption & TDSyncOptionReplaceIfCloudExits) {
        [TDAlert showMessageWithTitle:TDLocStrOne(@"WriteCloudDataSure") message:@"" delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagAlertReplaceExitsFile];
        return;
    }
    NSDate *deviceDate = [self deviceModifyDate];
    double length = [deviceDate timeIntervalSinceDate:date];
    if (length>0) { //device modified
        [TDAlert showMessageWithTitle:TDLocStrOne(@"WriteCloudDataSure") message:@"" delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagAlertDeviceToCloud];
    }else if (length<0){
        [TDAlert showMessageWithTitle:TDLocStrOne(@"ReadCloudDataSure") message:@"" delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagAlertCloudToDevice];
    }else{
        //they are the same
        [_syncDelegate syncFinish:self method:TDSyncMethodNone];
        //[TDAlert showMessageWithTitle:TDLocStrOne(@"FileAreSame") message:nil delegate:self];
    }
}
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    switch (alertView.tag) {
        case kTagAlertDeviceToCloud:
            if (buttonIndex == 0) {
                [self copyToCloud];
            }else{
                _isSyning = NO;
                [_syncDelegate syncError:self errorCode:TDSyncErrorUserCancel];
            }
            break;
        case kTagAlertCloudToDevice:
            if (buttonIndex == 0) {
                [self copyFromCloud];
            }else{
                _isSyning = NO;
                [_syncDelegate syncError:self errorCode:TDSyncErrorUserCancel];
            }
            break;
        case kTagAlertReplaceExitsFile:
            if (buttonIndex == 0) {
                [self copyToCloud];
            }else{
                _isSyning = NO;
                [_syncDelegate syncError:self errorCode:TDSyncErrorUserCancel];
            }
            break;
        default:
            break;
    }
}
-(void)copyToCloud{
    
}
-(void)copyFromCloud{
    
}
-(void)loadFile:(id)fileData{
    TDLOG(@"Not implemented");
}
@end


#pragma mark - TDSynDropbox
@interface TDSynsDropbox()<DBRestClientDelegate>{
    
}
@property(nonatomic, retain)DBRestClient *synRestClient;
@property(nonatomic, retain)DBMetadata *currentFile;
@end
@implementation TDSynsDropbox

-(void)loadCloudModifyDate{
    if (![DBSession sharedSession].isLinked) {
        _isSyning = NO;
        [_syncDelegate syncError:self errorCode:TDSyncErrorNotLinked];
        return;
    }
    [self.synRestClient loadMetadata:self.cloudRootFolder];
}
-(DBRestClient*)synRestClient{
    if (_synRestClient == nil) {
        _synRestClient = [[DBRestClient alloc] initWithSession:[DBSession sharedSession]];
        _synRestClient.delegate = self;
    }
    return _synRestClient;
}
-(NSString*)devicePath{
    return [self.deviceRootFolder stringByAppendingPathComponent:self.pathFromRoot];
}
-(NSString*)cloudPath{
    return [self.cloudRootFolder stringByAppendingPathComponent:self.cloudFileName];
}
-(void)copyToCloud{
    [_synRestClient uploadFile:self.cloudFileName toPath: self.cloudRootFolder  withParentRev:_currentFile.rev fromPath:[self devicePath]];
}
-(void)copyFromCloud{
    TDLOG(@"StartLoadFile=%@",_currentFile.path);
    [self.synRestClient loadFile:_currentFile.path intoPath:[self devicePath]];
}
-(void)loadFile:(id)fileData{
    if ([fileData isKindOfClass:[DBMetadata class]]) {
        self.currentFile = fileData;
        _isSyning = YES;
        [self copyFromCloud];
    }else{
        [_syncDelegate syncError:self errorCode:TDSyncErrorWrongFileClass];
    }
}
#pragma mark restDelegate
- (void)restClient:(DBRestClient*)client loadedMetadata:(DBMetadata*)metadata {
    if (_syncOption&TDSyncOptionOnlyLoadListFile) {
        self.listFiles = metadata.contents;
        if ([_syncDelegate respondsToSelector:@selector(syncLoadedFile:)]) {
            [_syncDelegate syncLoadedFile:self];
        }else{
            TDLOG(@"Sync only file but not implement method");
        }
        return;
    }
    for (DBMetadata* child in metadata.contents) {
        TDLOG(@"path = %@", child.path);
        if (!child.isDirectory && !child.isDeleted && [child.path isEqualToString:[self cloudPath]]) {
            NSDate *cloudModifyDate = child.lastModifiedDate;
            _isFileCloudExits = YES;
            self.currentFile = child;
            [self cloudLoadedModifyDate:cloudModifyDate];
            return;
        }
    }
    _isFileCloudExits = NO;
    [self cloudLoadedModifyDate:nil];
}

- (void)restClient:(DBRestClient*)client loadMetadataFailedWithError:(NSError*)error {
    TDLOG(@"restClient:loadMetadataFailedWithError: %@", error);
    _isSyning = NO;
    [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
}

- (void)restClient:(DBRestClient*)client uploadedFile:(NSString*)destPath from:(NSString*)srcPath
          metadata:(DBMetadata*)metadata{
    _isSyning = NO;
    [_syncDelegate syncFinish:self method:TDSyncMethodToCloud];
}
-(void)restClient:(DBRestClient *)client uploadFileFailedWithError:(NSError *)error{
    TDLOG(@"restClient:uploadFailedWithError: %@", error);
    _isSyning = NO;
    [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
}
-(void)restClient:(DBRestClient *)client loadFileFailedWithError:(NSError *)error{
    TDLOG(@"restClient:loadFileFailedWithError: %@", error);
    _isSyning = NO;
    [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
}
-(void)restClient:(DBRestClient *)client loadedFile:(NSString *)destPath
{
    _isSyning = NO;
    [_syncDelegate syncFinish:self method:TDSyncMethodFromCloud];
}

@end


#pragma mark - TDSynGDrive
@interface TDSynsGDrive()<DBRestClientDelegate>{
    
}
@property(nonatomic, retain)GTLDriveFile *currentFile;
@end
@implementation TDSynsGDrive
-(id)initWithFile:(NSString*)path option:(int)syncOp{
    if (self = [super initWithFile:path option:syncOp ]) {
        self.cloudRootFolder = @"IDManagerSync/";
    }
    return self;
}
-(void)loadCloudModifyDate{
    if (![[TDGDrive shareInstance] isAuthorized]) {
        _isSyning = NO;
        [_syncDelegate syncError:self errorCode:TDSyncErrorNotLinked];
        return;
    }
    GTLQueryDrive *query = [GTLQueryDrive queryForFilesList];
    [[TDGDrive shareService] executeQuery:query completionHandler:^(GTLServiceTicket *ticket, GTLDriveFileList *files, NSError *error) {
        if (error == nil) {
            
            if (_syncOption&TDSyncOptionOnlyLoadListFile) {
                self.listFiles = files.items;
                if ([_syncDelegate respondsToSelector:@selector(syncLoadedFile:)]) {
                    [_syncDelegate syncLoadedFile:self];
                }
                return;
            }
            
            for(GTLDriveFile *child in files.items){
                if([child.title isEqualToString:self.cloudFileName]){
                    NSDate *cloudModifyDate = child.modifiedDate.date;
                    _isFileCloudExits = YES;
                    self.currentFile = child;
                    [self cloudLoadedModifyDate:cloudModifyDate];
                    return ;
                }
                
            }
            _isFileCloudExits = NO;
            [self cloudLoadedModifyDate:nil];
        } else {
            TDLOG(@"An error occurred: %@", error);
            [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
        }
    }];
}

-(NSString*)devicePath{
    return [self.deviceRootFolder stringByAppendingPathComponent:self.pathFromRoot];
}
-(NSString*)cloudPath{
    return [self.cloudRootFolder stringByAppendingPathComponent:self.cloudFileName];
}
-(void)copyToCloud{
    GTLUploadParameters *uploadParameters = nil;
    uploadParameters =
    [GTLUploadParameters uploadParametersWithFileHandle:[NSFileHandle fileHandleForReadingAtPath:[self devicePath]] MIMEType:@"text/plain"];
    
    if (self.currentFile == nil) {
        self.currentFile = [[[GTLDriveFile alloc] init] autorelease];
    }
    self.currentFile.title = self.cloudFileName;
    
    GTLQueryDrive *query = nil;
    if (self.currentFile.identifier == nil || self.currentFile.identifier.length == 0) {
        // This is a new file, instantiate an insert query.
        query = [GTLQueryDrive queryForFilesInsertWithObject:self.currentFile
                                            uploadParameters:uploadParameters];
    } else {
        // This file already exists, instantiate an update query.
        query = [GTLQueryDrive queryForFilesUpdateWithObject:self.currentFile
                                                      fileId:self.currentFile.identifier
                                            uploadParameters:uploadParameters];
    }

    
    [[TDGDrive shareService] executeQuery:query completionHandler:^(GTLServiceTicket *ticket,
                                                              GTLDriveFile *updatedFile,
                                                              NSError *error) {
        if (error == nil) {
            self.currentFile = updatedFile;
             [_syncDelegate syncFinish:self method:TDSyncMethodToCloud];
        } else {
            TDLOG(@"An error occurred: %@", error);
            [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
        }
    }];
}
-(void)copyFromCloud{
    GTMHTTPFetcher *fetcher =
    [[TDGDrive shareService].fetcherService fetcherWithURLString:self.currentFile.downloadUrl];
    [fetcher beginFetchWithCompletionHandler:^(NSData *data, NSError *error) {
        if (error == nil) {
            [data writeToFile:[self devicePath] atomically:YES];
            [_syncDelegate syncFinish:self method:TDSyncMethodFromCloud];
        }else{
            TDLOG(@"An error occurred: %@", error);
            [_syncDelegate syncError:self errorCode:TDSyncErrorNetwork];
        }
    }];
}
-(void)loadFile:(id)fileData{
    if ([fileData isKindOfClass:[GTLDriveFile class]]) {
        self.currentFile = fileData;
        _isSyning = YES;
        [self copyFromCloud];
    }else{
        [_syncDelegate syncError:self errorCode:TDSyncErrorWrongFileClass];
    }
}
@end
