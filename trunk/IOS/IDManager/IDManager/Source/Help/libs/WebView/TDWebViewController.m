//
//  TDWebViewController.m
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDWebViewController.h"
#import "TDCommonLibs.h"
#import "MPAnimation.h"
#import "VAPassword.h"
#import <UIKit/UIGestureRecognizerSubclass.h>
#import "CGPointUtils.h"

@interface UIView(TDText)
-(NSMutableArray*)getListTextField;
@end

@implementation UIView(TDText)

-(NSMutableArray*)getListTextField{
    NSMutableArray *arr = [NSMutableArray array];
    for (UIView *v in self.subviews) {
        if ([v respondsToSelector:@selector(textInRange:)]) {
            [arr addObject:v];
        }else{
            [arr addObjectsFromArray:[v getListTextField]];
        }
    }
    
    return arr;
}
-(UIView*)mostParentView{
    if (self.superview) {
        return [self.superview mostParentView];
    }
    return self;
}

@end


@implementation TDRotateGesture
-(id)initWithTarget:(id)target action:(SEL)action{
    if (self = [super initWithTarget:target action:action]) {
        _angleStep = 80;
        _minRadiusFactor = 0.1;
        _maxRadiusFactor = 0.9;
        _centerPoint = (CGPoint){0.5, 0.5};
    }
    return self;
}
-(BOOL)isValidPos:(CGPoint)pos{
    float distanceToCenter = distanceBetweenPoints(_centerPoint, pos);
    if ((distanceToCenter >= _minRadiusFactor *_radius)
        &&(distanceToCenter <= _maxRadiusFactor * _radius)) {
        return YES;
    }
    return NO;
}
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesBegan:touches withEvent:event];
    _centerPoint = CGPointMake(_rect.origin.x + _rect.size.width*_centerFactor.x,
                               _rect.origin.y + _rect.size.height*_centerFactor.y);
    _radius = distanceBetweenPoints(_centerPoint, _rect.origin);
    
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self.view];
    if (![self isValidPos:point]) {
        self.state = UIGestureRecognizerStateFailed;
        return;
    }
    _beginPoint = point;
    _minCheckLen = _rect.size.width/2;
    
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesMoved:touches withEvent:event];
    UITouch *touch = [touches anyObject];
    CGPoint currentPoint = [touch locationInView:self.view];
    if (![self isValidPos:currentPoint]) {
        
        self.state = UIGestureRecognizerStateFailed;
        return;
    }
    
    float rotate = angleSignBetweenLines(_centerPoint, _beginPoint,
                                     _centerPoint, currentPoint);
    //TDLOG(@"rotate = %f", rotate);
    
    if (fabs(rotate)> _angleStep) {
        if (rotate>0) {
            _isClockWise = YES;
        }else{
            _isClockWise = NO;
        }
        self.state = UIGestureRecognizerStateEnded;
    }else{
        self.state = UIGestureRecognizerStateChanged;
    }
    
    /*
     
    if (fabs(angle)> kMinimumCircleAngle &&
        fabs(angle)<kMinimumCircleAngle &&
        _lineLengthSoFar > _minCheckLen) {
        self.state = UIGestureRecognizerStateEnded;
    }else{
        self.state = UIGestureRecognizerStateChanged;
    }*/
    
}
@end

@interface TDWebViewController (){
    int _numField;
    int _currentField;
    int _numPass;
    int _currentPass;
}
@property (retain, nonatomic) IBOutlet UIView *vBottom;
@property (retain, nonatomic) IBOutlet UIView *vTop;
@property (retain, nonatomic) IBOutlet UITextField *tfSearchEngine;
@property (retain, nonatomic) IBOutlet UIButton *btBackward;
@property (retain, nonatomic) IBOutlet UIButton *btForward;
@property (retain, nonatomic) IBOutlet UITextField *tfUrl;
@property (retain, nonatomic) IBOutlet UIButton *btScreenShot;
@property (retain, nonatomic) IBOutlet UIButton *btReload;


- (IBAction)btScreenShotPress:(id)sender;
- (IBAction)btReloadPressed:(id)sender;

- (IBAction)btBackPressed:(id)sender;

- (IBAction)btBackwardPressed:(id)sender;
- (IBAction)btForwardPressed:(id)sender;

//jogDial
@property (retain, nonatomic) IBOutlet UIView *vJogDial;
@property (retain, nonatomic) IBOutlet UIButton *btJogCenter;
@property (retain, nonatomic) IBOutlet UIButton *btJogNote;
@property (retain, nonatomic) IBOutlet UIButton *btJogKeyBoard;
@property (retain, nonatomic) IBOutlet UIScrollView *svScrollView;

@property (retain, nonatomic) IBOutlet UIView *vScrollViewContent;

@end

@implementation TDWebViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        _bIsTakeScreenShot = NO;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    
    _tfUrl.text = self.sUrlStart;
    [self gotoPage:_sUrlStart];
    if (_bIsTakeScreenShot) {
        _btScreenShot.hidden = NO;
        _vIconChoose.hidden = NO;
        _vIconChoose.layer.borderWidth = 2;
        _vIconChoose.layer.borderColor = [[UIColor redColor] CGColor];
        UIPanGestureRecognizer *panGesture = [[[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panView:)] autorelease];
        panGesture.maximumNumberOfTouches = 1;
        panGesture.minimumNumberOfTouches = 1;
        [_vIconChoose addGestureRecognizer:panGesture];
    }else{
        _btScreenShot.hidden = YES;
        _vIconChoose.hidden = YES;
    }
    
    TDRotateGesture *gesture = [[[TDRotateGesture alloc] initWithTarget:self action:@selector(touchPan:)] autorelease];
    gesture.rect = CGRectMake(0, 0,
                                 _vJogDial.frame.size.width,
                                 _vJogDial.frame.size.height);
    gesture.centerFactor = (CGPoint){0.5,303.0f/523.0f};
    gesture.minRadiusFactor = 6.0f/303.0f;
    gesture.maxRadiusFactor = 193.0f/303.0f;
    [_vJogDial addGestureRecognizer:gesture];
}
-(void)panView:(UIPanGestureRecognizer*)gesture{
    if (gesture.state == UIGestureRecognizerStateChanged) {
        CGPoint pos = [gesture translationInView:gesture.view];
        
        //TDLOG(@"translate = %f, %f", pos.x, pos.y);
        CGRect oldFrame = _vIconChoose.frame;
        CGPoint des = CGPointMake(pos.x + oldFrame.origin.x, pos.y + oldFrame.origin.y);
        if (des.x<0) {
            des.x = 0;
        }else if(des.x+oldFrame.size.width > _wvContent.frame.size.width){
            des.x = _wvContent.frame.size.width - oldFrame.size.width;
        }
        if (des.y<_wvContent.frame.origin.y) {
            des.y = _wvContent.frame.origin.y;
        }else if(des.y+oldFrame.size.height > _wvContent.frame.origin.y + _wvContent.frame.size.height){
            des.y = _wvContent.frame.origin.y + _wvContent.frame.size.height - oldFrame.size.height;
        }
        oldFrame.origin = des;
        _vIconChoose.frame = oldFrame;
        [gesture setTranslation:CGPointMake(0, 0) inView:gesture.view];
    }
}
-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    _vJogDial.hidden = NO;
    [self registerKeyboard];
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    _vJogDial.hidden = YES;
    [[NSNotificationCenter defaultCenter] removeObserver:self];
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
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [_wvContent release];
    [_vBottom release];
    [_vTop release];
    [_tfSearchEngine release];
    [_btBackward release];
    [_btForward release];
    [_tfUrl release];
    
    [_sUrlStart release];
    [_sUrl release];
    [_btScreenShot release];
    [_btReload release];
    [_vJogDial release];
    [_btJogCenter release];
    [_btJogNote release];
    [_btJogKeyBoard release];
    [_svScrollView release];
    [_vScrollViewContent release];
    [_vIconChoose release];
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
    [self setBtScreenShot:nil];
    [self setBtReload:nil];
    [self setVJogDial:nil];
    [self setBtJogCenter:nil];
    [self setBtJogNote:nil];
    [self setBtJogKeyBoard:nil];
    [self setSvScrollView:nil];
    [self setVScrollViewContent:nil];
    [self setVIconChoose:nil];
    [super viewDidUnload];
}
- (IBAction)btScreenShotPress:(id)sender {
    CGRect rect = [_wvContent convertRect:_vIconChoose.frame fromView:_vIconChoose.superview];
    self.screenShot = [MPAnimation renderImageFromView:_wvContent withRect:rect];
    [_webDelegate browserBack:self];
}

- (IBAction)btReloadPressed:(id)sender {
    if (_wvContent.isLoading) {
        
    }else{
        [_wvContent reload];
    }
}

- (IBAction)btBackPressed:(id)sender {
    [_vJogDial removeFromSuperview];
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
    [self showJogDial];
}
- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    NSString *mess = [NSString stringWithFormat:@"%@", error];
    [self showAlertSimple:TDLocStrOne(@"Error") mess: mess tag:0];
}
#pragma mark - jogDial
- (IBAction)btKeyboardPressed:(id)sender {
    [_vJogDial removeFromSuperview];
}

-(void)viewDidLayoutSubviews{
    _vJogDial.frame = (CGRect){0, _vJogDial.superview.frame.size.height - _vJogDial.frame.size.height,
        _vJogDial.frame.size.width, _vJogDial.frame.size.height};
}
-(NSString *)focusField:(int)index{
    NSString *format = @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email]\");tdkmFields.item(%d).focus();";
    NSString *script = [NSString stringWithFormat:format, index];
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
-(NSString *)focusPass:(int)index{
    NSString *format = @"tdkmPass=document.querySelectorAll(\"input[type='password']\");tdkmPass.length;tdkmPass.item(%d).focus();";
    NSString *script = [NSString stringWithFormat:format, index];
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
-(NSString *)setField:(NSString *)value index:(int)i{
    NSString *format = @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email]\");tdkmFields.item(%d).value=\"%@\";";
    NSString *script = [NSString stringWithFormat:format, i, value];
    TDLOG(@"script=%@",script);
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
-(NSString *)setPass:(NSString *)value index:(int)i{
    NSString *format = @"tdkmPass=document.querySelectorAll(\"input[type='password']\");tdkmPass.length;tdkmPass.item(%d).value=\"%@\";";
    NSString *script = [NSString stringWithFormat:format, i, value];
    TDLOG(@"script=%@",script);
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}

-(void)showJogDial
{
    if (!_bIsUseJogDial) {
        return;
    }
    if (_wvContent.isLoading) {
        return;
    }
    
    //create js strings
    NSString *getFields =
    @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email]\");tdkmFields.length;";
    
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:getFields];
    if (str != nil) {
        _numField = [str intValue];
    }else{
        _numField = 0;
    }
    
    NSString *getPasss =
    @"tdkmPass=document.querySelectorAll(\"input[type='password']\");tdkmPass.length;";
    
    str = [self.wvContent stringByEvaluatingJavaScriptFromString:getPasss];
    if (str != nil) {
        _numPass = [str intValue];
    }else{
        _numPass = 0;
    }
    
    if (_numField >0) {
        [self focusField:0];
    }else if(_numPass >0){
        [self focusPass:0];
    }


    
    [[_wvContent mostParentView] addSubview:_vJogDial ];
    
    _vJogDial.frame = (CGRect){0, _vJogDial.superview.frame.size.height - _vJogDial.frame.size.height,
        _vJogDial.frame.size.width, _vJogDial.frame.size.height};
    NSArray *arr = _vScrollViewContent.subviews;
    for (UIView *v in arr) {
        [v removeFromSuperview];
    }
    float width = 60, height = _vScrollViewContent.frame.size.height/2;
    float mul = 1;
    for (int i=0; i<_listPWID.count*mul; i++){
        VAPassword *pass = [_listPWID objectAtIndex:i/mul];
        UIButton *btID = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [btID setTitle:pass.sTitleNameId forState:UIControlStateNormal];
        btID.frame = (CGRect){i*width, 0, width, height};
        
        UIButton *btPassword = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [btPassword setTitle:pass.sPassword forState:UIControlStateNormal];
        btPassword.frame = (CGRect){i*width, height, width, height};
        [_vScrollViewContent addSubview:btID];
        [_vScrollViewContent addSubview:btPassword];
    }
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0,0.0, 0.0,
                                                 _listPWID.count * width *mul);
    _svScrollView.contentInset = contentInsets;
    _svScrollView.contentSize = (CGSize){ 0, 0};
    
//    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0,0.0, 0.0,
//                                                  0.0);
//    _svScrollView.contentInset = contentInsets;
//    _svScrollView.contentSize = (CGSize){ 0, _listPWID.count * width *mul};
    
    CGRect f = _vScrollViewContent.frame;
    _vScrollViewContent.frame = (CGRect){f.origin.x, f.origin.y,
        _listPWID.count * width *mul, f.size.height};
    _svScrollView.scrollIndicatorInsets = contentInsets;
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
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:mess delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles: nil];
    alert.tag = tag;
    [alert show];
}
#pragma mark - gesture
-(void)touchPan:(TDRotateGesture*)gesture{
    CGPoint pos = [gesture locationInView:gesture.view];
    if (gesture.state == UIGestureRecognizerStateEnded) {
        TDLOG(@"JogActive %f, %f", pos.x, pos.y);
        if (gesture.isClockWise) {
            if (_currentField >= MIN(_listPWID.count, _numField)) {
                return;
            }
        }else{
            if (_currentField<0) {
                return;
            }
        }
        [self setField:[[_listPWID objectAtIndex:_currentField] sTitleNameId] index:_currentField];
        [self setPass:[[_listPWID objectAtIndex:_currentField] sPassword]
                index:_currentField];
        [TDSoundManager playShortEffectWithFile:@"jogwheel.caf"];
        
        if (gesture.isClockWise) {
            if (_currentField+1 >= MIN(_listPWID.count, _numField)) {
                return;
            }
            _currentField++;
            
        }else{
            if (_currentField-1 <0) {
                return;
            }
            _currentField--;
        }
        [self focusField:_currentField];
    }else{
        //TDLOG(@"Change %f, %f", pos.x, pos.y);
    }
    
}

#pragma mark - keyboard

-(void)registerKeyboard{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShown:)
                                                 name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];

}
- (UIView*) findKeyboard:(UIView *) superView;
{
    UIView *currentView;
    if ([superView.subviews count] > 0) {
        for(int i = 0; i < [superView.subviews count]; i++){
            
            currentView = [superView.subviews objectAtIndex:i];
            NSLog(@"%@",[currentView description]);
            if([[currentView description] hasPrefix:@"<UIKeyboard"] == YES){
                
                return currentView;
            }
            return ([self findKeyboard:currentView]);
        }
    }
    
    return nil;
    
}
-(UIView*) checkKeyBoard {
    UIWindow* tempWindow;
    
    for(int c = [[[UIApplication sharedApplication] windows] count]-1; c>=0; c--){
        tempWindow = [[[UIApplication sharedApplication] windows] objectAtIndex:c];
        UIView *v = [self findKeyboard:tempWindow];
        if (v){
            TDLOG(@"Finally, I found it");
            return v;
        }
    }
    return nil;
}
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    TDLOG(@"supper = %@", _vJogDial.superview);
}

// Called when the UIKeyboardWillHideNotification is sent
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    
}

- (void)keyboardWillShown:(NSNotification*)aNotification{
    UIView *v = [[[UIApplication sharedApplication] windows] lastObject];
    [v addSubview:_vJogDial];

}
@end
