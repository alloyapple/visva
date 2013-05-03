//
//  VASettingViewController.m
//  IDManager
//
//  Created by tranduc on 2/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASettingViewController.h"
#import "TDCommonLibs.h"
#import "VAGlobal.h"
#import "TDDatabase.h"
#import "VADropboxViewController.h"
#import "TDAlert.h"
#import "TDSyn.h"
#import "TDString.h"
#import "VASyncChooseFile.h"
#import "TDAppDelegate.h"

@interface VASettingViewController ()<TDSyncDelegate>
@property (retain, nonatomic) IBOutlet UILabel *lbSettingTitle;
@property (retain, nonatomic) IBOutlet UITableView *tbSetting;
@property (nonatomic, retain) UIAlertView *loadingAlert;
@property (nonatomic, retain) NSString *exportPath;

- (IBAction)btBackPress:(id)sender;


@end

@implementation VASettingViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    _lbSettingTitle.text = TDLocStrOne(@"Setting");
}
-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [_tbSetting reloadData];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btBackPress:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
#pragma mark - tableview
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 5;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    if (section == 2) {
        return TDLocStrOne(@"Cloud");
    }else if (section == 4){
        return TDLocStrOne(@"Inapp purchase");
    }else{
        return nil;
    }
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    VASetting *setting = [VAGlobal share].appSetting;
    int section = indexPath.section;
    int row = indexPath.row;
    static NSString *cellIdentifier = @"CellSetting";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellIdentifier];
        
    }
    cell.imageView.image = nil;
    cell.detailTextLabel.text = @"";
    cell.textLabel.text = @"";
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    if (section == 0) {
        if (row == 0) {
            cell.textLabel.text = TDLocStrOne(@"ResetMasterPassword");
            
        }else if (row == 1){
            cell.textLabel.text = TDLocStrOne(@"SecurityMode");
            cell.detailTextLabel.text = [setting stringDisplaySecurityTime];
        }
    }else if (section == 1){
        cell.textLabel.text = TDLocStrOne(@"ConfigurationFail");
        cell.detailTextLabel.text = [setting stringDisplayNumBeforeDestroy];
    }else if (section == 2){
        if (row == 0) {
            cell.imageView.image = [UIImage imageNamed:@"dropboxLogo.png"];
            cell.textLabel.text = TDLocStrOne(@"Dropbox");
        }else if (row == 1){
            cell.imageView.image = [UIImage imageNamed:@"GoogleDriveLogo.png"];
            cell.textLabel.text = TDLocStrOne(@"GoogleDrive");
        }else{
//            NSString *cellLabel = TDLocStrOne(@"NoSync");
//            if (setting.isUseDropboxSync) {
//                cellLabel = TDLocStrOne(@"DropboxSyned");
//            }else{
//                cellLabel = TDLocStrOne(@"GDriverSyned");
//            }
//            cell.textLabel.text = cellLabel;
//            [cell setAccessoryType:UITableViewCellAccessoryNone];
            
            cell.textLabel.text = TDLocStrOne(@"LoadTextData");
        }
    }else if (section == 3){
        cell.textLabel.text = TDLocStrOne(@"Email");
    }else if (section == 4){
        if (row==0) {
            cell.textLabel.text = TDLocStrOne(@"UnlimitedID");
        }else if (row ==1){
            cell.textLabel.text = TDLocStrOne(@"AdCancellation");
        }else{
            cell.textLabel.text = TDLocStrOne(@"CSVExport");
        }
    }
    return cell;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    switch (section) {
        case 0:
            return 2;
            break;
        case 1:
            return 1;
            break;
        case 2:
            return 3;
            break;
        case 3:
            return 1;
            break;
        case 4:
            return 3;
            break;
        default:
            return 0;
            break;
    }
}
#define kTagPickerSecurityTime 123
#define kTagPickerSecurityCount 124
#define kValueDefaultDestroyDataOff -1
#define kFileExport @"idpass.csv"
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    int section = indexPath.section;
    int row = indexPath.row;
    if (section ==0) {
        if (row == 0) {
            [self changePassword];
        }else if (row == 1){
            [self showSettingTimeSecurity];
        }
    }else if (section == 1){
        
        [self showSettingNumLoginFail];
    }else if (section == 2){
        if (row == 0) {
            [self openSyncView:kTypeCloudDropbox];
        }else if (row == 1){
            [self openSyncView:kTypeCloudGDrive];
        }else{
            [self chooseImportCSV];
        }
    }else if (section == 3){
        [self changeEmail];
    }else if (section == 4){
        if (row == 0) {
            [self unlimitedID];
        }else if (row == 1){
            [self cancelAd];
        } else if (row == 2) {
            [self chooseExportCsv];
        }
    }
}
-(void)changeEmail{
    VAEmailViewController *vc = [[[VAEmailViewController alloc] initWithNibName:@"VAEmailViewController" bundle:nil] autorelease];
    vc.type = kTypeEmailVCChange;
    [self presentModalViewController:vc animated:YES];
}
#pragma mark - tag

#define kTagUnlockCSVExport 402
#define kTagEnterExportFileName 403
#define kTagSyncExportCsv 410

#define kDefaultExportFileName @"idpx.idp"
#define kTagUnlockUnlimitedId 420
#define kTagUnlockAds 421

-(void)unlimitedID
{
    if ([VAGlobal share].appSetting.isUnlockLimitId) {
        return;
    }
    [TDAlert showMessageWithTitle:TDLocStrOne(@"ConfirmInApp")
                          message:TDLocStrOne(@"InApp99")
                         delegate:self
                     cancelButton:TDLocStrOne(@"Approval")
                            other:TDLocStrOne(@"Cancel")
                              tag:kTagUnlockUnlimitedId];
    
}

-(void)cancelAd
{
    if ([VAGlobal share].appSetting.isUnlockHideIad) {
        return;
    }
    [TDAlert showMessageWithTitle:TDLocStrOne(@"ConfirmInApp")
                          message:TDLocStrOne(@"InApp499")
                         delegate:self
                     cancelButton:TDLocStrOne(@"Approval")
                            other:TDLocStrOne(@"Cancel")
                              tag:kTagUnlockAds];
}

-(void)openSyncView:(kTypeCloud)type{
    VASyncSettingViewController *vc = [[VASyncSettingViewController alloc] initWithNibName:@"VASyncSettingViewController" bundle:nil];
    vc.typeCloud = type;
    [self.navigationController pushViewController:vc animated:YES];
    [vc release];
}


-(void)chooseImportCSV{
    VASetting *setting = [VAGlobal share].appSetting;
    if (!((setting.isUseDropboxSync &&
                [VASyncSettingViewController isLinkWithCloud:kTypeCloudDropbox])
           ||(setting.isUseGoogleDriveSync &&
                [VASyncSettingViewController isLinkWithCloud:kTypeCloudGDrive]))) {
        [TDAlert showMessageWithTitle:TDLocStrOne(@"NoCloudSetup") message:nil delegate:self];
        return;
    }
    VASyncChooseFile *vc = [[[VASyncChooseFile alloc] initWithNibName:@"VASyncChooseFile" bundle:nil] autorelease];
    if (setting.isUseDropboxSync) {
        vc.typeCloud = kTypeCloudDropbox;
    }else{
        vc.typeCloud = kTypeCloudGDrive;
    }
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark -  exportCSV

-(void)chooseExportCsv{
    VASetting *setting = [VAGlobal share].appSetting;
    if (setting.isUnlockCSVExport) {
        
        if (!(setting.isUseDropboxSync || setting.isUseGoogleDriveSync)) {
            [TDAlert showMessageWithTitle:TDLocStrOne(@"NoCloudSetup") message:nil delegate:self];
            return;
        }
        self.exportPath = [TDDatabase pathInDocument:kFileExport];
        self.loadingAlert = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Exporting") delegate:self];
        [[VAGlobal share].user exportToCSV:_exportPath];
        TDAlertTextField *al = [TDAlert showTextFieldAlert:TDLocStrOne(@"FileName") delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagEnterExportFileName];
        al.textField.text = kDefaultExportFileName;
        [self.loadingAlert dismissWithClickedButtonIndex:0 animated:YES];
    }else{
        [TDAlert showMessageWithTitle:TDLocStrOne(@"ConfirmInApp")
                              message:TDLocStrOne(@"InAppCSV999")
                             delegate:self
                         cancelButton:TDLocStrOne(@"Approval")
                                other:TDLocStrOne(@"Cancel")
                                  tag:kTagUnlockCSVExport];
    }
}
-(void)deleteExportFile{
    [TDDatabase deleteFile:self.exportPath];
    self.exportPath = nil;
}
-(void)syncExportFileToCloud:(NSString*)fileName{
    self.loadingAlert = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading") delegate:self];
    VASetting *setting = [VAGlobal share].appSetting;
    int option = TDSyncMethodToCloud;
    TDSync *sync = nil;
    if (setting.isUseDropboxSync) {
        sync = [[TDSynsDropbox alloc] initWithFile:kFileExport option:option];
    }else{
        sync = [[TDSynsGDrive alloc] initWithFile:kFileExport option:option];
    }
    sync.syncDelegate = self;
    sync.cloudFileName = [fileName stringByAppendingString:@".csv"];
    sync.tagSync = kTagSyncExportCsv;
    [sync startSync];
}

#pragma mark - sync delegate
-(void)syncError:(TDSync *)sync errorCode:(TDSyncErrorCode)code{
    [self deleteExportFile];
    TDLOG(@"SyncError");
    if (code != TDSyncErrorUserCancel) {
        [TDAlert showMessageWithTitle:TDLocStrOne(@"SyncFail") message:nil delegate:self];
    }
    [self.loadingAlert dismissWithClickedButtonIndex:0 animated:YES];
}
-(void)syncFinish:(TDSync *)sync method:(TDSyncMethod)method{
    [self deleteExportFile];
    [TDAlert showMessageWithTitle:TDLocStrOne(@"SyncFinish") message:nil delegate:self];
    [self.loadingAlert dismissWithClickedButtonIndex:0 animated:YES];
    [[TDAppDelegate share].viewController reLoadData];
}

-(void)showSettingTimeSecurity{
    VASecurityModeViewController *controller = [[[VASecurityModeViewController alloc] initWithNibName:@"VASecurityModeViewController" bundle:nil] autorelease];
    controller.sideDelegate = self;
    controller.listSelection = [NSArray arrayWithObjects:@"Off", @"1", @"3", @"5", @"10",nil];
    
    int selection = 0;
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (appSetting.isSecurityOn) {
        float time = appSetting.fSecurityDuration;
        if (time == 1) {
            selection = 1;
        }else if (time == 3){
            selection = 2;
        }else if (time == 5){
            selection = 3;
        }else{
            selection = 4;
        }
    }
    controller.currentRow = selection;
    controller.iTag = kTagPickerSecurityTime;
   
    [self presentModalViewController:controller animated:YES];
     controller.lbTitle.text = TDLocStrOne(@"SecurityMode");
}
-(void)showSettingNumLoginFail{
    VASecurityModeViewController *controller = [[[VASecurityModeViewController alloc] initWithNibName:@"VASecurityModeViewController" bundle:nil] autorelease];
    controller.sideDelegate = self;
    NSMutableArray *array = [NSMutableArray arrayWithObject:@"off"];
    for (int i=0; i<99; i++) {
        [array addObject:[NSString stringWithFormat:@"%d", i+1]];
    }
    controller.listSelection = array;
    controller.iTag = kTagPickerSecurityCount;
    
    int selection = 0;
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (appSetting.numBeforeDestroyData >0) {
        selection = appSetting.numBeforeDestroyData;
    }
    controller.currentRow = selection;
    
    [self presentModalViewController:controller animated:YES];
    controller.lbTitle.text = TDLocStrOne(@"SecurityMode");
}

-(void)changePassword{
    VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
    
    login.loginDelegate = self;
    login.typeMasterPass = kTypeMasterPasswordChangePassCheck1;
    [self presentModalViewController:login animated:YES];
}
#pragma mark - unlock function

-(void)unlockCSVExport{
    [[TDAppDelegate share].viewController purchaseWithProductID:kID_CSV_EXPORT];
}
#pragma mark - alert

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (alertView.tag == kTagUnlockCSVExport) {
        switch (buttonIndex) {
            case 0:
                [self unlockCSVExport];
                break;
                
            default:
                break;
        }
    }else if (alertView.tag == kTagEnterExportFileName){
        if (buttonIndex == 0) {
            TDAlertTextField *al = (TDAlertTextField*)alertView;
            if ([al.textField.text isNotEmpty]) {
                [self syncExportFileToCloud:al.textField.text];
                
            }else{
                [TDAlert showMessageWithTitle:TDLocStrOne(@"InvalidFileName") message:nil delegate:self];
                [self deleteExportFile];
            }
            
        }else{
            [self deleteExportFile];
        }
    }else if (alertView.tag == kTagUnlockUnlimitedId){
        if (buttonIndex == 0 && ![VAGlobal share].appSetting.isUnlockLimitId) {
            [[TDAppDelegate share].viewController purchaseWithProductID:kUnlimitedId];
        }
    }else if (alertView.tag == kTagUnlockAds){
        if (buttonIndex == 0 && ![VAGlobal share].appSetting.isUnlockHideIad) {
            [[TDAppDelegate share].viewController purchaseWithProductID:kAdRemoveID];
        }
    }
}
#pragma mark - login delegate
-(void)loginViewDidLogin:(VALoginController *)vc{
    [[VAGlobal share].appSetting updateSecurityTime];
    [vc  dismissModalViewControllerAnimated:YES];
}
-(void)loginViewDidCancel:(VALoginController *)vc{
    [vc  dismissModalViewControllerAnimated:YES];
}
-(void)pickerDidDissmiss:(VASecurityModeViewController *)vc{
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (vc.iTag == kTagPickerSecurityTime) {
        int selection = vc.currentRow;
        if (selection == 0) {
            if ( appSetting.isSecurityOn) {
                [self showAlert:TDLocStrOne(@"SettingChange")];
                appSetting.isSecurityOn = NO;
                [appSetting updateSecurityTime];
            }else{
                //do nothing
            }
            
        }else{
            float val = [[vc.listSelection objectAtIndex:selection] floatValue];
            if (appSetting.isSecurityOn && val == appSetting.fSecurityDuration) {
                return;
            }
            [self showAlert:TDLocStrOne(@"SettingChange")];
            appSetting.isSecurityOn = YES;
            appSetting.fSecurityDuration = val;
            [appSetting updateSecurityTime];
        }
        [_tbSetting reloadData];
    }else{ //data protect
        int selection = vc.currentRow;
        int value = kValueDefaultDestroyDataOff;
        if (selection != 0) {
            value = [[vc.listSelection objectAtIndex:selection] intValue];
        }
        if (value == appSetting.numBeforeDestroyData) {
            return;
        }
        [self showAlert:TDLocStrOne(@"SettingChange")];
        appSetting.numBeforeDestroyData = value;
        [_tbSetting reloadData];
    }
    
    
}
-(void)showAlert:(NSString*)str{
    UIAlertView *al = [[[UIAlertView alloc] initWithTitle:str message:nil delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles:nil]autorelease];
    [al show];
}

- (void)dealloc {
    [_lbSettingTitle release];
    [_tbSetting release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setLbSettingTitle:nil];
    [self setTbSetting:nil];
    [super viewDidUnload];
}
@end
