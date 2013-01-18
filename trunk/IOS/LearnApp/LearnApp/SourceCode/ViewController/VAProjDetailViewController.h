//
//  VAProjDetailViewController.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDTextView.h"

@interface VAProjDetailViewController : UIViewController
@property (retain, nonatomic) IBOutlet UILabel *processLabel;
@property (retain, nonatomic) IBOutlet UITextField *processStartPointTextView;
@property (retain, nonatomic) IBOutlet UITextField *processEndPointTextView;



@end
