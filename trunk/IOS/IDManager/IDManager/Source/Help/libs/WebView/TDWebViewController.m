//
//  TDWebViewController.m
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDWebViewController.h"
#import "TDCommonLibs.h"

@interface TDWebViewController ()
@property (retain, nonatomic) IBOutlet UIView *vBottom;
@property (retain, nonatomic) IBOutlet UIView *vTop;
@property (retain, nonatomic) IBOutlet UITextField *tfSearchEngine;
@property (retain, nonatomic) IBOutlet UIButton *btBackward;
@property (retain, nonatomic) IBOutlet UIButton *btForward;
@property (retain, nonatomic) IBOutlet UITextField *tfUrl;



- (IBAction)btBackPressed:(id)sender;

- (IBAction)btBackwardPressed:(id)sender;
- (IBAction)btForwardPressed:(id)sender;

@end

@implementation TDWebViewController

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
    [self gotoPage:_sUrlStart];
}
-(void)gotoPage:(NSString *)urlstr{
    //urlstr = [urlstr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:urlstr];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    [_wvContent loadRequest:request];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_wvContent release];
    [_vBottom release];
    [_vTop release];
    [_tfSearchEngine release];
    [_btBackward release];
    [_btForward release];
    [_tfUrl release];
    
    [_sUrlStart release];
    [_sUrl release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setWvContent:nil];
    [self setVBottom:nil];
    [self setVTop:nil];
    [self setTfSearchEngine:nil];
    [self setBtBackward:nil];
    [self setBtForward:nil];
    [self setTfUrl:nil];
    [super viewDidUnload];
}
- (IBAction)btBackPressed:(id)sender {
    [self.webDelegate browserBack:self];
}

- (IBAction)btBackwardPressed:(id)sender {
    if ([_wvContent canGoBack]) {
        [_wvContent goBack];
    }
}

- (IBAction)btForwardPressed:(id)sender {
    if ([_wvContent canGoForward]) {
        [_wvContent goForward];
    }
}
-(void)checkAvailable{
    if ([_wvContent canGoForward]) {
        _btForward.enabled = YES;
    }else{
        _btForward.enabled = NO;
    }
    if ([_wvContent canGoBack]) {
        _btBackward.enabled = YES;
    }else{
        _btBackward.enabled = NO;
    }
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    return YES;
}
- (void)webViewDidStartLoad:(UIWebView *)webView{
    self.sUrl = [NSString stringWithFormat:@"%@", webView.request.URL];
    self.tfUrl.text = _sUrl;
    
}
- (void)webViewDidFinishLoad:(UIWebView *)webView{
    self.sUrl = [NSString stringWithFormat:@"%@", webView.request.URL];
    self.tfUrl.text = _sUrl;
    [self checkAvailable];
}
- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    NSString *mess = [NSString stringWithFormat:@"%@", error];
    [self showAlertSimple:TDLocalizedStringOne(@"Error") mess: mess tag:0];
}

#pragma mark - text field
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    if (textField == _tfSearchEngine) {
        textField.text = @"";
    }
}

-(void)googleSearch:(NSString*)str{
    str = [str stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSString *url = [NSString stringWithFormat:@"https://www.google.com.vn/#hl=en&output=search&q=%@", str];
    [self gotoPage:url];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    if (textField == _tfSearchEngine) {
        [self googleSearch:_tfSearchEngine.text];
        return YES;
    }else if(textField == _tfUrl){
        NSString *url = _tfUrl.text;
        [self gotoPage:url];
        return YES;
    }
    return YES;
}

#pragma mark - alert
-(void)showAlertSimple:(NSString*)title mess:(NSString*)mess tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:mess delegate:self cancelButtonTitle:TDLocalizedStringOne(@"OK") otherButtonTitles: nil];
    alert.tag = tag;
    [alert show];
}
@end
