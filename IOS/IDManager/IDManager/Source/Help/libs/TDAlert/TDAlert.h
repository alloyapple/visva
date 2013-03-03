//
//  TDAlert.h
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface TDAlertTextField:UIAlertView
@property(nonatomic, retain)UITextField *textField;
@end
@interface UIAlertView(TDDissmiss)
-(void)dismiss;
@end

@interface TDAlert : NSObject
+ (UIAlertView *)showLoadingMessageWithTitle:(NSString *)title
                                    delegate:(id)delegate;
+ (void)showMessageWithTitle:(NSString *)title
                          message:(NSString*)message
                         delegate:(id)delegate;
+(void)showMessageWithTitle:(NSString *)title message:(NSString *)message delegate:(id)delegate otherButton:(NSString*)other tag:(int)tag;
+(void)showMessageWithTitle:(NSString *)title message:(NSString *)message delegate:(id)delegate cancelButton:(NSString*)str other:(NSString *)other tag:(int)tag;

+(TDAlertTextField *)showTextFieldAlert:(NSString*)title delegate:(id)delegate otherButton:(NSString*)other tag:(int)tag;
@end

@protocol TDPickerNumberDelegate;
@interface TDPickerNumber : UIView<UIPickerViewDelegate, UIPickerViewDataSource>{
    
}
@property(nonatomic, assign)int minValue;
@property(nonatomic, assign)int maxValue;
@property(nonatomic, assign)int step;
@property(nonatomic, assign)int currentValue;
@property(nonatomic, assign)id<TDPickerNumberDelegate> delegate;
-(id)initWithFrom:(int)minValue to:(int)maxValue step:(int)step delegate:(id<TDPickerNumberDelegate>)delegate;
-(void)show;
+(TDPickerNumber*)showPickerFrom:(id)minValue to:(int)maxValue delegate:(id)delegate;
@end

@protocol TDPickerNumberDelegate <NSObject>
-(void)numberPickerdissmiss:(TDPickerNumber*)picker;
@end