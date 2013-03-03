//
//  VADropboxViewController.m
//  IDManager
//
//  Created by tranduc on 2/26/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VADropboxViewController.h"
#import <DropboxSDK/DropboxSDK.h>
#import "TDDatabase.h"
#import "VAGlobal.h"
#import "TDCommonLibs.h"
#import "TDSyn.h"
#import "TDGDrive.h"
#import "TDAlert.h"
#import "TDAppDelegate.h"

@interface VADropboxViewController ()<TDSyncDelegate>
//@property (nonatomic, retain) DBFilesystem *filesystem;
@property(nonatomic, assign)BOOL bIsAuthenning;

@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) UIAlertView *loadingAlert;

@end

@implementation VADropboxViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _typeCloud = kTypeCloudDropbox;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _bIsAuthenning = NO;
    [self updateType];
    [self updateSysStatus];
    
}

-(void)updateType{
    [_btUnlink setTitle:TDLocStrOne(@"UnLink") forState:UIControlStateNormal];
    [_btToCloud setTitle:TDLocStrOne(@"IphoneToCloud") forState:UIControlStateNormal];
    [_btFromCloud setTitle:TDLocStrOne(@"CloudToIphone") forState:UIControlStateNormal];
    
    if (_typeCloud == kTypeCloudDropbox) {
        _lbTitle.text = TDLocStrOne(@"Dropbox");
        _lbInfo.text = TDLocStrOne(@"DropboxInfo");
        _lbCloud.text = TDLocStrOne(@"DropboxSyned");
    }else{
        _lbTitle.text = TDLocStrOne(@"GoogleDrive");
        _lbInfo.text = TDLocStrOne(@"GDriverInfo");
        _lbCloud.text = TDLocStrOne(@"GDriverSyned");

    }
    [self updateTimeSync];
}
-(void)updateTimeSync{
    VASetting *setting = [VAGlobal share].appSetting;
    NSDateFormatter *dateFormat = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormat setDateFormat:@"dd/MM/yyyy - HH:mm"];
    
    if (_typeCloud == kTypeCloudDropbox) {
        if (setting.dropboxLastSync) {
            NSString *str = [dateFormat stringFromDate: setting.dropboxLastSync];
            _lbTime.text = [NSString stringWithFormat:@"%@: %@",TDLocStrOne(@"LastSync"),str];
        }else{
            _lbTime.text = TDLocStrOne(@"FirstSync");
        }
        
    }else{
        if (setting.googleDriveLastSync) {
            NSString *str = [dateFormat stringFromDate: setting.googleDriveLastSync];
            _lbTime.text = [NSString stringWithFormat:@"%@: %@",TDLocStrOne(@"LastSync"),str];
        }else{
            _lbTime.text = TDLocStrOne(@"FirstSync");
        }
    }
}
-(void)viewWillAppear:(BOOL)animated{
    if (_bIsAuthenning) {
        _bIsAuthenning = NO;
        [self updateSysStatus];
    }
}
-(BOOL)isLinkWithCloudAccount{
    if (_typeCloud == kTypeCloudDropbox) {
        return [[DBSession sharedSession] isLinked];
    }else{
        return [[TDGDrive shareInstance] isAuthorized];
    }
}
-(void)startAuthen{
    
    if (_typeCloud == kTypeCloudDropbox) {
        _bIsAuthenning = YES;
        [[DBSession sharedSession] linkFromController:self];
    }else{
        _bIsAuthenning = YES;
        [[TDGDrive shareInstance] authorizedFrom:self];
    }
}

-(void)unlink{
    if (_typeCloud == kTypeCloudDropbox) {
        _bIsAuthenning = NO;
        [[DBSession sharedSession] unlinkAll];
        [self updateSysStatus];
    }else{
        _bIsAuthenning = NO;
        [[TDGDrive shareInstance] unlinkAll];
        [self updateSysStatus];
    }
}
-(void)updateSysStatus{
    VASetting *appSetting = [VAGlobal share].appSetting;
    if ([self isLinkWithCloudAccount]) {
        //dropbox linked
        _vNotSync.hidden = YES;
        _vSync.hidden = NO;
        
        if (_typeCloud == kTypeCloudDropbox) {
            appSetting.isUseDropboxSync = YES;
        }else{
            appSetting.isUseGoogleDriveSync = YES;
        }
        [appSetting saveSetting];
        [self synctoCloud];
    }else{
        //dropbox is not linked
        _vNotSync.hidden = NO;
        _vSync.hidden = YES;
        if (_typeCloud == kTypeCloudDropbox) {
            appSetting.isUseDropboxSync = NO;
        }else{
            appSetting.isUseGoogleDriveSync = NO;
        }
        [appSetting saveSetting];
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_vNotSync release];
    [_vSync release];
    [_lbInfo release];
    [_lbCloud release];
    [_lbTime release];
    [_btUnlink release];
    [_lbTitle release];
    [_btToCloud release];
    [_btFromCloud release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setVNotSync:nil];
    [self setVSync:nil];
    [self setLbInfo:nil];
    [self setLbCloud:nil];
    [self setLbTime:nil];
    [self setBtUnlink:nil];
    [self setLbTitle:nil];
    [self setBtToCloud:nil];
    [self setBtFromCloud:nil];
    [super viewDidUnload];
}
#define kTagWarningUsedCloud 101
-(void)showAuthenWarning{
    BOOL isShowWarning=YES;
    NSString *title = nil;
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (_typeCloud == kTypeCloudDropbox) {
        if (appSetting.isUseGoogleDriveSync) {
            isShowWarning = YES;
            title = TDLocStrOne(@"GDriverToDropbox");
        }
    }else{
        if (appSetting.isUseDropboxSync) {
            isShowWarning = YES;
            title = TDLocStrOne(@"DropboxToGDriver");
        }
    }
    if (isShowWarning) {
        [TDAlert showMessageWithTitle:title message:@"" delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagWarningUsedCloud];
    }else{
        [self startAuthen];
    }
}
-(void)acceptUnlinkOther{
    VASetting *setting = [VAGlobal share].appSetting;
    if (_typeCloud == kTypeCloudDropbox) {
        [[TDGDrive shareInstance] unlinkAll];
        setting.isUseGoogleDriveSync = NO;
        
    }else{
        [[DBSession sharedSession] unlinkAll];
        setting.isUseDropboxSync = NO;
    }
}
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == kTagWarningUsedCloud) {
        switch (buttonIndex) {
            case 0:
                [self acceptUnlinkOther];
                [self startAuthen];
                break;
                
            default:
                break;
        }
    }
}
- (IBAction)btStartSyncPressed:(id)sender {
    [self showAuthenWarning];
}

-(void)synctoCloud{
    int option = TDSyncOptionCopyIfCloudEmpty;
    TDSync *syn = nil;
    if (_typeCloud == kTypeCloudDropbox) {
        syn = [[TDSynsDropbox alloc] initWithFile:[VAGlobal share].dbFileName option:option];
    }else{
        syn = [[TDSynsGDrive alloc] initWithFile:[VAGlobal share].dbFileName option:option];
    }
    syn.syncDelegate = self;
    self.loadingAlert = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading") delegate:self];
    [syn startSync];
}
- (IBAction)btDeviceToClound:(id)sender {
    [self synctoCloud];
}

- (IBAction)btCloundToDevice:(id)sender {
    int option = TDSyncOptionStopIfCloudEmpty;
    TDSync *syn = nil;
    if (_typeCloud == kTypeCloudDropbox) {
        syn = [[TDSynsDropbox alloc] initWithFile:[VAGlobal share].dbFileName option:option];
    }else{
        syn = [[TDSynsGDrive alloc] initWithFile:[VAGlobal share].dbFileName option:option];
    }
    syn.syncDelegate = self;
    self.loadingAlert = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading") delegate:self];
    [syn startSync];
}

- (IBAction)btBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btUnlinkPressed:(id)sender {
    [self unlink];
}

-(void)syncFinish:(TDSync*)sync method:(TDSyncMethod)method{
    TDLOG(@"Complete");
    if (method == TDSyncMethodFromCloud) {
        [[VAGlobal share] reloadUserData];
        [[TDAppDelegate share].viewController reLoadData];
    }
    VASetting *setting = [VAGlobal share].appSetting;
    NSDate *current = [NSDate date];
    if (_typeCloud == kTypeCloudDropbox) {
        setting.dropboxLastSync = current;
    }else{
        setting.googleDriveLastSync = current;
    }
    [self.loadingAlert dismiss];
    [self updateTimeSync];
    [TDAlert showMessageWithTitle:TDLocStrOne(@"SyncFinish") message:nil delegate:self];
}
-(void)syncError:(TDSync*)sync errorCode:(TDSyncErrorCode)code{
    TDLOG(@"Error");
    [self.loadingAlert dismissWithClickedButtonIndex:0 animated:YES];
    if (code == TDSyncErrorUserCancel) {
        return;
    }
    [TDAlert showMessageWithTitle:TDLocStrOne(@"SyncFail") message:nil delegate:self];
}
@end






