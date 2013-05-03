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
#import "TDAlert.h"

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
        _angleStep = 60;
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
    _isValidTouch = YES;
    if (![self isValidPos:point]) {
        _isValidTouch = NO;
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
        _isValidTouch = NO;
        self.state = UIGestureRecognizerStateFailed;
        return;
    }
    
    float rotate = angleSignBetweenLines(_centerPoint, _beginPoint,
                                     _centerPoint, currentPoint);
    TDLOG(@"rotate = %f, pos = (%f,%f),cen = (%f,%f)", rotate, currentPoint.x, currentPoint.y,_centerPoint.x, _centerPoint.y);
    
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
-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
    if (self.state == UIGestureRecognizerStateBegan || !_isValidTouch) {
        self.state = UIGestureRecognizerStateFailed;
    }
}
-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    if (self.state == UIGestureRecognizerStateBegan || !_isValidTouch) {
        self.state = UIGestureRecognizerStateFailed;
    }
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
@property (retain, nonatomic) IBOutlet UIView *vJogCircle;

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
    gesture.delegate = self;
    gesture.rect = CGRectMake(0, 0,
                                 _vJogCircle.frame.size.width,
                                 _vJogCircle.frame.size.height);
    gesture.centerFactor = (CGPoint){0.5,0.5};
    //gesture.minRadiusFactor = 6.0f/303.0f;
    gesture.minRadiusFactor = 36.0f/186.0f;
    gesture.maxRadiusFactor = 1.2;
    //gesture.maxRadiusFactor = 193.0f/303.0f;
    [_vJogCircle addGestureRecognizer:gesture];
    _vJogDial.hidden = NO;
    
    if (_bIsUseJogDial) {
        _btScreenShot.hidden = NO;
        [_btScreenShot setImage:[UIImage imageNamed:@"wheel-icon.png"] forState:UIControlStateNormal];
        [_btScreenShot setImage:[UIImage imageNamed:@"wheel-icon_push.png"] forState:UIControlStateSelected];
        [_btScreenShot setImage:nil forState:UIControlStateHighlighted];
        
    }
    
}


-(BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer{
    if (gestureRecognizer.view == _vJogDial) {
        CGPoint pos = [gestureRecognizer locationInView:gestureRecognizer.view];
        CGRect frame = _btJogCenter.frame;
        if (CGRectContainsPoint(frame, pos)) {
            return NO;
        }
        return YES;
    }
    return YES;
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
    
    //[self registerKeyboard];
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
    TDLOGFUNCS();
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [_sNote release];
    [_listPWID release];
    [_screenShot release];
    [_sUrlStart release];
    [_sUrl release];
    
    [_wvContent release];
    [_vBottom release];
    [_vTop release];
    [_tfSearchEngine release];
    [_btBackward release];
    [_btForward release];
    [_tfUrl release];
    
    
    [_btScreenShot release];
    [_btReload release];
    [_vJogDial release];
    [_btJogCenter release];
    [_btJogNote release];
    [_btJogKeyBoard release];
    [_svScrollView release];
    [_vScrollViewContent release];
    [_vIconChoose release];
    [_vJogCircle release];
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
    [self setVJogCircle:nil];
    [super viewDidUnload];
}
- (IBAction)btScreenShotPress:(id)sender {
    if (_bIsTakeScreenShot) {
        CGRect rect = [_wvContent convertRect:_vIconChoose.frame fromView:_vIconChoose.superview];
        self.screenShot = [MPAnimation renderImageFromView:_wvContent withRect:rect];
        [_webDelegate browserBack:self];
    }else if (_bIsUseJogDial){
        [_btScreenShot setSelected:!_btScreenShot.isSelected];
        if (_btScreenShot.isSelected) {
            [self showJogDial];
        }else{
            [self hideJogDial];
        }
    }
    
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
    //[self showJogDial];
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
    NSString *format = @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email],input[type='password']\");tdit=tdkmFields.item(%d);tdit.focus();tdit.scrollIntoView();";
    NSString *script = [NSString stringWithFormat:format, index];
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
/*
-(NSString *)focusPass:(int)index{
    NSString *format = @"tdkmPass=document.querySelectorAll(\"input[type='password']\");tdkmPass.length;tdkmPass.item(%d).focus();";
    NSString *script = [NSString stringWithFormat:format, index];
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}*/
-(NSString *)setField:(NSString *)value index:(int)i{
    NSString *format = @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email],input[type='password']\");tdkmFields.item(%d).value=\"%@\";";
    NSString *script = [NSString stringWithFormat:format, i, value];
    TDLOG(@"script=%@",script);
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
/*
-(NSString *)setPass:(NSString *)value index:(int)i{
    NSString *format = @"tdkmPass=document.querySelectorAll(\"input[type='password']\");tdkmPass.length;tdkmPass.item(%d).value=\"%@\";";
    NSString *script = [NSString stringWithFormat:format, i, value];
    TDLOG(@"script=%@",script);
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:script];
    return str;
}
*/

-(void)hideJogDial{
    _vJogDial.hidden = YES;
    [_btScreenShot setSelected:NO];
}
-(void)showJogDial
{
    if (!_bIsUseJogDial) {
        return;
    }
    if (_wvContent.isLoading) {
        return;
    }
    _vJogDial.hidden = NO;
    //create js strings
    NSString *getFields =
    @"tdkmFields=document.querySelectorAll(\"input[type=text],input[type=email],input[type='password']\");tdkmFields.length;";
    
    NSString *str = [self.wvContent stringByEvaluatingJavaScriptFromString:getFields];
    if (str != nil) {
        _numField = [str intValue];
    }else{
        _numField = 0;
    }
    
    if (_numField >0) {
        [self focusField:0];
    }
    
    [[_wvContent mostParentView] addSubview:_vJogDial ];
    
    _vJogDial.frame = (CGRect){0, _vJogDial.superview.frame.size.height - _vJogDial.frame.size.height,
        _vJogDial.frame.size.width, _vJogDial.frame.size.height};
    NSArray *arr = _vScrollViewContent.subviews;
    for (UIView *v in arr) {
        [v removeFromSuperview];
    }
    
    float width = 120, height = _vScrollViewContent.frame.size.height/2;
    float mul = 1;
    
    UIImage *bgImage = [UIImage imageNamed:@"jog_upperswitch.png"];
    for (int i=0; i<_listPWID.count*mul; i++){
        VAPassword *pass = [_listPWID objectAtIndex:i/mul];
        UILabel *btID = [[[UILabel alloc] init] autorelease];
        [btID setText:pass.sTitleNameId];
        btID.backgroundColor = [UIColor clearColor];
        btID.textAlignment = NSTextAlignmentCenter;
        //[btID setTitle:pass.sTitleNameId forState:UIControlStateNormal];
        //btID.frame = (CGRect){i*width, 0, width, height};
        btID.frame = (CGRect){0, 0, width-10, height};
        
        UILabel *btPassword = [[[UILabel alloc] init] autorelease];
        btPassword.text = pass.sPassword;
        //[btPassword setTitle:pass.sPassword forState:UIControlStateNormal];
        //btPassword.frame = (CGRect){i*width, height, width, height};
        btPassword.frame = (CGRect){0, height, width-10, height};
        btPassword.backgroundColor = [UIColor clearColor];
        btPassword.textAlignment = NSTextAlignmentCenter;
        
        CGRect frame = (CGRect){i*width, 0, width-10, height*2};
        UIButton *v = [[[UIButton alloc] initWithFrame:frame] autorelease];
        [v setBackgroundImage:bgImage forState:UIControlStateNormal];
        [_vScrollViewContent addSubview:v];
        [v addSubview:btID];
        [v addSubview:btPassword];
        
        [v addSubview:btID];
        [v addSubview:btPassword];
        
        //[_vScrollViewContent addSubview:btID];
        //[_vScrollViewContent addSubview:btPassword];
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
            if (_currentField < _listPWID.count) {
                [self setField:[[_listPWID objectAtIndex:_currentField] sPassword] index:_currentField];
            }
            
            [self gotoNexField:YES circle:NO];
        }else{
            [self setField:@"" index:_currentField];
            [self gotoNexField:NO circle:NO];
        }
        [TDSoundManager playShortEffectWithFile:@"jogwheel.caf"];
        [self focusField:_currentField];
    }else{
        //TDLOG(@"Change %f, %f", pos.x, pos.y);
    }
}
-(void)gotoNexField:(BOOL)isNext circle:(BOOL)isCircle
{
    int next;
    if (isNext) {
        next = _currentField +1;
    }else{
        next = _currentField - 1;
    }
    if (!isCircle) {
        if (next < 0 || next >= _numField || next >= [_listPWID count]) {
            return;
        }
    }
    if (next >= _numField && next >= [_listPWID count]) {
        _currentField = 0;
    }else if (next <0){
        _currentField = MIN(_numField-1, [_listPWID count]-1);
    }else{
        _currentField = next;
    }
    [self focusField:_currentField];
}
- (IBAction)btCenterPressed:(id)sender {
    [self gotoNexField:YES circle:YES];
    
//    int next = _currentField+1;
//    if (next < _numField && next < [_listPWID count]) {
//        _currentField = next;
//    }else{
//        _currentField = 0;
//    }
//    for (int i=0; i<_vScrollViewContent.subviews.count; i++) {
//        UIButton *v = [_vScrollViewContent.subviews objectAtIndex:i];
//        if (i==_currentField) {
//            [v setSelected:YES];
//        }else{
//            [v setSelected:NO];
//        }
//    }
//    [self focusField:_currentField];
}
- (IBAction)btNotePressed:(id)sender {
    [TDAlert showMessageWithTitle:@"Note" message:_sNote delegate:self];
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
//    UIView *v = [[[UIApplication sharedApplication] windows] lastObject];
//    [v addSubview:_vJogDial];

}
@end
