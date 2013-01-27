//
//  VALoginController.m
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VALoginController.h"
#import "TDCommonLibs.h"

@interface VALoginController ()
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
        case kTypeMasterPasswordLogin:
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

- (IBAction)confirm:(id)sender {
    
}
@end
