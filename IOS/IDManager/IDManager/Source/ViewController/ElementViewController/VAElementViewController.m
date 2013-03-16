//
//  ElementViewController.m
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAElementViewController.h"
#import "VAPassword.h"
#import "TDCommonLibs.h"
#import "TDString.h"
#import "TDWebViewController.h"
#import "VAChooseIconViewController.h"
#import "VAGenTextViewController.h"
#import "TDImageEncrypt.h"
#import "VAGlobal.h"

@implementation VAPasswordIDView
- (IBAction)btGenTextId:(id)sender {
    self.selectedField = self.vId;
    [self.delegate genTextFor:self];
}
- (IBAction)btGenTextPassword:(id)sender {
    self.selectedField = self.vPassword;
    [self.delegate genTextFor:self];
}
- (void)updatePWIDFromView{
    _currentPwId.sTitleNameId = _vId.text;
    _currentPwId.sPassword = _vPassword.text;
}
-(void)updatePWIDToView{
    _vId.text = _currentPwId.sTitleNameId ;
    _vPassword.text = _currentPwId.sPassword;
}
-(void)textFieldDidEndEditing:(UITextField *)textField{
    [self updatePWIDFromView];
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
-(void)dealloc{
    [_vPassword release];
    [_vId release];
    [super dealloc];
}
@end

#define kMaxIDPw 5

@interface VAElementViewController ()<VAGenTextDelegate>

@property (retain, nonatomic) id tfCurrActive;

@property (retain, nonatomic) IBOutlet UIImageView *imIcon;
@property (retain, nonatomic) IBOutlet UIButton *btIcon;
@property (retain, nonatomic) IBOutlet UIButton *btImage;


@property (retain, nonatomic) IBOutlet UITextField *tfTitle;
@property (retain, nonatomic) IBOutlet UIButton *btFavorite;

@property (retain, nonatomic) IBOutlet VAPasswordIDView *pwNormal;

@property (retain, nonatomic) IBOutlet UITableView *tbListPwId;
@property (retain, nonatomic) IBOutlet UIScrollView *svScrollView;
@property (retain, nonatomic) IBOutlet UIView *vContent;
@property (retain, nonatomic) IBOutlet UIView *vTop;
@property (retain, nonatomic) IBOutlet UIView *vDetail;
@property (retain, nonatomic) IBOutlet UIControl *vListID;
@property (retain, nonatomic) IBOutlet UIImageView *imFlick;


@property (nonatomic, retain) VAPasswordIDView *selectPw;
@property (retain, nonatomic) IBOutlet UITextField *tfUrl;
@property (retain, nonatomic) IBOutlet UITextView *tfNote;

@property(nonatomic, retain)  NSString *sIconUrl;
@property(nonatomic, retain) NSString *sImagePath;
@property(nonatomic, retain) NSMutableArray *listPwId;

- (IBAction)btIconPressed:(id)sender;

- (IBAction)btFavoritePressed:(id)sender;
- (IBAction)btUrlPressed:(id)sender;

- (IBAction)btBackPressed:(id)sender;
- (IBAction)btInfoPressed:(id)sender;
- (IBAction)touchOnBg:(id)sender;

-(void)updateView;
-(void)updateFavoriteStatus;
-(void)setUpScrollView;

@end

@implementation VAElementViewController

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
    
    self.listPwId = [NSMutableArray arrayWithCapacity:5];
    
    //create list PwId to save ID-Password
    for (int i=0; i<kMaxIDPw; i++) {
        VAPassword *pass = [[[VAPassword alloc] init] autorelease];
        if (_currentElement.aPasswords.count > i) {
            VAPassword *oldPass = [_currentElement.aPasswords objectAtIndex:i];
            [pass copyFrom:oldPass];
        }
        [_listPwId addObject:pass];
    }
    
    [self updateView];
    [self registerKeyboard];
    
    UIPanGestureRecognizer *panGesture = [[[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panView:)] autorelease];
    [_vListID addGestureRecognizer:panGesture];
}
#pragma mark gesture
#define kMaxHeightListID 250
-(void)panView:(UIPanGestureRecognizer*)gesture{
    if (gesture.state == UIGestureRecognizerStateChanged) {
        float dy = [gesture translationInView:gesture.view].y;
        //TDLOG(@"translate = %f", dy);
        CGRect oldFrame = _vListID.frame;
        if (oldFrame.size.height + dy < _imFlick.frame.size.height) {
            oldFrame.size.height = _imFlick.frame.size.height;
        }else if (oldFrame.size.height + dy > kMaxHeightListID){
            oldFrame.size.height = kMaxHeightListID;
        }else{
            oldFrame.size.height = oldFrame.size.height + dy;
        }
        _vListID.frame = oldFrame;
        
        CGRect tailFrame = _vDetail.frame;
        tailFrame.origin.y = oldFrame.origin.y + oldFrame.size.height;
        tailFrame.size.height = MAX(self.view.frame.size.height - tailFrame.origin.y, 400);
        _vDetail.frame = tailFrame;
        
        [gesture setTranslation:CGPointMake(0, 0) inView:gesture.view];
    }else if (gesture.state == UIGestureRecognizerStateEnded||
              gesture.state == UIGestureRecognizerStateCancelled){
        CGRect content = _vContent.frame;
        content.size.height = _vTop.frame.size.height + _vListID.frame.size.height +
        _vDetail.frame.size.height;
        _vContent.frame = content;
        [self setUpScrollView];
    }
}
-(void)setUpScrollView{
    _svScrollView.contentSize = _vContent.frame.size;
    _svScrollView.contentInset = (UIEdgeInsets){0,0,1,0};
}
-(void)viewDidLayoutSubviews{
    [self setUpScrollView];
}
-(void)updateView{
    [self updateFavoriteStatus];
    _tfNote.text = _currentElement.sNote;
    self.sIconUrl = _currentElement.sEIcon;
    _tfUrl.text =  _currentElement.sUrl;
    self.sImagePath = _currentElement.sImage;
    _currentElement.dTimeStamp = [[NSDate date] timeIntervalSince1970];
    _tfTitle.text = _currentElement.sTitle ;
    //icon
    UIImage *image = [TDImageEncrypt imageWithName:_sIconUrl];
    if (image) {
        [_btIcon setImage:image forState:UIControlStateNormal];
    }
    
    //image
    [self updateButtonImage];
}
-(void)updateButtonImage{
    UIImage *im= nil;
    if (self.sImagePath) {
        im = [TDImageEncrypt imageWithName:_sImagePath];
    }
    if (im) {
        [_btImage setImage:im forState:UIControlStateNormal];
        [_btImage setTitle:@"" forState:UIControlStateNormal];
    }else{
        [_btImage setImage:nil forState:UIControlStateNormal];
        [_btImage setTitle:TDLocStrOne(@"NoImage") forState:UIControlStateNormal];
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)updateFavoriteStatus{
    if (_currentElement.iFavorite == 0) {
        [_btFavorite setImage:[UIImage imageNamed:@"F1.png"] forState:UIControlStateNormal];
    }else{
        [_btFavorite setImage:[UIImage imageNamed:@"F2.png"] forState:UIControlStateNormal];
    }
}

#define kTagChooseImage 1022
- (IBAction)btImagePressed:(id)sender {
    VAEditImageViewController *vc = [[VAEditImageViewController alloc] initWithNibName:@"VAEditImageViewController" bundle:nil];
    vc.delegate = self;
    vc.sCurrentImagePath = self.sImagePath;
    vc.type = kTypeChooseMemoImage;
    
    [self.navigationController pushViewController:vc animated:YES];
    
    [vc release];
}

#pragma mark - chooseImage delegate
-(void)editImageAccept:(VAEditImageViewController *)vc
{
    if (vc.sCurrentImagePath == nil) {
        if (self.sImagePath) {
            [TDImageEncrypt deleteImage:self.sImagePath];
            self.sImagePath = nil;
        }
    }else{
        if (self.sImagePath) {
            [TDImageEncrypt deleteImage:self.sImagePath];
        }
        self.sImagePath = vc.sCurrentImagePath;
    }
    [self updateButtonImage];
    [vc.navigationController popViewControllerAnimated :YES];
}
-(void)editImageCancel:(VAEditImageViewController *)vc{
    [vc.navigationController popViewControllerAnimated :YES];
}

- (IBAction)btIconPressed:(id)sender {
    VAChooseIconViewController *vc = [[VAChooseIconViewController alloc] initWithNibName:@"VAChooseIconViewController" bundle:nil];
    vc.chooseIcDelegate = self;
    vc.currentIconPath = self.sIconUrl;
    [self.navigationController pushViewController:vc animated:YES];
    
    [vc release];
    
}

- (IBAction)btFavoritePressed:(id)sender {
    if (_currentElement.iFavorite == 0) {
        _currentElement.iFavorite = 1;
    }else{
        _currentElement.iFavorite = 0;
    }
    [self updateFavoriteStatus];
}

#define kTagUrl 11
#define kTagInfo 12
- (IBAction)btUrlPressed:(id)sender {
    TDWebViewController *web = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil]autorelease];
    web.sUrlStart = @"https://google.com";
    web.webDelegate = self;
    web.iTag = kTagUrl;
    [self presentModalViewController:web animated:YES];
}
- (IBAction)btInfoPressed:(id)sender {
    TDWebViewController *web = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil]autorelease];
    web.sUrlStart = @"https://google.com";
    web.webDelegate = self;
    web.iTag = kTagInfo;
    [self presentModalViewController:web animated:YES];
}

- (IBAction)touchOnBg:(id)sender {
    [_tfCurrActive resignFirstResponder];
}


- (IBAction)btBackPressed:(id)sender {
    
    if ([_tfTitle.text isNotEmpty]) {
        _currentElement.sTitle = _tfTitle.text;
        _currentElement.sNote = _tfNote.text;
        _currentElement.sEIcon = _sIconUrl;
        _currentElement.sUrl = _tfUrl.text;
        _currentElement.sImage = _sImagePath;
        _currentElement.dTimeStamp = [[NSDate date] timeIntervalSince1970];
        
        //pass
        for (VAPasswordIDView *cell in [_tbListPwId visibleCells]) {
            [cell updatePWIDFromView];
        }
        
        NSMutableArray *listRemove = [NSMutableArray array];
        for (VAPassword *pass in _listPwId) {
            if ((![pass.sTitleNameId isNotEmpty]) ||
                (![pass.sPassword isNotEmpty]))
                {
                [listRemove addObject:pass];
            }
        }
        for (VAPassword *pass in _listPwId) {
            if (pass.elementId ) { //pass is in database -> remove it
                [pass deleteFromDb:[VAGlobal share].dbManager];
            }
        }
        
        //remove empty pass
        [_listPwId removeObjectsInArray:listRemove];
        _currentElement.aPasswords = _listPwId;
        for (VAPassword *pass in _currentElement.aPasswords) {
            pass.elementId = _currentElement;
        }
        
        [self.elementDelegate elementViewDidSave:self];
    }else{
        if (_isEditMode) {
            UIAlertView *al = [[[UIAlertView alloc] initWithTitle:TDLocStrOne(@"TitleIsNil") message:nil delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles: nil] autorelease];
            [al show];
        }else{
            [self.elementDelegate elementViewDidCancel:self];
        }
    
    }
}


#pragma mark - chooseIcon delegate
-(void)chooseIconCancel:(VAChooseIconViewController *)vc{
    [vc.navigationController popViewControllerAnimated:YES];
}
-(void)chooseIconSave:(VAChooseIconViewController *)vc{
    NSString *path = vc.currentIconPath;
    UIImage *image = [TDImageEncrypt imageWithName:path];
    self.sIconUrl = path;
    
    if (!image) {
        image = [UIImage imageNamed:@"albumIcon.png"];
    }
    
    [_btIcon setImage:image forState:UIControlStateNormal];
    
    [vc.navigationController popViewControllerAnimated:YES];
}


#pragma mark - webDelegate
- (void)browserBack:(TDWebViewController *)controller{
    NSString *url = controller.sUrl;
    if (controller.iTag == kTagUrl) {
        self.tfUrl.text = url;
        [controller dismissModalViewControllerAnimated:YES];
    }else{
        [controller dismissModalViewControllerAnimated:YES];
    }
}
- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [_sIconUrl release];
    [_imIcon release];
    [_tfTitle release];
    [_btFavorite release];
    [_pwNormal release];
    [_tfUrl release];
    [_tfNote release];
    [_tfCurrActive release];
    [_btIcon release];
    [_tbListPwId release];
    [_listPwId release];
    [_svScrollView release];
    [_vContent release];
    [_vTop release];
    [_vDetail release];
    [_vListID release];
    [_imFlick release];
    [_btImage release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setImIcon:nil];
    [self setTfTitle:nil];
    [self setBtFavorite:nil];
    [self setPwNormal:nil];
    [self setTfUrl:nil];
    [self setTfNote:nil];
    [self setBtIcon:nil];
    [self setTbListPwId:nil];
    [self setSvScrollView:nil];
    [self setVContent:nil];
    [self setVTop:nil];
    [self setVDetail:nil];
    [self setVListID:nil];
    [self setImFlick:nil];
    [self setBtImage:nil];
    [super viewDidUnload];
}

#pragma mark - textfield
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    self.tfCurrActive = textField;
}
-(void)textFieldDidEndEditing:(UITextField *)textField{
    self.tfCurrActive = nil;
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
#pragma mark - TextView
-(void)textViewDidBeginEditing:(UITextView *)textView{
    self.tfCurrActive = textView;
}
-(void)textViewDidEndEditing:(UITextView *)textView{
    self.tfCurrActive = textView;
}
-(void)genTextFor:(VAPasswordIDView *)view{
    VAGenTextViewController *vc = [[[VAGenTextViewController alloc] initWithNibName:@"VAGenTextViewController" bundle:nil]autorelease];
    vc.delegate = self;
    self.selectPw = view;
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)textGeneratorBack:(VAGenTextViewController *)vc{
    NSString *str = vc.currentText;
    TDLOG(@"GenText = %@", str);
    self.selectPw.selectedField.text = str;
    [self.selectPw updatePWIDFromView];
    [vc.navigationController popViewControllerAnimated:YES];
}

#pragma mark - tableViewDelegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _listPwId.count;
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *cellIdentify = @"VAPasswordIDView";
    VAPasswordIDView *cell = (VAPasswordIDView*)[[[NSBundle mainBundle] loadNibNamed:cellIdentify owner:self options:nil]objectAtIndex:0];
    [cell retain];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    VAPassword *pass = [_listPwId objectAtIndex:indexPath.row];
    cell.currentPwId = pass;
    [cell updatePWIDToView];
    cell.delegate = self;
    return cell;
}

#pragma mark - keyboard
-(void)registerKeyboard{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];
}
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
    _svScrollView.contentInset = contentInsets;
    _svScrollView.scrollIndicatorInsets = contentInsets;
    
    // If active text field is hidden by keyboard, scroll it so it's visible
    // Your application might not need or want this behavior.
    if (kbSize.width < kbSize.height) {
        kbSize = CGSizeMake(kbSize.height, kbSize.width);
    }
    CGRect aRect = self.view.frame;
    aRect.size.height = aRect.size.height - kbSize.height;
    if (_tfCurrActive &&
        !CGRectContainsPoint(aRect, [_tfCurrActive frame].origin) ) {
        
        
        CGPoint scrollPoint;
        scrollPoint = CGPointMake(0.0, [_tfCurrActive frame].origin.y);
        
        [_svScrollView setContentOffset:scrollPoint animated:YES];
    }
}

// Called when the UIKeyboardWillHideNotification is sent
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    UIEdgeInsets contentInsets = {0,0,1,0};
    _svScrollView.contentInset = contentInsets;
    _svScrollView.scrollIndicatorInsets = contentInsets;
    
}

@end
