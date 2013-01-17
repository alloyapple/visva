//
//  TDTextViewPlaceHolder.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TDTextView : UITextView{
    BOOL _shouldDrawPlaceholder;
}
/*
 * place holder text, will display when notext in textview
 */
@property (nonatomic, retain) NSString *placeholder;
/*
 * color for place holder text, default is graycolor
 */
@property (nonatomic, retain) UIColor *placeholderTextColor;
@end
