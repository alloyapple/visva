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

@implementation VAPasswordIDView

-(void)dealloc{
    [_vPassword release];
    [_vId release];
    [super dealloc];
}
@end

@interface VAElementViewController ()

@property (retain, nonatomic) UITextField *tfCurrActive;

@property (retain, nonatomic) IBOutlet UIImageView *imIcon;
@property (retain, nonatomic) IBOutlet UITextField *tfTitle;
@property (retain, nonatomic) IBOutlet UIButton *btFavorite;

@property (retain, nonatomic) IBOutlet VAPasswordIDView *pwNormal;
@property (retain, nonatomic) IBOutlet UITextField *tfUrl;
@property (retain, nonatomic) IBOutlet UITextView *tfNote;

@property(nonatomic, retain)  NSString *sIconUrl;
@property(nonatomic, retain) NSString *sImagePath;


- (IBAction)btFavoritePressed:(id)sender;
- (IBAction)btUrlPressed:(id)sender;

- (IBAction)btBackPressed:(id)sender;
- (IBAction)btInfoPressed:(id)sender;
- (IBAction)touchOnBg:(id)sender;

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
    [self updateFavoriteStatus];
    
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
    _currentElement.sTitle = _tfTitle.text;
    if ([_currentElement.sTitle isNotEmpty]) {
        _currentElement.sNote = _tfNote.text;
        _currentElement.sEIcon = _sIconUrl;
        _currentElement.sUrl = _tfUrl.text;
        _currentElement.sImage = _sImagePath;
        _currentElement.dTimeStamp = [[NSDate date] timeIntervalSince1970];
        //pass
        VAPassword* pass;
        if (_currentElement.aPasswords.count == 0) {
            pass = [[[VAPassword alloc] init] autorelease];
            pass.elementId = _currentElement;
            [_currentElement.aPasswords addObject:pass];
        }else{
            [_currentElement.aPasswords objectAtIndex:0];
        }
        
        pass.sTitleNameId = _pwNormal.vId.text;
        pass.sPassword = _pwNormal.vPassword.text;
        [self.elementDelegate elementViewDidSave:self];
    }else{
        [self.elementDelegate elementViewDidCancel:self];
    }
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
    [_sIconUrl release];
    [_imIcon release];
    [_tfTitle release];
    [_btFavorite release];
    [_pwNormal release];
    [_tfUrl release];
    [_tfNote release];
    [_tfCurrActive release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setImIcon:nil];
    [self setTfTitle:nil];
    [self setBtFavorite:nil];
    [self setPwNormal:nil];
    [self setTfUrl:nil];
    [self setTfNote:nil];
    [super viewDidUnload];
}

#pragma mark - textfield
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    _tfCurrActive = textField;
}
-(void)textFieldDidEndEditing:(UITextField *)textField{
    _tfCurrActive = nil;
}
@end
