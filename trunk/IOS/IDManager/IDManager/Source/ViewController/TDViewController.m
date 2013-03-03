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

@interface TDViewController ()
@property (nonatomic, assign) int iSelectedGroup;
@property (nonatomic, retain) VAGroup *selectedGroup;
@property (nonatomic, retain) VAUser *user;



@property (nonatomic, retain) UIView *cellParent;
@property (nonatomic, assign) CGRect cellRect;
@property (nonatomic, retain) VAElementId *selectedElement;

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
    login.loginDelegate = self;
    VASetting *setting = [VAGlobal share].appSetting;
    if (setting.isFirstUse) {
        VATermViewController *vc = [[[VATermViewController alloc] initWithNibName:@"VATermViewController" bundle:nil] autorelease];
        login.typeMasterPass = kTypeMasterPasswordFirst;
        [login presentModalViewController:vc animated:NO];
    }else{
        login.typeMasterPass = kTypeMasterPasswordLogin;
    }
}


#pragma mark - loginDelegate
-(void)loginViewDidLogin:(VALoginController *)vc{
    if (vc.typeMasterPass == kTypeMasterPasswordFirst ||
        vc.typeMasterPass == kTypeMasterPasswordLogin) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(idleTimeRaise:) name:TDIdleNotification object:nil];
        [[VAGlobal share].appSetting updateSecurityTime];
    }else if (vc.typeMasterPass == kTypeMasterPasswordReLogin){
        [[VAGlobal share].appSetting updateSecurityTime];
    }
    
}
-(void)idleTimeRaise:(NSNotification*)notifi{
    if ([VAGlobal share].appSetting.isSecurityOn) {
        VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
        [self.navigationController pushViewController:login animated:NO];
        login.loginDelegate = self;
        login.typeMasterPass = kTypeMasterPasswordReLogin;
    }else{
        TDLOGERROR(@"Error security off but idle is running");
    }
}
//called after login
-(void)reLoadData{
    self.user = [VAGlobal share].user;
    [_tbGroup reloadData];
    [self updateSelectedGroup];
}
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
    [self showAlert:TDLocStrOne(@"DataDestroyed")];
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
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"\n\n" delegate:self cancelButtonTitle:TDLocStrOne(@"Cancel") otherButtonTitles:TDLocStrOne(@"OK"), nil];
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
    TDWebViewController *web  = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil] autorelease];
    web.sUrlStart = @"http://google.com";
    web.webDelegate = self;
    [self.navigationController pushViewController:web animated:YES];
}

- (IBAction)btSettingPressed:(id)sender {
    VASettingViewController *setting = [[VASettingViewController alloc] initWithNibName:@"VASettingViewController" bundle:nil];
    [self.navigationController pushViewController:setting animated:YES];
}


- (IBAction)btAddGroupPressed:(id)sender {
    [self showTextFileAlert:TDLocStrOne(@"AddFolder") tag:kTagAlertAddGroup];
}


- (IBAction)btEditPressed:(UIButton*)sender {
    [_tbGroup setEditing:!_tbGroup.editing animated:YES];
    [_tbId setEditing:!_tbId.editing animated:YES];
    [sender setSelected:_tbId.editing];
    if (!_tbId.isEditing) {
        [_user updateGroupOrder:[VAGlobal share].dbManager];
    }
}

- (IBAction)btAddIdPressed:(id)sender {
    if (_iSelectedGroup >= _user.aUserFolder.count) {
        TDLOGERROR(@"Not create in here");
    }else{
        VAElementId *element = [[[VAElementId alloc] init] autorelease];
        element.group = _selectedGroup;
        element.iOrder = [[_selectedGroup.aElements lastObject] iOrder]+1;
        [self showEditElement:element isEdit:NO];
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
            //cell.backgroundView = [(VAImageCell*)cell bgView];
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
    
    float offset = 5;
    float height = kHeightOfListElementCell - offset *2;
    UIImageView *imgView=[[UIImageView alloc] initWithFrame:CGRectMake(offset, offset, height, height)];
    imgView.backgroundColor=[UIColor clearColor];
    [cell.contentView addSubview:imgView];
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
        [self selectGroup:indexPath];
    }else{ //_tb id
        [self showActionSheetForElement:[_selectedGroup.aElements objectAtIndex:indexPath.row]];
    }
}

-(void)showActionSheetForElement:(VAElementId*)element{
    self.selectedElement = element;
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
#pragma mark - edit table
-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (tableView == _tbGroup && indexPath.row>= _user.aUserFolder.count) {
        return UITableViewCellEditingStyleNone;
    }
    return UITableViewCellEditingStyleDelete;
}
-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    TDLOG(@"row= %d, count=%d", indexPath.row, _user.aUserFolder.count);
    if ((tableView == _tbGroup) && (indexPath.row >= (_user.aUserFolder.count))) {
        return NO;
    }
    return YES;
}
-(BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath{
    if (tableView == _tbGroup && indexPath.row > _user.aUserFolder.count-1) {
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
        [_user.aUserFolder removeObjectAtIndex:row];
        if (_iSelectedGroup >= _user.aUserFolder.count) {
            _iSelectedGroup = _user.aUserFolder.count-1;
        }
        [self selectGroup:[NSIndexPath indexPathForRow:_iSelectedGroup inSection:0]];
        [_tbGroup deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationLeft];
    }else{
        VAElementId *element = [_selectedGroup.aElements objectAtIndex:row];
        [element weakDeleteFromDb:[VAGlobal share].dbManager];
        [_selectedGroup.aElements removeObjectAtIndex:row];
        [_tbId deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationRight];
    }
}
@end
