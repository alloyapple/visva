//
//  TDAlert.m
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDAlert.h"
#import "TDCommonLibs.h"
@implementation TDAlertTextField
-(void)dealloc{
    [_textField release];
    [super dealloc];
}
@end
@implementation UIAlertView(TDDissmiss)

-(void)dismiss{
    [self dismissWithClickedButtonIndex:0 animated:YES];
}

@end
@implementation TDAlert
+ (UIAlertView *)showLoadingMessageWithTitle:(NSString *)title
                                    delegate:(id)delegate {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                    message:@""
                                                   delegate:delegate
                                          cancelButtonTitle:nil
                                          otherButtonTitles:nil];
    UIActivityIndicatorView *progress=
    [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(125, 50, 30, 30)];
    progress.activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhiteLarge;
    [alert addSubview:progress];
    [progress startAnimating];
    [alert show];
    [alert release];
    return alert;
}

+ (void)showMessageWithTitle:(NSString *)title
                          message:(NSString*)message
                         delegate:(id)delegate {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                    message:message
                                                   delegate:delegate
                                          cancelButtonTitle:TDLocStrOne(@"OK")
                                          otherButtonTitles:nil];
    [alert show];
    [alert release];
}
+(void)showMessageWithTitle:(NSString *)title message:(NSString *)message delegate:(id)delegate otherButton:(NSString *)other tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                    message:message
                                                   delegate:delegate
                                          cancelButtonTitle:TDLocStrOne(@"OK")
                                          otherButtonTitles:other, nil];
    alert.tag = tag;
    [alert show];
    [alert release];
}

+(void)showMessageWithTitle:(NSString *)title message:(NSString *)message delegate:(id)delegate cancelButton:(NSString*)str other:(NSString *)other tag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                    message:message
                                                   delegate:delegate
                                          cancelButtonTitle:str
                                          otherButtonTitles:other, nil];
    alert.tag = tag;
    [alert show];
    [alert release];
}

+(TDAlertTextField *)showTextFieldAlert:(NSString*)title delegate:(id)delegate otherButton:(NSString*)other tag:(int)tag{
    TDAlertTextField *alert = [[[TDAlertTextField alloc] initWithTitle:title
                                                     message:@"\n\n"
                                                    delegate:delegate
                                           cancelButtonTitle:TDLocStrOne(@"OK")
                                           otherButtonTitles:other, nil] autorelease];
    CGRect rect = {12, 60, 260, 25};
    UITextField *dirField = [[[UITextField alloc] initWithFrame:rect] autorelease];
    dirField.backgroundColor = [UIColor whiteColor];
    [dirField becomeFirstResponder];
    [alert addSubview:dirField];
    alert.tag = tag;
    alert.textField = dirField;
    [alert show];
    return alert;
}
@end
@interface TDPickerNumber()
@property(nonatomic, retain)UIPickerView *picker;
@end
@implementation TDPickerNumber
-(id)initWithFrom:(int)minValue to:(int)maxValue step:(int)step delegate:(id<TDPickerNumberDelegate>)delegate{
    if (self = [super init]) {
        _minValue = minValue;
        _maxValue = maxValue;
        _step = step;
        if (_step == 0) {
            _step = 1;
        }
        _delegate = delegate;
        [self initPicker];
    }
    return self;
}
-(void)initPicker{
    UIWindow *window = [[UIApplication sharedApplication] keyWindow];
    self.frame = window.frame;
    self.picker = [[[UIPickerView alloc] init] autorelease];
    _picker.delegate = self;
    _picker.dataSource = self;
    _picker.frame = CGRectMake(0, self.frame.size.height - _picker.frame.size.height, self.frame.size.width, _picker.frame.size.height);
    [_picker setShowsSelectionIndicator:TRUE];
    [self addSubview:_picker];
    
    UIToolbar *toolBar = [[[UIToolbar alloc] init] autorelease];
    toolBar.barStyle = UIBarStyleBlackTranslucent;
    toolBar.frame = CGRectMake(0, _picker.frame.origin.y - 40, 320, 40);
    
    UIBarButtonItem *item1 = [[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil] autorelease];
    
    UIBarButtonItem *item2 = [[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(donePressed:)] autorelease];
    [toolBar setItems:[NSArray arrayWithObjects:item1, item2, nil]];
    [self addSubview:toolBar];
    
    self.backgroundColor = [UIColor clearColor];
}

-(void)hide{
    CGPoint newCenter = CGPointMake(self.center.x, self.frame.size.height);
    [UIView animateWithDuration:0.5 animations:^{
        self.center = newCenter;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}
-(void)donePressed:(UIBarButtonItem*)sender{
    [_delegate numberPickerdissmiss:self];
    [self hide];
}
-(void)show{
    _currentValue = MIN(_currentValue, _maxValue);
    _currentValue = MAX(_currentValue, _minValue);
    
    int startIndex = (_currentValue - _minValue)/_step;
    _currentValue = _minValue + _step *startIndex;
    [_picker selectRow:startIndex inComponent:0 animated:NO];
    [self showView];
}
-(void)showView{
    UIWindow *window = [[UIApplication sharedApplication] keyWindow];
    CGRect newFrame = CGRectMake(0, 0, window.frame.size.width, window.frame.size.height);
    self.frame = CGRectMake(0, newFrame.size.height, newFrame.size.width, newFrame.size.height);
    [window addSubview:self];
    [UIView animateWithDuration:0.5 animations:^{
        self.frame = newFrame;
    } completion:^(BOOL finished) {
        
    }];
}

#pragma mark - Picker
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return (_maxValue - _minValue)/_step;
}
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [NSString stringWithFormat:@"%d", _minValue + _step*row];
}
+(TDPickerNumber*)showPickerFrom:(id)minValue to:(int)maxValue delegate:(id)delegate{
    TDPickerNumber *picker = [[TDPickerNumber alloc] initWithFrom:minValue to:maxValue step:1 delegate:delegate];
    [picker show];
    [picker release];
    return picker;
}
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    _currentValue = _minValue + _step*row;
    
}
@end
