//
//  VALoginController.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VALoginController.h"
#import "TDCommonLibs.h"
#import "VAGlobal.h"
#import "TDString.h"
#import "TDAppDelegate.h"
#import "VAGroup.h"
#import "TDAlert.h"

@interface VALoginController ()
@property (assign, nonatomic) int iCountWrongPass;

@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UITextField *tfOrigin;
@property (retain, nonatomic) IBOutlet UITextField *tfConfirm;
@property (retain, nonatomic) IBOutlet UIButton *btConfirm;
@property (retain, nonatomic) IBOutlet UIButton *btChangeKeyboard;
@property (retain, nonatomic) IBOutlet UILabel *lbOrigin;
@property (retain, nonatomic) IBOutlet UILabel *lbConfirm;

@property (assign, nonatomic) BOOL isChangeKeyboardPressed;
- (IBAction)changeKeyboard:(id)sender;
- (IBAction)confirm:(id)sender;

@end

@implementation VALoginController

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
    self.isChangeKeyboardPressed = NO;
    [(TDApplicationIdle*)[UIApplication sharedApplication] removeIdleCheck];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    [self registerNotification];
    switch (_typeMasterPass) {
        case kTypeMasterPasswordFirst:
            _lbOrigin.hidden = NO;
            _lbConfirm.hidden = NO;
            _tfOrigin.hidden = NO;
            [_tfOrigin becomeFirstResponder];
            _lbTitle.text = TDLocStrOne(@"MasterPassword");
            _lbOrigin.text = TDLocStrOne(@"New");
            _lbConfirm.text = TDLocStrOne(@"Verify");
            break;
            
        //login and relogin
        case kTypeMasterPasswordLogin:
        case kTypeMasterPasswordReLogin:
            _lbOrigin.hidden = YES;
            _lbConfirm.hidden = YES;
            _tfOrigin.hidden = YES;
            [_tfConfirm becomeFirstResponder];
            _lbTitle.text = TDLocStrOne(@"MasterPassword");
            break;
        case kTypeMasterPasswordChangePassCheck1:
        case kTypeMasterPasswordChangePassCheck2:
            _lbOrigin.hidden = YES;
            _tfOrigin.hidden = YES;
            
            _lbConfirm.hidden = YES;
            [_tfConfirm becomeFirstResponder];
            _lbTitle.text = TDLocStrOne(@"CurrentMasterPassword");
            //_lbConfirm.text = TDLocStrOne(@"CurrentMasterPassword");
            break;
        case kTypeMasterPasswordChangePass:
            _lbConfirm.hidden = NO;
            _lbOrigin.hidden = NO;
            _tfOrigin.hidden = NO;
            [_tfOrigin becomeFirstResponder];
            _lbTitle.text = TDLocStrOne(@"ResetMasterPassword");
            _lbOrigin.text = TDLocStrOne(@"New");
            _lbConfirm.text = TDLocStrOne(@"Verify");
            default:
            break;
    }
    _iCountWrongPass = 0;
    
}
-(void)registerNotification{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillChangeFrame:) name:UIKeyboardWillChangeFrameNotification object:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [_lbTitle release];
    [_tfOrigin release];
    [_tfConfirm release];
    [_btConfirm release];
    [_btChangeKeyboard release];
    [_lbOrigin release];
    [_lbConfirm release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setLbTitle:nil];
    [self setTfOrigin:nil];
    [self setTfConfirm:nil];
    [self setBtConfirm:nil];
    [self setBtChangeKeyboard:nil];
    [self setLbOrigin:nil];
    [self setLbConfirm:nil];
    [super viewDidUnload];
}

#pragma mark - notification
-(void)keyboardWillShown:(NSNotification*)notifi{
    [self keyboardWillChangeFrame:notifi];
    return;
}
-(void)keyboardWillChangeFrame:(NSNotification*)notifi{
    NSValue *keyboardBoundsValue = [[notifi userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardFrame;
    [keyboardBoundsValue getValue:&keyboardFrame];
    keyboardFrame = [[TDAppDelegate share].window convertRect:keyboardFrame toView:self.view];
    _btChangeKeyboard.frame = CGRectMake(0, keyboardFrame.origin.y - _btChangeKeyboard.frame.size.height, _btChangeKeyboard.frame.size.width, _btChangeKeyboard.frame.size.height);
}
-(void)changeKeyboardFor:(UITextField *)tf{
    
    if (tf.keyboardType == UIKeyboardTypeASCIICapable) {
        tf.keyboardType = UIKeyboardTypeNumberPad;
    }else{
        tf.keyboardType = UIKeyboardTypeASCIICapable;
    }
}
-(void)reAssignFor:(UITextField*)tf{
    NSString *str = tf.text;
    [tf resignFirstResponder];
    [tf becomeFirstResponder];
    tf.text = str;
}
- (IBAction)changeKeyboard:(id)sender {
    [self changeKeyboardFor:_tfOrigin];
    [self changeKeyboardFor:_tfConfirm];
    self.isChangeKeyboardPressed = YES;
    if (_tfConfirm.isEditing) {
        [self reAssignFor:_tfConfirm];
    }else if (_tfOrigin.isEditing){
        [self reAssignFor:_tfOrigin];
    }
}
#define kMaxLengPass 50
-(BOOL)checkValidConfirm
{
    if (![_tfConfirm.text isNotEmpty]) {
        [self showAlertSimple:TDLocStrOne(@"PassEmpty") message:nil tag:0];
        TDLOGERROR(@"enter password error");
        _tfConfirm.text = @"";
        _tfOrigin.text = @"";
        return NO;
    }
    if ([_tfConfirm.text length]>kMaxLengPass) {
        TDLOGERROR(@"enter password error");
        _tfConfirm.text = @"";
        _tfOrigin.text = @"";
        [self showAlertSimple:TDLocStrOne(@"PassTooLong") message:nil tag:0];
        return NO;
    }
    return YES;
}

-(void)loginFirstTime{
    if (![self checkValidConfirm]) {
        return;
    }
    if (![_tfConfirm.text isEqualToString:_tfOrigin.text]) {
        TDLOG(@"Wrong pass when init first password");
        _tfConfirm.text = @"";
        _tfOrigin.text = @"";
        //[self showAlertSimple:TDLocStrOne(@"WrongPass") tag:0];
        //        _iCountWrongPass +=1;
        //        [self checkDestroyData];
        [self showAlertSimple:TDLocStrOne(@"WrongPass") message:nil tag:0];
        return;
    }
    UIAlertView *al = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading...") delegate:self];
    VAGlobal *global = [VAGlobal share];
    NSString *masterPass = _tfConfirm.text;
    
    
    global.user.sUserPassword = masterPass;
    [global initFirstDatabase];
    if ([global insertCurrentUser]) {
        TDLOG(@"Init complete");
        VAGroup *group = [[[VAGroup alloc] init] autorelease];
        group.user = global.user;
        group.sGroupName = TDLocStrOne(@"General");
        [global.user.aUserFolder addObject:group];
        [group insertToDb:global.dbManager];
        
        global.appSetting.isFirstUse = NO;
        [global.appSetting saveSetting];
        [global loadDataAfterLogin];
        [[TDAppDelegate share].viewController reLoadData];
        
        [_loginDelegate loginViewDidLogin:self];
    }else{
        TDLOGERROR(@"Init error");
        _tfConfirm.text = @"";
        [self showAlertSimple:TDLocStrOne(@"InitUserError") tag:0];
    }
    [al dismiss];
}

-(NSString*)warningFor:(int)time{
    return [NSString stringWithFormat:TDLocStrOne(@"DestructionTurnOnWarn1s"),
            [NSString stringWithFormat:@"%d", time]];
}

-(void)checkDestroyData{
    VASetting *setting = [VAGlobal share].appSetting;
    if (![setting isDestroyDataEnable]) {
        [self showAlertSimple:TDLocStrOne(@"WrongPass")
                      message:@""
                          tag:0];
        return;
    }
    if (_iCountWrongPass < setting.numBeforeDestroyData) {
        [self showAlertSimple:[self warningFor:setting.numBeforeDestroyData - _iCountWrongPass]
                      message:nil
                          tag:0];
    }else{
        TDLOG(@"Destroy data");
        TDViewController *vc = [TDAppDelegate share].viewController;
        [vc destroyData];
    }
}

-(void)login{
    UIAlertView *al = [TDAlert showLoadingMessageWithTitle:TDLocStrOne(@"Loading...") delegate:self];
    VAGlobal *global = [VAGlobal share];
    if ([global.user.sUserPassword isEqualToString:_tfConfirm.text]) {
        [global loadDataAfterLogin];
        [[TDAppDelegate share].viewController reLoadData];
        [_loginDelegate loginViewDidLogin:self];
    }else{
        //wrong pass - clear - show alert
        TDLOG(@"Wrong pass when login");
        _tfConfirm.text = @"";
        _iCountWrongPass +=1;
        [self checkDestroyData];
    }
    [al dismiss];
}
-(void)relogin{
    if ([[VAGlobal share].user.sUserPassword isEqualToString:_tfConfirm.text]) {
        
        [_loginDelegate loginViewDidLogin:self];
    }else{
        //wrong pass
        TDLOG(@"Wrong pass when relogin");
        _tfConfirm.text = @"";
        //[self showAlertSimple:TDLocStrOne(@"WrongPass") tag:0];
        _iCountWrongPass +=1;
        [self checkDestroyData];
    }
}
-(void)changePass{
    VAUser *user = [VAGlobal share].user;
    if (![self checkValidConfirm]) {
        return;
    }
    
    if ([_tfConfirm.text isEqualToString:_tfOrigin.text]) {
        user.sUserPassword = _tfConfirm.text;
        [user updateToDb:[VAGlobal share].dbManager];
        
        [_loginDelegate loginViewDidLogin:self];
    }else{
        //wrong pass
        TDLOG(@"Wrong pass when change password");
        _tfConfirm.text = @"";
        _tfOrigin.text = @"";
        //[self showAlertSimple:TDLocStrOne(@"WrongPass") tag:0];
//        _iCountWrongPass +=1;
//        [self checkDestroyData];
        [self showAlertSimple:TDLocStrOne(@"WrongPass") message:nil tag:0];
    }
}
-(void)checkPassForChange{
    if ([[VAGlobal share].user.sUserPassword isEqualToString:_tfConfirm.text]) {
        VALoginController *vc = [[[VALoginController alloc] init] autorelease];
        if (_typeMasterPass == kTypeMasterPasswordChangePassCheck1) {
            vc.typeMasterPass = kTypeMasterPasswordChangePassCheck2;
        }else{
            vc.typeMasterPass = kTypeMasterPasswordChangePass;
        }
        vc.loginDelegate = self.loginDelegate;
        [self dismissViewControllerAnimated:YES completion:^{
            [_loginDelegate presentModalViewController:vc animated:YES];
        }];
    }else{
        //wrong pass
        TDLOG(@"Wrong pass when relogin");
        _tfConfirm.text = @"";
        //[self showAlertSimple:TDLocStrOne(@"WrongPass") tag:0];
        _iCountWrongPass +=1;
        [self checkDestroyData];
    }
}
- (IBAction)confirm:(id)sender {
    if (_typeMasterPass == kTypeMasterPasswordFirst) { //user app first time
        [self loginFirstTime];
    }else if (_typeMasterPass == kTypeMasterPasswordLogin){
        [self login];
    }else if (_typeMasterPass == kTypeMasterPasswordReLogin){ //login
        [self relogin];
    }else if(_typeMasterPass == kTypeMasterPasswordChangePass){ //changepass
        [self changePass];
    }else{
        [self checkPassForChange];
    }
}
- (IBAction)btBackPressed:(id)sender {
    if ([_loginDelegate respondsToSelector:@selector(loginViewDidCancel:)]) {
        [_loginDelegate loginViewDidCancel:self];
    }
}

#pragma mark - alert
-(void)showAlertSimple:(NSString*)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"" delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles: nil];
    alert.tag = tag;
    [alert show];
}

-(void)showAlertSimple:(NSString*)title message:(NSString*)mess tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:mess delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles: nil];
    alert.tag = tag;
    [alert show];
}
@end
