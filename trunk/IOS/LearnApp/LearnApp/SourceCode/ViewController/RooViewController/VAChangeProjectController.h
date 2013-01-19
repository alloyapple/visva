//
//  VAChangeProjectController.h
//  LearnApp
//
//  Created by Tam Nguyen on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VAChangeProjectController : UIViewController <UIPickerViewDataSource, UIPickerViewDelegate>
@property (retain, nonatomic) UIPickerView *pickerView;
@property (retain, nonatomic) NSMutableArray *listProject;
@property (retain, nonatomic) NSMutableArray *listProcess;

@end
