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


@implementation VAPasswordIDView
- (IBAction)btGenTextId:(id)sender {
    self.selectedField = self.vId;
    [self.delegate genTextFor:self];
}
- (IBAction)btGenTextPassword:(id)sender {
    self.selectedField = self.vPassword;
    [self.delegate genTextFor:self];
}

-(void)dealloc{
    [_vPassword release];
    [_vId release];
    [super dealloc];
}
@end

@interface VAElementViewController ()<VAGenTextDelegate>

@property (retain, nonatomic) UITextField *tfCurrActive;

@property (retain, nonatomic) IBOutlet UIImageView *imIcon;
@property (retain, nonatomic) IBOutlet UIButton *btIcon;


@property (retain, nonatomic) IBOutlet UITextField *tfTitle;
@property (retain, nonatomic) IBOutlet UIButton *btFavorite;

@property (retain, nonatomic) IBOutlet VAPasswordIDView *pwNormal;
@property (nonatomic, retain) VAPasswordIDView *selectPw;
@property (retain, nonatomic) IBOutlet UITextField *tfUrl;
@property (retain, nonatomic) IBOutlet UITextView *tfNote;

@property(nonatomic, retain)  NSString *sIconUrl;
@property(nonatomic, retain) NSString *sImagePath;

- (IBAction)btIconPressed:(id)sender;

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
    [self updateView];
    
}
-(void)updateView{
    [self updateFavoriteStatus];
    _tfNote.text = _currentElement.sNote;
    _sIconUrl = _currentElement.sEIcon;
    _tfUrl.text =  _currentElement.sUrl;
    _sImagePath = _currentElement.sImage;
    _currentElement.dTimeStamp = [[NSDate date] timeIntervalSince1970];
    _tfTitle.text = _currentElement.sTitle ;
    //icon
    UIImage *image = [TDImageEncrypt imageWithName:_sIconUrl];
    if (image) {
        [_btIcon setImage:image forState:UIControlStateNormal];
    }
    
    //pass
    VAPassword* pass = nil;
    if (_currentElement.aPasswords.count == 0) {
    }else{
        pass = [_currentElement.aPasswords objectAtIndex:0];
        _pwNormal.vId.text = pass.sTitleNameId;
        _pwNormal.vPassword.text = pass.sPassword;
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
- (IBAction)btIconPressed:(id)sender {
    VAChooseIconViewController *vc = [[VAChooseIconViewController alloc] initWithNibName:@"VAChooseIconViewController" bundle:nil];
    vc.chooseIcDelegate = self;
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
        VAPassword* pass = nil;
        if (_currentElement.aPasswords.count == 0) {
            pass = [[[VAPassword alloc] init] autorelease];
            pass.elementId = _currentElement;
            [_currentElement.aPasswords addObject:pass];
        }else{
            pass = [_currentElement.aPasswords objectAtIndex:0];
        }
        
        pass.sTitleNameId = _pwNormal.vId.text;
        pass.sPassword = _pwNormal.vPassword.text;
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
    if (image) {
        [_btIcon setImage:image forState:UIControlStateNormal];
        self.sIconUrl = path;
    }
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
    [_sIconUrl release];
    [_imIcon release];
    [_tfTitle release];
    [_btFavorite release];
    [_pwNormal release];
    [_tfUrl release];
    [_tfNote release];
    [_tfCurrActive release];
    [_btIcon release];
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
    [super viewDidUnload];
}

#pragma mark - textfield
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    _tfCurrActive = textField;
}
-(void)textFieldDidEndEditing:(UITextField *)textField{
    _tfCurrActive = nil;
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
    [vc.navigationController popViewControllerAnimated:YES];
}
@end
