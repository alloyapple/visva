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
#import "TDAppDelegate.h"
#import "VAPassword.h"
#import "VASetting.h"
#import "VASettingViewController.h"
#import "TDIdleWindow.h"
#import "TDImageEncrypt.h"
#import "TDAlert.h"
#import "TDPreference.h"
#import "VADropboxViewController.h"
#import "ASPurchaseView.h"
#import "VAElementOptionController.h"


@interface TDViewController ()
@property (nonatomic, assign) int iSelectedGroup;
@property (nonatomic, retain) VAGroup *selectedGroup;
@property (nonatomic, retain) VAUser *user;
@property (nonatomic, retain) VAGroup *searchGroup;


@property (nonatomic, retain) UIView *cellParent;
@property (nonatomic, assign) CGRect cellRect;
@property (nonatomic, retain) VAElementId *selectedElement;

@property (nonatomic, retain) IBOutlet UITextField *tfAlert;
@property (retain, nonatomic) IBOutlet UIButton *btEdit;
@property (retain, nonatomic) IBOutlet UITableView *tbGroup;
@property (retain, nonatomic) IBOutlet UITableView *tbId;
@property (retain, nonatomic) IBOutlet UISearchBar *sbSearchBar;

@property(nonatomic, retain)ASPurchaseView *purchaseView;
@property(nonatomic, assign)BOOL isShowLoginWindow;


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
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
	[self.navigationController setNavigationBarHidden:YES];
    _isSearching = NO;
    _isEditting = NO;
    _iSelectedGroup = 0;
    self.searchGroup = [[[VAGroup alloc] init] autorelease];
    [self pushLogin];
    [self initInappPurchase];
    
}

-(void)pushLogin{
    VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
    [self.navigationController pushViewController:login animated:NO];
    self.isShowLoginWindow = YES;
    login.loginDelegate = self;
    VASetting *setting = [VAGlobal share].appSetting;
    if (setting.isFirstUse) {
        VATermViewController *vc = [[[VATermViewController alloc] initWithNibName:@"VATermViewController" bundle:nil] autorelease];
        login.typeMasterPass = kTypeMasterPasswordFirst;
        [login presentModalViewController:vc animated:NO];
        
    }else if(setting.isCreatePassword){
        login.typeMasterPass = kTypeMasterPasswordLogin;
    }else{
        login.typeMasterPass = kTypeMasterPasswordFirst;
    }
}


#pragma mark - loginDelegate
#define kLastPasteboard @"kLastPasteboard"
-(void)loginViewDidLogin:(VALoginController *)vc{
    if (vc.typeMasterPass == kTypeMasterPasswordFirst ||
        vc.typeMasterPass == kTypeMasterPasswordLogin) {
        [vc.navigationController popViewControllerAnimated:YES];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(idleTimeRaise:) name:TDIdleNotification object:nil];
        [VAGlobal share].appSetting.isCreatePassword = YES;
        [[VAGlobal share].appSetting saveSetting];
        [[VAGlobal share].appSetting updateSecurityTime];
        
        if (vc.typeMasterPass == kTypeMasterPasswordFirst) {
            VAEmailViewController *vc = [[[VAEmailViewController alloc] initWithNibName:@"VAEmailViewController" bundle:nil] autorelease];
            vc.type = kTypeEmailVCRegister;
            [self presentModalViewController:vc animated:YES];
        }
        self.isShowLoginWindow = NO;
    }else if (vc.typeMasterPass == kTypeMasterPasswordReLogin){
        [vc dismissModalViewControllerAnimated:YES];
        [[VAGlobal share].appSetting updateSecurityTime];
        
        NSString *str = [TDPreference getValue:kLastPasteboard];
        if ([str isNotEmpty]) {
            [[UIPasteboard generalPasteboard] setString:str];
        }
        self.isShowLoginWindow = NO;
    }
    
}
-(void)showReloginWindow{
    if ([VAGlobal share].appSetting.isSecurityOn) {
        if (self.isShowLoginWindow) {
            return;
        }
        self.isShowLoginWindow = YES;
        VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
        
        login.loginDelegate = self;
        login.typeMasterPass = kTypeMasterPasswordReLogin;
        [[TDAppDelegate share].window.rootViewController presentModalViewController:login animated:YES];
    }else{
        TDLOGERROR(@"Error security off but idle is running");
    }
}

-(void)idleTimeRaise:(NSNotification*)notifi{
    NSString *str = [UIPasteboard generalPasteboard].string;
    if (str) {
        [[UIPasteboard generalPasteboard] setString:@""];
        [TDPreference set:str forkey:kLastPasteboard];
    }
    [self showReloginWindow];
}
//called after login
-(void)reLoadData{
    self.user = [VAGlobal share].user;
    [_tbGroup reloadData];
    [self updateSelectedGroup];
}
#define kTagDestroyData 3980
-(void)destroyData{
    UIAlertView *al = [[UIAlertView alloc] initWithTitle:TDLocStrOne(@"Destroying...") message:nil delegate:self cancelButtonTitle:nil otherButtonTitles: nil];
    [al show];
    
    [[VAGlobal share] destroyData];
    
    self.user = nil;
    [_tbGroup reloadData];
    _iSelectedGroup = 0;
    [self updateSelectedGroup];
    
    [self dismissModalViewControllerAnimated:NO];
    
    [self.navigationController popToRootViewControllerAnimated:NO];
    
    [self pushLogin];
    
    [al dismissWithClickedButtonIndex:0 animated:YES];
    [TDAlert showMessageWithTitle:TDLocStrOne(@"DataDestroyed") message:nil delegate:self otherButton:nil tag:kTagDestroyData];
    
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
        self.selectedGroup = [_user.aUserFolder objectAtIndex:_iSelectedGroup];
        if (_isSearching) {
            _isSearching = NO;
            [self reLoadData];
        }
    }
    else if (_isSearching){
        if (_iSelectedGroup == _user.aUserFolder.count){
            self.selectedGroup = self.searchGroup;
        }else if (_iSelectedGroup == _user.aUserFolder.count+1){
            self.selectedGroup = _user.favoriteGroup;
            _isSearching = NO;
            [self reLoadData];
        }else{
            self.selectedGroup = _user.recentGroup;
            _isSearching = NO;
            [self reLoadData];
        }
    }else
    {
        if (_iSelectedGroup == _user.aUserFolder.count){
            self.selectedGroup = _user.favoriteGroup;
        }else{
            self.selectedGroup = _user.recentGroup;
        }
    }
    [_tbId reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_searchGroup release];
    [_btEdit release];
    [_tbGroup release];
    [_tbId release];
    [_selectedGroup release];
    [_tfAlert release];
    [_sbSearchBar release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setBtEdit:nil];
    [self setTbGroup:nil];
    [self setTbId:nil];
    [self setSbSearchBar:nil];
    [super viewDidUnload];
}
#pragma mark - alert
#define kTagAlertAddGroup 101
#define kTagAlertChangeGroupName 102

-(void)showTextFileAlert:(NSString *)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"\n\n" delegate:self cancelButtonTitle:TDLocStrOne(@"OK")otherButtonTitles:TDLocStrOne(@"Cancel"), nil];
    _tfAlert.frame = CGRectMake(20, 50, 240, 35);
    _tfAlert.text = @"";
    [alert addSubview:_tfAlert];
    [_tfAlert becomeFirstResponder];
    alert.tag = tag;
    [alert show];
    
}
-(void)showAlert:(NSString*)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"" delegate:self cancelButtonTitle:TDLocStrOne(@"Cancel") otherButtonTitles:TDLocStrOne(@"OK"), nil];
    alert.tag = tag;
    [alert show];
}

#pragma mark - uialertview
#define kTagAlertAddID 261
#define kTagAlertAddIDLimitReach 262
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == kTagAlertAddGroup) {
        if (buttonIndex == 0) {
            [_tfAlert resignFirstResponder];
            [self addGroup:_tfAlert.text];
        }else{
            
        }
    }else if (alertView.tag == kTagAlertChangeGroupName){
        if (buttonIndex == 0) {
            [self changeCurrentGroupName:_tfAlert.text];
        }
        
        [_tfAlert resignFirstResponder];
        
    }else if (alertView.tag == kTagAlertAddID){
        [self addElementID];
    }else if (alertView.tag == kTagDestroyData){
        exit(0);
    }
    
}

#pragma mark - button action
#define kUrlHome @"http://www.japanappstudio.com/home.html"
- (IBAction)btInfoPressed:(id)sender {
    TDWebViewController *web  = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil] autorelease];
    web.sUrlStart = kUrlHome;
    web.webDelegate = self;
    [self.navigationController pushViewController:web animated:YES];
}

- (IBAction)btSettingPressed:(id)sender {
    VASettingViewController *setting = [[VASettingViewController alloc] initWithNibName:@"VASettingViewController" bundle:nil];
    [self.navigationController pushViewController:setting animated:YES];
}
- (IBAction)btSyncPressed:(id)sender {
    VASetting *appSetting = [VAGlobal share].appSetting;
    if (appSetting.isUseDropboxSync && [VASyncSettingViewController isLinkWithCloud:kTypeCloudDropbox]) {
        VADropboxViewController *vc = [[VADropboxViewController alloc] initWithNibName:@"VADropboxViewController" bundle:nil];
        vc.typeCloud = kTypeCloudDropbox;
        [self.navigationController pushViewController:vc animated:YES];
        return;
    }
    if (appSetting.isUseGoogleDriveSync && [VASyncSettingViewController isLinkWithCloud:kTypeCloudGDrive]) {
        VADropboxViewController *vc = [[VADropboxViewController alloc] initWithNibName:@"VADropboxViewController" bundle:nil];
        vc.typeCloud = kTypeCloudGDrive;
        [self.navigationController pushViewController:vc animated:YES];
        return;
    }
    
    [TDAlert showMessageWithTitle:TDLocStrOne(@"NoCloudSetup") message:nil delegate:self];
}


- (IBAction)btAddGroupPressed:(id)sender {
    [self showTextFileAlert:TDLocStrOne(@"AddFolder") tag:kTagAlertAddGroup];
}


- (IBAction)btEditPressed:(UIButton*)sender {
    if (_isSearching) {
        return;
    }
    _isEditting = !_isEditting;
    [_tbGroup setEditing:_isEditting animated:YES];
    [_tbId setEditing:_isEditting animated:YES];
    
    [sender setSelected:_isEditting];
    if (!_isEditting) {
        CGRect f = sender.frame;
        f.size = CGSizeMake(17, 28);
        sender.frame = f;
        [_user updateGroupOrder:[VAGlobal share].dbManager];
    }else{
        CGRect f = sender.frame;
        f.size = CGSizeMake(25, 24);
        sender.frame = f;
    }
}
-(void)addElementID{
    VAElementId *element = [[[VAElementId alloc] init] autorelease];
    element.group = _selectedGroup;
    element.iOrder = [[_selectedGroup.aElements lastObject] iOrder]+1;
    
    [self showEditElement:element isEdit:NO];
}

- (IBAction)btAddIdPressed:(id)sender
{
    if (_iSelectedGroup >= _user.aUserFolder.count) {
        TDLOGERROR(@"Not create in here");
        [TDAlert showMessageWithTitle:TDLocStrOne(@"InvalidFolder") message:nil delegate:self];
        return;
    }
    if ([VAGlobal share].appSetting.isUnlockLimitId) {
        [self addElementID];
        return;
    }
    int total = 0;
    for (VAGroup *g in _user.aUserFolder) {
        total += g.aElements.count;
    }
    if (total < kMaxNumElementID) {
        [self addElementID];
        //[TDAlert showMessageWithTitle:TDLocStrOne(@"InAppLimitWarn") message:nil delegate:self otherButton:nil tag:kTagAlertAddID];
    }else{
        [TDAlert showMessageWithTitle:TDLocStrOne(@"InAppLimitWarn") message:nil delegate:self otherButton:nil tag:kTagAlertAddIDLimitReach];
    }
}
-(void)showEditElement:(VAElementId*)element isEdit:(BOOL)isEdit{
    VAElementViewController *vc = [[[VAElementViewController alloc] initWithNibName:@"VAElementViewController" bundle:nil]autorelease];
    vc.currentElement = element;
    vc.elementDelegate = self;
    vc.isEditMode = isEdit;
    [self.navigationController pushViewController:vc animated:YES];
}
#pragma mark - ElementViewDelegate
-(void)elementViewDidCancel:(VAElementViewController *)controller{
    [controller.navigationController popToRootViewControllerAnimated:YES];
}
-(void)insertTorecent:(VAElementId*)element{
    [_user.recentGroup.aElements removeObject:element];
    [_user.recentGroup.aElements insertObject:element atIndex:0];
    if (_user.recentGroup.aElements.count >10) {
        [_user.recentGroup.aElements removeObjectAtIndex:_user.recentGroup.aElements.count -1];
    }
}
-(void)elementViewDidSave:(VAElementViewController *)controller{
    
    VAElementId *element = controller.currentElement;
    
    if (![element.sTitle isNotEmpty]) {
        return;
    }
    if (controller.isEditMode) {
        [element updateToDb:[VAGlobal share].dbManager];
        NSInteger indexFav = [_user.favoriteGroup.aElements indexOfObject:element];
        if (element.iFavorite == 1) {
            if (indexFav == NSNotFound) {
                [_user.favoriteGroup.aElements addObject:element];
            }else{
                
            }
        
        }else{
            if (indexFav != NSNotFound) {
                [_user.favoriteGroup.aElements removeObjectAtIndex:indexFav];
            }
        }
        [self insertTorecent:element];
    }else{
        [element insertToDb:[VAGlobal share].dbManager];
        [_selectedGroup.aElements addObject:element];
        if (element.iFavorite == 1) {
            [_user.favoriteGroup.aElements addObject:element];
        }
        [self insertTorecent:element];
    }
    
    [controller.navigationController popToRootViewControllerAnimated:YES];
    [_tbId reloadData];
}

#pragma mark - edit db
-(void)addGroup:(NSString *)name{
    if ([name isNotEmpty]) {
        BOOL val = [_user addGroup:name database:[VAGlobal share].dbManager];
        if (val) {
            _iSelectedGroup = _user.aUserFolder.count-1;
            [self reLoadData];
        }else{
            [self showAlert:TDLocStrOne(@"InsertGroupError") tag:0];
        }
    }else{
        [self showAlert:TDLocStrOne(@"NameGrError") tag:0];
    }
}
-(void)changeCurrentGroupName:(NSString*)name{
    if ([name isNotEmpty]) {
        _selectedGroup.sGroupName = name;
        [_selectedGroup updateToDb:[VAGlobal share].dbManager];
        [_tbGroup reloadData];
    }else{
        [self showAlert:TDLocStrOne(@"NameGrError") tag:0];
    }
    
}


#pragma mark - table view
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (_user.bIsLoadFullData) {
        if (tableView == _tbGroup) {
            if (_isSearching) {
                return _user.aUserFolder.count + 3;
            }else{
                return _user.aUserFolder.count + 2;
            }
            
        }else{
            return _selectedGroup.aElements.count;
        }
    }else{
        return 0;
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
#define kHeightOfListElementCell 83
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPat{
    int row = indexPat.row;
    if (tableView == _tbGroup) {
        UITableViewCell *cell = nil;
        if (row < _user.aUserFolder.count) {
            static NSString *normalCellGroup = @"VAImgLabelCell";
            cell = [tableView dequeueReusableCellWithIdentifier:normalCellGroup];
            if (cell == nil) {
                cell = [[[NSBundle mainBundle] loadNibNamed:normalCellGroup owner:self options:nil]objectAtIndex:0];
                UITapGestureRecognizer *gesture = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView:)] autorelease];
                
                gesture.delegate = self;
                
//                for (UIGestureRecognizer *g in cell.gestureRecognizers) {
//                    [g requireGestureRecognizerToFail:gesture];
//                }
                [cell addGestureRecognizer:gesture];
            }
            cell.tag = indexPat.row;
            //[(VAImgLabelCell*)cell vEdit].tag = indexPat.row;
            VAGroup *group = [_user.aUserFolder objectAtIndex:row];
            ((VAImgLabelCell*)cell).lbTitle.text = group.sGroupName;
            
        }else{
            static NSString *specialCellGroup = @"VAImageCell";
            cell = [tableView dequeueReusableCellWithIdentifier:specialCellGroup];
            //cell.backgroundView = [(VAImageCell*)cell bgView];
            if (cell == nil) {
                cell = [[[NSBundle mainBundle] loadNibNamed:specialCellGroup owner:self options:nil]objectAtIndex:0];
            }
            
            if (_isSearching) {
                if (row == _user.aUserFolder.count) {
                    ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"sync.png"];
                }else if (row == _user.aUserFolder.count+1){
                    ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"Favorite.png"];
                }
                else{
                    ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"history.png"];
                }
            }else{
                if (row == _user.aUserFolder.count){
                    ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"Favorite.png"];
                }
                else{
                    ((VAImageCell*)cell).imForceground.image = [UIImage imageNamed:@"history.png"];
                }
            }
            
            
        }
        [self setGroupSelected:cell row:row];
        return cell;
        
    }else{
        //_tb id
        return [self cellForElementTable:row];
    }
}
-(BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gesture
{
    if ([gesture isKindOfClass:[UITapGestureRecognizer class]]) {
        if (!_isEditting) {
            return NO;
        }
        UITableViewCell *cell = (UITableViewCell*)gesture.view;
        CGPoint pos = [gesture locationInView:cell];
        CGSize size = cell.frame.size;
        
        CGRect haftFrame = CGRectMake(size.width *0.5f, size.height *0.2,
                                      size.width* 0.4f, size.height*0.6);
        if (CGRectContainsPoint(haftFrame, pos)) {
            return YES;
        }else{
            return NO;
        }
    }
    return YES;
}
-(void)tapView:(UITapGestureRecognizer*)gesture{
    UIView *cell = (UIView*)gesture.view;
    if (gesture.state == UIGestureRecognizerStateEnded){
        TDLOG(@"end");
        int tag = cell.tag;
        if (tag < _user.aUserFolder.count) {
            [self selectGroup:[NSIndexPath indexPathForRow:tag inSection:0]];
            
            [self showTextFileAlert:TDLocStrOne(@"ChangeFolderName") tag:kTagAlertChangeGroupName];
        }
    }
}
-(void)moveIdCell:(UILongPressGestureRecognizer*)recognizer{
    UITableViewCell *cell = (UITableViewCell *)recognizer.view;
    if (recognizer.state == UIGestureRecognizerStateBegan
        || recognizer.state == UIGestureRecognizerStateChanged) {
        if (cell.superview != self.view) {
            self.cellParent = cell.superview;
            self.cellRect = cell.frame;
            
            CGRect rect = [self.view convertRect:cell.frame fromView:cell.superview];
            [self.view addSubview:cell];
            cell.frame = rect;
            _tbId.scrollEnabled = NO;
        }
    
        CGPoint movePosition = [recognizer locationInView:self.view];
        cell.center = movePosition;
    }else if (recognizer.state == UIGestureRecognizerStateEnded) {
        if (cell.superview == self.view) {
            NSArray *cellGroups = [_tbGroup visibleCells];
            
            for (UITableViewCell *view in cellGroups) {
                NSIndexPath *indexPath = [_tbGroup indexPathForCell:view];
                if (indexPath.row >= _user.aUserFolder.count) {
                    continue;
                }
                
                CGRect rect = [self.view convertRect:view.frame fromView:view.superview];
                rect.size.width *= 0.2;
                rect.size.height *=0.2;
                rect.origin.x += rect.size.width*0.5;
                rect.origin.y += rect.size.height*0.5;
                BOOL intersect = CGRectIntersectsRect(rect, cell.frame);
                if (intersect ) {
                    if (indexPath.row != _iSelectedGroup) {
                        NSIndexPath *currentRow = [_tbId indexPathForCell:cell];
                        VAElementId *element = [[_selectedGroup.aElements objectAtIndex:currentRow.row] retain];
                        [_selectedGroup.aElements removeObjectAtIndex:currentRow.row];
                        
                        VAGroup *newGroup = [_user.aUserFolder objectAtIndex:indexPath.row];
                        [newGroup.aElements addObject:element];
                        element.group = newGroup;
                        element.iOrder = [[newGroup.aElements lastObject] iOrder]+1;
                        [element updateToDb:[VAGlobal share].dbManager];
                        
                        [cell removeFromSuperview];
                        
                        [self selectGroup:indexPath];
                        [element release];
                        
                        return;
                    }else{
                        break;
                    }
                    
                }
            }
            [_cellParent addSubview:cell];
            cell.frame = _cellRect;
             
            _tbId.scrollEnabled = YES;
        }
    }
}
#define kTagImageView 101
-(UITableViewCell*)cellForElementTable:(int)row{
    NSString *cellIdentifier = @"VAElementCell";
    UITableViewCell *cell = [_tbId dequeueReusableCellWithIdentifier:cellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellIdentifier];
        [cell.imageView setContentMode:UIViewContentModeScaleAspectFit];
        cell.textLabel.textColor = [UIColor whiteColor];
        cell.detailTextLabel.textColor = [UIColor whiteColor];
        UILongPressGestureRecognizer *longGesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(moveIdCell:)];
        [cell addGestureRecognizer:longGesture];
    }
    VAElementId *element = [_selectedGroup.aElements objectAtIndex:row];
    
    cell.backgroundView = [[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"item-bar.png"]] autorelease];
    [cell setSelectedBackgroundView:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"item-bar-select.png"]] autorelease]];
    
    cell.textLabel.text = element.sTitle;
    cell.detailTextLabel.text = element.sUrl;
    
    float offset = 16;
    float height = kHeightOfListElementCell - offset *2;
    
    float offsetX = 10;
    float width = height *4/3;
    
    UIImageView *imgView= nil;
    for (UIView *v  in cell.contentView.subviews) {
        if (v.tag == kTagImageView) {
            imgView = (UIImageView*)v;
            break;
        }
    }
    if (imgView == nil) {
        imgView=[[UIImageView alloc] initWithFrame:CGRectMake(offsetX, offset, width, height)];
        imgView.backgroundColor=[UIColor clearColor];
        imgView.tag = kTagImageView;
        [cell.contentView addSubview:imgView];
    }
    
    cell.indentationWidth = kHeightOfListElementCell;
    cell.indentationLevel = 1;
    
    UIImage *im = nil;
    if (element.sEIcon != nil) {
        im = [TDImageEncrypt imageWithName:element.sEIcon];
    }
    if (im == nil) {
        im = [UIImage imageNamed:@"albumIcon.png"];
    }
    
    imgView.image = im;
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView == _tbGroup) {
        if (_iSelectedGroup == indexPath.row && (indexPath.row < _user.aUserFolder.count)) {
            return;
            [self showTextFileAlert:TDLocStrOne(@"ChangeFolderName") tag:kTagAlertChangeGroupName];
        }else{
            [self selectGroup:indexPath];
        }
    }else{
        //_tb id
        [self showActionSheetForElement:[_selectedGroup.aElements objectAtIndex:indexPath.row]];
    }
}


#pragma mark - edit table
-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (tableView == _tbGroup && indexPath.row>= _user.aUserFolder.count) {
        return UITableViewCellEditingStyleNone;
    }
    return UITableViewCellEditingStyleDelete;
}
-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    TDLOG(@"row= %d, count=%d", indexPath.row, _user.aUserFolder.count);
    if (tableView == _tbGroup) {
        if ((indexPath.row >= (_user.aUserFolder.count))) {
            return NO;
        }
        return YES;
    }
    if (_iSelectedGroup >= _user.aUserFolder.count) {
        return NO;
    }
    return YES;
}
-(BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath{
    if (tableView == _tbGroup) {
        if ((indexPath.row >= (_user.aUserFolder.count))) {
            return NO;
        }
        return YES;
    }
    if (_iSelectedGroup >= _user.aUserFolder.count) {
        return NO;
    }
    return YES;
}
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *) sourceIndexPath toIndexPath:(NSIndexPath *)destinationIndexPath {
    NSUInteger sourceRow = sourceIndexPath.row;
    NSUInteger desRow = destinationIndexPath.row;
    
    if (sourceRow != desRow) {
        
    }
    
    if (tableView == _tbGroup) {
        if (desRow > _user.aUserFolder.count-1) {
            return;
        }
        id object = [[_user.aUserFolder objectAtIndex:sourceRow] retain];
        [_user.aUserFolder removeObjectAtIndex:sourceRow];
        [_user.aUserFolder insertObject:object atIndex:desRow];
        [object release];
        int newSelected = [_user.aUserFolder indexOfObject:_selectedGroup];
        [self selectGroup:[NSIndexPath indexPathForRow:newSelected inSection:0]];
    } else {
        id object = [[_selectedGroup.aElements objectAtIndex:sourceRow] retain];
        [_selectedGroup.aElements removeObjectAtIndex:sourceRow];
        [_selectedGroup.aElements insertObject:object atIndex:desRow];
        [object release];
        
        [_selectedGroup updateElementOrder:[VAGlobal share].dbManager];
    }
}
- (NSIndexPath *)tableView:(UITableView *)tableView targetIndexPathForMoveFromRowAtIndexPath:(NSIndexPath *)sourceIndexPath toProposedIndexPath:(NSIndexPath *)proposedDestinationIndexPath{
    
    if (tableView == _tbGroup) {
        if (proposedDestinationIndexPath.row > _user.aUserFolder.count-1) {
            return sourceIndexPath;
        }
    }
    return proposedDestinationIndexPath;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    if (UITableViewCellEditingStyleDelete != editingStyle) {
        return;
    }
    int row = indexPath.row;
    if (tableView == _tbGroup) {
        VAGroup *group = [_user.aUserFolder objectAtIndex:row];
        [group weakDeleteFromDb:[VAGlobal share].dbManager];
        for (VAElementId *element in group.aElements) {
            [element weakDeleteFromDb:[VAGlobal share].dbManager];
            
        }
        [_user.favoriteGroup.aElements removeObjectsInArray:group.aElements];
        [_user.recentGroup.aElements removeObjectsInArray:group.aElements];
        [_searchGroup.aElements removeObjectsInArray:group.aElements];
        
        [_user.aUserFolder removeObjectAtIndex:row];
        if (_iSelectedGroup >= _user.aUserFolder.count) {
            _iSelectedGroup = _user.aUserFolder.count-1;
        }
        [self selectGroup:[NSIndexPath indexPathForRow:_iSelectedGroup inSection:0]];
        [_tbGroup deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationLeft];
        [_tbId reloadData];
    }else{
        VAElementId *element = [_selectedGroup.aElements objectAtIndex:row];
        [element weakDeleteFromDb:[VAGlobal share].dbManager];
        [_user.favoriteGroup.aElements removeObject:element];
        [_user.recentGroup.aElements removeObject:element];
        [_searchGroup.aElements removeObject:element];
        
        [_selectedGroup.aElements removeObjectAtIndex:row];
        
        [_tbId deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationRight];
    }
}
- (void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath
{
    
}
-(void)showActionSheetForElement:(VAElementId*)element{
    self.selectedElement = element;
    VAElementOptionController *vc = [[[VAElementOptionController alloc] initWithNibName:@"VAElementOptionController" bundle:nil] autorelease];
    vc.selectedElement = element;
    vc.elementDelegate = self;
    [self.navigationController pushViewController:vc animated:YES];
    
    return;
    
    UIActionSheet *acSh = [[UIActionSheet alloc]initWithTitle:nil
                                                     delegate:self
                                            cancelButtonTitle:
                           TDLocStrOne(@"Cancel")
                                       destructiveButtonTitle:
                           TDLocStrOne(@"Password")
                                            otherButtonTitles:
                           TDLocStrOne(@"ID"),
                           TDLocStrOne(@"Web"),
                           
                           TDLocStrOne(@"Edit"),
                           nil];
    UIWindow *window = [TDAppDelegate share].window;
    [acSh showInView:window];
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (buttonIndex == actionSheet.cancelButtonIndex) {
        
    }else if (buttonIndex == actionSheet.destructiveButtonIndex){
        if (_selectedElement.aPasswords.count>0) {
            [self pasteClipBoard:[(VAPassword*)[_selectedElement.aPasswords objectAtIndex:0] sPassword]];
            [self showAlert:@"PasswordCopy"];
        }else{
            [self showAlert:@"NoPassword"];
        }
        
    }else if (buttonIndex == actionSheet.firstOtherButtonIndex){
        if (_selectedElement.aPasswords.count > 0) {
            [self pasteClipBoard:[(VAPassword*)[_selectedElement.aPasswords objectAtIndex:0] sTitleNameId]];
            [self showAlert:@"IdCopy"];
        }else{
            [self showAlert:@"NoId"];
        }
        
    }else if (buttonIndex == actionSheet.firstOtherButtonIndex +1){
        //
        TDLOG(@"Start web");
        TDWebViewController *web = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil]autorelease];
        web.sUrlStart = _selectedElement.sUrl;
        web.bIsUseJogDial = YES;
        web.listPWID = _selectedElement.aPasswords;
        web.sNote = _selectedElement.sNote;
        web.webDelegate = self;
        web.iTag = 0;
        [self.navigationController pushViewController:web animated:YES];
    }else{
        //
        TDLOG(@"Begin edit");
        [self showEditElement:_selectedElement isEdit:YES];
    }
}
#pragma mark - webview delegate
-(void)browserBack:(TDWebViewController*)controller{
    [controller.navigationController popViewControllerAnimated:YES];
}
#pragma - utility
-(void)pasteClipBoard:(NSString*)str{
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.persistent = YES;
    pasteboard.string = str;
}
-(void)showAlert:(NSString*)str{
    UIAlertView *al = [[[UIAlertView alloc] initWithTitle:str message:nil delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles:nil]autorelease];
    [al show];
}
#pragma mark - search bar
-(void)doSearch:(NSString*)text{
    if (![text isNotEmpty]) {
        _searchGroup.aElements = [NSMutableArray array];
    }else{
        NSMutableArray *arr = [NSMutableArray array];
        for (VAGroup *group in _user.aUserFolder) {
            for (VAElementId *element in group.aElements) {
                BOOL isAdded = ([element.sTitle rangeOfString:text options:NSCaseInsensitiveSearch].location != NSNotFound);
                if (!isAdded) {
                    isAdded = ([element.sNote rangeOfString:text options:NSCaseInsensitiveSearch].location != NSNotFound);
                }
                if (isAdded) {
                    [arr addObject:element];
                }
            }
        }
        _searchGroup.aElements = arr;
    }
    
    //check selected group
    if (_iSelectedGroup != _user.aUserFolder.count) {
        [self selectGroup:[NSIndexPath indexPathForRow:_user.aUserFolder.count inSection:0]];
    }else{
        [_tbId reloadData];
    }
}
-(void)beginSearch{
    _isSearching = YES;    
}

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar{
    if (_isEditting) {
        return NO;
    }
    return YES;
}// return NO to not become first responder
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar{
    [searchBar setShowsCancelButton:YES animated:YES];
    _isSearching = YES;
    _iSelectedGroup = _user.aUserFolder.count;
    [self reLoadData];
    
}// called when text starts editing
- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar{
    return YES;
}// return NO to not resign first responder
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    NSString *str = searchBar.text;
    [self doSearch:str];
    [searchBar setShowsCancelButton:NO animated:YES];
    //_isSearching = NO;
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    [self doSearch:searchText];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar{
    searchBar.text = @"";
    [searchBar resignFirstResponder];
}
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [searchBar resignFirstResponder];
}

#pragma mark - inapp purchase
-(void)initInappPurchase{
    //Đoạn code dưới khởi tạo object ASPurchaseView
    //Các tham số trong NSDictionary truyền vào params là các tham số cần gửi lên server ở bước xác thực mua item
    //Trong đó tham số ID của user là bắt buộc phải có để định danh user, hoặc định danh device
    //Các tham số khác là optional
    
    NSMutableDictionary *paramsDic = [[NSMutableDictionary alloc] init];
    [paramsDic setObject:@"d9ba61da72582d3ff0288b937763d53c" forKey:@"user_id"];
    [paramsDic setObject:@"ios" forKey:@"platform"];
    [paramsDic setObject:@"vn" forKey:@"location"];
    [paramsDic setObject:@"true" forKey:@"sandbox"];
    
    //Link đến server để gửi request xác thực mua item
    NSString *urlString = nil;
    
    //Parent param ở đây mặc định phải là kiểu UIViewController, nếu là kiểu UIView thì lại phải sửa lại 1 chỗ addSubview trong ASPurchaseView.m
    //language param có thể là "en", "jp", "vn". Nếu ko thuộc các kiểu trên thì mặc định là "en"
    self.purchaseView = [[[ASPurchaseView alloc] initWithParent:self
                                              purchaseServerURL:urlString
                                                         params:paramsDic
                                                       language:@"en"] autorelease];
    self.purchaseView.isVerifyByServer = NO;
}

-(void)purchaseWithProductID:(NSString*)productId {
    [[UIApplication sharedApplication].keyWindow addSubview:_purchaseView];
    [_purchaseView purchaseWithProductID:productId];
    
}

//Add hàm này vào với đúng tên hàm.
-(void) receivePurchaseError:(NSNotification*)notification {
    TDLOG(@"receivePurchaseError");

}
//Add hàm này vào với đúng tên hàm.
-(void) receivePurchaseFinish:(NSNotification*)notification {
    TDLOG(@"receivePurchaseFinish");
    NSMutableDictionary *responseDic = (NSMutableDictionary*)[notification object];
    TDLOG(@"%@",responseDic);
    NSString *product_id = [responseDic objectForKey:@"product_id"];
    if (!product_id) {
        return;
    }
    VASetting *appSetting = [VAGlobal share].appSetting;
    
    if ([product_id isEqualToString:kUnlimitedId]) {
        appSetting.isUnlockLimitId = YES;
    }else if ([product_id isEqualToString:kID_CSV_EXPORT]){
        appSetting.isUnlockCSVExport = YES;
    }else if ([product_id isEqualToString:kAdRemoveID]){
        appSetting.isUnlockHideIad = YES;
    }else{
        TDLOGERROR(@"Invalid id = %@", product_id);
    }
    
    [appSetting saveSetting];
    
}

@end
