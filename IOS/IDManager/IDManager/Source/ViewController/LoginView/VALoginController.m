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

@interface VALoginController ()
@property (assign, nonatomic) int iCountWrongPass;

@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UITextField *tfOrigin;
@property (retain, nonatomic) IBOutlet UITextField *tfConfirm;
@property (retain, nonatomic) IBOutlet UIButton *btConfirm;
@property (retain, nonatomic) IBOutlet UIButton *btChangeKeyboard;
@property (retain, nonatomic) IBOutlet UILabel *lbOrigin;
@property (retain, nonatomic) IBOutlet UILabel *lbConfirm;
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
    switch (_typeMasterPass) {
        case kTypeMasterPasswordFirst:
            _lbOrigin.hidden = YES;
            _lbConfirm.hidden = YES;
            _tfOrigin.hidden = YES;
            [_tfConfirm becomeFirstResponder];
            _lbTitle.text = TDLocalizedStringOne(@"PasswordMaster");
            break;
            
        //login and relogin
        case kTypeMasterPasswordLogin:
        case kTypeMasterPasswordReLogin:
            _lbOrigin.hidden = YES;
            _lbConfirm.hidden = YES;
            _tfOrigin.hidden = YES;
            [_tfConfirm becomeFirstResponder];
            _lbTitle.text = TDLocalizedStringOne(@"PasswordLogin");
            break;

        case kTypeMasterPasswordChangePass:
            _lbConfirm.hidden = NO;
            _lbOrigin.hidden = NO;
            _tfOrigin.hidden = NO;
            [_tfOrigin becomeFirstResponder];
            _lbTitle.text = TDLocalizedStringOne(@"PasswordNew");
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
    
}
-(void)keyboardWillChangeFrame:(NSNotification*)notifi{
    NSValue *keyboardBoundsValue = [[notifi userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardFrame;
    [keyboardBoundsValue getValue:&keyboardFrame];
    keyboardFrame = [self.view convertRect:keyboardFrame toView:self.view];
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
    [tf resignFirstResponder];
    [tf becomeFirstResponder];
}
- (IBAction)changeKeyboard:(id)sender {
    [self changeKeyboardFor:_tfOrigin];
    [self changeKeyboardFor:_tfConfirm];
    if (_tfConfirm.isEditing) {
        [self reAssignFor:_tfConfirm];
    }else if (_tfOrigin.isEditing){
        [self reAssignFor:_tfOrigin];
    }
}

-(BOOL)isValidPasswordString:(NSString*)str{
    return [str isNotEmpty];
}
-(void)loginFirstTime{
    VAGlobal *global = [VAGlobal share];
    NSString *masterPass = _tfConfirm.text;
    if ([self isValidPasswordString:masterPass]) {
        global.user.sUserPassword = masterPass;
        [global initFirstDatabase];
        if ([global insertCurrentUser]) {
            TDLOG(@"Init complete");
            global.appSetting.isFirstUse = NO;
            [global.appSetting saveSetting];
            [global loadDataAfterLogin];
            [[TDAppDelegate share].viewController reLoadData];
            [self.navigationController popViewControllerAnimated:YES];
        }else{
            TDLOGERROR(@"Init error");
            _tfConfirm.text = @"";
            [self showAlertSimple:TDLocalizedStringOne(@"InitUserError") tag:0];
        }
        
    }else{
        //pass error
        TDLOGERROR(@"enter password error");
        _tfConfirm.text = @"";
        [self showAlertSimple:TDLocalizedStringOne(@"InvalidPass") tag:0];
    }
}
-(void)login{
    VAGlobal *global = [VAGlobal share];
    if ([global.user.sUserPassword isEqualToString:_tfConfirm.text]) {
        [global loadDataAfterLogin];
        [[TDAppDelegate share].viewController reLoadData];
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        //wrong pass - clear - show alert
        TDLOG(@"Wrong pass when login");
        _tfConfirm.text = @"";
        [self showAlertSimple:TDLocalizedStringOne(@"WrongPass") tag:0];
        _iCountWrongPass +=1;
    }
}
-(void)relogin{
    if ([[VAGlobal share].user.sUserPassword isEqualToString:_tfConfirm.text]) {
         [self.navigationController popViewControllerAnimated:YES];
    }else{
        //wrong pass
        TDLOG(@"Wrong pass when relogin");
        _tfConfirm.text = @"";
        [self showAlertSimple:TDLocalizedStringOne(@"WrongPass") tag:0];
        _iCountWrongPass +=1;
    }
}
-(void)changePass{
    VAUser *user = [VAGlobal share].user;
    if ([user.sUserPassword isEqualToString:_tfOrigin.text]) {
        user.sUserPassword = _tfConfirm.text;
        [user updateToDb:[VAGlobal share].dbManager];
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        //wrong pass
        TDLOG(@"Wrong pass when change password");
        _tfConfirm.text = @"";
        _tfOrigin.text = @"";
        [self showAlertSimple:TDLocalizedStringOne(@"WrongPass") tag:0];
        _iCountWrongPass +=1;
    }
}
- (IBAction)confirm:(id)sender {
    if (_typeMasterPass == kTypeMasterPasswordFirst) { //user app first time
        [self loginFirstTime];
    }else if (_typeMasterPass == kTypeMasterPasswordLogin){
        [self login];
    }else if (_typeMasterPass == kTypeMasterPasswordLogin){ //login
        [self relogin];
    }else{ //changepass
        [self changePass];
    }
}
#pragma mark - alert
-(void)showAlertSimple:(NSString*)title tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:@"" delegate:self cancelButtonTitle:TDLocalizedStringOne(@"OK") otherButtonTitles: nil];
    alert.tag = tag;
    [alert show];
}

@end
