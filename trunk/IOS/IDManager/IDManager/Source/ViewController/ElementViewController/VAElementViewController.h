//
//  ElementViewController.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAElementId.h"
#import "TDWebViewController.h"
@interface VAPasswordIDView : UIView
@property(nonatomic, retain)IBOutlet UITextField *vPassword;
@property(nonatomic, retain)IBOutlet UITextField *vId;
@end

@protocol VAElementViewDelegate;
@interface VAElementViewController : UIViewController<TDWebViewDelegate, UITextFieldDelegate>
@property(nonatomic, assign)IBOutlet id<VAElementViewDelegate> elementDelegate;
@property(nonatomic, retain)VAElementId *currentElement;

@end

@protocol VAElementViewDelegate
-(void)elementViewDidSave:(VAElementViewController*)controller;
-(void)elementViewDidCancel:(VAElementViewController *)controller;

@end