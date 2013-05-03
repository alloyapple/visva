//
//  VASyncSettingViewController.m
//  IDManager
//
//  Created by tranduc on 3/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASyncSettingViewController.h"
#import <DropboxSDK/DropboxSDK.h>

#import "TDDatabase.h"
#import "VAGlobal.h"
#import "TDCommonLibs.h"
#import "TDSyn.h"
#import "TDGDrive.h"
#import "TDAlert.h"
#import "TDAppDelegate.h"

@interface VASyncSettingViewController ()
@property (retain, nonatomic) IBOutlet UIButton *btButton;
@property (retain, nonatomic) IBOutlet UILabel *lbName;
@property (retain, nonatomic) IBOutlet UIImageView *imIcon;
@property (retain, nonatomic) IBOutlet UILabel *lbInfo;

@property(nonatomic, assign)BOOL bIsAuthenning;
@property (retain, nonatomic) UIAlertView *loadingAlert;

-(void)updateType;
-(void)startAuthen;
@end

@implementation VASyncSettingViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)updateType{
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (_typeCloud == kTypeCloudDropbox) {
        _lbName.text = TDLocStrOne(@"Dropbox");
        _lbInfo.text = TDLocStrOne(@"DropboxInfo");
        if (appSetting.isUseDropboxSync && [self isLinkWithCloudAccount]) {
            [_btButton setTitle:TDLocStrOne(@"DropboxLoginCompleted") forState:UIControlStateNormal];
        }else{
            [_btButton setTitle:TDLocStrOne(@"StartUse") forState:UIControlStateNormal];
        }
        _imIcon.image = [UIImage imageNamed:@"dropboxLogo.png"];
        
    }else{
        _lbName.text = TDLocStrOne(@"GoogleDrive");
        _lbInfo.text = TDLocStrOne(@"GDriverInfo");
        if ( appSetting.isUseGoogleDriveSync && [self isLinkWithCloudAccount]) {
            [_btButton setTitle:TDLocStrOne(@"GDriverLogined") forState:UIControlStateNormal];
        }else{
            [_btButton setTitle:TDLocStrOne(@"StartUse") forState:UIControlStateNormal];
        }
        _imIcon.image = [UIImage imageNamed:@"GoogleDriveLogo.png"];
    }
    
}

-(void)startAuthen{
    
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (_typeCloud == kTypeCloudDropbox) {
        appSetting.isUseDropboxSync = YES;
        appSetting.isUseGoogleDriveSync = NO;
    }else{
        appSetting.isUseDropboxSync = NO;
        appSetting.isUseGoogleDriveSync = YES;
    }
    [appSetting saveSetting];
    
    if (_typeCloud == kTypeCloudDropbox) {
        _bIsAuthenning = YES;
        [[DBSession sharedSession] linkFromController:self];
    }else{
        _bIsAuthenning = YES;
        [[TDGDrive shareInstance] authorizedFrom:self];
    }
    
}

-(BOOL)isLinkWithCloudAccount{
    return [VASyncSettingViewController isLinkWithCloud:_typeCloud];
}
+(BOOL)isLinkWithCloud:(kTypeCloud)type{
    if (type == kTypeCloudDropbox) {
        return [[DBSession sharedSession] isLinked];
    }else if(kTypeCloudGDrive == type){
        return [[TDGDrive shareInstance] isAuthorized];
    }
    return NO;
}


#pragma mark authen
#define kTagWarningUsedCloud 101
-(void)showAuthenWarning{
    BOOL isShowWarning=NO;
    NSString *title = nil;
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (_typeCloud == kTypeCloudDropbox) {
        if (appSetting.isUseGoogleDriveSync && [VASyncSettingViewController isLinkWithCloud:kTypeCloudGDrive]) {
            isShowWarning = YES;
            title = TDLocStrOne(@"GDriverToDropbox");
        }
    }else{
        if (appSetting.isUseDropboxSync && [VASyncSettingViewController isLinkWithCloud:kTypeCloudDropbox]) {
            isShowWarning = YES;
            title = TDLocStrOne(@"DropboxToGDriver");
        }
    }
    if (isShowWarning) {
        [TDAlert showMessageWithTitle:title message:@"" delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagWarningUsedCloud];
    }else{
        if ([self isLinkWithCloudAccount]) {
            [self unlinkCloud];
            [self updateType];
        }else{
            [self startAuthen];
        }
        
    }
}

-(void)unlinkCloud
{
    if (_typeCloud == kTypeCloudDropbox) {
        //[VAGlobal share].appSetting.isUseDropboxSync = NO;
        [[DBSession sharedSession] unlinkAll];
    }else if (_typeCloud == kTypeCloudGDrive){
        //[VAGlobal share].appSetting.isUseGoogleDriveSync = NO;
        [[TDGDrive shareInstance] unlinkAll];
    }
}

-(void)acceptUnlinkOther{
    if (_typeCloud == kTypeCloudDropbox) {
        [[TDGDrive shareInstance] unlinkAll];
        
    }else{
        [[DBSession sharedSession] unlinkAll];
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
- (IBAction)btButtonPressed:(id)sender {
    [self showAuthenWarning];
}
- (IBAction)btBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self updateType];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidAppear:(BOOL)animated{
    if (_bIsAuthenning) {
        _bIsAuthenning = NO;
        [self updateType];
    }
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)dealloc {
    [_btButton release];
    [_lbName release];
    [_imIcon release];
    [_lbInfo release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setBtButton:nil];
    [self setLbName:nil];
    [self setImIcon:nil];
    [self setLbInfo:nil];
    [super viewDidUnload];
}
@end
