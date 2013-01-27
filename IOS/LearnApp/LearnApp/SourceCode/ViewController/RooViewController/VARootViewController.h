//
//  VARootViewController.h
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VARootViewController : UIViewController <UIPopoverControllerDelegate>
- (IBAction)changeProjectProcessButtonPressed:(id)sender;
- (IBAction)versionButtonPressed:(id)sender;

-(void)selectTaskTimeTab;
@property (retain, nonatomic) UIPopoverController *popoverController;

@end
