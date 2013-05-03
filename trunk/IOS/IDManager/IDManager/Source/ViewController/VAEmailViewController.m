//
//  VAEmailViewController.m
//  IDManager
//
//  Created by tranduc on 3/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAEmailViewController.h"
#import "VAGlobal.h"
#import "TDAlert.h"
#import "TDCommonLibs.h"
#import "TDString.h"
#import "TDAlert.h"

@interface VAEmailViewController ()
@property (retain, nonatomic) IBOutlet UITextField *tfEmail;
@property (retain, nonatomic) IBOutlet UIButton *btBack;
@property (retain, nonatomic) IBOutlet UILabel *lbTitile;
@property (retain, nonatomic) IBOutlet UILabel *lbEmail;
@property (retain, nonatomic) IBOutlet UIButton *btNext;
@property (retain, nonatomic) IBOutlet UILabel *lbInfo;

@end

@implementation VAEmailViewController

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
    if (_type == kTypeEmailVCRegister) {
        _btBack.hidden = YES;
        _lbTitile.text = TDLocStrOne(@"EmailRegister");

    }else{
        _lbTitile.text = TDLocStrOne(@"ChangeEmail");
    }
    VAUser *user = [VAGlobal share].user;
    _tfEmail.text = user.sEmail;
    [_btNext setTitle:TDLocStrOne(@"Next") forState:UIControlStateNormal];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL)isValidEmail:(NSString*)str
{
    if (str == nil) {
        return NO;
    }
    if ([str rangeOfString:@"@"].location == NSNotFound) {
        return NO;
    }
    return YES;
}
#define kTageAlertEmailChanged 1043
-(void)changeEmail:(NSString*)str{
    VAUser *user = [VAGlobal share].user;
    user.sEmail = str;
    [user updateToDb:[VAGlobal share].dbManager];
    //[self dismissModalViewControllerAnimated:YES];
    if (_type == kTypeEmailVCChange) {
        [TDAlert showMessageWithTitle:TDLocStrOne(@"EmailSetted") message:nil delegate:self otherButton:nil tag:kTageAlertEmailChanged];
    }else{
        [self dismissModalViewControllerAnimated:YES];
    }

}
#define kTagAlertEmail 1041
- (IBAction)btNextpressed:(id)sender {
    NSString *str = _tfEmail.text;
    if ([self isValidEmail:str]) {
        VAUser *user = [VAGlobal share].user;
        if ([user.sEmail isNotEmpty]) {
            [TDAlert showMessageWithTitle:TDLocStrOne(@"EmailWarning") message:nil delegate:self otherButton:TDLocStrOne(@"Cancel") tag:kTagAlertEmail];
        }else{
            [self changeEmail:str];
        }
    }else{
        [TDAlert showMessageWithTitle:TDLocStrOne(@"EmailError") message:nil delegate:self];
    }
}
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == kTagAlertEmail) {
        if (buttonIndex == 0) {
            [self changeEmail:_tfEmail.text];
        }
    }else if (alertView.tag == kTageAlertEmailChanged){
        [self dismissModalViewControllerAnimated:YES];
    }
}


- (IBAction)btBackPressed:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
- (void)dealloc {
    [_tfEmail release];
    [_btBack release];
    [_lbTitile release];
    [_lbEmail release];
    [_btNext release];
    [_lbInfo release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setTfEmail:nil];
    [self setBtBack:nil];
    [self setLbTitile:nil];
    [self setLbEmail:nil];
    [self setBtNext:nil];
    [self setLbInfo:nil];
    [super viewDidUnload];
}
@end
