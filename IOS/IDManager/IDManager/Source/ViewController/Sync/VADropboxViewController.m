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
#import <QuartzCore/QuartzCore.h>

@interface VADropboxViewController ()<TDSyncDelegate>

@property (retain, nonatomic) UIAlertView *loadingAlert;

@property (retain, nonatomic) IBOutlet UILabel *lbTitle;


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
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    [self updateType];
    _btFromCloud.layer.cornerRadius = 3;
    _btToCloud.layer.cornerRadius = 3;
}

-(void)updateType{
    [_btUnlink setTitle:TDLocStrOne(@"UnifyNewData") forState:UIControlStateNormal];
    [_btToCloud setTitle:TDLocStrOne(@"iPhone") forState:UIControlStateNormal];
    [_btFromCloud setTitle:TDLocStrOne(@"iPhone") forState:UIControlStateNormal];
    
    
    
    if (_typeCloud == kTypeCloudDropbox) {
        _lbTitle.text = TDLocStrOne(@"Dropbox");
        _lbCloud.text = TDLocStrOne(@"DropboxSyned");
    }else{
        _lbTitle.text = TDLocStrOne(@"GoogleDrive");
        _lbCloud.text = TDLocStrOne(@"GDriverSyned");
        
    }
    [self updateTimeSync];
}
-(void)updateTimeSync{
    VASetting *setting = [VAGlobal share].appSetting;
    NSDateFormatter *dateFormat = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormat setDateFormat:@"dd/MM/yyyy"];
    
    if (_typeCloud == kTypeCloudDropbox) {
        if (setting.dropboxLastSync) {
            NSString *str = [dateFormat stringFromDate: setting.dropboxLastSync];
            _lbTime.text = [NSString stringWithFormat:@"%@ %@",TDLocStrOne(@"LastSync"),str];
        }else{
            _lbTime.text = [NSString stringWithFormat:@"%@ No",TDLocStrOne(@"LastSync")];
        }
        
    }else{
        if (setting.googleDriveLastSync) {
            NSString *str = [dateFormat stringFromDate: setting.googleDriveLastSync];
            _lbTime.text = [NSString stringWithFormat:@"%@ %@",TDLocStrOne(@"LastSync"),str];
        }else{
            _lbTime.text = [NSString stringWithFormat:@"%@ No",TDLocStrOne(@"LastSync")];
        }
    }
}

-(BOOL)isLinkWithCloudAccount{
    return [VASyncSettingViewController isLinkWithCloud:_typeCloud];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_vSync release];
    [_lbCloud release];
    [_lbTime release];
    [_btUnlink release];
    [_lbTitle release];
    [_btToCloud release];
    [_btFromCloud release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setVSync:nil];
    [self setLbCloud:nil];
    [self setLbTime:nil];
    [self setBtUnlink:nil];
    [self setLbTitle:nil];
    [self setBtToCloud:nil];
    [self setBtFromCloud:nil];
    [super viewDidUnload];
}


-(void)synctoCloud{
    int option = TDSyncOptionToCloud;
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
    int option = TDSyncOptionFromCloud;
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
    //choose newest
    int option = TDSyncOptionChooseNewest;
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






