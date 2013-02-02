//
//  TDViewController.m
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDViewController.h"
#import "VALoginController.h"
#import "VATermViewController.h"
#import "VAGlobal.h"
#import "VAGroup.h"
#import "VAUser.h"
#import "VAElementId.h"
#import "VAImgLabelCell.h"
#import "VAImageCell.h"
#import "TDCommonLibs.h"
#import "TDString.h"

@interface TDViewController ()
@property (nonatomic, assign) int iSelectedGroup;
@property (nonatomic, retain) VAGroup *selectedGroup;
@property (nonatomic, retain) VAUser *user;
@property (nonatomic, retain) IBOutlet UITextField *tfAlert;

@property (retain, nonatomic) IBOutlet UIButton *btEdit;
@property (retain, nonatomic) IBOutlet UITableView *tbGroup;
@property (retain, nonatomic) IBOutlet UITableView *tbId;


- (IBAction)btInfoPressed:(id)sender;
- (IBAction)btSettingPressed:(id)sender;
- (IBAction)btAddGroupPressed:(id)sender;

- (IBAction)btAddIdPressed:(id)sender;
- (IBAction)btEditPressed:(id)sender;
@end

@implementation TDViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	[self.navigationController setNavigationBarHidden:YES];
    
    _iSelectedGroup = 0;
    [self pushLogin];
}
-(void)pushLogin{
    VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
    [self.navigationController pushViewController:login animated:NO];
    
    VASetting *setting = [VAGlobal share].appSetting;
    if (setting.isFirstUse) {
        VATermViewController *vc = [[[VATermViewController alloc] initWithNibName:@"VATermViewController" bundle:nil] autorelease];
        login.typeMasterPass = kTypeMasterPasswordFirst;
        [login presentModalViewController:vc animated:NO];
    }else{
        login.typeMasterPass = kTypeMasterPasswordLogin;
    }
}

//called after login
-(void)reLoadData{
    self.user = [VAGlobal share].user;
    [_tbGroup reloadData];
    [self updateSelectedGroup];
}
-(void)selectGroup:(NSIndexPath*)indexPath{
    int row = indexPath.row;
    if (row == _iSelectedGroup) {
        return;
    }
   
    int lastSelect = _iSelectedGroup;
    _iSelectedGroup = row;
    
    UITableViewCell *cell1 = [_tbGroup cellForRowAtIndexPath:[NSIndexPath indexPathForRow:lastSelect inSection:indexPath.section]];
    UITableViewCell *cell2 = [_tbGroup cellForRowAtIndexPath:indexPath];
    
    [self setGroupSelected:cell1 row:lastSelect];
    [self setGroupSelected:cell2 row:row];
    [self updateSelectedGroup];
}
-(void)setGroupSelected:(UITableViewCell*)cell row:(int)row{
    if (row<_user.aUserFolder.count) {
        [(VAImgLabelCell *)cell setCellGroupSelected:(row == _iSelectedGroup)];
    }else{
        [(VAImageCell*)cell setSpecialCellGroupSelected:(row == _iSelectedGroup)];
    }
}
-(void)updateSelectedGroup{
    if (_iSelectedGroup < _user.aUserFolder.count) {
        _selectedGroup = [_user.aUserFolder objectAtIndex:_iSelectedGroup];
    }else if (_iSelectedGroup == _user.aUserFolder.count){
        _selectedGroup = _user.favoriteGroup;
    }else{
        _selectedGroup = _user.recentGroup;
    }
    [_tbId reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_btEdit release];
    [_tbGroup release];
    [_tbId release];
    [_selectedGroup release];
    [_tfAlert release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setBtEdit:nil];
    [self setTbGroup:nil];
    [self setTbId:nil];
    [super viewDidUnload];
}
#pragma mark - alert
#define kTagAlertAddGroup 101

-(void)showTextFileAlert:(NSString *)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"\n\n" delegate:self cancelButtonTitle:TDLocalizedStringOne(@"Cancel") otherButtonTitles:TDLocalizedStringOne(@"OK"), nil];
    _tfAlert.frame = CGRectMake(20, 50, 240, 35);
    _tfAlert.text = @"";
    [alert addSubview:_tfAlert];
    [_tfAlert becomeFirstResponder];
    alert.tag = tag;
    [alert show];
    
}
-(void)showAlert:(NSString*)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"" delegate:self cancelButtonTitle:TDLocalizedStringOne(@"Cancel") otherButtonTitles:TDLocalizedStringOne(@"OK"), nil];
    alert.tag = tag;
    [alert show];
}

#pragma mark - uialertview
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    switch (alertView.tag) {
        case kTagAlertAddGroup:
            if (buttonIndex == 0) {
                return;
            }else{
                [_tfAlert resignFirstResponder];
                [self addGroup:_tfAlert.text];
            }
            break;
            
        default:
            break;
    }
}

#pragma mark - button action
- (IBAction)btInfoPressed:(id)sender {
}

- (IBAction)btSettingPressed:(id)sender {
}


- (IBAction)btAddGroupPressed:(id)sender {
    [self showTextFileAlert:TDLocalizedStringOne(@"EnterGrName") tag:kTagAlertAddGroup];
}

- (IBAction)btAddIdPressed:(id)sender {
    if (_iSelectedGroup >= _user.aUserFolder.count) {
        TDLOGERROR(@"Not create in here");
    }else{
        VAElementId *element = [[[VAElementId alloc] init] autorelease];
        element.sTitle = @"element";
        element.sUrl = @"http://";
        element.sEIcon = [[NSBundle mainBundle] pathForResource:@"Thumb/JP_ISP/au.png" ofType:nil];
        element.group = _selectedGroup;
        [element insertToDb:[VAGlobal share].dbManager];
        [_selectedGroup.aElements addObject:element];
        [_tbId reloadData];
    }
}

- (IBAction)btEditPressed:(id)sender {
}

#pragma mark - edit db
-(void)addGroup:(NSString *)name{
    if ([name isNotEmpty]) {
        BOOL val = [_user addGroup:name database:[VAGlobal share].dbManager];
        if (val) {
            _iSelectedGroup = _user.aUserFolder.count-1;
            [self reLoadData];
        }else{
            [self showAlert:TDLocalizedStringOne(@"InsertGroupError") tag:0];
        }
    }else{
        [self showAlert:TDLocalizedStringOne(@"NameGrError") tag:0];
    }
}


#pragma mark - table view
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (_user.bIsLoadFullData) {
        if (tableView == _tbGroup) {
            return _user.aUserFolder.count +2;
        }else{
            return _selectedGroup.aElements.count;
        }
    }else{
        return 0;
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
#define kHeightOfListElementCell 80
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPat{
    int row = indexPat.row;
    if (tableView == _tbGroup) {
        UITableViewCell *cell = nil;
        if (row < _user.aUserFolder.count) {
            static NSString *normalCellGroup = @"VAImgLabelCell";
            cell = [tableView dequeueReusableCellWithIdentifier:normalCellGroup];
            if (cell == nil) {
                cell = [[[NSBundle mainBundle] loadNibNamed:normalCellGroup owner:self options:nil]objectAtIndex:0];
            }
            VAGroup *group = [_user.aUserFolder objectAtIndex:row];
            ((VAImgLabelCell*)cell).lbTitle.text = group.sGroupName;
            
        }else{
            static NSString *specialCellGroup = @"VAImageCell";
            cell = [tableView dequeueReusableCellWithIdentifier:specialCellGroup];
            if (cell == nil) {
                cell = [[[NSBundle mainBundle] loadNibNamed:specialCellGroup owner:self options:nil]objectAtIndex:0];
            }
            if (row == _user.aUserFolder.count) {
                ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"Favorite.png"];
            }else{
                ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"history.png"];
            }
        }
        [self setGroupSelected:cell row:row];
        return cell;
        
    }else{
        //_tb id
        return [self cellForElementTable:row];
    }
}
-(UITableViewCell*)cellForElementTable:(int)row{
    NSString *cellIdentifier = @"VAElementCell";
    UITableViewCell *cell = [_tbId dequeueReusableCellWithIdentifier:cellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellIdentifier];
        [cell.imageView setContentMode:UIViewContentModeScaleAspectFit];
        cell.textLabel.textColor = [UIColor whiteColor];
        cell.detailTextLabel.textColor = [UIColor whiteColor];
    }
    VAElementId *element = [_selectedGroup.aElements objectAtIndex:row];
    
    cell.backgroundView = [[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"item-bar.png"]] autorelease];
    [cell setSelectedBackgroundView:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"item-bar-select.png"]] autorelease]];
    
    cell.textLabel.text = element.sTitle;
    cell.detailTextLabel.text = element.sUrl;
    
    float offset = 5;
    float height = kHeightOfListElementCell - offset *2;
    UIImageView *imgView=[[UIImageView alloc] initWithFrame:CGRectMake(offset, offset, height, height)];
    imgView.backgroundColor=[UIColor clearColor];
    [cell.contentView addSubview:imgView];
    cell.indentationWidth = kHeightOfListElementCell;
    cell.indentationLevel = 1;
    if (element.sEIcon == nil) {
        imgView.image = [UIImage imageNamed:@"albumIcon.png"];
    }else{
        imgView.image = [UIImage imageWithContentsOfFile:element.sEIcon];
    }
    
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView == _tbGroup) {
        [self selectGroup:indexPath];
    }else{ //_tb id
        
        
    }
}

@end
