//
//  VASyncChooseFile.m
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASyncChooseFile.h"
#import "TDSyn.h"
#import "TDCommonLibs.h"
#import "TDAppDelegate.h"
#import "TDAlert.h"
#import "VAGlobal.h"
#import "TDDatabase.h"
#define kLoadFileTemp @"temp.csv"
#define kTagLoadFileContent 102

@interface VASyncChooseFile ()
@property(nonatomic, retain)TDSync *synMachine;
@property(nonatomic, retain)NSArray *listFile;
@property(nonatomic, retain)NSString *fileNameImport;
@property(nonatomic, retain)UIAlertView *loadingAlertView;
@end

@implementation VASyncChooseFile

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
    [self startLoad];
    _lbTitle.text = TDLocStrOne(@"LoadTextData");
}




- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btBackPressed:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)dealloc {
    [_lbTitle release];
    [_tbListFile release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setLbTitle:nil];
    [self setTbListFile:nil];
    [super viewDidUnload];
}

#pragma mark - sync
-(void)startLoad{
    int option = TDSyncOptionOnlyLoadListFile;
    if (_typeCloud == kTypeCloudDropbox) {
        self.synMachine = [[TDSynsDropbox alloc] initWithFile:@"" option:option];
    }else{
        self.synMachine = [[TDSynsGDrive alloc] initWithFile:@"" option:option];
    }
    self.loadingAlertView = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading") delegate:self];
    self.synMachine.syncDelegate = self;
    [self.synMachine startSync];
}
-(void)syncLoadedFile:(TDSync *)sync{
    self.listFile = _synMachine.listFiles;
    [_tbListFile reloadData];
    [self.loadingAlertView dismiss];
}
-(void)syncError:(TDSync *)sync errorCode:(TDSyncErrorCode)code{
    TDLOG(@"SyncErrorCode = %d", code);
    [self.loadingAlertView dismiss];
    if (code != TDSyncErrorUserCancel) {
        //[TDAlert showMessageWithTitle:TDLocStrOne(@"LoadError") message:nil delegate:self];
        [self.navigationController popViewControllerAnimated:YES];
    }
    if (sync.tagSync == kTagLoadFileContent) {
        [TDDatabase deleteFile:[TDDatabase pathInDocument:_fileNameImport]];
    }
}
#define kTagLoadComplete 161
-(void)syncFinish:(TDSync *)sync method:(TDSyncMethod)method{
    [[VAGlobal share].user readCSV:[TDDatabase pathInDocument:_fileNameImport] dbManager:[VAGlobal share].dbManager error:NULL];
    
    [TDAlert showMessageWithTitle:TDLocStrOne(@"SyncFinish") message:nil delegate:nil];
    
    [TDDatabase deleteFile:[TDDatabase pathInDocument:_fileNameImport]];
    [self.loadingAlertView dismiss];
   
}


#pragma mark - tableview
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    int cout = _listFile.count;
    if (cout == 0) {
        return 1;
    }
    return cout;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *cellIdentify = @"CellChooseFile";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
    if (cell == nil){
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify] autorelease];
    }
    NSString *title = @"";
    if (_listFile.count == 0) {
        cell.textLabel.text = TDLocStrOne(@"NoResult");
        return cell;
    }
    if (_typeCloud == kTypeCloudDropbox) {
        DBMetadata *child = [_listFile objectAtIndex:indexPath.row];
        title = child.filename;
    }else{
        GTLDriveFile *child = [_listFile objectAtIndex:indexPath.row];
        title = child.title;
    }
    cell.textLabel.text = title;
    return cell;
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (_listFile.count == 0) {
        return;
    }
    int option = TDSyncOptionStopIfCloudEmpty;
    self.fileNameImport = kLoadFileTemp;
    self.loadingAlertView = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading") delegate:self];
    [TDDatabase createFile:[TDDatabase pathInDocument:self.fileNameImport]];
    TDSync *sync = nil;
    id child = [_listFile objectAtIndex:indexPath.row];
    if (_typeCloud == kTypeCloudDropbox) {
       sync = [[TDSynsDropbox alloc] initWithFile:_fileNameImport option:option];
        
    }else{
        sync = [[TDSynsGDrive alloc] initWithFile:_fileNameImport option:option];
    }
    sync.tagSync = kTagLoadFileContent;
    sync.syncDelegate = self;
    [sync loadFile:child];
}

@end
